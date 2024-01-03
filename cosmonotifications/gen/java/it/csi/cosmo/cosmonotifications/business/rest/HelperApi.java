/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest;

import it.csi.cosmo.cosmonotifications.dto.rest.*;


import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.DecodificaHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;

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

@Path("/helper")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface HelperApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteHelperId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/codicePagina")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCodicePagina(@Context SecurityContext securityContext);
    @GET
    @Path("/codicePagina/{codice}/codiceTab")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCodiciTabs( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getHelper( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/codice-modale")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getHelperCodiceModale( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/decodifica")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getHelperDecodifica( @NotNull @QueryParam("pagina") String pagina, @QueryParam("tab") String tab, @QueryParam("form") String form,@Context SecurityContext securityContext);
    @GET
    @Path("/export/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getHelperExport( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getHelperId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postHelper( Helper body,@Context SecurityContext securityContext);
    @POST
    @Path("/import")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postHelperImport( HelperImportRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putHelperId( @PathParam("id") String id, Helper body,@Context SecurityContext securityContext);
}
