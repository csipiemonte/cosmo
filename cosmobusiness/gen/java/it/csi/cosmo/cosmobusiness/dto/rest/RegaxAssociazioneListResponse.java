/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAssociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxErrore;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;
@SuppressWarnings("unused")
public class RegaxAssociazioneListResponse  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Boolean success = null;

  private String version = null;

  private Long totalRecordCount = null;

  private List<RegaxAssociazione> associazione = new ArrayList<>();

  private Integer statusCode = null;

  private RegaxErrore error = null;

  /**
   **/
  
  @JsonProperty("success")
  public Boolean Success() {
    return success;
  }
  public void setSuccess(Boolean success) {
    this.success = success;
  }

  /**
   **/
  
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   **/
  
  @JsonProperty("totalRecordCount")
  public Long getTotalRecordCount() {
    return totalRecordCount;
  }
  public void setTotalRecordCount(Long totalRecordCount) {
    this.totalRecordCount = totalRecordCount;
  }

  /**
   **/
  
  @JsonProperty("associazione")
  public List<RegaxAssociazione> getAssociazione() {
    return associazione;
  }
  public void setAssociazione(List<RegaxAssociazione> associazione) {
    this.associazione = associazione;
  }

  /**
   **/
  
  @JsonProperty("statusCode")
  public Integer getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  /**
   **/
  
  @JsonProperty("error")
  public RegaxErrore getError() {
    return error;
  }
  public void setError(RegaxErrore error) {
    this.error = error;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxAssociazioneListResponse regaxAssociazioneListResponse = (RegaxAssociazioneListResponse) o;
    return Objects.equals(success, regaxAssociazioneListResponse.success) &&
        Objects.equals(version, regaxAssociazioneListResponse.version) &&
        Objects.equals(totalRecordCount, regaxAssociazioneListResponse.totalRecordCount) &&
        Objects.equals(associazione, regaxAssociazioneListResponse.associazione) &&
        Objects.equals(statusCode, regaxAssociazioneListResponse.statusCode) &&
        Objects.equals(error, regaxAssociazioneListResponse.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, version, totalRecordCount, associazione, statusCode, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxAssociazioneListResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    totalRecordCount: ").append(toIndentedString(totalRecordCount)).append("\n");
    sb.append("    associazione: ").append(toIndentedString(associazione)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

