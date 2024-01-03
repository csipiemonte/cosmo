/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TemplateFea  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String descrizione = null;
  private RiferimentoEnte ente = null;
  private TipoPratica tipoPratica = null;
  private TipoDocumento tipoDocumento = null;
  private Double coordinataX = null;
  private Double coordinataY = null;
  private Long pagina = null;
  private Long idPratica = null;
  private Boolean caricatoDaTemplate = null;

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
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  public TipoPratica getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(TipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoDocumento 
  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }
  public void setTipoDocumento(TipoDocumento tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: coordinataX 
  public Double getCoordinataX() {
    return coordinataX;
  }
  public void setCoordinataX(Double coordinataX) {
    this.coordinataX = coordinataX;
  }

  /**
   **/
  


  // nome originario nello yaml: coordinataY 
  public Double getCoordinataY() {
    return coordinataY;
  }
  public void setCoordinataY(Double coordinataY) {
    this.coordinataY = coordinataY;
  }

  /**
   **/
  


  // nome originario nello yaml: pagina 
  public Long getPagina() {
    return pagina;
  }
  public void setPagina(Long pagina) {
    this.pagina = pagina;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: caricatoDaTemplate 
  public Boolean isCaricatoDaTemplate() {
    return caricatoDaTemplate;
  }
  public void setCaricatoDaTemplate(Boolean caricatoDaTemplate) {
    this.caricatoDaTemplate = caricatoDaTemplate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateFea templateFea = (TemplateFea) o;
    return Objects.equals(id, templateFea.id) &&
        Objects.equals(descrizione, templateFea.descrizione) &&
        Objects.equals(ente, templateFea.ente) &&
        Objects.equals(tipoPratica, templateFea.tipoPratica) &&
        Objects.equals(tipoDocumento, templateFea.tipoDocumento) &&
        Objects.equals(coordinataX, templateFea.coordinataX) &&
        Objects.equals(coordinataY, templateFea.coordinataY) &&
        Objects.equals(pagina, templateFea.pagina) &&
        Objects.equals(idPratica, templateFea.idPratica) &&
        Objects.equals(caricatoDaTemplate, templateFea.caricatoDaTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizione, ente, tipoPratica, tipoDocumento, coordinataX, coordinataY, pagina, idPratica, caricatoDaTemplate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateFea {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    tipoDocumento: ").append(toIndentedString(tipoDocumento)).append("\n");
    sb.append("    coordinataX: ").append(toIndentedString(coordinataX)).append("\n");
    sb.append("    coordinataY: ").append(toIndentedString(coordinataY)).append("\n");
    sb.append("    pagina: ").append(toIndentedString(pagina)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    caricatoDaTemplate: ").append(toIndentedString(caricatoDaTemplate)).append("\n");
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

