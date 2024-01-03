/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CampiTecnici  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private OffsetDateTime dtFineVal = null;
  private OffsetDateTime dtIniVal = null;
  private OffsetDateTime dtModifica = null;

  /**
   **/
  


  // nome originario nello yaml: dtFineVal 
  public OffsetDateTime getDtFineVal() {
    return dtFineVal;
  }
  public void setDtFineVal(OffsetDateTime dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  /**
   **/
  


  // nome originario nello yaml: dtIniVal 
  public OffsetDateTime getDtIniVal() {
    return dtIniVal;
  }
  public void setDtIniVal(OffsetDateTime dtIniVal) {
    this.dtIniVal = dtIniVal;
  }

  /**
   **/
  


  // nome originario nello yaml: dtModifica 
  public OffsetDateTime getDtModifica() {
    return dtModifica;
  }
  public void setDtModifica(OffsetDateTime dtModifica) {
    this.dtModifica = dtModifica;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CampiTecnici campiTecnici = (CampiTecnici) o;
    return Objects.equals(dtFineVal, campiTecnici.dtFineVal) &&
        Objects.equals(dtIniVal, campiTecnici.dtIniVal) &&
        Objects.equals(dtModifica, campiTecnici.dtModifica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dtFineVal, dtIniVal, dtModifica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampiTecnici {\n");
    
    sb.append("    dtFineVal: ").append(toIndentedString(dtFineVal)).append("\n");
    sb.append("    dtIniVal: ").append(toIndentedString(dtIniVal)).append("\n");
    sb.append("    dtModifica: ").append(toIndentedString(dtModifica)).append("\n");
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

