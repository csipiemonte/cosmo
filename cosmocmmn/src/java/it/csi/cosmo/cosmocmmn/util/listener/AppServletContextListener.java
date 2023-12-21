/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.util.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppServletContextListener implements ServletContextListener {

  private static ServletContext sc;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    /* Sets the context in a static variable */
    setServletContext(sce.getServletContext());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // NOP
  }

  public static void setServletContext(ServletContext servletContext) {
    AppServletContextListener.sc = servletContext;
  }

  public static ServletContext getServletContext() {
    return sc;
  }
}
