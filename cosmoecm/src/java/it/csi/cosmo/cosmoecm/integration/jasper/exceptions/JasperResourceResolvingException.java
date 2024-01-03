/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperResourceResolvingException extends JasperRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperResourceResolvingException () {

    }

    public JasperResourceResolvingException ( String message ) {
        super ( message );

    }

    public JasperResourceResolvingException ( Throwable cause ) {
        super ( cause );

    }

    public JasperResourceResolvingException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperResourceResolvingException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
