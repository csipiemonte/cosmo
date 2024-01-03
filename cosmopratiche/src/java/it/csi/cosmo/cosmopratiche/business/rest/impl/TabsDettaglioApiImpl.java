/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.cosmopratiche.business.rest.TabsDettaglioApi;
import it.csi.cosmo.cosmopratiche.business.service.TabsDettaglioService;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;



public class TabsDettaglioApiImpl extends ParentApiImpl implements TabsDettaglioApi {

	@Autowired
	private TabsDettaglioService tabsDettaglioService;

  @Override
  public Response getTabsDettaglio(SecurityContext securityContext) {
	 List<TabsDettaglio> tabsDettaglio = tabsDettaglioService.getTabsDettaglio();
    return Response.ok(tabsDettaglio).build();
  }

	@Override
	public Response getTabsDettaglioCodiceTipoPratica(String codiceTipoPratica, SecurityContext securityContext) {
		List<TabsDettaglio> tabsDettaglio = tabsDettaglioService.getTabsDettaglioCodiceTipoPratica(codiceTipoPratica);
		return Response.ok(tabsDettaglio).build();
	}
	
	

}
