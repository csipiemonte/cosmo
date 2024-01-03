/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/metadati")  
public interface MetadatiApi  {
   
    @PUT @Path("/{idPratica}")  @Produces({ "application/json" })
    public Pratica putMetadatiIdPratica( @PathParam("idPratica") String idPratica);

    @PUT @Path("/{idPratica}/variabili-processo")  @Produces({ "application/json" })
    public Pratica putMetadatiIdPraticaVariabiliProcesso( @PathParam("idPratica") String idPratica);

}
