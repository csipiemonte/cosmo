/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_form_logico database table.
 *
 */
@Entity
@Table(name="cosmo_t_form_logico")
@NamedQuery(name="CosmoTFormLogico.findAll", query="SELECT c FROM CosmoTFormLogico c")
public class CosmoTFormLogico extends CosmoTEntity  implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "COSMO_T_FORM_LOGICO_ID_GENERATOR")
  @SequenceGenerator(name = "COSMO_T_FORM_LOGICO_ID_GENERATOR",
  sequenceName = "COSMO_T_FORM_LOGICO_ID_SEQ", allocationSize = 1)
  private Long id;

  private String codice;

  private String descrizione;

  private Boolean wizard;

  //bi-directional many-to-one association to CosmoRFormLogicoIstanzaFunzionalita
  @OneToMany(mappedBy="cosmoTFormLogico")
  @OrderBy("ordine asc")
  private List<CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas;

  @Column(name = "eseguibile_massivamente", nullable = true)
  private Boolean eseguibileMassivamente;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente")
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoTAttivita
  @OneToMany(mappedBy = "formLogico")
  private List<CosmoTAttivita> cosmoTAttivitas;

  public CosmoTFormLogico() {
    // NOP
  }

  public Boolean getEseguibileMassivamente() {
    return eseguibileMassivamente;
  }

  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }
  public List<CosmoRFormLogicoIstanzaFunzionalita> getCosmoRFormLogicoIstanzaFunzionalitas() {
    return this.cosmoRFormLogicoIstanzaFunzionalitas;
  }

  public void setCosmoRFormLogicoIstanzaFunzionalitas(List<CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas) {
    this.cosmoRFormLogicoIstanzaFunzionalitas = cosmoRFormLogicoIstanzaFunzionalitas;
  }

  public CosmoRFormLogicoIstanzaFunzionalita addCosmoRFormLogicoIstanzaFunzionalita(CosmoRFormLogicoIstanzaFunzionalita cosmoRFormLogicoIstanzaFunzionalita) {
    getCosmoRFormLogicoIstanzaFunzionalitas().add(cosmoRFormLogicoIstanzaFunzionalita);
    cosmoRFormLogicoIstanzaFunzionalita.setCosmoTFormLogico(this);

    return cosmoRFormLogicoIstanzaFunzionalita;
  }

  public CosmoRFormLogicoIstanzaFunzionalita removeCosmoRFormLogicoIstanzaFunzionalita(CosmoRFormLogicoIstanzaFunzionalita cosmoRFormLogicoIstanzaFunzionalita) {
    getCosmoRFormLogicoIstanzaFunzionalitas().remove(cosmoRFormLogicoIstanzaFunzionalita);
    cosmoRFormLogicoIstanzaFunzionalita.setCosmoTFormLogico(null);

    return cosmoRFormLogicoIstanzaFunzionalita;
  }


  public List<CosmoTAttivita> getCosmoTAttivitas() {
    return this.cosmoTAttivitas;
  }

  public void setCosmoTAttivitas(List<CosmoTAttivita> cosmoTAttivitas) {
    this.cosmoTAttivitas = cosmoTAttivitas;
  }

  public CosmoTAttivita addCosmoTAttivita(CosmoTAttivita cosmoTAttivita) {
    getCosmoTAttivitas().add(cosmoTAttivita);
    cosmoTAttivita.setFormLogico(this);

    return cosmoTAttivita;
  }

  public CosmoTAttivita removeCosmoTAttivita(CosmoTAttivita cosmoTAttivita) {
    getCosmoTAttivitas().remove(cosmoTAttivita);
    cosmoTAttivita.setFormLogico(null);

    return cosmoTAttivita;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }


  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, id, cosmoTEnte);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoTFormLogico other = (CosmoTFormLogico) obj;
    return Objects.equals(codice, other.codice) && Objects.equals(descrizione, other.descrizione)
        && Objects.equals(id, other.id) && Objects.equals(cosmoTEnte, other.cosmoTEnte);
  }

  @Override
  public String toString() {
    return "CosmoTFormLogico [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione
        + ", cosmoTEnte=" + cosmoTEnte + "]";
  }

  public Boolean getWizard() {
    return wizard;
  }

  public void setWizard(Boolean wizard) {
    this.wizard = wizard;
  }


  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

}
