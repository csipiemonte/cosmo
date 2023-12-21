/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;


public abstract class ParentApiImpl extends SpringBeanAutowiringSupport {

  @Autowired
  public ConfigurazioneService configurazioneService;

  protected ConfigurazioneService getConfigurazioneService() {
    return this.configurazioneService;
  }
}
