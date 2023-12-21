/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.cosmoauthorization.business.service.UseCaseService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CategorieUseCaseResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCaseResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class UseCaseServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private UseCaseService useCaseService;

  @Test
  public void getUseCase() {
    CategorieUseCaseResponse useCase = useCaseService.getUseCase("ADMIN");

    assertNotNull("useCase non nullo", useCase);
  }

  @Test
  public void getUseCaseNotExist() {
    try {

      useCaseService.getUseCase("ADMIN PRIN");
      fail("useCase non deve esistere");
    } catch (ManagedException e) {
      assertTrue(e.getMessage().equals("Codice categoria dello use-case non trovato"));
    }
  }

  @Test
  public void getUseCaseCodiceNullo() {
    try {

      useCaseService.getUseCase("");
      fail("useCase non deve esistere");
    } catch (ManagedException e) {
      assertTrue(e.getMessage().equals("Codice dello use-case non valorizzato"));
    }
  }
  
  @Test
  public void getUseCasesFilterNull() {
    UseCaseResponse response = useCaseService.getUseCases(null);
    assertNotNull(response);
    assertNotNull(response.getUseCases());
    assertTrue(response.getUseCases().size() == 3);
  }
  
  @Test
  public void getUseCasesFilterBlank() {
    UseCaseResponse response = useCaseService.getUseCases("");
    assertNotNull(response);
    assertNotNull(response.getUseCases());
    assertTrue(response.getUseCases().size() == 3);
  }
  
  @Test
  public void getUseCasesFilterNotBlank() {
    UseCaseResponse response = useCaseService.getUseCases("prova");
    assertNull(response);
  }
}
