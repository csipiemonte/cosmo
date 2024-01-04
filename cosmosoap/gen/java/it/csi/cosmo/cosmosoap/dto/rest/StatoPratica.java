/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StatoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String classe = null;
  private OffsetDateTime dtInizioVal = null;
  private OffsetDateTime dtFineVal = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  @Size(min=1,max=100)
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
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
  


  // nome originario nello yaml: classe 
  public String getClasse() {
    return classe;
  }
  public void setClasse(String classe) {
    this.classe = classe;
  }

  /**
   **/
  


  // nome originario nello yaml: dtInizioVal 
  public OffsetDateTime getDtInizioVal() {
    return dtInizioVal;
  }
  public void setDtInizioVal(OffsetDateTime dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

  /**
   **/
  


  // nome originario nello yaml: dtFineVal 
  public OffsetDateTime getDtFineVal() {
    return dtFineVal;
  }
  public void setDtFineVal(OffsetDateTime dtFineVal) {
    this.dtFineVal = dtFineVal;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatoPratica statoPratica = (StatoPratica) o;
    return Objects.equals(codice, statoPratica.codice) &&
        Objects.equals(descrizione, statoPratica.descrizione) &&
        Objects.equals(classe, statoPratica.classe) &&
        Objects.equals(dtInizioVal, statoPratica.dtInizioVal) &&
        Objects.equals(dtFineVal, statoPratica.dtFineVal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, classe, dtInizioVal, dtFineVal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatoPratica {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    classe: ").append(toIndentedString(classe)).append("\n");
    sb.append("    dtInizioVal: ").append(toIndentedString(dtInizioVal)).append("\n");
    sb.append("    dtFineVal: ").append(toIndentedString(dtFineVal)).append("\n");
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

