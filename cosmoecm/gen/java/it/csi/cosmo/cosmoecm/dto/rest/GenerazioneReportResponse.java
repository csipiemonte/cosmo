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

public class GenerazioneReportResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idDocumento = null;
  private Long idContenutoDocumento = null;
  private String url = null;

  /**
   **/
  


  // nome originario nello yaml: idDocumento 
  public Long getIdDocumento() {
    return idDocumento;
  }
  public void setIdDocumento(Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: idContenutoDocumento 
  public Long getIdContenutoDocumento() {
    return idContenutoDocumento;
  }
  public void setIdContenutoDocumento(Long idContenutoDocumento) {
    this.idContenutoDocumento = idContenutoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerazioneReportResponse generazioneReportResponse = (GenerazioneReportResponse) o;
    return Objects.equals(idDocumento, generazioneReportResponse.idDocumento) &&
        Objects.equals(idContenutoDocumento, generazioneReportResponse.idContenutoDocumento) &&
        Objects.equals(url, generazioneReportResponse.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDocumento, idContenutoDocumento, url);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerazioneReportResponse {\n");
    
    sb.append("    idDocumento: ").append(toIndentedString(idDocumento)).append("\n");
    sb.append("    idContenutoDocumento: ").append(toIndentedString(idContenutoDocumento)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

