/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.GenerazioneReportResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/report")  
public interface ReportApi  {
   
    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public GenerazioneReportResponse postReport( @Valid RichiediGenerazioneReportRequest body);

    @POST @Path("/anteprima") @Consumes({ "application/json" }) @Produces({ "application/octet-stream", "application/json" })
    public RiferimentoOperazioneAsincrona postReportAnteprima( @Valid RichiediGenerazioneReportRequest body);

    @POST @Path("/async") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postReportAsync( @Valid RichiediGenerazioneReportRequest body);

}
