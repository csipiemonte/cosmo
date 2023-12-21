/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.apimgr.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class ApiMgrConfig {

  private ApiMgrConfig() {
    // private
  }

  public static final String JWT_ASSERTION_NAMESPACE = "http://wso2.org";

  public static final String JWT_ASSERTION_CLAIMS_NAMESPACE = JWT_ASSERTION_NAMESPACE + "/claims";

  public static final Charset JWT_ASSERTION_ENCODING = StandardCharsets.UTF_8;

  public static final String JWT_ASSERTION_HEADER_NAME = "X-JWT-Assertion";
}
