/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.feignclient.exception.FeignClientRegistrationException;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.util.RestTemplateUtils;


/**
 *
 */
public abstract class FeignClientProvider implements BeanDefinitionRegistryPostProcessor {

  private Logger logger;

  public abstract <T> T getBean(Class<T> requiredBean);

  public abstract String resolveConfiguration(String key);

  public abstract String getNamespaceToScan();

  private String loggingPrefix;

  public FeignClientProvider(String loggingPrefix) {
    super();
    this.loggingPrefix = loggingPrefix;
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".feignclient.FeignClientProvider");
  }

  public static RestTemplate getBasicFeignClientRestTemplate() {
    //@formatter:off
    return RestTemplateUtils.builder()
        .withAllowConnectionReuse(false)
        .withReadTimeout(30000)
        .build();
    //@formatter:on
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    try {
      loadRegisteredFeignClients(this.getClass().getClassLoader(), beanFactory,
          getNamespaceToScan());
    } catch (IOException e) {
      throw new FeignClientRegistrationException(e);
    }

  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanRegistry) {
    // NOP
    beanRegistry.toString();
  }

  private void loadRegisteredFeignClients(ClassLoader cl,
      ConfigurableListableBeanFactory beanFactory, String completePackageName) throws IOException {
    String packagePath = completePackageName.replace(".", "/");

    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] resources;
    Set<String> registeredClasses = new HashSet<>();

    // scansiono il percorso specificato, ricercando tutte le classi compilate che vi appartengono
    resources = resolver.getResources("classpath*:" + packagePath + "/**/*.class");
    for (Resource resource : resources) {
      String resourceURI = resource.getURI().toString();

      // costruisco il nome della classe Java a partire dal percorso relativo all'interno del
      // package specificato
      // in questo modo posso supportare anche le classi nei vari sub-package
      String subclassPath =
          resourceURI.substring(resourceURI.indexOf(packagePath) + packagePath.length() + 1);
      subclassPath = subclassPath.substring(0, subclassPath.length() - 6);
      String className = completePackageName + "." + subclassPath.replace("/", ".");

      try {
        Class<?> foundClass = Class.forName(className);

        if (foundClass.isAnnotationPresent(FeignClient.class)) {

          if (!registeredClasses.contains(foundClass.getName())) {
            registeredClasses.add(foundClass.getName());

            FeignClientContext context = new FeignClientContext();
            context.setProvider(this);
            context.setRestTemplate(getBasicFeignClientRestTemplate());

            Object bean = new FeignClientFactory(loggingPrefix).buildClient(foundClass, context);

            beanFactory.registerSingleton(foundClass.getSimpleName(), bean);

          } else {
            logger.debug("skipping duplicated class scanned :" + foundClass.getName());
          }
        }

      } catch (Exception e) {
        logger.error("error registering FeignClient " + className + ": class is not available.", e);
      }
    }
  }
}
