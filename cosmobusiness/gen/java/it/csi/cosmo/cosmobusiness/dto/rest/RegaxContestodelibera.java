/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;
@SuppressWarnings("unused")
public class RegaxContestodelibera  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long contdelCodice = null;

  private String contdelContestodelibera = null;

  /**
   **/
  
  @JsonProperty("contdel_codice")
  public Long getContdelCodice() {
    return contdelCodice;
  }
  public void setContdelCodice(Long contdelCodice) {
    this.contdelCodice = contdelCodice;
  }

  /**
   **/
  
  @JsonProperty("contdel_contestodelibera")
  public String getContdelContestodelibera() {
    return contdelContestodelibera;
  }
  public void setContdelContestodelibera(String contdelContestodelibera) {
    this.contdelContestodelibera = contdelContestodelibera;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxContestodelibera regaxContestodelibera = (RegaxContestodelibera) o;
    return Objects.equals(contdelCodice, regaxContestodelibera.contdelCodice) &&
        Objects.equals(contdelContestodelibera, regaxContestodelibera.contdelContestodelibera);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contdelCodice, contdelContestodelibera);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxContestodelibera {\n");
    
    sb.append("    contdelCodice: ").append(toIndentedString(contdelCodice)).append("\n");
    sb.append("    contdelContestodelibera: ").append(toIndentedString(contdelContestodelibera)).append("\n");
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

