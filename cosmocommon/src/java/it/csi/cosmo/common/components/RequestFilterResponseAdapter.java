/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import java.util.List;
import java.util.Map;

public interface RequestFilterResponseAdapter extends RequestFilterRequestAdapter {

  Object getResponseEntity();

  Integer getResponseStatus();

  Map<String, List<Object>> getResponseHeaders();

  void addResponseHeaders(String key, String value);

  void removeResponseHeader(String key);
}
