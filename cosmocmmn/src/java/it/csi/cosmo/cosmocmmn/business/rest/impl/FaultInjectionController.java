/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmocmmn.business.service.FaultInjectionService;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;

@RestController
@RequestMapping("/cosmo/fault-injection")
public class FaultInjectionController {

  @Autowired(required = false)
  FaultInjectionService faultInjectionService;

  @Autowired
  ConfigurazioneService configurazioneService;

  private void assertEnabled() {
    if (!(Boolean.TRUE.equals(this.configurazioneService
        .getConfig(ParametriApplicativo.ENABLE_FAULT_INJECTION).asBoolean()))) {

      throw new ForbiddenException("fault injection is not allowed in this environment");
    }
  }

  @RequestMapping("/inject/{hook}")
  public Map<String, Object> inject(@PathVariable String hook) {

    assertEnabled();
    faultInjectionService.injectFault(hook, () -> new InternalServerException("injected fault"));

    Map<String, Object> out = new HashMap<>();
    out.put("result", "injected");
    return out;
  }

  @RequestMapping("/clear/{hook}")
  public Map<String, Object> clearOne(@PathVariable String hook) {

    assertEnabled();
    faultInjectionService.clearFault(hook);
    
    Map<String, Object> out = new HashMap<>();
    out.put("result", "cleared");
    return out;
  }

  @RequestMapping("/clear")
  public Map<String, Object> clearAll() {

    assertEnabled();
    faultInjectionService.clearAllFaults();

    Map<String, Object> out = new HashMap<>();
    out.put("result", "all_cleared");
    return out;
  }

  @RequestMapping("/")
  public Map<String, Object> listAll() {

    assertEnabled();

    Map<String, Object> out = new HashMap<>();
    out.put("result", faultInjectionService.listAllFaults());
    return out;
  }

}
