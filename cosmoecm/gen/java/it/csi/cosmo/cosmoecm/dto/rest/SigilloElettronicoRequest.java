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

public class SigilloElettronicoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CodiceTipologiaDocumento> tipiDocumento = new ArrayList<>();
  private String alias = null;
  private Long idPratica = null;
  private String identificativoMessaggio = null;

  /**
   **/
  


  // nome originario nello yaml: tipiDocumento 
  @NotNull
  public List<CodiceTipologiaDocumento> getTipiDocumento() {
    return tipiDocumento;
  }
  public void setTipiDocumento(List<CodiceTipologiaDocumento> tipiDocumento) {
    this.tipiDocumento = tipiDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: alias 
  @NotNull
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoMessaggio 
  @NotNull
  public String getIdentificativoMessaggio() {
    return identificativoMessaggio;
  }
  public void setIdentificativoMessaggio(String identificativoMessaggio) {
    this.identificativoMessaggio = identificativoMessaggio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SigilloElettronicoRequest sigilloElettronicoRequest = (SigilloElettronicoRequest) o;
    return Objects.equals(tipiDocumento, sigilloElettronicoRequest.tipiDocumento) &&
        Objects.equals(alias, sigilloElettronicoRequest.alias) &&
        Objects.equals(idPratica, sigilloElettronicoRequest.idPratica) &&
        Objects.equals(identificativoMessaggio, sigilloElettronicoRequest.identificativoMessaggio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipiDocumento, alias, idPratica, identificativoMessaggio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SigilloElettronicoRequest {\n");
    
    sb.append("    tipiDocumento: ").append(toIndentedString(tipiDocumento)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    identificativoMessaggio: ").append(toIndentedString(identificativoMessaggio)).append("\n");
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

