/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateDocumento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FirmaFeaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<TemplateDocumento> templateDocumenti = new ArrayList<>();
  private String otp = null;
  private String iconaFea = null;

  /**
   **/
  


  // nome originario nello yaml: templateDocumenti 
  @NotNull
  public List<TemplateDocumento> getTemplateDocumenti() {
    return templateDocumenti;
  }
  public void setTemplateDocumenti(List<TemplateDocumento> templateDocumenti) {
    this.templateDocumenti = templateDocumenti;
  }

  /**
   **/
  


  // nome originario nello yaml: otp 
  @NotNull
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
  }

  /**
   **/
  


  // nome originario nello yaml: iconaFea 
  @NotNull
  public String getIconaFea() {
    return iconaFea;
  }
  public void setIconaFea(String iconaFea) {
    this.iconaFea = iconaFea;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FirmaFeaRequest firmaFeaRequest = (FirmaFeaRequest) o;
    return Objects.equals(templateDocumenti, firmaFeaRequest.templateDocumenti) &&
        Objects.equals(otp, firmaFeaRequest.otp) &&
        Objects.equals(iconaFea, firmaFeaRequest.iconaFea);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateDocumenti, otp, iconaFea);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FirmaFeaRequest {\n");
    
    sb.append("    templateDocumenti: ").append(toIndentedString(templateDocumenti)).append("\n");
    sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
    sb.append("    iconaFea: ").append(toIndentedString(iconaFea)).append("\n");
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

