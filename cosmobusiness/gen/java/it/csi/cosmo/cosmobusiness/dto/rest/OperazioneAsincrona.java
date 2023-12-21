/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class OperazioneAsincrona  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String uuid = null;
  private String nome = null;
  private String stato = null;
  private Long versione = null;
  private Object risultato = null;
  private OffsetDateTime dataAvvio = null;
  private OffsetDateTime dataFine = null;
  private String errore = null;
  private String dettagliErrore = null;
  private List<MessaggioOperazioneAsincrona> messaggi = new ArrayList<>();
  private List<OperazioneAsincrona> steps = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: uuid 
  @NotNull
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   **/
  


  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  @NotNull
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: versione 
  @NotNull
  public Long getVersione() {
    return versione;
  }
  public void setVersione(Long versione) {
    this.versione = versione;
  }

  /**
   **/
  


  // nome originario nello yaml: risultato 
  public Object getRisultato() {
    return risultato;
  }
  public void setRisultato(Object risultato) {
    this.risultato = risultato;
  }

  /**
   **/
  


  // nome originario nello yaml: dataAvvio 
  public OffsetDateTime getDataAvvio() {
    return dataAvvio;
  }
  public void setDataAvvio(OffsetDateTime dataAvvio) {
    this.dataAvvio = dataAvvio;
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
  


  // nome originario nello yaml: errore 
  public String getErrore() {
    return errore;
  }
  public void setErrore(String errore) {
    this.errore = errore;
  }

  /**
   **/
  


  // nome originario nello yaml: dettagliErrore 
  public String getDettagliErrore() {
    return dettagliErrore;
  }
  public void setDettagliErrore(String dettagliErrore) {
    this.dettagliErrore = dettagliErrore;
  }

  /**
   **/
  


  // nome originario nello yaml: messaggi 
  public List<MessaggioOperazioneAsincrona> getMessaggi() {
    return messaggi;
  }
  public void setMessaggi(List<MessaggioOperazioneAsincrona> messaggi) {
    this.messaggi = messaggi;
  }

  /**
   **/
  


  // nome originario nello yaml: steps 
  public List<OperazioneAsincrona> getSteps() {
    return steps;
  }
  public void setSteps(List<OperazioneAsincrona> steps) {
    this.steps = steps;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperazioneAsincrona operazioneAsincrona = (OperazioneAsincrona) o;
    return Objects.equals(uuid, operazioneAsincrona.uuid) &&
        Objects.equals(nome, operazioneAsincrona.nome) &&
        Objects.equals(stato, operazioneAsincrona.stato) &&
        Objects.equals(versione, operazioneAsincrona.versione) &&
        Objects.equals(risultato, operazioneAsincrona.risultato) &&
        Objects.equals(dataAvvio, operazioneAsincrona.dataAvvio) &&
        Objects.equals(dataFine, operazioneAsincrona.dataFine) &&
        Objects.equals(errore, operazioneAsincrona.errore) &&
        Objects.equals(dettagliErrore, operazioneAsincrona.dettagliErrore) &&
        Objects.equals(messaggi, operazioneAsincrona.messaggi) &&
        Objects.equals(steps, operazioneAsincrona.steps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, nome, stato, versione, risultato, dataAvvio, dataFine, errore, dettagliErrore, messaggi, steps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazioneAsincrona {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
    sb.append("    risultato: ").append(toIndentedString(risultato)).append("\n");
    sb.append("    dataAvvio: ").append(toIndentedString(dataAvvio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    errore: ").append(toIndentedString(errore)).append("\n");
    sb.append("    dettagliErrore: ").append(toIndentedString(dettagliErrore)).append("\n");
    sb.append("    messaggi: ").append(toIndentedString(messaggi)).append("\n");
    sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
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

