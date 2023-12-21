/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsterna;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ApplicazioneEsternaConValidita  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String descrizione = null;
  private String icona = null;
  private FunzionalitaApplicazioneEsterna funzionalitaPrincipale = null;
  private CampiTecnici campiTecnici = null;
  private Boolean associataUtenti = null;
  private Boolean associataEnti = null;
  private Long numEntiAssociati = null;

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
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: icona 
  @NotNull
  public String getIcona() {
    return icona;
  }
  public void setIcona(String icona) {
    this.icona = icona;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalitaPrincipale 
  public FunzionalitaApplicazioneEsterna getFunzionalitaPrincipale() {
    return funzionalitaPrincipale;
  }
  public void setFunzionalitaPrincipale(FunzionalitaApplicazioneEsterna funzionalitaPrincipale) {
    this.funzionalitaPrincipale = funzionalitaPrincipale;
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
  


  // nome originario nello yaml: associataUtenti 
  public Boolean isAssociataUtenti() {
    return associataUtenti;
  }
  public void setAssociataUtenti(Boolean associataUtenti) {
    this.associataUtenti = associataUtenti;
  }

  /**
   **/
  


  // nome originario nello yaml: associataEnti 
  public Boolean isAssociataEnti() {
    return associataEnti;
  }
  public void setAssociataEnti(Boolean associataEnti) {
    this.associataEnti = associataEnti;
  }

  /**
   **/
  


  // nome originario nello yaml: numEntiAssociati 
  public Long getNumEntiAssociati() {
    return numEntiAssociati;
  }
  public void setNumEntiAssociati(Long numEntiAssociati) {
    this.numEntiAssociati = numEntiAssociati;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicazioneEsternaConValidita applicazioneEsternaConValidita = (ApplicazioneEsternaConValidita) o;
    return Objects.equals(id, applicazioneEsternaConValidita.id) &&
        Objects.equals(descrizione, applicazioneEsternaConValidita.descrizione) &&
        Objects.equals(icona, applicazioneEsternaConValidita.icona) &&
        Objects.equals(funzionalitaPrincipale, applicazioneEsternaConValidita.funzionalitaPrincipale) &&
        Objects.equals(campiTecnici, applicazioneEsternaConValidita.campiTecnici) &&
        Objects.equals(associataUtenti, applicazioneEsternaConValidita.associataUtenti) &&
        Objects.equals(associataEnti, applicazioneEsternaConValidita.associataEnti) &&
        Objects.equals(numEntiAssociati, applicazioneEsternaConValidita.numEntiAssociati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizione, icona, funzionalitaPrincipale, campiTecnici, associataUtenti, associataEnti, numEntiAssociati);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicazioneEsternaConValidita {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    icona: ").append(toIndentedString(icona)).append("\n");
    sb.append("    funzionalitaPrincipale: ").append(toIndentedString(funzionalitaPrincipale)).append("\n");
    sb.append("    campiTecnici: ").append(toIndentedString(campiTecnici)).append("\n");
    sb.append("    associataUtenti: ").append(toIndentedString(associataUtenti)).append("\n");
    sb.append("    associataEnti: ").append(toIndentedString(associataEnti)).append("\n");
    sb.append("    numEntiAssociati: ").append(toIndentedString(numEntiAssociati)).append("\n");
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

