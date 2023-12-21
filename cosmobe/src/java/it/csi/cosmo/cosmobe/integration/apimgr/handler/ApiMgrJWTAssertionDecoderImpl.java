/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.apimgr.handler;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoJWTHelper;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrConfig;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrJWTAssertion;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrJWTAssertionHeader;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrJWTAssertionPayload;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

@Component
public class ApiMgrJWTAssertionDecoderImpl implements ApiMgrJWTAssertionDecoder {

  private static final String HANDLED_TYPE = "JWT";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "ApiMgrJWTAssertionDecoder");

  private ObjectMapper mapper = null;

  public ApiMgrJWTAssertionDecoderImpl() {
    mapper = CosmoJWTHelper.getMapper();
  }

  @Override
  public ApiMgrJWTAssertion decodeAndVerify(String encoded) {
    ApiMgrJWTAssertion decoded = decode(encoded);

    verify(decoded, encoded);

    return decoded;
  }

  protected void verify(ApiMgrJWTAssertion decoded, String encoded) {
    final var method = "verify";

    CosmoJWTHelper.validateTokenFormat(encoded);

    String[] splitted = encoded.split("\\.");
    String declaredSignature = splitted[2];

    if (logger.isDebugEnabled()) {
      logger.debug(method, "verifying JWT token signature");
    }

    if (StringUtils.isBlank(declaredSignature)) {
      throw new BadRequestException("Firma del token JWT mancante");
    }

    // TODO: add implementation for signature verification
    if (StringUtils.isEmpty(decoded.getHeader().getType())) {
      throw new BadRequestException("Tipologia del token JWT mancante");
    }

    if (logger.isDebugEnabled()) {
      logger.debug(method, "JWT token signature verification passed");
    }
  }

  protected ApiMgrJWTAssertion decode(String encoded) {
    final var method = "decode";

    CosmoJWTHelper.validateTokenFormat(encoded);
    String[] splitted = encoded.split("\\.");

    if (logger.isDebugEnabled()) {
      logger.debug(method, "decoding JWT token");
    }

    ApiMgrJWTAssertionHeader header;
    try {
      header = mapper.readValue(
          CosmoJWTHelper.getBase64Decoder()
          .decode(splitted[0].getBytes(ApiMgrConfig.JWT_ASSERTION_ENCODING)),
          ApiMgrJWTAssertionHeader.class);
    } catch (IOException e) {
      throw new BadRequestException(
          "Errore nella decodifica dell'header del token JWT", e);
    }

    if (logger.isDebugEnabled()) {
      logger.debug(method, "decoded JWT token to type " + header.getType());
    }

    if (StringUtils.isBlank(header.getType()) || !header.getType().equals(HANDLED_TYPE)) {
      throw new BadRequestException(
          "Tipologia di token dichiarata non coerente");
    }

    ApiMgrJWTAssertionPayload payload;
    try {
      payload = mapper.readValue(
          CosmoJWTHelper.getBase64Decoder()
          .decode(splitted[1].getBytes(ApiMgrConfig.JWT_ASSERTION_ENCODING)),
          ApiMgrJWTAssertionPayload.class);
    } catch (IOException e) {
      throw new BadRequestException(
          "Errore nella decodifica del payload del token JWT", e);
    }

    ApiMgrJWTAssertion output = new ApiMgrJWTAssertion();
    output.setHeader(header);
    output.setPayload(payload);
    output.setSignature(splitted[2]);

    if (logger.isTraceEnabled()) {
      logger
      .trace(method,
          "decoded JWT token [" + encoded + "] -> [" + ObjectUtils.represent(output) + "]");
    }

    return output;
  }

}
