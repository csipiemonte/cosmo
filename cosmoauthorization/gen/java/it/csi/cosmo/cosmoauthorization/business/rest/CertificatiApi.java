/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;

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

@Path("/certificati")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface CertificatiApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteCertificatiId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCertificati(@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCertificatiId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCertificati( CertificatoFirma body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putCertificatiId( @PathParam("id") String id, CertificatoFirma body,@Context SecurityContext securityContext);
    @PUT
    @Path("/salva-ultimo-usato/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putCertificatiSalvaUltimoUsato( @PathParam("id") String id,@Context SecurityContext securityContext);
}
