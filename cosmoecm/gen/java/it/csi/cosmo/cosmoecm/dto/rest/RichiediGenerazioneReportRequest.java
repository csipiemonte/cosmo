/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportInputUtenteRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RichiediGenerazioneReportRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private String codiceTemplate = null;
  private String codiceTipoDocumento = null;
  private String mappaturaParametri = null;
  private String nomeFile = null;
  private String titolo = null;
  private String autore = null;
  private String formato = null;
  private Boolean sovrascrivi = null;
  private List<RichiediGenerazioneReportInputUtenteRequest> inputUtente = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTemplate 
  public String getCodiceTemplate() {
    return codiceTemplate;
  }
  public void setCodiceTemplate(String codiceTemplate) {
    this.codiceTemplate = codiceTemplate;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoDocumento 
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaParametri 
  public String getMappaturaParametri() {
    return mappaturaParametri;
  }
  public void setMappaturaParametri(String mappaturaParametri) {
    this.mappaturaParametri = mappaturaParametri;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: titolo 
  public String getTitolo() {
    return titolo;
  }
  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  /**
   **/
  


  // nome originario nello yaml: autore 
  public String getAutore() {
    return autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }

  /**
   **/
  


  // nome originario nello yaml: formato 
  public String getFormato() {
    return formato;
  }
  public void setFormato(String formato) {
    this.formato = formato;
  }

  /**
   **/
  


  // nome originario nello yaml: sovrascrivi 
  public Boolean isSovrascrivi() {
    return sovrascrivi;
  }
  public void setSovrascrivi(Boolean sovrascrivi) {
    this.sovrascrivi = sovrascrivi;
  }

  /**
   **/
  


  // nome originario nello yaml: inputUtente 
  public List<RichiediGenerazioneReportInputUtenteRequest> getInputUtente() {
    return inputUtente;
  }
  public void setInputUtente(List<RichiediGenerazioneReportInputUtenteRequest> inputUtente) {
    this.inputUtente = inputUtente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichiediGenerazioneReportRequest richiediGenerazioneReportRequest = (RichiediGenerazioneReportRequest) o;
    return Objects.equals(idPratica, richiediGenerazioneReportRequest.idPratica) &&
        Objects.equals(codiceTemplate, richiediGenerazioneReportRequest.codiceTemplate) &&
        Objects.equals(codiceTipoDocumento, richiediGenerazioneReportRequest.codiceTipoDocumento) &&
        Objects.equals(mappaturaParametri, richiediGenerazioneReportRequest.mappaturaParametri) &&
        Objects.equals(nomeFile, richiediGenerazioneReportRequest.nomeFile) &&
        Objects.equals(titolo, richiediGenerazioneReportRequest.titolo) &&
        Objects.equals(autore, richiediGenerazioneReportRequest.autore) &&
        Objects.equals(formato, richiediGenerazioneReportRequest.formato) &&
        Objects.equals(sovrascrivi, richiediGenerazioneReportRequest.sovrascrivi) &&
        Objects.equals(inputUtente, richiediGenerazioneReportRequest.inputUtente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, codiceTemplate, codiceTipoDocumento, mappaturaParametri, nomeFile, titolo, autore, formato, sovrascrivi, inputUtente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichiediGenerazioneReportRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    codiceTemplate: ").append(toIndentedString(codiceTemplate)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
    sb.append("    mappaturaParametri: ").append(toIndentedString(mappaturaParametri)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    formato: ").append(toIndentedString(formato)).append("\n");
    sb.append("    sovrascrivi: ").append(toIndentedString(sovrascrivi)).append("\n");
    sb.append("    inputUtente: ").append(toIndentedString(inputUtente)).append("\n");
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

