/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.EsitoVerificaFirma;
import it.csi.cosmo.cosmosoap.dto.rest.InfoVerificaFirma;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InfoVerificaFirma  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceFiscaleFirmatario = null;
  private OffsetDateTime dataVerificaFirma = null;
  private OffsetDateTime dataApposizione = null;
  private OffsetDateTime dataApposizioneMarcaturaTemporale = null;
  private String firmatario = null;
  private String organizzazione = null;
  private List<InfoVerificaFirma> infoVerificaFirme = new ArrayList<>();
  private EsitoVerificaFirma esito = null;
  private String codiceErrore = null;

  /**
   **/
  


  // nome originario nello yaml: codiceFiscaleFirmatario 
  public String getCodiceFiscaleFirmatario() {
    return codiceFiscaleFirmatario;
  }
  public void setCodiceFiscaleFirmatario(String codiceFiscaleFirmatario) {
    this.codiceFiscaleFirmatario = codiceFiscaleFirmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: dataVerificaFirma 
  public OffsetDateTime getDataVerificaFirma() {
    return dataVerificaFirma;
  }
  public void setDataVerificaFirma(OffsetDateTime dataVerificaFirma) {
    this.dataVerificaFirma = dataVerificaFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: dataApposizione 
  public OffsetDateTime getDataApposizione() {
    return dataApposizione;
  }
  public void setDataApposizione(OffsetDateTime dataApposizione) {
    this.dataApposizione = dataApposizione;
  }

  /**
   **/
  


  // nome originario nello yaml: dataApposizioneMarcaturaTemporale 
  public OffsetDateTime getDataApposizioneMarcaturaTemporale() {
    return dataApposizioneMarcaturaTemporale;
  }
  public void setDataApposizioneMarcaturaTemporale(OffsetDateTime dataApposizioneMarcaturaTemporale) {
    this.dataApposizioneMarcaturaTemporale = dataApposizioneMarcaturaTemporale;
  }

  /**
   **/
  


  // nome originario nello yaml: firmatario 
  public String getFirmatario() {
    return firmatario;
  }
  public void setFirmatario(String firmatario) {
    this.firmatario = firmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: organizzazione 
  public String getOrganizzazione() {
    return organizzazione;
  }
  public void setOrganizzazione(String organizzazione) {
    this.organizzazione = organizzazione;
  }

  /**
   **/
  


  // nome originario nello yaml: infoVerificaFirme 
  public List<InfoVerificaFirma> getInfoVerificaFirme() {
    return infoVerificaFirme;
  }
  public void setInfoVerificaFirme(List<InfoVerificaFirma> infoVerificaFirme) {
    this.infoVerificaFirme = infoVerificaFirme;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  @NotNull
  public EsitoVerificaFirma getEsito() {
    return esito;
  }
  public void setEsito(EsitoVerificaFirma esito) {
    this.esito = esito;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceErrore 
  @Size(max=100)
  public String getCodiceErrore() {
    return codiceErrore;
  }
  public void setCodiceErrore(String codiceErrore) {
    this.codiceErrore = codiceErrore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfoVerificaFirma infoVerificaFirma = (InfoVerificaFirma) o;
    return Objects.equals(codiceFiscaleFirmatario, infoVerificaFirma.codiceFiscaleFirmatario) &&
        Objects.equals(dataVerificaFirma, infoVerificaFirma.dataVerificaFirma) &&
        Objects.equals(dataApposizione, infoVerificaFirma.dataApposizione) &&
        Objects.equals(dataApposizioneMarcaturaTemporale, infoVerificaFirma.dataApposizioneMarcaturaTemporale) &&
        Objects.equals(firmatario, infoVerificaFirma.firmatario) &&
        Objects.equals(organizzazione, infoVerificaFirma.organizzazione) &&
        Objects.equals(infoVerificaFirme, infoVerificaFirma.infoVerificaFirme) &&
        Objects.equals(esito, infoVerificaFirma.esito) &&
        Objects.equals(codiceErrore, infoVerificaFirma.codiceErrore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscaleFirmatario, dataVerificaFirma, dataApposizione, dataApposizioneMarcaturaTemporale, firmatario, organizzazione, infoVerificaFirme, esito, codiceErrore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfoVerificaFirma {\n");
    
    sb.append("    codiceFiscaleFirmatario: ").append(toIndentedString(codiceFiscaleFirmatario)).append("\n");
    sb.append("    dataVerificaFirma: ").append(toIndentedString(dataVerificaFirma)).append("\n");
    sb.append("    dataApposizione: ").append(toIndentedString(dataApposizione)).append("\n");
    sb.append("    dataApposizioneMarcaturaTemporale: ").append(toIndentedString(dataApposizioneMarcaturaTemporale)).append("\n");
    sb.append("    firmatario: ").append(toIndentedString(firmatario)).append("\n");
    sb.append("    organizzazione: ").append(toIndentedString(organizzazione)).append("\n");
    sb.append("    infoVerificaFirme: ").append(toIndentedString(infoVerificaFirme)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    codiceErrore: ").append(toIndentedString(codiceErrore)).append("\n");
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

