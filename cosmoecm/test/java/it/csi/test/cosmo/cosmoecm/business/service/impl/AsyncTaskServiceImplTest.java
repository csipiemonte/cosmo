/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertEquals;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class AsyncTaskServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @After
  public void destroyTest() throws Exception {
    ((DisposableBean) asyncTaskService).destroy();
  }

  @Test
  public void testBasic() throws InterruptedException, ExecutionException {
    SecurityUtils.getUtenteCorrente();

    var executor = asyncTaskService.getExecutor();

    LongTaskFuture<String> asyncOperation = asyncTaskService.start("mega async operation", task -> {
      log("doing things at root level");
      task.sleep(100);
      log("did things at root level");
      task.step("first step", step -> {
        log("doing things at first step");
        step.info("sleeping now");
        task.sleep(100);
        step.warn("i overslept!");
        log("did things at first step");
        return 111;
      });
      log("preparing step 2");
      task.sleep(100);
      task.step("first step", step -> {
        log("doing things at second step");
        step.debug("sleep start");
        task.sleep(100);
        step.debug("sleep end");
        log("did things at second step");
        return "step2result";
      });
      log("done with root operation");
      return "123";
    });

    dump("asyncOperation waiting for task", asyncOperation.getTask().getUuid());
    var getResult = asyncOperation.getFuture().get();
    dump("asyncOperation result", getResult);

    var recovered = executor.get(asyncOperation.getTask().getUuid());
    asyncTaskService.wait(asyncOperation.getTask().getUuid(), null);
    var recoveredResult = recovered.getResult();
    dump("recovered task", recovered);

    dump("recovered task result", recoveredResult);

    assertEquals(ObjectUtils.toJson(getResult), recoveredResult);
  }

  @Test
  public void testCancellaOperazioneAsincrona() {
    asyncTaskService.deleteOperazioneAsincrona("uuid");

  }

  @Test
  public void testWatcher() {
    asyncTaskService.watcher("uuid", 5L);
  }

}

