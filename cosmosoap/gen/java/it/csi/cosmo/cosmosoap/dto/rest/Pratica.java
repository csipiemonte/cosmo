/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Attivita;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmosoap.dto.rest.Processo;
import it.csi.cosmo.cosmosoap.dto.rest.RiferimentoFruitore;
import it.csi.cosmo.cosmosoap.dto.rest.StatoPratica;
import it.csi.cosmo.cosmosoap.dto.rest.TipoPratica;
import it.csi.cosmo.cosmosoap.dto.rest.Variabile;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Pratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private String codiceIpaEnte = null;
  private TipoPratica tipo = null;
  private String oggetto = null;
  private StatoPratica stato = null;
  private String riassunto = null;
  private String idPraticaExt = null;
  private String linkPratica = null;
  private String linkPraticaEsterna = null;
  private String uuidNodo = null;
  private String utenteCreazionePratica = null;
  private OffsetDateTime dataCreazionePratica = null;
  private OffsetDateTime dataFinePratica = null;
  private OffsetDateTime dataCambioStato = null;
  private Boolean preferita = null;
  private OffsetDateTime dataAggiornamentoPratica = null;
  private List<Attivita> attivita = new ArrayList<>();
  private Processo processo = null;
  private RiferimentoFruitore fruitore = null;
  private String metadati = null;
  private OffsetDateTime dataCancellazione = null;
  private List<CondivisionePratica> condivisioni = new ArrayList<>();
  private List<String> visibilita = new ArrayList<>();
  private Boolean associata = null;
  private Boolean esterna = null;
  private List<Long> tags = new ArrayList<>();
  private List<Variabile> variabiliProcesso = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
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
  


  // nome originario nello yaml: tipo 
  public TipoPratica getTipo() {
    return tipo;
  }
  public void setTipo(TipoPratica tipo) {
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
  public StatoPratica getStato() {
    return stato;
  }
  public void setStato(StatoPratica stato) {
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
  


  // nome originario nello yaml: idPraticaExt 
  public String getIdPraticaExt() {
    return idPraticaExt;
  }
  public void setIdPraticaExt(String idPraticaExt) {
    this.idPraticaExt = idPraticaExt;
  }

  /**
   **/
  


  // nome originario nello yaml: linkPratica 
  public String getLinkPratica() {
    return linkPratica;
  }
  public void setLinkPratica(String linkPratica) {
    this.linkPratica = linkPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: linkPraticaEsterna 
  public String getLinkPraticaEsterna() {
    return linkPraticaEsterna;
  }
  public void setLinkPraticaEsterna(String linkPraticaEsterna) {
    this.linkPraticaEsterna = linkPraticaEsterna;
  }

  /**
   **/
  


  // nome originario nello yaml: uuidNodo 
  public String getUuidNodo() {
    return uuidNodo;
  }
  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteCreazionePratica 
  public String getUtenteCreazionePratica() {
    return utenteCreazionePratica;
  }
  public void setUtenteCreazionePratica(String utenteCreazionePratica) {
    this.utenteCreazionePratica = utenteCreazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: dataCreazionePratica 
  public OffsetDateTime getDataCreazionePratica() {
    return dataCreazionePratica;
  }
  public void setDataCreazionePratica(OffsetDateTime dataCreazionePratica) {
    this.dataCreazionePratica = dataCreazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFinePratica 
  public OffsetDateTime getDataFinePratica() {
    return dataFinePratica;
  }
  public void setDataFinePratica(OffsetDateTime dataFinePratica) {
    this.dataFinePratica = dataFinePratica;
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
  


  // nome originario nello yaml: preferita 
  public Boolean isPreferita() {
    return preferita;
  }
  public void setPreferita(Boolean preferita) {
    this.preferita = preferita;
  }

  /**
   **/
  


  // nome originario nello yaml: dataAggiornamentoPratica 
  public OffsetDateTime getDataAggiornamentoPratica() {
    return dataAggiornamentoPratica;
  }
  public void setDataAggiornamentoPratica(OffsetDateTime dataAggiornamentoPratica) {
    this.dataAggiornamentoPratica = dataAggiornamentoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public List<Attivita> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<Attivita> attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: processo 
  public Processo getProcesso() {
    return processo;
  }
  public void setProcesso(Processo processo) {
    this.processo = processo;
  }

  /**
   **/
  


  // nome originario nello yaml: fruitore 
  public RiferimentoFruitore getFruitore() {
    return fruitore;
  }
  public void setFruitore(RiferimentoFruitore fruitore) {
    this.fruitore = fruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: metadati 
  public String getMetadati() {
    return metadati;
  }
  public void setMetadati(String metadati) {
    this.metadati = metadati;
  }

  /**
   **/
  


  // nome originario nello yaml: dataCancellazione 
  public OffsetDateTime getDataCancellazione() {
    return dataCancellazione;
  }
  public void setDataCancellazione(OffsetDateTime dataCancellazione) {
    this.dataCancellazione = dataCancellazione;
  }

  /**
   **/
  


  // nome originario nello yaml: condivisioni 
  public List<CondivisionePratica> getCondivisioni() {
    return condivisioni;
  }
  public void setCondivisioni(List<CondivisionePratica> condivisioni) {
    this.condivisioni = condivisioni;
  }

  /**
   **/
  


  // nome originario nello yaml: visibilita 
  public List<String> getVisibilita() {
    return visibilita;
  }
  public void setVisibilita(List<String> visibilita) {
    this.visibilita = visibilita;
  }

  /**
   **/
  


  // nome originario nello yaml: associata 
  public Boolean isAssociata() {
    return associata;
  }
  public void setAssociata(Boolean associata) {
    this.associata = associata;
  }

  /**
   **/
  


  // nome originario nello yaml: esterna 
  public Boolean isEsterna() {
    return esterna;
  }
  public void setEsterna(Boolean esterna) {
    this.esterna = esterna;
  }

  /**
   **/
  


  // nome originario nello yaml: tags 
  public List<Long> getTags() {
    return tags;
  }
  public void setTags(List<Long> tags) {
    this.tags = tags;
  }

  /**
   **/
  


  // nome originario nello yaml: variabiliProcesso 
  public List<Variabile> getVariabiliProcesso() {
    return variabiliProcesso;
  }
  public void setVariabiliProcesso(List<Variabile> variabiliProcesso) {
    this.variabiliProcesso = variabiliProcesso;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pratica pratica = (Pratica) o;
    return Objects.equals(id, pratica.id) &&
        Objects.equals(codiceIpaEnte, pratica.codiceIpaEnte) &&
        Objects.equals(tipo, pratica.tipo) &&
        Objects.equals(oggetto, pratica.oggetto) &&
        Objects.equals(stato, pratica.stato) &&
        Objects.equals(riassunto, pratica.riassunto) &&
        Objects.equals(idPraticaExt, pratica.idPraticaExt) &&
        Objects.equals(linkPratica, pratica.linkPratica) &&
        Objects.equals(linkPraticaEsterna, pratica.linkPraticaEsterna) &&
        Objects.equals(uuidNodo, pratica.uuidNodo) &&
        Objects.equals(utenteCreazionePratica, pratica.utenteCreazionePratica) &&
        Objects.equals(dataCreazionePratica, pratica.dataCreazionePratica) &&
        Objects.equals(dataFinePratica, pratica.dataFinePratica) &&
        Objects.equals(dataCambioStato, pratica.dataCambioStato) &&
        Objects.equals(preferita, pratica.preferita) &&
        Objects.equals(dataAggiornamentoPratica, pratica.dataAggiornamentoPratica) &&
        Objects.equals(attivita, pratica.attivita) &&
        Objects.equals(processo, pratica.processo) &&
        Objects.equals(fruitore, pratica.fruitore) &&
        Objects.equals(metadati, pratica.metadati) &&
        Objects.equals(dataCancellazione, pratica.dataCancellazione) &&
        Objects.equals(condivisioni, pratica.condivisioni) &&
        Objects.equals(visibilita, pratica.visibilita) &&
        Objects.equals(associata, pratica.associata) &&
        Objects.equals(esterna, pratica.esterna) &&
        Objects.equals(tags, pratica.tags) &&
        Objects.equals(variabiliProcesso, pratica.variabiliProcesso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codiceIpaEnte, tipo, oggetto, stato, riassunto, idPraticaExt, linkPratica, linkPraticaEsterna, uuidNodo, utenteCreazionePratica, dataCreazionePratica, dataFinePratica, dataCambioStato, preferita, dataAggiornamentoPratica, attivita, processo, fruitore, metadati, dataCancellazione, condivisioni, visibilita, associata, esterna, tags, variabiliProcesso);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pratica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    idPraticaExt: ").append(toIndentedString(idPraticaExt)).append("\n");
    sb.append("    linkPratica: ").append(toIndentedString(linkPratica)).append("\n");
    sb.append("    linkPraticaEsterna: ").append(toIndentedString(linkPraticaEsterna)).append("\n");
    sb.append("    uuidNodo: ").append(toIndentedString(uuidNodo)).append("\n");
    sb.append("    utenteCreazionePratica: ").append(toIndentedString(utenteCreazionePratica)).append("\n");
    sb.append("    dataCreazionePratica: ").append(toIndentedString(dataCreazionePratica)).append("\n");
    sb.append("    dataFinePratica: ").append(toIndentedString(dataFinePratica)).append("\n");
    sb.append("    dataCambioStato: ").append(toIndentedString(dataCambioStato)).append("\n");
    sb.append("    preferita: ").append(toIndentedString(preferita)).append("\n");
    sb.append("    dataAggiornamentoPratica: ").append(toIndentedString(dataAggiornamentoPratica)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    processo: ").append(toIndentedString(processo)).append("\n");
    sb.append("    fruitore: ").append(toIndentedString(fruitore)).append("\n");
    sb.append("    metadati: ").append(toIndentedString(metadati)).append("\n");
    sb.append("    dataCancellazione: ").append(toIndentedString(dataCancellazione)).append("\n");
    sb.append("    condivisioni: ").append(toIndentedString(condivisioni)).append("\n");
    sb.append("    visibilita: ").append(toIndentedString(visibilita)).append("\n");
    sb.append("    associata: ").append(toIndentedString(associata)).append("\n");
    sb.append("    esterna: ").append(toIndentedString(esterna)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    variabiliProcesso: ").append(toIndentedString(variabiliProcesso)).append("\n");
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

