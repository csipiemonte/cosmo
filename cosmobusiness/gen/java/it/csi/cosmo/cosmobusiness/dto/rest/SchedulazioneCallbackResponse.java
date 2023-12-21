/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoCallback;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SchedulazioneCallbackResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String uuid = null;
  private StatoCallback stato = null;

  /**
   **/
  


  // nome originario nello yaml: uuid 
  @NotNull
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  @NotNull
  public StatoCallback getStato() {
    return stato;
  }
  public void setStato(StatoCallback stato) {
    this.stato = stato;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SchedulazioneCallbackResponse schedulazioneCallbackResponse = (SchedulazioneCallbackResponse) o;
    return Objects.equals(uuid, schedulazioneCallbackResponse.uuid) &&
        Objects.equals(stato, schedulazioneCallbackResponse.stato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, stato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SchedulazioneCallbackResponse {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
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

