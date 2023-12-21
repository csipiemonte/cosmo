/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoPratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Gruppo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codice = null;
  private String nome = null;
  private String descrizione = null;
  private RiferimentoEnte ente = null;
  private List<RiferimentoUtente> utenti = new ArrayList<>();
  private List<TipoPratica> tipologiePratiche = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: ente 
  @NotNull
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: utenti 
  public List<RiferimentoUtente> getUtenti() {
    return utenti;
  }
  public void setUtenti(List<RiferimentoUtente> utenti) {
    this.utenti = utenti;
  }

  /**
   **/
  


  // nome originario nello yaml: tipologiePratiche 
  public List<TipoPratica> getTipologiePratiche() {
    return tipologiePratiche;
  }
  public void setTipologiePratiche(List<TipoPratica> tipologiePratiche) {
    this.tipologiePratiche = tipologiePratiche;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Gruppo gruppo = (Gruppo) o;
    return Objects.equals(id, gruppo.id) &&
        Objects.equals(codice, gruppo.codice) &&
        Objects.equals(nome, gruppo.nome) &&
        Objects.equals(descrizione, gruppo.descrizione) &&
        Objects.equals(ente, gruppo.ente) &&
        Objects.equals(utenti, gruppo.utenti) &&
        Objects.equals(tipologiePratiche, gruppo.tipologiePratiche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codice, nome, descrizione, ente, utenti, tipologiePratiche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Gruppo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    utenti: ").append(toIndentedString(utenti)).append("\n");
    sb.append("    tipologiePratiche: ").append(toIndentedString(tipologiePratiche)).append("\n");
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

