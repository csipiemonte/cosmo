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

public class TrasformazioneDatiPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codiceTipoPratica = null;
  private String codiceFase = null;
  private Integer ordine = null;
  private Boolean obbligatoria = null;
  private String descrizione = null;
  private String definizione = null;

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
  


  // nome originario nello yaml: codiceTipoPratica 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFase 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceFase() {
    return codiceFase;
  }
  public void setCodiceFase(String codiceFase) {
    this.codiceFase = codiceFase;
  }

  /**
   **/
  


  // nome originario nello yaml: ordine 
  public Integer getOrdine() {
    return ordine;
  }
  public void setOrdine(Integer ordine) {
    this.ordine = ordine;
  }

  /**
   **/
  


  // nome originario nello yaml: obbligatoria 
  public Boolean isObbligatoria() {
    return obbligatoria;
  }
  public void setObbligatoria(Boolean obbligatoria) {
    this.obbligatoria = obbligatoria;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @Size(max=255)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: definizione 
  @NotNull
  @Size(min=1)
  public String getDefinizione() {
    return definizione;
  }
  public void setDefinizione(String definizione) {
    this.definizione = definizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrasformazioneDatiPratica trasformazioneDatiPratica = (TrasformazioneDatiPratica) o;
    return Objects.equals(id, trasformazioneDatiPratica.id) &&
        Objects.equals(codiceTipoPratica, trasformazioneDatiPratica.codiceTipoPratica) &&
        Objects.equals(codiceFase, trasformazioneDatiPratica.codiceFase) &&
        Objects.equals(ordine, trasformazioneDatiPratica.ordine) &&
        Objects.equals(obbligatoria, trasformazioneDatiPratica.obbligatoria) &&
        Objects.equals(descrizione, trasformazioneDatiPratica.descrizione) &&
        Objects.equals(definizione, trasformazioneDatiPratica.definizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codiceTipoPratica, codiceFase, ordine, obbligatoria, descrizione, definizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrasformazioneDatiPratica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceTipoPratica: ").append(toIndentedString(codiceTipoPratica)).append("\n");
    sb.append("    codiceFase: ").append(toIndentedString(codiceFase)).append("\n");
    sb.append("    ordine: ").append(toIndentedString(ordine)).append("\n");
    sb.append("    obbligatoria: ").append(toIndentedString(obbligatoria)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    definizione: ").append(toIndentedString(definizione)).append("\n");
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

