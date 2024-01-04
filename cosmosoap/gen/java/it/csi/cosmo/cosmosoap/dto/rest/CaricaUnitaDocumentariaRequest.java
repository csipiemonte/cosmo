/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.AttributoUnitaDocumentaria;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CaricaUnitaDocumentariaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String registrazioneStilo = null;
  private String tipoDocumentoStilo = null;
  private String oggetto = null;
  private String note = null;
  private Integer nroAllegati = null;
  private List<AttributoUnitaDocumentaria> attributi = new ArrayList<>();

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
  


  // nome originario nello yaml: tipoDocumentoStilo 
  public String getTipoDocumentoStilo() {
    return tipoDocumentoStilo;
  }
  public void setTipoDocumentoStilo(String tipoDocumentoStilo) {
    this.tipoDocumentoStilo = tipoDocumentoStilo;
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
  


  // nome originario nello yaml: note 
  public String getNote() {
    return note;
  }
  public void setNote(String note) {
    this.note = note;
  }

  /**
   **/
  


  // nome originario nello yaml: nroAllegati 
  public Integer getNroAllegati() {
    return nroAllegati;
  }
  public void setNroAllegati(Integer nroAllegati) {
    this.nroAllegati = nroAllegati;
  }

  /**
   **/
  


  // nome originario nello yaml: attributi 
  public List<AttributoUnitaDocumentaria> getAttributi() {
    return attributi;
  }
  public void setAttributi(List<AttributoUnitaDocumentaria> attributi) {
    this.attributi = attributi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaricaUnitaDocumentariaRequest caricaUnitaDocumentariaRequest = (CaricaUnitaDocumentariaRequest) o;
    return Objects.equals(registrazioneStilo, caricaUnitaDocumentariaRequest.registrazioneStilo) &&
        Objects.equals(tipoDocumentoStilo, caricaUnitaDocumentariaRequest.tipoDocumentoStilo) &&
        Objects.equals(oggetto, caricaUnitaDocumentariaRequest.oggetto) &&
        Objects.equals(note, caricaUnitaDocumentariaRequest.note) &&
        Objects.equals(nroAllegati, caricaUnitaDocumentariaRequest.nroAllegati) &&
        Objects.equals(attributi, caricaUnitaDocumentariaRequest.attributi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registrazioneStilo, tipoDocumentoStilo, oggetto, note, nroAllegati, attributi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaricaUnitaDocumentariaRequest {\n");
    
    sb.append("    registrazioneStilo: ").append(toIndentedString(registrazioneStilo)).append("\n");
    sb.append("    tipoDocumentoStilo: ").append(toIndentedString(tipoDocumentoStilo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    nroAllegati: ").append(toIndentedString(nroAllegati)).append("\n");
    sb.append("    attributi: ").append(toIndentedString(attributi)).append("\n");
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

