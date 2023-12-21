/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.config;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmo.security.Scopes;
import it.csi.cosmo.cosmo.security.UseCase;
import it.csi.cosmo.cosmo.security.proxy.model.AbstractProxyConfigurationProvider;
import it.csi.cosmo.cosmo.security.proxy.model.ProxyConfiguration;

/**
 *
 */
@Component
public class ProxyConfig extends AbstractProxyConfigurationProvider {

  public static final String PROXY_CATCHALL_URL = "/proxy/{var:.+}";

  @Override
  public void configure(ProxyConfiguration conf) {

    conf
    //@formatter:off
    .antMatchers("/**/status", "/**/whoami", "/**/ping")
    .get()
    .permitAll()

    .antMatchers("/business/**", "/pratiche/**", "/ecm/**", "/soap/**", "/authorization/**", "/notifications/**")
    .permitAll()

    .antMatchers("/cmmn/process/repository/deployments")
    .post()
    .hasAnyUseCase(UseCase.Constants.ADMIN_COSMO, UseCase.Constants.CONF)


    .antMatchers("/cmmn/process/repository/deployments-m2m")
    .post()
    .hasScope(Scopes.DEPLOY_PROCESS)
    .rewrite("/cmmn/process/repository/deployments")

    .antMatchers("/cmmn/process/cosmo/processi/deploy")
    .post()
    .hasAnyUseCase(UseCase.Constants.ADMIN_COSMO, UseCase.Constants.CONF)

    .antMatchers("/cmmn/**")
    .clientAuthenticated()

    .antMatchers("/**")
    .denyAll();

    //@formatter:on
  }

}
