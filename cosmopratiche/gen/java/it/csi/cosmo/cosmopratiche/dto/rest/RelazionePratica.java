/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RelazionePratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idPraticaExtA = null;
  private String tipoRelazione = null;
  private OffsetDateTime dtInizioValidita = null;
  private OffsetDateTime dtFineValidita = null;

  /**
   * id esterno della pratica verso cui è diretta una relazione
   **/
  


  // nome originario nello yaml: idPraticaExtA 
  @NotNull
  public String getIdPraticaExtA() {
    return idPraticaExtA;
  }
  public void setIdPraticaExtA(String idPraticaExtA) {
    this.idPraticaExtA = idPraticaExtA;
  }

  /**
   * tipo della relazione
   **/
  


  // nome originario nello yaml: tipoRelazione 
  @NotNull
  public String getTipoRelazione() {
    return tipoRelazione;
  }
  public void setTipoRelazione(String tipoRelazione) {
    this.tipoRelazione = tipoRelazione;
  }

  /**
   * data di inizio validità della relazione
   **/
  


  // nome originario nello yaml: dtInizioValidita 
  public OffsetDateTime getDtInizioValidita() {
    return dtInizioValidita;
  }
  public void setDtInizioValidita(OffsetDateTime dtInizioValidita) {
    this.dtInizioValidita = dtInizioValidita;
  }

  /**
   * data di fine validità della relazione
   **/
  


  // nome originario nello yaml: dtFineValidita 
  public OffsetDateTime getDtFineValidita() {
    return dtFineValidita;
  }
  public void setDtFineValidita(OffsetDateTime dtFineValidita) {
    this.dtFineValidita = dtFineValidita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelazionePratica relazionePratica = (RelazionePratica) o;
    return Objects.equals(idPraticaExtA, relazionePratica.idPraticaExtA) &&
        Objects.equals(tipoRelazione, relazionePratica.tipoRelazione) &&
        Objects.equals(dtInizioValidita, relazionePratica.dtInizioValidita) &&
        Objects.equals(dtFineValidita, relazionePratica.dtFineValidita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPraticaExtA, tipoRelazione, dtInizioValidita, dtFineValidita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelazionePratica {\n");
    
    sb.append("    idPraticaExtA: ").append(toIndentedString(idPraticaExtA)).append("\n");
    sb.append("    tipoRelazione: ").append(toIndentedString(tipoRelazione)).append("\n");
    sb.append("    dtInizioValidita: ").append(toIndentedString(dtInizioValidita)).append("\n");
    sb.append("    dtFineValidita: ").append(toIndentedString(dtFineValidita)).append("\n");
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

