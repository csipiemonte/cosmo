/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;


public class GruppoDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1285182471L;

  private Long id;

  private String descrizione;

  private String nome;

  private String codice;

  private GruppoDTO(Builder builder) {
    this.id = builder.id;
    this.descrizione = builder.descrizione;
    this.nome = builder.nome;
    this.codice = builder.codice;
  }

  public GruppoDTO() {
    // public
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  @Override
  public String toString() {
    return "GruppoDTO [id=" + id + ", descrizione=" + descrizione + ", nome=" + nome + ", codice="
        + codice + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GruppoDTO other = (GruppoDTO) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /**
   * Creates builder to build {@link GruppoDTO}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link GruppoDTO}.
   */
  public static final class Builder {
    private Long id;
    private String descrizione;
    private String nome;
    private String codice;

    private Builder() {}

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withNome(String nome) {
      this.nome = nome;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public GruppoDTO build() {
      return new GruppoDTO(this);
    }
  }

}
