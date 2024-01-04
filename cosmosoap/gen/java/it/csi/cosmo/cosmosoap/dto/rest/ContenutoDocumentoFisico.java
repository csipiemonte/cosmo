/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ContenutoDocumentoFisico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private byte[] stream = null;
  private String mimetype = null;
  private String filename = null;

  /**
   **/
  


  // nome originario nello yaml: stream 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getStream() {
    return stream;
  }
  public void setStream(byte[] stream) {
    this.stream = stream;
  }

  /**
   **/
  


  // nome originario nello yaml: mimetype 
  public String getMimetype() {
    return mimetype;
  }
  public void setMimetype(String mimetype) {
    this.mimetype = mimetype;
  }

  /**
   **/
  


  // nome originario nello yaml: filename 
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContenutoDocumentoFisico contenutoDocumentoFisico = (ContenutoDocumentoFisico) o;
    return Objects.equals(stream, contenutoDocumentoFisico.stream) &&
        Objects.equals(mimetype, contenutoDocumentoFisico.mimetype) &&
        Objects.equals(filename, contenutoDocumentoFisico.filename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stream, mimetype, filename);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContenutoDocumentoFisico {\n");
    
    sb.append("    stream: ").append(toIndentedString(stream)).append("\n");
    sb.append("    mimetype: ").append(toIndentedString(mimetype)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
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

