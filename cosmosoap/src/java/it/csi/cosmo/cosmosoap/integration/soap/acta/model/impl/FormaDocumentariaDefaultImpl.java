/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.util.Date;
import javax.annotation.Generated;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FormaDocumentaria;


/**
 *
 */
public class FormaDocumentariaDefaultImpl extends FormaDocumentaria {


  @Generated("SparkTools")
  private FormaDocumentariaDefaultImpl(Builder builder) {
    idFormaDocumentaria = builder.idFormaDocumentaria;
    descrizione = builder.descrizione;
    firmato = builder.firmato;
    dataFineValidita = builder.dataFineValidita;
    daConservareSostitutiva = builder.daConservareSostitutiva;
    originale = builder.originale;
    unico = builder.unico;
    idVitalRecordCode = builder.idVitalRecordCode;
  }

  public FormaDocumentariaDefaultImpl() {
    // NOP
  }

  /**
   * Creates builder to build {@link FormaDocumentariaDefaultImpl}.
   * 
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link FormaDocumentariaDefaultImpl}.
   */
  @Generated("SparkTools")
  public static final class Builder {

    private String idFormaDocumentaria;
    private String descrizione;
    private Boolean firmato;
    private Date dataFineValidita;
    private boolean daConservareSostitutiva;
    private boolean originale;
    private boolean unico;
    private String idVitalRecordCode;

    private Builder() {}

    public Builder withIdFormaDocumentaria(String idFormaDocumentaria) {
      this.idFormaDocumentaria = idFormaDocumentaria;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withFirmato(Boolean firmato) {
      this.firmato = firmato;
      return this;
    }

    public Builder withDataFineValidita(Date dataFineValidita) {
      this.dataFineValidita = dataFineValidita;
      return this;
    }

    public Builder withDaConservareSostitutiva(Boolean daConservareSostitutiva) {
      this.daConservareSostitutiva = daConservareSostitutiva;
      return this;
    }

    public Builder withOriginale(Boolean originale) {
      this.originale = originale;
      return this;
    }

    public Builder withIdVitalRecordCode(String idVitalRecordCode) {
      this.idVitalRecordCode = idVitalRecordCode;
      return this;
    }


    public FormaDocumentariaDefaultImpl build() {
      return new FormaDocumentariaDefaultImpl(this);
    }
  }

}
