/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoInvioSegnaleRequest;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/esito-invio-segnale")  
public interface EsitoInvioSegnaleApi  {
   
    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Esito callbackPostStatoInvioSegnale( @Valid EsitoInvioSegnaleRequest body);

}
