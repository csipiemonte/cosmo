/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/certificati")  
public interface CertificatiApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public CertificatoFirma deleteCertificatiId( @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public List<CertificatoFirma> getCertificati();

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public CertificatoFirma getCertificatiId( @PathParam("id") String id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CertificatoFirma postCertificati( @Valid CertificatoFirma body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CertificatoFirma putCertificatiId( @PathParam("id") String id,  @Valid CertificatoFirma body);

    @PUT @Path("/salva-ultimo-usato/{id}")  @Produces({ "application/json" })
    public CertificatoFirma putCertificatiSalvaUltimoUsato( @PathParam("id") String id);

}
