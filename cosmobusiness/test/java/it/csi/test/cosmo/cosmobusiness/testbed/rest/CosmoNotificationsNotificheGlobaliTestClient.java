/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;


/**
 *
 */

@Component
public class CosmoNotificationsNotificheGlobaliTestClient extends ParentTestClient
implements CosmoNotificationsNotificheGlobaliFeignClient {

  @Override
  public void postNotificheGlobali(NotificheGlobaliRequest arg0) {
    notMocked();
    return;
  }
}
