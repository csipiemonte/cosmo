/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobe.business.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.cosmobe.business.service.ClientService;
import it.csi.test.cosmo.cosmobe.testbed.config.CosmoUnitTest;
import it.csi.test.cosmo.cosmobe.testbed.model.ParentIntegrationTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoUnitTest.class } )
public class ClientServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ClientService clientService;

  @Test
  public void clientCorrenteNonDeveEssereNull() {

    ClientInfoDTO clientCorrente = clientService.getClientCorrente();

    assertNotNull("client corrente non deve mai essere null", clientCorrente);
  }

}
