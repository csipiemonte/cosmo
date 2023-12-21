/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentUnitTest;
import it.csi.test.cosmo.cosmobusiness.testbed.repository.CosmoTEnteCustomTestbedRepository;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional()
public class CustomH2FunctionTest extends ParentUnitTest {

  @Autowired
  private CosmoTEnteCustomTestbedRepository enteRepository;

  @Test
  public void shouldUpdateEventData() {
    dump(enteRepository.findAll().get(0).getNome());
  }

}
