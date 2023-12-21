/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/*
 * Copyright 2001-2019 CSI Piemonte. All Rights Reserved.
 *
 * This software is proprietary information of CSI Piemonte.
 * Use is subject to license terms.
 *
 */

package it.csi.cosmo.cosmoauthorization.security;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public enum Scopes {
        MONITORING ( "Monitoraggio dello stato applicativo" ),
        DEFAULT_CLIENT ( "Default client" );

    private String descrizione;

    Scopes ( String descrizione ) {
        this.descrizione = descrizione;
    }

    public String getDescrizione () {
        return descrizione;
    }

}
