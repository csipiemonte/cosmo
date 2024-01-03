/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.ApprovazioneDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.SigilloDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.SmistamentoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.StatoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Documento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private String idDocumentoExt = null;
  private String titolo = null;
  private TipoDocumento tipo = null;
  private String descrizione = null;
  private String idDocumentoParentExt = null;
  private Long id = null;
  private String autore = null;
  private StatoDocumento stato = null;
  private List<ContenutoDocumento> contenuti = new ArrayList<>();
  private OffsetDateTime ultimaModifica = null;
  private SmistamentoDocumento smistamento = null;
  private List<Documento> allegati = new ArrayList<>();
  private List<ApprovazioneDocumento> approvazioni = new ArrayList<>();
  private List<SigilloDocumento> sigillo = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumentoExt 
  public String getIdDocumentoExt() {
    return idDocumentoExt;
  }
  public void setIdDocumentoExt(String idDocumentoExt) {
    this.idDocumentoExt = idDocumentoExt;
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
  


  // nome originario nello yaml: tipo 
  public TipoDocumento getTipo() {
    return tipo;
  }
  public void setTipo(TipoDocumento tipo) {
    this.tipo = tipo;
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
  


  // nome originario nello yaml: idDocumentoParentExt 
  public String getIdDocumentoParentExt() {
    return idDocumentoParentExt;
  }
  public void setIdDocumentoParentExt(String idDocumentoParentExt) {
    this.idDocumentoParentExt = idDocumentoParentExt;
  }

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
  


  // nome originario nello yaml: autore 
  public String getAutore() {
    return autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  public StatoDocumento getStato() {
    return stato;
  }
  public void setStato(StatoDocumento stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: contenuti 
  public List<ContenutoDocumento> getContenuti() {
    return contenuti;
  }
  public void setContenuti(List<ContenutoDocumento> contenuti) {
    this.contenuti = contenuti;
  }

  /**
   **/
  


  // nome originario nello yaml: ultimaModifica 
  public OffsetDateTime getUltimaModifica() {
    return ultimaModifica;
  }
  public void setUltimaModifica(OffsetDateTime ultimaModifica) {
    this.ultimaModifica = ultimaModifica;
  }

  /**
   **/
  


  // nome originario nello yaml: smistamento 
  public SmistamentoDocumento getSmistamento() {
    return smistamento;
  }
  public void setSmistamento(SmistamentoDocumento smistamento) {
    this.smistamento = smistamento;
  }

  /**
   **/
  


  // nome originario nello yaml: allegati 
  public List<Documento> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<Documento> allegati) {
    this.allegati = allegati;
  }

  /**
   **/
  


  // nome originario nello yaml: approvazioni 
  public List<ApprovazioneDocumento> getApprovazioni() {
    return approvazioni;
  }
  public void setApprovazioni(List<ApprovazioneDocumento> approvazioni) {
    this.approvazioni = approvazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: sigillo 
  public List<SigilloDocumento> getSigillo() {
    return sigillo;
  }
  public void setSigillo(List<SigilloDocumento> sigillo) {
    this.sigillo = sigillo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Documento documento = (Documento) o;
    return Objects.equals(idPratica, documento.idPratica) &&
        Objects.equals(idDocumentoExt, documento.idDocumentoExt) &&
        Objects.equals(titolo, documento.titolo) &&
        Objects.equals(tipo, documento.tipo) &&
        Objects.equals(descrizione, documento.descrizione) &&
        Objects.equals(idDocumentoParentExt, documento.idDocumentoParentExt) &&
        Objects.equals(id, documento.id) &&
        Objects.equals(autore, documento.autore) &&
        Objects.equals(stato, documento.stato) &&
        Objects.equals(contenuti, documento.contenuti) &&
        Objects.equals(ultimaModifica, documento.ultimaModifica) &&
        Objects.equals(smistamento, documento.smistamento) &&
        Objects.equals(allegati, documento.allegati) &&
        Objects.equals(approvazioni, documento.approvazioni) &&
        Objects.equals(sigillo, documento.sigillo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, idDocumentoExt, titolo, tipo, descrizione, idDocumentoParentExt, id, autore, stato, contenuti, ultimaModifica, smistamento, allegati, approvazioni, sigillo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Documento {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    idDocumentoExt: ").append(toIndentedString(idDocumentoExt)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    idDocumentoParentExt: ").append(toIndentedString(idDocumentoParentExt)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    contenuti: ").append(toIndentedString(contenuti)).append("\n");
    sb.append("    ultimaModifica: ").append(toIndentedString(ultimaModifica)).append("\n");
    sb.append("    smistamento: ").append(toIndentedString(smistamento)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
    sb.append("    approvazioni: ").append(toIndentedString(approvazioni)).append("\n");
    sb.append("    sigillo: ").append(toIndentedString(sigillo)).append("\n");
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

