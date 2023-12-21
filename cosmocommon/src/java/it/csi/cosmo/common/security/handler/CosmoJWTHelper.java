/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.handler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.csi.cosmo.common.exception.BadRequestException;


public interface CosmoJWTHelper {

  static boolean isJwtFormat(String token) {
    if (StringUtils.isBlank(token)) {
      return false;
    }
    token = token.trim();
    return token.matches(
        "[a-zA-Z0-9\\+\\/\\-\\_\\=]+\\.[a-zA-Z0-9\\+\\/\\-\\__\\=]+\\.[a-zA-Z0-9\\+\\/\\-\\_\\=]+");
  }

  static void validateTokenFormat(String token) {
    if (StringUtils.isBlank(token)) {
      throw new BadRequestException("Token JWT vuoto o mancante");
    }
    token = token.trim();
    if (!token.matches(
        "[a-zA-Z0-9\\+\\/\\-\\_\\=]+\\.[a-zA-Z0-9\\+\\/\\-\\__\\=]+\\.[a-zA-Z0-9\\+\\/\\-\\_\\=]+")) {
      throw new BadRequestException("Formato del token JWT non riconosciuto");
    }
  }

  static ObjectMapper getMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }


  static Encoder getBase64Encoder() {
    return Base64.getUrlEncoder().withoutPadding();
  }

  static Decoder getBase64Decoder() {
    return Base64.getUrlDecoder();
  }

  static String encrypt(String originalString, String alg)
      throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance(alg);
    return bytesToHex(digest.digest(originalString.getBytes(CosmoAuthenticationConfig.ENCODING)));
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
