/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tipo_relazione_pratica database table.
 *
 */
@Entity
@Table(name="cosmo_d_tipo_relazione_pratica")
@NamedQuery(name="CosmoDTipoRelazionePratica.findAll", query="SELECT c FROM CosmoDTipoRelazionePratica c")
public class CosmoDTipoRelazionePratica extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 10)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  @Column(name = "descrizione_inversa", length = 255)
  private String descrizioneInversa;

  //bi-directional many-to-one association to CosmoRPraticaPratica
  @OneToMany(mappedBy="cosmoDTipoRelazionePratica")
  private List<CosmoRPraticaPratica> cosmoRPraticaPraticas;

  public CosmoDTipoRelazionePratica() {
    // empty constructor
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

  public String getDescrizioneInversa() {
    return descrizioneInversa;
  }

  public void setDescrizioneInversa(String descrizioneInversa) {
    this.descrizioneInversa = descrizioneInversa;
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

  public List<CosmoRPraticaPratica> getCosmoRPraticaPraticas() {
    return this.cosmoRPraticaPraticas;
  }

  public void setCosmoRPraticaPraticas(List<CosmoRPraticaPratica> cosmoRPraticaPraticas) {
    this.cosmoRPraticaPraticas = cosmoRPraticaPraticas;
  }

  public CosmoRPraticaPratica addCosmoRPraticaPratica(CosmoRPraticaPratica cosmoRPraticaPratica) {
    getCosmoRPraticaPraticas().add(cosmoRPraticaPratica);
    cosmoRPraticaPratica.setCosmoDTipoRelazionePratica(this);

    return cosmoRPraticaPratica;
  }

  public CosmoRPraticaPratica removeCosmoRPraticaPratica(CosmoRPraticaPratica cosmoRPraticaPratica) {
    getCosmoRPraticaPraticas().remove(cosmoRPraticaPratica);
    cosmoRPraticaPratica.setCosmoDTipoRelazionePratica(null);

    return cosmoRPraticaPratica;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((dtInizioVal == null) ? 0 : dtInizioVal.hashCode());
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
    CosmoDTipoRelazionePratica other = (CosmoDTipoRelazionePratica) obj;
    if (codice == null) {
      if (other.codice != null) {
        return false;
      }
    } else if (!codice.equals(other.codice)) {
      return false;
    }
    if (dtInizioVal == null) {
      if (other.dtInizioVal != null) {
        return false;
      }
    } else if (!dtInizioVal.equals(other.dtInizioVal)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDTipoRelazionePratica [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (descrizioneInversa != null ? "descrizioneInversa=" + descrizioneInversa + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "") + "]";
  }
}
