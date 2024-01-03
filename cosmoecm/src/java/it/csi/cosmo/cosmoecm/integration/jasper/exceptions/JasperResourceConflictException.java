/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperResourceConflictException extends JasperRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperResourceConflictException () {

    }

    public JasperResourceConflictException ( String message ) {
        super ( message );

    }

    public JasperResourceConflictException ( Throwable cause ) {
        super ( cause );

    }

    public JasperResourceConflictException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperResourceConflictException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
