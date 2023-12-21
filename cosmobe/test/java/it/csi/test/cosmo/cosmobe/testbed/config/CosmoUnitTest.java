/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmobe.testbed.config;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import it.csi.cosmo.cosmobe.util.listener.SpringApplicationContextHelper;


@Configuration
@ComponentScan(basePackages = {"it.csi.cosmo.cosmobe"},
excludeFilters = {
    @Filter ( type = FilterType.ANNOTATION, value = Configuration.class ) } )
@TestExecutionListeners ( { DirtiesContextTestExecutionListener.class } )
public class CosmoUnitTest {

  @Resource
  private Environment env;

  @Bean
  public SpringApplicationContextHelper springApplicationContextHelper () {
    return new SpringApplicationContextHelper ();
  }

}
