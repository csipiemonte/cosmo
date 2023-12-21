/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import it.csi.cosmo.cosmocmmn.business.service.UserService;

@RestController
public class CosmoFlowableInit implements ApplicationContextAware {

  ApplicationContext ctx;

  @Autowired
  UserService us;

  @GetMapping("/test")
  public Map<String, Object> test(Authentication authentication) {

    Map<String, Object> result = new HashMap<>();
    result.put("test", "test");

    result.put("authentication", authentication);


    result.put("current_user", us.getUtenteCorrente());
    // @formatter:off

    String[] names = ctx.getBeanDefinitionNames();
    List<String> packages = Arrays.asList("org.flowable.", "it.csi.", "org.springframework.");
    packages
    .stream()
    .forEach(s -> result.put(s + "_context", Arrays
        .asList(names)
        .stream()
        .filter(e->beanClass(e).startsWith(s))
        .collect(Collectors.toMap(e -> e, this :: beanClass)))
        );

    result.put("_context", Arrays
        .asList(names)
        .stream()
        .filter(e-> packages.stream().filter(p->beanClass(e).startsWith(p)).findAny().isEmpty())
        .collect(Collectors.toMap(e -> e, this :: beanClass)));


    // @formatter:on
    return result;
  } 

  private String beanClass(String e) {
    return ctx.getBean(e).getClass().getName();
  }

  @Override
  public void setApplicationContext(ApplicationContext ctx) {
    this.ctx = ctx;
  }
}
