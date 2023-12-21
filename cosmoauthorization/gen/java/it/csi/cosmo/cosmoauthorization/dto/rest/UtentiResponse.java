/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UtentiResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Utente> utenti = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: utenti 
  public List<Utente> getUtenti() {
    return utenti;
  }
  public void setUtenti(List<Utente> utenti) {
    this.utenti = utenti;
  }

  /**
   **/
  


  // nome originario nello yaml: pageInfo 
  public PageInfo getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtentiResponse utentiResponse = (UtentiResponse) o;
    return Objects.equals(utenti, utentiResponse.utenti) &&
        Objects.equals(pageInfo, utentiResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utenti, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtentiResponse {\n");
    
    sb.append("    utenti: ").append(toIndentedString(utenti)).append("\n");
    sb.append("    pageInfo: ").append(toIndentedString(pageInfo)).append("\n");
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

