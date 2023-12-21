/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.config;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobe.security.Scopes;
import it.csi.cosmo.cosmobe.security.proxy.model.AbstractProxyConfigurationProvider;
import it.csi.cosmo.cosmobe.security.proxy.model.ProxyConfiguration;

/**
 *
 */
@Component
public class ProxyConfig extends AbstractProxyConfigurationProvider {

  public static final String PROXY_CATCHALL = "{var:.+}";

  @Override
  public void configure(ProxyConfiguration conf) {

    conf
    //@formatter:off

    .antMatchers("/notifiche")
    .post()
    .authenticated()
    .rewrite("/notifications/fruitori/notifiche")

    .antMatchers("/documenti")
    .post()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/ecm/fruitori/documenti")

    .antMatchers("/documenti/link")
    .post()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/ecm/fruitori/documenti/link")

    .antMatchers("/documenti/{idDocumento}/contenuto")
    .get()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/ecm/fruitori/documenti/{idDocumento}/contenuto")

    .antMatchers("/pratiche")
    .post()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/pratiche/fruitori/pratiche")

    .antMatchers("/pratiche/{idPraticaExt}/in-relazione")
    .put()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/pratiche/fruitori/pratiche/{idPraticaExt}/in-relazione")

    .antMatchers("/pratiche/{idPratica}")
    .get()
    .authenticated()
    .rewrite("/business/fruitori/pratiche/{idPratica}")

    .antMatchers("/pratiche/{idPratica}/segnala")
    .post()
    .authenticated()
    .rewrite("/business/fruitori/pratiche/{idPratica}/segnala")

    .antMatchers("/pratiche-esterne")
    .post()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/business/fruitori/pratiche-esterne")

    .antMatchers("/pratiche-esterne/*")
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/business/fruitori/pratiche-esterne/*")

    .antMatchers("/eventi")
    .post()
    .authenticated()
    .rewrite("/business/fruitori/evento")

    .antMatchers("/eventi/{idEvento}")
    .authenticated()
    .rewrite("/business/fruitori/evento/{idEvento}")

    .antMatchers("/evento")
    .post()
    .authenticated()
    .rewrite("/business/fruitori/evento")

    .antMatchers("/evento/{idEvento}")
    .authenticated()
    .rewrite("/business/fruitori/evento/{idEvento}")

    .antMatchers("/processo")
    .post()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/business/fruitori/processo")

    .antMatchers("/pratiche/{idPratica}")
    .put()
    .hasScope(Scopes.AP_PRAT.name())
    .rewrite("/business/pratiche/{idPratica}")

    .antMatchers("/*/status", "/*/whoami", "/*/ping")
    .get()
    .permitAll()

    .antMatchers("/callback/esitoSmistaDocumento")
    .post()
    .hasScope(Scopes.STARDAS_CALLBACK.name())
    .rewrite("/business/callback/esitoSmistaDocumento")

    .antMatchers("/callback/esitoSmistaDocumento")
    .put()
    .hasScope(Scopes.STARDAS_CALLBACK.name())
    .rewrite("/business/callback/esitoSmistaDocumento")

    .antMatchers("/**")
    .denyAll();

    //@formatter:on
  }

}
