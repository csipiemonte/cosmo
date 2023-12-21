/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmocmmn.testbed.config;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import it.csi.cosmo.common.infinispan.connector.InfinispanResourceConnector;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;


@Configuration
@ComponentScan(
    basePackages = {"it.csi.cosmo.cosmocmmn.business", "it.csi.cosmo.cosmocmmn.discovery",
        "it.csi.cosmo.cosmocmmn.security", "it.csi.cosmo.cosmocmmn.integration.rest"},
    excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
@TestExecutionListeners({DirtiesContextTestExecutionListener.class})
public class CosmoUnitTest {

  private final Logger logger = Logger.getLogger(CosmoUnitTest.class);

  @Resource
  private Environment env;

  @Bean
  public SpringApplicationContextHelper springApplicationContextHelper() {
    return new SpringApplicationContextHelper();
  }

  @Bean
  public InfinispanResourceConnector infinispanConnector() {

    return new InfinispanResourceConnector() {

      private Map<String, Object> fallbackLocalCache = new LinkedHashMap<>();

      @Override
      public void close() throws Exception {
        // NOP
      }

      @Override
      public Map<String, Object> getCache() {
        return fallbackLocalCache;
      }

    };
  }

}
