/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TemplateDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Documento documento = null;
  private Double coordinataX = null;
  private Double coordinataY = null;
  private Long pagina = null;

  /**
   **/
  


  // nome originario nello yaml: documento 
  public Documento getDocumento() {
    return documento;
  }
  public void setDocumento(Documento documento) {
    this.documento = documento;
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
    TemplateDocumento templateDocumento = (TemplateDocumento) o;
    return Objects.equals(documento, templateDocumento.documento) &&
        Objects.equals(coordinataX, templateDocumento.coordinataX) &&
        Objects.equals(coordinataY, templateDocumento.coordinataY) &&
        Objects.equals(pagina, templateDocumento.pagina);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documento, coordinataX, coordinataY, pagina);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateDocumento {\n");
    
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
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

