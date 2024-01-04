/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmosoap.testbed.config;

import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;


public abstract class TestConstants {

  private TestConstants() {
    // private constructor
  }

  public static final String REQUEST_URL_VALIDA = "/cosmosoap/api/currentUser";

  public static UserInfoDTO buildUtenteAutenticato() {

    return UserInfoDTO.builder().withIdentita(null).withNome("Utente").withCognome("Di Prova")
        .withCodiceFiscale("AAAAAA00A00A000F").withAnonimo(true)
        .withEnte(EnteDTO.builder().withNome("Ente").withId(1L).build()).build();
  }

}
