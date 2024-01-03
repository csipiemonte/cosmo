/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoFirma;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FormatoFileProfiloFeqTipoFirma  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private ProfiloFEQ profiloFeq = null;
  private TipoFirma tipoFirma = null;

  /**
   **/
  


  // nome originario nello yaml: profiloFeq 
  @NotNull
  public ProfiloFEQ getProfiloFeq() {
    return profiloFeq;
  }
  public void setProfiloFeq(ProfiloFEQ profiloFeq) {
    this.profiloFeq = profiloFeq;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoFirma 
  @NotNull
  public TipoFirma getTipoFirma() {
    return tipoFirma;
  }
  public void setTipoFirma(TipoFirma tipoFirma) {
    this.tipoFirma = tipoFirma;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormatoFileProfiloFeqTipoFirma formatoFileProfiloFeqTipoFirma = (FormatoFileProfiloFeqTipoFirma) o;
    return Objects.equals(profiloFeq, formatoFileProfiloFeqTipoFirma.profiloFeq) &&
        Objects.equals(tipoFirma, formatoFileProfiloFeqTipoFirma.tipoFirma);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profiloFeq, tipoFirma);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormatoFileProfiloFeqTipoFirma {\n");
    
    sb.append("    profiloFeq: ").append(toIndentedString(profiloFeq)).append("\n");
    sb.append("    tipoFirma: ").append(toIndentedString(tipoFirma)).append("\n");
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

