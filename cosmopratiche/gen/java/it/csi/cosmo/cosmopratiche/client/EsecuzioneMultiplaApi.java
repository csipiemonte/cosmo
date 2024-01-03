/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/esecuzione-multipla")  
public interface EsecuzioneMultiplaApi  {
   
    @GET @Path("/task-disponibili")  @Produces({ "application/json" })
    public List<RiferimentoAttivita> getEsecuzioneMultiplaTaskDisponibili(  @QueryParam("filter") String filter);

    @GET @Path("/task")  @Produces({ "application/json" })
    public List<AttivitaEseguibileMassivamente> getEsecuzioneMultiplaTasks();

}
