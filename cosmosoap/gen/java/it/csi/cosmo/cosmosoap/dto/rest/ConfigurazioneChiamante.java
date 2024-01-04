/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ConfigurazioneChiamante  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceFiscaleEnte = null;
  private String codiceFruitore = null;
  private String codiceApplicazione = null;
  private String codiceTipoDocumento = null;

  /**
   **/
  


  // nome originario nello yaml: codiceFiscaleEnte 
  @NotNull
  @Size(max=50)
  public String getCodiceFiscaleEnte() {
    return codiceFiscaleEnte;
  }
  public void setCodiceFiscaleEnte(String codiceFiscaleEnte) {
    this.codiceFiscaleEnte = codiceFiscaleEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFruitore 
  @NotNull
  @Size(max=50)
  public String getCodiceFruitore() {
    return codiceFruitore;
  }
  public void setCodiceFruitore(String codiceFruitore) {
    this.codiceFruitore = codiceFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceApplicazione 
  @NotNull
  @Size(max=50)
  public String getCodiceApplicazione() {
    return codiceApplicazione;
  }
  public void setCodiceApplicazione(String codiceApplicazione) {
    this.codiceApplicazione = codiceApplicazione;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoDocumento 
  @NotNull
  @Size(max=50)
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneChiamante configurazioneChiamante = (ConfigurazioneChiamante) o;
    return Objects.equals(codiceFiscaleEnte, configurazioneChiamante.codiceFiscaleEnte) &&
        Objects.equals(codiceFruitore, configurazioneChiamante.codiceFruitore) &&
        Objects.equals(codiceApplicazione, configurazioneChiamante.codiceApplicazione) &&
        Objects.equals(codiceTipoDocumento, configurazioneChiamante.codiceTipoDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscaleEnte, codiceFruitore, codiceApplicazione, codiceTipoDocumento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneChiamante {\n");
    
    sb.append("    codiceFiscaleEnte: ").append(toIndentedString(codiceFiscaleEnte)).append("\n");
    sb.append("    codiceFruitore: ").append(toIndentedString(codiceFruitore)).append("\n");
    sb.append("    codiceApplicazione: ").append(toIndentedString(codiceApplicazione)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
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

