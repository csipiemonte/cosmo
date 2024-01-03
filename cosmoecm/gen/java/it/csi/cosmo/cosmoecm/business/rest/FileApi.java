/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.Esito;

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

@Path("/file")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FileApi  {
   
    @DELETE
    @Path("/{uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFileUuid( @PathParam("uuid") String uuid,@Context SecurityContext securityContext);
}
