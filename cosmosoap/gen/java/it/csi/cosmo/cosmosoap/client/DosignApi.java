/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/dosign")  
public interface DosignApi  {
   
    @POST @Path("/end-session") @Consumes({ "application/json" }) 
    public void endSession( @Valid EndSessionRequest body);

    @POST @Path("/firma") @Consumes({ "application/json" }) 
    public void firma( @Valid DoSignFirmaRequest body);

    @POST @Path("/firma-massiva") @Consumes({ "application/json" }) 
    public void firmaMassiva( @Valid DoSignFirmaMassivaRequest body);

    @POST @Path("/sigillo") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SigillaDocumentoResponse postDosignSigillo( @Valid DoSignSigilloRequest body);

    @POST @Path("/richiedi-otp") @Consumes({ "application/json" }) 
    public void richiediOTP( @Valid RichiediOTPRequest body);

    @POST @Path("/start-session") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CommonRemoteData startSession( @Valid StartSessionRequest body);

}
