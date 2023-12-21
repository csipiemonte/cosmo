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
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;
import it.csi.cosmo.common.util.ObjectUtils;


/**
 * The persistent class for the cosmo_t_attivita database table.
 *
 */
@Entity
@Table(name = "cosmo_t_attivita")
@NamedQuery(name = "CosmoTAttivita.findAll", query = "SELECT c FROM CosmoTAttivita c")
public class CosmoTAttivita extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_ATTIVITA_ID_GENERATOR", allocationSize = 1,
  sequenceName = "COSMO_T_ATTIVITA_ID_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_ATTIVITA_ID_GENERATOR")
  private Long id;

  private String descrizione;

  @Column(name = "link_attivita")
  private String linkAttivita;

  @Column(name = "link_attivita_esterna")
  private String linkAttivitaEsterna;

  private String nome;

  // bi-directional many-to-one association to CosmoRAttivitaAssegnazione
  @JsonIgnoreProperties("cosmoTAttivita")
  @OneToMany(mappedBy = "cosmoTAttivita")
  private List<CosmoRAttivitaAssegnazione> cosmoRAttivitaAssegnaziones;

  // bi-directional many-to-one association to CosmoTCommento
  @OneToMany(mappedBy = "attivita")
  private List<CosmoTCommento> commenti;


  // bi-directional many-to-one association to CosmoTPratica
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  @JsonIgnoreProperties("subtasks")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent")
  private CosmoTAttivita parent;

  @JsonIgnoreProperties("parent")
  @OneToMany(mappedBy = "parent")
  private List<CosmoTAttivita> subtasks;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_form_logico")
  @Where(clause = "dt_cancellazione IS NULL")
  private CosmoTFormLogico formLogico;

  @Column(name = "form_key")
  private String formKey;

  public String getLinkAttivitaEsterna() {
    return linkAttivitaEsterna;
  }

  public void setLinkAttivitaEsterna(String linkAttivitaEsterna) {
    this.linkAttivitaEsterna = linkAttivitaEsterna;
  }

  public CosmoTFormLogico getFormLogico() {
    return formLogico;
  }

  public void setFormLogico(CosmoTFormLogico formLogico) {
    this.formLogico = formLogico;
  }

  public String getFormKey() {
    return formKey;
  }

  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  public CosmoTAttivita getParent() {
    return parent;
  }

  public String getTaskId() {
    return ObjectUtils.getIdFromLink(this.linkAttivita);
  }

  public void setParent(CosmoTAttivita parent) {
    this.parent = parent;
  }

  public List<CosmoTAttivita> getSubtasks() {
    return subtasks;
  }

  public void setSubtasks(List<CosmoTAttivita> subtasks) {
    this.subtasks = subtasks;
  }

  /**
   * @return the commenti
   */
  public List<CosmoTCommento> getCommenti() {
    return commenti;
  }


  /**
   * @param commenti the commenti to set
   */
  public void setCommenti(List<CosmoTCommento> commenti) {
    this.commenti = commenti;
  }


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
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


  public String getLinkAttivita() {
    return this.linkAttivita;
  }

  public void setLinkAttivita(String linkAttivita) {
    this.linkAttivita = linkAttivita;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }


  public List<CosmoRAttivitaAssegnazione> getCosmoRAttivitaAssegnaziones() {
    return this.cosmoRAttivitaAssegnaziones;
  }

  public void setCosmoRAttivitaAssegnaziones(
      List<CosmoRAttivitaAssegnazione> cosmoRAttivitaAssegnaziones) {
    this.cosmoRAttivitaAssegnaziones = cosmoRAttivitaAssegnaziones;
  }

  public CosmoRAttivitaAssegnazione addCosmoRAttivitaAssegnazione(
      CosmoRAttivitaAssegnazione cosmoRAttivitaAssegnazione) {
    getCosmoRAttivitaAssegnaziones().add(cosmoRAttivitaAssegnazione);
    cosmoRAttivitaAssegnazione.setCosmoTAttivita(this);

    return cosmoRAttivitaAssegnazione;
  }

  public CosmoRAttivitaAssegnazione removeCosmoRAttivitaAssegnazione(
      CosmoRAttivitaAssegnazione cosmoRAttivitaAssegnazione) {
    getCosmoRAttivitaAssegnaziones().remove(cosmoRAttivitaAssegnazione);
    cosmoRAttivitaAssegnazione.setCosmoTAttivita(null);

    return cosmoRAttivitaAssegnazione;
  }

  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }


}
