/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.ReportApi;
import it.csi.cosmo.cosmoecm.business.service.ReportService;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.dto.jasper.RisultatoGenerazioneReport;
import it.csi.cosmo.cosmoecm.dto.rest.GenerazioneReportResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.util.FilesUtils;


public class ReportApiImpl extends ParentApiImpl implements ReportApi {

  @Autowired
  public ReportService reportService;

  @Override
  public Response postReport(RichiediGenerazioneReportRequest body,
      SecurityContext securityContext) {

    GenerazioneReportResponse response = reportService.generaReportDaProcesso(body);
    return Response.status(201).entity(response).build();
  }

  @Override
  public Response postReportAsync(RichiediGenerazioneReportRequest body,
      SecurityContext securityContext) {

    RiferimentoOperazioneAsincrona response = reportService.generaReportAsincrono(body);
    return Response.status(201).entity(response).build();
  }

  @Override
  public Response postReportAnteprima(RichiediGenerazioneReportRequest body,
      SecurityContext securityContext) {

    RisultatoGenerazioneReport content = reportService.generaReportDaProcessoInMemory(body);
    FileContent fileContent = new FileContent();
    fileContent.setContent(content.getCompilato());
    fileContent.setFileName("anteprima-report." + content.getEstensione());
    fileContent.setMimeType(content.getMimeType());

    return FilesUtils.toPreviewResponse(fileContent);
  }

}
