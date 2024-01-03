/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.FirmaFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/fea")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FeaApi  {
   
    @POST
    @Path("/firma")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFeaFirma( FirmaFeaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/richiedi-otp")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFeaRichiediOtp(@Context SecurityContext securityContext);
}
