/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EstremiRegNumType  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String categoriaReg = null;
  private String siglaReg = null;
  private Integer annoReg = null;
  private Integer numReg = null;
  private OffsetDateTime dataOraReg = null;

  /**
   **/
  


  // nome originario nello yaml: categoriaReg 
  public String getCategoriaReg() {
    return categoriaReg;
  }
  public void setCategoriaReg(String categoriaReg) {
    this.categoriaReg = categoriaReg;
  }

  /**
   **/
  


  // nome originario nello yaml: siglaReg 
  public String getSiglaReg() {
    return siglaReg;
  }
  public void setSiglaReg(String siglaReg) {
    this.siglaReg = siglaReg;
  }

  /**
   **/
  


  // nome originario nello yaml: annoReg 
  public Integer getAnnoReg() {
    return annoReg;
  }
  public void setAnnoReg(Integer annoReg) {
    this.annoReg = annoReg;
  }

  /**
   **/
  


  // nome originario nello yaml: numReg 
  public Integer getNumReg() {
    return numReg;
  }
  public void setNumReg(Integer numReg) {
    this.numReg = numReg;
  }

  /**
   **/
  


  // nome originario nello yaml: dataOraReg 
  public OffsetDateTime getDataOraReg() {
    return dataOraReg;
  }
  public void setDataOraReg(OffsetDateTime dataOraReg) {
    this.dataOraReg = dataOraReg;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstremiRegNumType estremiRegNumType = (EstremiRegNumType) o;
    return Objects.equals(categoriaReg, estremiRegNumType.categoriaReg) &&
        Objects.equals(siglaReg, estremiRegNumType.siglaReg) &&
        Objects.equals(annoReg, estremiRegNumType.annoReg) &&
        Objects.equals(numReg, estremiRegNumType.numReg) &&
        Objects.equals(dataOraReg, estremiRegNumType.dataOraReg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoriaReg, siglaReg, annoReg, numReg, dataOraReg);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstremiRegNumType {\n");
    
    sb.append("    categoriaReg: ").append(toIndentedString(categoriaReg)).append("\n");
    sb.append("    siglaReg: ").append(toIndentedString(siglaReg)).append("\n");
    sb.append("    annoReg: ").append(toIndentedString(annoReg)).append("\n");
    sb.append("    numReg: ").append(toIndentedString(numReg)).append("\n");
    sb.append("    dataOraReg: ").append(toIndentedString(dataOraReg)).append("\n");
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

