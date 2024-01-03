/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.TemplateFeaApi;
import it.csi.cosmo.cosmoecm.business.service.TemplateFeaService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;

/**
 *
 */

public class TemplateFeaApiImpl extends ParentApiImpl implements TemplateFeaApi {

  @Autowired
  TemplateFeaService templateFeaService;

  @Override
  public Response deleteTemplateFeaId(Long id, SecurityContext securityContext) {
    templateFeaService.deleteTemplateFea(id);
    return Response.noContent().build();
  }

  @Override
  public Response getTemplateFea(String filter, Boolean tutti, SecurityContext securityContext) {
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filter, tutti);
    return Response.ok(tf).build();
  }

  @Override
  public Response getTemplateFeaId(Long id, SecurityContext securityContext) {
    TemplateFea tf = templateFeaService.getTemplateFeaId(id);
    return Response.ok(tf).build();
  }

  @Override
  public Response postTemplateFea(CreaTemplateFeaRequest body, SecurityContext securityContext) {
    TemplateFea tf = templateFeaService.creaTemplateFea(body);
    return Response.ok(tf).build();
  }

  @Override
  public Response putTemplateFeaId(Long id, CreaTemplateFeaRequest body,
      SecurityContext securityContext) {
    TemplateFea tf = templateFeaService.updateTemplateFea(id, body);
    return Response.ok(tf).build();
  }





}
