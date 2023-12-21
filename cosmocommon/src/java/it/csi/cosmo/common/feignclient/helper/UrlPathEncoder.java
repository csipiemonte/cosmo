/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.helper;

import java.io.UnsupportedEncodingException;
import org.springframework.web.util.UriUtils;
import it.csi.cosmo.common.util.ExceptionUtils;

/**
 *
 */
public class UrlPathEncoder {

  private UrlPathEncoder() {
    // NOP
  }

  public static String encode(String input) {
    try {
      return UriUtils.encodePathSegment(input, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

}
