/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.util.listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmonotifications.business.rest.impl.ParentApiImpl;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;

public class SpringApplicationContextHelper implements ApplicationContextAware {

  private static final String ERRORE_NELLA_REGISTRAZIONE_DELLA_RISORSA =
      "errore nella registrazione della risorsa ";

  private static final CosmoLogger LOGGER = LoggerFactory.getLogger(
      LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY, "SpringApplicationContextHelper");

  private static ApplicationContext appContext;

  private static Map<String, Object> beanCache = new HashMap<>();

  private static Map<Class<?>, Class<? extends ParentApiImpl>> implementations = new HashMap<>();

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

  public static Class<? extends ParentApiImpl> getImplementationClass(Class<?> interfaceClass) {
    if (interfaceClass == null) {
      return null;
    }

    return implementations.getOrDefault(interfaceClass, null);
  }

  public static Method getImplementationMethod(Class<?> interfaceClass, Method interfaceMethod) {
    if (interfaceMethod == null) {
      return null;
    }

    Class<? extends ParentApiImpl> cl = getImplementationClass(interfaceClass);
    if (cl == null) {
      return null;
    }

    try {
      return cl.getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
    } catch (Exception e) {
      throw new RuntimeException("Error resolving implementation for method", e); // NOSONAR
    }
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

  /**
   * questo metodo agisce come proxy per 'caricaRisorseRestEasyInPackage'. La differenza e' che,
   * essendo richiamabile passando una classe come argomento, il metodo continuera' a funzionare
   * anche in caso di spostamento delle classi, cambio di nome dei package o altri refactoring. Se
   * passassi invece 'nome.del.package' come stringa, in tali casi il metodo rischierebbe di fallire
   * in modo silenzioso, senza dare adeguata evidenza dell'errore.
   *
   * @param cl il classLoader da utilizzare per la scansione
   * @param singletons la collection in cui caricare gli oggetti
   * @param sampleClass una classe all'interno del package che si vuole sottoporre a scansione
   * @see caricaRisorseRestEasyInPackage
   * @throws IOException in caso di problemi con l'accesso al package specificato
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   */
  public static void caricaRisorseRestEasyNellaStessaCartellaDi(ClassLoader cl,
      Collection<? super Object> singletons, Class<?> sampleClass)
          throws IOException, InvocationTargetException, NoSuchMethodException {
    caricaRisorseRestEasyInPackage(cl, singletons, sampleClass.getPackage().getName());
  }

  /**
   * Eseguo una scansione del package desiderato, utilizzando lo stesso motore che Spring utilizza
   * per l'autoscan. Estraggo da tale package una istanza di tutte le classi caricabili
   * automaticamente; in questo modo posso evitare di dover referenziare manualmente tutte le
   * risorse, operazione ripetitiva e prona ad errori.
   *
   * @param cl il classLoader da utilizzare per la scansione
   * @param singletons la collection in cui caricare gli oggetti
   * @param completePackageName il nome completo del package da sottoporre a scansione
   * @throws IOException in caso di problemi con l'accesso al package specificato
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   */
  @SuppressWarnings("unchecked")
  public static void caricaRisorseRestEasyInPackage(ClassLoader cl,
      Collection<? super Object> singletons, String completePackageName)
          throws IOException, InvocationTargetException, NoSuchMethodException {
    String methodName = "caricaRisorseRestEasyInPackage";
    String packagePath = completePackageName.replace(".", "/");

    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] resources;

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
        // istanzio la classe a partire dal suo nome completo.
        // ATTENZIONE: la classe deve avere un costruttore di default
        Class<?> foundClass = Class.forName(className);

        if (!foundClass.equals(ParentApiImpl.class)
            && ParentApiImpl.class.isAssignableFrom(foundClass)) {
          Object instance = foundClass.getDeclaredConstructor().newInstance();
          LOGGER.info(methodName, "registro la risorsa " + instance.getClass().getSimpleName());
          singletons.add(instance);

          // register implementation for all interfaces
          for (Class<?> i : foundClass.getInterfaces()) {
            implementations.put(i, (Class<? extends ParentApiImpl>) foundClass);
          }
        }

      } catch (InstantiationException e) {
        LOGGER.error(methodName, ERRORE_NELLA_REGISTRAZIONE_DELLA_RISORSA + className + ": "
            + "errore nella creazione dell'istanza. Verificare che la classe " + className
            + "disponga di un costruttore di default e che non sia una classe astratta oppure un'interfaccia.",
            e);

      } catch (ClassNotFoundException e) {
        LOGGER.error(methodName,
            ERRORE_NELLA_REGISTRAZIONE_DELLA_RISORSA + className + ": "
                + "la classe non e' disponibile. Verificare la coerenza della classe " + className
                + " col package " + completePackageName,
                e);

      } catch (IllegalAccessException e) {
        LOGGER.error(methodName, ERRORE_NELLA_REGISTRAZIONE_DELLA_RISORSA + className + ": "
            + "errore nell'accesso ai metodi di costruzione. Verificare che la classe " + className
            + " disponga di un costruttore di default e che tale costruttore abbia visibilita' pubblica.",
            e);
      }
    }
  }

}
