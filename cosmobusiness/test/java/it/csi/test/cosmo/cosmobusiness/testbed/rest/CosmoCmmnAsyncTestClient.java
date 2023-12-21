/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnAsyncFeignClient;
import it.csi.cosmo.cosmocmmn.dto.rest.InviaSegnaleProcessoRequest;

/**
 *
 */

/**
 *
 *
 */
@Component
public class CosmoCmmnAsyncTestClient extends ParentTestClient
implements CosmoCmmnAsyncFeignClient {

  /**
   *
   */
  public CosmoCmmnAsyncTestClient() {
    // Auto-generated constructor stub
  }

  @Override
  public RiferimentoOperazioneAsincrona updateTaskById(String id, Task task) {
    notMocked();
    return null;
  }

  @Override
  public RiferimentoOperazioneAsincrona completeTaskById(String id, Task task) {
    notMocked();
    return null;
  }

  @Override
  public RiferimentoOperazioneAsincrona inviaSegnaleProcesso(String processId,
      InviaSegnaleProcessoRequest payload) {
    notMocked();
    return null;
  }

}
