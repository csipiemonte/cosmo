/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmoauthorization.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoCredenzialiFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoOTP;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ImpostazioniFirma  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<EnteCertificatore> entiCertificatori = new ArrayList<>();
  private List<ProfiloFEQ> profiliFEQ = new ArrayList<>();
  private List<SceltaMarcaTemporale> scelteMarcaTemporale = new ArrayList<>();
  private List<TipoCredenzialiFirma> tipiCredenzialiFirma = new ArrayList<>();
  private List<TipoOTP> tipiOTP = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: entiCertificatori 
  public List<EnteCertificatore> getEntiCertificatori() {
    return entiCertificatori;
  }
  public void setEntiCertificatori(List<EnteCertificatore> entiCertificatori) {
    this.entiCertificatori = entiCertificatori;
  }

  /**
   **/
  


  // nome originario nello yaml: profiliFEQ 
  public List<ProfiloFEQ> getProfiliFEQ() {
    return profiliFEQ;
  }
  public void setProfiliFEQ(List<ProfiloFEQ> profiliFEQ) {
    this.profiliFEQ = profiliFEQ;
  }

  /**
   **/
  


  // nome originario nello yaml: scelteMarcaTemporale 
  public List<SceltaMarcaTemporale> getScelteMarcaTemporale() {
    return scelteMarcaTemporale;
  }
  public void setScelteMarcaTemporale(List<SceltaMarcaTemporale> scelteMarcaTemporale) {
    this.scelteMarcaTemporale = scelteMarcaTemporale;
  }

  /**
   **/
  


  // nome originario nello yaml: tipiCredenzialiFirma 
  public List<TipoCredenzialiFirma> getTipiCredenzialiFirma() {
    return tipiCredenzialiFirma;
  }
  public void setTipiCredenzialiFirma(List<TipoCredenzialiFirma> tipiCredenzialiFirma) {
    this.tipiCredenzialiFirma = tipiCredenzialiFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: tipiOTP 
  public List<TipoOTP> getTipiOTP() {
    return tipiOTP;
  }
  public void setTipiOTP(List<TipoOTP> tipiOTP) {
    this.tipiOTP = tipiOTP;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpostazioniFirma impostazioniFirma = (ImpostazioniFirma) o;
    return Objects.equals(entiCertificatori, impostazioniFirma.entiCertificatori) &&
        Objects.equals(profiliFEQ, impostazioniFirma.profiliFEQ) &&
        Objects.equals(scelteMarcaTemporale, impostazioniFirma.scelteMarcaTemporale) &&
        Objects.equals(tipiCredenzialiFirma, impostazioniFirma.tipiCredenzialiFirma) &&
        Objects.equals(tipiOTP, impostazioniFirma.tipiOTP);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entiCertificatori, profiliFEQ, scelteMarcaTemporale, tipiCredenzialiFirma, tipiOTP);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpostazioniFirma {\n");
    
    sb.append("    entiCertificatori: ").append(toIndentedString(entiCertificatori)).append("\n");
    sb.append("    profiliFEQ: ").append(toIndentedString(profiliFEQ)).append("\n");
    sb.append("    scelteMarcaTemporale: ").append(toIndentedString(scelteMarcaTemporale)).append("\n");
    sb.append("    tipiCredenzialiFirma: ").append(toIndentedString(tipiCredenzialiFirma)).append("\n");
    sb.append("    tipiOTP: ").append(toIndentedString(tipiOTP)).append("\n");
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

