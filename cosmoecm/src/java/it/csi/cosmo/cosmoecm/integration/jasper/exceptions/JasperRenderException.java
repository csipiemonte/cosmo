/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.exceptions;


/**
 *
 */

public class JasperRenderException extends JasperRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JasperRenderException () {

    }

    public JasperRenderException ( String message ) {
        super ( message );

    }

    public JasperRenderException ( Throwable cause ) {
        super ( cause );

    }

    public JasperRenderException ( String message, Throwable cause ) {
        super ( message, cause );

    }

    public JasperRenderException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super ( message, cause, enableSuppression, writableStackTrace );

    }

}
