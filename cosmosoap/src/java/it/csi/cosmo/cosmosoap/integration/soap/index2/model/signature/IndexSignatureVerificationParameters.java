/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

public class IndexSignatureVerificationParameters {

  private Boolean verifyCertificateList;

  private IndexSignatureVerificationParameters(Builder builder) {
    this.verifyCertificateList = builder.verifyCertificateList;
  }

  public IndexSignatureVerificationParameters() {}

  public Boolean isVerifyCertificateList() {
    return verifyCertificateList;
  }

  /**
   * Creates builder to build {@link IndexSignatureVerificationParameters}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IndexSignatureVerificationParameters}.
   */
  public static final class Builder {
    private boolean verifyCertificateList;

    private Builder() {}

    public Builder withVerifyCertificateList(Boolean verifyCertificateList) {
      this.verifyCertificateList = verifyCertificateList;
      return this;
    }

    public IndexSignatureVerificationParameters build() {
      return new IndexSignatureVerificationParameters(this);
    }
  }

}
