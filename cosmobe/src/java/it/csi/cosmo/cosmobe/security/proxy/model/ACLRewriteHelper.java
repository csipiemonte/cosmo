/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security.proxy.model;

import java.util.Map;
import java.util.Map.Entry;
import org.springframework.util.AntPathMatcher;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

/**
 *
 */

public abstract class ACLRewriteHelper {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "ACLRewriteHelper");

  private ACLRewriteHelper() {
    // NOP
  }

  public static String rewrite(String patternInput, String patternOutput, String input) {
    final var method = "rewrite";
    Map<String, String> uriParamsMatches;

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(method, "rewriting url [{}] with pattern spec [{}] -> [{}]", input, patternInput,
          patternOutput);
    }

    if (patternInput.contains("{")) {
      var matcher = new AntPathMatcher();

      uriParamsMatches = matcher.extractUriTemplateVariables(patternInput, input);

      if (LOGGER.isDebugEnabled()) {
        for (Entry<String, String> entry : uriParamsMatches.entrySet()) {
          LOGGER.debug(method, "found param for template variable [{}]=[{}]", entry.getKey(),
              entry.getValue());
        }
      }

    } else {
      uriParamsMatches = null;
    }

    var output = patternOutput;

    if (output.contains("*")) {

      var matcher = new AntPathMatcher();

      var pathMatchResult = matcher.extractPathWithinPattern(patternInput, input);

      int firstPositionInOutputPattern = output.indexOf('*');
      output = output.substring(0, firstPositionInOutputPattern) + pathMatchResult;
    }

    if (uriParamsMatches != null) {
      for (Entry<String, String> entry : uriParamsMatches.entrySet()) {
        output =
            output.replaceAll("\\{\\s*" + entry.getKey().strip() + "\\s*\\}", entry.getValue());
      }
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(method, "rewrote url [{}]->[{}]", input, output);
    }

    return output;
  }

}
