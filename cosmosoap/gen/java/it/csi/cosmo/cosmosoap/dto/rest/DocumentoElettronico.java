/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.EmbeddedBinary;
import it.csi.cosmo.cosmosoap.dto.rest.RiferimentoECM;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentoElettronico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeFile = null;
  private EmbeddedBinary contenutoBinario = null;
  private RiferimentoECM riferimentoECM = null;
  private Boolean documentoFirmato = null;
  private String mimeType = null;

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  @NotNull
  @Size(max=4000)
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: contenutoBinario 
  public EmbeddedBinary getContenutoBinario() {
    return contenutoBinario;
  }
  public void setContenutoBinario(EmbeddedBinary contenutoBinario) {
    this.contenutoBinario = contenutoBinario;
  }

  /**
   **/
  


  // nome originario nello yaml: riferimentoECM 
  public RiferimentoECM getRiferimentoECM() {
    return riferimentoECM;
  }
  public void setRiferimentoECM(RiferimentoECM riferimentoECM) {
    this.riferimentoECM = riferimentoECM;
  }

  /**
   **/
  


  // nome originario nello yaml: documentoFirmato 
  public Boolean isDocumentoFirmato() {
    return documentoFirmato;
  }
  public void setDocumentoFirmato(Boolean documentoFirmato) {
    this.documentoFirmato = documentoFirmato;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  @NotNull
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoElettronico documentoElettronico = (DocumentoElettronico) o;
    return Objects.equals(nomeFile, documentoElettronico.nomeFile) &&
        Objects.equals(contenutoBinario, documentoElettronico.contenutoBinario) &&
        Objects.equals(riferimentoECM, documentoElettronico.riferimentoECM) &&
        Objects.equals(documentoFirmato, documentoElettronico.documentoFirmato) &&
        Objects.equals(mimeType, documentoElettronico.mimeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, contenutoBinario, riferimentoECM, documentoFirmato, mimeType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoElettronico {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    contenutoBinario: ").append(toIndentedString(contenutoBinario)).append("\n");
    sb.append("    riferimentoECM: ").append(toIndentedString(riferimentoECM)).append("\n");
    sb.append("    documentoFirmato: ").append(toIndentedString(documentoFirmato)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
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

