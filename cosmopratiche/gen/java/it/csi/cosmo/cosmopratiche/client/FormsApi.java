/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/forms")  
public interface FormsApi  {
   
    @GET @Path("/definitions/{formKey}")  @Produces({ "application/json" })
    public SimpleForm getFormDefinitionFormKey( @PathParam("formKey") String formKey);

    @GET @Path("/attivita/{id}")  @Produces({ "application/json" })
    public StrutturaFormLogico getFormsAttivitaId( @PathParam("id") String id);

    @GET @Path("/{nome}")  @Produces({ "application/json" })
    public StrutturaFormLogico getFormsNome( @PathParam("nome") String nome);

}
