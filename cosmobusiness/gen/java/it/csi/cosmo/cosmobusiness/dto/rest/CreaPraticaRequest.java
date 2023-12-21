/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaPraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String oggetto = null;
  private String codiceTipo = null;
  private String codiceFruitore = null;
  private String riassunto = null;
  private Long idPraticaEsistente = null;
  private List<Long> documentiDaDuplicare = new ArrayList<>();
  private Boolean daAssociare = null;

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  @NotNull
  @Size(min=1,max=255)
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipo 
  @NotNull
  @Size(min=1,max=100)
  public String getCodiceTipo() {
    return codiceTipo;
  }
  public void setCodiceTipo(String codiceTipo) {
    this.codiceTipo = codiceTipo;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFruitore 
  public String getCodiceFruitore() {
    return codiceFruitore;
  }
  public void setCodiceFruitore(String codiceFruitore) {
    this.codiceFruitore = codiceFruitore;
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
  


  // nome originario nello yaml: idPraticaEsistente 
  public Long getIdPraticaEsistente() {
    return idPraticaEsistente;
  }
  public void setIdPraticaEsistente(Long idPraticaEsistente) {
    this.idPraticaEsistente = idPraticaEsistente;
  }

  /**
   **/
  


  // nome originario nello yaml: documentiDaDuplicare 
  public List<Long> getDocumentiDaDuplicare() {
    return documentiDaDuplicare;
  }
  public void setDocumentiDaDuplicare(List<Long> documentiDaDuplicare) {
    this.documentiDaDuplicare = documentiDaDuplicare;
  }

  /**
   **/
  


  // nome originario nello yaml: daAssociare 
  public Boolean isDaAssociare() {
    return daAssociare;
  }
  public void setDaAssociare(Boolean daAssociare) {
    this.daAssociare = daAssociare;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaPraticaRequest creaPraticaRequest = (CreaPraticaRequest) o;
    return Objects.equals(oggetto, creaPraticaRequest.oggetto) &&
        Objects.equals(codiceTipo, creaPraticaRequest.codiceTipo) &&
        Objects.equals(codiceFruitore, creaPraticaRequest.codiceFruitore) &&
        Objects.equals(riassunto, creaPraticaRequest.riassunto) &&
        Objects.equals(idPraticaEsistente, creaPraticaRequest.idPraticaEsistente) &&
        Objects.equals(documentiDaDuplicare, creaPraticaRequest.documentiDaDuplicare) &&
        Objects.equals(daAssociare, creaPraticaRequest.daAssociare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oggetto, codiceTipo, codiceFruitore, riassunto, idPraticaEsistente, documentiDaDuplicare, daAssociare);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaPraticaRequest {\n");
    
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    codiceFruitore: ").append(toIndentedString(codiceFruitore)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    idPraticaEsistente: ").append(toIndentedString(idPraticaEsistente)).append("\n");
    sb.append("    documentiDaDuplicare: ").append(toIndentedString(documentiDaDuplicare)).append("\n");
    sb.append("    daAssociare: ").append(toIndentedString(daAssociare)).append("\n");
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

