/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CampiTecnici;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RiferimentoAttivita  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private String nome = null;
  private String descrizione = null;
  private OffsetDateTime dataCancellazione = null;
  private String formKey = null;
  private CampiTecnici campiTecnici = null;
  private Boolean esterna = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
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
  


  // nome originario nello yaml: dataCancellazione 
  public OffsetDateTime getDataCancellazione() {
    return dataCancellazione;
  }
  public void setDataCancellazione(OffsetDateTime dataCancellazione) {
    this.dataCancellazione = dataCancellazione;
  }

  /**
   **/
  


  // nome originario nello yaml: formKey 
  public String getFormKey() {
    return formKey;
  }
  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  /**
   **/
  


  // nome originario nello yaml: campiTecnici 
  public CampiTecnici getCampiTecnici() {
    return campiTecnici;
  }
  public void setCampiTecnici(CampiTecnici campiTecnici) {
    this.campiTecnici = campiTecnici;
  }

  /**
   **/
  


  // nome originario nello yaml: esterna 
  public Boolean isEsterna() {
    return esterna;
  }
  public void setEsterna(Boolean esterna) {
    this.esterna = esterna;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiferimentoAttivita riferimentoAttivita = (RiferimentoAttivita) o;
    return Objects.equals(id, riferimentoAttivita.id) &&
        Objects.equals(nome, riferimentoAttivita.nome) &&
        Objects.equals(descrizione, riferimentoAttivita.descrizione) &&
        Objects.equals(dataCancellazione, riferimentoAttivita.dataCancellazione) &&
        Objects.equals(formKey, riferimentoAttivita.formKey) &&
        Objects.equals(campiTecnici, riferimentoAttivita.campiTecnici) &&
        Objects.equals(esterna, riferimentoAttivita.esterna);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, descrizione, dataCancellazione, formKey, campiTecnici, esterna);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoAttivita {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dataCancellazione: ").append(toIndentedString(dataCancellazione)).append("\n");
    sb.append("    formKey: ").append(toIndentedString(formKey)).append("\n");
    sb.append("    campiTecnici: ").append(toIndentedString(campiTecnici)).append("\n");
    sb.append("    esterna: ").append(toIndentedString(esterna)).append("\n");
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

