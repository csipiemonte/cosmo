/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

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

@Path("/tipi-documento")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TipiDocumentoApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipiDocumento( @NotNull @QueryParam("codici") List<String> codici,@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response queryTipiDocumento( @NotNull @QueryParam("codici") List<String> codici, @QueryParam("addFormatoFile") Boolean addFormatoFile, @QueryParam("codicePadre") String codicePadre, @QueryParam("codiceTipoPratica") String codiceTipoPratica,@Context SecurityContext securityContext);
}
