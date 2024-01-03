/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.FeaApi;
import it.csi.cosmo.cosmoecm.business.service.FeaService;
import it.csi.cosmo.cosmoecm.business.service.OtpService;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public class FeaApiImpl extends ParentApiImpl implements FeaApi {

  @Autowired
  private OtpService otpService;

  @Autowired
  private FeaService feaService;

  @Override
  public Response postFeaRichiediOtp(SecurityContext securityContext) {

    otpService.richiediOtp();

    return Response.ok().build();
  }

  @Override
  public Response postFeaFirma(FirmaFeaRequest body, SecurityContext securityContext) {
    RiferimentoOperazioneAsincrona response = feaService.firma(body);
    return Response.ok(response).build();
  }

}
