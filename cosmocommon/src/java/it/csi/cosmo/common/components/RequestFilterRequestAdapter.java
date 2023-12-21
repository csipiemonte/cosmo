/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import javax.servlet.http.HttpServletRequest;

public interface RequestFilterRequestAdapter {

  HttpServletRequest getRequest();

  String getTargetClassName();

  String getTargetMethodName();

}
