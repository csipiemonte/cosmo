/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.common.infinispan.exceptions;


/**
 *
 */

public class InfinispanResourceBusyException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public InfinispanResourceBusyException () {
  }

  public InfinispanResourceBusyException ( String message ) {
    super ( message );
  }

  public InfinispanResourceBusyException ( Throwable cause ) {
    super ( cause );
  }

  public InfinispanResourceBusyException ( String message, Throwable cause ) {
    super ( message, cause );
  }

  public InfinispanResourceBusyException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
    super ( message, cause, enableSuppression, writableStackTrace );
  }

}
