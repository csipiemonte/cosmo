/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

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
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_attivita_assegnazione database table.
 *
 */
@Entity
@Table(name = "cosmo_r_funzionalita_parametro_form_logico")
@NamedQuery(name = "CosmoRFunzionalitaParametroFormLogico.findAll",
    query = "SELECT r FROM CosmoRFunzionalitaParametroFormLogico r")
public class CosmoRFunzionalitaParametroFormLogico extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_FUNZIONALITA_PARAMETRO_FORM_LOGICO_ID_GENERATOR",
      sequenceName = "COSMO_R_FUNZIONALITA_PARAMETRO_FORM_LOGICO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_R_FUNZIONALITA_PARAMETRO_FORM_LOGICO_ID_GENERATOR")
  private Long id;

  @Column(name = "obbligatorio", nullable = true)
  private Boolean obbligatorio;

  @Column(name = "codice_funzionalita", insertable = false, updatable = false, nullable = false)
  private String codiceFunzionalita;

  @Column(name = "codice_parametro", insertable = false, updatable = false, nullable = false)
  private String codiceParametro;

  // bi-directional many-to-one association to CosmoTAttivita
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_funzionalita", nullable = false)
  private CosmoDFunzionalitaFormLogico funzionalita;

  //bi-directional many-to-one association to CosmoTAttivita
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_parametro", nullable = false)
  private CosmoDChiaveParametroFunzionalitaFormLogico parametro;

  public CosmoRFunzionalitaParametroFormLogico() {
    // NOP
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getObbligatorio() {
    return obbligatorio;
  }

  public void setObbligatorio(Boolean obbligatorio) {
    this.obbligatorio = obbligatorio;
  }

  public String getCodiceFunzionalita() {
    return codiceFunzionalita;
  }

  public void setCodiceFunzionalita(String codiceFunzionalita) {
    this.codiceFunzionalita = codiceFunzionalita;
  }

  public String getCodiceParametro() {
    return codiceParametro;
  }

  public void setCodiceParametro(String codiceParametro) {
    this.codiceParametro = codiceParametro;
  }

  public CosmoDFunzionalitaFormLogico getFunzionalita() {
    return funzionalita;
  }

  public void setFunzionalita(CosmoDFunzionalitaFormLogico funzionalita) {
    this.funzionalita = funzionalita;
  }

  public CosmoDChiaveParametroFunzionalitaFormLogico getParametro() {
    return parametro;
  }

  public void setParametro(CosmoDChiaveParametroFunzionalitaFormLogico parametro) {
    this.parametro = parametro;
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
    CosmoRFunzionalitaParametroFormLogico other = (CosmoRFunzionalitaParametroFormLogico) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRFunzionalitaParametroFormLogico [id=" + id + ", obbligatorio=" + obbligatorio
        + ", codiceFunzionalita=" + codiceFunzionalita + ", codiceParametro=" + codiceParametro
        + "]";
  }
}
