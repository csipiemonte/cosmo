/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaTemplateFeaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String descrizione = null;
  private Long idEnte = null;
  private String codiceTipoPratica = null;
  private String codiceTipoDocumento = null;
  private Double coordinataX = null;
  private Double coordinataY = null;
  private Long pagina = null;
  private Boolean caricatoDaTemplate = null;

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
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
  


  // nome originario nello yaml: codiceTipoPratica 
  @NotNull
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoDocumento 
  @NotNull
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: coordinataX 
  @NotNull
  public Double getCoordinataX() {
    return coordinataX;
  }
  public void setCoordinataX(Double coordinataX) {
    this.coordinataX = coordinataX;
  }

  /**
   **/
  


  // nome originario nello yaml: coordinataY 
  @NotNull
  public Double getCoordinataY() {
    return coordinataY;
  }
  public void setCoordinataY(Double coordinataY) {
    this.coordinataY = coordinataY;
  }

  /**
   **/
  


  // nome originario nello yaml: pagina 
  @NotNull
  public Long getPagina() {
    return pagina;
  }
  public void setPagina(Long pagina) {
    this.pagina = pagina;
  }

  /**
   **/
  


  // nome originario nello yaml: caricatoDaTemplate 
  @NotNull
  public Boolean isCaricatoDaTemplate() {
    return caricatoDaTemplate;
  }
  public void setCaricatoDaTemplate(Boolean caricatoDaTemplate) {
    this.caricatoDaTemplate = caricatoDaTemplate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaTemplateFeaRequest creaTemplateFeaRequest = (CreaTemplateFeaRequest) o;
    return Objects.equals(descrizione, creaTemplateFeaRequest.descrizione) &&
        Objects.equals(idEnte, creaTemplateFeaRequest.idEnte) &&
        Objects.equals(codiceTipoPratica, creaTemplateFeaRequest.codiceTipoPratica) &&
        Objects.equals(codiceTipoDocumento, creaTemplateFeaRequest.codiceTipoDocumento) &&
        Objects.equals(coordinataX, creaTemplateFeaRequest.coordinataX) &&
        Objects.equals(coordinataY, creaTemplateFeaRequest.coordinataY) &&
        Objects.equals(pagina, creaTemplateFeaRequest.pagina) &&
        Objects.equals(caricatoDaTemplate, creaTemplateFeaRequest.caricatoDaTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, idEnte, codiceTipoPratica, codiceTipoDocumento, coordinataX, coordinataY, pagina, caricatoDaTemplate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaTemplateFeaRequest {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    codiceTipoPratica: ").append(toIndentedString(codiceTipoPratica)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
    sb.append("    coordinataX: ").append(toIndentedString(coordinataX)).append("\n");
    sb.append("    coordinataY: ").append(toIndentedString(coordinataY)).append("\n");
    sb.append("    pagina: ").append(toIndentedString(pagina)).append("\n");
    sb.append("    caricatoDaTemplate: ").append(toIndentedString(caricatoDaTemplate)).append("\n");
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

