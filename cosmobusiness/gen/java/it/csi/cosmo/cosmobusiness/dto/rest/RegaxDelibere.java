/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;
@SuppressWarnings("unused")
public class RegaxDelibere  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long delCodice = null;

  private Long delNumero = null;

  private Long contdelContesto = null;

  private Long progressivo = null;

  private String delDataapprovazione = null;

  private String delOggetto = null;

  /**
   **/
  
  @JsonProperty("del_codice")
  @NotNull
  public Long getDelCodice() {
    return delCodice;
  }
  public void setDelCodice(Long delCodice) {
    this.delCodice = delCodice;
  }

  /**
   **/
  
  @JsonProperty("del_numero")
  public Long getDelNumero() {
    return delNumero;
  }
  public void setDelNumero(Long delNumero) {
    this.delNumero = delNumero;
  }

  /**
   **/
  
  @JsonProperty("contdel_contesto")
  @NotNull
  public Long getContdelContesto() {
    return contdelContesto;
  }
  public void setContdelContesto(Long contdelContesto) {
    this.contdelContesto = contdelContesto;
  }

  /**
   **/
  
  @JsonProperty("progressivo")
  public Long getProgressivo() {
    return progressivo;
  }
  public void setProgressivo(Long progressivo) {
    this.progressivo = progressivo;
  }

  /**
   **/
  
  @JsonProperty("del_dataapprovazione")
  public String getDelDataapprovazione() {
    return delDataapprovazione;
  }
  public void setDelDataapprovazione(String delDataapprovazione) {
    this.delDataapprovazione = delDataapprovazione;
  }

  /**
   **/
  
  @JsonProperty("del_oggetto")
  @NotNull
  public String getDelOggetto() {
    return delOggetto;
  }
  public void setDelOggetto(String delOggetto) {
    this.delOggetto = delOggetto;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxDelibere regaxDelibere = (RegaxDelibere) o;
    return Objects.equals(delCodice, regaxDelibere.delCodice) &&
        Objects.equals(delNumero, regaxDelibere.delNumero) &&
        Objects.equals(contdelContesto, regaxDelibere.contdelContesto) &&
        Objects.equals(progressivo, regaxDelibere.progressivo) &&
        Objects.equals(delDataapprovazione, regaxDelibere.delDataapprovazione) &&
        Objects.equals(delOggetto, regaxDelibere.delOggetto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(delCodice, delNumero, contdelContesto, progressivo, delDataapprovazione, delOggetto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxDelibere {\n");
    
    sb.append("    delCodice: ").append(toIndentedString(delCodice)).append("\n");
    sb.append("    delNumero: ").append(toIndentedString(delNumero)).append("\n");
    sb.append("    contdelContesto: ").append(toIndentedString(contdelContesto)).append("\n");
    sb.append("    progressivo: ").append(toIndentedString(progressivo)).append("\n");
    sb.append("    delDataapprovazione: ").append(toIndentedString(delDataapprovazione)).append("\n");
    sb.append("    delOggetto: ").append(toIndentedString(delOggetto)).append("\n");
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

