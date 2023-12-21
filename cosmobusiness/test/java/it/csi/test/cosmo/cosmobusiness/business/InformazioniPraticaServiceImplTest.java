/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.InformazioniPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class InformazioniPraticaServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private InformazioniPraticaService informazioniPraticaService;

  @Test
  public void getInfoPratica() {
    var result = informazioniPraticaService.getInfoPratica("1", "1");

    assertNotNull("Devono esserci le informazioni di una pratica", result);
    assertNotNull("L'oggetto della pratica deve essere valorizzato", result.getOggetto());
    assertNotNull("C'e' un tipo di pratica", result.getTipo());
    assertNotNull("C'e' un documento", result.getDocumentoPerSmistamento());
    assertTrue(result.getCommenti().size() == 2);
    assertTrue(result.getAttivita().size() == 4);
    assertTrue(result.getDocumentoPerSmistamento().getFirme().size() == 1);
  }
  
  @Test(expected = BadRequestException.class)
  public void getInfoPraticaIdNotNumeric() {
    informazioniPraticaService.getInfoPratica("prova", "1");
  }
  
  @Test(expected = BadRequestException.class)
  public void getInfoPraticaIdDocumentoNotNumeric() {
    informazioniPraticaService.getInfoPratica("1", "prova");
  }
  
  @Test(expected = NotFoundException.class)
  public void getInfoPraticaNotFound() {
    informazioniPraticaService.getInfoPratica("111", "1");
  }
  
  @Test(expected = NotFoundException.class)
  public void getInfoPraticaDocumentoNotFound() {
    informazioniPraticaService.getInfoPratica("1", "111");
  }
  
  @Test(expected = NotFoundException.class)
  public void elaboraInformazioniPraticaNotFound() {
    GetElaborazionePraticaRequest body = new GetElaborazionePraticaRequest();
    informazioniPraticaService.elaboraInformazioniPratica(111L, body);
  }
  
  @Test
  public void elaboraInformazioniPratica() {
    GetElaborazionePraticaRequest body = new GetElaborazionePraticaRequest();
    body.setMappatura("{\"datoinviato\": \"Esempio di dato inviato\"}");
    informazioniPraticaService.elaboraInformazioniPratica(1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void getContestoInformazioniPraticaNotFound() {
    informazioniPraticaService.getContestoInformazioniPratica(111L);
  }
  
  @Test
  public void getContestoInformazioniPratica() {
    informazioniPraticaService.getContestoInformazioniPratica(1L);
  }
}