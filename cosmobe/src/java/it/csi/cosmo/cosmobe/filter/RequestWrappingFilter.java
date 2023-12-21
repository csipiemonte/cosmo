/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.filter;

import it.csi.cosmo.common.components.CosmoRequestWrappingFilter;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;

public class RequestWrappingFilter extends CosmoRequestWrappingFilter {

  public RequestWrappingFilter() {
    super(LogCategory.REST_LOG_CATEGORY.getCategory());
  }

}
