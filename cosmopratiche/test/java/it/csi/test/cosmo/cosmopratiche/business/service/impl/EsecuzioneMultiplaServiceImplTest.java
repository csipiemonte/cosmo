/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmopratiche.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoPraticheUnitTestInMemory.class} )
@Transactional
public class EsecuzioneMultiplaServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test
  public void getTaskDisponibiliPerUtenteCorrenteConRisultato() {
    List<AttivitaEseguibileMassivamente> listaAttivita = esecuzioneMultiplaService.getTaskDisponibiliPerUtenteCorrente();
    assertNotNull(listaAttivita);
  }
  
  @Test
  public void getTipologieTaskDisponibiliPerUtenteCorrente() {
    String filter = "{\"id\":{\"gt\":\"2\"}}";
    List<RiferimentoAttivita> listaAttivita = esecuzioneMultiplaService.getTipologieTaskDisponibiliPerUtenteCorrente(filter);
    assertNotNull(listaAttivita);
  }
}
