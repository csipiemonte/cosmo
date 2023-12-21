/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Order(99)
@Configuration
@EnableWebSecurity
public class CosmoSecurityConfiguration extends WebSecurityConfigurerAdapter {



  @Override
  protected void configure(HttpSecurity http) throws Exception {
 // @formatter:off
    http
      .addFilterAfter(new CosmoAuthenticationFilter(), BasicAuthenticationFilter.class)
      .csrf()
      .disable()
      .authorizeRequests()
      .anyRequest()
      .permitAll();
 // @formatter:on


  }
}
