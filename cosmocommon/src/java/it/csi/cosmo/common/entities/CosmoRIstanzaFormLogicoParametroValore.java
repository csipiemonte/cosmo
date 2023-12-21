/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The persistent class for the cosmo_r_istanza_form_logico_parametro_valore database table.
 *
 */
@Entity
@Table(name="cosmo_r_istanza_form_logico_parametro_valore")
@NamedQuery(name="CosmoRIstanzaFormLogicoParametroValore.findAll", query="SELECT c FROM CosmoRIstanzaFormLogicoParametroValore c")
public class CosmoRIstanzaFormLogicoParametroValore extends CosmoREntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRIstanzaFormLogicoParametroValorePK id;

  @Column(name="valore_parametro")
  private String valoreParametro;

  //bi-directional many-to-one association to CosmoDChiaveParametroFunzionalitaFormLogico
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "codice_chiave_parametro", nullable = false, insertable = false,
      updatable = false)
  private CosmoDChiaveParametroFunzionalitaFormLogico cosmoDChiaveParametroFunzionalitaFormLogico;

  //bi-directional many-to-one association to CosmoTIstanzaFunzionalitaFormLogico
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "id_istanza", nullable = false, insertable = false, updatable = false)
  private CosmoTIstanzaFunzionalitaFormLogico cosmoTIstanzaFunzionalitaFormLogico;

  public CosmoRIstanzaFormLogicoParametroValore() {
    // NOOP
  }

  public CosmoRIstanzaFormLogicoParametroValorePK getId() {
    return this.id;
  }

  public void setId(CosmoRIstanzaFormLogicoParametroValorePK id) {
    this.id = id;
  }

  @Override
  public Timestamp getDtFineVal() {
    return this.dtFineVal;
  }

  @Override
  public void setDtFineVal(Timestamp dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  @Override
  public Timestamp getDtInizioVal() {
    return this.dtInizioVal;
  }

  @Override
  public void setDtInizioVal(Timestamp dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

  public String getValoreParametro() {
    return this.valoreParametro;
  }

  public void setValoreParametro(String valoreParametro) {
    this.valoreParametro = valoreParametro;
  }

  public CosmoDChiaveParametroFunzionalitaFormLogico getCosmoDChiaveParametroFunzionalitaFormLogico() {
    return this.cosmoDChiaveParametroFunzionalitaFormLogico;
  }

  public void setCosmoDChiaveParametroFunzionalitaFormLogico(CosmoDChiaveParametroFunzionalitaFormLogico cosmoDChiaveParametroFunzionalitaFormLogico) {
    this.cosmoDChiaveParametroFunzionalitaFormLogico = cosmoDChiaveParametroFunzionalitaFormLogico;
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
    result = prime * result + ((valoreParametro == null) ? 0 : valoreParametro.hashCode());
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
    CosmoRIstanzaFormLogicoParametroValore other = (CosmoRIstanzaFormLogicoParametroValore) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (valoreParametro == null) {
      if (other.valoreParametro != null)
        return false;
    } else if (!valoreParametro.equals(other.valoreParametro))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRIstanzaFormLogicoParametroValore [id=" + id + ", valoreParametro="
        + valoreParametro + "]";
  }

}
