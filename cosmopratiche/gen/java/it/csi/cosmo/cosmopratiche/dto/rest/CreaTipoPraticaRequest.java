/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaDocumentoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaStatoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaTrasformazioneDatiRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaTipoPraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String processDefinitionKey = null;
  private String caseDefinitionKey = null;
  private String codiceApplicazioneStardas = null;
  private Long idEnte = null;
  private List<AggiornaTipoPraticaStatoRequest> stati = new ArrayList<>();
  private List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento = new ArrayList<>();
  private Boolean creabileDaInterfaccia = null;
  private Boolean creabileDaServizio = null;
  private List<AggiornaTipoPraticaTrasformazioneDatiRequest> trasformazioni = new ArrayList<>();
  private String codiceCustomForm = null;
  private String responsabileTrattamentoStardas = null;
  private Boolean overrideResponsabileTrattamento = null;
  private Boolean annullabile = null;
  private Boolean condivisibile = null;
  private Boolean assegnabile = null;
  private List<TabsDettaglio> tabsDettaglio = new ArrayList<>();
  private Integer ordine = null;
  private String codiceFruitoreStardas = null;
  private Boolean overrideFruitoreDefault = null;
  private Long idGruppoCreatore = null;
  private Long idGruppoSupervisore = null;
  private String registrazioneStilo = null;
  private String tipoUnitaDocumentariaStilo = null;
  private String immagine = null;
  private String codiceEnteCertificatore = null;
  private String codiceTipoCredenziale = null;
  private String codiceTipoOtp = null;
  private String codiceProfiloFEQ = null;
  private String codiceSceltaMarcaTemporale = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  @Size(min=1,max=255)
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @Size(max=500)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinitionKey 
  @Size(max=500)
  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }
  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  /**
   **/
  


  // nome originario nello yaml: caseDefinitionKey 
  @Size(max=500)
  public String getCaseDefinitionKey() {
    return caseDefinitionKey;
  }
  public void setCaseDefinitionKey(String caseDefinitionKey) {
    this.caseDefinitionKey = caseDefinitionKey;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceApplicazioneStardas 
  @Size(max=50)
  public String getCodiceApplicazioneStardas() {
    return codiceApplicazioneStardas;
  }
  public void setCodiceApplicazioneStardas(String codiceApplicazioneStardas) {
    this.codiceApplicazioneStardas = codiceApplicazioneStardas;
  }

  /**
   **/
  


  // nome originario nello yaml: idEnte 
  @NotNull
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: stati 
  public List<AggiornaTipoPraticaStatoRequest> getStati() {
    return stati;
  }
  public void setStati(List<AggiornaTipoPraticaStatoRequest> stati) {
    this.stati = stati;
  }

  /**
   **/
  


  // nome originario nello yaml: tipiDocumento 
  public List<AggiornaTipoPraticaDocumentoRequest> getTipiDocumento() {
    return tipiDocumento;
  }
  public void setTipiDocumento(List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento) {
    this.tipiDocumento = tipiDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: creabileDaInterfaccia 
  @NotNull
  public Boolean isCreabileDaInterfaccia() {
    return creabileDaInterfaccia;
  }
  public void setCreabileDaInterfaccia(Boolean creabileDaInterfaccia) {
    this.creabileDaInterfaccia = creabileDaInterfaccia;
  }

  /**
   **/
  


  // nome originario nello yaml: creabileDaServizio 
  @NotNull
  public Boolean isCreabileDaServizio() {
    return creabileDaServizio;
  }
  public void setCreabileDaServizio(Boolean creabileDaServizio) {
    this.creabileDaServizio = creabileDaServizio;
  }

  /**
   **/
  


  // nome originario nello yaml: trasformazioni 
  public List<AggiornaTipoPraticaTrasformazioneDatiRequest> getTrasformazioni() {
    return trasformazioni;
  }
  public void setTrasformazioni(List<AggiornaTipoPraticaTrasformazioneDatiRequest> trasformazioni) {
    this.trasformazioni = trasformazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceCustomForm 
  public String getCodiceCustomForm() {
    return codiceCustomForm;
  }
  public void setCodiceCustomForm(String codiceCustomForm) {
    this.codiceCustomForm = codiceCustomForm;
  }

  /**
   **/
  


  // nome originario nello yaml: responsabileTrattamentoStardas 
  @Size(max=100)
  public String getResponsabileTrattamentoStardas() {
    return responsabileTrattamentoStardas;
  }
  public void setResponsabileTrattamentoStardas(String responsabileTrattamentoStardas) {
    this.responsabileTrattamentoStardas = responsabileTrattamentoStardas;
  }

  /**
   **/
  


  // nome originario nello yaml: overrideResponsabileTrattamento 
  public Boolean isOverrideResponsabileTrattamento() {
    return overrideResponsabileTrattamento;
  }
  public void setOverrideResponsabileTrattamento(Boolean overrideResponsabileTrattamento) {
    this.overrideResponsabileTrattamento = overrideResponsabileTrattamento;
  }

  /**
   **/
  


  // nome originario nello yaml: annullabile 
  public Boolean isAnnullabile() {
    return annullabile;
  }
  public void setAnnullabile(Boolean annullabile) {
    this.annullabile = annullabile;
  }

  /**
   **/
  


  // nome originario nello yaml: condivisibile 
  public Boolean isCondivisibile() {
    return condivisibile;
  }
  public void setCondivisibile(Boolean condivisibile) {
    this.condivisibile = condivisibile;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnabile 
  public Boolean isAssegnabile() {
    return assegnabile;
  }
  public void setAssegnabile(Boolean assegnabile) {
    this.assegnabile = assegnabile;
  }

  /**
   **/
  


  // nome originario nello yaml: tabsDettaglio 
  public List<TabsDettaglio> getTabsDettaglio() {
    return tabsDettaglio;
  }
  public void setTabsDettaglio(List<TabsDettaglio> tabsDettaglio) {
    this.tabsDettaglio = tabsDettaglio;
  }

  /**
   **/
  


  // nome originario nello yaml: ordine 
  public Integer getOrdine() {
    return ordine;
  }
  public void setOrdine(Integer ordine) {
    this.ordine = ordine;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFruitoreStardas 
  public String getCodiceFruitoreStardas() {
    return codiceFruitoreStardas;
  }
  public void setCodiceFruitoreStardas(String codiceFruitoreStardas) {
    this.codiceFruitoreStardas = codiceFruitoreStardas;
  }

  /**
   **/
  


  // nome originario nello yaml: overrideFruitoreDefault 
  public Boolean isOverrideFruitoreDefault() {
    return overrideFruitoreDefault;
  }
  public void setOverrideFruitoreDefault(Boolean overrideFruitoreDefault) {
    this.overrideFruitoreDefault = overrideFruitoreDefault;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppoCreatore 
  public Long getIdGruppoCreatore() {
    return idGruppoCreatore;
  }
  public void setIdGruppoCreatore(Long idGruppoCreatore) {
    this.idGruppoCreatore = idGruppoCreatore;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppoSupervisore 
  public Long getIdGruppoSupervisore() {
    return idGruppoSupervisore;
  }
  public void setIdGruppoSupervisore(Long idGruppoSupervisore) {
    this.idGruppoSupervisore = idGruppoSupervisore;
  }

  /**
   **/
  


  // nome originario nello yaml: registrazioneStilo 
  public String getRegistrazioneStilo() {
    return registrazioneStilo;
  }
  public void setRegistrazioneStilo(String registrazioneStilo) {
    this.registrazioneStilo = registrazioneStilo;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoUnitaDocumentariaStilo 
  public String getTipoUnitaDocumentariaStilo() {
    return tipoUnitaDocumentariaStilo;
  }
  public void setTipoUnitaDocumentariaStilo(String tipoUnitaDocumentariaStilo) {
    this.tipoUnitaDocumentariaStilo = tipoUnitaDocumentariaStilo;
  }

  /**
   **/
  


  // nome originario nello yaml: immagine 
  public String getImmagine() {
    return immagine;
  }
  public void setImmagine(String immagine) {
    this.immagine = immagine;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEnteCertificatore 
  public String getCodiceEnteCertificatore() {
    return codiceEnteCertificatore;
  }
  public void setCodiceEnteCertificatore(String codiceEnteCertificatore) {
    this.codiceEnteCertificatore = codiceEnteCertificatore;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoCredenziale 
  public String getCodiceTipoCredenziale() {
    return codiceTipoCredenziale;
  }
  public void setCodiceTipoCredenziale(String codiceTipoCredenziale) {
    this.codiceTipoCredenziale = codiceTipoCredenziale;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoOtp 
  public String getCodiceTipoOtp() {
    return codiceTipoOtp;
  }
  public void setCodiceTipoOtp(String codiceTipoOtp) {
    this.codiceTipoOtp = codiceTipoOtp;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceProfiloFEQ 
  public String getCodiceProfiloFEQ() {
    return codiceProfiloFEQ;
  }
  public void setCodiceProfiloFEQ(String codiceProfiloFEQ) {
    this.codiceProfiloFEQ = codiceProfiloFEQ;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceSceltaMarcaTemporale 
  public String getCodiceSceltaMarcaTemporale() {
    return codiceSceltaMarcaTemporale;
  }
  public void setCodiceSceltaMarcaTemporale(String codiceSceltaMarcaTemporale) {
    this.codiceSceltaMarcaTemporale = codiceSceltaMarcaTemporale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaTipoPraticaRequest creaTipoPraticaRequest = (CreaTipoPraticaRequest) o;
    return Objects.equals(codice, creaTipoPraticaRequest.codice) &&
        Objects.equals(descrizione, creaTipoPraticaRequest.descrizione) &&
        Objects.equals(processDefinitionKey, creaTipoPraticaRequest.processDefinitionKey) &&
        Objects.equals(caseDefinitionKey, creaTipoPraticaRequest.caseDefinitionKey) &&
        Objects.equals(codiceApplicazioneStardas, creaTipoPraticaRequest.codiceApplicazioneStardas) &&
        Objects.equals(idEnte, creaTipoPraticaRequest.idEnte) &&
        Objects.equals(stati, creaTipoPraticaRequest.stati) &&
        Objects.equals(tipiDocumento, creaTipoPraticaRequest.tipiDocumento) &&
        Objects.equals(creabileDaInterfaccia, creaTipoPraticaRequest.creabileDaInterfaccia) &&
        Objects.equals(creabileDaServizio, creaTipoPraticaRequest.creabileDaServizio) &&
        Objects.equals(trasformazioni, creaTipoPraticaRequest.trasformazioni) &&
        Objects.equals(codiceCustomForm, creaTipoPraticaRequest.codiceCustomForm) &&
        Objects.equals(responsabileTrattamentoStardas, creaTipoPraticaRequest.responsabileTrattamentoStardas) &&
        Objects.equals(overrideResponsabileTrattamento, creaTipoPraticaRequest.overrideResponsabileTrattamento) &&
        Objects.equals(annullabile, creaTipoPraticaRequest.annullabile) &&
        Objects.equals(condivisibile, creaTipoPraticaRequest.condivisibile) &&
        Objects.equals(assegnabile, creaTipoPraticaRequest.assegnabile) &&
        Objects.equals(tabsDettaglio, creaTipoPraticaRequest.tabsDettaglio) &&
        Objects.equals(ordine, creaTipoPraticaRequest.ordine) &&
        Objects.equals(codiceFruitoreStardas, creaTipoPraticaRequest.codiceFruitoreStardas) &&
        Objects.equals(overrideFruitoreDefault, creaTipoPraticaRequest.overrideFruitoreDefault) &&
        Objects.equals(idGruppoCreatore, creaTipoPraticaRequest.idGruppoCreatore) &&
        Objects.equals(idGruppoSupervisore, creaTipoPraticaRequest.idGruppoSupervisore) &&
        Objects.equals(registrazioneStilo, creaTipoPraticaRequest.registrazioneStilo) &&
        Objects.equals(tipoUnitaDocumentariaStilo, creaTipoPraticaRequest.tipoUnitaDocumentariaStilo) &&
        Objects.equals(immagine, creaTipoPraticaRequest.immagine) &&
        Objects.equals(codiceEnteCertificatore, creaTipoPraticaRequest.codiceEnteCertificatore) &&
        Objects.equals(codiceTipoCredenziale, creaTipoPraticaRequest.codiceTipoCredenziale) &&
        Objects.equals(codiceTipoOtp, creaTipoPraticaRequest.codiceTipoOtp) &&
        Objects.equals(codiceProfiloFEQ, creaTipoPraticaRequest.codiceProfiloFEQ) &&
        Objects.equals(codiceSceltaMarcaTemporale, creaTipoPraticaRequest.codiceSceltaMarcaTemporale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, processDefinitionKey, caseDefinitionKey, codiceApplicazioneStardas, idEnte, stati, tipiDocumento, creabileDaInterfaccia, creabileDaServizio, trasformazioni, codiceCustomForm, responsabileTrattamentoStardas, overrideResponsabileTrattamento, annullabile, condivisibile, assegnabile, tabsDettaglio, ordine, codiceFruitoreStardas, overrideFruitoreDefault, idGruppoCreatore, idGruppoSupervisore, registrazioneStilo, tipoUnitaDocumentariaStilo, immagine, codiceEnteCertificatore, codiceTipoCredenziale, codiceTipoOtp, codiceProfiloFEQ, codiceSceltaMarcaTemporale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaTipoPraticaRequest {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    processDefinitionKey: ").append(toIndentedString(processDefinitionKey)).append("\n");
    sb.append("    caseDefinitionKey: ").append(toIndentedString(caseDefinitionKey)).append("\n");
    sb.append("    codiceApplicazioneStardas: ").append(toIndentedString(codiceApplicazioneStardas)).append("\n");
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    stati: ").append(toIndentedString(stati)).append("\n");
    sb.append("    tipiDocumento: ").append(toIndentedString(tipiDocumento)).append("\n");
    sb.append("    creabileDaInterfaccia: ").append(toIndentedString(creabileDaInterfaccia)).append("\n");
    sb.append("    creabileDaServizio: ").append(toIndentedString(creabileDaServizio)).append("\n");
    sb.append("    trasformazioni: ").append(toIndentedString(trasformazioni)).append("\n");
    sb.append("    codiceCustomForm: ").append(toIndentedString(codiceCustomForm)).append("\n");
    sb.append("    responsabileTrattamentoStardas: ").append(toIndentedString(responsabileTrattamentoStardas)).append("\n");
    sb.append("    overrideResponsabileTrattamento: ").append(toIndentedString(overrideResponsabileTrattamento)).append("\n");
    sb.append("    annullabile: ").append(toIndentedString(annullabile)).append("\n");
    sb.append("    condivisibile: ").append(toIndentedString(condivisibile)).append("\n");
    sb.append("    assegnabile: ").append(toIndentedString(assegnabile)).append("\n");
    sb.append("    tabsDettaglio: ").append(toIndentedString(tabsDettaglio)).append("\n");
    sb.append("    ordine: ").append(toIndentedString(ordine)).append("\n");
    sb.append("    codiceFruitoreStardas: ").append(toIndentedString(codiceFruitoreStardas)).append("\n");
    sb.append("    overrideFruitoreDefault: ").append(toIndentedString(overrideFruitoreDefault)).append("\n");
    sb.append("    idGruppoCreatore: ").append(toIndentedString(idGruppoCreatore)).append("\n");
    sb.append("    idGruppoSupervisore: ").append(toIndentedString(idGruppoSupervisore)).append("\n");
    sb.append("    registrazioneStilo: ").append(toIndentedString(registrazioneStilo)).append("\n");
    sb.append("    tipoUnitaDocumentariaStilo: ").append(toIndentedString(tipoUnitaDocumentariaStilo)).append("\n");
    sb.append("    immagine: ").append(toIndentedString(immagine)).append("\n");
    sb.append("    codiceEnteCertificatore: ").append(toIndentedString(codiceEnteCertificatore)).append("\n");
    sb.append("    codiceTipoCredenziale: ").append(toIndentedString(codiceTipoCredenziale)).append("\n");
    sb.append("    codiceTipoOtp: ").append(toIndentedString(codiceTipoOtp)).append("\n");
    sb.append("    codiceProfiloFEQ: ").append(toIndentedString(codiceProfiloFEQ)).append("\n");
    sb.append("    codiceSceltaMarcaTemporale: ").append(toIndentedString(codiceSceltaMarcaTemporale)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

