/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ConfigurazioneMessaggioNotifica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private TipoNotifica tipoMessaggio = null;
  private RiferimentoEnte ente = null;
  private TipoPratica tipoPratica = null;
  private String testo = null;

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
  


  // nome originario nello yaml: tipoMessaggio 
  @NotNull
  public TipoNotifica getTipoMessaggio() {
    return tipoMessaggio;
  }
  public void setTipoMessaggio(TipoNotifica tipoMessaggio) {
    this.tipoMessaggio = tipoMessaggio;
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
  


  // nome originario nello yaml: tipoPratica 
  public TipoPratica getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(TipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: testo 
  @NotNull
  public String getTesto() {
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneMessaggioNotifica configurazioneMessaggioNotifica = (ConfigurazioneMessaggioNotifica) o;
    return Objects.equals(id, configurazioneMessaggioNotifica.id) &&
        Objects.equals(tipoMessaggio, configurazioneMessaggioNotifica.tipoMessaggio) &&
        Objects.equals(ente, configurazioneMessaggioNotifica.ente) &&
        Objects.equals(tipoPratica, configurazioneMessaggioNotifica.tipoPratica) &&
        Objects.equals(testo, configurazioneMessaggioNotifica.testo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tipoMessaggio, ente, tipoPratica, testo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneMessaggioNotifica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tipoMessaggio: ").append(toIndentedString(tipoMessaggio)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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

