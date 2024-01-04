/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.EstremiRegNumType;
import it.csi.cosmo.cosmosoap.dto.rest.VersioneElettronicaNonCaricata;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class OutputUnitaDocumentaria  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idUD = null;
  private List<EstremiRegNumType> registrazioneDataUD = new ArrayList<>();
  private List<VersioneElettronicaNonCaricata> versioneElettronicaNonCaricata = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idUD 
  public String getIdUD() {
    return idUD;
  }
  public void setIdUD(String idUD) {
    this.idUD = idUD;
  }

  /**
   **/
  


  // nome originario nello yaml: registrazioneDataUD 
  public List<EstremiRegNumType> getRegistrazioneDataUD() {
    return registrazioneDataUD;
  }
  public void setRegistrazioneDataUD(List<EstremiRegNumType> registrazioneDataUD) {
    this.registrazioneDataUD = registrazioneDataUD;
  }

  /**
   **/
  


  // nome originario nello yaml: versioneElettronicaNonCaricata 
  public List<VersioneElettronicaNonCaricata> getVersioneElettronicaNonCaricata() {
    return versioneElettronicaNonCaricata;
  }
  public void setVersioneElettronicaNonCaricata(List<VersioneElettronicaNonCaricata> versioneElettronicaNonCaricata) {
    this.versioneElettronicaNonCaricata = versioneElettronicaNonCaricata;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutputUnitaDocumentaria outputUnitaDocumentaria = (OutputUnitaDocumentaria) o;
    return Objects.equals(idUD, outputUnitaDocumentaria.idUD) &&
        Objects.equals(registrazioneDataUD, outputUnitaDocumentaria.registrazioneDataUD) &&
        Objects.equals(versioneElettronicaNonCaricata, outputUnitaDocumentaria.versioneElettronicaNonCaricata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUD, registrazioneDataUD, versioneElettronicaNonCaricata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutputUnitaDocumentaria {\n");
    
    sb.append("    idUD: ").append(toIndentedString(idUD)).append("\n");
    sb.append("    registrazioneDataUD: ").append(toIndentedString(registrazioneDataUD)).append("\n");
    sb.append("    versioneElettronicaNonCaricata: ").append(toIndentedString(versioneElettronicaNonCaricata)).append("\n");
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

