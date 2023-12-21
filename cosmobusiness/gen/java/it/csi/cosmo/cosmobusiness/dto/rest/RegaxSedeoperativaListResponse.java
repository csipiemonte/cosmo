/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxErrore;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedeoperativa;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;
@SuppressWarnings("unused")
public class RegaxSedeoperativaListResponse  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Boolean success = null;

  private String version = null;

  private Long totalRecordCount = null;

  private List<RegaxSedeoperativa> sedeoperativa = new ArrayList<>();

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
  
  @JsonProperty("sedeoperativa")
  public List<RegaxSedeoperativa> getSedeoperativa() {
    return sedeoperativa;
  }
  public void setSedeoperativa(List<RegaxSedeoperativa> sedeoperativa) {
    this.sedeoperativa = sedeoperativa;
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
    RegaxSedeoperativaListResponse regaxSedeoperativaListResponse = (RegaxSedeoperativaListResponse) o;
    return Objects.equals(success, regaxSedeoperativaListResponse.success) &&
        Objects.equals(version, regaxSedeoperativaListResponse.version) &&
        Objects.equals(totalRecordCount, regaxSedeoperativaListResponse.totalRecordCount) &&
        Objects.equals(sedeoperativa, regaxSedeoperativaListResponse.sedeoperativa) &&
        Objects.equals(statusCode, regaxSedeoperativaListResponse.statusCode) &&
        Objects.equals(error, regaxSedeoperativaListResponse.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, version, totalRecordCount, sedeoperativa, statusCode, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSedeoperativaListResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    totalRecordCount: ").append(toIndentedString(totalRecordCount)).append("\n");
    sb.append("    sedeoperativa: ").append(toIndentedString(sedeoperativa)).append("\n");
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

