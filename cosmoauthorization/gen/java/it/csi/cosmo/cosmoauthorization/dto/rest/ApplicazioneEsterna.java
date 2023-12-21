/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsterna;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ApplicazioneEsterna  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String descrizione = null;
  private String icona = null;
  private List<FunzionalitaApplicazioneEsterna> funzionalita = new ArrayList<>();
  private Integer posizione = null;

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
  


  // nome originario nello yaml: icona 
  public String getIcona() {
    return icona;
  }
  public void setIcona(String icona) {
    this.icona = icona;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalita 
  public List<FunzionalitaApplicazioneEsterna> getFunzionalita() {
    return funzionalita;
  }
  public void setFunzionalita(List<FunzionalitaApplicazioneEsterna> funzionalita) {
    this.funzionalita = funzionalita;
  }

  /**
   **/
  


  // nome originario nello yaml: posizione 
  public Integer getPosizione() {
    return posizione;
  }
  public void setPosizione(Integer posizione) {
    this.posizione = posizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicazioneEsterna applicazioneEsterna = (ApplicazioneEsterna) o;
    return Objects.equals(id, applicazioneEsterna.id) &&
        Objects.equals(descrizione, applicazioneEsterna.descrizione) &&
        Objects.equals(icona, applicazioneEsterna.icona) &&
        Objects.equals(funzionalita, applicazioneEsterna.funzionalita) &&
        Objects.equals(posizione, applicazioneEsterna.posizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizione, icona, funzionalita, posizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicazioneEsterna {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    icona: ").append(toIndentedString(icona)).append("\n");
    sb.append("    funzionalita: ").append(toIndentedString(funzionalita)).append("\n");
    sb.append("    posizione: ").append(toIndentedString(posizione)).append("\n");
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

