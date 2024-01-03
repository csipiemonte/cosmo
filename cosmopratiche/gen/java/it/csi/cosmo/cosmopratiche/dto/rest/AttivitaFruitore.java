/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AttivitaFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String descrizione = null;
  private String nomeGruppoAssegnatario = null;
  private String dataInserimento = null;

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
  


  // nome originario nello yaml: nomeGruppoAssegnatario 
  public String getNomeGruppoAssegnatario() {
    return nomeGruppoAssegnatario;
  }
  public void setNomeGruppoAssegnatario(String nomeGruppoAssegnatario) {
    this.nomeGruppoAssegnatario = nomeGruppoAssegnatario;
  }

  /**
   **/
  


  // nome originario nello yaml: dataInserimento 
  public String getDataInserimento() {
    return dataInserimento;
  }
  public void setDataInserimento(String dataInserimento) {
    this.dataInserimento = dataInserimento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttivitaFruitore attivitaFruitore = (AttivitaFruitore) o;
    return Objects.equals(nome, attivitaFruitore.nome) &&
        Objects.equals(descrizione, attivitaFruitore.descrizione) &&
        Objects.equals(nomeGruppoAssegnatario, attivitaFruitore.nomeGruppoAssegnatario) &&
        Objects.equals(dataInserimento, attivitaFruitore.dataInserimento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, descrizione, nomeGruppoAssegnatario, dataInserimento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttivitaFruitore {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    nomeGruppoAssegnatario: ").append(toIndentedString(nomeGruppoAssegnatario)).append("\n");
    sb.append("    dataInserimento: ").append(toIndentedString(dataInserimento)).append("\n");
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

