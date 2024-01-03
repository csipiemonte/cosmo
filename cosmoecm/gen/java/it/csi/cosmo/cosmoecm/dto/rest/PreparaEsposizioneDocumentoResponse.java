/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PreparaEsposizioneDocumentoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Esito esito = null;
  private Long idDocumento = null;
  private String nomeFile = null;
  private String codiceTipoDocumento = null;
  private String url = null;
  private Long dimensione = null;
  private String shaFile = null;

  /**
   **/
  


  // nome originario nello yaml: esito 
  public Esito getEsito() {
    return esito;
  }
  public void setEsito(Esito esito) {
    this.esito = esito;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumento 
  public Long getIdDocumento() {
    return idDocumento;
  }
  public void setIdDocumento(Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoDocumento 
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: dimensione 
  public Long getDimensione() {
    return dimensione;
  }
  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  /**
   **/
  


  // nome originario nello yaml: shaFile 
  public String getShaFile() {
    return shaFile;
  }
  public void setShaFile(String shaFile) {
    this.shaFile = shaFile;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreparaEsposizioneDocumentoResponse preparaEsposizioneDocumentoResponse = (PreparaEsposizioneDocumentoResponse) o;
    return Objects.equals(esito, preparaEsposizioneDocumentoResponse.esito) &&
        Objects.equals(idDocumento, preparaEsposizioneDocumentoResponse.idDocumento) &&
        Objects.equals(nomeFile, preparaEsposizioneDocumentoResponse.nomeFile) &&
        Objects.equals(codiceTipoDocumento, preparaEsposizioneDocumentoResponse.codiceTipoDocumento) &&
        Objects.equals(url, preparaEsposizioneDocumentoResponse.url) &&
        Objects.equals(dimensione, preparaEsposizioneDocumentoResponse.dimensione) &&
        Objects.equals(shaFile, preparaEsposizioneDocumentoResponse.shaFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esito, idDocumento, nomeFile, codiceTipoDocumento, url, dimensione, shaFile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreparaEsposizioneDocumentoResponse {\n");
    
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    idDocumento: ").append(toIndentedString(idDocumento)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    dimensione: ").append(toIndentedString(dimensione)).append("\n");
    sb.append("    shaFile: ").append(toIndentedString(shaFile)).append("\n");
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

