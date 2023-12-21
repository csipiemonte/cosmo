/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_credenziali_autenticazione_fruitore database table.
 * 
 */
@Entity
@Table(name = "cosmo_t_credenziali_autenticazione_fruitore")
@NamedQuery(name = "CosmoTCredenzialiAutenticazioneFruitore.findAll",
    query = "SELECT c FROM CosmoTCredenzialiAutenticazioneFruitore c")
public class CosmoTCredenzialiAutenticazioneFruitore extends CosmoTEntity
    implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CREDENZIALI_AUTENTICAZIONE_FRUITORE_ID_GENERATOR",
      sequenceName = "COSMO_T_CREDENZIALI_AUTENTICAZIONE_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_CREDENZIALI_AUTENTICAZIONE_FRUITORE_ID_GENERATOR")
  private Long id;

  @Column(name = "client_id")
  private String clientId;

  @Column(name = "client_secret")
  private String clientSecret;

  private String password;

  private String username;

  // bi-directional many-to-one association to CosmoTSchemaAutenticazioneFruitore
  @ManyToOne
  @JoinColumn(name = "id_schema")
  private CosmoTSchemaAutenticazioneFruitore schemaAutenticazione;

  public CosmoTCredenzialiAutenticazioneFruitore() {
    // NOP
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public CosmoTSchemaAutenticazioneFruitore getSchemaAutenticazione() {
    return schemaAutenticazione;
  }

  public void setSchemaAutenticazione(CosmoTSchemaAutenticazioneFruitore schemaAutenticazione) {
    this.schemaAutenticazione = schemaAutenticazione;
  }

  @Override
  public String toString() {
    return "CosmoTCredenzialiAutenticazioneFruitore [id=" + id + ", clientId=" + clientId
        + ", username=" + username + "]";
  }
}
