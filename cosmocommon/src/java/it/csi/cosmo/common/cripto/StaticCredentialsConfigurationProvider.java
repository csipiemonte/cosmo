/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.cripto;

import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.logger.CosmoLogger;

/**
 *
 *
 */
public class StaticCredentialsConfigurationProvider implements CryptoConfigurationProvider {

  private CosmoLogger logger;

  private String passPhrase = null;
  private String ivParameter = null;

  private StaticCredentialsConfigurationProvider(Builder builder) {
    logger = new CosmoLogger(
        StringUtils.isBlank(builder.loggingPrefix) ? Constants.PRODUCT : builder.loggingPrefix,
        "FilesystemConfigurationProvider");

    logger.info("constructor", "initializing crypto configuration provider from fixed credentials");

    this.passPhrase = builder.passPhrase;
    this.ivParameter = builder.ivParameter;
  }

  @Override
  public String getIvParameter() {
    return ivParameter;
  }

  @Override
  public String getPassphrase() {
    return passPhrase;
  }

  /**
   * Creates builder to build {@link StaticCredentialsConfigurationProvider}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link StaticCredentialsConfigurationProvider}.
   */
  public static final class Builder {

    private String loggingPrefix;
    private String passPhrase;
    private String ivParameter;

    public Builder() {
      // NOP
    }

    public Builder withPassPhrase(String passPhrase) {
      this.passPhrase = passPhrase;
      return this;
    }

    public Builder withIvParameter(String ivParameter) {
      this.ivParameter = ivParameter;
      return this;
    }

    public Builder withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

    public StaticCredentialsConfigurationProvider build() {
      return new StaticCredentialsConfigurationProvider(this);
    }
  }

}
