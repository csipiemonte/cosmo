/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.integration.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmocmmn.dto.rest.InviaSegnaleProcessoRequest;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/cmmn/process/async",
    interceptors = CosmoBusinessFeignClientConfiguration.class,
    configurator = CosmoBusinessCmmnFeignClientConfiguration.class)
public interface CosmoCmmnAsyncFeignClient {

  @POST
  @Path("/runtime/tasks/{id}/update")
  RiferimentoOperazioneAsincrona updateTaskById(@PathParam("id") String id, Task task);

  @POST
  @Path("/runtime/tasks/{id}/complete")
  RiferimentoOperazioneAsincrona completeTaskById(@PathParam("id") String id, Task task);

  @POST
  @Path("/runtime/process/{processId}/signal")
  RiferimentoOperazioneAsincrona inviaSegnaleProcesso(@PathParam("processId") String processId,
      InviaSegnaleProcessoRequest payload);

}
