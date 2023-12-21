/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;


public class ScopeDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1252182472L;

  private String codice;

  private String descrizione;

  public ScopeDTO() {
    // NOP
  }

  private ScopeDTO ( Builder builder ) {
    codice = builder.codice;
    descrizione = builder.descrizione;
  }

  public static long getSerialversionuid () {
    return serialVersionUID;
  }

  public String getCodice () {
    return codice;
  }

  public String getDescrizione () {
    return descrizione;
  }

  /**
   * Creates builder to build {@link ScopeDTO}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder ();
  }

  /**
   * Builder to build {@link ScopeDTO}.
   */
  public static final class Builder {

    private String codice;

    private String descrizione;

    private Builder () {
    }

    public Builder withCodice ( String codice ) {
      this.codice = codice;
      return this;
    }

    public Builder withDescrizione ( String descrizione ) {
      this.descrizione = descrizione;
      return this;
    }

    public ScopeDTO build () {
      return new ScopeDTO ( this );
    }
  }

  @Override
  public String toString () {
    return "ScopeDTO [" + ( codice != null ? "codice=" + codice + ", " : "" ) + ( descrizione != null ? "descrizione=" + descrizione : "" ) + "]";
  }

}
