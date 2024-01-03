/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperDataValidationException extends JasperRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperDataValidationException () {

    }

    public JasperDataValidationException ( String message ) {
        super ( message );

    }

    public JasperDataValidationException ( Throwable cause ) {
        super ( cause );

    }

    public JasperDataValidationException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperDataValidationException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
