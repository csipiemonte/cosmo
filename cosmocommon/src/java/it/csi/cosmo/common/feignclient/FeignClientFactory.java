/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.feignclient.exception.BadFeignClientConfigurationException;
import it.csi.cosmo.common.feignclient.exception.FeignClientBadRequestException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientConflictException;
import it.csi.cosmo.common.feignclient.exception.FeignClientForbiddenException;
import it.csi.cosmo.common.feignclient.exception.FeignClientNotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientRestException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientStatusCodeException;
import it.csi.cosmo.common.feignclient.exception.FeignClientUnauthorizedException;
import it.csi.cosmo.common.feignclient.exception.FeignClientUnprocessableEntityException;
import it.csi.cosmo.common.feignclient.helper.UrlPathEncoder;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.common.feignclient.model.FeignClientBeforeRequestInterceptor;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.feignclient.model.FeignClientContextConfigurator;
import it.csi.cosmo.common.feignclient.model.FeignClientRequestContext;
import it.csi.cosmo.common.feignclient.proto.FeignClientLoggingInterceptor;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 *
 */

public class FeignClientFactory {

  private static final String LOG_FEIGN_PREFIX = "[feign %s ] ";

  private static final String FORMAT_APPLICATION_JSON = "application/json";

  private static final String HEADER_ACCEPT = "Accept";

  private static final String HEADER_CONTENT_TYPE = "Content-Type";

  private static final String HEADER_DATE = "Date";

  private static final String URI_PROPERTIES_PLACEHOLDER = "\\$\\{([^\\}]+)\\}";

  private static final String URI_PARAMS_PLACEHOLDER = "\\{([^\\}]+)\\}";

  private static final String QUERY_ENCODING = "UTF-8";

  private static final String HEADER_INTERCEPTED_MARKER = Constants.HEADERS_PREFIX + "Intercepted";

  private Logger logger;

  private String loggingPrefix;

