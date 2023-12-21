/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;


public abstract class AbstractCosmoRequestLoggingInterceptor {

  /**
   *
   */
  private static final String UNIQUE_RAY_RESUME = "uniqueRayResume";
  private static final String UNIQUE_RAY_ID_HEADER = Constants.HEADERS_PREFIX + "Ray-Id";
  private static final String TRANSFER_ENCODING_HEADER = "Transfer-Encoding";

  private static final String ATTRIBUTES_STATS_PREFIX = "stat_";
  private static final String ATTRIBUTES_STATS_ENABLED = "statsEnabled";
  private static final String ATTRIBUTES_START_TIME = ATTRIBUTES_STATS_PREFIX + "startTime";
  private static final String ATTRIBUTES_END_TIME = ATTRIBUTES_STATS_PREFIX + "endTime";
  private static final String ATTRIBUTES_X_HEADERS = ATTRIBUTES_STATS_PREFIX + "xRequestHeaders";
  private static final String ATTRIBUTES_ORIGIN_IP = ATTRIBUTES_STATS_PREFIX + "originIp";

  private CosmoLogger logger;

  private CosmoLogger statsLogger;

  private boolean doRemoveChunkedHeader;

  public AbstractCosmoRequestLoggingInterceptor(String loggingCategory,
      boolean doRemoveChunkedHeader) {
    logger = new CosmoLogger(loggingCategory, "CosmoRequestLoggingInterceptor");
    statsLogger = new CosmoLogger(Constants.PRODUCT + ".profiling", "CosmoRequestStats");
    this.doRemoveChunkedHeader = doRemoveChunkedHeader;
  }

  protected Map<String, Object> gatherStatistics() {
    return null;
  }

  boolean isStatsEnabled() {
    return statsLogger != null && statsLogger.isDebugEnabled();
  }

  boolean dumpBody(HttpServletRequest r) {
    if (r == null) {
      return false;
    }

    return true;
  }

  boolean isJson(HttpServletRequest r) {

    if (r == null) {
      return false;
    }

    // define rules when to read body
    return r.getContentType() != null
        && r.getContentType().toLowerCase().contains("application/json");
  }

  public void before(RequestFilterRequestAdapter r) throws IOException {

    HttpServletRequest request = r.getRequest();
    if (request == null) {
      return;
    }

    // assign ray id
    String rayId = RequestUtils.getCurrentRequestId().orElse(null);

    // log ray id
    if (logger.isDebugEnabled()) {
      logger.debug("http", request.getMethod() + " " + request.getRequestURL() + " - id " + rayId);
    }

    // log BEGIN
    logger.beginForClass(r.getTargetClassName(), r.getTargetMethodName());

    if (isStatsEnabled()) {
      addStatsToRequest(r);
    }

    if (logger.isDebugEnabled()) {
      addLogInfoToRequest(r);
    }
  }

  public void after(RequestFilterResponseAdapter responseContext) throws IOException {

    try {
      postProcessResponse(responseContext);
    } finally {
      logger.endForClass(responseContext.getTargetClassName(),
          responseContext.getTargetMethodName());
    }
  }

  @SuppressWarnings("unchecked")
  private void addStatsToRequest(RequestFilterRequestAdapter r) {
    HttpServletRequest request = r.getRequest();
    if (request == null) {
      return;
    }

    if (!isStatsEnabled()) {
      return;
    }

    // add stats for collection
    request.setAttribute(ATTRIBUTES_STATS_ENABLED, true);
    request.setAttribute(ATTRIBUTES_START_TIME, LocalDateTime.now().toString());
    request.setAttribute(ATTRIBUTES_ORIGIN_IP, request.getRemoteAddr());

    // add x- headers
    Map<String, List<String>> xHeaders = new HashMap<>();
    request.getHeaderNames().asIterator().forEachRemaining((Object headerNameRaw) -> {
      String headerName = (String) headerNameRaw;
      if (headerName.toUpperCase().startsWith("X-")
          && !headerName.toUpperCase().startsWith("X-COSMO-CLIENT")
          && !headerName.toUpperCase().startsWith("X-COSMO-USER")) {
        List<String> values = new ArrayList<>();
        request.getHeaders(headerName).asIterator().forEachRemaining(headerValue -> {
          values.add((String) headerValue);
        });
        xHeaders.put(headerName, values);
      }
    });
    if (!xHeaders.isEmpty()) {
      request.setAttribute(ATTRIBUTES_X_HEADERS, xHeaders);
    }
  }

