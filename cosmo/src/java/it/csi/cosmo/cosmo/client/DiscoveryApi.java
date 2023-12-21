/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.client;

import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import it.csi.cosmo.common.discovery.model.DiscoveredService;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatRequest;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;


/**
 * Risorsa RestEasy per il reperimento di informazioni sullo stato
 */
@Path("/discovery")
@Produces ( { "application/json" } )
public interface DiscoveryApi {

  /**
   * invia un heartbeat
   *
   * @return response DiscoveryHeartBeatResponse
   */
  @POST
  @Path("/heartbeat")
  public DiscoveryHeartBeatResponse sendHeartBeat(DiscoveryHeartBeatRequest request);

  /**
   * ottiene il registro dei servizi
   *
   * @return response DiscoveryRegistry
   */
  @GET
  @Path("/registry")
  public DiscoveryRegistry getRegistry();

  /**
   * ottiene la lista dei servizi
   *
   * @return response DiscoveredServices
   */
  @GET
  @Path("/services")
  public Set<DiscoveredService> getServices();

}
