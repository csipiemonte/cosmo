/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.helper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;

/**
 *
 */

public class FeignClientHeaderHelper {

  private Logger logger;

  public static final String HEADER_FORWARD_MARKER = "-Fwd-";

  static final Pattern HEADER_FINDER_PATTERN = Pattern.compile(
      "(.+)" + HEADER_FORWARD_MARKER.replace("-", "\\-") + "([0-9]+)", Pattern.CASE_INSENSITIVE);

  public FeignClientHeaderHelper(String loggingPrefix) {
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".feignclient.FeignClientHeaderHelper");
  }

  public List<Entry<String, String>> buildHeadersForResponseForward(
      Collection<Entry<String, List<String>>> headers) {

    if (logger.isTraceEnabled()) {
      logger.trace("parsing response headers for application headers to forward");
    }

    List<Entry<String, String>> output = new ArrayList<>();

    for (Entry<String, List<String>> header : headers) {
      buildHeaderForResponseForward(header, output);
    }

    return output;
  }

  private void buildHeaderForResponseForward(Entry<String, List<String>> header,
      List<Entry<String, String>> output) {
    String upperName = header.getKey().toUpperCase();
    if (upperName.startsWith(Constants.HEADERS_PREFIX.toUpperCase())) {

      if (logger.isTraceEnabled()) {
        logger.trace("found response header [" + header.getKey() + "] to forward");
      }

      String newName = buildForwardHeaderName(header);

      for (String value : header.getValue()) {

        if (logger.isTraceEnabled()) {
          logger.trace(
              "re-forwarding response header [" + header.getKey() + "] as [" + newName + "]");
        }

        output.add(new AbstractMap.SimpleEntry<String, String>(newName, value));
      }
    }
  }

  private String buildForwardHeaderName(Entry<String, List<String>> header) {
    String newName = header.getKey();
    String upperName = header.getKey().toUpperCase();

    if (upperName.contains(HEADER_FORWARD_MARKER.toUpperCase())) {
      Matcher m = HEADER_FINDER_PATTERN.matcher(header.getKey());
      if (m.find()) {
        newName = Integer.valueOf(m.group(1)) + 1 + HEADER_FORWARD_MARKER
            + (Integer.valueOf(m.group(2)) + 1);
      }
    } else {
      newName = header.getKey() + HEADER_FORWARD_MARKER + "0";
    }
    return newName;
  }
}
