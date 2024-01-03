/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaNotificheRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CreaNotificaRequest> notifiche = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: notifiche 
  @NotNull
  public List<CreaNotificaRequest> getNotifiche() {
    return notifiche;
  }
  public void setNotifiche(List<CreaNotificaRequest> notifiche) {
    this.notifiche = notifiche;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaNotificheRequest creaNotificheRequest = (CreaNotificheRequest) o;
    return Objects.equals(notifiche, creaNotificheRequest.notifiche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notifiche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaNotificheRequest {\n");
    
    sb.append("    notifiche: ").append(toIndentedString(notifiche)).append("\n");
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

