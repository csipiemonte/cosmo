/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Signature;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VerifyReport  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Boolean valida = null;
  private VerifyReport child = null;
  private String conformitaParametriInput = null;
  private String errorCode = null;
  private String formatoFirma = null;
  private Integer numCertificatiFirma = null;
  private Integer numCertificatiMarca = null;
  private List<Signature> signature = new ArrayList<>();
  private String tipoFirma = null;

  /**
   **/
  


  // nome originario nello yaml: valida 
  public Boolean isValida() {
    return valida;
  }
  public void setValida(Boolean valida) {
    this.valida = valida;
  }

  /**
   **/
  


  // nome originario nello yaml: child 
  public VerifyReport getChild() {
    return child;
  }
  public void setChild(VerifyReport child) {
    this.child = child;
  }

  /**
   **/
  


  // nome originario nello yaml: conformitaParametriInput 
  public String getConformitaParametriInput() {
    return conformitaParametriInput;
  }
  public void setConformitaParametriInput(String conformitaParametriInput) {
    this.conformitaParametriInput = conformitaParametriInput;
  }

  /**
   **/
  


  // nome originario nello yaml: errorCode 
  public String getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   **/
  


  // nome originario nello yaml: formatoFirma 
  public String getFormatoFirma() {
    return formatoFirma;
  }
  public void setFormatoFirma(String formatoFirma) {
    this.formatoFirma = formatoFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: numCertificatiFirma 
  public Integer getNumCertificatiFirma() {
    return numCertificatiFirma;
  }
  public void setNumCertificatiFirma(Integer numCertificatiFirma) {
    this.numCertificatiFirma = numCertificatiFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: numCertificatiMarca 
  public Integer getNumCertificatiMarca() {
    return numCertificatiMarca;
  }
  public void setNumCertificatiMarca(Integer numCertificatiMarca) {
    this.numCertificatiMarca = numCertificatiMarca;
  }

  /**
   **/
  


  // nome originario nello yaml: signature 
  public List<Signature> getSignature() {
    return signature;
  }
  public void setSignature(List<Signature> signature) {
    this.signature = signature;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoFirma 
  public String getTipoFirma() {
    return tipoFirma;
  }
  public void setTipoFirma(String tipoFirma) {
    this.tipoFirma = tipoFirma;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerifyReport verifyReport = (VerifyReport) o;
    return Objects.equals(valida, verifyReport.valida) &&
        Objects.equals(child, verifyReport.child) &&
        Objects.equals(conformitaParametriInput, verifyReport.conformitaParametriInput) &&
        Objects.equals(errorCode, verifyReport.errorCode) &&
        Objects.equals(formatoFirma, verifyReport.formatoFirma) &&
        Objects.equals(numCertificatiFirma, verifyReport.numCertificatiFirma) &&
        Objects.equals(numCertificatiMarca, verifyReport.numCertificatiMarca) &&
        Objects.equals(signature, verifyReport.signature) &&
        Objects.equals(tipoFirma, verifyReport.tipoFirma);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valida, child, conformitaParametriInput, errorCode, formatoFirma, numCertificatiFirma, numCertificatiMarca, signature, tipoFirma);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerifyReport {\n");
    
    sb.append("    valida: ").append(toIndentedString(valida)).append("\n");
    sb.append("    child: ").append(toIndentedString(child)).append("\n");
    sb.append("    conformitaParametriInput: ").append(toIndentedString(conformitaParametriInput)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    formatoFirma: ").append(toIndentedString(formatoFirma)).append("\n");
    sb.append("    numCertificatiFirma: ").append(toIndentedString(numCertificatiFirma)).append("\n");
    sb.append("    numCertificatiMarca: ").append(toIndentedString(numCertificatiMarca)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    tipoFirma: ").append(toIndentedString(tipoFirma)).append("\n");
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

