/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.model;



/**
 *
 */

public interface FeignClientContextConfigurator {

  void configure(FeignClientContext context);
}
