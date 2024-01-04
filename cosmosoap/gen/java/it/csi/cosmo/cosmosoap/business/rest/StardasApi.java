/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/stardas")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface StardasApi  {
   
    @POST
    @Path("/smistamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response smistamentoDocumento( SmistaDocumentoRequest body,@Context SecurityContext securityContext);
    @GET
    @Path("/smistamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response statoRichiesta( GetStatoRichiestaRequest body,@Context SecurityContext securityContext);
}