  private void addLogInfoToRequest(RequestFilterRequestAdapter responseContext) {

    HttpServletRequest request = responseContext.getRequest();
    if (request == null) {
      return;
    }

    String prefix = null;
    List<String> resume = new LinkedList<>();
    String line;

    String rayId = RequestUtils.getCurrentRequestId().orElse(null);

    prefix = "[RAY " + rayId + "] ";
    request.setAttribute(UNIQUE_RAY_RESUME, resume);

    line = request.getMethod() + " to " + request.getRequestURI();
    logger.debugForClass(responseContext.getTargetClassName(),
        responseContext.getTargetMethodName(), prefix + line);
    resume.add("\n" + line + "\n");

    if (logger.isTraceEnabled()) {
      try {
        dumpRequestParams(request, resume);
      } catch (Exception e) {
        logger.warnForClass(responseContext.getTargetClassName(),
            responseContext.getTargetMethodName(), "Could not log request params", e);
      }

      try {
        dumpRequestBody(request, resume);
      } catch (Exception e) {
        logger.warnForClass(responseContext.getTargetClassName(),
            responseContext.getTargetMethodName(), "Could not log request body", e);
      }

      try {
        dumpCookies(request, resume);
      } catch (Exception e) {
        logger.warnForClass(responseContext.getTargetClassName(),
            responseContext.getTargetMethodName(), "Could not log request cookies", e);
      }

      try {
        dumpHeaders(request, resume);
      } catch (Exception e) {
        logger.warnForClass(responseContext.getTargetClassName(),
            responseContext.getTargetMethodName(), "Could not log request headers", e);
      }
    }
  }

  private void dumpRequestBody(HttpServletRequest request, List<String> resume) throws IOException {

    if (request instanceof CosmoContentCachingRequestWrapper) {
      String json = getMessagePayload(request);

      if (!StringUtils.isBlank(json)) {
        resume.add("REQUEST BODY = \n" + json + "\n");
      } else {
        resume.add("REQUEST BODY = NONE");
      }
    }
  }

  protected String getMessagePayload(HttpServletRequest request) throws IOException {
    CosmoContentCachingRequestWrapper wrapper =
        WebUtils.getNativeRequest(request, CosmoContentCachingRequestWrapper.class);
    if (wrapper != null) {

      var requestBodyBytes = wrapper.getCachedContent();
      if (requestBodyBytes != null) {
        if (requestBodyBytes.length > 0) {
          if (isJson(request)) {
            return attemptBeautification(
                new String(requestBodyBytes, wrapper.getCharacterEncoding()));
          } else {
            return dumpByteArray(requestBodyBytes);
          }
        }
      }
    }
    return null;
  }

  private String attemptBeautification(String raw) {
    try {
      if (StringUtils.isBlank(raw)) {
        return raw;
      }
      var mapper = ObjectUtils.getMapper();
      return mapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(mapper.readValue(raw, Map.class));
    } catch (Exception e) {
      logger.warn("attemptBeautification",
          "content beautification attempt failed: " + e.getMessage());
      return raw;
    }
  }

