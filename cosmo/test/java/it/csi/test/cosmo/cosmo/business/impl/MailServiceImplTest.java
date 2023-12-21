/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmo.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.mail.model.MailStatus;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.business.service.MailService;
import it.csi.test.cosmo.cosmo.testbed.config.CosmoUnitTest;
import it.csi.test.cosmo.cosmo.testbed.model.ParentIntegrationTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoUnitTest.class } )
public class MailServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private MailService mailService;

  @Before
  public void before() {
    assertNotNull(mailService);
  }

  @Ignore
  @Test
  public void testSendMail() throws InterruptedException, ExecutionException {

    Future<CosmoMailDeliveryResult> future =
        mailService.inviaMailAssistenza("Test mail", "Testing mail content from unit test");

    CosmoMailDeliveryResult result = future.get();

    log("RESULT: " + ObjectUtils.represent(result));
  }

  @Ignore
  @Test
  public void testSendMailDebounce() throws InterruptedException, ExecutionException {

    String subject = "Test mail";
    String text = "Testing mail content from unit test";

    CosmoMailDeliveryResult result = mailService.inviaMailAssistenza(subject, text).get();

    log("RESULT: " + ObjectUtils.represent(result));

    assertNotNull(result);

    // resend
    result = mailService.inviaMailAssistenza(subject, text).get();
    assertEquals(result.getStatus(), MailStatus.SUPPRESSED_BY_DEBOUNCE);

    // resend again
    result = mailService.inviaMailAssistenza(subject, text).get();
    assertEquals(result.getStatus(), MailStatus.SUPPRESSED_BY_DEBOUNCE);

    // resend different
    result = mailService.inviaMailAssistenza(subject + " - 2", text).get();
    assertEquals(result.getStatus(), MailStatus.SENT);

    // resend
    result = mailService.inviaMailAssistenza(subject + " - 2", text).get();
    assertEquals(result.getStatus(), MailStatus.SUPPRESSED_BY_DEBOUNCE);

    // wait 11 sec
    Thread.sleep(11000);

    result = mailService.inviaMailAssistenza(subject, text).get();
    assertEquals(result.getStatus(), MailStatus.SENT);

    result = mailService.inviaMailAssistenza(subject + " - 2", text).get();
    assertEquals(result.getStatus(), MailStatus.SENT);

    result = mailService.inviaMailAssistenza(subject, text).get();
    assertEquals(result.getStatus(), MailStatus.SUPPRESSED_BY_DEBOUNCE);

    result = mailService.inviaMailAssistenza(subject + " - 2", text).get();
    assertEquals(result.getStatus(), MailStatus.SUPPRESSED_BY_DEBOUNCE);
  }

}
