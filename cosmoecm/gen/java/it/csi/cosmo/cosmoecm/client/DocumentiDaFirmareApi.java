/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/documenti-da-firmare")  
public interface DocumentiDaFirmareApi  {
   
    @GET   @Produces({ "application/json" })
    public List<DocumentiResponse> getDocumentiFirmabili( @NotNull  @QueryParam("filter") String filter,  @NotNull  @QueryParam("filterDocDaFirmare") String filterDocDaFirmare,   @QueryParam("export") Boolean export);

}
