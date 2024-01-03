/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/tipi-documento")  
public interface TipiDocumentoApi  {
   
    @GET   @Produces({ "application/json" })
    public List<TipoDocumento> getTipiDocumento( @NotNull  @QueryParam("codici") List<String> codici);

    @GET @Path("/ricerca")  @Produces({ "application/json" })
    public List<TipoDocumento> queryTipiDocumento( @NotNull  @QueryParam("codici") List<String> codici,   @QueryParam("addFormatoFile") Boolean addFormatoFile,   @QueryParam("codicePadre") String codicePadre,   @QueryParam("codiceTipoPratica") String codiceTipoPratica);

}
