/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InformazioniPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String idPraticaExt = null;
  private String codiceIpaEnte = null;
  private String descrizione = null;
  private TipoPraticaFruitore tipo = null;
  private String oggetto = null;
  private StatoPraticaFruitore stato = null;
  private String riassunto = null;
  private UtenteFruitore utenteCreazione = null;
  private OffsetDateTime dataCreazione = null;
  private OffsetDateTime dataFine = null;
  private OffsetDateTime dataCambioStato = null;
  private OffsetDateTime dataAggiornamento = null;
  private Object metadati = null;
  private List<AttivitaFruitore> attivita = new ArrayList<>();
  private List<MessaggioFruitore> commenti = new ArrayList<>();
  private DocumentoFruitore documentoPerSmistamento = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: idPraticaExt 
  public String getIdPraticaExt() {
    return idPraticaExt;
  }
  public void setIdPraticaExt(String idPraticaExt) {
    this.idPraticaExt = idPraticaExt;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: tipo 
  public TipoPraticaFruitore getTipo() {
    return tipo;
  }
  public void setTipo(TipoPraticaFruitore tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  public StatoPraticaFruitore getStato() {
    return stato;
  }
  public void setStato(StatoPraticaFruitore stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: riassunto 
  public String getRiassunto() {
    return riassunto;
  }
  public void setRiassunto(String riassunto) {
    this.riassunto = riassunto;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteCreazione 
  public UtenteFruitore getUtenteCreazione() {
    return utenteCreazione;
  }
  public void setUtenteCreazione(UtenteFruitore utenteCreazione) {
    this.utenteCreazione = utenteCreazione;
  }

  /**
   **/
  


  // nome originario nello yaml: dataCreazione 
  public OffsetDateTime getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(OffsetDateTime dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFine 
  public OffsetDateTime getDataFine() {
    return dataFine;
  }
  public void setDataFine(OffsetDateTime dataFine) {
    this.dataFine = dataFine;
  }

  /**
   **/
  


  // nome originario nello yaml: dataCambioStato 
  public OffsetDateTime getDataCambioStato() {
    return dataCambioStato;
  }
  public void setDataCambioStato(OffsetDateTime dataCambioStato) {
    this.dataCambioStato = dataCambioStato;
  }

  /**
   **/
  


  // nome originario nello yaml: dataAggiornamento 
  public OffsetDateTime getDataAggiornamento() {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(OffsetDateTime dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  /**
   * metadati della pratica in formato libero
   **/
  


  // nome originario nello yaml: metadati 
  public Object getMetadati() {
    return metadati;
  }
  public void setMetadati(Object metadati) {
    this.metadati = metadati;
  }

  /**
   * array di tutte le attività della pratica in ordine cronologico, le attività concluse hanno dataFine not null
   **/
  


  // nome originario nello yaml: attivita 
  public List<AttivitaFruitore> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<AttivitaFruitore> attivita) {
    this.attivita = attivita;
  }

  /**
   * array in ordine cronologico dei commenti della pratica
   **/
  


  // nome originario nello yaml: commenti 
  public List<MessaggioFruitore> getCommenti() {
    return commenti;
  }
  public void setCommenti(List<MessaggioFruitore> commenti) {
    this.commenti = commenti;
  }

  /**
   **/
  


  // nome originario nello yaml: documentoPerSmistamento 
  public DocumentoFruitore getDocumentoPerSmistamento() {
    return documentoPerSmistamento;
  }
  public void setDocumentoPerSmistamento(DocumentoFruitore documentoPerSmistamento) {
    this.documentoPerSmistamento = documentoPerSmistamento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InformazioniPratica informazioniPratica = (InformazioniPratica) o;
    return Objects.equals(id, informazioniPratica.id) &&
        Objects.equals(idPraticaExt, informazioniPratica.idPraticaExt) &&
        Objects.equals(codiceIpaEnte, informazioniPratica.codiceIpaEnte) &&
        Objects.equals(descrizione, informazioniPratica.descrizione) &&
        Objects.equals(tipo, informazioniPratica.tipo) &&
        Objects.equals(oggetto, informazioniPratica.oggetto) &&
        Objects.equals(stato, informazioniPratica.stato) &&
        Objects.equals(riassunto, informazioniPratica.riassunto) &&
        Objects.equals(utenteCreazione, informazioniPratica.utenteCreazione) &&
        Objects.equals(dataCreazione, informazioniPratica.dataCreazione) &&
        Objects.equals(dataFine, informazioniPratica.dataFine) &&
        Objects.equals(dataCambioStato, informazioniPratica.dataCambioStato) &&
        Objects.equals(dataAggiornamento, informazioniPratica.dataAggiornamento) &&
        Objects.equals(metadati, informazioniPratica.metadati) &&
        Objects.equals(attivita, informazioniPratica.attivita) &&
        Objects.equals(commenti, informazioniPratica.commenti) &&
        Objects.equals(documentoPerSmistamento, informazioniPratica.documentoPerSmistamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idPraticaExt, codiceIpaEnte, descrizione, tipo, oggetto, stato, riassunto, utenteCreazione, dataCreazione, dataFine, dataCambioStato, dataAggiornamento, metadati, attivita, commenti, documentoPerSmistamento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InformazioniPratica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idPraticaExt: ").append(toIndentedString(idPraticaExt)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    utenteCreazione: ").append(toIndentedString(utenteCreazione)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    dataCambioStato: ").append(toIndentedString(dataCambioStato)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    metadati: ").append(toIndentedString(metadati)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    commenti: ").append(toIndentedString(commenti)).append("\n");
    sb.append("    documentoPerSmistamento: ").append(toIndentedString(documentoPerSmistamento)).append("\n");
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

