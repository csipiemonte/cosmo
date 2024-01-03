/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmoecm.testbed.config;

import java.util.Arrays;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;


public abstract class TestConstants {

  private TestConstants() {
    // private constructor
  }

  public static final String REQUEST_URL_VALIDA = "/cosmoecm/api/currentUser";

  public static UserInfoDTO buildUtenteAutenticato() {

    return UserInfoDTO.builder().withIdentita(null).withNome("Utente").withCognome("Di Prova")
        .withCodiceFiscale("AAAAAA00A00A000F").withAnonimo(true).withId(1L)
        .withEnte(EnteDTO.builder().withNome("Ente").withId(1L).build()).build();
  }

  public static ClientInfoDTO buildFruitoreAutenticato() {

    return ClientInfoDTO.builder().withCodice("FruitoreTest1")
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();
  }

}
