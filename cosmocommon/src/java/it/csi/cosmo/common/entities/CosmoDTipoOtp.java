/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tipo_otp database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_otp")
@NamedQuery(name = "CosmoDTipoOtp.findAll", query = "SELECT c FROM CosmoDTipoOtp c")
public class CosmoDTipoOtp extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  @Column(name = "non_valido_in_apposizione", nullable = false)
  private Boolean nonValidoInApposizione;

  // bi-directional many-to-one association to CosmoTCertificatoFirma
  @OneToMany(mappedBy = "cosmoDTipoOtp")
  private List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @OneToMany(mappedBy = "cosmoDTipoOtp")
  private List<CosmoDTipoPratica> cosmoDTipoPraticas;

  public CosmoDTipoOtp() {
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

  public Boolean getNonValidoInApposizione() {
    return nonValidoInApposizione;
  }

  public void setNonValidoInApposizione(Boolean nonValidoInApposizione) {
    this.nonValidoInApposizione = nonValidoInApposizione;
  }

  public List<CosmoTCertificatoFirma> getCosmoTCertificatoFirmas() {
    return this.cosmoTCertificatoFirmas;
  }

  public void setCosmoTCertificatoFirmas(List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas) {
    this.cosmoTCertificatoFirmas = cosmoTCertificatoFirmas;
  }

  public CosmoTCertificatoFirma addCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().add(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoDTipoOtp(this);

    return cosmoTCertificatoFirma;
  }

  public CosmoTCertificatoFirma removeCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().remove(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoDTipoOtp(null);

    return cosmoTCertificatoFirma;
  }

  public List<CosmoDTipoPratica> getCosmoDTipoPraticas() {
    return this.cosmoDTipoPraticas;
  }

  public void setCosmoDTipoPraticas(List<CosmoDTipoPratica> cosmoDTipoPraticas) {
    this.cosmoDTipoPraticas = cosmoDTipoPraticas;
  }

  public CosmoDTipoPratica addCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    getCosmoDTipoPraticas().add(cosmoDTipoPratica);
    cosmoDTipoPratica.setCosmoDTipoOtp(this);

    return cosmoDTipoPratica;
  }

  public CosmoDTipoPratica removeCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    getCosmoDTipoPraticas().remove(cosmoDTipoPratica);
    cosmoDTipoPratica.setCosmoDTipoOtp(null);

    return cosmoDTipoPratica;
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
    CosmoDTipoOtp other = (CosmoDTipoOtp) obj;
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
    return "CosmoDTipoOtp [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "")
        + (nonValidoInApposizione != null ? "nonValidoInApposizione=" + nonValidoInApposizione : "")
        + "]";
  }


}
