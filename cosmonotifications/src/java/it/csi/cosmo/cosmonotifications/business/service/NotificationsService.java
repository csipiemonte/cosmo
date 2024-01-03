/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.service;

import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;

public interface NotificationsService {

  PaginaNotifiche getNotifications(Integer offset, Integer limit);

  Notifica getNotificationsId(String idNotifica);

  CreaNotificheResponse postNotifications(CreaNotificheRequest body);

  Notifica putNotificationsId(String id, Notifica body);

  void sendNotification(CosmoTNotifica notifica, String titolo, String evento);

  void sendNotifications(NotificheGlobaliRequest notifica);

  CreaNotificaResponse postNotificationFruitore(CreaNotificaRequest body, CosmoTFruitore fruitore);

  void putNotificationsAll();
}
