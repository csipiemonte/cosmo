/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.converter.JsonNodeConverter;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_invio_callback_fruitore database table.
 * 
 */
@Entity
@Table(name = "cosmo_t_invio_callback_fruitore")
@NamedQuery(name = "CosmoTInvioCallbackFruitore.findAll",
    query = "SELECT c FROM CosmoTInvioCallbackFruitore c")
public class CosmoTInvioCallbackFruitore extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_INVIO_CALLBACK_FRUITORE_ID_GENERATOR",
      sequenceName = "COSMO_T_INVIO_CALLBACK_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_INVIO_CALLBACK_FRUITORE_ID_GENERATOR")
  private Long id;

  @Column(name = "dettagli_esito")
  private String dettagliEsito;

  // bi-directional many-to-one association to CosmoDEsitoInvioCallbackFruitore
  @ManyToOne
  @JoinColumn(name = "codice_esito")
  private CosmoDEsitoInvioCallbackFruitore esito;

  // bi-directional many-to-one association to CosmoTCallbackFruitore
  @ManyToOne
  @JoinColumn(name = "id_callback")
  private CosmoTCallbackFruitore callback;

  @Column(nullable = true, columnDefinition = "json")
  @Convert(converter = JsonNodeConverter.class)
  private JsonNode risposta;

  public CosmoTInvioCallbackFruitore() {
    // NOP
  }

  public JsonNode getRisposta() {
    return risposta;
  }

  public void setRisposta(JsonNode risposta) {
    this.risposta = risposta;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDettagliEsito() {
    return dettagliEsito;
  }

  public void setDettagliEsito(String dettagliEsito) {
    this.dettagliEsito = dettagliEsito;
  }

  public CosmoDEsitoInvioCallbackFruitore getEsito() {
    return esito;
  }

  public void setEsito(CosmoDEsitoInvioCallbackFruitore esito) {
    this.esito = esito;
  }

  public CosmoTCallbackFruitore getCallback() {
    return callback;
  }

  public void setCallback(CosmoTCallbackFruitore callback) {
    this.callback = callback;
  }

  @Override
  public String toString() {
    return "CosmoTInvioCallbackFruitore [id=" + id + ", esito=" + esito + "]";
  }

}
