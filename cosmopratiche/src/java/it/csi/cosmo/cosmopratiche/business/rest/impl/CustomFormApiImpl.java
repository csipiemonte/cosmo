/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.CustomFormsApi;
import it.csi.cosmo.cosmopratiche.business.service.CustomFormsService;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;

/**
 *
 */

public class CustomFormApiImpl extends ParentApiImpl implements CustomFormsApi {

  @Autowired
  private CustomFormsService customFormsService;

  @Override
  public Response deleteCustomForm(String codice, SecurityContext securityContext) {
    customFormsService.deleteCustomForm(codice);

    return Response.ok().build();
  }

  @Override
  public Response getCustomForms(String filter, SecurityContext securityContext) {
    CustomFormResponse customForms = customFormsService.getCustomForms(filter);
    return Response.ok(customForms).build();
  }

  @Override
  public Response getCustomForm(String codice, SecurityContext securityContext) {
    CustomForm customForm = customFormsService.getCustomForm(codice);
    return Response.ok(customForm).build();
  }

  @Override
  public Response postCustomForm(CustomForm body, SecurityContext securityContext) {
    CustomForm customForm = customFormsService.postCustomForm(body);
    return Response.ok(customForm).build();
  }

  @Override
  public Response putCustomForm(String codice, CustomForm body,
      SecurityContext securityContext) {
    CustomForm customForm = customFormsService.putCustomForm(codice, body);
    return Response.ok(customForm).build();
  }

  @Override
  public Response getCustomFormsCodiceTipoPratica(String codiceTipoPratica,
      SecurityContext securityContext) {
    CustomForm customForm =
        customFormsService.getCustomFormFromCodiceTipoPratica(codiceTipoPratica);
    return Response.ok(customForm).build();
  }

}
