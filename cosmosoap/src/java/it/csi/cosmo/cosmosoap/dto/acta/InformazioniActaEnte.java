/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.dto.acta;

/**
 *
 */

public class InformazioniActaEnte {

  private String appKey;

  private String repositoryName;

  private InformazioniActaEnte(Builder builder) {
    this.appKey = builder.appKey;
    this.repositoryName = builder.repositoryName;
  }

  public InformazioniActaEnte() {}

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getRepositoryName() {
    return repositoryName;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  @Override
  public String toString() {
    return "InformazioniActaEnte [appKey=" + appKey + ", repositoryName=" + repositoryName + "]";
  }

  /**
   * Creates builder to build {@link InformazioniActaEnte}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link InformazioniActaEnte}.
   */
  public static final class Builder {
    private String appKey;
    private String repositoryName;

    private Builder() {}

    public Builder withAppKey(String appKey) {
      this.appKey = appKey;
      return this;
    }

    public Builder withRepositoryName(String repositoryName) {
      this.repositoryName = repositoryName;
      return this;
    }

    public InformazioniActaEnte build() {
      return new InformazioniActaEnte(this);
    }
  }

}
