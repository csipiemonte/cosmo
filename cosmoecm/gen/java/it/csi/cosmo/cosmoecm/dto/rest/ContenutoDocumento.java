/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.AnteprimaContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoVerificaFirma;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFile;
import it.csi.cosmo.cosmoecm.dto.rest.InfoFirmaFea;
import it.csi.cosmo.cosmoecm.dto.rest.InfoVerificaFirma;
import it.csi.cosmo.cosmoecm.dto.rest.TipoContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoContenutoFirmato;
import it.csi.cosmo.cosmoecm.dto.rest.TipoFirma;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ContenutoDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeFile = null;
  private Long dimensione = null;
  private TipoContenutoDocumento tipo = null;
  private TipoFirma tipoFirma = null;
  private FormatoFile formatoFile = null;
  private Long id = null;
  private OffsetDateTime dataVerificaFirma = null;
  private List<InfoVerificaFirma> infoVerificaFirme = new ArrayList<>();
  private EsitoVerificaFirma esitoVerificaFirma = null;
  private OffsetDateTime dtCancellazione = null;
  private OffsetDateTime dtInserimento = null;
  private List<AnteprimaContenutoDocumento> anteprime = new ArrayList<>();
  private String shaFile = null;
  private TipoContenutoFirmato tipoContenutoFirmato = null;
  private InfoFirmaFea infoFirmaFea = null;

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
  


  // nome originario nello yaml: dimensione 
  public Long getDimensione() {
    return dimensione;
  }
  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  /**
   **/
  


  // nome originario nello yaml: tipo 
  public TipoContenutoDocumento getTipo() {
    return tipo;
  }
  public void setTipo(TipoContenutoDocumento tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoFirma 
  public TipoFirma getTipoFirma() {
    return tipoFirma;
  }
  public void setTipoFirma(TipoFirma tipoFirma) {
    this.tipoFirma = tipoFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: formatoFile 
  public FormatoFile getFormatoFile() {
    return formatoFile;
  }
  public void setFormatoFile(FormatoFile formatoFile) {
    this.formatoFile = formatoFile;
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
  


  // nome originario nello yaml: dataVerificaFirma 
  public OffsetDateTime getDataVerificaFirma() {
    return dataVerificaFirma;
  }
  public void setDataVerificaFirma(OffsetDateTime dataVerificaFirma) {
    this.dataVerificaFirma = dataVerificaFirma;
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
  


  // nome originario nello yaml: esitoVerificaFirma 
  public EsitoVerificaFirma getEsitoVerificaFirma() {
    return esitoVerificaFirma;
  }
  public void setEsitoVerificaFirma(EsitoVerificaFirma esitoVerificaFirma) {
    this.esitoVerificaFirma = esitoVerificaFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: dtCancellazione 
  public OffsetDateTime getDtCancellazione() {
    return dtCancellazione;
  }
  public void setDtCancellazione(OffsetDateTime dtCancellazione) {
    this.dtCancellazione = dtCancellazione;
  }

  /**
   **/
  


  // nome originario nello yaml: dtInserimento 
  public OffsetDateTime getDtInserimento() {
    return dtInserimento;
  }
  public void setDtInserimento(OffsetDateTime dtInserimento) {
    this.dtInserimento = dtInserimento;
  }

  /**
   **/
  


  // nome originario nello yaml: anteprime 
  public List<AnteprimaContenutoDocumento> getAnteprime() {
    return anteprime;
  }
  public void setAnteprime(List<AnteprimaContenutoDocumento> anteprime) {
    this.anteprime = anteprime;
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

  /**
   **/
  


  // nome originario nello yaml: tipoContenutoFirmato 
  public TipoContenutoFirmato getTipoContenutoFirmato() {
    return tipoContenutoFirmato;
  }
  public void setTipoContenutoFirmato(TipoContenutoFirmato tipoContenutoFirmato) {
    this.tipoContenutoFirmato = tipoContenutoFirmato;
  }

  /**
   **/
  


  // nome originario nello yaml: infoFirmaFea 
  public InfoFirmaFea getInfoFirmaFea() {
    return infoFirmaFea;
  }
  public void setInfoFirmaFea(InfoFirmaFea infoFirmaFea) {
    this.infoFirmaFea = infoFirmaFea;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContenutoDocumento contenutoDocumento = (ContenutoDocumento) o;
    return Objects.equals(nomeFile, contenutoDocumento.nomeFile) &&
        Objects.equals(dimensione, contenutoDocumento.dimensione) &&
        Objects.equals(tipo, contenutoDocumento.tipo) &&
        Objects.equals(tipoFirma, contenutoDocumento.tipoFirma) &&
        Objects.equals(formatoFile, contenutoDocumento.formatoFile) &&
        Objects.equals(id, contenutoDocumento.id) &&
        Objects.equals(dataVerificaFirma, contenutoDocumento.dataVerificaFirma) &&
        Objects.equals(infoVerificaFirme, contenutoDocumento.infoVerificaFirme) &&
        Objects.equals(esitoVerificaFirma, contenutoDocumento.esitoVerificaFirma) &&
        Objects.equals(dtCancellazione, contenutoDocumento.dtCancellazione) &&
        Objects.equals(dtInserimento, contenutoDocumento.dtInserimento) &&
        Objects.equals(anteprime, contenutoDocumento.anteprime) &&
        Objects.equals(shaFile, contenutoDocumento.shaFile) &&
        Objects.equals(tipoContenutoFirmato, contenutoDocumento.tipoContenutoFirmato) &&
        Objects.equals(infoFirmaFea, contenutoDocumento.infoFirmaFea);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, dimensione, tipo, tipoFirma, formatoFile, id, dataVerificaFirma, infoVerificaFirme, esitoVerificaFirma, dtCancellazione, dtInserimento, anteprime, shaFile, tipoContenutoFirmato, infoFirmaFea);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContenutoDocumento {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    dimensione: ").append(toIndentedString(dimensione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    tipoFirma: ").append(toIndentedString(tipoFirma)).append("\n");
    sb.append("    formatoFile: ").append(toIndentedString(formatoFile)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dataVerificaFirma: ").append(toIndentedString(dataVerificaFirma)).append("\n");
    sb.append("    infoVerificaFirme: ").append(toIndentedString(infoVerificaFirme)).append("\n");
    sb.append("    esitoVerificaFirma: ").append(toIndentedString(esitoVerificaFirma)).append("\n");
    sb.append("    dtCancellazione: ").append(toIndentedString(dtCancellazione)).append("\n");
    sb.append("    dtInserimento: ").append(toIndentedString(dtInserimento)).append("\n");
    sb.append("    anteprime: ").append(toIndentedString(anteprime)).append("\n");
    sb.append("    shaFile: ").append(toIndentedString(shaFile)).append("\n");
    sb.append("    tipoContenutoFirmato: ").append(toIndentedString(tipoContenutoFirmato)).append("\n");
    sb.append("    infoFirmaFea: ").append(toIndentedString(infoFirmaFea)).append("\n");
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

