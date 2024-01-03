/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;

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
   
    @POST @Path("/firma") @Consumes({ "application/json" }) 
    public void postEsecuzioneMultiplaFirma( @Valid EsecuzioneMultiplaFirmaRequest body);

    @POST @Path("/rifiutoFirma") @Consumes({ "application/json" }) 
    public void postEsecuzioneMultiplaRifiutoFirma( @Valid EsecuzioneMultiplaRifiutoFirmaRequest body);

}
