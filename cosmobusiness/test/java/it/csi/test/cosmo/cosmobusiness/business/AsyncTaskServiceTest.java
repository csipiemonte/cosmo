/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertEquals;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class AsyncTaskServiceTest extends ParentIntegrationTest {

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Test
  public void testBasic() throws Exception {
    SecurityUtils.getUtenteCorrente();

    var executor = asyncTaskService.getExecutor();

    LongTaskFuture<String> asyncOperation = executor.start("mega async operation", task -> {
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
    var recoveredResult = recovered.getResult();
    dump("recovered task", recovered);

    dump("recovered task result", recoveredResult);

    assertEquals(ObjectUtils.toJson(getResult), recoveredResult);
  }

  @Test
  public void watcher() {
    asyncTaskService.watcher("uuid", 1L);
  }

  @Test
  public void deleteOperazioneAsincrona() {
    asyncTaskService.deleteOperazioneAsincrona("uuid");
  }

  @Test
  public void getOperazioneAsincronaNotFound() {
    asyncTaskService.getOperazioneAsincrona("uuid");
  }

  @Test
  public void postOperazioneAsincrona() {
    CreaOperazioneAsincronaRequest body = new CreaOperazioneAsincronaRequest();
    body.setDataAvvio(OffsetDateTime.now());
    body.setStato(LongTaskStatus.STARTED.name());
    body.setVersione(1L);
    asyncTaskService.postOperazioneAsincrona(body );
  }

  @Test
  public void putOperazioneAsincrona() {
    OperazioneAsincrona body = new OperazioneAsincrona();
    body.setDataAvvio(OffsetDateTime.now());
    body.setStato(LongTaskStatus.STARTED.name());
    body.setVersione(1L);
    body.setDataFine(OffsetDateTime.now());
    body.setUuid("uuid");
    asyncTaskService.putOperazioneAsincrona("uuid", body);
  }
}
