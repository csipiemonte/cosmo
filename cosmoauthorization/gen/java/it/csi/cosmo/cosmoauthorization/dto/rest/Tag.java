/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Tag  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private RiferimentoEnte ente = null;
  private TipoTag tipoTag = null;
  private Long id = null;
  private List<RiferimentoUtente> utenti = new ArrayList<>();

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
  


  // nome originario nello yaml: tipoTag 
  @NotNull
  public TipoTag getTipoTag() {
    return tipoTag;
  }
  public void setTipoTag(TipoTag tipoTag) {
    this.tipoTag = tipoTag;
  }

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
  


  // nome originario nello yaml: utenti 
  public List<RiferimentoUtente> getUtenti() {
    return utenti;
  }
  public void setUtenti(List<RiferimentoUtente> utenti) {
    this.utenti = utenti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tag tag = (Tag) o;
    return Objects.equals(codice, tag.codice) &&
        Objects.equals(descrizione, tag.descrizione) &&
        Objects.equals(ente, tag.ente) &&
        Objects.equals(tipoTag, tag.tipoTag) &&
        Objects.equals(id, tag.id) &&
        Objects.equals(utenti, tag.utenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, ente, tipoTag, id, utenti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tag {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoTag: ").append(toIndentedString(tipoTag)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    utenti: ").append(toIndentedString(utenti)).append("\n");
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

