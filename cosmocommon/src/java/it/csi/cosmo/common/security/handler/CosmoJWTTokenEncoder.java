/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.handler;

import java.time.ZonedDateTime;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.security.model.AuthenticationToken;
import it.csi.cosmo.common.security.model.AuthenticationTokenHeader;
import it.csi.cosmo.common.security.model.AuthenticationTokenPayload;
import it.csi.cosmo.common.util.ObjectUtils;


public class CosmoJWTTokenEncoder {

  private static final String PRODUCED_TYPE = "JWT";

  private Logger logger;

  private ObjectMapper mapper = null;

  private String algorithm;

  private String secret;

  private String loggingPrefix;

  public CosmoJWTTokenEncoder(String algorithm, String secret, String loggingPrefix) {
    this.loggingPrefix = loggingPrefix;
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".security.CosmoJWTTokenEncoder");
    this.secret = secret;
    this.algorithm = algorithm;
    mapper = CosmoJWTHelper.getMapper();
  }

  public <T> String buildAndSign(T payloadContent, String issuer,
      String subject) {

    AuthenticationToken<T> token = buildToken(payloadContent, issuer, subject);

    try {
      return encodeAndSign(token);
    } catch (JsonProcessingException e) {
      throw new InternalServerException("Errore nell'encoding del token",
          e);
    }
  }

  private <T> AuthenticationToken<T> buildToken(T payloadContent,
      String issuer, String subject) {

    AuthenticationToken<T> token = new AuthenticationToken<>();
    AuthenticationTokenHeader header = new AuthenticationTokenHeader();
    AuthenticationTokenPayload<T> payload = new AuthenticationTokenPayload<>();

    token.setHeader(header);
    token.setPayload(payload);

    header.setAlgorithm(algorithm);
    header.setType(PRODUCED_TYPE);

    payload.setContent(payloadContent);
    payload.setIssuedAtEpoch(ZonedDateTime.now().toEpochSecond());
    payload.setIssuer(issuer);
    payload.setSubject(subject);

    return token;
  }

  private <T> String encodeAndSign(AuthenticationToken<T> token)
      throws JsonProcessingException {

    StringBuilder builder = new StringBuilder();

    if (logger.isDebugEnabled()) {
      logger.debug("encoding JWT token of type " + token.getHeader().getType());
    }

    String encodedHeader = new String(
        CosmoJWTHelper.getBase64Encoder()
        .encode(mapper.writeValueAsString(token.getHeader())
            .getBytes(CosmoAuthenticationConfig.ENCODING)),
        CosmoAuthenticationConfig.ENCODING);

    String encodedPayload = new String(
        CosmoJWTHelper.getBase64Encoder()
        .encode(mapper.writeValueAsString(token.getPayload())
            .getBytes(CosmoAuthenticationConfig.ENCODING)),
        CosmoAuthenticationConfig.ENCODING);

    if (logger.isDebugEnabled()) {
      logger.debug("signing JWT token with algorithm " + algorithm);
    }

    String signature = new CosmoJWTSigner(secret, algorithm, loggingPrefix)
        .computeSignature(encodedHeader, encodedPayload);

    //@formatter:off
    builder
    .append(encodedHeader)
    .append(".")
    .append(encodedPayload)
    .append(".")
    .append(signature);
    //@formatter:on

    String output = builder.toString();

    if (logger.isTraceEnabled()) {
      logger.trace("encoded JWT token [" + ObjectUtils.represent(token) + "] -> [" + output + "]");
    }

    return output;
  }

}
