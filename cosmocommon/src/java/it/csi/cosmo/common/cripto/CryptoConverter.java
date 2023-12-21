/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.cripto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.Base64;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.logger.CosmoLogger;

/**
 *
 */

public class CryptoConverter {

  private static final String CRYPT_ALGORITHM = "AES";
  private static final String CRYPT_TRANSFORMATION = "AES/CBC/PKCS5Padding";

  private CosmoLogger logger;

  private CryptoConfigurationProvider configurationProvider;
  private Cipher encryptCipher = null;
  private Cipher decryptCipher = null;

  private CryptoConverter(Builder cryptoBuilder) {

    logger = new CosmoLogger(StringUtils.isBlank(cryptoBuilder.loggingPrefix) ? Constants.PRODUCT
        : cryptoBuilder.loggingPrefix, "CosmoRequestLoggingInterceptor");

    this.configurationProvider = cryptoBuilder.configurationProvider;
  }

  public String decrypt(String value) {
    String decrypted = null;

    try {
      decrypted = new String(getDecryptCipher().doFinal(Base64.decode(value.getBytes())));
    } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
      logger.error("decrypt", ErrorMessages.CRYPT_ERRORE_DURANTE_DECRIPTAZIONE, e);
    }

    return decrypted;
  }


  public String encrypt(String value) {
    String encrypted = null;

    try {
      encrypted = String.valueOf(Base64.encodeBytes(getEncryptCipher().doFinal(value.getBytes())));
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      logger.error("encrypt", ErrorMessages.CRYPT_ERRORE_DURANTE_CRIPTAZIONE, e);
    }

    return encrypted;
  }

  private Cipher getEncryptCipher() {

    if (null == encryptCipher) {
      try {
        SecretKeySpec key = new SecretKeySpec(Base64.decode(getPassphrase()), CRYPT_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.decode(getIvparameter()));
        encryptCipher = Cipher.getInstance(CRYPT_TRANSFORMATION);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
          | InvalidAlgorithmParameterException | IOException e) {
        logger.error("getEncryptCipher",
            ErrorMessages.CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_EN, e);
      }
    }

    return encryptCipher;
  }

  private Cipher getDecryptCipher() {

    if (null == decryptCipher) {
      try {
        SecretKeySpec key = new SecretKeySpec(Base64.decode(getPassphrase()), CRYPT_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.decode(getIvparameter()));
        decryptCipher = Cipher.getInstance(CRYPT_TRANSFORMATION);
        decryptCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
          | InvalidAlgorithmParameterException | IOException e) {
        logger.error("getDecryptCipher",
            ErrorMessages.CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_DEC, e);
      }
    }

    return decryptCipher;
  }

  private String getPassphrase() {
    return configurationProvider.getPassphrase();
  }

  private String getIvparameter() {
    return configurationProvider.getIvParameter();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CryptoConverter}.
   */

  public static class Builder {

    private String loggingPrefix;
    private CryptoConfigurationProvider configurationProvider;

    public Builder() {
      // NOP
    }

    public Builder withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

    public Builder withConfigurationProvider(CryptoConfigurationProvider configurationProvider) {
      this.configurationProvider = configurationProvider;
      return this;
    }

    public CryptoConverter build() {
      return new CryptoConverter(this);
    }
  }

}
