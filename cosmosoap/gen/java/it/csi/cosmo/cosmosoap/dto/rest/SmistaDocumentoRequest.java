/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.ConfigurazioneChiamante;
import it.csi.cosmo.cosmosoap.dto.rest.DatiSmistaDocumento;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SmistaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private ConfigurazioneChiamante configurazioneChiamante = null;
  private DatiSmistaDocumento datiSmistaDocumento = null;

  /**
   **/
  


  // nome originario nello yaml: configurazioneChiamante 
  @NotNull
  public ConfigurazioneChiamante getConfigurazioneChiamante() {
    return configurazioneChiamante;
  }
  public void setConfigurazioneChiamante(ConfigurazioneChiamante configurazioneChiamante) {
    this.configurazioneChiamante = configurazioneChiamante;
  }

  /**
   **/
  


  // nome originario nello yaml: datiSmistaDocumento 
  @NotNull
  public DatiSmistaDocumento getDatiSmistaDocumento() {
    return datiSmistaDocumento;
  }
  public void setDatiSmistaDocumento(DatiSmistaDocumento datiSmistaDocumento) {
    this.datiSmistaDocumento = datiSmistaDocumento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmistaDocumentoRequest smistaDocumentoRequest = (SmistaDocumentoRequest) o;
    return Objects.equals(configurazioneChiamante, smistaDocumentoRequest.configurazioneChiamante) &&
        Objects.equals(datiSmistaDocumento, smistaDocumentoRequest.datiSmistaDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configurazioneChiamante, datiSmistaDocumento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmistaDocumentoRequest {\n");
    
    sb.append("    configurazioneChiamante: ").append(toIndentedString(configurazioneChiamante)).append("\n");
    sb.append("    datiSmistaDocumento: ").append(toIndentedString(datiSmistaDocumento)).append("\n");
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

