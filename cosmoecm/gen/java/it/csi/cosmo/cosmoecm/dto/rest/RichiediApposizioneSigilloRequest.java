/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RichiediApposizioneSigilloRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String identificativoMessaggio = null;
  private List<CodiceTipologiaDocumento> tipiDocumento = new ArrayList<>();
  private String identificativoAlias = null;

  /**
   **/
  


  // nome originario nello yaml: identificativoMessaggio 
  public String getIdentificativoMessaggio() {
    return identificativoMessaggio;
  }
  public void setIdentificativoMessaggio(String identificativoMessaggio) {
    this.identificativoMessaggio = identificativoMessaggio;
  }

  /**
   **/
  


  // nome originario nello yaml: tipiDocumento 
  public List<CodiceTipologiaDocumento> getTipiDocumento() {
    return tipiDocumento;
  }
  public void setTipiDocumento(List<CodiceTipologiaDocumento> tipiDocumento) {
    this.tipiDocumento = tipiDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoAlias 
  public String getIdentificativoAlias() {
    return identificativoAlias;
  }
  public void setIdentificativoAlias(String identificativoAlias) {
    this.identificativoAlias = identificativoAlias;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichiediApposizioneSigilloRequest richiediApposizioneSigilloRequest = (RichiediApposizioneSigilloRequest) o;
    return Objects.equals(identificativoMessaggio, richiediApposizioneSigilloRequest.identificativoMessaggio) &&
        Objects.equals(tipiDocumento, richiediApposizioneSigilloRequest.tipiDocumento) &&
        Objects.equals(identificativoAlias, richiediApposizioneSigilloRequest.identificativoAlias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificativoMessaggio, tipiDocumento, identificativoAlias);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichiediApposizioneSigilloRequest {\n");
    
    sb.append("    identificativoMessaggio: ").append(toIndentedString(identificativoMessaggio)).append("\n");
    sb.append("    tipiDocumento: ").append(toIndentedString(tipiDocumento)).append("\n");
    sb.append("    identificativoAlias: ").append(toIndentedString(identificativoAlias)).append("\n");
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

