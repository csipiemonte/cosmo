/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.VariabiliFiltroService;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabiliDiFiltroResponse;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class VariabiliFiltroServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private VariabiliFiltroService variabiliDiFiltroService;

  @Autowired
  CosmoTPraticaRepository cosmoPracticesRepository;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  @Test
  public void testSearchVariabiliFiltroByDescrizioneTipoPratica() {

    String filter = "{\"filter\":{\"descrizioneTipoPratica\":{\"eq\": \"Tipo pratica 1\"}" + "}}";

    VariabiliDiFiltroResponse response =
        variabiliDiFiltroService.getVariabiliFiltro(filter);
    assertNotNull(response);
    assertNotNull(response.getPageInfo().getTotalPages());
    assertNotNull(response.getPageInfo().getPageSize());
    assertNotNull(response.getPageInfo().getPage());
    assertNotNull(response.getPageInfo().getTotalElements());
    assertNotNull(response.getVariabiliDiFiltro());
    assertTrue(response.getVariabiliDiFiltro().size() == 2);
  }

  @Test
  public void testSearchVariabiliFiltroByNomeEnte() {

    String filter = "{\"filter\":{\"nomeEnte\":{\"eq\": \"Regione Piemonte\"}" + "}}";

    VariabiliDiFiltroResponse response = variabiliDiFiltroService.getVariabiliFiltro(filter);
    assertNotNull(response);
    assertNotNull(response.getPageInfo().getTotalPages());
    assertNotNull(response.getPageInfo().getPageSize());
    assertNotNull(response.getPageInfo().getPage());
    assertNotNull(response.getPageInfo().getTotalElements());
    assertNotNull(response.getVariabiliDiFiltro());
    assertTrue(response.getVariabiliDiFiltro().size() == 3);
  }

  @Test
  public void testSearchVariabiliFiltroByLabelFiltro() {

    String filter = "{\"filter\":{\"labelFiltro\":{\"eq\": \"prova3\"}" + "}}";

    VariabiliDiFiltroResponse response = variabiliDiFiltroService.getVariabiliFiltro(filter);
    assertNotNull(response);
    assertNotNull(response.getPageInfo().getTotalPages());
    assertNotNull(response.getPageInfo().getPageSize());
    assertNotNull(response.getPageInfo().getPage());
    assertNotNull(response.getPageInfo().getTotalElements());
    assertNotNull(response.getVariabiliDiFiltro());
    assertTrue(response.getVariabiliDiFiltro().size() == 1);
  }



  @Test
  public void testInsertVariabiliFiltro() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    VariabileDiFiltro savedVariabileDiFiltro =
        variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);

    assertNull(newVariabileDiFiltro.getId());
    assertNotNull(savedVariabileDiFiltro.getId());

  }

  @Test
  public void testGetByIdVariabiliFiltro() {

    VariabileDiFiltro variabileDiFiltro = variabiliDiFiltroService.getVariabiliFiltroId("1");
    assertTrue(variabileDiFiltro.getNome().equals("prova flowable"));
  }

  @Test(expected = NotFoundException.class)
  public void testGetByIdNotFoundVariabiliFiltro() {

    variabiliDiFiltroService.getVariabiliFiltroId("4");
  }

  @Test
  public void testUpdateVariabiliFiltro() {

    VariabileDiFiltro variabileDiFiltro = variabiliDiFiltroService.getVariabiliFiltroId("1");
    assertTrue(variabileDiFiltro.getNome().equals("prova flowable"));
    variabileDiFiltro.setNome("prova flowable modificata");
    variabiliDiFiltroService.putVariabiliFiltroId("1", variabileDiFiltro);
    variabileDiFiltro = variabiliDiFiltroService.getVariabiliFiltroId("1");
    assertTrue(variabileDiFiltro.getNome().equals("prova flowable modificata"));
  }


  @Test
  public void testDeleteVariabiliFiltro() {
    VariabileDiFiltro variabileDiFiltro = variabiliDiFiltroService.getVariabiliFiltroId("1");
    variabiliDiFiltroService.deleteVariabiliFiltroId(variabileDiFiltro.getId().toString());
  }
  
  @Test(expected = BadRequestException.class)
  public void testInsertVariabiliFiltroTipoPraticaNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP15");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testInsertVariabiliFiltroTipoFiltroNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("prova filtro");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testInsertVariabiliFiltroFormatoCampoNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("prova formato");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testPutVariabiliFiltroTipoPraticaNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP15");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.putVariabiliFiltroId("1", newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testPutVariabiliFiltroTipoFiltroNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("prova filtro");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.putVariabiliFiltroId("1", newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testPutVariabiliFiltroFormatoCampoNotFound() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("nome_variabile");
    newVariabileDiFiltro.setNome("nome_variabile_flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("prova formato");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.putVariabiliFiltroId("1", newVariabileDiFiltro);
  }
  
  @Test
  public void getVariabiliFiltroFormati() {
    List<FormatoVariabileDiFiltro> listaVariabili = variabiliDiFiltroService.getVariabiliFiltroFormati();
    assertNotNull(listaVariabili);
    assertTrue(listaVariabili.size() == 4);
  }
  
  @Test
  public void getVariabiliFiltroTipiFiltro() {
    List<TipoFiltro> listaFiltri = variabiliDiFiltroService.getVariabiliFiltroTipiFiltro();
    assertNotNull(listaFiltri);
    assertTrue(listaFiltri.size() == 2);
  }
  
  @Test
  public void getVariabiliFiltroTipoPratica() {
    List<VariabileDiFiltro> listaFiltri = variabiliDiFiltroService.getVariabiliFiltroTipoPratica("TP1");
    assertNotNull(listaFiltri);
    assertTrue(listaFiltri.size() == 2);
  }
  
  @Test(expected = NotFoundException.class)
  public void getVariabiliFiltroTipoPraticaNotFound() {
    variabiliDiFiltroService.getVariabiliFiltroTipoPratica("TP18");
  }
  
  @Test(expected = BadRequestException.class)
  public void testInsertVariabiliFiltroConNomeVariabileErrato() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("prova");
    newVariabileDiFiltro.setNome("prova flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testInsertVariabiliFiltroConLabelVariabileErrato() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("prova11");
    newVariabileDiFiltro.setNome("prova flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.postVariabiliFiltro(newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testPutVariabiliFiltroConNomeVariabileErrato() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("prova");
    newVariabileDiFiltro.setNome("prova flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.putVariabiliFiltroId("3", newVariabileDiFiltro);
  }
  
  @Test(expected = BadRequestException.class)
  public void testPutVariabiliFiltroConLabelVariabileErrato() {

    VariabileDiFiltro newVariabileDiFiltro = new VariabileDiFiltro();
    newVariabileDiFiltro.setLabel("prova11");
    newVariabileDiFiltro.setNome("prova flowable");
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice("numerico");
    newVariabileDiFiltro.setFormato(formatoVariabileDiFiltro);
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice("range");
    newVariabileDiFiltro.setTipoFiltro(tipoFiltro);
    TipoPratica tipoPratica = new TipoPratica();
    tipoPratica.setCodice("TP1");
    newVariabileDiFiltro.setTipoPratica(tipoPratica);
    variabiliDiFiltroService.putVariabiliFiltroId("3", newVariabileDiFiltro);
  }
}
