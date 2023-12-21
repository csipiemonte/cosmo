/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.handler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import it.csi.cosmo.common.config.Constants;


public abstract class CosmoAuthenticationConfig {

  public static final Charset ENCODING = StandardCharsets.UTF_8;

  public static final String TRANSMISSION_HEADER_USER = Constants.HEADERS_PREFIX + "User";

  public static final String TRANSMISSION_HEADER_CLIENT = Constants.HEADERS_PREFIX + "Client";

  public static final String TRANSMISSION_HEADER_ORIGINAL_USER =
      Constants.HEADERS_PREFIX + "User-Original";

  public static final String TRANSMISSION_HEADER_ORIGINAL_IP =
      Constants.HEADERS_PREFIX + "Forwarded-For";

  public static final String HEADER_CLIENT_ATTIVO = Constants.HEADERS_PREFIX + "Client-Attivo";

  public static final String HEADER_IDENTITA_ATTIVA = Constants.HEADERS_PREFIX + "Identita-Attiva";

  private CosmoAuthenticationConfig() {
    // NOP
  }
}
