/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
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
 * The persistent class for the cosmo_t_sigillo_elettronico database table.
 *
 */
@Entity
@Table(name="cosmo_t_sigillo_elettronico")
@NamedQuery(name="CosmoTSigilloElettronico.findAll", query="SELECT c FROM CosmoTSigilloElettronico c")
public class CosmoTSigilloElettronico extends CosmoTEntity implements CsiLogAuditedEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_SIGILLO_ELETTRONICO_ID_GENERATOR",
  sequenceName = "COSMO_T_SIGILLO_ELETTRONICO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_SIGILLO_ELETTRONICO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  @Column(name = "identificativo_evento")
  private String identificativoEvento;

  @Column(name = "identificativo_alias")
  private String identificativoAlias;

  private Boolean utilizzato;

  // bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoTSigilloElettronico", fetch = FetchType.LAZY)
  private List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos;

  public CosmoTSigilloElettronico() {}

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Timestamp getDtCancellazione() {
    return this.dtCancellazione;
  }

  @Override
  public void setDtCancellazione(Timestamp dtCancellazione) {
    this.dtCancellazione = dtCancellazione;
  }

  @Override
  public Timestamp getDtInserimento() {
    return this.dtInserimento;
  }

  @Override
  public void setDtInserimento(Timestamp dtInserimento) {
    this.dtInserimento = dtInserimento;
  }

  @Override
  public Timestamp getDtUltimaModifica() {
    return this.dtUltimaModifica;
  }

  @Override
  public void setDtUltimaModifica(Timestamp dtUltimaModifica) {
    this.dtUltimaModifica = dtUltimaModifica;
  }

  public String getIdentificativoEvento() {
    return this.identificativoEvento;
  }

  public void setIdentificativoEvento(String identificativoEvento) {
    this.identificativoEvento = identificativoEvento;
  }

  public String getIdentificativoAlias() {
    return this.identificativoAlias;
  }

  public void setIdentificativoAlias(String identificativoAlias) {
    this.identificativoAlias = identificativoAlias;
  }

  @Override
  public String getUtenteCancellazione() {
    return this.utenteCancellazione;
  }

  @Override
  public void setUtenteCancellazione(String utenteCancellazione) {
    this.utenteCancellazione = utenteCancellazione;
  }

  @Override
  public String getUtenteInserimento() {
    return this.utenteInserimento;
  }

  @Override
  public void setUtenteInserimento(String utenteInserimento) {
    this.utenteInserimento = utenteInserimento;
  }

  @Override
  public String getUtenteUltimaModifica() {
    return this.utenteUltimaModifica;
  }

  @Override
  public void setUtenteUltimaModifica(String utenteUltimaModifica) {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public Boolean getUtilizzato() {
    return this.utilizzato;
  }

  public void setUtilizzato(Boolean utilizzato) {
    this.utilizzato = utilizzato;
  }

  public List<CosmoRSigilloDocumento> getCosmoRSigilloDocumentos() {
    return this.cosmoRSigilloDocumentos;
  }

  public void setCosmoRSigilloDocumentos(List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos) {
    this.cosmoRSigilloDocumentos = cosmoRSigilloDocumentos;
  }

  public CosmoRSigilloDocumento addCosmoRSigilloDocumento(
      CosmoRSigilloDocumento cosmoRSigilloDocumento) {
    getCosmoRSigilloDocumentos().add(cosmoRSigilloDocumento);
    cosmoRSigilloDocumento.setCosmoTSigilloElettronico(this);

    return cosmoRSigilloDocumento;
  }

  public CosmoRSigilloDocumento removeCosmoRSigilloDocumento(
      CosmoRSigilloDocumento cosmoRSigilloDocumento) {
    getCosmoRSigilloDocumentos().remove(cosmoRSigilloDocumento);
    cosmoRSigilloDocumento.setCosmoTSigilloElettronico(null);

    return cosmoRSigilloDocumento;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoTSigilloElettronico other = (CosmoTSigilloElettronico) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTSigilloElettronico [id=" + id + ", identificativoEvento=" + identificativoEvento
        + "]";
  }

  public CosmoTPratica getCosmoTPratica() {
    return cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }


}
