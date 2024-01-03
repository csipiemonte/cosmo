/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.client;

import it.csi.cosmo.cosmonotifications.dto.rest.*;

import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Esito;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/notifiche")  
public interface NotificheApi  {
   
    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CreaNotificheResponse postNotifications( @Valid CreaNotificheRequest body);

}
