/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.concurrent.Future;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;

public interface MailService {

  Future<CosmoMailDeliveryResult> inviaMailAssistenza(String subject, String text);

  Future<CosmoMailDeliveryResult> inviaMailOtp(String otp, String mailTo);

}
