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
public class RegaxAssociazione  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String assDenominazione = null;

  private String assEtichetta = null;

  private String assCodicefiscale = null;

  private String assPartitaiva = null;

  private String assDatainserimento = null;

  private String assDataarrivo = null;

  private Long statoassCodice = null;

  private Long assNumeroaderenti = null;

  private Long protCodice = null;

  /**
   **/
  
  @JsonProperty("ass_cod")
  public Long getAssCod() {
    return assCod;
  }
  public void setAssCod(Long assCod) {
    this.assCod = assCod;
  }

  /**
   **/
  
  @JsonProperty("ass_denominazione")
  public String getAssDenominazione() {
    return assDenominazione;
  }
  public void setAssDenominazione(String assDenominazione) {
    this.assDenominazione = assDenominazione;
  }

  /**
   **/
  
  @JsonProperty("ass_etichetta")
  public String getAssEtichetta() {
    return assEtichetta;
  }
  public void setAssEtichetta(String assEtichetta) {
    this.assEtichetta = assEtichetta;
  }

  /**
   **/
  
  @JsonProperty("ass_codicefiscale")
  public String getAssCodicefiscale() {
    return assCodicefiscale;
  }
  public void setAssCodicefiscale(String assCodicefiscale) {
    this.assCodicefiscale = assCodicefiscale;
  }

  /**
   **/
  
  @JsonProperty("ass_partitaiva")
  public String getAssPartitaiva() {
    return assPartitaiva;
  }
  public void setAssPartitaiva(String assPartitaiva) {
    this.assPartitaiva = assPartitaiva;
  }

  /**
   **/
  
  @JsonProperty("ass_datainserimento")
  public String getAssDatainserimento() {
    return assDatainserimento;
  }
  public void setAssDatainserimento(String assDatainserimento) {
    this.assDatainserimento = assDatainserimento;
  }

  /**
   **/
  
  @JsonProperty("ass_dataarrivo")
  public String getAssDataarrivo() {
    return assDataarrivo;
  }
  public void setAssDataarrivo(String assDataarrivo) {
    this.assDataarrivo = assDataarrivo;
  }

  /**
   **/
  
  @JsonProperty("statoass_codice")
  public Long getStatoassCodice() {
    return statoassCodice;
  }
  public void setStatoassCodice(Long statoassCodice) {
    this.statoassCodice = statoassCodice;
  }

  /**
   **/
  
  @JsonProperty("ass_numeroaderenti")
  public Long getAssNumeroaderenti() {
    return assNumeroaderenti;
  }
  public void setAssNumeroaderenti(Long assNumeroaderenti) {
    this.assNumeroaderenti = assNumeroaderenti;
  }

  /**
   **/
  
  @JsonProperty("prot_codice")
  public Long getProtCodice() {
    return protCodice;
  }
  public void setProtCodice(Long protCodice) {
    this.protCodice = protCodice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxAssociazione regaxAssociazione = (RegaxAssociazione) o;
    return Objects.equals(assCod, regaxAssociazione.assCod) &&
        Objects.equals(assDenominazione, regaxAssociazione.assDenominazione) &&
        Objects.equals(assEtichetta, regaxAssociazione.assEtichetta) &&
        Objects.equals(assCodicefiscale, regaxAssociazione.assCodicefiscale) &&
        Objects.equals(assPartitaiva, regaxAssociazione.assPartitaiva) &&
        Objects.equals(assDatainserimento, regaxAssociazione.assDatainserimento) &&
        Objects.equals(assDataarrivo, regaxAssociazione.assDataarrivo) &&
        Objects.equals(statoassCodice, regaxAssociazione.statoassCodice) &&
        Objects.equals(assNumeroaderenti, regaxAssociazione.assNumeroaderenti) &&
        Objects.equals(protCodice, regaxAssociazione.protCodice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, assDenominazione, assEtichetta, assCodicefiscale, assPartitaiva, assDatainserimento, assDataarrivo, statoassCodice, assNumeroaderenti, protCodice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxAssociazione {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    assDenominazione: ").append(toIndentedString(assDenominazione)).append("\n");
    sb.append("    assEtichetta: ").append(toIndentedString(assEtichetta)).append("\n");
    sb.append("    assCodicefiscale: ").append(toIndentedString(assCodicefiscale)).append("\n");
    sb.append("    assPartitaiva: ").append(toIndentedString(assPartitaiva)).append("\n");
    sb.append("    assDatainserimento: ").append(toIndentedString(assDatainserimento)).append("\n");
    sb.append("    assDataarrivo: ").append(toIndentedString(assDataarrivo)).append("\n");
    sb.append("    statoassCodice: ").append(toIndentedString(statoassCodice)).append("\n");
    sb.append("    assNumeroaderenti: ").append(toIndentedString(assNumeroaderenti)).append("\n");
    sb.append("    protCodice: ").append(toIndentedString(protCodice)).append("\n");
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

