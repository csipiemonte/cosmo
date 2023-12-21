/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tipo_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_pratica")
@NamedQuery(name = "CosmoDTipoPratica.findAll", query = "SELECT c FROM CosmoDTipoPratica c")
public class CosmoDTipoPratica extends CosmoDEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 100)
  private String codice;

  private Boolean annullabile;

  private Boolean assegnabile;

  @Column(name = "case_definition_key", length = 500, nullable = true)
  private String caseDefinitionKey;

  @Column(name = "codice_applicazione_stardas")
  private String codiceApplicazioneStardas;

  private Boolean condivisibile;

  @Column(name = "creabile_da_interfaccia", nullable = false)
  private Boolean creabileDaInterfaccia;

  @Column(name = "creabile_da_servizio", nullable = false)
  private Boolean creabileDaServizio;

  @Column(length = 100)
  private String descrizione;

  @Column(name = "override_responsabile_trattamento")
  private Boolean overrideResponsabileTrattamento;

  @Column(name = "process_definition_key", length = 500, nullable = true)
  private String processDefinitionKey;

  @Column(name = "responsabile_trattamento_stardas", length = 100)
  private String responsabileTrattamentoStardas;

  @Column(name = "override_fruitore_default")
  private Boolean overrideFruitoreDefault;

  @Column(name = "codice_fruitore_stardas")
  private String codiceFruitoreStardas;

  @Column(name = "registrazione_stilo")
  private String registrazioneStilo;

  @Column(name = "tipo_unita_documentaria_stilo")
  private String tipoUnitaDocumentariaStilo;

  private byte[] icona;

  //bi-directional many-to-one association to CosmoDEnteCertificatore
  @ManyToOne
  @JoinColumn(name="ente_certificatore")
  private CosmoDEnteCertificatore cosmoDEnteCertificatore;

  //bi-directional many-to-one association to CosmoDProfiloFeq
  @ManyToOne
  @JoinColumn(name="profilo_feq")
  private CosmoDProfiloFeq cosmoDProfiloFeq;

  //bi-directional many-to-one association to CosmoDSceltaMarcaTemporale
  @ManyToOne
  @JoinColumn(name="scelta_marca_temporale")
  private CosmoDSceltaMarcaTemporale cosmoDSceltaMarcaTemporale;

  //bi-directional many-to-one association to CosmoDTipoCredenzialiFirma
  @ManyToOne
  @JoinColumn(name="tipo_credenziali_firma")
  private CosmoDTipoCredenzialiFirma cosmoDTipoCredenzialiFirma;

  //bi-directional many-to-one association to CosmoDTipoOtp
  @ManyToOne
  @JoinColumn(name="tipo_otp")
  private CosmoDTipoOtp cosmoDTipoOtp;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoCConfigurazioneMetadati
  @OneToMany(mappedBy = "cosmoDTipoPratica", fetch = FetchType.LAZY)
  private List<CosmoCConfigurazioneMetadati> cosmoCConfigurazioneMetadatis;

  // bi-directional many-to-one association to CosmoRTipodocTipopratica
  @OneToMany(mappedBy = "cosmoDTipoPratica", fetch = FetchType.LAZY)
  private List<CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas;

  // bi-directional many-to-one association to CosmoTPratica
  @OneToMany(mappedBy = "tipo", fetch = FetchType.LAZY)
  private List<CosmoTPratica> cosmoTPraticas;

  // bi-directional many-to-one association to CosmoRStatoTipoPratica
  @OneToMany(mappedBy = "cosmoDTipoPratica")
  private List<CosmoRStatoTipoPratica> cosmoRStatoTipoPraticas;

  // bi-directional many-to-one association to CosmoTPratica
  @OneToMany(mappedBy = "tipoPratica", fetch = FetchType.LAZY)
  private List<CosmoDTrasformazioneDatiPratica> trasformazioni;

  // bi-directional many-to-one association to CosmoRGruppoTipoPratica
  @OneToMany(mappedBy = "cosmoDTipoPratica", fetch = FetchType.LAZY)
  private List<CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas;

  // bi-directional many-to-one association to CosmoDCustomFormFormio
  @OneToMany(mappedBy = "cosmoDTipoPratica", fetch = FetchType.LAZY)
  private List<CosmoDCustomFormFormio> cosmoDCustomFormFormios;

  // bi-directional many-to-one association to CosmoRTabDettaglioTipoPratica
  @OneToMany(mappedBy = "cosmoDTipoPratica", fetch = FetchType.LAZY)
  private List<CosmoRTabDettaglioTipoPratica> cosmoRTabDettaglioTipoPraticas;

  // bi-directional many-to-one association to CosmoTVariabileProcesso
  @OneToMany(mappedBy = "tipoPratica", fetch = FetchType.LAZY)
  private List<CosmoTVariabileProcesso> cosmoTVariabileProcessos;

  // bi-directional many-to-one association to CosmoRTipoDocumentoTipoDocumento
  @OneToMany(mappedBy = "cosmoDTipoPratica")
  private List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentos;

  // bi-directional many-to-one association to CosmoTTemplateFea
  @OneToMany(mappedBy = "tipologiaPratica")
  private List<CosmoTTemplateFea> cosmoTTemplateFeas;

  public CosmoDTipoPratica() {
    // empty constructor
  }

  public Boolean getAnnullabile() {
    return this.annullabile;
  }

  public void setAnnullabile(Boolean annullabile) {
    this.annullabile = annullabile;
  }

  public Boolean getAssegnabile() {
    return this.assegnabile;
  }

  public void setAssegnabile(Boolean assegnabile) {
    this.assegnabile = assegnabile;
  }

  public String getCaseDefinitionKey() {
    return caseDefinitionKey;
  }

  public void setCaseDefinitionKey(String caseDefinitionKey) {
    this.caseDefinitionKey = caseDefinitionKey;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getCodiceApplicazioneStardas() {
    return codiceApplicazioneStardas;
  }

  public void setCodiceApplicazioneStardas(String codiceApplicazioneStardas) {
    this.codiceApplicazioneStardas = codiceApplicazioneStardas;
  }

  public Boolean getCondivisibile() {
    return this.condivisibile;
  }

  public void setCondivisibile(Boolean condivisibile) {
    this.condivisibile = condivisibile;
  }

  public Boolean getCreabileDaInterfaccia() {
    return this.creabileDaInterfaccia;
  }

  public void setCreabileDaInterfaccia(Boolean creabileDaInterfaccia) {
    this.creabileDaInterfaccia = creabileDaInterfaccia;
  }

  public Boolean getCreabileDaServizio() {
    return this.creabileDaServizio;
  }

  public void setCreabileDaServizio(Boolean creabileDaServizio) {
    this.creabileDaServizio = creabileDaServizio;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  public Boolean getOverrideResponsabileTrattamento() {
    return overrideResponsabileTrattamento;
  }

  public void setOverrideResponsabileTrattamento(Boolean overrideResponsabileTrattamento) {
    this.overrideResponsabileTrattamento = overrideResponsabileTrattamento;
  }

  public Boolean getOverrideFruitoreDefault() {
    return this.overrideFruitoreDefault;
  }

  public void setOverrideFruitoreDefault(Boolean overrideFruitoreDefault) {
    this.overrideFruitoreDefault = overrideFruitoreDefault;
  }

  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  public String getResponsabileTrattamentoStardas() {
    return responsabileTrattamentoStardas;
  }

  public void setResponsabileTrattamentoStardas(String responsabileTrattamentoStardas) {
    this.responsabileTrattamentoStardas = responsabileTrattamentoStardas;
  }

  public String getCodiceFruitoreStardas() {
    return this.codiceFruitoreStardas;
  }

  public void setCodiceFruitoreStardas(String codiceFruitoreStardas) {
    this.codiceFruitoreStardas = codiceFruitoreStardas;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public byte[] getIcona() {
    return this.icona;
  }

  public void setIcona(byte[] icona) {
    this.icona = icona;
  }


  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public List<CosmoDTrasformazioneDatiPratica> getTrasformazioni() {
    return trasformazioni;
  }

  public void setTrasformazioni(List<CosmoDTrasformazioneDatiPratica> trasformazioni) {
    this.trasformazioni = trasformazioni;
  }

  public List<CosmoRStatoTipoPratica> getCosmoRStatoTipoPraticas() {
    return cosmoRStatoTipoPraticas;
  }

  public void setCosmoRStatoTipoPraticas(List<CosmoRStatoTipoPratica> cosmoRStatoTipoPraticas) {
    this.cosmoRStatoTipoPraticas = cosmoRStatoTipoPraticas;
  }

  public List<CosmoCConfigurazioneMetadati> getCosmoCConfigurazioneMetadatis() {
    return this.cosmoCConfigurazioneMetadatis;
  }

  public void setCosmoCConfigurazioneMetadatis(
      List<CosmoCConfigurazioneMetadati> cosmoCConfigurazioneMetadatis) {
    this.cosmoCConfigurazioneMetadatis = cosmoCConfigurazioneMetadatis;
  }

  public CosmoCConfigurazioneMetadati addCosmoCConfigurazioneMetadati(
      CosmoCConfigurazioneMetadati cosmoCConfigurazioneMetadati) {
    getCosmoCConfigurazioneMetadatis().add(cosmoCConfigurazioneMetadati);
    cosmoCConfigurazioneMetadati.setCosmoDTipoPratica(this);

    return cosmoCConfigurazioneMetadati;
  }

  public CosmoCConfigurazioneMetadati removeCosmoCConfigurazioneMetadati(
      CosmoCConfigurazioneMetadati cosmoCConfigurazioneMetadati) {
    getCosmoCConfigurazioneMetadatis().remove(cosmoCConfigurazioneMetadati);
    cosmoCConfigurazioneMetadati.setCosmoDTipoPratica(null);

    return cosmoCConfigurazioneMetadati;
  }

  public List<CosmoRTipodocTipopratica> getCosmoRTipodocTipopraticas() {
    return this.cosmoRTipodocTipopraticas;
  }

  public void setCosmoRTipodocTipopraticas(
      List<CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas) {
    this.cosmoRTipodocTipopraticas = cosmoRTipodocTipopraticas;
  }

  public CosmoRTipodocTipopratica addCosmoRTipodocTipopratica(
      CosmoRTipodocTipopratica cosmoRTipodocTipopratica) {
    getCosmoRTipodocTipopraticas().add(cosmoRTipodocTipopratica);
    cosmoRTipodocTipopratica.setCosmoDTipoPratica(this);

    return cosmoRTipodocTipopratica;
  }

  public CosmoRTipodocTipopratica removeCosmoRTipodocTipopratica(
      CosmoRTipodocTipopratica cosmoRTipodocTipopratica) {
    getCosmoRTipodocTipopraticas().remove(cosmoRTipodocTipopratica);
    cosmoRTipodocTipopratica.setCosmoDTipoPratica(null);

    return cosmoRTipodocTipopratica;
  }

  public List<CosmoTPratica> getCosmoTPraticas() {
    return this.cosmoTPraticas;
  }

  public void setCosmoTPraticas(List<CosmoTPratica> cosmoTPraticas) {
    this.cosmoTPraticas = cosmoTPraticas;
  }

  public CosmoTPratica addCosmoTPratica(CosmoTPratica cosmoTPratica) {
    getCosmoTPraticas().add(cosmoTPratica);
    cosmoTPratica.setTipo(this);

    return cosmoTPratica;
  }

  public CosmoTPratica removeCosmoTPratica(CosmoTPratica cosmoTPratica) {
    getCosmoTPraticas().remove(cosmoTPratica);
    cosmoTPratica.setTipo(null);

    return cosmoTPratica;
  }

  public List<CosmoRGruppoTipoPratica> getCosmoRGruppoTipoPraticas() {
    return this.cosmoRGruppoTipoPraticas;
  }

  public void setCosmoRGruppoTipoPraticas(List<CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas) {
    this.cosmoRGruppoTipoPraticas = cosmoRGruppoTipoPraticas;
  }

  public CosmoRGruppoTipoPratica addCosmoRGruppoTipoPratica(
      CosmoRGruppoTipoPratica cosmoRGruppoTipoPratica) {
    getCosmoRGruppoTipoPraticas().add(cosmoRGruppoTipoPratica);
    cosmoRGruppoTipoPratica.setCosmoDTipoPratica(this);

    return cosmoRGruppoTipoPratica;
  }

  public CosmoRGruppoTipoPratica removeCosmoRGruppoTipoPratica(
      CosmoRGruppoTipoPratica cosmoRGruppoTipoPratica) {
    getCosmoRGruppoTipoPraticas().remove(cosmoRGruppoTipoPratica);
    cosmoRGruppoTipoPratica.setCosmoDTipoPratica(null);

    return cosmoRGruppoTipoPratica;
  }

  public List<CosmoDCustomFormFormio> getCosmoDCustomFormFormios() {
    return this.cosmoDCustomFormFormios;
  }

  public void setCosmoDCustomFormFormios(List<CosmoDCustomFormFormio> cosmoDCustomFormFormios) {
    this.cosmoDCustomFormFormios = cosmoDCustomFormFormios;
  }

  public CosmoDCustomFormFormio addCosmoDCustomFormFormio(
      CosmoDCustomFormFormio cosmoDCustomFormFormio) {
    getCosmoDCustomFormFormios().add(cosmoDCustomFormFormio);
    cosmoDCustomFormFormio.setCosmoDTipoPratica(this);

    return cosmoDCustomFormFormio;
  }

  public CosmoDCustomFormFormio removeCosmoDCustomFormFormio(
      CosmoDCustomFormFormio cosmoDCustomFormFormio) {
    getCosmoDCustomFormFormios().remove(cosmoDCustomFormFormio);
    cosmoDCustomFormFormio.setCosmoDTipoPratica(null);

    return cosmoDCustomFormFormio;
  }

  public List<CosmoRTabDettaglioTipoPratica> getCosmoRTabDettaglioTipoPraticas() {
    return this.cosmoRTabDettaglioTipoPraticas;
  }

  public void setCosmoRTabDettaglioTipoPraticas(
      List<CosmoRTabDettaglioTipoPratica> cosmoRTabDettaglioTipoPraticas) {
    this.cosmoRTabDettaglioTipoPraticas = cosmoRTabDettaglioTipoPraticas;
  }

  public CosmoRTabDettaglioTipoPratica addCosmoRTabDettaglioTipoPratica(
      CosmoRTabDettaglioTipoPratica cosmoRTabDettaglioTipoPratica) {
    getCosmoRTabDettaglioTipoPraticas().add(cosmoRTabDettaglioTipoPratica);
    cosmoRTabDettaglioTipoPratica.setCosmoDTipoPratica(this);

    return cosmoRTabDettaglioTipoPratica;
  }

  public CosmoRTabDettaglioTipoPratica removeCosmoRTabDettaglioTipoPratica(
      CosmoRTabDettaglioTipoPratica cosmoRTabDettaglioTipoPratica) {
    getCosmoRTabDettaglioTipoPraticas().remove(cosmoRTabDettaglioTipoPratica);
    cosmoRTabDettaglioTipoPratica.setCosmoDTipoPratica(null);

    return cosmoRTabDettaglioTipoPratica;
  }



  public List<CosmoTVariabileProcesso> getCosmoTVariabileProcessos() {
    return cosmoTVariabileProcessos;
  }

  public void setCosmoTVariabileProcessos(List<CosmoTVariabileProcesso> cosmoTVariabileProcessos) {
    this.cosmoTVariabileProcessos = cosmoTVariabileProcessos;
  }

  public CosmoTVariabileProcesso addCosmoTVariabileProcesso(
      CosmoTVariabileProcesso cosmoTVariabileProcesso) {
    getCosmoTVariabileProcessos().add(cosmoTVariabileProcesso);
    cosmoTVariabileProcesso.setTipoPratica(this);

    return cosmoTVariabileProcesso;
  }

  public CosmoTVariabileProcesso removeCosmoTVariabileProcesso(
      CosmoTVariabileProcesso cosmoTVariabileProcesso) {
    getCosmoTVariabileProcessos().remove(cosmoTVariabileProcesso);
    cosmoTVariabileProcesso.setTipoPratica(null);

    return cosmoTVariabileProcesso;
  }


  public String getRegistrazioneStilo() {
    return this.registrazioneStilo;
  }

  public void setRegistrazioneStilo(String registrazioneStilo) {
    this.registrazioneStilo = registrazioneStilo;
  }


  public String getTipoUnitaDocumentariaStilo() {
    return tipoUnitaDocumentariaStilo;
  }

  public void setTipoUnitaDocumentariaStilo(String tipoUnitaDocumentariaStilo) {
    this.tipoUnitaDocumentariaStilo = tipoUnitaDocumentariaStilo;
  }

  public CosmoDEnteCertificatore getCosmoDEnteCertificatore() {
    return this.cosmoDEnteCertificatore;
  }

  public void setCosmoDEnteCertificatore(CosmoDEnteCertificatore cosmoDEnteCertificatore) {
    this.cosmoDEnteCertificatore = cosmoDEnteCertificatore;
  }

  public CosmoDProfiloFeq getCosmoDProfiloFeq() {
    return this.cosmoDProfiloFeq;
  }

  public void setCosmoDProfiloFeq(CosmoDProfiloFeq cosmoDProfiloFeq) {
    this.cosmoDProfiloFeq = cosmoDProfiloFeq;
  }

  public CosmoDSceltaMarcaTemporale getCosmoDSceltaMarcaTemporale() {
    return this.cosmoDSceltaMarcaTemporale;
  }

  public void setCosmoDSceltaMarcaTemporale(CosmoDSceltaMarcaTemporale cosmoDSceltaMarcaTemporale) {
    this.cosmoDSceltaMarcaTemporale = cosmoDSceltaMarcaTemporale;
  }

  public CosmoDTipoCredenzialiFirma getCosmoDTipoCredenzialiFirma() {
    return this.cosmoDTipoCredenzialiFirma;
  }

  public void setCosmoDTipoCredenzialiFirma(CosmoDTipoCredenzialiFirma cosmoDTipoCredenzialiFirma) {
    this.cosmoDTipoCredenzialiFirma = cosmoDTipoCredenzialiFirma;
  }

  public CosmoDTipoOtp getCosmoDTipoOtp() {
    return this.cosmoDTipoOtp;
  }

  public void setCosmoDTipoOtp(CosmoDTipoOtp cosmoDTipoOtp) {
    this.cosmoDTipoOtp = cosmoDTipoOtp;
  }

  public List<CosmoRTipoDocumentoTipoDocumento> getCosmoRTipoDocumentoTipoDocumentos() {
    return this.cosmoRTipoDocumentoTipoDocumentos;
  }

  public void setCosmoRTipoDocumentoTipoDocumentos(
      List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentos) {
    this.cosmoRTipoDocumentoTipoDocumentos = cosmoRTipoDocumentoTipoDocumentos;
  }

  public CosmoRTipoDocumentoTipoDocumento addCosmoRTipoDocumentoTipoDocumento(
      CosmoRTipoDocumentoTipoDocumento cosmoRTipoDocumentoTipoDocumento) {
    getCosmoRTipoDocumentoTipoDocumentos().add(cosmoRTipoDocumentoTipoDocumento);
    cosmoRTipoDocumentoTipoDocumento.setCosmoDTipoPratica(this);

    return cosmoRTipoDocumentoTipoDocumento;
  }

  public CosmoRTipoDocumentoTipoDocumento removeCosmoRTipoDocumentoTipoDocumento(
      CosmoRTipoDocumentoTipoDocumento cosmoRTipoDocumentoTipoDocumento) {
    getCosmoRTipoDocumentoTipoDocumentos().remove(cosmoRTipoDocumentoTipoDocumento);
    cosmoRTipoDocumentoTipoDocumento.setCosmoDTipoPratica(null);

    return cosmoRTipoDocumentoTipoDocumento;
  }


  public List<CosmoTTemplateFea> getCosmoTTemplateFeas() {
    return this.cosmoTTemplateFeas;
  }

  public void setCosmoTTemplateFeas(List<CosmoTTemplateFea> cosmoTTemplateFeas) {
    this.cosmoTTemplateFeas = cosmoTTemplateFeas;
  }

  public CosmoTTemplateFea addCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().add(cosmoTTemplateFea);
    cosmoTTemplateFea.setTipologiaPratica(this);

    return cosmoTTemplateFea;
  }

  public CosmoTTemplateFea removeCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().remove(cosmoTTemplateFea);
    cosmoTTemplateFea.setTipologiaPratica(null);

    return cosmoTTemplateFea;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDTipoPratica other = (CosmoDTipoPratica) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDTipoPratica [descrizione=" + descrizione + ", codice=" + codice + "]";
  }

}
