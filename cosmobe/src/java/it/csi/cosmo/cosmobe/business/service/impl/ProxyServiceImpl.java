/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import it.csi.cosmo.common.discovery.model.DiscoveredInstance;
import it.csi.cosmo.common.discovery.model.DiscoveredService;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.helper.FeignClientHeaderHelper;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.common.util.RestTemplateUtils;
import it.csi.cosmo.cosmobe.business.service.DiscoveryDeclarativeFallbackService;
import it.csi.cosmo.cosmobe.business.service.DiscoveryFetchService;
import it.csi.cosmo.cosmobe.business.service.ProxyService;
import it.csi.cosmo.cosmobe.config.Constants;
import it.csi.cosmo.cosmobe.security.AuthenticationTokenManager;
import it.csi.cosmo.cosmobe.security.proxy.model.ACLRewriteHelper;
import it.csi.cosmo.cosmobe.security.proxy.model.ACLVerificationHelper;
import it.csi.cosmo.cosmobe.security.proxy.model.ProxyConfiguration;
import it.csi.cosmo.cosmobe.security.proxy.model.ProxyConfigurationProvider;
import it.csi.cosmo.cosmobe.security.proxy.model.RouteMatchingHelper;
import it.csi.cosmo.cosmobe.security.proxy.model.RouteMatchingHelper.ProxyRouteConfigurationMatch;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerConstants;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

@Service
public class ProxyServiceImpl implements ProxyService, InitializingBean {

