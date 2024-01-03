/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;

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

@Path("/esecuzione-multipla")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface EsecuzioneMultiplaApi  {
   
    @POST
    @Path("/firma")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postEsecuzioneMultiplaFirma( EsecuzioneMultiplaFirmaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/rifiutoFirma")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postEsecuzioneMultiplaRifiutoFirma( EsecuzioneMultiplaRifiutoFirmaRequest body,@Context SecurityContext securityContext);
}
