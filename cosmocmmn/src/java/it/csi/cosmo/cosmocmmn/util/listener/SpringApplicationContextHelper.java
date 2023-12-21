/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.util.listener;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContextHelper implements ApplicationContextAware {

  private static ApplicationContext appContext;

  private static Map<String, Object> beanCache = new HashMap<>();

  public static void setGlobalApplicationContext(ApplicationContext applicationContext) {
    SpringApplicationContextHelper.appContext = applicationContext;
  }

  public SpringApplicationContextHelper() {
    // NOP - empty constructor
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    setGlobalApplicationContext(applicationContext);
  }

  public static ApplicationContext getAppContext() {
    return appContext;
  }

  public static Object getBean(String beanName, boolean cacheable) {

    if (cacheable && beanCache.containsKey(beanName)) {
      return beanCache.get(beanName);
    }

    Object bean = null;

    if (appContext.containsBean(beanName)) {
      bean = appContext.getBean(beanName);
    } else {
      bean = appContext.getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
    }

    if (cacheable) {
      beanCache.put(beanName, bean);
    }

    return bean;
  }

  public static Object getBean(String beanName) {
    return getBean(beanName, true);
  }

  /**
   * Ottiene il bean specificato dall'application context di Spring
   *
   * @param class1 la classe del bean da cercare
   * @return il bean trovato
   */
  public static Object getBean(Class<?> class1) {
    String beanName = class1.getSimpleName();
    if (beanCache.containsKey(beanName)) {
      return beanCache.get(beanName);
    }

    Object bean = appContext.containsBean(beanName) ? appContext.getBean(beanName)
        : appContext.getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));

    beanCache.put(beanName, bean);
    return bean;
  }

}
