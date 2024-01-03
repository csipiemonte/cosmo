/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperException () {

    }

    public JasperException ( String message ) {
        super ( message );

    }

    public JasperException ( Throwable cause ) {
        super ( cause );

    }

    public JasperException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
