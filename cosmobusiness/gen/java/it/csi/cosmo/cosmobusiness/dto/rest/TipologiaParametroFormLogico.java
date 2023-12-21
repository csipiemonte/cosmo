/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TipologiaParametroFormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private Boolean obbligatorio = null;
  private String tipo = null;
  private String schema = null;
  private String valoreDefault = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  @Size(max=30)
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @Size(max=100)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: obbligatorio 
  public Boolean isObbligatorio() {
    return obbligatorio;
  }
  public void setObbligatorio(Boolean obbligatorio) {
    this.obbligatorio = obbligatorio;
  }

  /**
   **/
  


  // nome originario nello yaml: tipo 
  @Size(max=100)
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  


  // nome originario nello yaml: schema 
  public String getSchema() {
    return schema;
  }
  public void setSchema(String schema) {
    this.schema = schema;
  }

  /**
   **/
  


  // nome originario nello yaml: valoreDefault 
  public String getValoreDefault() {
    return valoreDefault;
  }
  public void setValoreDefault(String valoreDefault) {
    this.valoreDefault = valoreDefault;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipologiaParametroFormLogico tipologiaParametroFormLogico = (TipologiaParametroFormLogico) o;
    return Objects.equals(codice, tipologiaParametroFormLogico.codice) &&
        Objects.equals(descrizione, tipologiaParametroFormLogico.descrizione) &&
        Objects.equals(obbligatorio, tipologiaParametroFormLogico.obbligatorio) &&
        Objects.equals(tipo, tipologiaParametroFormLogico.tipo) &&
        Objects.equals(schema, tipologiaParametroFormLogico.schema) &&
        Objects.equals(valoreDefault, tipologiaParametroFormLogico.valoreDefault);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, obbligatorio, tipo, schema, valoreDefault);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipologiaParametroFormLogico {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    obbligatorio: ").append(toIndentedString(obbligatorio)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    valoreDefault: ").append(toIndentedString(valoreDefault)).append("\n");
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

