/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.exception;


/**
 * Eccezione lanciata quando la property remotesign.dosign.testmode.enabled e' valorizzata a true all'interno dei file properties o sulla base dati. Da gestire
 * opportunamente
 */

public class DosignTestModeEnabledException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 3291230257224980237L;

  public DosignTestModeEnabledException () {
    super ();
  }

  public DosignTestModeEnabledException ( String arg0, Throwable arg1, boolean arg2, boolean arg3 ) {
    super ( arg0, arg1, arg2, arg3 );
  }

  public DosignTestModeEnabledException ( String arg0, Throwable arg1 ) {
    super ( arg0, arg1 );
  }

  public DosignTestModeEnabledException ( String arg0 ) {
    super ( arg0 );
  }

  public DosignTestModeEnabledException ( Throwable arg0 ) {
    super ( arg0 );
  }

}
