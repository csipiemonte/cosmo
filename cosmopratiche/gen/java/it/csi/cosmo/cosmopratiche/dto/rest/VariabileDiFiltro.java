/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VariabileDiFiltro  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String nome = null;
  private String label = null;
  private FormatoVariabileDiFiltro formato = null;
  private TipoPratica tipoPratica = null;
  private TipoFiltro tipoFiltro = null;
  private Boolean aggiungereARisultatoRicerca = null;

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
  


  // nome originario nello yaml: label 
  @NotNull
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   **/
  


  // nome originario nello yaml: formato 
  @NotNull
  public FormatoVariabileDiFiltro getFormato() {
    return formato;
  }
  public void setFormato(FormatoVariabileDiFiltro formato) {
    this.formato = formato;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  @NotNull
  public TipoPratica getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(TipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoFiltro 
  @NotNull
  public TipoFiltro getTipoFiltro() {
    return tipoFiltro;
  }
  public void setTipoFiltro(TipoFiltro tipoFiltro) {
    this.tipoFiltro = tipoFiltro;
  }

  /**
   **/
  


  // nome originario nello yaml: aggiungereARisultatoRicerca 
  public Boolean isAggiungereARisultatoRicerca() {
    return aggiungereARisultatoRicerca;
  }
  public void setAggiungereARisultatoRicerca(Boolean aggiungereARisultatoRicerca) {
    this.aggiungereARisultatoRicerca = aggiungereARisultatoRicerca;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VariabileDiFiltro variabileDiFiltro = (VariabileDiFiltro) o;
    return Objects.equals(id, variabileDiFiltro.id) &&
        Objects.equals(nome, variabileDiFiltro.nome) &&
        Objects.equals(label, variabileDiFiltro.label) &&
        Objects.equals(formato, variabileDiFiltro.formato) &&
        Objects.equals(tipoPratica, variabileDiFiltro.tipoPratica) &&
        Objects.equals(tipoFiltro, variabileDiFiltro.tipoFiltro) &&
        Objects.equals(aggiungereARisultatoRicerca, variabileDiFiltro.aggiungereARisultatoRicerca);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, label, formato, tipoPratica, tipoFiltro, aggiungereARisultatoRicerca);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VariabileDiFiltro {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    formato: ").append(toIndentedString(formato)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    tipoFiltro: ").append(toIndentedString(tipoFiltro)).append("\n");
    sb.append("    aggiungereARisultatoRicerca: ").append(toIndentedString(aggiungereARisultatoRicerca)).append("\n");
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

