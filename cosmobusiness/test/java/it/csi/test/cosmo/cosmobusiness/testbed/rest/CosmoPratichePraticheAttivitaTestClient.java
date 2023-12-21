/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPratichePraticheAttivitaFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

/**
 *
 */

@Component
public class CosmoPratichePraticheAttivitaTestClient extends ParentTestClient
    implements CosmoPratichePraticheAttivitaFeignClient {

  @Override
  public Attivita getPraticheAttivitaIdAttivita(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public Pratica postPraticheAttivita(Pratica arg0) {
    notMocked();
    return null;
  }

  @Override
  public Pratica putPraticheAttivita(Pratica arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public Boolean putPraticheAttivitaIdAttivita(String arg0) { // NOSONAR
    notMocked();
    return null; // NOSONAR
  }
}
