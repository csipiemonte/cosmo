/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmoauthorization.testbed.config;

import java.util.Arrays;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UseCaseDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;

public abstract class TestConstants {

  private static final String CF_DEMO_20 = "AAAAAA00B77B000F";

  public static final String REQUEST_URL_VALIDA = "/cosmoauthorization/api/currentUser";

  public static UserInfoDTO buildUtenteAutenticato() {

    return buildUtenteAutenticato(CF_DEMO_20);
  }

  public static UserInfoDTO buildEnteUtenteAutenticato() {

    return buildUtenteEnteAutenticato(CF_DEMO_20);
  }

  public static UserInfoDTO buildUtenteAutenticato(String cf) {

    return UserInfoDTO.builder().withId(1234L).withIdentita(null).withNome("DEMO 20")
        .withCognome("CSI PIEMONTE").withCodiceFiscale(cf).withAnonimo(false).build();
  }

  public static UserInfoDTO buildUtenteEnteAutenticato(String cf) {

    return UserInfoDTO.builder().withId(1234L).withIdentita(null).withNome("DEMO 20")
        .withCognome("CSI PIEMONTE").withCodiceFiscale(cf).withAnonimo(false)
        .withEnte(buildEnteAutenticato()).build();
  }


  private static EnteDTO buildEnteAutenticato() {
    return EnteDTO.builder().withId(1L).withNome("Regione Piemonte").build();
  }
  
  public static UserInfoDTO buildUtenteAutenticatoId1() {

    return UserInfoDTO.builder().withId(1L).withIdentita(null).withNome("DEMO 20")
        .withCognome("CSI PIEMONTE").withCodiceFiscale(CF_DEMO_20).withAnonimo(false).build();
  }

  public static UserInfoDTO buildUtenteAmministratoreSistema() {
    //@formatter:off
    return UserInfoDTO.builder()
        .withId(1234L)
        .withIdentita(null)
        .withNome("DEMO 20")
        .withCognome("CSI PIEMONTE")
        .withCodiceFiscale(CF_DEMO_20)
        .withAnonimo(false)
        .withEnte(EnteDTO.builder()
            .withId(1L)
            .withNome("Regione Piemonte")
            .withTenantId("r_piemon")
            .build())
        .withProfilo(ProfiloDTO.builder()
            .withCodice("ADMIN_ENTE")
            .withDescrizione("Amministratore ente")
            .withUseCases(Arrays.asList(UseCaseDTO.builder().withCodice("ADMIN_COSMO").build()))
            .build())
        .build();
    //@formatter:on
  }

  private TestConstants() {
    // private
  }
}
