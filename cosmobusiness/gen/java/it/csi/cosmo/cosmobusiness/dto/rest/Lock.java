/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoUtente;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Lock  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codiceRisorsa = null;
  private String codiceOwner = null;
  private OffsetDateTime dataScadenza = null;
  private RiferimentoUtente utente = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceRisorsa 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceRisorsa() {
    return codiceRisorsa;
  }
  public void setCodiceRisorsa(String codiceRisorsa) {
    this.codiceRisorsa = codiceRisorsa;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceOwner 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceOwner() {
    return codiceOwner;
  }
  public void setCodiceOwner(String codiceOwner) {
    this.codiceOwner = codiceOwner;
  }

  /**
   **/
  


  // nome originario nello yaml: dataScadenza 
  public OffsetDateTime getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(OffsetDateTime dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   **/
  


  // nome originario nello yaml: utente 
  public RiferimentoUtente getUtente() {
    return utente;
  }
  public void setUtente(RiferimentoUtente utente) {
    this.utente = utente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lock lock = (Lock) o;
    return Objects.equals(id, lock.id) &&
        Objects.equals(codiceRisorsa, lock.codiceRisorsa) &&
        Objects.equals(codiceOwner, lock.codiceOwner) &&
        Objects.equals(dataScadenza, lock.dataScadenza) &&
        Objects.equals(utente, lock.utente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codiceRisorsa, codiceOwner, dataScadenza, utente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lock {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceRisorsa: ").append(toIndentedString(codiceRisorsa)).append("\n");
    sb.append("    codiceOwner: ").append(toIndentedString(codiceOwner)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
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

