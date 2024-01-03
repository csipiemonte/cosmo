/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.CustomFormsService;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentUnitTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class CustomFormsServiceImplTest extends ParentUnitTest {

  private static final String CUSTOM1 = "custom-1";

  @Autowired
  private CustomFormsService customFormsService;

  @Test(expected = NotFoundException.class)
  public void getFormNonEsistente() {
    customFormsService.getCustomForm("custom-7");
  }

  @Test
  public void getForm() {
    CustomForm customForm = customFormsService.getCustomForm(CUSTOM1);
    assertNotNull("Deve esserci un form", customForm);
    assertEquals(CUSTOM1, customForm.getCodice());
  }

  @Test
  public void deleteForm() {
    customFormsService.deleteCustomForm(CUSTOM1);
  }

  @Test
  public void getFormsConPaginazione() {
    String filter = "{\"page\":0,\"size\":10,\"fields\":\"codice, descrizione\",\"filter\":{}}";
    CustomFormResponse customForms = customFormsService.getCustomForms(filter);
    assertNotNull("Deve esserci una lista di forms", customForms.getCustomForms());
    assertEquals(6, customForms.getCustomForms().size());
  }

  @Test
  public void getFormsConRicerca() {
    String filter =
        "{\"page\":0,\"size\":10,\"fields\":\"codice, descrizione\",\"filter\":{\"fullText\":{\"ci\":\"custom-1\"}}}";
    CustomFormResponse customForms = customFormsService.getCustomForms(filter);
    assertNotNull("Deve esserci una lista di forms", customForms.getCustomForms());
    assertEquals(1, customForms.getCustomForms().size());
    assertTrue(CUSTOM1.equals(customForms.getCustomForms().get(0).getCodice()));
  }

  @Test
  public void salvaForm() {
    CustomForm customForm = new CustomForm();
    customForm.setCodice("custom-3");
    customForm.setDescrizione("Descrizione custom-3");
    customForm.setCustomForm("{\"id\":1, \"nome\":\"Nome form\"}");

    CustomForm nuovoForm = customFormsService.postCustomForm(customForm);
    assertNotNull("Deve esserci un nuovo form", nuovoForm);
    assertEquals("custom-3", nuovoForm.getCodice());
  }

  @Test
  public void aggiornaFormDescrizione() {
    CustomForm customForm = customFormsService.getCustomForm(CUSTOM1);
    customForm.setDescrizione("Nuova Descrizione");

    CustomForm formAggiornato = customFormsService.putCustomForm(CUSTOM1, customForm);
    assertNotNull("Deve esserci un form", formAggiornato);
    assertEquals(CUSTOM1, formAggiornato.getCodice());
    assertEquals("Nuova Descrizione", formAggiornato.getDescrizione());
  }
  
  @Test(expected = BadRequestException.class)
  public void getFormCodiceNull() {
    customFormsService.getCustomForm(null);
  }
  
  @Test(expected = BadRequestException.class)
  public void getFormCodiceBlank() {
    customFormsService.getCustomForm("");
  }
  
  @Test(expected = BadRequestException.class)
  public void getCustomFormFromCodiceTipoPraticaNull() {
    customFormsService.getCustomFormFromCodiceTipoPratica(null);
  }
  
  @Test(expected = BadRequestException.class)
  public void getCustomFormFromCodiceTipoPraticaEmpty() {
    customFormsService.getCustomFormFromCodiceTipoPratica("");
  }
  
  @Test
  public void getCustomFormFromCodiceTipoPraticaNotFound() {
    CustomForm customForm = customFormsService.getCustomFormFromCodiceTipoPratica("TP3");
    assertNull(customForm);
  }
  
  @Test(expected = BadRequestException.class)
  public void getCustomFormFromCodiceTipoPraticaSizePiuDiUno() {
    customFormsService.getCustomFormFromCodiceTipoPratica("TP5");
  }
  
  @Test
  public void getCustomFormFromCodiceTipoPratica() {
    CustomForm customForm = customFormsService.getCustomFormFromCodiceTipoPratica("TP1");
    assertNotNull(customForm);
    assertEquals(customForm.getCodice(), "custom-6");
    assertEquals(customForm.getCodiceTipoPratica(), "TP1");
    assertEquals(customForm.getDescrizione(), "Esempio di custom form 6");
  }
}
