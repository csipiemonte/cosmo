/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.business.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmosoap.business.service.MailService;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentIntegrationTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class MailServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private MailService mailService;

  @Test
  public void testSendMail() throws InterruptedException, ExecutionException {

    Future<CosmoMailDeliveryResult> future =
        mailService.inviaMailAssistenza("Test mail", "Testing mail content from unit test");

    CosmoMailDeliveryResult result = future.get();

    log("RESULT: " + ObjectUtils.represent(result));
  }

}
