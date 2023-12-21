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
import it.csi.cosmo.common.util.ObjectUtils;


/**
 * The persistent class for the cosmo_t_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_t_pratica")
@NamedQuery(name = "CosmoTPratica.findAll", query = "SELECT c FROM CosmoTPratica c")
public class CosmoTPratica extends CosmoTEntity implements CsiLogAuditedEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_PRATICA_ID_GENERATOR", sequenceName = "COSMO_T_PRATICA_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_PRATICA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "data_cambio_stato")
  private Timestamp dataCambioStato;

  @Column(name = "data_creazione_pratica")
  private Timestamp dataCreazionePratica;

  @Column(name = "data_fine_pratica")
  private Timestamp dataFinePratica;

  @Column(name = "id_pratica_ext", length = 255)
  private String idPraticaExt;

  @Column(name = "link_pratica", length = 1000)
  private String linkPratica;

  @Column(name = "link_pratica_esterna", length = 1000)
  private String linkPraticaEsterna;

  private String metadati;

  @Column(length = 255, nullable = false)
  private String oggetto;

  private String riassunto;

  @Column(name = "riassunto_testuale")
  private String riassuntoTestuale;

  @Column(name = "utente_creazione_pratica", length = 50)
  private String utenteCreazionePratica;

  @Column(name = "uuid_nodo", length = 50)
  private String uuidNodo;

  @Column(name = "esterna", nullable = false)
  private Boolean esterna = Boolean.FALSE;

  // bi-directional many-to-one association to CosmoRPraticaUtenteGruppo
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoRPraticaUtenteGruppo> associazioneUtentiGruppi;

  // bi-directional many-to-one association to CosmoTAttivita
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoTAttivita> attivita;

  // bi-directional many-to-one association to CosmoTDocumento
  @OneToMany(mappedBy = "pratica")
  private List<CosmoTDocumento> documenti;

  // bi-directional many-to-one association to CosmoTCommento
  @OneToMany(mappedBy = "pratica")
  private List<CosmoTCommento> commenti;


  // bi-directional many-to-one association to CosmoTNotifica
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoTNotifica> notifiche;

  // bi-directional many-to-one association to CosmoTFruitore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_fruitore")
  private CosmoTFruitore fruitore;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica tipo;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stato")
  private CosmoDStatoPratica stato;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte ente;

  // bi-directional many-to-one association to CosmoRPraticaPratica
  @OneToMany(mappedBy = "cosmoTPraticaDa")
  private List<CosmoRPraticaPratica> cosmoRPraticaPraticasA;

  // bi-directional many-to-one association to CosmoRPraticaPratica
  @OneToMany(mappedBy = "cosmoTPraticaA")
  private List<CosmoRPraticaPratica> cosmoRPraticaPraticasDa;

  // bi-directional many-to-one association to CosmoRPraticaTag
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoRPraticaTag> cosmoRPraticaTags;

  // bi-directional many-to-one association to CosmoTVariabile
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoTVariabile> variabili;

  @OneToMany(mappedBy = "cosmoTPratica")
  // bi-directional many-to-one association to CosmoTCaricamentoPratica
  private List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;

  // bi-directional many-to-one association to CosmoTTemplateFea
  @OneToMany(mappedBy = "cosmoTPratica")
  private List<CosmoTTemplateFea> cosmoTTemplateFeas;


  public CosmoTPratica() {
    // empty constructor
  }

  public String getProcessInstanceId() {
    return ObjectUtils.getIdFromLink(this.linkPratica);
  }

  public String getLinkPraticaEsterna() {
    return linkPraticaEsterna;
  }

  public void setLinkPraticaEsterna(String linkPraticaEsterna) {
    this.linkPraticaEsterna = linkPraticaEsterna;
  }

  public Boolean getEsterna() {
    return esterna;
  }

  public void setEsterna(Boolean esterna) {
    this.esterna = esterna;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the dataCambioStato
   */
  public Timestamp getDataCambioStato() {
    return dataCambioStato;
  }

  /**
   * @param dataCambioStato the dataCambioStato to set
   */
  public void setDataCambioStato(Timestamp dataCambioStato) {
    this.dataCambioStato = dataCambioStato;
  }

  /**
   * @return the dataCreazionePratica
   */
  public Timestamp getDataCreazionePratica() {
    return dataCreazionePratica;
  }

  /**
   * @param dataCreazionePratica the dataCreazionePratica to set
   */
  public void setDataCreazionePratica(Timestamp dataCreazionePratica) {
    this.dataCreazionePratica = dataCreazionePratica;
  }

  /**
   * @return the dataFinePratica
   */
  public Timestamp getDataFinePratica() {
    return dataFinePratica;
  }

  /**
   * @param dataFinePratica the dataFinePratica to set
   */
  public void setDataFinePratica(Timestamp dataFinePratica) {
    this.dataFinePratica = dataFinePratica;
  }

  /**
   * @return the idPraticaExt
   */
  public String getIdPraticaExt() {
    return idPraticaExt;
  }

  /**
   * @param idPraticaExt the idPraticaExt to set
   */
  public void setIdPraticaExt(String idPraticaExt) {
    this.idPraticaExt = idPraticaExt;
  }

  /**
   * @return the linkPratica
   */
  public String getLinkPratica() {
    return linkPratica;
  }

  /**
   * @param linkPratica the linkPratica to set
   */
  public void setLinkPratica(String linkPratica) {
    this.linkPratica = linkPratica;
  }

  /**
   * @return the metadati
   */
  public String getMetadati() {
    return metadati;
  }

  /**
   * @param metadati the metadati to set
   */
  public void setMetadati(String metadati) {
    this.metadati = metadati;
  }

  /**
   * @return the oggetto
   */
  public String getOggetto() {
    return oggetto;
  }

  /**
   * @param oggetto the oggetto to set
   */
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   * @return the riassunto
   */
  public String getRiassunto() {
    return riassunto;
  }

  /**
   * @param riassunto the riassunto to set
   */
  public void setRiassunto(String riassunto) {
    this.riassunto = riassunto;
  }

  public String getRiassuntoTestuale() {
    return riassuntoTestuale;
  }

  public void setRiassuntoTestuale(String riassuntoTestuale) {
    this.riassuntoTestuale = riassuntoTestuale;
  }

  /**
   * @return the utenteCreazionePratica
   */
  public String getUtenteCreazionePratica() {
    return utenteCreazionePratica;
  }

  /**
   * @param utenteCreazionePratica the utenteCreazionePratica to set
   */
  public void setUtenteCreazionePratica(String utenteCreazionePratica) {
    this.utenteCreazionePratica = utenteCreazionePratica;
  }

  /**
   * @return the uuidNodo
   */
  public String getUuidNodo() {
    return uuidNodo;
  }

  /**
   * @param uuidNodo the uuidNodo to set
   */
  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  /**
   * @return the associazioneUtenti
   */
  public List<CosmoRPraticaUtenteGruppo> getAssociazioneUtentiGruppi() {
    return associazioneUtentiGruppi;
  }

  /**
   * @param associazioneUtenti the associazioneUtenti to set
   */
  public void setAssociazioneUtentiGruppi(
      List<CosmoRPraticaUtenteGruppo> associazioneUtentiGruppi) {
    this.associazioneUtentiGruppi = associazioneUtentiGruppi;
  }

  /**
   * @return the attivita
   */
  public List<CosmoTAttivita> getAttivita() {
    return attivita;
  }

  /**
   * @param attivita the attivita to set
   */
  public void setAttivita(List<CosmoTAttivita> attivita) {
    this.attivita = attivita;
  }

  /**
   * @return the documenti
   */
  public List<CosmoTDocumento> getDocumenti() {
    return documenti;
  }

  /**
   * @param documenti the documenti to set
   */
  public void setDocumenti(List<CosmoTDocumento> documenti) {
    this.documenti = documenti;
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

  /**
   * @return the notifiche
   */
  public List<CosmoTNotifica> getNotifiche() {
    return notifiche;
  }

  /**
   * @param notifiche the notifiche to set
   */
  public void setNotifiche(List<CosmoTNotifica> notifiche) {
    this.notifiche = notifiche;
  }

  /**
   * @return the fruitore
   */
  public CosmoTFruitore getFruitore() {
    return fruitore;
  }

  /**
   * @param fruitore the fruitore to set
   */
  public void setFruitore(CosmoTFruitore fruitore) {
    this.fruitore = fruitore;
  }

  /**
   * @return the tipo
   */
  public CosmoDTipoPratica getTipo() {
    return tipo;
  }

  /**
   * @param tipo the tipo to set
   */
  public void setTipo(CosmoDTipoPratica tipo) {
    this.tipo = tipo;
  }

  /**
   * @return the stato
   */
  public CosmoDStatoPratica getStato() {
    return stato;
  }

  /**
   * @param stato the stato to set
   */
  public void setStato(CosmoDStatoPratica stato) {
    this.stato = stato;
  }

  /**
   * @return the ente
   */
  public CosmoTEnte getEnte() {
    return ente;
  }

  /**
   * @param ente the ente to set
   */
  public void setEnte(CosmoTEnte ente) {
    this.ente = ente;
  }

  public List<CosmoRPraticaPratica> getCosmoRPraticaPraticasA() {
    return this.cosmoRPraticaPraticasA;
  }

  public void setCosmoRPraticaPraticasA(List<CosmoRPraticaPratica> cosmoRPraticaPraticasA) {
    this.cosmoRPraticaPraticasA = cosmoRPraticaPraticasA;
  }

  public CosmoRPraticaPratica addCosmoRPraticaPraticasA(
      CosmoRPraticaPratica cosmoRPraticaPraticasA) {
    getCosmoRPraticaPraticasA().add(cosmoRPraticaPraticasA);
    cosmoRPraticaPraticasA.setCosmoTPraticaA(this);

    return cosmoRPraticaPraticasA;
  }

  public CosmoRPraticaPratica removeCosmoRPraticaPraticasA(
      CosmoRPraticaPratica cosmoRPraticaPraticasA) {
    getCosmoRPraticaPraticasA().remove(cosmoRPraticaPraticasA);
    cosmoRPraticaPraticasA.setCosmoTPraticaA(null);

    return cosmoRPraticaPraticasA;
  }

  public List<CosmoRPraticaPratica> getCosmoRPraticaPraticasDa() {
    return this.cosmoRPraticaPraticasDa;
  }

  public void setCosmoRPraticaPraticasDa(List<CosmoRPraticaPratica> cosmoRPraticaPraticasDa) {
    this.cosmoRPraticaPraticasDa = cosmoRPraticaPraticasDa;
  }

  public CosmoRPraticaPratica addCosmoRPraticaPraticasDa(
      CosmoRPraticaPratica cosmoRPraticaPraticasDa) {
    getCosmoRPraticaPraticasDa().add(cosmoRPraticaPraticasDa);
    cosmoRPraticaPraticasDa.setCosmoTPraticaDa(this);

    return cosmoRPraticaPraticasDa;
  }

  public CosmoRPraticaPratica removeCosmoRPraticaPraticasDa(
      CosmoRPraticaPratica cosmoRPraticaPraticasDa) {
    getCosmoRPraticaPraticasDa().remove(cosmoRPraticaPraticasDa);
    cosmoRPraticaPraticasDa.setCosmoTPraticaDa(null);

    return cosmoRPraticaPraticasDa;
  }

  public List<CosmoTCaricamentoPratica> getCosmoTCaricamentoPraticas() {
    return cosmoTCaricamentoPraticas;
  }

  public void setCosmoTCaricamentoPraticas(
      List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas) {
    this.cosmoTCaricamentoPraticas = cosmoTCaricamentoPraticas;
  }

  public CosmoTCaricamentoPratica addCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().add(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTPratica(this);

    return cosmoTCaricamentoPratica;
  }

  public CosmoTCaricamentoPratica removeCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().remove(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTPratica(null);

    return cosmoTCaricamentoPratica;
  }



  public List<CosmoRPraticaTag> getCosmoRPraticaTags() {
    return this.cosmoRPraticaTags;
  }

  public void setCosmoRPraticaTags(List<CosmoRPraticaTag> cosmoRPraticaTags) {
    this.cosmoRPraticaTags = cosmoRPraticaTags;
  }

  public CosmoRPraticaTag addCosmoRPraticaTag(CosmoRPraticaTag cosmoRPraticaTag) {
    getCosmoRPraticaTags().add(cosmoRPraticaTag);
    cosmoRPraticaTag.setCosmoTPratica(this);

    return cosmoRPraticaTag;
  }

  public CosmoRPraticaTag removeCosmoRPraticaTag(CosmoRPraticaTag cosmoRPraticaTag) {
    getCosmoRPraticaTags().remove(cosmoRPraticaTag);
    cosmoRPraticaTag.setCosmoTPratica(null);

    return cosmoRPraticaTag;
  }


  public List<CosmoTVariabile> getVariabili() {
    return variabili;
  }

  public void setVariabili(List<CosmoTVariabile> variabili) {
    this.variabili = variabili;
  }

  public CosmoTVariabile addCosmoTVariabile(CosmoTVariabile variabile) {
    getVariabili().add(variabile);
    variabile.setCosmoTPratica(this);

    return variabile;
  }

  public CosmoTVariabile removeCosmoTVariabile(CosmoTVariabile variabile) {
    getVariabili().remove(variabile);
    variabile.setCosmoTPratica(null);

    return variabile;
  }

  public List<CosmoTTemplateFea> getCosmoTTemplateFeas() {
    return this.cosmoTTemplateFeas;
  }

  public void setCosmoTTemplateFeas(List<CosmoTTemplateFea> cosmoTTemplateFeas) {
    this.cosmoTTemplateFeas = cosmoTTemplateFeas;
  }

  public CosmoTTemplateFea addCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().add(cosmoTTemplateFea);
    cosmoTTemplateFea.setCosmoTPratica(this);

    return cosmoTTemplateFea;
  }

  public CosmoTTemplateFea removeCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().remove(cosmoTTemplateFea);
    cosmoTTemplateFea.setCosmoTPratica(null);

    return cosmoTTemplateFea;
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoTPratica other = (CosmoTPratica) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTPratica [" + (id != null ? "id=" + id + ", " : "")
        + (dataCambioStato != null ? "dataCambioStato=" + dataCambioStato + ", " : "")
        + (dataCreazionePratica != null ? "dataCreazionePratica=" + dataCreazionePratica + ", "
            : "")
        + (dataFinePratica != null ? "dataFinePratica=" + dataFinePratica + ", " : "")
        + (idPraticaExt != null ? "idPraticaExt=" + idPraticaExt + ", " : "")
        + (linkPratica != null ? "linkPratica=" + linkPratica + ", " : "")
        + (oggetto != null ? "oggetto=" + oggetto + ", " : "")
        + (riassuntoTestuale != null ? "riassuntoTestuale=" + riassuntoTestuale + ", " : "")
        + (stato != null ? "stato=" + stato + ", " : "")
        + (esterna != null ? "esterna=" + esterna + ", " : "")
        + (linkPraticaEsterna != null ? "linkPraticaEsterna=" + linkPraticaEsterna + ", " : "")
        + (uuidNodo != null ? "uuidNodo=" + uuidNodo : "") + "]";
  }

}
