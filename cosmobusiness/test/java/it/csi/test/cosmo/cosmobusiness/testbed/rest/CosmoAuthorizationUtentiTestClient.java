/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoAuthorizationUtentiFeignClient;


/**
 *
 */

@Component
public class CosmoAuthorizationUtentiTestClient extends ParentTestClient
implements CosmoAuthorizationUtentiFeignClient {

  @Override
  public UtenteResponse deleteUtentiId(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public UtentiResponse getUtenti(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public UtenteResponse getUtentiCodiceFiscale(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public UtentiResponse getUtentiEnte(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public UtenteResponse getUtentiId(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public UtenteResponse getUtentiUtenteCorrente() {
    notMocked();
    return null;
  }

  @Override
  public UtenteCampiTecnici getUtentiEntiValiditaId(String arg0, String arg1) {
    notMocked();
    return null;
  }

  @Override
  public UtenteResponse postUtenti(UtenteCampiTecnici arg0) {
    notMocked();
    return null;
  }

  @Override
  public UtenteResponse putUtenti(UtenteCampiTecnici arg0) {
    notMocked();
    return null;
  }
}
