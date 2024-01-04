/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.client;

import it.csi.cosmo.cosmosoap.dto.rest.*;

import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/stardas")  
public interface StardasApi  {
   
    @POST @Path("/smistamento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SmistaDocumentoResponse smistamentoDocumento( @Valid SmistaDocumentoRequest body);

    @GET @Path("/smistamento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public GetStatoRichiestaResponse statoRichiesta( @Valid GetStatoRichiestaRequest body);

}
