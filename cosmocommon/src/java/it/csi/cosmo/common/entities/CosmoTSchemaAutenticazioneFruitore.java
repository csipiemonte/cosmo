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
import org.hibernate.annotations.Where;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_schema_autenticazione_fruitore database table.
 * 
 */
@Entity
@Table(name = "cosmo_t_schema_autenticazione_fruitore")
@NamedQuery(name = "CosmoTSchemaAutenticazioneFruitore.findAll",
    query = "SELECT c FROM CosmoTSchemaAutenticazioneFruitore c")
public class CosmoTSchemaAutenticazioneFruitore extends CosmoTEntity
    implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_SCHEMA_AUTENTICAZIONE_FRUITORE_ID_GENERATOR",
      sequenceName = "COSMO_T_SCHEMA_AUTENTICAZIONE_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_SCHEMA_AUTENTICAZIONE_FRUITORE_ID_GENERATOR")
  private Long id;

  // bi-directional many-to-one association to CosmoTFruitore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_fruitore")
  private CosmoTFruitore fruitore;

  // bi-directional many-to-one association to CosmoTCredenzialiAutenticazioneFruitore
  @OneToMany(mappedBy = "schemaAutenticazione")
  @Where(clause = "dt_cancellazione is NULL")
  private List<CosmoTCredenzialiAutenticazioneFruitore> credenziali;

  // bi-directional many-to-one association to CosmoDTipoSchemaAutenticazione
  @ManyToOne
  @JoinColumn(name = "codice_tipo_schema")
  private CosmoDTipoSchemaAutenticazione tipo;

  @Column(name = "in_ingresso", nullable = true)
  private Boolean inIngresso;

  @Column(name = "endpoint_token", nullable = true)
  private String tokenEndpoint;

  @Column(name = "mappatura_richiesta_token", nullable = true)
  private String mappaturaRichiestaToken;

  @Column(name = "mappatura_output_token", nullable = true)
  private String mappaturaOutputToken;

  @Column(name = "metodo_richiesta_token", nullable = true)
  private String metodoRichiestaToken;

  @Column(name = "contenttype_richiesta_token", nullable = true)
  private String contentTypeRichiestaToken;

  @Column(name = "nome_header", nullable = true)
  private String nomeHeader;

  @Column(name = "formato_header", nullable = true)
  private String formatoHeader;

  public CosmoTSchemaAutenticazioneFruitore() {
    // NOP
  }

  public String getFormatoHeader() {
    return formatoHeader;
  }

  public void setFormatoHeader(String formatoHeader) {
    this.formatoHeader = formatoHeader;
  }

  public String getNomeHeader() {
    return nomeHeader;
  }

  public void setNomeHeader(String nomeHeader) {
    this.nomeHeader = nomeHeader;
  }

  public String getMetodoRichiestaToken() {
    return metodoRichiestaToken;
  }

  public void setMetodoRichiestaToken(String metodoRichiestaToken) {
    this.metodoRichiestaToken = metodoRichiestaToken;
  }

  public String getContentTypeRichiestaToken() {
    return contentTypeRichiestaToken;
  }

  public void setContentTypeRichiestaToken(String contentTypeRichiestaToken) {
    this.contentTypeRichiestaToken = contentTypeRichiestaToken;
  }

  public String getTokenEndpoint() {
    return tokenEndpoint;
  }

  public void setTokenEndpoint(String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  public String getMappaturaRichiestaToken() {
    return mappaturaRichiestaToken;
  }

  public void setMappaturaRichiestaToken(String mappaturaRichiestaToken) {
    this.mappaturaRichiestaToken = mappaturaRichiestaToken;
  }

  public String getMappaturaOutputToken() {
    return mappaturaOutputToken;
  }

  public void setMappaturaOutputToken(String mappaturaOutputToken) {
    this.mappaturaOutputToken = mappaturaOutputToken;
  }

  public Boolean getInIngresso() {
    return inIngresso;
  }

  public void setInIngresso(Boolean inIngresso) {
    this.inIngresso = inIngresso;
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

  public List<CosmoTCredenzialiAutenticazioneFruitore> getCredenziali() {
    return credenziali;
  }

  public void setCredenziali(List<CosmoTCredenzialiAutenticazioneFruitore> credenziali) {
    this.credenziali = credenziali;
  }

  public CosmoDTipoSchemaAutenticazione getTipo() {
    return tipo;
  }

  public void setTipo(CosmoDTipoSchemaAutenticazione tipo) {
    this.tipo = tipo;
  }

  @Override
  public String toString() {
    return "CosmoTSchemaAutenticazioneFruitore [id=" + id + ", tipo=" + tipo + "]";
  }

}
