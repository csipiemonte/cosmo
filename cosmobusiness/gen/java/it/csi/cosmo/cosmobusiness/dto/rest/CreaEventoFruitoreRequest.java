/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaEventoFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private String destinatario = null;
  private String titolo = null;
  private String descrizione = null;
  private OffsetDateTime scadenza = null;

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: destinatario 
  @NotNull
  @Size(min=1,max=255)
  public String getDestinatario() {
    return destinatario;
  }
  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: titolo 
  @NotNull
  @Size(min=1,max=255)
  public String getTitolo() {
    return titolo;
  }
  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  @Size(min=1)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: scadenza 
  @NotNull
  public OffsetDateTime getScadenza() {
    return scadenza;
  }
  public void setScadenza(OffsetDateTime scadenza) {
    this.scadenza = scadenza;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaEventoFruitoreRequest creaEventoFruitoreRequest = (CreaEventoFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, creaEventoFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(destinatario, creaEventoFruitoreRequest.destinatario) &&
        Objects.equals(titolo, creaEventoFruitoreRequest.titolo) &&
        Objects.equals(descrizione, creaEventoFruitoreRequest.descrizione) &&
        Objects.equals(scadenza, creaEventoFruitoreRequest.scadenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, destinatario, titolo, descrizione, scadenza);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaEventoFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    scadenza: ").append(toIndentedString(scadenza)).append("\n");
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

