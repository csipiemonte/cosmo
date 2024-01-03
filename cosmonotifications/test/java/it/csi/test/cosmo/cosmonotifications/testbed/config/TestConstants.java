/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmonotifications.testbed.config;

import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;

public abstract class TestConstants {

  private TestConstants() {
    // private
  }

  public static final String REQUEST_URL_VALIDA = "/cwnotifications/api/currentUser";

  public static final UserInfoDTO buildUtenteAutenticatoTest = buildUtenteAutenticatoTest();

  private static UserInfoDTO buildUtenteAutenticatoTest () {
    String nome = "DEMO 20";
    String cognome = "CSI PIEMONTE";
    String codiceFiscale = "AAAAAA00B77B000F";

    return UserInfoDTO.builder ()
        .withId(1L)
        .withNome ( nome )
        .withCognome ( cognome )
        .withCodiceFiscale ( codiceFiscale )
        .withAnonimo(false)
        .withEnte(EnteDTO.builder()
            .withId(1L)
            .withNome("Regione Piemonte")
            .withTenantId("r_piemon")
            .build())
        .withProfilo(ProfiloDTO.builder()
            .withId(1L)
            .withCodice("ADMIN")
            .withDescrizione("AMMINISTRATORE")
            .build())
        .build ();
  }
}
