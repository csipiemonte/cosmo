/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmobusiness.testbed.config;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;

public abstract class TestConstants {

  private TestConstants() {
    // private
  }

  public static final String REQUEST_URL_VALIDA = "/cosmobusiness/api/currentUser";


  public static UserInfoDTO buildUtenteAutenticato() {

    return UserInfoDTO.builder()
        .withIdentita ( null )
        .withNome("Utente").withCognome("Di Prova").withCodiceFiscale("AAAAAA00A00A000F")
        .withAnonimo ( true )
        .build ();
  }
  
  public static UserInfoDTO buildUtenteAutenticatoTest () {
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

  public static ClientInfoDTO buildClientAutenticato() {

    return ClientInfoDTO.builder()
        .withCodice("STARDAS")
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo ( false )
        .build ();
  }
  
  public static ClientInfoDTO buildClientAutenticatoTest() {

    return ClientInfoDTO.builder()
        .withCodice("FruitoreTest1")
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo ( false )
        .build ();
  }
  
  public static Optional<HttpServletRequest> buildResourceLockHeader() {
    
    return null;
    
  }
}
