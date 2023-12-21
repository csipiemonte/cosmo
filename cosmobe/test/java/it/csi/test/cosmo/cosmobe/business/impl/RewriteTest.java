/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobe.business.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobe.security.proxy.model.ACLRewriteHelper;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;


public class RewriteTest {

  CosmoLogger logger = LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "RewriteTest");

  private static final String CONTEXT = "http://dev-host.it/cosmobe";

  private String rewrite(String patternInput, String patternOutput, String input) {
    return ACLRewriteHelper.rewrite(patternInput, patternOutput, input);
  }

  @Test
  public void testRewrite() {
    final var method = "testRewrite";

    String[][] test = new String[][] {
      //@formatter:off
      {"/api/v1/status", "/api/v1/status", "/api/v1/status", "/api/v1/status"}, // NOSONAR
      {"/api/{ version }/*/status", "/api/proxy/*", "/api/v2/ecm/status", "/api/proxy/ecm/status"},
      {"/api/{ version }/document/**", "/api/proxy/ecm/document/{version}/*", "/api/v2/document/12/metadata", "/api/proxy/ecm/document/v2/12/metadata"},
      //@formatter:on
    };

    for (int i = 0; i < test.length; i ++) {
      var url = test[i][2];

      var patternInput = test[i][0];

      var patternOutput = test[i][1];

      var result = rewrite(patternInput, patternOutput, url);

      logger.info(method, "{} -> {}", patternInput, patternOutput);
      logger.info(method, "{} -> {}", url, result);

      assertEquals(test[i][3], result);
    }
  }

  @Test
  public void testExtractPathWithinPattern() {
    final var method = "testExtractPathWithinPattern";

    var matcher = new AntPathMatcher();

    var url = CONTEXT + "/api/proxy/v1/basic/mode/document/12/metadata/asd";

    var pattern = CONTEXT + "/api/proxy/*/document/**/metadata/asd";

    var result = matcher.extractPathWithinPattern(pattern, url);

    logger.info(method, result);

    assertEquals("v1/basic/mode/document/12/metadata/asd", result);
  }

  @Test
  public void testGlobPattern() {
    final var method = "testGlobPattern";

    var matcher = new AntPathMatcher();

    var url = CONTEXT + "/api/proxy/v1/basic/mode/document/12/metadata";

    var pattern = CONTEXT + "/api/proxy/**/document/{id}/metadata";

    var params = matcher.extractUriTemplateVariables(pattern, url);

    logger.info(method, ObjectUtils.toJson(params));

    assertEquals("12", params.get("id"));
  }

  @Test
  public void testPatternExtraction() {
    final var method = "testPatternExtraction";

    var matcher = new AntPathMatcher();

    var url = CONTEXT + "/api/proxy/v1/document/12/metadata";

    var pattern = CONTEXT + "/api/proxy/v{version}/document/{id}/metadata";

    var params = matcher.extractUriTemplateVariables(pattern, url);

    logger.info(method, ObjectUtils.toJson(params));

    assertEquals("12", params.get("id"));
    assertEquals("1", params.get("version"));
  }

}
