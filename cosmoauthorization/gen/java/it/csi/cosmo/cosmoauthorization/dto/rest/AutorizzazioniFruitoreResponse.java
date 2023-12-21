/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AutorizzazioniFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<AutorizzazioneFruitore> autorizzazioni = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: autorizzazioni 
  public List<AutorizzazioneFruitore> getAutorizzazioni() {
    return autorizzazioni;
  }
  public void setAutorizzazioni(List<AutorizzazioneFruitore> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
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
    AutorizzazioniFruitoreResponse autorizzazioniFruitoreResponse = (AutorizzazioniFruitoreResponse) o;
    return Objects.equals(autorizzazioni, autorizzazioniFruitoreResponse.autorizzazioni) &&
        Objects.equals(pageInfo, autorizzazioniFruitoreResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autorizzazioni, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutorizzazioniFruitoreResponse {\n");
    
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
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

