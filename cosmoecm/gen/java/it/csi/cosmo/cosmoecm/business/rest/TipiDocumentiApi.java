/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.Esito;
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

@Path("/tipi-documenti")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TipiDocumentiApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipiDocumenti( @NotNull @QueryParam("idPratica") String idPratica, @QueryParam("codiceTipoDocPadre") String codiceTipoDocPadre,@Context SecurityContext securityContext);
    @GET
    @Path("/all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipiDocumentiAll( @NotNull @QueryParam("tipoPratica") String tipoPratica,@Context SecurityContext securityContext);
}
