/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;

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

@Path("/tabs-dettaglio")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TabsDettaglioApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTabsDettaglio(@Context SecurityContext securityContext);
    @GET
    @Path("/{codiceTipoPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTabsDettaglioCodiceTipoPratica( @PathParam("codiceTipoPratica") String codiceTipoPratica,@Context SecurityContext securityContext);
}
