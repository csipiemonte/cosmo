/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;

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

@Path("/forms")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FormsApi  {
   
    @GET
    @Path("/definitions/{formKey}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormDefinitionFormKey( @PathParam("formKey") String formKey,@Context SecurityContext securityContext);
    @GET
    @Path("/attivita/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormsAttivitaId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{nome}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormsNome( @PathParam("nome") String nome,@Context SecurityContext securityContext);
}
