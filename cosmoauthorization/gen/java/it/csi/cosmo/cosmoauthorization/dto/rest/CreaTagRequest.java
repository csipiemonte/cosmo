/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTipoTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoEnte;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaTagRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private AggiornaTipoTagRequest tipoTag = null;
  private RiferimentoEnte ente = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoTag 
  public AggiornaTipoTagRequest getTipoTag() {
    return tipoTag;
  }
  public void setTipoTag(AggiornaTipoTagRequest tipoTag) {
    this.tipoTag = tipoTag;
  }

  /**
   **/
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaTagRequest creaTagRequest = (CreaTagRequest) o;
    return Objects.equals(codice, creaTagRequest.codice) &&
        Objects.equals(descrizione, creaTagRequest.descrizione) &&
        Objects.equals(tipoTag, creaTagRequest.tipoTag) &&
        Objects.equals(ente, creaTagRequest.ente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, tipoTag, ente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaTagRequest {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipoTag: ").append(toIndentedString(tipoTag)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
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

