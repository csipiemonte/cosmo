/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.log4j.Logger;


/**
 *
 */

public class ServiceStatusDTO {

  private static final Logger logger = Logger.getLogger("cosmo.cosmo.ServiceStatusDTO");

  public static ServiceStatusDTO.Builder of(boolean bool) {
    if (bool) {
      return up ();
    } else {
      return down ();
    }
  }

  public static ServiceStatusDTO.Builder of(BooleanSupplier test,
      ServiceStatusEnum inCaseOfFailure) {

    boolean bool;
    try {
      bool = test.getAsBoolean();
    } catch (Exception e) {
      logger.error("error checking service status", e);
      return ServiceStatusDTO.builder().withMessage("Service is down (error checking for status)")
          .withStatus(inCaseOfFailure);
    }

    if (bool) {
      return up();
    } else {
      return ServiceStatusDTO.builder().withMessage("Service is down (reported failure)")
          .withStatus(inCaseOfFailure);
    }
  }

  public static ServiceStatusDTO.Builder up () {
    return ServiceStatusDTO.builder ()
        .withMessage ( "Service is UP" )
        .withStatus(ServiceStatusEnum.UP);
  }

  public static ServiceStatusDTO.Builder down () {
    return ServiceStatusDTO.builder ()
        .withMessage ( "Service is DOWN" )
        .withStatus(ServiceStatusEnum.DOWN);
  }

  public static ServiceStatusDTO.Builder warning() {
    return ServiceStatusDTO.builder().withMessage("Service is in WARNING")
        .withStatus(ServiceStatusEnum.WARNING);
  }

  public static ServiceStatusDTO.Builder unknown () {
    return ServiceStatusDTO.builder ()
        .withMessage ( "Service is UNKNOWN" )
        .withStatus(ServiceStatusEnum.UNKNOWN);
  }

  private ServiceStatusEnum status;

  private Long responseTime;

  private String name;

  private String message;

  private Map<String, Object> details;

  private ServiceStatusDTO ( Builder builder ) {
    status = builder.status;
    message = builder.message;
    details = builder.details;
    name = builder.name;
    responseTime = builder.responseTime;
  }

  public Long getResponseTime () {
    return responseTime;
  }

  public void setResponseTime ( Long responseTime ) {
    this.responseTime = responseTime;
  }

  public String getName () {
    return name;
  }

  public void setName ( String name ) {
    this.name = name;
  }

  public ServiceStatusEnum getStatus() {
    return status;
  }

  public void setStatus(ServiceStatusEnum status) {
    this.status = status;
  }

  public String getMessage () {
    return message;
  }

  public void setMessage ( String message ) {
    this.message = message;
  }

  public Map<String, Object> getDetails () {
    return details;
  }

  public void setDetails ( Map<String, Object> details ) {
    this.details = details;
  }

  /**
   * Creates builder to build {@link ServiceStatusDTO}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder ();
  }

  /**
   * Builder to build {@link ServiceStatusDTO}.
   */
  public static final class Builder {

    private ServiceStatusEnum status;

    private String name;

    private String message;

    private Long responseTime;

    private Map<String, Object> details = new HashMap<> ();

    private Builder () {
    }

    public Builder withStatus(ServiceStatusEnum status) {
      this.status = status;
      return this;
    }

    public Builder withResponseTime ( Long responseTime ) {
      this.responseTime = responseTime;
      return this;
    }

    public Builder withName ( String name ) {
      this.name = name;
      return this;
    }

    public Builder withMessage ( String message ) {
      this.message = message;
      return this;
    }

    public Builder withDetails ( Map<String, Object> details ) {
      this.details = details;
      return this;
    }

    public Builder withDetail ( String key, Object value ) {
      details.put ( key, value );
      return this;
    }

    public ServiceStatusDTO build () {
      return new ServiceStatusDTO ( this );
    }
  }

}

