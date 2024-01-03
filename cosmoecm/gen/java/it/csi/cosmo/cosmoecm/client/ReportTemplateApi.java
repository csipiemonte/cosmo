/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReport;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReportResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/report-template")  
public interface ReportTemplateApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteReportTemplateId( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public TemplateReportResponse getReportTemplate(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public TemplateReport getReportTemplateId( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TemplateReport postReportTemplate( @Valid CreaTemplateRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TemplateReport putReportTemplateId( @PathParam("id") Long id,  @Valid CreaTemplateRequest body);

}
