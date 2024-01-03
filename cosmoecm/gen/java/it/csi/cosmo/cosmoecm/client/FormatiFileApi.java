/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatiFileResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/formati-file")  
public interface FormatiFileApi  {
   
    @GET   @Produces({ "application/json" })
    public FormatoFileResponse getFormatiFile(  @QueryParam("filter") String filter);

    @GET @Path("/grouped")  @Produces({ "application/json" })
    public RaggruppamentoFormatiFileResponse getFormatiFileGrouped(  @QueryParam("filter") String filter);

}
