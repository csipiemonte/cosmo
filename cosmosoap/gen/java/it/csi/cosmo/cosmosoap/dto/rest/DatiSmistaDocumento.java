/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoElettronico;
import it.csi.cosmo.cosmosoap.dto.rest.EmbeddedJSON;
import it.csi.cosmo.cosmosoap.dto.rest.EmbeddedXML;
import it.csi.cosmo.cosmosoap.dto.rest.Metadati;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DatiSmistaDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String responsabileTrattamento = null;
  private String idDocumentoFruitore = null;
  private DocumentoElettronico documentoElettronico = null;
  private EmbeddedXML datiDocumentoXML = null;
  private EmbeddedJSON datiDocumentoJSON = null;
  private Metadati metadati = null;
  private String messageUUIDPrincipale = null;
  private Integer numAllegati = null;
  private String uuidNodo = null;
  private Integer numCopieMulticlassificazione = null;
  private String indiceClassificazioneEsteso = null;
  private String nodoArchiviazione = null;

  /**
   **/
  


  // nome originario nello yaml: responsabileTrattamento 
  @NotNull
  @Pattern(regexp="[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][A-Z0-9]{3}[A-Z]")
  public String getResponsabileTrattamento() {
    return responsabileTrattamento;
  }
  public void setResponsabileTrattamento(String responsabileTrattamento) {
    this.responsabileTrattamento = responsabileTrattamento;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumentoFruitore 
  @NotNull
  @Size(max=200)
  public String getIdDocumentoFruitore() {
    return idDocumentoFruitore;
  }
  public void setIdDocumentoFruitore(String idDocumentoFruitore) {
    this.idDocumentoFruitore = idDocumentoFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: documentoElettronico 
  @NotNull
  public DocumentoElettronico getDocumentoElettronico() {
    return documentoElettronico;
  }
  public void setDocumentoElettronico(DocumentoElettronico documentoElettronico) {
    this.documentoElettronico = documentoElettronico;
  }

  /**
   **/
  


  // nome originario nello yaml: datiDocumentoXML 
  public EmbeddedXML getDatiDocumentoXML() {
    return datiDocumentoXML;
  }
  public void setDatiDocumentoXML(EmbeddedXML datiDocumentoXML) {
    this.datiDocumentoXML = datiDocumentoXML;
  }

  /**
   **/
  


  // nome originario nello yaml: datiDocumentoJSON 
  public EmbeddedJSON getDatiDocumentoJSON() {
    return datiDocumentoJSON;
  }
  public void setDatiDocumentoJSON(EmbeddedJSON datiDocumentoJSON) {
    this.datiDocumentoJSON = datiDocumentoJSON;
  }

  /**
   **/
  


  // nome originario nello yaml: metadati 
  public Metadati getMetadati() {
    return metadati;
  }
  public void setMetadati(Metadati metadati) {
    this.metadati = metadati;
  }

  /**
   **/
  


  // nome originario nello yaml: messageUUIDPrincipale 
  @Size(max=50)
  public String getMessageUUIDPrincipale() {
    return messageUUIDPrincipale;
  }
  public void setMessageUUIDPrincipale(String messageUUIDPrincipale) {
    this.messageUUIDPrincipale = messageUUIDPrincipale;
  }

  /**
   **/
  


  // nome originario nello yaml: numAllegati 
  public Integer getNumAllegati() {
    return numAllegati;
  }
  public void setNumAllegati(Integer numAllegati) {
    this.numAllegati = numAllegati;
  }

  /**
   **/
  


  // nome originario nello yaml: uuidNodo 
  @NotNull
  public String getUuidNodo() {
    return uuidNodo;
  }
  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  /**
   **/
  


  // nome originario nello yaml: numCopieMulticlassificazione 
  public Integer getNumCopieMulticlassificazione() {
    return numCopieMulticlassificazione;
  }
  public void setNumCopieMulticlassificazione(Integer numCopieMulticlassificazione) {
    this.numCopieMulticlassificazione = numCopieMulticlassificazione;
  }

  /**
   **/
  


  // nome originario nello yaml: indiceClassificazioneEsteso 
  public String getIndiceClassificazioneEsteso() {
    return indiceClassificazioneEsteso;
  }
  public void setIndiceClassificazioneEsteso(String indiceClassificazioneEsteso) {
    this.indiceClassificazioneEsteso = indiceClassificazioneEsteso;
  }

  /**
   **/
  


  // nome originario nello yaml: nodoArchiviazione 
  public String getNodoArchiviazione() {
    return nodoArchiviazione;
  }
  public void setNodoArchiviazione(String nodoArchiviazione) {
    this.nodoArchiviazione = nodoArchiviazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatiSmistaDocumento datiSmistaDocumento = (DatiSmistaDocumento) o;
    return Objects.equals(responsabileTrattamento, datiSmistaDocumento.responsabileTrattamento) &&
        Objects.equals(idDocumentoFruitore, datiSmistaDocumento.idDocumentoFruitore) &&
        Objects.equals(documentoElettronico, datiSmistaDocumento.documentoElettronico) &&
        Objects.equals(datiDocumentoXML, datiSmistaDocumento.datiDocumentoXML) &&
        Objects.equals(datiDocumentoJSON, datiSmistaDocumento.datiDocumentoJSON) &&
        Objects.equals(metadati, datiSmistaDocumento.metadati) &&
        Objects.equals(messageUUIDPrincipale, datiSmistaDocumento.messageUUIDPrincipale) &&
        Objects.equals(numAllegati, datiSmistaDocumento.numAllegati) &&
        Objects.equals(uuidNodo, datiSmistaDocumento.uuidNodo) &&
        Objects.equals(numCopieMulticlassificazione, datiSmistaDocumento.numCopieMulticlassificazione) &&
        Objects.equals(indiceClassificazioneEsteso, datiSmistaDocumento.indiceClassificazioneEsteso) &&
        Objects.equals(nodoArchiviazione, datiSmistaDocumento.nodoArchiviazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responsabileTrattamento, idDocumentoFruitore, documentoElettronico, datiDocumentoXML, datiDocumentoJSON, metadati, messageUUIDPrincipale, numAllegati, uuidNodo, numCopieMulticlassificazione, indiceClassificazioneEsteso, nodoArchiviazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiSmistaDocumento {\n");
    
    sb.append("    responsabileTrattamento: ").append(toIndentedString(responsabileTrattamento)).append("\n");
    sb.append("    idDocumentoFruitore: ").append(toIndentedString(idDocumentoFruitore)).append("\n");
    sb.append("    documentoElettronico: ").append(toIndentedString(documentoElettronico)).append("\n");
    sb.append("    datiDocumentoXML: ").append(toIndentedString(datiDocumentoXML)).append("\n");
    sb.append("    datiDocumentoJSON: ").append(toIndentedString(datiDocumentoJSON)).append("\n");
    sb.append("    metadati: ").append(toIndentedString(metadati)).append("\n");
    sb.append("    messageUUIDPrincipale: ").append(toIndentedString(messageUUIDPrincipale)).append("\n");
    sb.append("    numAllegati: ").append(toIndentedString(numAllegati)).append("\n");
    sb.append("    uuidNodo: ").append(toIndentedString(uuidNodo)).append("\n");
    sb.append("    numCopieMulticlassificazione: ").append(toIndentedString(numCopieMulticlassificazione)).append("\n");
    sb.append("    indiceClassificazioneEsteso: ").append(toIndentedString(indiceClassificazioneEsteso)).append("\n");
    sb.append("    nodoArchiviazione: ").append(toIndentedString(nodoArchiviazione)).append("\n");
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

