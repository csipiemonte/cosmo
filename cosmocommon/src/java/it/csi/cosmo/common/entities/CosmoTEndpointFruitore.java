/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_endpoint_fruitore database table.
 *
 */
@Entity
@Table(name = "cosmo_t_endpoint_fruitore")
@NamedQuery(name = "CosmoTEndpointFruitore.findAll",
query = "SELECT c FROM CosmoTEndpointFruitore c")
public class CosmoTEndpointFruitore extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_ENDPOINT_FRUITORE_ID_GENERATOR",
  sequenceName = "COSMO_T_ENDPOINT_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_ENDPOINT_FRUITORE_ID_GENERATOR")
  private Long id;

  // bi-directional many-to-one association to CosmoTFruitore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_fruitore")
  private CosmoTFruitore fruitore;

  @Column(name = "codice_descrittivo")
  private String codiceDescrittivo;

  @Column(name = "codice_tipo_endpoint")
  private String codiceTipoEndpoint;

  private String endpoint;

  @Column(name = "metodo_http")
  private String metodoHttp;

  // bi-directional many-to-one association to CosmoTCallbackFruitore
  @OneToMany(mappedBy = "endpoint")
  private List<CosmoTCallbackFruitore> callbacks;

  // bi-directional many-to-one association to CosmoDOperazioneFruitore
  @ManyToOne
  @JoinColumn(name = "codice_operazione_fruitore")
  private CosmoDOperazioneFruitore operazione;

  // bi-directional many-to-one association to CosmoTSchemaAutenticazioneFruitore
  @ManyToOne
  @JoinColumn(name = "id_schema_autenticazione_fruitore")
  private CosmoTSchemaAutenticazioneFruitore schemaAutenticazione;

  public CosmoTEndpointFruitore() {
    // NOP
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CosmoTFruitore getFruitore() {
    return fruitore;
  }

  public void setFruitore(CosmoTFruitore fruitore) {
    this.fruitore = fruitore;
  }

  public String getCodiceDescrittivo() {
    return this.codiceDescrittivo;
  }

  public void setCodiceDescrittivo(String codiceDescrittivo) {
    this.codiceDescrittivo = codiceDescrittivo;
  }

  public String getCodiceTipoEndpoint() {
    return codiceTipoEndpoint;
  }

  public void setCodiceTipoEndpoint(String codiceTipoEndpoint) {
    this.codiceTipoEndpoint = codiceTipoEndpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getMetodoHttp() {
    return metodoHttp;
  }

  public void setMetodoHttp(String metodoHttp) {
    this.metodoHttp = metodoHttp;
  }

  public List<CosmoTCallbackFruitore> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(List<CosmoTCallbackFruitore> callbacks) {
    this.callbacks = callbacks;
  }

  public CosmoDOperazioneFruitore getOperazione() {
    return operazione;
  }

  public void setOperazione(CosmoDOperazioneFruitore operazione) {
    this.operazione = operazione;
  }

  public CosmoTSchemaAutenticazioneFruitore getSchemaAutenticazione() {
    return schemaAutenticazione;
  }

  public void setSchemaAutenticazione(CosmoTSchemaAutenticazioneFruitore schemaAutenticazione) {
    this.schemaAutenticazione = schemaAutenticazione;
  }

  @Override
  public String toString() {
    return "CosmoTEndpointFruitore [id=" + id + ", fruitore=" + fruitore + ", codiceTipoEndpoint="
        + codiceTipoEndpoint + ", metodoHttp=" + metodoHttp + ", operazione=" + operazione + "]";
  }

}
