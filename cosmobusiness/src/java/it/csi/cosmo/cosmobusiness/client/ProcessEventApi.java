/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.common.security.model.Secured;

@Path("/process")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface ProcessEventApi {

  @Secured(hasScopes = {"PROCESS_EVENTS"})
  @POST
  @Path("/event")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response postProcessEvent(ProcessEngineEventDTO body);


  @Secured(hasScopes = {"PROCESS_EVENTS"})
  @POST
  @Path("/variable/event")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response postProcessVariableEvent(ProcessInstanceVariableEventDTO body);

}
