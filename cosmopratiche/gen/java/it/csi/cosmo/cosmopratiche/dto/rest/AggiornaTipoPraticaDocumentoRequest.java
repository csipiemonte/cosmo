/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaDocumentoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoFile;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaTipoPraticaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String codiceStardas = null;
  private String descrizione = null;
  private Boolean firmabile = null;
  private List<AggiornaTipoPraticaDocumentoRequest> allegati = new ArrayList<>();
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
  


  // nome originario nello yaml: allegati 
  public List<AggiornaTipoPraticaDocumentoRequest> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<AggiornaTipoPraticaDocumentoRequest> allegati) {
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
    AggiornaTipoPraticaDocumentoRequest aggiornaTipoPraticaDocumentoRequest = (AggiornaTipoPraticaDocumentoRequest) o;
    return Objects.equals(codice, aggiornaTipoPraticaDocumentoRequest.codice) &&
        Objects.equals(codiceStardas, aggiornaTipoPraticaDocumentoRequest.codiceStardas) &&
        Objects.equals(descrizione, aggiornaTipoPraticaDocumentoRequest.descrizione) &&
        Objects.equals(firmabile, aggiornaTipoPraticaDocumentoRequest.firmabile) &&
        Objects.equals(allegati, aggiornaTipoPraticaDocumentoRequest.allegati) &&
        Objects.equals(dimensioneMassima, aggiornaTipoPraticaDocumentoRequest.dimensioneMassima) &&
        Objects.equals(formatiFile, aggiornaTipoPraticaDocumentoRequest.formatiFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, codiceStardas, descrizione, firmabile, allegati, dimensioneMassima, formatiFile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaTipoPraticaDocumentoRequest {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    codiceStardas: ").append(toIndentedString(codiceStardas)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    firmabile: ").append(toIndentedString(firmabile)).append("\n");
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

