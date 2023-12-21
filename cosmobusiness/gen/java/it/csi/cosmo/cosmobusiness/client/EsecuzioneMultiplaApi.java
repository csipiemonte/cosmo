/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaApprovaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaVariabiliProcessoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;

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
   
    @POST @Path("/approva") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postEsecuzioneMultiplaApprova( @Valid EsecuzioneMultiplaApprovaRequest body);

    @POST @Path("/variabili-processo") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postEsecuzioneMultiplaVariabiliProcesso( @Valid EsecuzioneMultiplaVariabiliProcessoRequest body);

}
