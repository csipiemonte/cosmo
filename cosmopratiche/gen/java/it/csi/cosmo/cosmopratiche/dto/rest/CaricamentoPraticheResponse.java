/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CaricamentoPraticheResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CaricamentoPratica> caricamentoPratiche = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: caricamentoPratiche 
  public List<CaricamentoPratica> getCaricamentoPratiche() {
    return caricamentoPratiche;
  }
  public void setCaricamentoPratiche(List<CaricamentoPratica> caricamentoPratiche) {
    this.caricamentoPratiche = caricamentoPratiche;
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
    CaricamentoPraticheResponse caricamentoPraticheResponse = (CaricamentoPraticheResponse) o;
    return Objects.equals(caricamentoPratiche, caricamentoPraticheResponse.caricamentoPratiche) &&
        Objects.equals(pageInfo, caricamentoPraticheResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caricamentoPratiche, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaricamentoPraticheResponse {\n");
    
    sb.append("    caricamentoPratiche: ").append(toIndentedString(caricamentoPratiche)).append("\n");
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

