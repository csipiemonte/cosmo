/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail;

import java.io.Closeable;
import java.util.concurrent.Future;
import it.csi.cosmo.common.mail.model.CosmoMail;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;

/**
 *
 */

public interface ICosmoMailer extends Closeable {

  Future<CosmoMailDeliveryResult> send(CosmoMail mail);

}
