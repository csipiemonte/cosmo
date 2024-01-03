/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperRuntimeException () {

    }

    public JasperRuntimeException ( String message ) {
        super ( message );

    }

    public JasperRuntimeException ( Throwable cause ) {
        super ( cause );

    }

    public JasperRuntimeException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperRuntimeException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
