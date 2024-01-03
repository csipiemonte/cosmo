/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.service.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmonotifications.business.service.UserService;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;
import it.csi.test.cosmo.cosmonotifications.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class})
@Transactional
public class UserServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private UserService userService;

  @Test
  public void utenteCorrenteNonDeveEssereNull() {

    UserInfoDTO utenteCorrente = userService.getUtenteCorrente();

    assertNotNull("utente corrente non deve mai essere null", utenteCorrente);
  }

}
