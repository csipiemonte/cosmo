/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/stilo")  
public interface StiloApi  {
   
    @POST @Path("/carica-unita-documentaria") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CaricaUnitaDocumentariaResponse postStiloCaricaUnitaDocumentaria( @Valid CaricaUnitaDocumentariaRequest body);

    @PUT @Path("/aggiorna-unita-documentaria") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public AggiornaUnitaDocumentariaResponse putAggiornaUnitaDocumentaria( @Valid AggiornaUnitaDocumentariaRequest body);

}
