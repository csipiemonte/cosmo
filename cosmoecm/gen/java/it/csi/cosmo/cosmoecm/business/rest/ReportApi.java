/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.GenerazioneReportResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/report")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface ReportApi  {
   
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postReport( RichiediGenerazioneReportRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/anteprima")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream", "application/json" })

    public Response postReportAnteprima( RichiediGenerazioneReportRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/async")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postReportAsync( RichiediGenerazioneReportRequest body,@Context SecurityContext securityContext);
}
