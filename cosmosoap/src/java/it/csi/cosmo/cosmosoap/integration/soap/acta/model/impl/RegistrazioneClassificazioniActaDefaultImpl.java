/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.RegistrazioneClassificazioniActa;


/**
 *
 */
public class RegistrazioneClassificazioniActaDefaultImpl extends RegistrazioneClassificazioniActa {

  private RegistrazioneClassificazioniActaDefaultImpl(Builder builder) {
    this.id = builder.id;
    this.dbKey = builder.dbKey;
    this.changeToken = builder.changeToken;
    this.dataProtocollo = builder.dataProtocollo;
    this.stato = builder.stato;
    this.objectIdRegistrazione = builder.objectIdRegistrazione;
    this.annoProtocolloMittente = builder.annoProtocolloMittente;
    this.idAooResponsabile = builder.idAooResponsabile;
    this.objectIdAooResponsabile = builder.objectIdAooResponsabile;
    this.dataProtocolloMittente = builder.dataProtocolloMittente;
    this.idAooProtocollante = builder.idAooProtocollante;
    this.objectIdAooProtocollante = builder.objectIdAooProtocollante;
    this.codiceProtocolloMittente = builder.codiceProtocolloMittente;
    this.oggetto = builder.oggetto;
    this.idRegistroProtocollo = builder.idRegistroProtocollo;
    this.codice = builder.codice;
    this.codiceAooProtocollante = builder.codiceAooProtocollante;
    this.codiceAooResponsabile = builder.codiceAooResponsabile;
    this.dbKeyTipoRegistrazione = builder.dbKeyTipoRegistrazione;
    this.objectIdClassificazione = builder.objectIdClassificazione;
    this.anno = builder.anno;
    this.flagRiservato = builder.flagRiservato;
  }

  public RegistrazioneClassificazioniActaDefaultImpl() {}

  /**
   * Creates builder to build {@link RegistrazioneClassificazioniActaDefaultImpl}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RegistrazioneClassificazioniActaDefaultImpl}.
   */
  public static final class Builder {
    private String id;
    private String dbKey;
    private LocalDateTime changeToken;
    private LocalDate dataProtocollo;
    private String stato;
    private String objectIdRegistrazione;
    private Long annoProtocolloMittente;
    private String idAooResponsabile;
    private String objectIdAooResponsabile;
    private String dataProtocolloMittente;
    private String idAooProtocollante;
    private String objectIdAooProtocollante;
    private String codiceProtocolloMittente;
    private String oggetto;
    private String idRegistroProtocollo;
    private String codice;
    private String codiceAooProtocollante;
    private String codiceAooResponsabile;
    private String dbKeyTipoRegistrazione;
    private String objectIdClassificazione;
    private Long anno;
    private Boolean flagRiservato;

    private Builder() {}

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withDbKey(String dbKey) {
      this.dbKey = dbKey;
      return this;
    }

    public Builder withChangeToken(LocalDateTime changeToken) {
      this.changeToken = changeToken;
      return this;
    }

    public Builder withDataProtocollo(LocalDate dataProtocollo) {
      this.dataProtocollo = dataProtocollo;
      return this;
    }

    public Builder withStato(String stato) {
      this.stato = stato;
      return this;
    }

    public Builder withObjectIdRegistrazione(String objectIdRegistrazione) {
      this.objectIdRegistrazione = objectIdRegistrazione;
      return this;
    }

    public Builder withAnnoProtocolloMittente(Long annoProtocolloMittente) {
      this.annoProtocolloMittente = annoProtocolloMittente;
      return this;
    }

    public Builder withIdAooResponsabile(String idAooResponsabile) {
      this.idAooResponsabile = idAooResponsabile;
      return this;
    }

    public Builder withObjectIdAooResponsabile(String objectIdAooResponsabile) {
      this.objectIdAooResponsabile = objectIdAooResponsabile;
      return this;
    }

    public Builder withDataProtocolloMittente(String dataProtocolloMittente) {
      this.dataProtocolloMittente = dataProtocolloMittente;
      return this;
    }

    public Builder withIdAooProtocollante(String idAooProtocollante) {
      this.idAooProtocollante = idAooProtocollante;
      return this;
    }

    public Builder withObjectIdAooProtocollante(String objectIdAooProtocollante) {
      this.objectIdAooProtocollante = objectIdAooProtocollante;
      return this;
    }

    public Builder withCodiceProtocolloMittente(String codiceProtocolloMittente) {
      this.codiceProtocolloMittente = codiceProtocolloMittente;
      return this;
    }

    public Builder withOggetto(String oggetto) {
      this.oggetto = oggetto;
      return this;
    }

    public Builder withIdRegistroProtocollo(String idRegistroProtocollo) {
      this.idRegistroProtocollo = idRegistroProtocollo;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withCodiceAooProtocollante(String codiceAooProtocollante) {
      this.codiceAooProtocollante = codiceAooProtocollante;
      return this;
    }

    public Builder withCodiceAooResponsabile(String codiceAooResponsabile) {
      this.codiceAooResponsabile = codiceAooResponsabile;
      return this;
    }

    public Builder withDbKeyTipoRegistrazione(String dbKeyTipoRegistrazione) {
      this.dbKeyTipoRegistrazione = dbKeyTipoRegistrazione;
      return this;
    }

    public Builder withObjectIdClassificazione(String objectIdClassificazione) {
      this.objectIdClassificazione = objectIdClassificazione;
      return this;
    }

    public Builder withAnno(Long anno) {
      this.anno = anno;
      return this;
    }

    public Builder withFlagRiservato(Boolean flagRiservato) {
      this.flagRiservato = flagRiservato;
      return this;
    }

    public RegistrazioneClassificazioniActaDefaultImpl build() {
      return new RegistrazioneClassificazioniActaDefaultImpl(this);
    }
  }


}
