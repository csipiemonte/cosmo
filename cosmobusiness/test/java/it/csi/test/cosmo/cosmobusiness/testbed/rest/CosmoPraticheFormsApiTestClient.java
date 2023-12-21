/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFormsApiFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;

/**
 *
 */
@Component
public class CosmoPraticheFormsApiTestClient extends ParentTestClient
implements CosmoPraticheFormsApiFeignClient {

  @Override
  public StrutturaFormLogico getFormsNome(String arg0) {
    notMocked();
    return null; // NOSONAR
  }

  @Override
  public SimpleForm getFormDefinitionFormKey(String arg0) {
    notMocked();
    return null; // NOSONAR
  }

  @Override
  public StrutturaFormLogico getFormsAttivitaId(String arg0) {
    notMocked();
    return null;
  }

}
