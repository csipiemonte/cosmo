/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.service.FirmaService;
import it.csi.cosmo.cosmoecm.client.FirmaApi;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaRequest;

@SuppressWarnings("unused")
public class FirmaApiServiceImpl extends ParentApiImpl implements FirmaApi {

  @Autowired
  FirmaService firmaService;

  @Override
  public void postFirma(FirmaRequest firmaRequest) {
    firmaService.firma(firmaRequest);
  }

}
