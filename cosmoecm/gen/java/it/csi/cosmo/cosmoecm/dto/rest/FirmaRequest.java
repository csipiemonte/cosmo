/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FirmaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Documento> documenti = new ArrayList<>();
  private CertificatoFirma certificato = null;
  private String otp = null;
  private Long idAttivita = null;

  /**
   **/
  


  // nome originario nello yaml: documenti 
  public List<Documento> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<Documento> documenti) {
    this.documenti = documenti;
  }

  /**
   **/
  


  // nome originario nello yaml: certificato 
  public CertificatoFirma getCertificato() {
    return certificato;
  }
  public void setCertificato(CertificatoFirma certificato) {
    this.certificato = certificato;
  }

  /**
   **/
  


  // nome originario nello yaml: otp 
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
  }

  /**
   **/
  


  // nome originario nello yaml: idAttivita 
  public Long getIdAttivita() {
    return idAttivita;
  }
  public void setIdAttivita(Long idAttivita) {
    this.idAttivita = idAttivita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FirmaRequest firmaRequest = (FirmaRequest) o;
    return Objects.equals(documenti, firmaRequest.documenti) &&
        Objects.equals(certificato, firmaRequest.certificato) &&
        Objects.equals(otp, firmaRequest.otp) &&
        Objects.equals(idAttivita, firmaRequest.idAttivita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documenti, certificato, otp, idAttivita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FirmaRequest {\n");
    
    sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
    sb.append("    certificato: ").append(toIndentedString(certificato)).append("\n");
    sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
    sb.append("    idAttivita: ").append(toIndentedString(idAttivita)).append("\n");
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