  public FeignClientFactory(String loggingPrefix) {
    this.loggingPrefix = loggingPrefix;
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".feignclient.FeignClientFactory");
  }

  public <T> T buildClient(Class<T> clazz, FeignClientContext rootContext) {

    logger.debug("building client for interface " + clazz);

    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);

    FeignClientContext context = new FeignClientContext();
    context.setProvider(rootContext.getProvider());
    context.setRestTemplate(rootContext.getRestTemplate());

    enhancer.setCallback(buildCallback(clazz, context));

    @SuppressWarnings("unchecked")
    T proxy = (T) enhancer.create();
    return proxy;
  }

  private <T> void applyConfiguratorsIfNeeded(Class<T> clazz, FeignClientContext context) {
    if (!context.isConfigured()) {
      context.setConfigured(true);

      FeignClient clientAnnotation = clazz.getAnnotation(FeignClient.class);

      if (clientAnnotation.configurator() != null) {
        for (Class<? extends FeignClientContextConfigurator> configurator : clientAnnotation
            .configurator()) {
          applyConfigurator(clazz, context, configurator);
        }
      }

      if (clientAnnotation.interceptors() != null) {
        for (Class<? extends FeignClientBeforeRequestInterceptor> interceptor : clientAnnotation
            .interceptors()) {
          applyInterceptor(clazz, context, interceptor);
        }
      }

      // add logging interceptor
      context.getRestTemplate().getInterceptors()
          .add(new FeignClientLoggingInterceptor(loggingPrefix));
    }
  }

  private <T> void applyConfigurator(Class<T> clazz, FeignClientContext context,
      Class<? extends FeignClientContextConfigurator> configuratorClass) {

    FeignClientContextConfigurator configBean = context.getProvider().getBean(configuratorClass);

    if (configBean == null) {
      throw new InternalServerErrorException(
          "Feign client configurator class not found: " + clazz.getName());
    }

    configBean.configure(context);
  }

  private <T> void applyInterceptor(Class<T> clazz, FeignClientContext context,
      Class<? extends FeignClientBeforeRequestInterceptor> configuratorClass) {

    FeignClientBeforeRequestInterceptor configBean =
        context.getProvider().getBean(configuratorClass);

    if (configBean == null) {
      throw new InternalServerErrorException(
          "Feign client interceptor class not found: " + clazz.getName());
    }

    context.getRestTemplate().getInterceptors().add(new ClientHttpRequestInterceptor() {

      @Override
      public ClientHttpResponse intercept(HttpRequest request, byte[] body,
          ClientHttpRequestExecution execution) throws IOException {

        var key = configuratorClass.getSimpleName();

        HttpHeaders headers = request.getHeaders();
        var found = headers.getFirst(HEADER_INTERCEPTED_MARKER);
        boolean apply = true;

        if (!StringUtils.isBlank(found)) {
          if (found.contains("|" + key + "|")) {
            logger.warn("skipped duplicate interceptor call - is http call a retry?");
            apply = false;
          }
        } else {
          found = "|";
        }

        if (apply) {
          found = found + key + "|";
          headers.remove(HEADER_INTERCEPTED_MARKER);
          headers.set(HEADER_INTERCEPTED_MARKER, found);

          if (logger.isDebugEnabled()) {
            logger.debug("applying interceptor " + configuratorClass.getSimpleName() + " to "
                + request.getMethod() + " request to " + request.getURI().toString());
          }

          configBean.beforeRequest(request, context);
        }

        if (logger.isTraceEnabled()) {
          logger.trace("interceptor marker for " + request.getMethod() + " request to "
              + request.getURI().toString() + " is " + found);
        }

        return execution.execute(request, body);
      }
    });
  }

  private <T> MethodInterceptor buildCallback(Class<T> clazz, FeignClientContext context) {
    return new MethodInterceptor() {

      @Override
      public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
          throws Throwable {
        if (!context.isConfigured()) {
          applyConfiguratorsIfNeeded(clazz, context);
        }

        return interceptOuter(clazz, obj, method, args, proxy, context);
      }
    };
  }

  private Object interceptOuter(Class<?> clazz, Object obj, Method method, Object[] args,
      MethodProxy proxy, FeignClientContext context) throws Throwable {
    if (method.getDeclaringClass() == Object.class) {
      return proxy.invokeSuper(obj, args);
    }

    var requestContext = new FeignClientRequestContext(context);

    if (logger.isTraceEnabled()) {
      var prefix = String.format(LOG_FEIGN_PREFIX, requestContext.getRequestId());
      logger.trace(prefix + "preparing feign call from method " + method.getName());
    }

    requestContext.setMethod(getHttpMethod(method));

    var annotationOnClass = clazz.getAnnotation(FeignClient.class);
    var pathAnnotationOnClass = clazz.getAnnotation(Path.class);
    var pathAnnotationOnMethod = method.getAnnotation(Path.class);
    var producesAnnotationOnMethod = method.getAnnotation(Produces.class);

    checkForDuplicatePath(annotationOnClass, pathAnnotationOnClass, clazz);

    requestContext.setBody(extractPayload(method, args));

    sortParametersInContext(requestContext, method, args);

    requestContext.setUri(
        buildURI(requestContext, annotationOnClass, pathAnnotationOnClass, pathAnnotationOnMethod));

    checkExplicitContentType(requestContext, producesAnnotationOnMethod);

    addCommonFeignHeaders(requestContext);

    return execute(requestContext, method);
  }

  private void checkExplicitContentType(FeignClientRequestContext requestContext,
      Produces producesAnnotationOnMethod) {

    if (producesAnnotationOnMethod != null) {
      requestContext.setExplicitContentType(producesAnnotationOnMethod.value()[0]);
    }
  }

  private URI buildURI(FeignClientRequestContext requestContext, FeignClient annotationOnClass,
      Path pathAnnotationOnClass, Path pathAnnotationOnMethod) throws UnsupportedEncodingException {

    var prefix = String.format(LOG_FEIGN_PREFIX, requestContext.getRequestId());
    String rawUrl = buildUrlFromSegments(annotationOnClass, pathAnnotationOnClass,
        pathAnnotationOnMethod, requestContext);

    rawUrl = replaceParamsInUrl(prefix, rawUrl, requestContext.getPathParams());

    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(rawUrl);
    requestContext.getQueryParams().entrySet()
        .forEach(param -> builder.queryParam(param.getKey(), param.getValue()));

    URI uri = builder.build().encode().toUri();
    logger.trace(prefix + "parsed uri " + uri.toString());

    return uri;
  }

  private void sortParametersInContext(FeignClientRequestContext requestContext, Method method,
      Object[] args) {

    int parameterIndex = 0;

    for (Annotation[] annotationsOnArgument : method.getParameterAnnotations()) {
      // parametro con annotations
      sortParameterInContext(requestContext, annotationsOnArgument, args, parameterIndex);
      parameterIndex++;
    }
  }

  private void sortParameterInContext(FeignClientRequestContext requestContext,
      Annotation[] annotationsOnArgument, Object[] args, int parameterIndex) {

    for (Annotation annotationOnArgument : annotationsOnArgument) {
      if (annotationOnArgument instanceof PathParam) {
        addPathParam(requestContext, annotationOnArgument, args[parameterIndex]);

      } else if (annotationOnArgument instanceof QueryParam) {
        addQueryParam(requestContext, annotationOnArgument, args[parameterIndex]);

      } else if (annotationOnArgument instanceof HeaderParam) {
        addHeaderParam(requestContext, annotationOnArgument, args[parameterIndex]);
      }
    }
  }

  private void addHeaderParam(FeignClientRequestContext requestContext,
      Annotation annotationOnArgument, Object value) {
    HeaderParam typedAnnotation = (HeaderParam) annotationOnArgument;
    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
      return;
    }

    requestContext.getHeaders().add(typedAnnotation.value(),
        resolveProperties(value.toString(), requestContext.getFeignContext()));
  }

  private void addQueryParam(FeignClientRequestContext requestContext,
      Annotation annotationOnArgument, Object value) {
    QueryParam typedAnnotation = (QueryParam) annotationOnArgument;

    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
      return;
    }
    requestContext.getQueryParams().put(typedAnnotation.value(), value.toString());
  }

  private void addPathParam(FeignClientRequestContext requestContext,
      Annotation annotationOnArgument, Object value) {
    var prefix = String.format(LOG_FEIGN_PREFIX, requestContext.getRequestId());

    PathParam typedAnnotation = (PathParam) annotationOnArgument;
    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
      throw new BadFeignClientConfigurationException(
          "Empty PathParam for given placeholder: " + typedAnnotation.value());
    }
    logger.trace(
        prefix + "resolved URI placeholder " + typedAnnotation.value() + " to " + value.toString());
    requestContext.getPathParams().put(typedAnnotation.value(), value.toString());
  }

  private Object extractPayload(Method method, Object[] args) {

    Object payload = null;

    int parameterIndex = 0;

    for (Annotation[] annotationsOnArgument : method.getParameterAnnotations()) {
      if (parameterIsPayload(annotationsOnArgument)) {
        if (payload != null) {
          throw new BadFeignClientConfigurationException(
              "Multiple payload methods for " + method.getName());
        }

        payload = args[parameterIndex];
      }
      parameterIndex++;
    }

    return payload;
  }

  private boolean parameterIsPayload(Annotation[] annotationsOnArgument) {
    for (Annotation a : annotationsOnArgument) {
      if (a instanceof PathParam || a instanceof QueryParam || a instanceof HeaderParam
          || a instanceof Context) {
        return false;
      }
    }
    return true;
  }

  private String buildUrlFromSegments(FeignClient annotationOnClass, Path pathAnnotationOnClass,
      Path pathAnnotationOnMethod, FeignClientRequestContext requestContext) {
    String rawUrl = "";
    if (pathAnnotationOnClass != null) {
      rawUrl += resolveProperties(pathAnnotationOnClass.value(), requestContext.getFeignContext());
      if (pathAnnotationOnMethod != null) {
        rawUrl += "/";
      }
    } else if (annotationOnClass != null && !StringUtils.isEmpty(annotationOnClass.value())) {
      rawUrl += resolveProperties(annotationOnClass.value(), requestContext.getFeignContext());
      if (pathAnnotationOnMethod != null) {
        rawUrl += "/";
      }
    }
    if (pathAnnotationOnMethod != null) {
      rawUrl += resolveProperties(pathAnnotationOnMethod.value(), requestContext.getFeignContext());
    }
    return rawUrl;
  }

  private String resolveProperties(String raw, FeignClientContext context) {
    if (raw == null || !raw.contains("$" + "")) {
      return raw;
    }

    final Pattern uriParamsPattern = Pattern.compile(URI_PROPERTIES_PLACEHOLDER);
    Matcher uriParamsMatcher = uriParamsPattern.matcher(raw);
    while (uriParamsMatcher.find()) {
      for (int i = 0; i < uriParamsMatcher.groupCount(); i++) {
        String rawMatch = uriParamsMatcher.group(i);
        String match = rawMatch.replace("$" + "{", "").replace("}", "").trim();

        logger.trace("resolving URI placeholder " + match);

        String value = context.getProvider().resolveConfiguration(match);
        if (StringUtils.isBlank(value)) {
          throw new BadFeignClientConfigurationException(
              "Missing property " + match + " for placeholder: " + raw);
        }

        raw = raw.replace(rawMatch, value);
      }
    }

    return raw;
  }

  private void checkForDuplicatePath(FeignClient annotationOnClass, Path pathAnnotationOnClass,
      Class<?> clazz) {
    if (pathAnnotationOnClass != null && annotationOnClass != null
        && !StringUtils.isEmpty(pathAnnotationOnClass.value())
        && !StringUtils.isEmpty(annotationOnClass.value())) {
      throw new BadFeignClientConfigurationException(
          "Duplicate root path on class " + clazz.getName());
    }
  }

  private void addCommonFeignHeaders(FeignClientRequestContext requestContext) {
    var headers = requestContext.getHeaders();

    if (headers.keySet().stream().noneMatch(c -> c.equalsIgnoreCase(HEADER_CONTENT_TYPE))) {
      headers.add(HEADER_CONTENT_TYPE,
          requestContext.getExplicitContentType() != null ? requestContext.getExplicitContentType()
              : FORMAT_APPLICATION_JSON);
    }

    headers.add(HEADER_ACCEPT, FORMAT_APPLICATION_JSON);
    headers.add(Constants.FEIGN.FEIGN_MARKER_HEADER, Boolean.TRUE.toString());
    headers.add(Constants.FEIGN.M2M_MARKER_HEADER, Boolean.TRUE.toString());
    headers.add(Constants.FEIGN.FEIGN_RAY_ID_HEADER, requestContext.getRequestId());
    headers.add(Constants.FEIGN.FEIGN_RAY_ID_HEADER, requestContext.getRequestId());
    headers.add(HEADER_DATE, RequestUtils.formatDateHeader(OffsetDateTime.now()));

    RequestUtils.getCurrentRequestId()
        .ifPresent(cri -> headers.add(Constants.FEIGN.FEIGN_SOURCE_RAY_HEADER, cri));
  }

  private String replaceParamsInUrl(String prefix, String rawUrl, Map<String, String> pathParams)
      throws UnsupportedEncodingException {
    final Pattern uriParamsPattern = Pattern.compile(URI_PARAMS_PLACEHOLDER);
    Matcher uriParamsMatcher = uriParamsPattern.matcher(rawUrl);
    while (uriParamsMatcher.find()) {
      for (int i = 0; i < uriParamsMatcher.groupCount(); i++) {
        String rawMatch = uriParamsMatcher.group(i);
        String match = rawMatch.replace("{", "").replace("}", "").trim();

        logger.trace(prefix + "resolving URI placeholder " + match);

        if (!pathParams.containsKey(match)) {
          throw new BadFeignClientConfigurationException(
              "No PathParam for given placeholder: " + match);
        } else {
          rawUrl = rawUrl.replace(rawMatch, UrlPathEncoder.encode(pathParams.get(match)));
        }
      }
    }

    return rawUrl;
  }

  private HttpMethod getHttpMethod(Method method) {
    HttpMethod httpMethod;

    if (method.getAnnotation(GET.class) != null) {
      httpMethod = HttpMethod.GET;

    } else if (method.getAnnotation(POST.class) != null) {
      httpMethod = HttpMethod.POST;

    } else if (method.getAnnotation(PUT.class) != null) {
      httpMethod = HttpMethod.PUT;

    } else if (method.getAnnotation(DELETE.class) != null) {
      httpMethod = HttpMethod.DELETE;

    } else if (method.getAnnotation(OPTIONS.class) != null) {
      httpMethod = HttpMethod.OPTIONS;

    } else if (method.getAnnotation(HEAD.class) != null) {
      httpMethod = HttpMethod.HEAD;

    } else {
      throw new BadFeignClientConfigurationException(
          "Unrecognized HTTP method on " + method.getName());
    }

    return httpMethod;
  }

  private ResponseEntity<?> wrapCall(FeignClientRequestContext requestContext,
      Callable<ResponseEntity<?>> callable) throws Exception {
    String prefix = "[feign " + requestContext.getRequestId() + "] ";

    try {
      var r = callable.call();

      logger.info(prefix + "<- " + r.getStatusCodeValue() + " - "
          + r.getStatusCode().getReasonPhrase() + " from " + requestContext.getUri());

      return r;

    } catch (HttpClientErrorException e) {
      logHttpStatusCodeException(requestContext, e);
      throw convertClientErrorException(requestContext, e);

    } catch (HttpServerErrorException e) {
      logHttpStatusCodeException(requestContext, e);
      throw convertServerErrorException(requestContext, e);

    } catch (HttpStatusCodeException e) {
      logHttpStatusCodeException(requestContext, e);
      throw convertStatusCodeException(requestContext, e);

    } catch (RestClientException e) {
      logger.error(prefix + "rethrowing error from feign request to " + requestContext.getUri()
          + ": " + e.getMessage(), e);
      throw new FeignClientRestException(requestContext, e);

    } catch (Exception e) {
      logger.error(prefix + "rethrowing error from feign request to " + requestContext.getUri()
          + ": " + e.getMessage(), e);
      throw e;
    }
  }

  private FeignClientClientErrorException convertClientErrorException(
      FeignClientRequestContext requestContext, HttpClientErrorException e) {

    var responseIsJson = MediaType.APPLICATION_JSON.equals(e.getResponseHeaders().getContentType());
    ErrorMessageDTO decoded = null;
    if (responseIsJson) {
      decoded = attemptDecode(e.getResponseBodyAsString(), e);
    }

    if (e.getStatusCode() != null) {
      switch (e.getStatusCode()) {
        case BAD_REQUEST:
          return new FeignClientBadRequestException(requestContext, e, decoded);
        case UNAUTHORIZED:
          return new FeignClientUnauthorizedException(requestContext, e, decoded);
        case FORBIDDEN:
          return new FeignClientForbiddenException(requestContext, e, decoded);
        case NOT_FOUND:
          return new FeignClientNotFoundException(requestContext, e, decoded);
        case CONFLICT:
          return new FeignClientConflictException(requestContext, e, decoded);
        case UNPROCESSABLE_ENTITY:
          return new FeignClientUnprocessableEntityException(requestContext, e, decoded);
        default:
          break;
      }
    }

    return new FeignClientClientErrorException(requestContext, e, decoded);
  }

  private FeignClientServerErrorException convertServerErrorException(
      FeignClientRequestContext requestContext, HttpServerErrorException e) {

    var responseIsJson = MediaType.APPLICATION_JSON.equals(e.getResponseHeaders().getContentType());
    ErrorMessageDTO decoded = null;
    if (responseIsJson) {
      decoded = attemptDecode(e.getResponseBodyAsString(), e);
    }

    return new FeignClientServerErrorException(requestContext, e, decoded);
  }

  private FeignClientStatusCodeException convertStatusCodeException(
      FeignClientRequestContext requestContext, HttpStatusCodeException e) {

    var responseIsJson = MediaType.APPLICATION_JSON.equals(e.getResponseHeaders().getContentType());
    ErrorMessageDTO decoded = null;
    if (responseIsJson) {
      decoded = attemptDecode(e.getResponseBodyAsString(), e);
    }

    return new FeignClientStatusCodeException(requestContext, e, decoded);
  }

  private void logHttpStatusCodeException(FeignClientRequestContext requestContext,
      HttpStatusCodeException e) {
    String prefix = "[feign " + requestContext.getRequestId() + "] ";
    logger.error(prefix + "received error (HttpStatusCodeException) from feign request to "
        + requestContext.getUri() + ": " + e.getStatusCode() + " - " + e.getStatusText() + " - "
        + e.getMessage(), e);

    logger.info(prefix + "<- " + e.getStatusCode() + " - " + e.getStatusText() + " from "
        + requestContext.getUri());
    if (logger.isDebugEnabled()) {
      logger.debug(prefix + "<- repeating error response from " + requestContext.getUri() + ": "
          + e.getResponseBodyAsString());
    }
  }

  private Object execute(FeignClientRequestContext requestContext, Method method) throws Exception {

    var prefix = String.format(LOG_FEIGN_PREFIX, requestContext.getRequestId());

    HttpEntity<Object> requestEntity =
        new HttpEntity<>(requestContext.getBody(), requestContext.getHeaders());

    boolean returnsResponse = (method.getReturnType().equals(ResponseEntity.class));
    boolean returnsJaxRsResponse = (method.getReturnType().equals(javax.ws.rs.core.Response.class));
    boolean returnsVoid = method.getReturnType().equals(Void.TYPE);

    Class<?> returnType = returnsVoid ? Object.class : method.getReturnType();

    logger.info("http feign request " + requestContext.getMethod() + " to "
        + requestContext.getUri() + " (ray " + requestContext.getRequestId() + ")");

    if (logger.isDebugEnabled()) {
      logger.debug(prefix + "-> preparing http feign request " + requestContext.getMethod() + " to "
          + requestContext.getUri());
      if (requestContext.getBody() != null) {
        logger.debug(
            prefix + "-> request payload: " + ObjectUtils.represent(requestContext.getBody()));
      }
    }

    if (returnsJaxRsResponse) {
      // returns javax.ws.rs.core.Response
      ResponseEntity<?> result = wrapCall(requestContext,
          () -> requestContext.getFeignContext().getRestTemplate().exchange(requestContext.getUri(),
              requestContext.getMethod(), requestEntity, Object.class));

      return Response.status(result.getStatusCodeValue()).entity(result.getBody()).build();

    } else if (returnsResponse) {
      // returns ResponseEntity
      return wrapCall(requestContext,
          () -> requestContext.getFeignContext().getRestTemplate().exchange(requestContext.getUri(),
              requestContext.getMethod(), requestEntity, Object.class));

    } else {
      // returns raw POJO
      ResponseEntity<?> result = wrapCall(requestContext,
          () -> requestContext.getFeignContext().getRestTemplate().exchange(requestContext.getUri(),
              requestContext.getMethod(), requestEntity, returnType));
      if (!returnsVoid) {
        return result.getBody();
      } else {
        return null;
      }
    }
  }

  private ErrorMessageDTO attemptDecode(String raw, HttpStatusCodeException originalException) {
    // attempt decode as cosmo error
    try {
      ErrorMessageDTO decoded = ObjectUtils.fromJson(raw, ErrorMessageDTO.class);
      if (!StringUtils.isEmpty(decoded.getCode()) && !StringUtils.isEmpty(decoded.getTitle())
          && decoded.getStatus() != null && decoded.getStatus() >= 400) {
        return decoded;
      }
    } catch (Exception e) {
      // l'oggetto di risposta non e' probabilmente un ErrorMessageDTO
    }

    // attempt decode as flowable error
    try {
      @SuppressWarnings("unchecked")
      Map<String, String> decoded = ObjectUtils.fromJson(raw, HashMap.class);

      if (decoded != null && !StringUtils.isEmpty(decoded.get("message"))
          && !StringUtils.isEmpty(decoded.get("exception"))) {
        return ErrorMessageDTO.builder().withCodice(decoded.get("message").toUpperCase())
            .withMessaggio(decoded.get("exception"))
            .withStatus(originalException.getRawStatusCode()).build();
      }
    } catch (Exception e) {
      // l'oggetto di risposta non e' probabilmente un errore di flowable
    }

    return null;
  }
}
