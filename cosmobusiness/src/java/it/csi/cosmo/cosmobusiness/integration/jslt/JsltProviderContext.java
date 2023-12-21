/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;

/**
 *
 */

public class JsltProviderContext {

  protected Map<String, JsonNode> cache = new HashMap<>();

  protected CosmoTPratica pratica;

  protected StatoPraticaService statoPraticaProvider;

  private JsltProviderContext(Builder builder) {
    this.pratica = builder.pratica;
    this.statoPraticaProvider = builder.statoPraticaProvider;
  }

  public JsltProviderContext() {}

  public Map<String, JsonNode> getCache() {
    return cache;
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public StatoPraticaService getStatoPraticaProvider() {
    return statoPraticaProvider;
  }

  /**
   * Creates builder to build {@link JsltProviderContext}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link JsltProviderContext}.
   */
  public static final class Builder {
    private CosmoTPratica pratica;
    private StatoPraticaService statoPraticaProvider;

    private Builder() {}

    public Builder withPratica(CosmoTPratica pratica) {
      this.pratica = pratica;
      return this;
    }

    public Builder withStatoPraticaProvider(StatoPraticaService statoPraticaProvider) {
      this.statoPraticaProvider = statoPraticaProvider;
      return this;
    }

    public JsltProviderContext build() {
      return new JsltProviderContext(this);
    }
  }


}
