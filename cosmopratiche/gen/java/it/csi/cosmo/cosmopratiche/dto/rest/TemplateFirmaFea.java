/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TemplateFirmaFea  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String descrizione = null;
  private String codiceTipoDocumento = null;
  private Double coordinataX = null;
  private Double coordinataY = null;
  private Long pagina = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateFirmaFea templateFirmaFea = (TemplateFirmaFea) o;
    return Objects.equals(descrizione, templateFirmaFea.descrizione) &&
        Objects.equals(codiceTipoDocumento, templateFirmaFea.codiceTipoDocumento) &&
        Objects.equals(coordinataX, templateFirmaFea.coordinataX) &&
        Objects.equals(coordinataY, templateFirmaFea.coordinataY) &&
        Objects.equals(pagina, templateFirmaFea.pagina);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, codiceTipoDocumento, coordinataX, coordinataY, pagina);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateFirmaFea {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
    sb.append("    coordinataX: ").append(toIndentedString(coordinataX)).append("\n");
    sb.append("    coordinataY: ").append(toIndentedString(coordinataY)).append("\n");
    sb.append("    pagina: ").append(toIndentedString(pagina)).append("\n");
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

