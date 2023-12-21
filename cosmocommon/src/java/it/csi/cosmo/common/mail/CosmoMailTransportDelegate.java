/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail;

import javax.mail.Message;

/**
 *
 */

public interface CosmoMailTransportDelegate {

  void send(Message message);
}

