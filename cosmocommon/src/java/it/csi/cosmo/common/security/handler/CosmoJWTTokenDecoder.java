/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.handler;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.security.model.AuthenticationToken;
import it.csi.cosmo.common.security.model.AuthenticationTokenHeader;
import it.csi.cosmo.common.security.model.AuthenticationTokenPayload;
import it.csi.cosmo.common.security.model.StandardAuthenticationToken;
import it.csi.cosmo.common.util.ObjectUtils;


public class CosmoJWTTokenDecoder {

  private static final String HANDLED_TYPE = "JWT";

  private Logger logger;

  private ObjectMapper mapper = null;

  private String secret;

  private String loggingPrefix;

  public CosmoJWTTokenDecoder(String secret, String loggingPrefix) {
    this.loggingPrefix = loggingPrefix;
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".security.CosmoJWTTokenDecoder");
    this.secret = secret;
    mapper = CosmoJWTHelper.getMapper();
  }

  public <T> AuthenticationToken<T> decodeAndVerify(String encoded,
      Class<T> payloadClass) {
    AuthenticationToken<T> decoded = decode(encoded, payloadClass);

    verify(decoded, encoded);

    return decoded;
  }

  public StandardAuthenticationToken decode(String encoded) {
    var decoded = this.decode(encoded, Object.class);
    StandardAuthenticationToken output = new StandardAuthenticationToken();
    output.setHeader(decoded.getHeader());
    output.setPayload(decoded.getPayload());
    output.setSignature(decoded.getSignature());
    return output;
  }

  private <T> void verify(AuthenticationToken<T> decoded, String encoded) {
    CosmoJWTHelper.validateTokenFormat(encoded);

    String[] splitted = encoded.split("\\.");
    String declaredSignature = splitted[2];

    if (logger.isDebugEnabled()) {
      logger.debug("verifying JWT token signature");
    }

    if (StringUtils.isBlank(declaredSignature)) {
      throw new BadRequestException("Firma del token JWT mancante");
    }

    String effectiveSignature =
        new CosmoJWTSigner(secret, decoded.getHeader().getAlgorithm(), loggingPrefix)
        .computeSignature(splitted[0], splitted[1]);

    if (!declaredSignature.equals(effectiveSignature)) {
      throw new ForbiddenException("Firma del token JWT non valida");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("JWT token signature verification passed");
    }
  }

  @SuppressWarnings("unchecked")
  private <T> AuthenticationToken<T> decode(String encoded,
      Class<T> payloadClass) {
    CosmoJWTHelper.validateTokenFormat(encoded);
    String[] splitted = encoded.split("\\.");

    if (logger.isDebugEnabled()) {
      logger.debug("decoding JWT token");
    }

    AuthenticationTokenHeader header;
    try {
      header = mapper.readValue(
          CosmoJWTHelper.getBase64Decoder()
          .decode(splitted[0].getBytes(CosmoAuthenticationConfig.ENCODING)),
          AuthenticationTokenHeader.class);
    } catch (IOException e) {
      throw new BadRequestException(
          "Errore nella decodifica dell'header del token JWT", e);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("decoded JWT token to type " + header.getType());
    }

    if (StringUtils.isBlank(header.getType()) || !header.getType().equals(HANDLED_TYPE)) {
      throw new BadRequestException(
          "Tipologia di token dichiarata non coerente");
    }

    AuthenticationTokenPayload<T> payload;
    try {
      payload = mapper.readValue(
          CosmoJWTHelper.getBase64Decoder()
          .decode(splitted[1].getBytes(CosmoAuthenticationConfig.ENCODING)),
          AuthenticationTokenPayload.class);
    } catch (IOException e) {
      throw new BadRequestException(
          "Errore nella decodifica del payload del token JWT", e);
    }

    if (payloadClass != null) {
      // deserialize content
      T typedPayload;

      try {
        typedPayload =
            mapper.readValue(mapper.writeValueAsString(payload.getContent()), payloadClass);
      } catch (IOException e) {
        throw new BadRequestException("Errore nella decodifica del payload del token JWT", e);
      }

      payload.setContent(typedPayload);
    } else {
      payload.setContent(null);
    }

    AuthenticationToken<T> output = new AuthenticationToken<>();
    output.setHeader(header);
    output.setPayload(payload);
    output.setSignature(splitted[2]);

    if (logger.isTraceEnabled()) {
      logger
      .trace("decoded JWT token [" + encoded + "] -> [" + ObjectUtils.represent(output) + "]");
    }

    return output;
  }


}
