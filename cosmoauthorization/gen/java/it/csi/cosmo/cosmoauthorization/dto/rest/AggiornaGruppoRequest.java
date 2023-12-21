/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaGruppoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String codice = null;
  private String descrizione = null;
  private List<Long> idUtenti = new ArrayList<>();
  private List<String> codiciTipologiePratiche = new ArrayList<>();
  private List<Long> idTags = new ArrayList<>();
  private Long utenteTag = null;

  /**
   **/
  


  // nome originario nello yaml: nome 
  @NotNull
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

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
  


  // nome originario nello yaml: idUtenti 
  public List<Long> getIdUtenti() {
    return idUtenti;
  }
  public void setIdUtenti(List<Long> idUtenti) {
    this.idUtenti = idUtenti;
  }

  /**
   **/
  


  // nome originario nello yaml: codiciTipologiePratiche 
  public List<String> getCodiciTipologiePratiche() {
    return codiciTipologiePratiche;
  }
  public void setCodiciTipologiePratiche(List<String> codiciTipologiePratiche) {
    this.codiciTipologiePratiche = codiciTipologiePratiche;
  }

  /**
   **/
  


  // nome originario nello yaml: idTags 
  public List<Long> getIdTags() {
    return idTags;
  }
  public void setIdTags(List<Long> idTags) {
    this.idTags = idTags;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteTag 
  public Long getUtenteTag() {
    return utenteTag;
  }
  public void setUtenteTag(Long utenteTag) {
    this.utenteTag = utenteTag;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaGruppoRequest aggiornaGruppoRequest = (AggiornaGruppoRequest) o;
    return Objects.equals(nome, aggiornaGruppoRequest.nome) &&
        Objects.equals(codice, aggiornaGruppoRequest.codice) &&
        Objects.equals(descrizione, aggiornaGruppoRequest.descrizione) &&
        Objects.equals(idUtenti, aggiornaGruppoRequest.idUtenti) &&
        Objects.equals(codiciTipologiePratiche, aggiornaGruppoRequest.codiciTipologiePratiche) &&
        Objects.equals(idTags, aggiornaGruppoRequest.idTags) &&
        Objects.equals(utenteTag, aggiornaGruppoRequest.utenteTag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, codice, descrizione, idUtenti, codiciTipologiePratiche, idTags, utenteTag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaGruppoRequest {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    idUtenti: ").append(toIndentedString(idUtenti)).append("\n");
    sb.append("    codiciTipologiePratiche: ").append(toIndentedString(codiciTipologiePratiche)).append("\n");
    sb.append("    idTags: ").append(toIndentedString(idTags)).append("\n");
    sb.append("    utenteTag: ").append(toIndentedString(utenteTag)).append("\n");
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

