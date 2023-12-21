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
 * The persistent class for the cosmo_d_ente_certificatore database table.
 *
 */
@Entity
@Table(name = "cosmo_d_ente_certificatore")
@NamedQuery(name = "CosmoDEnteCertificatore.findAll",
query = "SELECT c FROM CosmoDEnteCertificatore c")
public class CosmoDEnteCertificatore extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  @Column(name = "codice_ca", nullable = true)
  private String codiceCa;

  @Column(name = "codice_tsa", nullable = true)
  private String codiceTsa;

  // bi-directional many-to-one association to CosmoTCertificatoFirma
  @OneToMany(mappedBy = "cosmoDEnteCertificatore")
  private List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas;

  // bi-directional many-to-one association to CosmoREnteCertificatoreEnte
  @OneToMany(mappedBy = "cosmoDEnteCertificatore")
  private List<CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes;

  @Column(length = 20)
  private String provider;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @OneToMany(mappedBy = "cosmoDEnteCertificatore")
  private List<CosmoDTipoPratica> cosmoDTipoPraticas;

  public CosmoDEnteCertificatore() {
    // empty constructor
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getCodiceCa() {
    return this.codiceCa;
  }

  public void setCodiceCa(String codiceCa) {
    this.codiceCa = codiceCa;
  }

  public String getCodiceTsa() {
    return this.codiceTsa;
  }

  public void setCodiceTsa(String codiceTsa) {
    this.codiceTsa = codiceTsa;
  }


  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public List<CosmoTCertificatoFirma> getCosmoTCertificatoFirmas() {
    return this.cosmoTCertificatoFirmas;
  }

  public void setCosmoTCertificatoFirmas(List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas) {
    this.cosmoTCertificatoFirmas = cosmoTCertificatoFirmas;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public CosmoTCertificatoFirma addCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().add(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoDEnteCertificatore(this);

    return cosmoTCertificatoFirma;
  }

  public CosmoTCertificatoFirma removeCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().remove(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoDEnteCertificatore(null);

    return cosmoTCertificatoFirma;
  }

  public List<CosmoREnteCertificatoreEnte> getCosmoREnteCertificatoreEntes() {
    return this.cosmoREnteCertificatoreEntes;
  }

  public void setCosmoREnteCertificatoreEntes(
      List<CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes) {
    this.cosmoREnteCertificatoreEntes = cosmoREnteCertificatoreEntes;
  }

  public CosmoREnteCertificatoreEnte addCosmoREnteCertificatoreEnte(
      CosmoREnteCertificatoreEnte cosmoREnteCertificatoreEnte) {
    getCosmoREnteCertificatoreEntes().add(cosmoREnteCertificatoreEnte);
    cosmoREnteCertificatoreEnte.setCosmoDEnteCertificatore(this);

    return cosmoREnteCertificatoreEnte;
  }

  public CosmoREnteCertificatoreEnte removeCosmoREnteCertificatoreEnte(
      CosmoREnteCertificatoreEnte cosmoREnteCertificatoreEnte) {
    getCosmoREnteCertificatoreEntes().remove(cosmoREnteCertificatoreEnte);
    cosmoREnteCertificatoreEnte.setCosmoDEnteCertificatore(null);

    return cosmoREnteCertificatoreEnte;
  }

  public List<CosmoDTipoPratica> getCosmoDTipoPraticas() {
    return this.cosmoDTipoPraticas;
  }

  public void setCosmoDTipoPraticas(List<CosmoDTipoPratica> cosmoDTipoPraticas) {
    this.cosmoDTipoPraticas = cosmoDTipoPraticas;
  }

  public CosmoDTipoPratica addCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    getCosmoDTipoPraticas().add(cosmoDTipoPratica);
    cosmoDTipoPratica.setCosmoDEnteCertificatore(this);

    return cosmoDTipoPratica;
  }

  public CosmoDTipoPratica removeCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    getCosmoDTipoPraticas().remove(cosmoDTipoPratica);
    cosmoDTipoPratica.setCosmoDEnteCertificatore(null);

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
    CosmoDEnteCertificatore other = (CosmoDEnteCertificatore) obj;
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
    return "CosmoDEnteCertificatore [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "") + "]";
  }
}
