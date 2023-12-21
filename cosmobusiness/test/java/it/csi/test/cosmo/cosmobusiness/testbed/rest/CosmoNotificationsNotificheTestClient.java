/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;


/**
 *
 */

@Component
public class CosmoNotificationsNotificheTestClient extends ParentTestClient
    implements CosmoNotificationsNotificheFeignClient {

  @Override
  public CreaNotificheResponse postNotifications(CreaNotificheRequest arg0) {
    notMocked();
    return null;
  }
}
