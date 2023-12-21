/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmoauthorization.business.service.AutorizzazioniFruitoreService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioniFruitoreResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class AutorizzazioniFruitoriServiceImplTest {
  
  @Autowired
  private AutorizzazioniFruitoreService autorizzazioniFruitoreService;
  
  @Test
  public void getAutorizzazioniFruitore() {
    AutorizzazioniFruitoreResponse response = autorizzazioniFruitoreService.getAutorizzazioniFruitore("");
    assertNotNull(response);
    assertNotNull(response.getAutorizzazioni());
    assertTrue(response.getAutorizzazioni().size() == 3);
  }
}
