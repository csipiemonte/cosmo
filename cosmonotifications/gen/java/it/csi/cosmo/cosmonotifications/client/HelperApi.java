/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.client;

import it.csi.cosmo.cosmonotifications.dto.rest.*;

import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.DecodificaHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/helper")  
public interface HelperApi  {
   
    @DELETE @Path("/{id}")  
    public void deleteHelperId( @PathParam("id") String id);

    @GET @Path("/codicePagina")  @Produces({ "application/json" })
    public List<CodicePagina> getCodicePagina();

    @GET @Path("/codicePagina/{codice}/codiceTab")  @Produces({ "application/json" })
    public List<CodiceTab> getCodiciTabs( @PathParam("codice") String codice);

    @GET   @Produces({ "application/json" })
    public HelperResponse getHelper(  @QueryParam("filter") String filter);

    @GET @Path("/codice-modale")  @Produces({ "application/json" })
    public List<CodiceModale> getHelperCodiceModale(  @QueryParam("filter") String filter);

    @GET @Path("/decodifica")  @Produces({ "application/json" })
    public DecodificaHelper getHelperDecodifica( @NotNull  @QueryParam("pagina") String pagina,   @QueryParam("tab") String tab,   @QueryParam("form") String form);

    @GET @Path("/export/{id}")  @Produces({ "application/json" })
    public byte[] getHelperExport( @PathParam("id") String id);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Helper getHelperId( @PathParam("id") String id);

    @POST  @Consumes({ "application/json" }) 
    public void postHelper( @Valid Helper body);

    @POST @Path("/import") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public HelperImportResult postHelperImport( @Valid HelperImportRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Helper putHelperId( @PathParam("id") String id,  @Valid Helper body);

}
