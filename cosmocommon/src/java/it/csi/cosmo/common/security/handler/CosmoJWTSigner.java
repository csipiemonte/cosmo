/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.handler;

import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;


public class CosmoJWTSigner {

  private Logger logger;

  private String secret;

  private String algorithm;

  public CosmoJWTSigner(String secret, String algorithm, String loggingPrefix) {
    logger = Logger.getLogger(
        (loggingPrefix != null ? loggingPrefix : Constants.PRODUCT) + ".security.CosmoJWTSigner");

    this.secret = secret;
    this.algorithm = algorithm;
  }

  public String computeSignature(String encodedHeader, String encodedPayload) {
    if (StringUtils.isBlank(encodedHeader)) {
      throw new BadRequestException("Header del token JWT mancante");
    }
    if (StringUtils.isBlank(encodedPayload)) {
      throw new BadRequestException("Payload del token JWT mancante");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("signing JWT token with algorithm " + algorithm);
    }

    try {

      String result = CosmoJWTHelper
          .encrypt(encodedHeader + "." + encodedPayload + "." + secret, algorithm);

      if (logger.isTraceEnabled()) {
        logger.trace("computed JWT token signature [" + encodedHeader + ", " + encodedPayload
            + "] -> [" + result + "]");
      }

      return result;
    } catch (NoSuchAlgorithmException e) {
      throw new InternalServerException("Errore nella codifica del token in " + algorithm, e);
    }
  }

}
