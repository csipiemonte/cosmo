/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 */

public class CosmoMailAuthenticator extends Authenticator {

  private String user;

  private String password;

  public CosmoMailAuthenticator(String user, String password) {
    this.user = user;
    this.password = password;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(user, password);
  }
}

