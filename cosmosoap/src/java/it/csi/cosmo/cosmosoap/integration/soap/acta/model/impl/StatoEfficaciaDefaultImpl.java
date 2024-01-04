/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.util.Date;
import javax.annotation.Generated;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.StatoEfficacia;


/**
 *
 */
public class StatoEfficaciaDefaultImpl extends StatoEfficacia {


  @Generated("SparkTools")
  private StatoEfficaciaDefaultImpl(Builder builder) {
    idStatoDiEfficacia = builder.idStatoDiEfficacia;
    descrizione = builder.descrizione;
    dataFineValidita = builder.dataFineValidita;
    valoreDefault = builder.valoreDefault;
  }

  public StatoEfficaciaDefaultImpl() {
    // NOP
  }

  /**
   * Creates builder to build {@link StatoEfficaciaDefaultImpl}.
   * 
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link StatoEfficaciaDefaultImpl}.
   */
  @Generated("SparkTools")
  public static final class Builder {

    private String idStatoDiEfficacia;
    private String descrizione;
    private Date dataFineValidita;
    private Boolean valoreDefault;

    private Builder() {}

    public Builder withIdStatoDiEfficacia(String idStatoDiEfficacia) {
      this.idStatoDiEfficacia = idStatoDiEfficacia;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withDataFineValidita(Date dataFineValidita) {
      this.dataFineValidita = dataFineValidita;
      return this;
    }

    public Builder withValoreDefault(Boolean valoreDefault) {
      this.valoreDefault = valoreDefault;
      return this;
    }

    public StatoEfficaciaDefaultImpl build() {
      return new StatoEfficaciaDefaultImpl(this);
    }
  }

}
