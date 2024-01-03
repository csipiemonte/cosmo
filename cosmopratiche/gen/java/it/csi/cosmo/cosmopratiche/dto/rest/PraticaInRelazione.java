/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PraticaInRelazione  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Pratica pratica = null;
  private TipoRelazionePraticaPratica tipoRelazione = null;

  /**
   **/
  


  // nome originario nello yaml: pratica 
  @NotNull
  public Pratica getPratica() {
    return pratica;
  }
  public void setPratica(Pratica pratica) {
    this.pratica = pratica;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoRelazione 
  @NotNull
  public TipoRelazionePraticaPratica getTipoRelazione() {
    return tipoRelazione;
  }
  public void setTipoRelazione(TipoRelazionePraticaPratica tipoRelazione) {
    this.tipoRelazione = tipoRelazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PraticaInRelazione praticaInRelazione = (PraticaInRelazione) o;
    return Objects.equals(pratica, praticaInRelazione.pratica) &&
        Objects.equals(tipoRelazione, praticaInRelazione.tipoRelazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pratica, tipoRelazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PraticaInRelazione {\n");
    
    sb.append("    pratica: ").append(toIndentedString(pratica)).append("\n");
    sb.append("    tipoRelazione: ").append(toIndentedString(tipoRelazione)).append("\n");
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

