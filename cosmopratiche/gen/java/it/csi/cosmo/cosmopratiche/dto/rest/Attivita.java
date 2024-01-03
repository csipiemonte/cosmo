/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione;
import it.csi.cosmo.cosmopratiche.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaAttivita;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Attivita  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private Integer idPratica = null;
  private String linkAttivita = null;
  private String linkAttivitaEsterna = null;
  private String nome = null;
  private String descrizione = null;
  private String evento = null;
  private List<Assegnazione> assegnazione = new ArrayList<>();
  private String parent = null;
  private OffsetDateTime dataCancellazione = null;
  private CampiTecnici campiTecnici = null;
  private String formKey = null;
  private Boolean esterna = null;
  private List<FunzionalitaAttivita> funzionalita = new ArrayList<>();
  private String gruppoAssegnatario = null;
  private Boolean hasChildren = null;

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
  


  // nome originario nello yaml: idPratica 
  public Integer getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Integer idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: linkAttivita 
  public String getLinkAttivita() {
    return linkAttivita;
  }
  public void setLinkAttivita(String linkAttivita) {
    this.linkAttivita = linkAttivita;
  }

  /**
   **/
  


  // nome originario nello yaml: linkAttivitaEsterna 
  public String getLinkAttivitaEsterna() {
    return linkAttivitaEsterna;
  }
  public void setLinkAttivitaEsterna(String linkAttivitaEsterna) {
    this.linkAttivitaEsterna = linkAttivitaEsterna;
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
  


  // nome originario nello yaml: evento 
  public String getEvento() {
    return evento;
  }
  public void setEvento(String evento) {
    this.evento = evento;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnazione 
  public List<Assegnazione> getAssegnazione() {
    return assegnazione;
  }
  public void setAssegnazione(List<Assegnazione> assegnazione) {
    this.assegnazione = assegnazione;
  }

  /**
   **/
  


  // nome originario nello yaml: parent 
  public String getParent() {
    return parent;
  }
  public void setParent(String parent) {
    this.parent = parent;
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
  


  // nome originario nello yaml: campiTecnici 
  public CampiTecnici getCampiTecnici() {
    return campiTecnici;
  }
  public void setCampiTecnici(CampiTecnici campiTecnici) {
    this.campiTecnici = campiTecnici;
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
  


  // nome originario nello yaml: esterna 
  public Boolean isEsterna() {
    return esterna;
  }
  public void setEsterna(Boolean esterna) {
    this.esterna = esterna;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalita 
  public List<FunzionalitaAttivita> getFunzionalita() {
    return funzionalita;
  }
  public void setFunzionalita(List<FunzionalitaAttivita> funzionalita) {
    this.funzionalita = funzionalita;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppoAssegnatario 
  public String getGruppoAssegnatario() {
    return gruppoAssegnatario;
  }
  public void setGruppoAssegnatario(String gruppoAssegnatario) {
    this.gruppoAssegnatario = gruppoAssegnatario;
  }

  /**
   **/
  


  // nome originario nello yaml: hasChildren 
  public Boolean isHasChildren() {
    return hasChildren;
  }
  public void setHasChildren(Boolean hasChildren) {
    this.hasChildren = hasChildren;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attivita attivita = (Attivita) o;
    return Objects.equals(id, attivita.id) &&
        Objects.equals(idPratica, attivita.idPratica) &&
        Objects.equals(linkAttivita, attivita.linkAttivita) &&
        Objects.equals(linkAttivitaEsterna, attivita.linkAttivitaEsterna) &&
        Objects.equals(nome, attivita.nome) &&
        Objects.equals(descrizione, attivita.descrizione) &&
        Objects.equals(evento, attivita.evento) &&
        Objects.equals(assegnazione, attivita.assegnazione) &&
        Objects.equals(parent, attivita.parent) &&
        Objects.equals(dataCancellazione, attivita.dataCancellazione) &&
        Objects.equals(campiTecnici, attivita.campiTecnici) &&
        Objects.equals(formKey, attivita.formKey) &&
        Objects.equals(esterna, attivita.esterna) &&
        Objects.equals(funzionalita, attivita.funzionalita) &&
        Objects.equals(gruppoAssegnatario, attivita.gruppoAssegnatario) &&
        Objects.equals(hasChildren, attivita.hasChildren);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idPratica, linkAttivita, linkAttivitaEsterna, nome, descrizione, evento, assegnazione, parent, dataCancellazione, campiTecnici, formKey, esterna, funzionalita, gruppoAssegnatario, hasChildren);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attivita {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    linkAttivita: ").append(toIndentedString(linkAttivita)).append("\n");
    sb.append("    linkAttivitaEsterna: ").append(toIndentedString(linkAttivitaEsterna)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    evento: ").append(toIndentedString(evento)).append("\n");
    sb.append("    assegnazione: ").append(toIndentedString(assegnazione)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    dataCancellazione: ").append(toIndentedString(dataCancellazione)).append("\n");
    sb.append("    campiTecnici: ").append(toIndentedString(campiTecnici)).append("\n");
    sb.append("    formKey: ").append(toIndentedString(formKey)).append("\n");
    sb.append("    esterna: ").append(toIndentedString(esterna)).append("\n");
    sb.append("    funzionalita: ").append(toIndentedString(funzionalita)).append("\n");
    sb.append("    gruppoAssegnatario: ").append(toIndentedString(gruppoAssegnatario)).append("\n");
    sb.append("    hasChildren: ").append(toIndentedString(hasChildren)).append("\n");
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

