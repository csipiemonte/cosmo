/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/*
 * Copyright 2001-2019 CSI Piemonte. All Rights Reserved.
 *
 * This software is proprietary information of CSI Piemonte. Use is subject to license terms.
 *
 */

package it.csi.cosmo.cosmoecm.integration.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.client.DosignApi;
import it.csi.cosmo.cosmosoap.dto.rest.CommonRemoteData;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.EndSessionRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.StartSessionRequest;

/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/soap/dosign",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = CosmoSoapFeignClientConfiguration.class)
public interface CosmoSoapDosignFeignClient extends DosignApi {

  @Override
  @POST
  @Path("/end-session")
  @Consumes({"application/json"})
  void endSession(@Valid EndSessionRequest var1);

  @Override
  @POST
  @Path("/firma")
  @Consumes({"application/json"})
  void firma(@Valid DoSignFirmaRequest var1);

  @Override
  @POST
  @Path("/firma-massiva")
  @Consumes({"application/json"})
  void firmaMassiva(@Valid DoSignFirmaMassivaRequest var1);

  @Override
  @POST
  @Path("/richiedi-otp")
  @Consumes({"application/json"})
  void richiediOTP(@Valid RichiediOTPRequest var1);

  @Override
  @POST
  @Path("/start-session")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  CommonRemoteData startSession(@Valid StartSessionRequest var1);
}
