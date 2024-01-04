/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.rest;

import it.csi.cosmo.cosmosoap.dto.rest.*;


import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaResponse;

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

@Path("/stilo")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface StiloApi  {
   
    @POST
    @Path("/carica-unita-documentaria")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postStiloCaricaUnitaDocumentaria( CaricaUnitaDocumentariaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/aggiorna-unita-documentaria")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putAggiornaUnitaDocumentaria( AggiornaUnitaDocumentariaRequest body,@Context SecurityContext securityContext);
}
