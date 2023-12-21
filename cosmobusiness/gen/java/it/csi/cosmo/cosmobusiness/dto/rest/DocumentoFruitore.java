/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.ArchiviazioneDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.FirmaDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoDocumentoFruitore;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentoFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String idPadre = null;
  private String nomeFile = null;
  private Long dimensione = null;
  private String titolo = null;
  private String descrizione = null;
  private String autore = null;
  private String mimeType = null;
  private List<FirmaDocumentoFruitore> firme = new ArrayList<>();
  private ArchiviazioneDocumentoFruitore archiviazione = null;
  private String refURL = null;
  private TipoDocumentoFruitore tipo = null;
  private StatoDocumentoFruitore stato = null;
  private String idCosmo = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: idPadre 
  public String getIdPadre() {
    return idPadre;
  }
  public void setIdPadre(String idPadre) {
    this.idPadre = idPadre;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: dimensione 
  public Long getDimensione() {
    return dimensione;
  }
  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  /**
   **/
  


  // nome originario nello yaml: titolo 
  public String getTitolo() {
    return titolo;
  }
  public void setTitolo(String titolo) {
    this.titolo = titolo;
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
  


  // nome originario nello yaml: autore 
  public String getAutore() {
    return autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: firme 
  public List<FirmaDocumentoFruitore> getFirme() {
    return firme;
  }
  public void setFirme(List<FirmaDocumentoFruitore> firme) {
    this.firme = firme;
  }

  /**
   **/
  


  // nome originario nello yaml: archiviazione 
  public ArchiviazioneDocumentoFruitore getArchiviazione() {
    return archiviazione;
  }
  public void setArchiviazione(ArchiviazioneDocumentoFruitore archiviazione) {
    this.archiviazione = archiviazione;
  }

  /**
   **/
  


  // nome originario nello yaml: refURL 
  public String getRefURL() {
    return refURL;
  }
  public void setRefURL(String refURL) {
    this.refURL = refURL;
  }

  /**
   **/
  


  // nome originario nello yaml: tipo 
  public TipoDocumentoFruitore getTipo() {
    return tipo;
  }
  public void setTipo(TipoDocumentoFruitore tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  @NotNull
  public StatoDocumentoFruitore getStato() {
    return stato;
  }
  public void setStato(StatoDocumentoFruitore stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: idCosmo 
  public String getIdCosmo() {
    return idCosmo;
  }
  public void setIdCosmo(String idCosmo) {
    this.idCosmo = idCosmo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoFruitore documentoFruitore = (DocumentoFruitore) o;
    return Objects.equals(id, documentoFruitore.id) &&
        Objects.equals(idPadre, documentoFruitore.idPadre) &&
        Objects.equals(nomeFile, documentoFruitore.nomeFile) &&
        Objects.equals(dimensione, documentoFruitore.dimensione) &&
        Objects.equals(titolo, documentoFruitore.titolo) &&
        Objects.equals(descrizione, documentoFruitore.descrizione) &&
        Objects.equals(autore, documentoFruitore.autore) &&
        Objects.equals(mimeType, documentoFruitore.mimeType) &&
        Objects.equals(firme, documentoFruitore.firme) &&
        Objects.equals(archiviazione, documentoFruitore.archiviazione) &&
        Objects.equals(refURL, documentoFruitore.refURL) &&
        Objects.equals(tipo, documentoFruitore.tipo) &&
        Objects.equals(stato, documentoFruitore.stato) &&
        Objects.equals(idCosmo, documentoFruitore.idCosmo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idPadre, nomeFile, dimensione, titolo, descrizione, autore, mimeType, firme, archiviazione, refURL, tipo, stato, idCosmo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoFruitore {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idPadre: ").append(toIndentedString(idPadre)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    dimensione: ").append(toIndentedString(dimensione)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    firme: ").append(toIndentedString(firme)).append("\n");
    sb.append("    archiviazione: ").append(toIndentedString(archiviazione)).append("\n");
    sb.append("    refURL: ").append(toIndentedString(refURL)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    idCosmo: ").append(toIndentedString(idCosmo)).append("\n");
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

