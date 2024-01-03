/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service;

import java.util.List;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoNotifica;

/**
 *
 */

public interface TipiNotificheService {

  List<TipoNotifica> getTipiNotifiche();

}
