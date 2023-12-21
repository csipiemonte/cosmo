/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.integration.messaging;

import it.csi.cosmo.cosmo.dto.messaging.ParentJMSMessage;

/**
 *
 */
public interface MessageHandler<T extends ParentJMSMessage> {

  boolean canHandle(Class<T> clazz);

  void onMessage(T message);

}
