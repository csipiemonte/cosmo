/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;


public class CategoriaUseCaseDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1285182471L;

  private String codice;

  private String descrizione;

  public CategoriaUseCaseDTO() {
    // public
  }

  private CategoriaUseCaseDTO(Builder builder) {
    this.codice = builder.codice;
    this.descrizione = builder.descrizione;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CategoriaUseCaseDTO other = (CategoriaUseCaseDTO) obj;
    if (codice == null) {
      if (other.codice != null) {
        return false;
      }
    } else if (!codice.equals(other.codice)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CategoriaUseCaseDTO [codice=" + codice + ", descrizione=" + descrizione + "]";
  }

  /**
   * Creates builder to build {@link CategoriaUseCaseDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CategoriaUseCaseDTO}.
   */
  public static final class Builder {
    private String codice;
    private String descrizione;

    private Builder() {}

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public CategoriaUseCaseDTO build() {
      return new CategoriaUseCaseDTO(this);
    }
  }

}
