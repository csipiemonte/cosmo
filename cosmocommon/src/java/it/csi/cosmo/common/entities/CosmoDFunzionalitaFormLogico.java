/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_funzionalita_form_logico database table.
 *
 */
@Entity
@Table(name="cosmo_d_funzionalita_form_logico")
@NamedQuery(name="CosmoDFunzionalitaFormLogico.findAll", query="SELECT c FROM CosmoDFunzionalitaFormLogico c")
public class CosmoDFunzionalitaFormLogico extends CosmoDEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "codice", length = 100, nullable = false)
  private String codice;

  @Column(name = "descrizione", length = 255, nullable = true)
  private String descrizione;

  @Column(name = "eseguibile_massivamente", nullable = false)
  private Boolean eseguibileMassivamente;

  @Column(name = "handler", length = 1000, nullable = true)
  private String handler;

  @Column(name = "multi_istanza", nullable = false)
  private Boolean multiIstanza;

  // bi-directional many-to-one association to CosmoTCertificatoFirma
  @OneToMany(mappedBy = "funzionalita")
  @Where(clause = "dt_fine_val is null")
  private List<CosmoRFunzionalitaParametroFormLogico> associazioniParametri;

  public CosmoDFunzionalitaFormLogico() {
    // NOOP
  }

  public List<CosmoRFunzionalitaParametroFormLogico> getAssociazioniParametri() {
    return associazioniParametri;
  }

  public void setAssociazioniParametri(
      List<CosmoRFunzionalitaParametroFormLogico> associazioniParametri) {
    this.associazioniParametri = associazioniParametri;
  }

  public String getHandler() {
    return handler;
  }

  public void setHandler(String handler) {
    this.handler = handler;
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

  public Boolean isMultiIstanza() {
    return this.multiIstanza;
  }

  public Boolean getEseguibileMassivamente() {
    return this.eseguibileMassivamente;
  }

  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
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
    CosmoDFunzionalitaFormLogico other = (CosmoDFunzionalitaFormLogico) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    if (descrizione == null) {
      if (other.descrizione != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDFunzionalitaFormLogico [codice=" + codice + ", descrizione=" + descrizione + "]";
  }


}
