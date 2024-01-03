/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;
import java.util.List;

import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;

public interface TabsDettaglioService {

  /**
   * Metodo che restituisce una lista di Tabs
   *
   * @return una lista di tabs 
   */
	List<TabsDettaglio> getTabsDettaglio();

	List<TabsDettaglio> getTabsDettaglioCodiceTipoPratica(String codiceTipoPratica);

  
}
