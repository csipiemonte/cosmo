/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.monitoring.StatisticsCollector;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.test.cosmo.cosmobusiness.business.StatisticsTest.StatisticsTestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, StatisticsTestConfig.class})
@Transactional
public class StatisticsTest extends ParentIntegrationTest {

  @Configuration
  public static class StatisticsTestConfig {
    // NOP
  }

  @Autowired
  private CosmoTEnteRepository repository;

  @Autowired
  private StatisticsCollector statisticsTestbedService;

  @Test
  public void testBean() {
    dump(repository.findAll().size());

    dump(statisticsTestbedService.gatherRelevantStatistics());
  }

}
