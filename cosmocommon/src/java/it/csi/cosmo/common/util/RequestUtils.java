/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;

/**
 *
 */

public abstract class RequestUtils {

  private static CosmoLogger logger = new CosmoLogger("cosmo.common", "RequestUtils");

  private static final String HEADER_USER_AGENT = "user-agent";
  private static final String HEADER_FORWARDED_FOR = "x-forwarded-for";
  private static final String HEADER_REQUEST_ID = "x-request-id";
  private static final String UNIQUE_RAY_ID = "rayId";

  private RequestUtils() {
    // NOP
  }

  public static synchronized Optional<String> getCurrentRequestId() {

    return getCurrentRequest().map(request -> {
      String existing = (String) request.getAttribute(UNIQUE_RAY_ID);
      if (existing == null) {

        existing = streamHeaders(request, HEADER_REQUEST_ID, ",").findFirst()
            .orElseGet(() -> UUID.randomUUID().toString());

        request.setAttribute(UNIQUE_RAY_ID, existing);
      }

      return existing;
    });
  }

  public static Optional<HttpServletRequest> getCurrentRequest() {
    HttpServletRequest hreq = null;

    RequestAttributes cra;
    try {
      cra = RequestContextHolder.currentRequestAttributes();
    } catch (Throwable e) {
      cra = null;
    }

    if (cra instanceof ServletRequestAttributes) {
      ServletRequestAttributes scra = (ServletRequestAttributes) cra;
      hreq = scra.getRequest();
    }

    return Optional.ofNullable(hreq);
  }

  public static Stream<String> streamHeaders(HttpServletRequest request, String headerName) {
    return streamHeaders(request, headerName, null);
  }

  public static Stream<String> streamHeaders(HttpServletRequest request, String headerName,
      String separator) {

    //@formatter:off
    Stream<String> intermediateStream = streamHeaderNames(request)
        .filter(name -> name.strip().equalsIgnoreCase(headerName.strip()))
        .map(request::getHeader)
        .filter(StringUtils::isNotBlank)
        .map(String::strip);
    //@formatter:on

    if (separator != null && !separator.isBlank()) {
      //@formatter:off
      return intermediateStream
          .flatMap(entry -> Arrays.asList(entry.split(separator)).stream())
          .filter(StringUtils::isNotBlank)
          .map(String::strip);
      //@formatter:on
    }

    return intermediateStream;
  }

  /**
   * @deprecated deprecato in favore di streamHeaders. Sostituire extractHeader(request, name) con
   *             streamHeaders(request, name).findFirst()
   */
  @Deprecated(forRemoval = true)
  public static List<String> extractHeader(HttpServletRequest request, String headerName) {
    return extractHeader(request, headerName, null);
  }

  /**
   * @deprecated deprecato in favore di streamHeaders. Sostituire extractHeader(request, name,
   *             separator) con streamHeaders(request, name, separator).findFirst()
   */
  @Deprecated(forRemoval = true)
  public static List<String> extractHeader(HttpServletRequest request, String headerName,
      String separator) {

    return streamHeaders(request, headerName, separator)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  public static Optional<String> extractIp(HttpServletRequest request) {
    if (request == null) {
      return Optional.empty();
    }

    //@formatter:off
    return
        streamHeaders(request, CosmoAuthenticationConfig.TRANSMISSION_HEADER_ORIGINAL_IP, ",")
        .findFirst()
        .or(() -> streamHeaders(request, HEADER_FORWARDED_FOR, ",").findFirst())
        .or(() -> Optional.ofNullable(request.getRemoteAddr()))
        .filter(ValidationUtils::isValidIpAddress);
    //@formatter:on
  }

  public static Optional<String> extractUserAgent(HttpServletRequest request) {
    return streamHeaders(request, HEADER_USER_AGENT).findFirst();
  }

  public static Optional<Long> extractHeaderId(HttpServletRequest request, String header) {
    return streamHeaders(request, header, ",")
        .findFirst()
        .map(raw -> Long.valueOf(raw.trim()));
  }

  public static Optional<Long> extractRequestParamId(HttpServletRequest request, String header) {
    String raw = request.getParameter(header);
    if (StringUtils.isBlank(raw)) {
      return Optional.empty();
    } else {
      return Optional.of(Long.valueOf(raw.trim()));
    }
  }

  // private helpers

  @SuppressWarnings("unchecked")
  private static Stream<String> streamHeaderNames(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames()).stream().map(h -> (String) h);
  }

  public static String formatDateHeader(OffsetDateTime source) {
    // var formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
    // .withZone(ZoneId.of("GMT"));
    var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return formatter.format(source);
  }
}
