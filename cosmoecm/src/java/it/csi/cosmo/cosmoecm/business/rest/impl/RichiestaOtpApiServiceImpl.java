/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.service.FirmaService;
import it.csi.cosmo.cosmoecm.client.RichiestaOtpApi;
import it.csi.cosmo.cosmoecm.dto.rest.RichiestaOTPRequest;

public class RichiestaOtpApiServiceImpl extends ParentApiImpl implements RichiestaOtpApi {

  @Autowired
  FirmaService firmaService;

  @Override
  public void getRichiestaOtp(RichiestaOTPRequest body) {
    firmaService.richiediOTP(body);
  }

}
