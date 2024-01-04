/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.PageInfo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioneClassificazioni;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RegistrazioniClassificazioni  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private PageInfo pageInfo = null;
  private List<RegistrazioneClassificazioni> registrazioni = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: pageInfo 
  public PageInfo getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  /**
   **/
  


  // nome originario nello yaml: registrazioni 
  public List<RegistrazioneClassificazioni> getRegistrazioni() {
    return registrazioni;
  }
  public void setRegistrazioni(List<RegistrazioneClassificazioni> registrazioni) {
    this.registrazioni = registrazioni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrazioniClassificazioni registrazioniClassificazioni = (RegistrazioniClassificazioni) o;
    return Objects.equals(pageInfo, registrazioniClassificazioni.pageInfo) &&
        Objects.equals(registrazioni, registrazioniClassificazioni.registrazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageInfo, registrazioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistrazioniClassificazioni {\n");
    
    sb.append("    pageInfo: ").append(toIndentedString(pageInfo)).append("\n");
    sb.append("    registrazioni: ").append(toIndentedString(registrazioni)).append("\n");
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

