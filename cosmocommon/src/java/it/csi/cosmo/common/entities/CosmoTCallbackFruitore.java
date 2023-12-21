/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.converter.HashMapConverter;
import it.csi.cosmo.common.entities.converter.JsonNodeConverter;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_callback_fruitore database table.
 *
 */
@Entity
@Table(name = "cosmo_t_callback_fruitore")
@NamedQuery(name = "CosmoTCallbackFruitore.findAll",
query = "SELECT c FROM CosmoTCallbackFruitore c")
public class CosmoTCallbackFruitore extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CALLBACK_FRUITORE_ID_GENERATOR",
  sequenceName = "COSMO_T_CALLBACK_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_CALLBACK_FRUITORE_ID_GENERATOR")
  private Long id;

  @Column(nullable = true, columnDefinition = "json")
  @Convert(converter = HashMapConverter.class)
  private Map<String, Object> parametri;

  @Column(nullable = true, columnDefinition = "json")
  @Convert(converter = JsonNodeConverter.class)
  private JsonNode payload;

  @Column(nullable = true, columnDefinition = "json")
  @Convert(converter = JsonNodeConverter.class)
  private JsonNode risposta;

  // bi-directional many-to-one association to CosmoDStatoCallbackFruitore
  @ManyToOne
  @JoinColumn(name = "codice_stato")
  private CosmoDStatoCallbackFruitore stato;

  // bi-directional many-to-one association to CosmoTEndpointFruitore
  @ManyToOne
  @JoinColumn(name = "id_endpoint")
  private CosmoTEndpointFruitore endpoint;

  // bi-directional many-to-one association to CosmoTInvioCallbackFruitore
  @OneToMany(mappedBy = "callback")
  private List<CosmoTInvioCallbackFruitore> tentativiInvio;

  @Column(nullable = true, name = "codice_segnale")
  private String codiceSegnale;

  public CosmoTCallbackFruitore() {
    // NOP
  }

  public String getCodiceSegnale() {
    return codiceSegnale;
  }

  public void setCodiceSegnale(String codiceSegnale) {
    this.codiceSegnale = codiceSegnale;
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

  public Map<String, Object> getParametri() {
    return parametri;
  }

  public void setParametri(Map<String, Object> parametri) {
    this.parametri = parametri;
  }

  public CosmoDStatoCallbackFruitore getStato() {
    return stato;
  }

  public void setStato(CosmoDStatoCallbackFruitore stato) {
    this.stato = stato;
  }

  public CosmoTEndpointFruitore getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(CosmoTEndpointFruitore endpoint) {
    this.endpoint = endpoint;
  }

  public List<CosmoTInvioCallbackFruitore> getTentativiInvio() {
    return tentativiInvio;
  }

  public void setTentativiInvio(List<CosmoTInvioCallbackFruitore> tentativiInvio) {
    this.tentativiInvio = tentativiInvio;
  }

  @Override
  public String toString() {
    return "CosmoTCallbackFruitore [id=" + id + ", stato=" + stato + ", endpoint=" + endpoint + "]";
  }

  public JsonNode getPayload() {
    return payload;
  }

  public void setPayload(JsonNode payload) {
    this.payload = payload;
  }

}