  private static final String URI_SEPARATOR = "/";

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.PROXY_LOG_CATEGORY, "ProxyServiceImpl");

  private static final String USER_AGENT = Constants.COMPONENT_DESCRIPTION + "/Proxy Agent";

  private RestTemplate proxyTemplate;

  private enum HeaderProxyPolicy {
    FORWARD, DROP
  }

  @Autowired
  private AuthenticationTokenManager authenticationTokenManager;

  @Autowired
  private DiscoveryFetchService discoveryFetchService;

  @Autowired
  private DiscoveryDeclarativeFallbackService discoveryFallbackService;

  @Autowired
  private ProxyConfigurationProvider proxyConfigurationProvider;

  private Map<String, HeaderProxyPolicy> requestHeaderProxyingPolicies;

  private Map<String, HeaderProxyPolicy> responseHeaderProxyingPolicies;

  private FeignClientHeaderHelper headerHelper =
      new FeignClientHeaderHelper(LoggerConstants.ROOT_LOG_CATEGORY);

  public ProxyServiceImpl() {
    requestHeaderProxyingPolicies = new LinkedHashMap<>();
    requestHeaderProxyingPolicies.put("ACCEPT", HeaderProxyPolicy.FORWARD);
    requestHeaderProxyingPolicies.put("CONTENT-TYPE", HeaderProxyPolicy.FORWARD);
    requestHeaderProxyingPolicies.put("IF\\-.*", HeaderProxyPolicy.FORWARD);
    requestHeaderProxyingPolicies.put("X\\-COSMO\\-.*", HeaderProxyPolicy.DROP);
    requestHeaderProxyingPolicies.put("X\\-.*", HeaderProxyPolicy.FORWARD);
    requestHeaderProxyingPolicies.put(".*", HeaderProxyPolicy.DROP);

    responseHeaderProxyingPolicies = new LinkedHashMap<>();
    responseHeaderProxyingPolicies.put("ETAG", HeaderProxyPolicy.FORWARD);
    responseHeaderProxyingPolicies.put("CONTENT-DISPOSITION", HeaderProxyPolicy.FORWARD);
    responseHeaderProxyingPolicies.put("CONTENT-TYPE", HeaderProxyPolicy.FORWARD);
    responseHeaderProxyingPolicies.put("LOCATION", HeaderProxyPolicy.FORWARD);
    responseHeaderProxyingPolicies.put("X\\-.*", HeaderProxyPolicy.FORWARD);
    responseHeaderProxyingPolicies.put(".*", HeaderProxyPolicy.DROP);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  @Override
  public Optional<DiscoveredInstance> pickInstanceForCall(String serviceName) {

    Optional<DiscoveredService> service = discoveryFetchService.getServices().stream()
        .filter(s -> s.getRegistryEntry().getConfiguration().getName().equals(serviceName))
        .findFirst();

    if (service.isEmpty()) {
      return Optional.empty();
    }

    return pickInstanceForProxying(service.get());
  }

  private ProxyRouteConfigurationMatch verifyACL(UriInfo uriInfo, HttpMethod method) {
    String methodName = "verifyACL";
    ProxyConfiguration conf = this.proxyConfigurationProvider.getConfiguration();

    String path = (URI_SEPARATOR + uriInfo.getPathSegments() // .subList(1,
        // uriInfo.getPathSegments().size())
        .stream().map(PathSegment::getPath).collect(Collectors.joining(URI_SEPARATOR)))
            .replace("//", URI_SEPARATOR);

    LOGGER.debug(methodName, "verifying requested route " + path + " over ACL specifications");

    ProxyRouteConfigurationMatch specification =
        RouteMatchingHelper.findMatchingRoute(conf, path, method);

    if (specification == null) {
      LOGGER.warn(methodName, "requested route " + path
          + " did not match any ACL specifications. Please provide an ACL specification. Route will be allowed");
      return null;
    }

    ACLVerificationHelper.verifyACL(specification.getRouteConfig());
    return specification;
  }

  private String rewriteIfNeeded(UriInfo uriInfo, ProxyRouteConfigurationMatch routeConfigMatch) {
    String inputUri = uriInfo.getPath();

    if (!StringUtils.isBlank(routeConfigMatch.getRouteConfig().getRewrite())) {
      inputUri = removeEdgeSeparators(
          ACLRewriteHelper.rewrite(removeEdgeSeparators(routeConfigMatch.getMatcher()),
              removeEdgeSeparators(routeConfigMatch.getRouteConfig().getRewrite()),
              removeEdgeSeparators(inputUri)));
    }

    return inputUri;
  }

  private String removeEdgeSeparators(String raw) {
    raw = raw.strip();
    while (raw.startsWith("/")) {
      raw = raw.substring(1);
    }
    while (raw.endsWith("/")) {
      raw = raw.substring(0, raw.length() - 1);
    }
    return raw;
  }

  private Response handleLocally(ProxyRouteConfigurationMatch routeConfig) {

    Supplier<Response> handler = routeConfig.getRouteConfig().getHandler();

    return handler.get();
  }

  @Override
  public Response doProxy(HttpServletRequest currentRequest, UriInfo uriInfoInput,
      HttpMethod method) {
    final String methodName = "doProxy";
    LOGGER.debug(methodName, "received request for proxy to " + uriInfoInput.getPath());

    DiscoveredService targetService = null;
    DiscoveredInstance targetInstance = null;
    String baseLocation = null;
    String targetPath = null;
    String firstRoutingErrorMessage = null;

    ProxyRouteConfigurationMatch routeConfig = this.verifyACL(uriInfoInput, method);

    String rewrited;

    if (routeConfig != null) {
      rewrited = rewriteIfNeeded(uriInfoInput, routeConfig);
    } else {
      rewrited = uriInfoInput.getPath();
    }

    // check if local handler
    if (routeConfig != null && routeConfig.getRouteConfig().getHandler() != null) {
      return handleLocally(routeConfig);
    }

    rewrited = removeEdgeSeparators(rewrited);

    targetService = getServiceByPath(rewrited).orElse(null);

    if (targetService != null) {
      targetInstance = pickInstanceForProxying(targetService).orElse(null);
      if (targetInstance != null) {

        targetPath = getSubpathForServiceRoute(
            targetService.getRegistryEntry().getConfiguration().getRoute(), rewrited);

        baseLocation =
            targetInstance.getRegistryEntry().getConfiguration().getLocation().toString();

      } else {
        firstRoutingErrorMessage = "no instances available for service "
            + targetService.getRegistryEntry().getConfiguration().getName();
        LOGGER.error(methodName,
            firstRoutingErrorMessage + "; falling back to automatic discovery");
      }

    } else {
      firstRoutingErrorMessage = "no service known to match route " + rewrited;
      LOGGER.warn(methodName, firstRoutingErrorMessage + "; falling back to automatic discovery");
    }

    if (targetInstance == null) {
      if (!discoveryFallbackService.isEnabled()) {
        throw new NotFoundException(firstRoutingErrorMessage);
      }

      URI fallbackURI;
      String targetServiceName;

      targetService = null;
      targetServiceName = rewrited.split("\\/")[0];
      fallbackURI = discoveryFallbackService.getLocationWithFallbackDiscovery(targetServiceName);

      if (fallbackURI == null) {
        throw new NotFoundException(
            firstRoutingErrorMessage + " AND fallback discovery failed for all routes");
      }

      baseLocation = fallbackURI.toString();
      targetPath = getSubpathForServiceRoute(targetServiceName, rewrited);
    }

    // request params
    List<Entry<String, String>> requestParamsResume = getRequestParams(currentRequest);

    // resolve target url
    URI resolvedTarget = buildProxyURI(baseLocation, targetPath, requestParamsResume);

    // request body
    byte[] body = getRequestBody(currentRequest);

    // headers
    List<Entry<String, String>> headersResume = getRequestHeaders(currentRequest);

    // compute proxy headers
    HttpHeaders headersToSend = buildProxyRequestHeaders(currentRequest, headersResume);

    HttpEntity<byte[]> entity = new HttpEntity<>(body, headersToSend);

    Map<String, Object> output = new LinkedHashMap<>();
    output.put("requested_path", uriInfoInput.getPath());
    output.put("processed_path", rewrited);
    output.put("requested_method", method);
    output.put("target_service", targetService);
    output.put("target_path", targetPath);
    output.put("request_params", requestParamsResume);
    output.put("request_headers", headersResume);
    output.put("internal_request_url", resolvedTarget.toString());
    output.put("internal_request_headers", headersToSend);

    // do proxy call
    return doProxyCall(resolvedTarget, method, entity, output);
  }

  private Response doProxyCall(URI targetURI, HttpMethod method, HttpEntity<?> entity,
      Map<String, Object> output) {

    String methodName = "doProxyCall";
    LOGGER.debug(methodName, "proxying request to target URL [ " + targetURI.getPath()
        + " ] with method [ " + method.name() + " ]");

    ResponseEntity<byte[]> rawCallResult;

    HttpStatus statusResult;
    HttpHeaders headers;
    byte[] body;
    HttpStatusCodeException exception;

    String prefix = "[ " + method.name() + " " + targetURI.toString() + " ] ";

    try {
      LOGGER.info(methodName, prefix + "request start");
      rawCallResult = getProxyTemplate().exchange(targetURI, method, entity, byte[].class);

      statusResult = rawCallResult.getStatusCode();
      LOGGER.info(methodName, prefix + "request completed succesfully, got " + statusResult.name()
          + " (" + statusResult.value() + ")");

      headers = rawCallResult.getHeaders();
      body = rawCallResult.getBody();
      exception = null;

    } catch (HttpStatusCodeException e) {

      statusResult = e.getStatusCode();
      LOGGER.warn(methodName, prefix + "request completed with failure response, got "
          + statusResult.name() + " (" + statusResult.value() + ") - " + e.getMessage());

      headers = e.getResponseHeaders();
      body = e.getResponseBodyAsByteArray();
      exception = e;

    } catch (Exception e) {
      LOGGER.error(methodName, "unexpected non-http error in proxy call", e);
      throw e;
    }

    List<Entry<String, String>> responseHeaders = buildProxyResponseHeaders(headers);

    if (LOGGER.isDebugEnabled()) {

      LOGGER.debug(methodName, "response status is [ " + statusResult + " ]");
      if (body != null && body.length > 0) {
        LOGGER.debug(methodName, "response has a body of [ " + body.length + " ] bytes");
      } else {
        LOGGER.debug(methodName, "response has no body");
      }
    }

    output.put("response_status", statusResult);
    output.put("internal_response_headers", headers);
    output.put("response_body", body);
    output.put("response_headers", responseHeaders);
    output.put("internal_exception", exception);

    ResponseBuilder responseBuilder = Response.status(statusResult.value()).entity(body);
    responseHeaders.forEach(h -> responseBuilder.header(h.getKey(), h.getValue()));

    if (LOGGER.isTraceEnabled()) {
      dumpDetailLog(output);
    }

    return responseBuilder.build();
  }

  private void dumpDetailLog(Map<String, Object> details) {
    if (details == null || details.isEmpty()) {
      return;
    }
    String methodName = "dumpDetailLog";
    String separator = "========================================";

    LOGGER.trace(methodName, separator);
    LOGGER.trace(methodName, "dumping request proxy resume (set log level above TRACE to hide)");

    for (Entry<String, Object> entry : details.entrySet()) {
      String repr;
      if (entry.getValue() != null && entry.getValue().getClass() == byte[].class) {
        repr = "byte[" + ((byte[]) entry.getValue()).length + "]";
      } else {
        repr = ObjectUtils.represent(entry.getValue());
      }
      LOGGER.trace(methodName, "[ {} ]=[ {} ]", entry.getKey(), repr);
    }
    LOGGER.trace(methodName, separator);
  }

  private List<String> getRelativePathSegments(String uriInfo) {

    String[] splitted = uriInfo.split("\\/");

    return Arrays.asList(splitted);
  }

  private String cleanRoute(String raw) {
    return raw.replaceAll("/{2,}", URI_SEPARATOR).replaceAll("^/*", "").replaceAll("/*$", "")
        .trim();
  }

  private Optional<DiscoveredService> getServiceByPath(String uriInfo) {
    String methodName = "getServiceByPath";
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "resolving target service from route match with requested path");
    }

    List<String> segments = getRelativePathSegments(uriInfo);

    String requestedSubroute =
        cleanRoute(segments.stream().collect(Collectors.joining(URI_SEPARATOR)));

    // ottengo tutti i servizi
    Set<DiscoveredService> services = discoveryFetchService.getServices();

    // ordino i servizi per numero di segmenti nella route gestita
    Optional<DiscoveredService> matching = services.stream().sorted((DiscoveredService service1,
        DiscoveredService service2) -> cleanRoute(
            service2.getRegistryEntry().getConfiguration().getRoute()).split(URI_SEPARATOR).length
            - cleanRoute(service1.getRegistryEntry().getConfiguration().getRoute())
                .split(URI_SEPARATOR).length)
        .filter(service -> (requestedSubroute + URI_SEPARATOR).startsWith(
            cleanRoute(service.getRegistryEntry().getConfiguration().getRoute()) + URI_SEPARATOR))
        .findFirst();

    if (LOGGER.isDebugEnabled()) {
      if (matching.isPresent()) {
        LOGGER.debug(methodName, "resolved subroute [ {} ] to service {}",
            matching.get().getRegistryEntry().getConfiguration().getName(), requestedSubroute);
      } else {
        LOGGER.warn(methodName, "could not resolve resolve subroute [ {} ] to any known service",
            requestedSubroute);
      }
    }

    return matching;
  }

  private String getSubpathForServiceRoute(String route, String uriInfo) {
    String methodName = "getSubpathForServiceRoute";
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "resolving subpath for target service from requested path");
    }

    List<String> segments = getRelativePathSegments(uriInfo);
    int numToRemove = cleanRoute(route).split(URI_SEPARATOR).length;

    String output = cleanRoute(segments.subList(numToRemove, segments.size()).stream()
        .collect(Collectors.joining(URI_SEPARATOR)));

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName,
          "resolved subpath [ {} ] for service route [ {} ] from originally requested path", output,
          route);
    }

    return output;
  }

  private Optional<DiscoveredInstance> pickInstanceForProxying(DiscoveredService service) {

    List<DiscoveredInstance> shuffledInstances =
        service.getInstances().stream().filter(i -> i.getStatus().canServeRequests())
            .collect(Collectors.toCollection(ArrayList::new));

    Collections.shuffle(shuffledInstances);

    //@formatter:off
    return shuffledInstances.stream()
        .sorted((DiscoveredInstance i1, DiscoveredInstance i2) ->
        i2.getStatus().getServePriority() - i1.getStatus().getServePriority())
        .findFirst();
    //@formatter:on
  }

  private URI buildProxyURI(String baseLocation, String path,
      List<Entry<String, String>> requestParams) {
    String methodName = "buildProxyURI";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "resolving target full URL from service name");
    }

    UriComponentsBuilder builder;
    try {
      builder = UriComponentsBuilder.fromUriString(baseLocation + "/api/" + path);
    } catch (Exception e) {
      throw new InternalServerException("Error in building proxying uri for instance", e);
    }

    requestParams.forEach(param -> builder.queryParam(param.getKey(), param.getValue()));

    UriComponents built = builder.build().encode();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "resolved target URL to [ {} ]", built.getPath());
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "resolved target full URL with parameters to [ {} ]", built.toUri());
    }

    return built.toUri().normalize();
  }

  private HttpHeaders buildProxyRequestHeaders(HttpServletRequest originalRequest,
      List<Entry<String, String>> requestHeaders) {
    String methodName = "buildProxyRequestHeaders";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "building headers for internal proxy request");
    }

    List<Entry<String, String>> headersToSend = requestHeaders.stream()
        .filter(h -> dropHeaderByPolicies(h.getKey(), requestHeaderProxyingPolicies))
        .collect(Collectors.toList());

    headersToSend.addAll(getInternalProxyHeaders(originalRequest));

    HttpHeaders headersOutput = new HttpHeaders();


    headersToSend.forEach(h -> headersOutput.set(h.getKey(), h.getValue()));

    if (headersToSend.stream().noneMatch(h -> h.getKey().equalsIgnoreCase("Content-Type"))) {
      headersOutput.set("Content-Type", "application/json");
    }

    if (headersToSend.stream().noneMatch(h -> h.getKey().equalsIgnoreCase("Accept"))) {
      headersOutput.set("Accept", "application/json");
    }

    return headersOutput;
  }

  private synchronized RestTemplate getProxyTemplate() {
    if (proxyTemplate == null) {
      //@formatter:off
      proxyTemplate = RestTemplateUtils.builder()
          .withAllowConnectionReuse(false)
          .withReadTimeout(30000)
          .build();
      //@formatter:on

      proxyTemplate.getInterceptors().add(new ProxyRequestLoggingInterceptor());
    }
    return proxyTemplate;
  }

  private List<Entry<String, String>> getInternalProxyHeaders(HttpServletRequest originalRequest) {
    String methodName = "getInternalAuthenticationHeaders";
    List<Entry<String, String>> output = new LinkedList<>();

    output.add(
        new AbstractMap.SimpleEntry<>("Date", RequestUtils.formatDateHeader(OffsetDateTime.now())));

    output.add(new AbstractMap.SimpleEntry<>(
        it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Name",
        Constants.COMPONENT_NAME));

    RequestUtils.getCurrentRequestId().ifPresent(cri -> output.add(new AbstractMap.SimpleEntry<>(
        it.csi.cosmo.common.config.Constants.FEIGN.FEIGN_SOURCE_RAY_HEADER, cri)));

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "adding custom header User-Agent of [ {} ]", USER_AGENT);
    }

    output.add(new AbstractMap.SimpleEntry<>("User-Agent", USER_AGENT));

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "building additional headers for internal authentication");
    }

    output.add(new AbstractMap.SimpleEntry<>(CosmoAuthenticationConfig.TRANSMISSION_HEADER_CLIENT,
        authenticationTokenManager.getSignedTokenForCurrentClient()));

    Optional<String> headerIpValue = RequestUtils.extractIp(originalRequest);

    if (headerIpValue.isPresent()) {
      output.add(new AbstractMap.SimpleEntry<>(
          CosmoAuthenticationConfig.TRANSMISSION_HEADER_ORIGINAL_IP, headerIpValue.get()));
      LOGGER.debug(methodName, "added header [ {} ]=[ {} ] for original IP tracking",
          CosmoAuthenticationConfig.TRANSMISSION_HEADER_ORIGINAL_IP, headerIpValue.get());
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "added header [ {} ]=[ ****** ] for internal user profiling",
          CosmoAuthenticationConfig.TRANSMISSION_HEADER_USER);
      LOGGER.debug(methodName, "added header [ {} ]=[ ****** ] for internal client profiling",
          CosmoAuthenticationConfig.TRANSMISSION_HEADER_CLIENT);

    }

    return output;
  }

  private List<Entry<String, String>> getRequestParams(HttpServletRequest request) {
    String methodName = "getRequestParams";
    List<Entry<String, String>> requestParamsResume = new LinkedList<>();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "parsing request params from original request");
    }

    @SuppressWarnings("unchecked")
    Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      String[] params = request.getParameterValues(paramName);
      for (String param : params) {
        requestParamsResume.add(new AbstractMap.SimpleEntry<String, String>(paramName, param));
      }
    }

    if (LOGGER.isDebugEnabled()) {
      requestParamsResume.forEach(param -> LOGGER.debug(methodName,
          "detected request param [ {} ]=[ {} ]", param.getKey(), param.getValue()));
    }

    return requestParamsResume;
  }

  @SuppressWarnings("unchecked")
  private List<Entry<String, String>> getRequestHeaders(HttpServletRequest request) {
    String methodName = "getRequestHeaders";
    List<Entry<String, String>> headersResume = new LinkedList<>();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "reading request headers from original request");
    }


    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();

      Enumeration<String> headers = request.getHeaders(headerName);
      while (headers.hasMoreElements()) {
        headersResume
            .add(new AbstractMap.SimpleEntry<String, String>(headerName, headers.nextElement()));
      }
    }

    if (LOGGER.isDebugEnabled()) {
      headersResume.forEach(h -> LOGGER.debug(methodName, "detected request header [ {} ]=[ {} ]",
          h.getKey(), h.getValue()));
    }

    return headersResume;
  }

  private byte[] getRequestBody(HttpServletRequest request) {
    String methodName = "getRequestBody";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "reading request body from original request");
    }

    try (InputStream inputStream = request.getInputStream()) {
      if (request.getInputStream() == null) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(methodName, "original request contains no request body");
        }

        return new byte[] {};
      } else {
        byte[] content = IOUtils.toByteArray(inputStream);
        if (LOGGER.isDebugEnabled()) {
          if (content != null && content.length > 0) {
            LOGGER.debug(methodName, "original request contains request body of [ {} ] bytes",
                content.length);
          } else {
            LOGGER.debug(methodName, "original request contains no request body");
          }
        }

        return content;
      }
    } catch (IOException e) {
      LOGGER.error(methodName, "error parsing request body for proxying", e);
      throw new InternalServerException("Errore nella lettura del contenuto della richiesta", e);
    }
  }

  private List<Entry<String, String>> buildProxyResponseHeaders(HttpHeaders responseHeaders) {
    String methodName = "buildProxyResponseHeaders";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(methodName, "reading internal response headers");
    }

    List<Entry<String, String>> headersToReturn =
        responseHeaders.entrySet().stream().filter(entryGroup -> {
          if (entryGroup.getKey().toUpperCase()
              .startsWith(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX.toUpperCase())) {
            if (LOGGER.isDebugEnabled()) {
              LOGGER.debug(methodName,
                  "dropping response header [ {} ] because it's an application header and will be re-forwarded",
                  entryGroup.getKey());
            }
            return false;
          }
          return true;
        }).filter(
            entryGroup -> dropHeaderByPolicies(entryGroup.getKey(), responseHeaderProxyingPolicies))
            .flatMap(entryGroup -> entryGroup.getValue().stream().map(
                value -> new AbstractMap.SimpleEntry<String, String>(entryGroup.getKey(), value)))
            .collect(Collectors.toList());

    // add additional headers here

    for (Entry<String, String> newHeader : headerHelper
        .buildHeadersForResponseForward(responseHeaders.entrySet())) {

      headersToReturn.add(
          new AbstractMap.SimpleEntry<String, String>(newHeader.getKey(), newHeader.getValue()));
    }

    return headersToReturn;
  }

  private boolean dropHeaderByPolicies(String key, Map<String, HeaderProxyPolicy> policies) {
    String methodName = "dropHeaderByPolicies";
    key = key.toUpperCase();

    for (Entry<String, HeaderProxyPolicy> policyEntry : policies.entrySet()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(methodName, "checking header {} against policy pattern {}", key,
            policyEntry.getKey());
      }
      if (key.toUpperCase().matches(policyEntry.getKey())) {
        LOGGER.debug(methodName, "header {} MATCHES policy pattern {}", key, policyEntry.getKey());
        LOGGER.debug(methodName, "applying to header {} policy action {}", key,
            policyEntry.getValue());
        return policyEntry.getValue() == HeaderProxyPolicy.FORWARD;
      } else {
        LOGGER.debug(methodName, "header {} does not match policy pattern {}", key,
            policyEntry.getKey());
      }
    }

    LOGGER.warn(methodName, "header {} did not match ANY policy pattern, dropping", key);
    return false;
  }

  private static class ProxyRequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final CosmoLogger logger =
        LoggerFactory.getLogger(LogCategory.PROXY_LOG_CATEGORY, "ProxyRequestLoggingInterceptor");

    public ProxyRequestLoggingInterceptor() {
      super();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
      String method = "intercept";
      String prefix = "[proxy logging] ";


      if (logger.isDebugEnabled()) {
        logger.debug(method, prefix + "-> preparing to send http proxy request "
            + request.getMethod() + " to " + request.getURI());

        if (request.getHeaders() != null) {
          for (Entry<String, List<String>> header : request.getHeaders().entrySet()) {
            logger.debug(method, prefix + "-> request header: [" + header.getKey() + "] = ["
                + header.getValue() + "]");
          }
        }

      }

      logger.info(method,
          "sending http proxy request " + request.getMethod() + " to " + request.getURI());

      try {
        ClientHttpResponse r = execution.execute(request, body);

        logger.info(method, "got HTTP " + r.getStatusCode() + " in response from proxy request "
            + request.getURI());

        if (logger.isDebugEnabled()) {
          logger.debug(method, prefix + "<- " + r.getStatusCode() + " from " + request.getURI());
          logger.debug(method, prefix + "<- raw response from " + request.getURI() + ": "
              + ObjectUtils.represent(r.getBody()));
          for (Entry<String, List<String>> header : r.getHeaders().entrySet()) {
            logger.debug(method, prefix + "<- response header: [" + header.getKey() + "] = ["
                + header.getValue() + "]");
          }
        }

        return r;
      } catch (HttpStatusCodeException e) {

        logger
            .error(method,
                prefix + "error (HttpStatusCodeException) in proxy request to " + request.getURI()
                    + ": " + e.getStatusCode() + " - " + e.getStatusText() + " - " + e.getMessage(),
                e);

        if (logger.isDebugEnabled()) {
          logger.debug(method, prefix + "<- " + e.getStatusCode() + " - " + e.getStatusText()
              + " from " + request.getURI());
          logger.debug(method, prefix + "<- error response from " + request.getURI() + ": "
              + e.getResponseBodyAsString());
        }
        throw e;
      } catch (RestClientException e) {
        logger.error(method, prefix + "error (RestClientException) in proxy request to "
            + request.getURI() + ": " + e.getMessage(), e);

        throw e;
      } catch (Exception e) {
        logger.error(method, prefix + "error (generic Exception) in proxy request to "
            + request.getURI() + ": " + e.getMessage(), e);
        throw e;
      }
    }
  }

}
