/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import it.csi.cosmo.cosmoauthorization.util.listener.SpringApplicationContextHelper;



@ApplicationPath("api")
public class CosmoAuthorizationBackOfficeRestApplication extends Application {

  private Set<Object> singletons = new HashSet<>();

  private Set<Class<?>> empty = new HashSet<>();

  public CosmoAuthorizationBackOfficeRestApplication()
      throws InvocationTargetException, NoSuchMethodException, IOException {

    /*
     * invece di aggiungere manualmente le risorse di restEasy, eseguo un 'autoscan' simile a quello
     * eseguito da spring
     */
    SpringApplicationContextHelper.caricaRisorseRestEasyInPackage(this.getClass().getClassLoader(),
        singletons, "it.csi.cosmo.cosmoauthorization");

  }

  @Override
  public Set<Class<?>> getClasses() {
    return empty;
  }

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }

}

