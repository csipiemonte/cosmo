/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service;

import javax.jms.MessageListener;
import it.csi.cosmo.cosmo.dto.messaging.ParentJMSMessage;

/**
 *
 */

public interface MessagingService extends MessageListener {

  boolean isEnabled();

  void sendMessage(ParentJMSMessage payload);

}
