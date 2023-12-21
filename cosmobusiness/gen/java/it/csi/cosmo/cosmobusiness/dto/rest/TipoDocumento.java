/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.FormatoFile;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoDocumento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TipoDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String codiceStardas = null;
  private String descrizione = null;
  private Boolean firmabile = null;
  private List<TipoDocumento> principali = new ArrayList<>();
  private List<TipoDocumento> allegati = new ArrayList<>();
  private Long dimensioneMassima = null;
  private List<FormatoFile> formatiFile = new ArrayList<>();

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
  


  // nome originario nello yaml: codiceStardas 
  public String getCodiceStardas() {
    return codiceStardas;
  }
  public void setCodiceStardas(String codiceStardas) {
    this.codiceStardas = codiceStardas;
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
  


  // nome originario nello yaml: firmabile 
  public Boolean isFirmabile() {
    return firmabile;
  }
  public void setFirmabile(Boolean firmabile) {
    this.firmabile = firmabile;
  }

  /**
   **/
  


  // nome originario nello yaml: principali 
  public List<TipoDocumento> getPrincipali() {
    return principali;
  }
  public void setPrincipali(List<TipoDocumento> principali) {
    this.principali = principali;
  }

  /**
   **/
  


  // nome originario nello yaml: allegati 
  public List<TipoDocumento> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<TipoDocumento> allegati) {
    this.allegati = allegati;
  }

  /**
   **/
  


  // nome originario nello yaml: dimensioneMassima 
  public Long getDimensioneMassima() {
    return dimensioneMassima;
  }
  public void setDimensioneMassima(Long dimensioneMassima) {
    this.dimensioneMassima = dimensioneMassima;
  }

  /**
   **/
  


  // nome originario nello yaml: formatiFile 
  public List<FormatoFile> getFormatiFile() {
    return formatiFile;
  }
  public void setFormatiFile(List<FormatoFile> formatiFile) {
    this.formatiFile = formatiFile;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoDocumento tipoDocumento = (TipoDocumento) o;
    return Objects.equals(codice, tipoDocumento.codice) &&
        Objects.equals(codiceStardas, tipoDocumento.codiceStardas) &&
        Objects.equals(descrizione, tipoDocumento.descrizione) &&
        Objects.equals(firmabile, tipoDocumento.firmabile) &&
        Objects.equals(principali, tipoDocumento.principali) &&
        Objects.equals(allegati, tipoDocumento.allegati) &&
        Objects.equals(dimensioneMassima, tipoDocumento.dimensioneMassima) &&
        Objects.equals(formatiFile, tipoDocumento.formatiFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, codiceStardas, descrizione, firmabile, principali, allegati, dimensioneMassima, formatiFile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoDocumento {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    codiceStardas: ").append(toIndentedString(codiceStardas)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    firmabile: ").append(toIndentedString(firmabile)).append("\n");
    sb.append("    principali: ").append(toIndentedString(principali)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
    sb.append("    dimensioneMassima: ").append(toIndentedString(dimensioneMassima)).append("\n");
    sb.append("    formatiFile: ").append(toIndentedString(formatiFile)).append("\n");
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

