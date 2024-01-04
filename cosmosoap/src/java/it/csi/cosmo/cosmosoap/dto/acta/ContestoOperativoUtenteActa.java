/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.dto.acta;

import it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa;

/**
 *
 */

public class ContestoOperativoUtenteActa {

  private String codiceFiscaleUtente;

  private IdentitaActa identitaSelezionata;

  private InformazioniActaEnte ente;

  private ContestoOperativoUtenteActa(Builder builder) {
    this.codiceFiscaleUtente = builder.codiceFiscaleUtente;
    this.identitaSelezionata = builder.identitaSelezionata;
    this.ente = builder.ente;
  }

  public ContestoOperativoUtenteActa() {}

  public String getCodiceFiscaleUtente() {
    return codiceFiscaleUtente;
  }

  public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
    this.codiceFiscaleUtente = codiceFiscaleUtente;
  }

  public IdentitaActa getIdentitaSelezionata() {
    return identitaSelezionata;
  }

  public void setIdentitaSelezionata(IdentitaActa identitaSelezionata) {
    this.identitaSelezionata = identitaSelezionata;
  }

  public InformazioniActaEnte getEnte() {
    return ente;
  }

  public void setEnte(InformazioniActaEnte ente) {
    this.ente = ente;
  }

  @Override
  public String toString() {
    return "ContestoOperativoUtenteActa [codiceFiscaleUtente=" + codiceFiscaleUtente
        + ", identitaSelezionata=" + identitaSelezionata + ", ente=" + ente + "]";
  }

  /**
   * Creates builder to build {@link ContestoOperativoUtenteActa}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ContestoOperativoUtenteActa}.
   */
  public static final class Builder {
    private String codiceFiscaleUtente;
    private IdentitaActa identitaSelezionata;
    private InformazioniActaEnte ente;

    private Builder() {}

    public Builder withCodiceFiscaleUtente(String codiceFiscaleUtente) {
      this.codiceFiscaleUtente = codiceFiscaleUtente;
      return this;
    }

    public Builder withIdentitaSelezionata(IdentitaActa identitaSelezionata) {
      this.identitaSelezionata = identitaSelezionata;
      return this;
    }

    public Builder withEnte(InformazioniActaEnte ente) {
      this.ente = ente;
      return this;
    }

    public ContestoOperativoUtenteActa build() {
      return new ContestoOperativoUtenteActa(this);
    }
  }


}
