/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FunzionalitaApplicazioneEsterna  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String descrizione = null;
  private String url = null;
  private Boolean principale = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  @NotNull
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: principale 
  @NotNull
  public Boolean isPrincipale() {
    return principale;
  }
  public void setPrincipale(Boolean principale) {
    this.principale = principale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FunzionalitaApplicazioneEsterna funzionalitaApplicazioneEsterna = (FunzionalitaApplicazioneEsterna) o;
    return Objects.equals(id, funzionalitaApplicazioneEsterna.id) &&
        Objects.equals(descrizione, funzionalitaApplicazioneEsterna.descrizione) &&
        Objects.equals(url, funzionalitaApplicazioneEsterna.url) &&
        Objects.equals(principale, funzionalitaApplicazioneEsterna.principale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizione, url, principale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FunzionalitaApplicazioneEsterna {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    principale: ").append(toIndentedString(principale)).append("\n");
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

