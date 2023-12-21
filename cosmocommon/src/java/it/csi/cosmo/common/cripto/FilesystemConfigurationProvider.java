/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.cripto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;

/**
 *
 *
 */
public class FilesystemConfigurationProvider implements CryptoConfigurationProvider {

  private static final String PASS_KEY = "cosmo.passphrase";
  private static final String IVPARAMETER_KEY = "cosmo.ivparameter";

  private CosmoLogger logger;

  private String file;

  private Properties externalProperties = null;

  private String passphrase = null;
  private String ivparameter = null;

  private FilesystemConfigurationProvider(Builder builder) {
    logger = new CosmoLogger(
        StringUtils.isBlank(builder.loggingPrefix) ? Constants.PRODUCT : builder.loggingPrefix,
        "FilesystemConfigurationProvider");

    logger.info("constructor",
        "initializing crypto configuration provider from filesystem credentials file");

    this.file = builder.file;
  }

  @Override
  public String getIvParameter() {
    if (null == ivparameter) {
      externalProperties = loadExternalProperties();

      ivparameter = externalProperties.getProperty(IVPARAMETER_KEY);

      if (null == ivparameter) {
        logger.error("getIvparameter",
            ErrorMessages.CRYPT_IVPARAMETER_NON_TROVATO_NELLE_PROPERTIES_ESTERNE);
        throw new BadRequestException(
            ErrorMessages.CRYPT_IVPARAMETER_NON_TROVATO_NELLE_PROPERTIES_ESTERNE);
      }
    }

    return ivparameter;
  }

  @Override
  public String getPassphrase() {
    if (null == passphrase) {
      externalProperties = loadExternalProperties();
      passphrase = externalProperties.getProperty(PASS_KEY);
      if (null == passphrase) {
        logger.error("getPassphrase",
            ErrorMessages.CRYPT_PASSPHRASE_NON_TROVATA_NELLE_PROPERTIES_ESTERNE);
        throw new BadRequestException(
            ErrorMessages.CRYPT_PASSPHRASE_NON_TROVATA_NELLE_PROPERTIES_ESTERNE);
      }
    }

    return passphrase;
  }

  private Properties loadExternalProperties() {
    final var method = "loadExternalProperties";
    if (null == externalProperties) {

      if (null == file || !StringUtils.isNotBlank(file)) {
        logger.error(method, ErrorMessages.FILE_NON_TROVATO);
        throw new InternalServerException(ErrorMessages.FILE_NON_TROVATO);
      }

      Properties loaded = loadExternalPropertiesFromFile(file);
      if (loaded == null) {
        loaded = loadExternalPropertiesFromFileSystemResource(file);
      }
      if (loaded == null) {
        loaded = loadExternalPropertiesFromClassPathResource(file);
      }

      if (loaded == null) {
        logger.error(method, ErrorMessages.FILE_NON_TROVATO);
        throw new InternalServerException(ErrorMessages.FILE_NON_TROVATO);
      }

      externalProperties = loaded;
    }

    return externalProperties;
  }

  private Properties loadExternalPropertiesFromFile(String location) {
    final var method = "loadExternalPropertiesFromFile";

    File cfile = new File(location);
    logger.info(method, "cerco file di configurazione in file al percorso {}",
        cfile.getAbsolutePath());

    if (cfile.exists()) {
      try (FileInputStream input = new FileInputStream(cfile)) {

        Properties loaded = new Properties();
        loaded.load(input);
        return loaded;

      } catch (IOException e) {
        logger.error(method, e.getMessage(), e);
        throw new InternalServerException(e.getMessage(), e);
      }
    }

    return null;
  }

  private Properties loadExternalPropertiesFromClassPathResource(String location) {
    final var method = "loadExternalPropertiesFromClassPathResource";
    logger.info(method, "risolvo risorsa al percorso {} per cercare il file di configurazione",
        location);

    var cpResource = new ClassPathResource(location);

    if (cpResource.exists()) {
      Properties loaded = new Properties();
      try {
        loaded.load(cpResource.getInputStream());
        return loaded;
      } catch (IOException e) {
        logger.error(method, e.getMessage(), e);
        throw new InternalServerException(e.getMessage(), e);
      }
    }
    return null;
  }

  private Properties loadExternalPropertiesFromFileSystemResource(String location) {
    final var method = "loadExternalPropertiesFromFileSystemResource";
    logger.info(method, "risolvo risorsa al percorso {} per cercare il file di configurazione",
        location);

    var cpResource = new FileSystemResource(location);

    if (cpResource.exists()) {
      Properties loaded = new Properties();
      try {
        loaded.load(cpResource.getInputStream());
        return loaded;
      } catch (IOException e) {
        logger.error(method, e.getMessage(), e);
        throw new InternalServerException(e.getMessage(), e);
      }
    }
    return null;
  }

  /**
   * Creates builder to build {@link FilesystemConfigurationProvider}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link FilesystemConfigurationProvider}.
   */
  public static final class Builder {

    private String loggingPrefix;
    private String file;

    public Builder() {
      // NOP
    }

    public Builder withFile(String file) {
      this.file = file;
      return this;
    }

    public Builder withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

    public FilesystemConfigurationProvider build() {
      return new FilesystemConfigurationProvider(this);
    }
  }

}
