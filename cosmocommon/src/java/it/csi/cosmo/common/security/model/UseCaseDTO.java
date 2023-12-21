/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;


public class UseCaseDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1285182471L;

  private String codice;

  private String descrizione;

  private CategoriaUseCaseDTO categoria;

  private UseCaseDTO(Builder builder) {
    this.codice = builder.codice;
    this.descrizione = builder.descrizione;
    this.categoria = builder.categoria;
  }

  public UseCaseDTO() {
    // NOP
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

  public CategoriaUseCaseDTO getCategoria() {
    return categoria;
  }

  public void setCategoria(CategoriaUseCaseDTO categoria) {
    this.categoria = categoria;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
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
    UseCaseDTO other = (UseCaseDTO) obj;
    if (categoria == null) {
      if (other.categoria != null) {
        return false;
      }
    } else if (!categoria.equals(other.categoria)) {
      return false;
    }
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
    return "UseCaseDTO [codice=" + codice + ", descrizione=" + descrizione + ", categoria="
        + categoria + "]";
  }

  /**
   * Creates builder to build {@link UseCaseDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link UseCaseDTO}.
   */
  public static final class Builder {
    private String codice;
    private String descrizione;
    private CategoriaUseCaseDTO categoria;

    private Builder() {}

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withCategoria(CategoriaUseCaseDTO categoria) {
      this.categoria = categoria;
      return this;
    }

    public UseCaseDTO build() {
      return new UseCaseDTO(this);
    }
  }


}
