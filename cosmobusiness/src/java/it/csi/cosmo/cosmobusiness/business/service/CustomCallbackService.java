/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbackResponse;

/**
 *
 */

public interface CustomCallbackService {

  CustomCallbackResponse getCustomEndopoint(String apiManagerId, String codiceDescrittivo,
      String processInstanceId);

}
