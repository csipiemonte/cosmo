/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperModelValidationException extends JasperRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperModelValidationException () {

    }

    public JasperModelValidationException ( String message ) {
        super ( message );

    }

    public JasperModelValidationException ( Throwable cause ) {
        super ( cause );

    }

    public JasperModelValidationException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperModelValidationException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