  private void dumpCookies(HttpServletRequest request, List<String> resume) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        resume.add("REQUEST COOKIE " + cookie.getName() + " = " + cookie.getValue());
      }
    }
  }

  private void dumpRequestParams(HttpServletRequest request, List<String> resume) {
    @SuppressWarnings("unchecked")
    Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      String[] params = request.getParameterValues(paramName);
      for (String param : params) {
        resume.add("REQUEST PARAM " + paramName + " = " + param);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void dumpHeaders(HttpServletRequest request, List<String> resume) {
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      Enumeration<String> headers = request.getHeaders(headerName);
      while (headers.hasMoreElements()) {
        String headerValue = headers.nextElement();
        resume.add("REQUEST HEADER " + headerName + " = " + headerValue);
      }
    }
  }

  private void checkTransferEncodingHeader(RequestFilterResponseAdapter response) {
    if (response.getResponseHeaders() == null) {
      return;
    }

    Object transferEncodingObject = response.getResponseHeaders().get(TRANSFER_ENCODING_HEADER);
    String encoding = null;
    if (null == transferEncodingObject) {
      logger.debugForClass(response.getTargetClassName(), response.getTargetMethodName(),
          "No Transfer-Encoding header found: proceeding");
    } else {
      if (transferEncodingObject instanceof String) {
        encoding = (String) transferEncodingObject;
      } else if (transferEncodingObject instanceof List
          && !((List<?>) transferEncodingObject).isEmpty()
          && ((List<?>) transferEncodingObject).get(0) instanceof String) {
        encoding = (String) ((List<?>) transferEncodingObject).get(0);
      }
    }
    if (null != encoding && encoding.contains("chunked")) {
      logger.warnForClass(response.getTargetClassName(), response.getTargetMethodName(),
          "Chunked transfer encoding detected, removing header");
      response.removeResponseHeader(TRANSFER_ENCODING_HEADER);
    }
  }

  public void postProcessResponse(RequestFilterResponseAdapter response) {

    if (doRemoveChunkedHeader) {
      checkTransferEncodingHeader(response);
    }

    // add ray id header
    String rayId = RequestUtils.getCurrentRequestId().orElse(null);
    if (rayId != null) {
      response.addResponseHeaders(UNIQUE_RAY_ID_HEADER, rayId);
    }

    // dump stats (if enabled)
    if (isStatsEnabled()) {
      dumpRequestStats(response, rayId);
    }

    // log stats (if enabled)
    if (logger.isDebugEnabled()) {
      logRequestStats(response);
    }
  }

  @SuppressWarnings("unchecked")
  private void logRequestStats(RequestFilterResponseAdapter response) {

    String prefix = null;
    List<String> resume = null;
    HttpServletRequest servletRequest = null;
    String line;
    String rayId;

    rayId = RequestUtils.getCurrentRequestId().orElse(null);

    try {
      servletRequest = response.getRequest();
      resume =
          servletRequest != null ? (List<String>) servletRequest.getAttribute(UNIQUE_RAY_RESUME)
              : null;
    } catch (Exception e) {
      logger.warnForClass(response.getTargetClassName(), response.getTargetMethodName(),
          "Could not log request result", e);
      return;
    }

    if (servletRequest == null) {
      return;
    } else {
      if (resume == null) {
        resume = new LinkedList<>();
      }
    }

    prefix = "[RAY " + rayId + "] ";

    line = "\nRESPONSE STATUS: " + response.getResponseStatus();
    resume.add(line);

    logResponseEntity(response, resume);

    String separator = "======================================================";

    StringBuilder builder = new StringBuilder();
    builder.append(separator);
    builder.append("\n");

    for (String resumeLine : resume) {
      builder.append(resumeLine);
      builder.append("\n");
    }

    builder.append(separator);

    StringBuilder outter = new StringBuilder();
    Pattern pattern = Pattern.compile("(?m)(^)");
    Matcher matcher = pattern.matcher(builder.toString());
    outter.append(matcher.replaceAll(prefix));

    logger.debugForClass(response.getTargetClassName(), response.getTargetMethodName(),
        outter.toString());
  }

  @SuppressWarnings("unchecked")
  private void dumpRequestStats(RequestFilterResponseAdapter response, String rayId) {

    HttpServletRequest request = response.getRequest();
    if (request == null) {
      return;
    }

    // add stats for collection
    request.setAttribute(ATTRIBUTES_END_TIME, LocalDateTime.now().toString());

    // dump all stat_ attributes
    Map<String, Object> statsPayload = new HashMap<>();
    statsPayload.put("rayId", rayId);
    statsPayload.put("url", request.getRequestURI());
    statsPayload.put("status", response.getResponseStatus());
    statsPayload.put("method", request.getMethod());
    statsPayload.put("rd", request.getHeader("Date"));

    request.getAttributeNames().asIterator().forEachRemaining(attributeNameRaw -> {
      String attributeName = (String) attributeNameRaw;
      if (attributeName.startsWith(ATTRIBUTES_STATS_PREFIX)) {
        statsPayload.put(attributeName.substring(ATTRIBUTES_STATS_PREFIX.length()),
            request.getAttribute(attributeName));
      }
    });

    // gather builtin
    var moreStats = gatherStatistics();
    if (moreStats != null) {
      moreStats.forEach((k, v) -> {
        statsPayload.put(k, v);
      });
    }

    statsLogger.debug("request_stats", ObjectUtils.toJson(statsPayload));
  }

  private void logResponseEntity(RequestFilterResponseAdapter response, List<String> resume) {
    if (response.getResponseEntity() != null) {
      resume.add("");
      if (response.getResponseHeaders() != null && !response.getResponseHeaders().isEmpty()) {
        for (Entry<String, List<Object>> headerEntry : response.getResponseHeaders().entrySet()) {
          for (Object headerValue : headerEntry.getValue()) {
            resume.add("RESPONSE HEADER " + headerEntry.getKey() + " = " + headerValue);
          }
        }
        resume.add("");
      }

      boolean isJson = false;
      for (Entry<String, List<Object>> entry : response.getResponseHeaders().entrySet()) {
        if (entry.getKey().toUpperCase().contains("Content-Type".toUpperCase())) {
          isJson = !entry.getValue().isEmpty() && entry.getValue().get(0) instanceof String
              && ((String) entry.getValue().get(0)).toUpperCase().contains("/json");
          break;
        }
      }

      resume.add("RESPONSE ENTITY: \n" + response.getResponseEntity().getClass().getName() + "\n");

      String responseRepresentation;
      Object responseEntity = response.getResponseEntity();
      if (isJson) {
        if (responseEntity instanceof String) {
          responseRepresentation = attemptBeautification((String) responseEntity);
        } else if (responseEntity instanceof byte[]) {
          String raw = new String((byte[]) responseEntity, Charset.defaultCharset());
          responseRepresentation = attemptBeautification(raw);
        } else {
          responseRepresentation = ObjectUtils.represent(response.getResponseEntity());
        }
      } else if (responseEntity instanceof byte[]) {
        responseRepresentation = dumpByteArray((byte[]) responseEntity);
      } else {
        responseRepresentation = ObjectUtils.represent(response.getResponseEntity());
      }

      resume.add("RESPONSE CONTENT: \n" + responseRepresentation + "\n");
    }
  }

  private String dumpByteArray(byte[] source) {
    Function<byte[], String> chunkEncoder = raw -> Base64.getEncoder().encodeToString(raw);

    var maxBytes = 5000;
    if (source.length < maxBytes) {
      return chunkEncoder.apply(source);
    } else {
      return chunkEncoder.apply(Arrays.copyOfRange(source, 0, (maxBytes / 2) - 10)) + " [...] "
          + chunkEncoder.apply(
              Arrays.copyOfRange(source, source.length - (maxBytes / 2) + 10, source.length - 1));
    }
  }
}
