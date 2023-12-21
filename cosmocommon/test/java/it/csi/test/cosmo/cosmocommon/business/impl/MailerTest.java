/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.mail.CosmoMailTransportDelegate;
import it.csi.cosmo.common.mail.DefaultCosmoMailerImpl;
import it.csi.cosmo.common.mail.model.CosmoMail;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class MailerTest extends ParentUnitTest {

  @Autowired
  private CosmoMailTransportDelegate transportDelegate;

  @Test
  public void testSendMail() throws InterruptedException, ExecutionException {

    DefaultCosmoMailerImpl client =
        DefaultCosmoMailerImpl.builder().withServer("smtp.unexisting.io").withPort(2525)
            .withEnableAuthentication(true).withEnableStartTLS(true)
            .withUsername("0caeebc5479335asd").withPassword("9e541caf59460casd")
            .withTransportProtocol("smtp").withTransportDelegate(cosmoMailTransportDelegate())
            .build();

    CosmoMail mail = CosmoMail.builder().withSubject("test subject").withText("test text")
        .withFrom("test@test.test").withTo(Arrays.asList("dest@test.test")).build();

    CosmoMailDeliveryResult result = client.send(mail).get();

    dump("send mail result", result);
  }

  public CosmoMailTransportDelegate cosmoMailTransportDelegate() {
    return (Message message) -> {
      try {
        log("SENDING MAIL VIA TRANSPORT: " + message.getSubject());
      } catch (MessagingException e) {
        log("error sending mail via transport", e);
      }
    };
  }
}
