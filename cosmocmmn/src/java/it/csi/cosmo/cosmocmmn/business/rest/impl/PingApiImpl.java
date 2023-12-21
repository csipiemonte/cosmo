/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingApiImpl {

  @GetMapping("/ping")
  public Map<String, Object> ping() {

    Map<String, Object> result = new HashMap<>();
    result.put("pong", "pong");
    return result;
  }

}
