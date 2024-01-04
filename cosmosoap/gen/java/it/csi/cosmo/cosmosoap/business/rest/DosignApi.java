/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.rest;

import it.csi.cosmo.cosmosoap.dto.rest.*;


import it.csi.cosmo.cosmosoap.dto.rest.CommonRemoteData;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.EndSessionRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SigillaDocumentoResponse;
import it.csi.cosmo.cosmosoap.dto.rest.StartSessionRequest;

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

@Path("/dosign")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface DosignApi  {
   
    @POST
    @Path("/end-session")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response endSession( EndSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/firma")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response firma( DoSignFirmaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/firma-massiva")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response firmaMassiva( DoSignFirmaMassivaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/sigillo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDosignSigillo( DoSignSigilloRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/richiedi-otp")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response richiediOTP( RichiediOTPRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/start-session")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response startSession( StartSessionRequest body,@Context SecurityContext securityContext);
}
