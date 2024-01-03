/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.TabsDettaglioService;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class TabsDettaglioServiceImplTest extends ParentUnitTest {

	@Autowired
	TabsDettaglioService tabsDettaglioService;
	
  @Test
  public void getTabsDettaglio() {
	List<TabsDettaglio> tabs = tabsDettaglioService.getTabsDettaglio();
    assertNotNull ( "errore lista dettaglio-tabs", tabs );
    assertTrue(!tabs.isEmpty());
  }
  
  @Test
  public void getTabsDettaglioCodiceTipoPratica() {
	  List<TabsDettaglio> tabsDettaglio = tabsDettaglioService.getTabsDettaglioCodiceTipoPratica("TP1");
	  assertNotNull ( "errore lista dettaglio-tabs", tabsDettaglio );
	  assertTrue(!tabsDettaglio.isEmpty());
  }
  
  @Test(expected = NotFoundException.class)
  public void getTabsDettaglioCodiceTipoPraticaNotFound() {
      tabsDettaglioService.getTabsDettaglioCodiceTipoPratica("TP11");
  }
}
