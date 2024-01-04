/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.dto;

import java.io.Serializable;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;


/**
 *
 */

public class DosignPayloadDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String originalFilename;

  private byte [] originalContent;

  private String signedFilename;

  private byte [] signedContent;

  private Integer id;

  private boolean primaFirma;

  private CosmoTContenutoDocumento contenuto;

  private DosignPayloadDTO ( Builder builder ) {
    this.originalFilename = builder.originalFilename;
    this.originalContent = builder.originalContent;
    this.signedFilename = builder.signedFilename;
    this.signedContent = builder.signedContent;
    this.primaFirma = builder.primaFirma;
    this.contenuto = builder.contenuto;
    this.id = builder.id;
  }

  /**
   * @return the originalFilename
   */
  public String getOriginalFilename () {
    return originalFilename;
  }

  /**
   * @param originalFilename the originalFilename to set
   */
  public void setOriginalFilename ( String originalFilename ) {
    this.originalFilename = originalFilename;
  }

  /**
   * @return the originalContent
   */
  public byte [] getOriginalContent () {
    return originalContent;
  }

  /**
   * @param originalContent the originalContent to set
   */
  public void setOriginalContent ( byte [] originalContent ) {
    this.originalContent = originalContent;
  }

  /**
   * @return the signedFilename
   */
  public String getSignedFilename () {
    return signedFilename;
  }

  /**
   * @param signedFilename the signedFilename to set
   */
  public void setSignedFilename ( String signedFilename ) {
    this.signedFilename = signedFilename;
  }

  /**
   * @return the signedContent
   */
  public byte [] getSignedContent () {
    return signedContent;
  }

  /**
   * @param signedContent the signedContent to set
   */
  public void setSignedContent ( byte [] signedContent ) {
    this.signedContent = signedContent;
  }

  /**
   * Creates builder to build {@link DosignPayloadDTO}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder();
  }

  /**
   * Builder to build {@link DosignPayloadDTO}.
   */
  public static final class Builder {

    private String originalFilename;

    private byte[] originalContent;

    private String signedFilename;

    private byte[] signedContent;

    private Integer id;

    private boolean primaFirma;

    private CosmoTContenutoDocumento contenuto;

    private Builder() {}

    public Builder withOriginalFilename(String originalFilename) {
      this.originalFilename = originalFilename;
      return this;
    }

    public Builder withOriginalContent(byte[] originalContent) {
      this.originalContent = originalContent;
      return this;
    }

    public Builder withSignedFilename(String signedFilename) {
      this.signedFilename = signedFilename;
      return this;
    }

    public Builder withSignedContent(byte[] signedContent) {
      this.signedContent = signedContent;
      return this;
    }

    public Builder withId(Integer id) {
      this.id = id;
      return this;
    }

    public Builder withPrimaFirma(boolean primaFirma) {
      this.setPrimaFirma(primaFirma);
      return this;
    }

    public Builder withContenuto(CosmoTContenutoDocumento contenuto) {
      this.setContenuto(contenuto);
      return this;
    }


    public DosignPayloadDTO build() {
      return new DosignPayloadDTO(this);
    }

    public boolean isPrimaFirma() {
      return primaFirma;
    }

    public void setPrimaFirma(boolean primaFirma) {
      this.primaFirma = primaFirma;
    }

    public CosmoTContenutoDocumento getContenuto() {
      return contenuto;
    }

    public void setContenuto(CosmoTContenutoDocumento contenuto) {
      this.contenuto = contenuto;
    }

  }

  /**
   * @return the id
   */
  public Integer getId () {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId ( Integer id ) {
    this.id = id;
  }

  public boolean isPrimaFirma() {
    return primaFirma;
  }

  public void setPrimaFirma(boolean primaFirma) {
    this.primaFirma = primaFirma;
  }

  public CosmoTContenutoDocumento getContenuto() {
    return contenuto;
  }

  public void setContenuto(CosmoTContenutoDocumento contenuto) {
    this.contenuto = contenuto;
  }

}
