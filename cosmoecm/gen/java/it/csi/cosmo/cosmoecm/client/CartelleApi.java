/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.Cartella;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/cartelle")  
public interface CartelleApi  {
   
    @DELETE  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public void deletePratiche( @Valid Cartella body);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Cartella postPratiche( @Valid Cartella body);

}
