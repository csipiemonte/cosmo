/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistemaResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/parametri-di-sistema")  
public interface ParametriDiSistemaApi  {
   
    @DELETE @Path("/{chiave}")  @Produces({ "application/json" })
    public void deleteParametroDiSistemaByChiave( @PathParam("chiave") String chiave);

    @GET   @Produces({ "application/json" })
    public ParametroDiSistemaResponse getParametriDiSistema(  @QueryParam("filter") String filter);

    @GET @Path("/{chiave}")  @Produces({ "application/json" })
    public ParametroDiSistema getParametroDiSistemaByChiave( @PathParam("chiave") String chiave);

    @POST   @Produces({ "application/json" })
    public ParametroDiSistema postParametroDiSistema( @Valid CreaParametroDiSistemaRequest body);

    @PUT @Path("/{chiave}")  @Produces({ "application/json" })
    public ParametroDiSistema putParametroDiSistemaByChiave( @PathParam("chiave") String chiave,  @Valid AggiornaParametroDiSistemaRequest body);

}
