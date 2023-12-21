/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_form_logico_istanza_funzionalita database table.
 *
 */
@Entity
@Table(name="cosmo_r_form_logico_istanza_funzionalita")
@NamedQuery(name="CosmoRFormLogicoIstanzaFunzionalita.findAll", query="SELECT c FROM CosmoRFormLogicoIstanzaFunzionalita c")
public class CosmoRFormLogicoIstanzaFunzionalita extends CosmoREntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRFormLogicoIstanzaFunzionalitaPK id;

  @Column
  private Long ordine;

  @Column(name = "eseguibile_massivamente", nullable = false)
  private Boolean eseguibileMassivamente;

  //bi-directional many-to-one association to CosmoTFormLogico
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "id_form_logico", nullable = false, insertable = false, updatable = false)
  private CosmoTFormLogico cosmoTFormLogico;

  //bi-directional many-to-one association to CosmoTIstanzaFunzionalitaFormLogico
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "id_istanza_funzionalita", nullable = false, insertable = false,
  updatable = false)
  private CosmoTIstanzaFunzionalitaFormLogico cosmoTIstanzaFunzionalitaFormLogico;

  public CosmoRFormLogicoIstanzaFunzionalita() {
    // NOOP
  }

  public CosmoRFormLogicoIstanzaFunzionalitaPK getId() {
    return this.id;
  }

  public void setId(CosmoRFormLogicoIstanzaFunzionalitaPK id) {
    this.id = id;
  }

  public CosmoTFormLogico getCosmoTFormLogico() {
    return this.cosmoTFormLogico;
  }

  public void setCosmoTFormLogico(CosmoTFormLogico cosmoTFormLogico) {
    this.cosmoTFormLogico = cosmoTFormLogico;
  }

  public CosmoTIstanzaFunzionalitaFormLogico getCosmoTIstanzaFunzionalitaFormLogico() {
    return this.cosmoTIstanzaFunzionalitaFormLogico;
  }

  public void setCosmoTIstanzaFunzionalitaFormLogico(CosmoTIstanzaFunzionalitaFormLogico cosmoTIstanzaFunzionalitaFormLogico) {
    this.cosmoTIstanzaFunzionalitaFormLogico = cosmoTIstanzaFunzionalitaFormLogico;
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
    CosmoRFormLogicoIstanzaFunzionalita other = (CosmoRFormLogicoIstanzaFunzionalita) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRFormLogicoIstanzaFunzionalita [id=" + id + "]";
  }

  public Long getOrdine() {
    return ordine;
  }

  public void setOrdine(Long ordine) {
    this.ordine = ordine;
  }

  public Boolean getEseguibileMassivamente() {
    return this.eseguibileMassivamente;
  }

  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
  }

}
