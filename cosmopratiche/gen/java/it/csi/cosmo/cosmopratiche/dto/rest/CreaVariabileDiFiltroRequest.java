/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaVariabileDiFiltroRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String label = null;
  private String formato = null;
  private TipoPratica tipoPratica = null;
  private String tipoFiltro = null;
  private String aggiungereARisultatoRicerca = null;

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
  public String getFormato() {
    return formato;
  }
  public void setFormato(String formato) {
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
  public String getTipoFiltro() {
    return tipoFiltro;
  }
  public void setTipoFiltro(String tipoFiltro) {
    this.tipoFiltro = tipoFiltro;
  }

  /**
   **/
  


  // nome originario nello yaml: aggiungereARisultatoRicerca 
  public String getAggiungereARisultatoRicerca() {
    return aggiungereARisultatoRicerca;
  }
  public void setAggiungereARisultatoRicerca(String aggiungereARisultatoRicerca) {
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
    CreaVariabileDiFiltroRequest creaVariabileDiFiltroRequest = (CreaVariabileDiFiltroRequest) o;
    return Objects.equals(nome, creaVariabileDiFiltroRequest.nome) &&
        Objects.equals(label, creaVariabileDiFiltroRequest.label) &&
        Objects.equals(formato, creaVariabileDiFiltroRequest.formato) &&
        Objects.equals(tipoPratica, creaVariabileDiFiltroRequest.tipoPratica) &&
        Objects.equals(tipoFiltro, creaVariabileDiFiltroRequest.tipoFiltro) &&
        Objects.equals(aggiungereARisultatoRicerca, creaVariabileDiFiltroRequest.aggiungereARisultatoRicerca);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, label, formato, tipoPratica, tipoFiltro, aggiungereARisultatoRicerca);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaVariabileDiFiltroRequest {\n");
    
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

