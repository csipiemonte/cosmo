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

public class CreaGruppoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String codice = null;
  private String descrizione = null;
  private List<Long> idUtenti = new ArrayList<>();
  private List<String> codiciTipologiePratiche = new ArrayList<>();

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaGruppoRequest creaGruppoRequest = (CreaGruppoRequest) o;
    return Objects.equals(nome, creaGruppoRequest.nome) &&
        Objects.equals(codice, creaGruppoRequest.codice) &&
        Objects.equals(descrizione, creaGruppoRequest.descrizione) &&
        Objects.equals(idUtenti, creaGruppoRequest.idUtenti) &&
        Objects.equals(codiciTipologiePratiche, creaGruppoRequest.codiciTipologiePratiche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, codice, descrizione, idUtenti, codiciTipologiePratiche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaGruppoRequest {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    idUtenti: ").append(toIndentedString(idUtenti)).append("\n");
    sb.append("    codiciTipologiePratiche: ").append(toIndentedString(codiciTipologiePratiche)).append("\n");
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

