/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ProtocolException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.config.CosmoJacksonDialect;

/**
 *
 */

public abstract class RestTemplateUtils {

  private static final String HEADER_CONNECTION = "Connection";

  private RestTemplateUtils() {
    // private
  }

  public static RestTemplateConfiguration.Builder builder() {
    return RestTemplateConfiguration.builder();
  }

  public static class RestTemplateConfiguration {

    private RestTemplateConfiguration() {
      // NOP
    }

    /**
     * Creates builder to build {@link RestTemplateConfiguration}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * Builder to build {@link RestTemplateConfiguration}.
     */
    public static final class Builder {
      private boolean allowConnectionReuse = false;
      private int connectionRequestTimeout = 5000;
      private int connectTimeout = 5000;
      private int readTimeout = 30000;
      private int maxConnections = 200;
      private int maxConnectionsPerRoute = 20;

      private Builder() {
        // NOP
      }

      public Builder withMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
      }

      public Builder withMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
      }

      public Builder withAllowConnectionReuse(boolean allowConnectionReuse) {
        this.allowConnectionReuse = allowConnectionReuse;
        return this;
      }

      public Builder withConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
      }

      public Builder withConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
      }

      public Builder withReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
      }

      public RestTemplate build() {
        return buildRestTemplate(this);
      }

      private static RestTemplate buildRestTemplate(RestTemplateConfiguration.Builder builder) {
        RestTemplate t = new RestTemplate();

        if (!builder.allowConnectionReuse) {
          t.getInterceptors().add(new ClientHttpRequestInterceptor() {

            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] payload,
                ClientHttpRequestExecution execution) throws IOException {
              if (!request.getHeaders().containsKey(HEADER_CONNECTION)) {
                request.getHeaders().add(HEADER_CONNECTION, "close");
              }
              return execution.execute(request, payload);
            }
          });
        }

        ConnectionReuseStrategy connectionResuseStrategy;
        if (builder.allowConnectionReuse) {
          connectionResuseStrategy = new DefaultConnectionReuseStrategy();
        } else {
          connectionResuseStrategy = new NoConnectionReuseStrategy();
        }

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(builder.maxConnections);
        cm.setDefaultMaxPerRoute(builder.maxConnectionsPerRoute);
        CloseableHttpClient httpClient =
                HttpClients.custom().setConnectionManager(cm).setConnectionReuseStrategy(connectionResuseStrategy)
                .setRedirectStrategy(new DefaultRedirectStrategy() {

                  @Override
                  public boolean isRedirected(org.apache.http.HttpRequest request,
                      org.apache.http.HttpResponse response, HttpContext context)
                          throws ProtocolException {

                    // If redirect intercept intermediate response.
                    if (super.isRedirected(request, response, context)) {

                      // check if direct-fwd header
                      var dnfHeader = response.getFirstHeader(Constants.HEADERS_PREFIX + "DNF");
                      return (dnfHeader == null || StringUtils.isBlank(dnfHeader.getValue()));
                    }
                    return false;
                  }
                }).build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory =
            new HttpComponentsClientHttpRequestFactory(httpClient);

        httpRequestFactory.setConnectionRequestTimeout(builder.connectionRequestTimeout);
        httpRequestFactory.setConnectTimeout(builder.connectTimeout);
        httpRequestFactory.setReadTimeout(builder.readTimeout);

        MappingJackson2HttpMessageConverter jsonConverter =
            new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(CosmoJacksonDialect.getDialectMapper());

        t.getMessageConverters()
        .removeIf(candidate -> candidate instanceof MappingJackson2HttpMessageConverter);

        t.getMessageConverters().add(jsonConverter);

        t.setRequestFactory(httpRequestFactory);
        return t;
      }
    }

  }
}
