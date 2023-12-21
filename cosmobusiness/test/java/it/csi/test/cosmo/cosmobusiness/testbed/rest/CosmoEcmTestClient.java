/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoEcmFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.Cartella;

/**
 *
 */


@Component
public class CosmoEcmTestClient extends ParentTestClient implements CosmoEcmFeignClient {

  @Override
  public void deletePratiche(Cartella arg0) {
    notMocked();

  }

  @Override
  public Cartella postPratiche(Cartella arg0) {
    notMocked();
    return null;
  }

}
