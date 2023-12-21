/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import java.util.List;
import java.util.concurrent.Future;
import it.csi.cosmo.common.mail.model.CosmoMailAttachment;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;

public interface MailService {

  Future<CosmoMailDeliveryResult> inviaMailAssistenza(String subject, String text);

  Future<CosmoMailDeliveryResult> inviaMail(List<String> to, String from, String fromName,
      List<String> cc, List<String> bcc, String subject, String text,
      List<CosmoMailAttachment> attachments);

}
