/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.ReportTemplateApi;
import it.csi.cosmo.cosmoecm.business.service.ReportTemplateService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReport;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReportResponse;

/**
 *
 */

public class ReportTemplateApiImpl extends ParentApiImpl implements ReportTemplateApi {

  @Autowired
  ReportTemplateService reportTemplateService;

  @Override
  public Response getReportTemplate(String filter, SecurityContext securityContext) {

    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filter);

    return Response.ok(tr).build();
  }

  @Override
  public Response deleteReportTemplateId(Long id, SecurityContext securityContext) {
    reportTemplateService.deleteReportTemplate(id);
    return Response.noContent().build();
  }

  @Override
  public Response getReportTemplateId(Long id, SecurityContext securityContext) {
    TemplateReport tr = reportTemplateService.getReportTemplateId(id);
    return Response.ok(tr).build();
  }

  @Override
  public Response postReportTemplate(CreaTemplateRequest body, SecurityContext securityContext) {
    TemplateReport tr = reportTemplateService.creaReportTemplate(body);
    return Response.ok(tr).build();
  }

  @Override
  public Response putReportTemplateId(Long id, CreaTemplateRequest body,
      SecurityContext securityContext) {
    TemplateReport tr = reportTemplateService.updateReportTemplate(id, body);
    return Response.ok(tr).build();
  }

}
