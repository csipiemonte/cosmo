/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;

/**
 *
 */

public interface TipiTagsService {

  /**
   * Metodo che restituisce una lista di tipi tags validi
   *
   * @return una lista di tipi tags validi
   */
  List<TipoTag> getTipiTags();

}
