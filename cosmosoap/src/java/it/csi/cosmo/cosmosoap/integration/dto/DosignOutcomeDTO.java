/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.csi.cosmo.cosmosoap.business.service.DoSignService;


/**
 *
 */

public class DosignOutcomeDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 3057746933454108262L;

  @JsonProperty ( "Status" )
  private String status;

  @JsonProperty ( "Description" )
  private String description;

  @JsonProperty ( "ReturnCode" )
  private String returnCode;

  public DosignOutcomeDTO ( String status, String description, String returnCode ) {
    super ();
    this.status = status;
    this.description = description;
    this.returnCode = returnCode;
  }

  public DosignOutcomeDTO () {
    this.status = DoSignService.DEFAULT_ERROR_STATUS;
    this.description = DoSignService.DEFAULT_ERROR_DESCRIPTION;
    this.returnCode = DoSignService.DEFAULT_ERROR_RETURN_CODE;
  }

  /**
   * @return the status
   */
  public String getStatus () {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus ( String status ) {
    this.status = status;
  }

  /**
   * @return the description
   */
  public String getDescription () {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription ( String description ) {
    this.description = description;
  }

  /**
   * @return the returnCode
   */
  public String getReturnCode () {
    return returnCode;
  }

  /**
   * @param returnCode the returnCode to set
   */
  public void setReturnCode ( String returnCode ) {
    this.returnCode = returnCode;
  }

}
