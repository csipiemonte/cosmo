/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaFileRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String uuidPratica = null;
  private String uuidContenutoTemporaneo = null;
  private LocalDate dtInserimento = null;
  private String descrizione = null;
  private String codiceTipo = null;
  private Long id = null;
  private String link = null;

  /**
   **/
  


  // nome originario nello yaml: uuidPratica 
  @NotNull
  public String getUuidPratica() {
    return uuidPratica;
  }
  public void setUuidPratica(String uuidPratica) {
    this.uuidPratica = uuidPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: uuidContenutoTemporaneo 
  @NotNull
  public String getUuidContenutoTemporaneo() {
    return uuidContenutoTemporaneo;
  }
  public void setUuidContenutoTemporaneo(String uuidContenutoTemporaneo) {
    this.uuidContenutoTemporaneo = uuidContenutoTemporaneo;
  }

  /**
   **/
  


  // nome originario nello yaml: dtInserimento 
  @NotNull
  public LocalDate getDtInserimento() {
    return dtInserimento;
  }
  public void setDtInserimento(LocalDate dtInserimento) {
    this.dtInserimento = dtInserimento;
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
  


  // nome originario nello yaml: codiceTipo 
  public String getCodiceTipo() {
    return codiceTipo;
  }
  public void setCodiceTipo(String codiceTipo) {
    this.codiceTipo = codiceTipo;
  }

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: link 
  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaFileRequest creaFileRequest = (CreaFileRequest) o;
    return Objects.equals(uuidPratica, creaFileRequest.uuidPratica) &&
        Objects.equals(uuidContenutoTemporaneo, creaFileRequest.uuidContenutoTemporaneo) &&
        Objects.equals(dtInserimento, creaFileRequest.dtInserimento) &&
        Objects.equals(descrizione, creaFileRequest.descrizione) &&
        Objects.equals(codiceTipo, creaFileRequest.codiceTipo) &&
        Objects.equals(id, creaFileRequest.id) &&
        Objects.equals(link, creaFileRequest.link);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuidPratica, uuidContenutoTemporaneo, dtInserimento, descrizione, codiceTipo, id, link);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaFileRequest {\n");
    
    sb.append("    uuidPratica: ").append(toIndentedString(uuidPratica)).append("\n");
    sb.append("    uuidContenutoTemporaneo: ").append(toIndentedString(uuidContenutoTemporaneo)).append("\n");
    sb.append("    dtInserimento: ").append(toIndentedString(dtInserimento)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

