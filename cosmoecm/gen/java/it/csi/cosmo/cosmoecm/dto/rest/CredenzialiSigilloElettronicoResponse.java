/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CredenzialiSigilloElettronicoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CredenzialiSigilloElettronico> sigilliElettronici = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: sigilliElettronici 
  public List<CredenzialiSigilloElettronico> getSigilliElettronici() {
    return sigilliElettronici;
  }
  public void setSigilliElettronici(List<CredenzialiSigilloElettronico> sigilliElettronici) {
    this.sigilliElettronici = sigilliElettronici;
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
    CredenzialiSigilloElettronicoResponse credenzialiSigilloElettronicoResponse = (CredenzialiSigilloElettronicoResponse) o;
    return Objects.equals(sigilliElettronici, credenzialiSigilloElettronicoResponse.sigilliElettronici) &&
        Objects.equals(pageInfo, credenzialiSigilloElettronicoResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sigilliElettronici, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredenzialiSigilloElettronicoResponse {\n");
    
    sb.append("    sigilliElettronici: ").append(toIndentedString(sigilliElettronici)).append("\n");
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

