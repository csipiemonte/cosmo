/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
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
import it.csi.cosmo.cosmobusiness.business.service.FormLogiciService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoIstanzaFunzionalitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaParametroFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoEnte;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional()
public class FormLogiciServiceImplTest extends ParentIntegrationTest {


  @Autowired
  private FormLogiciService formLogiciService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getFormLogiciByAttribute() {
    String filter = "{\"filter\":{\"codice\":{\"eq\":\"form1\"}}}";
    FormLogiciResponse response = formLogiciService.getFormLogici(filter);
    assertTrue("getFormLogiciByAttribute: ci sono due form con codice form1",
        response.getFormLogici().size() == 1);
  }

  @Test
  public void getFormLogicoById() {
    FormLogico response = formLogiciService.getFormLogico(1L);
    assertTrue("getFormLogiciById: il form con id=1 ha descrizione= dati-associazione ",
        response.getCodice().equals("form1"));

  }

  @Test
  public void postFormLogico() {
    RiferimentoEnte ente = new RiferimentoEnte();
    ente.setId(1L);
    ente.setCodiceIpa("r_piemon");
    ente.setCodiceFiscale("97603810017");
    CreaFormLogicoRequest request = new CreaFormLogicoRequest();
    request.setCodice("test");
    request.setRiferimentoEnte(ente);
    FormLogico response = formLogiciService.postFormLogici(request);
    assertTrue("postFormLogico: il form inserito ha codice test e codice Ipa ente = r_piemon ",
        response.getCodice().equals("test")
        && response.getRiferimentoEnte().getCodiceIpa().equals("r_piemon"));
  }

  @Test
  public void postFormLogiciSenzaRiferimentoEnte() {
    CreaFormLogicoRequest body = new CreaFormLogicoRequest();
    body.setCodice("codice");
    body.setWizard(true);
    formLogiciService.postFormLogici(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void putFormLogiciNotFound() {
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    formLogiciService.putFormLogici(111L, body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putFormLogiciConEnteSenzaFunzionalitaEseguibileMassivamente() {
    RiferimentoEnte ente = new RiferimentoEnte();
    ente.setId(1L);
    ente.setCodiceIpa("r_piemon");
    ente.setCodiceFiscale("97603810017");
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    body.setRiferimentoEnte(ente);
    body.setEseguibileMassivamente(true);
    formLogiciService.putFormLogici(6L, body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putFormLogiciSenzaRiferimentoEnteConPiuFunzionalitaEseguibiliMassivamente() {
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    body.setWizard(true);
    body.setEseguibileMassivamente(true);
    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanze = new ArrayList<>();
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza1 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza1.setEseguibileMassivamente(true);
    aggiornaIstanza1.setCodice("codice");
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza2 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza2.setEseguibileMassivamente(true);
    aggiornaIstanza2.setCodice("codice");
    istanze.add(aggiornaIstanza1);
    istanze.add(aggiornaIstanza2);
    body.setIstanzeFunzionalita(istanze);
    formLogiciService.putFormLogici(1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void putFormLogiciFunzionalitaFormNotFound() {
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    body.setWizard(true);
    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanze = new ArrayList<>();
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza1 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza1.setEseguibileMassivamente(true);
    aggiornaIstanza1.setCodice("codice");
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza2 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza2.setEseguibileMassivamente(true);
    aggiornaIstanza2.setCodice("codice");
    istanze.add(aggiornaIstanza1);
    istanze.add(aggiornaIstanza2);
    body.setIstanzeFunzionalita(istanze);
    formLogiciService.putFormLogici(3L, body);
  }
  
  @Test
  public void putFormLogiciFunzionalitaNuova() {
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    body.setWizard(true);
    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanze = new ArrayList<>();
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza1 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza1.setEseguibileMassivamente(true);
    aggiornaIstanza1.setCodice("PROVA-DOCUMENTI");
    istanze.add(aggiornaIstanza1);
    body.setIstanzeFunzionalita(istanze);
    formLogiciService.putFormLogici(3L, body);
  }
  
  @Test
  public void putFormLogiciFunzionalitaEsistenteParametroNuovo() {
    AggiornaFormLogicoRequest body = new AggiornaFormLogicoRequest();
    body.setCodice("codice");
    body.setWizard(true);
    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanze = new ArrayList<>();
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaIstanza1 = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaIstanza1.setEseguibileMassivamente(true);
    aggiornaIstanza1.setCodice("PROVA-DOCUMENTI");
    aggiornaIstanza1.setId(1L);
    List<AggiornaIstanzaParametroFormLogico> aggiornaParametri = new ArrayList<>();
    AggiornaIstanzaParametroFormLogico parametro = new AggiornaIstanzaParametroFormLogico();
    parametro.setChiave("O_ASSEGNAZIONE");
    parametro.setValore("assegnazione");
    aggiornaParametri.add(parametro );
    aggiornaIstanza1.setParametri(aggiornaParametri);
    istanze.add(aggiornaIstanza1);
    body.setIstanzeFunzionalita(istanze);
    formLogiciService.putFormLogici(4L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void setFunzionalitaMultiplaNotFound() {
    formLogiciService.setFunzionalitaMultipla(111L, null, false);
  }
  
  @Test
  public void setFunzionalitaMultipla() {
    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> input = new ArrayList<>();
    AggiornaFormLogicoIstanzaFunzionalitaRequest aggiornaFormLogico = new AggiornaFormLogicoIstanzaFunzionalitaRequest();
    aggiornaFormLogico.setId(5L);
    aggiornaFormLogico.setEseguibileMassivamente(true);
    input.add(aggiornaFormLogico );
    formLogiciService.setFunzionalitaMultipla(3L, input , true);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteFormLogiciNotFound() {
    formLogiciService.deleteFormLogici(111L);
  }
  
  @Test
  public void deleteFormLogici() {
    formLogiciService.deleteFormLogici(1L);
  }

}
