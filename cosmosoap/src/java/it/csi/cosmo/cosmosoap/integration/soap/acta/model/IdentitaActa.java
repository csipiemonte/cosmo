/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model;

/**
 *
 */

public class IdentitaActa {

  private String id;

  private String identificativoAOO;
  private String codiceAOO;
  private String descrizioneAOO;

  private String identificativoNodo;
  private String codiceNodo;
  private String descrizioneNodo;

  private String identificativoStruttura;
  private String codiceStruttura;
  private String descrizioneStruttura;

  private IdentitaActa(Builder builder) {
    this.id = builder.id;
    this.identificativoAOO = builder.identificativoAOO;
    this.codiceAOO = builder.codiceAOO;
    this.descrizioneAOO = builder.descrizioneAOO;
    this.identificativoNodo = builder.identificativoNodo;
    this.codiceNodo = builder.codiceNodo;
    this.descrizioneNodo = builder.descrizioneNodo;
    this.identificativoStruttura = builder.identificativoStruttura;
    this.codiceStruttura = builder.codiceStruttura;
    this.descrizioneStruttura = builder.descrizioneStruttura;
  }

  public IdentitaActa() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdentificativoAOO() {
    return identificativoAOO;
  }

  public void setIdentificativoAOO(String identificativoAOO) {
    this.identificativoAOO = identificativoAOO;
  }

  public String getCodiceAOO() {
    return codiceAOO;
  }

  public void setCodiceAOO(String codiceAOO) {
    this.codiceAOO = codiceAOO;
  }

  public String getDescrizioneAOO() {
    return descrizioneAOO;
  }

  public void setDescrizioneAOO(String descrizioneAOO) {
    this.descrizioneAOO = descrizioneAOO;
  }

  public String getIdentificativoNodo() {
    return identificativoNodo;
  }

  public void setIdentificativoNodo(String identificativoNodo) {
    this.identificativoNodo = identificativoNodo;
  }

  public String getCodiceNodo() {
    return codiceNodo;
  }

  public void setCodiceNodo(String codiceNodo) {
    this.codiceNodo = codiceNodo;
  }

  public String getDescrizioneNodo() {
    return descrizioneNodo;
  }

  public void setDescrizioneNodo(String descrizioneNodo) {
    this.descrizioneNodo = descrizioneNodo;
  }

  public String getIdentificativoStruttura() {
    return identificativoStruttura;
  }

  public void setIdentificativoStruttura(String identificativoStruttura) {
    this.identificativoStruttura = identificativoStruttura;
  }

  public String getCodiceStruttura() {
    return codiceStruttura;
  }

  public void setCodiceStruttura(String codiceStruttura) {
    this.codiceStruttura = codiceStruttura;
  }

  public String getDescrizioneStruttura() {
    return descrizioneStruttura;
  }

  public void setDescrizioneStruttura(String descrizioneStruttura) {
    this.descrizioneStruttura = descrizioneStruttura;
  }

  /**
   * Creates builder to build {@link IdentitaActa}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IdentitaActa}.
   */
  public static final class Builder {
    private String id;
    private String identificativoAOO;
    private String codiceAOO;
    private String descrizioneAOO;
    private String identificativoNodo;
    private String codiceNodo;
    private String descrizioneNodo;
    private String identificativoStruttura;
    private String codiceStruttura;
    private String descrizioneStruttura;

    private Builder() {}

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withIdentificativoAOO(String identificativoAOO) {
      this.identificativoAOO = identificativoAOO;
      return this;
    }

    public Builder withCodiceAOO(String codiceAOO) {
      this.codiceAOO = codiceAOO;
      return this;
    }

    public Builder withDescrizioneAOO(String descrizioneAOO) {
      this.descrizioneAOO = descrizioneAOO;
      return this;
    }

    public Builder withIdentificativoNodo(String identificativoNodo) {
      this.identificativoNodo = identificativoNodo;
      return this;
    }

    public Builder withCodiceNodo(String codiceNodo) {
      this.codiceNodo = codiceNodo;
      return this;
    }

    public Builder withDescrizioneNodo(String descrizioneNodo) {
      this.descrizioneNodo = descrizioneNodo;
      return this;
    }

    public Builder withIdentificativoStruttura(String identificativoStruttura) {
      this.identificativoStruttura = identificativoStruttura;
      return this;
    }

    public Builder withCodiceStruttura(String codiceStruttura) {
      this.codiceStruttura = codiceStruttura;
      return this;
    }

    public Builder withDescrizioneStruttura(String descrizioneStruttura) {
      this.descrizioneStruttura = descrizioneStruttura;
      return this;
    }

    public IdentitaActa build() {
      return new IdentitaActa(this);
    }
  }

}
