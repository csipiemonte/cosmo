/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service;

import java.util.concurrent.Future;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;

public interface MailService {

  Future<CosmoMailDeliveryResult> inviaNotifiche(String subject, String text, String recipient);

}
