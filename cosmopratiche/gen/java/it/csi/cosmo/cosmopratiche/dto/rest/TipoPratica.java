/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmopratiche.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoCredenzialiFirma;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoDocumento;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoOTP;
import it.csi.cosmo.cosmopratiche.dto.rest.TrasformazioneDatiPratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TipoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String processDefinitionKey = null;
  private String caseDefinitionKey = null;
  private String codiceApplicazioneStardas = null;
  private RiferimentoEnte ente = null;
  private List<StatoPratica> stati = new ArrayList<>();
  private List<TipoDocumento> tipiDocumento = new ArrayList<>();
  private Boolean creabileDaInterfaccia = null;
  private Boolean creabileDaServizio = null;
  private List<TrasformazioneDatiPratica> trasformazioni = new ArrayList<>();
  private CustomForm customForm = null;
  private String responsabileTrattamentoStardas = null;
  private Boolean overrideResponsabileTrattamento = null;
  private Boolean annullabile = null;
  private Boolean condivisibile = null;
  private Boolean assegnabile = null;
  private List<TabsDettaglio> tabsDettaglio = new ArrayList<>();
  private String codiceFruitoreStardas = null;
  private Boolean overrideFruitoreDefault = null;
  private RiferimentoGruppo gruppoCreatore = null;
  private RiferimentoGruppo gruppoSupervisore = null;
  private String registrazioneStilo = null;
  private String tipoUnitaDocumentariaStilo = null;
  private String immagine = null;
  private EnteCertificatore enteCertificatore = null;
  private TipoCredenzialiFirma tipoCredenziale = null;
  private TipoOTP tipoOtp = null;
  private ProfiloFEQ profiloFEQ = null;
  private SceltaMarcaTemporale sceltaMarcaTemporale = null;

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
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: stati 
  public List<StatoPratica> getStati() {
    return stati;
  }
  public void setStati(List<StatoPratica> stati) {
    this.stati = stati;
  }

  /**
   **/
  


  // nome originario nello yaml: tipiDocumento 
  public List<TipoDocumento> getTipiDocumento() {
    return tipiDocumento;
  }
  public void setTipiDocumento(List<TipoDocumento> tipiDocumento) {
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
  public List<TrasformazioneDatiPratica> getTrasformazioni() {
    return trasformazioni;
  }
  public void setTrasformazioni(List<TrasformazioneDatiPratica> trasformazioni) {
    this.trasformazioni = trasformazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: customForm 
  public CustomForm getCustomForm() {
    return customForm;
  }
  public void setCustomForm(CustomForm customForm) {
    this.customForm = customForm;
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
  


  // nome originario nello yaml: gruppoCreatore 
  public RiferimentoGruppo getGruppoCreatore() {
    return gruppoCreatore;
  }
  public void setGruppoCreatore(RiferimentoGruppo gruppoCreatore) {
    this.gruppoCreatore = gruppoCreatore;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppoSupervisore 
  public RiferimentoGruppo getGruppoSupervisore() {
    return gruppoSupervisore;
  }
  public void setGruppoSupervisore(RiferimentoGruppo gruppoSupervisore) {
    this.gruppoSupervisore = gruppoSupervisore;
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
  


  // nome originario nello yaml: enteCertificatore 
  public EnteCertificatore getEnteCertificatore() {
    return enteCertificatore;
  }
  public void setEnteCertificatore(EnteCertificatore enteCertificatore) {
    this.enteCertificatore = enteCertificatore;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoCredenziale 
  public TipoCredenzialiFirma getTipoCredenziale() {
    return tipoCredenziale;
  }
  public void setTipoCredenziale(TipoCredenzialiFirma tipoCredenziale) {
    this.tipoCredenziale = tipoCredenziale;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoOtp 
  public TipoOTP getTipoOtp() {
    return tipoOtp;
  }
  public void setTipoOtp(TipoOTP tipoOtp) {
    this.tipoOtp = tipoOtp;
  }

  /**
   **/
  


  // nome originario nello yaml: profiloFEQ 
  public ProfiloFEQ getProfiloFEQ() {
    return profiloFEQ;
  }
  public void setProfiloFEQ(ProfiloFEQ profiloFEQ) {
    this.profiloFEQ = profiloFEQ;
  }

  /**
   **/
  


  // nome originario nello yaml: sceltaMarcaTemporale 
  public SceltaMarcaTemporale getSceltaMarcaTemporale() {
    return sceltaMarcaTemporale;
  }
  public void setSceltaMarcaTemporale(SceltaMarcaTemporale sceltaMarcaTemporale) {
    this.sceltaMarcaTemporale = sceltaMarcaTemporale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPratica tipoPratica = (TipoPratica) o;
    return Objects.equals(codice, tipoPratica.codice) &&
        Objects.equals(descrizione, tipoPratica.descrizione) &&
        Objects.equals(processDefinitionKey, tipoPratica.processDefinitionKey) &&
        Objects.equals(caseDefinitionKey, tipoPratica.caseDefinitionKey) &&
        Objects.equals(codiceApplicazioneStardas, tipoPratica.codiceApplicazioneStardas) &&
        Objects.equals(ente, tipoPratica.ente) &&
        Objects.equals(stati, tipoPratica.stati) &&
        Objects.equals(tipiDocumento, tipoPratica.tipiDocumento) &&
        Objects.equals(creabileDaInterfaccia, tipoPratica.creabileDaInterfaccia) &&
        Objects.equals(creabileDaServizio, tipoPratica.creabileDaServizio) &&
        Objects.equals(trasformazioni, tipoPratica.trasformazioni) &&
        Objects.equals(customForm, tipoPratica.customForm) &&
        Objects.equals(responsabileTrattamentoStardas, tipoPratica.responsabileTrattamentoStardas) &&
        Objects.equals(overrideResponsabileTrattamento, tipoPratica.overrideResponsabileTrattamento) &&
        Objects.equals(annullabile, tipoPratica.annullabile) &&
        Objects.equals(condivisibile, tipoPratica.condivisibile) &&
        Objects.equals(assegnabile, tipoPratica.assegnabile) &&
        Objects.equals(tabsDettaglio, tipoPratica.tabsDettaglio) &&
        Objects.equals(codiceFruitoreStardas, tipoPratica.codiceFruitoreStardas) &&
        Objects.equals(overrideFruitoreDefault, tipoPratica.overrideFruitoreDefault) &&
        Objects.equals(gruppoCreatore, tipoPratica.gruppoCreatore) &&
        Objects.equals(gruppoSupervisore, tipoPratica.gruppoSupervisore) &&
        Objects.equals(registrazioneStilo, tipoPratica.registrazioneStilo) &&
        Objects.equals(tipoUnitaDocumentariaStilo, tipoPratica.tipoUnitaDocumentariaStilo) &&
        Objects.equals(immagine, tipoPratica.immagine) &&
        Objects.equals(enteCertificatore, tipoPratica.enteCertificatore) &&
        Objects.equals(tipoCredenziale, tipoPratica.tipoCredenziale) &&
        Objects.equals(tipoOtp, tipoPratica.tipoOtp) &&
        Objects.equals(profiloFEQ, tipoPratica.profiloFEQ) &&
        Objects.equals(sceltaMarcaTemporale, tipoPratica.sceltaMarcaTemporale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, processDefinitionKey, caseDefinitionKey, codiceApplicazioneStardas, ente, stati, tipiDocumento, creabileDaInterfaccia, creabileDaServizio, trasformazioni, customForm, responsabileTrattamentoStardas, overrideResponsabileTrattamento, annullabile, condivisibile, assegnabile, tabsDettaglio, codiceFruitoreStardas, overrideFruitoreDefault, gruppoCreatore, gruppoSupervisore, registrazioneStilo, tipoUnitaDocumentariaStilo, immagine, enteCertificatore, tipoCredenziale, tipoOtp, profiloFEQ, sceltaMarcaTemporale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPratica {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    processDefinitionKey: ").append(toIndentedString(processDefinitionKey)).append("\n");
    sb.append("    caseDefinitionKey: ").append(toIndentedString(caseDefinitionKey)).append("\n");
    sb.append("    codiceApplicazioneStardas: ").append(toIndentedString(codiceApplicazioneStardas)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    stati: ").append(toIndentedString(stati)).append("\n");
    sb.append("    tipiDocumento: ").append(toIndentedString(tipiDocumento)).append("\n");
    sb.append("    creabileDaInterfaccia: ").append(toIndentedString(creabileDaInterfaccia)).append("\n");
    sb.append("    creabileDaServizio: ").append(toIndentedString(creabileDaServizio)).append("\n");
    sb.append("    trasformazioni: ").append(toIndentedString(trasformazioni)).append("\n");
    sb.append("    customForm: ").append(toIndentedString(customForm)).append("\n");
    sb.append("    responsabileTrattamentoStardas: ").append(toIndentedString(responsabileTrattamentoStardas)).append("\n");
    sb.append("    overrideResponsabileTrattamento: ").append(toIndentedString(overrideResponsabileTrattamento)).append("\n");
    sb.append("    annullabile: ").append(toIndentedString(annullabile)).append("\n");
    sb.append("    condivisibile: ").append(toIndentedString(condivisibile)).append("\n");
    sb.append("    assegnabile: ").append(toIndentedString(assegnabile)).append("\n");
    sb.append("    tabsDettaglio: ").append(toIndentedString(tabsDettaglio)).append("\n");
    sb.append("    codiceFruitoreStardas: ").append(toIndentedString(codiceFruitoreStardas)).append("\n");
    sb.append("    overrideFruitoreDefault: ").append(toIndentedString(overrideFruitoreDefault)).append("\n");
    sb.append("    gruppoCreatore: ").append(toIndentedString(gruppoCreatore)).append("\n");
    sb.append("    gruppoSupervisore: ").append(toIndentedString(gruppoSupervisore)).append("\n");
    sb.append("    registrazioneStilo: ").append(toIndentedString(registrazioneStilo)).append("\n");
    sb.append("    tipoUnitaDocumentariaStilo: ").append(toIndentedString(tipoUnitaDocumentariaStilo)).append("\n");
    sb.append("    immagine: ").append(toIndentedString(immagine)).append("\n");
    sb.append("    enteCertificatore: ").append(toIndentedString(enteCertificatore)).append("\n");
    sb.append("    tipoCredenziale: ").append(toIndentedString(tipoCredenziale)).append("\n");
    sb.append("    tipoOtp: ").append(toIndentedString(tipoOtp)).append("\n");
    sb.append("    profiloFEQ: ").append(toIndentedString(profiloFEQ)).append("\n");
    sb.append("    sceltaMarcaTemporale: ").append(toIndentedString(sceltaMarcaTemporale)).append("\n");
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

