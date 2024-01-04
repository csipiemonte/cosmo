/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ProtocolloActa;
import it.doqui.acta.acaris.common.EnumCommonObjectType;


/**
 *
 */
public class ProtocolloActaDefaultImpl extends ProtocolloActa {

  private ProtocolloActaDefaultImpl(Builder builder) {
    this.id = builder.id;
    this.dbKey = builder.dbKey;
    this.changeToken = builder.changeToken;
    this.objectTypeId = builder.objectTypeId;
    this.numRegistrazioneProtocollo = builder.numRegistrazioneProtocollo;
    this.dataRegistrazioneProtocollo = builder.dataRegistrazioneProtocollo;
    this.oggetto = builder.oggetto;
    this.entrata = builder.entrata;
    this.aooCheRegistra = builder.aooCheRegistra;
    this.enteCheRegistra = builder.enteCheRegistra;
    this.mittente = builder.mittente;
    this.destinatario = builder.destinatario;
    this.riservato = builder.riservato;
    this.annullato = builder.annullato;
    this.uuidRegistrazioneProtocollo = builder.uuidRegistrazioneProtocollo;
  }

  public ProtocolloActaDefaultImpl() {}

  /**
   * Creates builder to build {@link ProtocolloActaDefaultImpl}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ProtocolloActaDefaultImpl}.
   */
  public static final class Builder {
    private String id;
    private String dbKey;
    private LocalDateTime changeToken;
    private EnumCommonObjectType objectTypeId;
    private String numRegistrazioneProtocollo;
    private OffsetDateTime dataRegistrazioneProtocollo;
    private String oggetto;
    private Boolean entrata;
    private String aooCheRegistra;
    private String enteCheRegistra;
    private List<String> mittente = Collections.emptyList();
    private List<String> destinatario = Collections.emptyList();
    private Boolean riservato;
    private Boolean annullato;
    private String uuidRegistrazioneProtocollo;

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

    public Builder withObjectTypeId(EnumCommonObjectType objectTypeId) {
      this.objectTypeId = objectTypeId;
      return this;
    }

    public Builder withNumRegistrazioneProtocollo(String numRegistrazioneProtocollo) {
      this.numRegistrazioneProtocollo = numRegistrazioneProtocollo;
      return this;
    }

    public Builder withDataRegistrazioneProtocollo(OffsetDateTime dataRegistrazioneProtocollo) {
      this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
      return this;
    }

    public Builder withOggetto(String oggetto) {
      this.oggetto = oggetto;
      return this;
    }

    public Builder withEntrata(Boolean entrata) {
      this.entrata = entrata;
      return this;
    }

    public Builder withAooCheRegistra(String aooCheRegistra) {
      this.aooCheRegistra = aooCheRegistra;
      return this;
    }

    public Builder withEnteCheRegistra(String enteCheRegistra) {
      this.enteCheRegistra = enteCheRegistra;
      return this;
    }

    public Builder withMittente(List<String> mittente) {
      this.mittente = mittente;
      return this;
    }

    public Builder withDestinatario(List<String> destinatario) {
      this.destinatario = destinatario;
      return this;
    }

    public Builder withRiservato(Boolean riservato) {
      this.riservato = riservato;
      return this;
    }

    public Builder withAnnullato(Boolean annullato) {
      this.annullato = annullato;
      return this;
    }

    public Builder withUuidRegistrazioneProtocollo(String uuidRegistrazioneProtocollo) {
      this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
      return this;
    }

    public ProtocolloActaDefaultImpl build() {
      return new ProtocolloActaDefaultImpl(this);
    }
  }

}
