/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the cosmo_t_commento database table.
 *
 */
@Entity
@Table(name = "cosmo_t_commento")
@NamedQuery(name = "CosmoTCommento.findAll", query = "SELECT c FROM CosmoTCommento c")
public class CosmoTCommento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;


  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_COMMENTO_ID_GENERATOR",
  sequenceName = "COSMO_T_COMMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_COMMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "data_creazione", nullable = false)
  private Timestamp dataCreazione;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica pratica;

  // bi-directional many-to-one association to CosmoTAttivita
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_attivita")
  private CosmoTAttivita attivita;


  private String messaggio;

  @Column(name = "utente_creatore", nullable = false)
  private String utenteCreatore;


  // bi-directional many-to-one association to CosmoDTipoCommento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo")
  private CosmoDTipoCommento tipo;


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(Timestamp dataCreazione) {
    this.dataCreazione = dataCreazione;
  }



  public String getMessaggio() {
    return this.messaggio;
  }

  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
  }

  public String getUtenteCreatore() {
    return this.utenteCreatore;
  }

  public void setUtenteCreatore(String utenteCreatore) {
    this.utenteCreatore = utenteCreatore;
  }

  public CosmoDTipoCommento getTipo() {
    return this.tipo;
  }

  public void setTipo(CosmoDTipoCommento tipo) {
    this.tipo = tipo;
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public void setPratica(CosmoTPratica pratica) {
    this.pratica = pratica;
  }


  public CosmoTAttivita getAttivita() {
    return attivita;
  }

  public void setAttivita(CosmoTAttivita attivita) {
    this.attivita = attivita;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoTCommento other = (CosmoTCommento) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTCommento [" + (id != null ? "id=" + id + ", " : "")
        + (messaggio != null ? "messaggio=" + messaggio + ", " : "")
        + (utenteCreatore != null ? "utenteCreatore=" + utenteCreatore + ", " : "")
        + (dataCreazione != null ? "data=" + dataCreazione + ", " : "");
  }



}
