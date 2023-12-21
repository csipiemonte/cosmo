/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.cripto.CryptoConverter;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class CryptoServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private CryptoConverter converter;

  @Test
  public void encrypt() {
    String input = "test";
    String converted = converter.encrypt(input);
    dump("ENCRYPTED " + input + " => " + converted);
  }

  @Test
  public void decrypt() {
    String input = "QAuay0875GeZBu1zyVbfzQ==";
    String converted = converter.decrypt(input);
    dump("DECRYPTED " + input + " => " + converted);
  }
}
