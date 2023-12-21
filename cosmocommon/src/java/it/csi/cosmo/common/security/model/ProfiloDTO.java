/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public class ProfiloDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Long id;

  private String codice;

  private String descrizione;

  private List<UseCaseDTO> useCases;

  public ProfiloDTO() {
    // public
  }

  private ProfiloDTO(Builder builder) {
    this.id = builder.id;
    this.codice = builder.codice;
    this.descrizione = builder.descrizione;
    this.useCases = builder.useCases;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public List<UseCaseDTO> getUseCases() {
    return useCases;
  }

  public void setUseCases(List<UseCaseDTO> useCases) {
    this.useCases = useCases;
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ProfiloDTO other = (ProfiloDTO) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProfiloDTO [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione + "]";
  }

  /**
   * Creates builder to build {@link ProfiloDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ProfiloDTO}.
   */
  public static final class Builder {
    private Long id;
    private String codice;
    private String descrizione;
    private List<UseCaseDTO> useCases = Collections.emptyList();

    private Builder() {}

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withUseCases(List<UseCaseDTO> useCases) {
      this.useCases = useCases;
      return this;
    }

    public ProfiloDTO build() {
      return new ProfiloDTO(this);
    }
  }

}
