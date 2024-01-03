/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmonotifications.business.service.HelperService;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.CustomForm;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;
import it.csi.test.cosmo.cosmonotifications.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class})
@Transactional
public class HelperServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private HelperService helperService;

  protected static final String DIRECTORY_PATH = "test/resources/helper/import";

  @Test(expected = NotFoundException.class)
  public void getHelperNonEsistente() {
    helperService.getHelper("1000");
  }

  @Test
  public void getCodiciPagina() {
    List<CodicePagina> codiciPagina = helperService.getCodiciPagina();
    assertNotNull("Deve esserci una lista di codici pagina", codiciPagina);
  }

  @Test
  public void getCodiciTab() {
    List<CodiceTab> codiciTab = helperService.getCodiciTab("preferenze_ente");
    assertNotNull("Deve esserci una lista di codici tab", codiciTab);
  }

  @Test
  public void getAllHelpers() {
    HelperResponse helpers = helperService.getHelpers("");
    assertNotNull("Deve esserci una lista di helpers", helpers.getHelpers());
    assertEquals(3, helpers.getHelpers().size());
  }

  @Test
  public void getHelpersConPaginazione() {
    String filter = "{\"page\":0,\"size\":10,\"fields\":\"codice_pagina, html\",\"filter\":{}}";
    HelperResponse helpers = helperService.getHelpers(filter);
    assertNotNull("Deve esserci una lista di helpers", helpers.getHelpers());
    assertEquals(3, helpers.getHelpers().size());
  }

  @Test
  public void getHelpersConFiltro() {
    String filter =
        "{\"page\":0,\"size\":10,\"fields\":\"codice_pagina, html\",\"filter\":{\"codicePagina\":{\"ci\":\"home\"}}}";
    HelperResponse helpers = helperService.getHelpers(filter);
    assertNotNull("Deve esserci una lista di helpers", helpers.getHelpers());
    assertEquals(1, helpers.getHelpers().size());
  }

  @Test
  public void salvaHelper() {
    Helper helper = new Helper();
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("custom-form");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("anteprima-documento");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    Helper nuovoHelper = helperService.postHelper(helper);
    assertNotNull("Deve esserci un nuovo form", nuovoHelper);
    assertEquals("preferenze_ente", nuovoHelper.getCodicePagina().getCodice());
  }

  @Test
  public void aggiornaHelper() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("custom-1");
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("custom-form");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("anteprima-documento");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    Helper helperAggiornato = helperService.putHelper("1", helper);
    assertNotNull("Deve esserci un helper", helperAggiornato);
    assertEquals("<p>Hello world!</p>", helperAggiornato.getHtml());
  }

  @Test
  public void cancellaHelper() {
    helperService.deleteHelper("1");
  }

  @Test
  public void aggiornaHelperConTabModaleEFormBlank() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("");
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    Helper helperAggiornato = helperService.putHelper("1", helper);
    assertNotNull("Deve esserci un helper", helperAggiornato);
    assertEquals("<p>Hello world!</p>", helperAggiornato.getHtml());
  }

  @Test(expected = BadRequestException.class)
  public void getHelperBlank() {
    helperService.getHelper("");
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaHelperConTabNonEsistente() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("");
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("prova-biagio");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");
    helperService.putHelper("1", helper);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaHelperConModaleNonEsistente() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("");
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("prova-biagio");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    helperService.putHelper("1", helper);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaHelperConFormNonEsistente() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("prova_biagio");
    codicePagina.setCodice("preferenze_ente");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    helperService.putHelper("1", helper);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaHelperConPaginaNonEsistente() {
    Helper helper = helperService.getHelper("1");
    CodicePagina codicePagina = new CodicePagina();
    CodiceTab codiceTab = new CodiceTab();
    CodiceModale codiceModale = new CodiceModale();
    CustomForm customForm = new CustomForm();
    customForm.setCodice("");
    codicePagina.setCodice("prova_biagio");
    codicePagina.setDescrizione("Helper preferenze utente");
    codiceTab.setCodice("");
    codiceTab.setDescrizione("descrizione");
    codiceModale.setCodice("");
    codiceModale.setDescrizione("anteprima-documento");
    helper.setCodiceTab(codiceTab);
    helper.setCodicePagina(codicePagina);
    helper.setCodiceForm(customForm);
    helper.setCodiceModale(codiceModale);
    helper.setHtml("<p>Hello world!</p>");

    helperService.putHelper("1", helper);
  }

  @Test
  public void getModaliByFilter() {
    List<CodiceModale> list = helperService.getModali("{\"filter\":{\"codicePagina\": {\"eq\":\"lavorazione_pratica\"}, \"codiceTab\":{\"eq\":\"custom-form\"}}}");
    assertNotNull(list);
  }

  @Test
  public void esporta() {
    var exportByteArray = helperService.getExport("1");
    assertNotNull("Deve essere presente un array di byte", exportByteArray);
  }

  @Test(expected = NotFoundException.class)
  public void tentaExportDiHelperNonPresente() {
    var exportByteArray = helperService.getExport("1000");
    assertNotNull("Deve essere presente un array di byte", exportByteArray);
  }

  @Test
  public void importa() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/creazione-pratica.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    try {

      var importResult = helperService.postImport(request);

      dump("importResult", importResult);

    } catch (Exception e) {

    }
  }

  @Test
  public void cercaDecodificaCodiceForm() {
    var decodifica = helperService.getDecodifica("lavorazione_pratica", "custom-form", "custom-1");
    assertNotNull(decodifica);
  }

  @Test
  public void cercaDecodificaCodiceTab() {
    var decodifica = helperService.getDecodifica("preferenze_ente", "tab1", "");
    assertNotNull(decodifica);
  }

  @Test
  public void cercaDecodificaCodicePagina() {
    var decodifica = helperService.getDecodifica("home", "", "");
    assertNotNull(decodifica);
  }
  
  @Test
  public void importaFormatoErrato() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/errore-formato.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Il file fornito non e' un export helper valido"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodicePaginaHelperErrato() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codice-pagina-errato.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste una pagina con codice prova per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodiceTabErrato() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codice-tab-errato.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste un tab con codice prova per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodiceTabECodicePaginaDiversi() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codici-tab-pagina-diversi.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste un tab con codice tab1 associato alla pagina con codice home per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodiceModaleErrato() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codice-modale-errato.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste un modale con codice prova per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodiceTabNull() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codice-tab-null.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste un modale con codice anteprima-documento associato alla pagina con codice home per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaCodiciModaleDiversi() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/codici-modale-diversi.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("KO"));
    assertNotNull(importResult.getReason());
    assertTrue(importResult.getReason().equals("Non esiste un modale tab con codice anteprima-documento associato alla pagina con codice preferenze_ente e associato alla tab con codice tab1 per cui creare un helper"));
    dump("importResult", importResult);
  }
  
  @Test
  public void importaHelper() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/import-helper.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("OK"));
    assertNull(importResult.getReason());
    dump("importResult", importResult);
  }
  
  @Test
  public void importaNuovoHelper() throws IOException {
    String cwd = Path.of(DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);
    File inputfile = new File(dir + "/import-helper-nuovo.json");
    HelperImportRequest request = new HelperImportRequest();
    request.setFile(FileUtils.readFileToByteArray(inputfile));
    HelperImportResult importResult = helperService.postImport(request);
    assertNotNull(importResult);
    assertNotNull(importResult.getStatus());
    assertTrue(importResult.getStatus().equals("OK"));
    assertNull(importResult.getReason());
    dump("importResult", importResult);
  }
}
