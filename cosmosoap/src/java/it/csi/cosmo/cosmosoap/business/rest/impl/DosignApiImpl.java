/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmosoap.business.rest.DosignApi;
import it.csi.cosmo.cosmosoap.business.service.DoSignService;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.EndSessionRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.StartSessionRequest;

/**
 *
 */

public class DosignApiImpl extends ParentApiImpl implements DosignApi {

  @Autowired
  private DoSignService doSignService;

  @Override
  public Response endSession(EndSessionRequest body, SecurityContext securityContext) {
    doSignService.executeEndSession(body.getContinueTransactionDto(), body.getIdTransazione());
    return Response.ok().build();
  }

  @Override
  public Response firma(DoSignFirmaRequest body, SecurityContext securityContext) {
    doSignService.firma(body);
    return Response.accepted().build();
  }

  @Override
  public Response firmaMassiva(DoSignFirmaMassivaRequest body, SecurityContext securityContext) {
    doSignService.firmaMassiva(body);
    return Response.accepted().build();
  }

  @Override
  public Response richiediOTP(RichiediOTPRequest body, SecurityContext securityContext) {
    return Response
        .ok(doSignService.richiediOTP(body)).build();
  }

  @Override
  public Response startSession(StartSessionRequest body, SecurityContext securityContext) {
    return Response.ok(
        doSignService.executeStartSession(body.getStartTransactionDto(), body.getIdTransazione()))
        .build();
  }

  @Override
  public Response postDosignSigillo(DoSignSigilloRequest body, SecurityContext securityContext) {
    return Response.ok(doSignService.apponiSigillo(body)).build();
  }

}
