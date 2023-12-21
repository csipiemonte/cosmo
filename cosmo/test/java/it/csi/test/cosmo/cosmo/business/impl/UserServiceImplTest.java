/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmo.business.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.business.service.UserService;
import it.csi.test.cosmo.cosmo.testbed.config.CosmoUnitTest;
import it.csi.test.cosmo.cosmo.testbed.model.ParentIntegrationTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoUnitTest.class } )
public class UserServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private UserService userService;

  @Test
  public void utenteCorrenteNonDeveEssereNull () {

    UserInfoDTO utenteCorrente = userService.getUtenteCorrente();

    assertNotNull ( "utente corrente non deve mai essere null", utenteCorrente );
  }

}
