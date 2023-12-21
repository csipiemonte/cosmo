/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.cosmoauthorization.business.service.FruitoriService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaCredenzialiAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaCredenzialiAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FruitoriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.OperazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class FruitoriServiceImplTest extends ParentIntegrationTest {

  private static final String COD_F = "FruitoreTest1";
  private static final String FRUITORE_NON_NULLO = "fruitore non nullo";
  private static final String TEST_111 = "Test111";
  private static final String PROVA = "prova";
  private static final String BASIC = "BASIC";
  private static final String CUSTOM = "CUSTOM";
  private static final String PASS4 = "pass4";
  private static final String ID111 = "111";

  @Autowired
  private FruitoriService fruitoriService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getFruitore() {
    Fruitore fruitore = fruitoriService.getFruitore(1L);

    assertNotNull(FRUITORE_NON_NULLO, fruitore);
    assertTrue("L'apiManagerId deve essere cod_f", COD_F.equals(fruitore.getApiManagerId()));
    assertTrue("Il nome deve essere Test1", fruitore.getNomeApp().equals("Test1"));
    assertTrue("Deve esserci l'ente con id=1",
        fruitore.getEnti().size() == 1 && fruitore.getEnti().get(0).getEnte().getId() == 1);
  }

  @Test(expected = NotFoundException.class)
  public void deleteFruitoreNotFound() {
    fruitoriService.deleteFruitore(111L);
  }

  @Test(expected = NotFoundException.class)
  public void deleteFruitoreCancellato() {
    fruitoriService.deleteFruitore(3L);
  }

  @Test
  public void deleteFruitore() {
    Fruitore fruitore = fruitoriService.deleteFruitore(1L);
    assertNotNull(fruitore);
    assertTrue(fruitore.getId() == 1);
    assertTrue(fruitore.getApiManagerId().equals(COD_F));
  }

  @Test(expected = InternalServerException.class)
  public void getFruitori() {
    FruitoriResponse response = fruitoriService.getFruitori("");
    assertNotNull(response);
    assertNotNull(response.getFruitori());
    assertTrue(response.getFruitori().size() == 3);
  }

  @Test(expected = NotFoundException.class)
  public void getFruitoreNotFound() {
    fruitoriService.getFruitore(111L);
  }

  @Test(expected = NotFoundException.class)
  public void getFruitoreCancellato() {
    fruitoriService.getFruitore(3L);
  }

  @Test(expected = BadRequestException.class)
  public void getFruitoreApiManagerIdWithIdBlank() {
    fruitoriService.getFruitoreApiManagerId("");
  }

  @Test(expected = NotFoundException.class)
  public void getFruitoreApiManagerIdNotFound() {
    fruitoriService.getFruitoreApiManagerId(ID111);
  }

  @Test
  public void getFruitoreApiManagerId() {
    Fruitore fruitore = fruitoriService.getFruitoreApiManagerId(COD_F);
    assertNotNull(fruitore);
    assertTrue(fruitore.getApiManagerId().equals(COD_F));
  }

  @Test(expected = ConflictException.class)
  public void createFruitoreApiManagerIdEsistente() {
    CreaFruitoreRequest request = new CreaFruitoreRequest();
    request.setApiManagerId(COD_F);
    request.setNomeApp(PROVA);
    fruitoriService.createFruitore(request);
  }

  @Test(expected = ConflictException.class)
  public void createFruitoreNomeAppEsistente() {
    CreaFruitoreRequest request = new CreaFruitoreRequest();
    request.setApiManagerId("");
    request.setNomeApp("Test1");
    fruitoriService.createFruitore(request);
  }

  @Test(expected = NotFoundException.class)
  public void createFruitoreAutorizzazioneNonTrovata() {
    CreaFruitoreRequest request = new CreaFruitoreRequest();
    request.setApiManagerId(ID111);
    request.setNomeApp(TEST_111);
    List<String> autorizzazioni = new ArrayList<>();
    autorizzazioni.add(PROVA);
    request.setAutorizzazioni(autorizzazioni);
    fruitoriService.createFruitore(request);
  }

  @Test(expected = NotFoundException.class)
  public void createFruitoreEnteNonTrovato() {
    CreaFruitoreRequest request = new CreaFruitoreRequest();
    request.setApiManagerId(ID111);
    request.setNomeApp(TEST_111);

    List<String> autorizzazioni = new ArrayList<>();
    autorizzazioni.add("I_PRATICA");
    request.setAutorizzazioni(autorizzazioni);

    List<Long> idEnti = new ArrayList<>();
    idEnti.add(111L);
    request.setIdEnti(idEnti);

    fruitoriService.createFruitore(request);
  }

  @Test
  public void createFruitore() {
    CreaFruitoreRequest request = new CreaFruitoreRequest();
    request.setApiManagerId(ID111);
    request.setNomeApp(TEST_111);

    List<String> autorizzazioni = new ArrayList<>();
    autorizzazioni.add("I_PRATICA");
    request.setAutorizzazioni(autorizzazioni);

    List<Long> idEnti = new ArrayList<>();
    idEnti.add(1L);
    request.setIdEnti(idEnti);

    Fruitore fruitore = fruitoriService.createFruitore(request);
    assertNotNull(fruitore);
    assertNotNull(fruitore.getApiManagerId());
    assertTrue(fruitore.getApiManagerId().equals(ID111));
  }

  @Test(expected = NotFoundException.class)
  public void putFruitoreNotFound() {
    AggiornaFruitoreRequest request = new AggiornaFruitoreRequest();
    request.setNomeApp(TEST_111);
    fruitoriService.putFruitore(111L, request);
  }

  @Test
  public void putFruitore() {
    AggiornaFruitoreRequest request = new AggiornaFruitoreRequest();
    request.setNomeApp(TEST_111);

    List<Long> idEnti = new ArrayList<>();
    idEnti.add(1L);
    request.setIdEnti(idEnti);

    Fruitore fruitore = fruitoriService.putFruitore(1L, request);
    assertNotNull(fruitore);
    assertNotNull(fruitore.getApiManagerId());
    assertTrue(fruitore.getNomeApp().equals(TEST_111));
  }

  @Test
  public void putFruitoreConEnteDiverso() {
    AggiornaFruitoreRequest request = new AggiornaFruitoreRequest();
    request.setNomeApp(TEST_111);

    List<Long> idEnti = new ArrayList<>();
    idEnti.add(2L);
    request.setIdEnti(idEnti);

    Fruitore fruitore = fruitoriService.putFruitore(1L, request);
    assertNotNull(fruitore);
    assertNotNull(fruitore.getApiManagerId());
    assertTrue(fruitore.getNomeApp().equals(TEST_111));
  }

  @Test
  public void getOperazioniFruitore() {
    List<OperazioneFruitore> response = fruitoriService.getOperazioniFruitore();
    assertNotNull(response);
    assertNotNull(response.get(0));
    assertTrue(response.size() == 3);
  }

  @Test(expected = NotFoundException.class)
  public void postSchemiAuthFruitoriNotFound() {
    CreaSchemaAutenticazioneFruitoreRequest request = new CreaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(PROVA);
    CreaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new CreaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    fruitoriService.postSchemiAuthFruitori(111L, request);
  }

  @Test
  public void postSchemiAuthFruitori() {
    CreaSchemaAutenticazioneFruitoreRequest request = new CreaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(BASIC);
    CreaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new CreaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    SchemaAutenticazioneFruitore auth = fruitoriService.postSchemiAuthFruitori(1L, request);
    assertNotNull(auth);
    assertNotNull(auth.getTipo());
    assertNotNull(auth.getTipo().getCodice());
    assertTrue(auth.getTipo().getCodice().equals(BASIC));
  }

  @Test(expected = NotFoundException.class)
  public void putSchemaAuthFruitoreSchemaAutenticazioneNotFound() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(BASIC);
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    fruitoriService.putSchemaAuthFruitore(1L, 111L, request);
  }

  @Test(expected = NotFoundException.class)
  public void putSchemaAuthFruitoreNotFoundFruitore() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(BASIC);
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    fruitoriService.putSchemaAuthFruitore(111L, 1L, request);
  }

  @Test
  public void putSchemaAuthFruitoreSenzaCredenziali() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(BASIC);
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    SchemaAutenticazioneFruitore schema = fruitoriService.putSchemaAuthFruitore(1L, 2L, request);
    assertNotNull(schema);
    assertNotNull(schema.getCredenziali());
    assertNull(schema.getCredenziali().getPassword());
  }

  @Test
  public void putSchemaAuthFruitoreSenzaCredenzialiConPassword() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo(BASIC);
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    credenziali.setPassword("pass1");
    request.setCredenziali(credenziali);
    SchemaAutenticazioneFruitore schema = fruitoriService.putSchemaAuthFruitore(1L, 2L, request);
    assertNotNull(schema);
    assertNotNull(schema.getCredenziali());
    assertNotNull(schema.getCredenziali().getPassword());
  }

  @Test
  public void putSchemaAuthFruitoreConCredenziali() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo("NONE");
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    request.setCredenziali(credenziali);
    SchemaAutenticazioneFruitore schema = fruitoriService.putSchemaAuthFruitore(1L, 1L, request);
    assertNotNull(schema);
    assertNotNull(schema.getCredenziali());
    assertNull(schema.getCredenziali().getPassword());
  }

  @Test
  public void putSchemaAuthFruitoreConCredenzialiConClientSecret() {
    AggiornaSchemaAutenticazioneFruitoreRequest request =
        new AggiornaSchemaAutenticazioneFruitoreRequest();
    request.setCodiceTipo("NONE");
    AggiornaCredenzialiAutenticazioneFruitoreRequest credenziali =
        new AggiornaCredenzialiAutenticazioneFruitoreRequest();
    credenziali.setClientSecret("client1");
    request.setCredenziali(credenziali);
    SchemaAutenticazioneFruitore schema = fruitoriService.putSchemaAuthFruitore(1L, 1L, request);
    assertNotNull(schema);
    assertNotNull(schema.getCredenziali());
    assertNotNull(schema.getCredenziali().getClientSecret());
  }

  @Test(expected = NotFoundException.class)
  public void deleteSchemaAuthFruitoreNotFoundSchema() {
    fruitoriService.deleteSchemaAuthFruitore(1L, 111L);
  }

  @Test(expected = NotFoundException.class)
  public void deleteSchemaAuthFruitoreNotFoundFruitore() {
    fruitoriService.deleteSchemaAuthFruitore(111L, 1L);
  }

  @Test(expected = BadRequestException.class)
  public void deleteSchemaAuthFruitoreConEndpointAttivi() {
    fruitoriService.deleteSchemaAuthFruitore(2L, 3L);
  }

  @Test
  public void deleteSchemaAuthFruitore() {
    fruitoriService.deleteSchemaAuthFruitore(1L, 1L);
  }

  @Test(expected = NotFoundException.class)
  public void postEndpointsFruitoriNotFoundFruitore() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione(PROVA);
    fruitoriService.postEndpointsFruitori(111L, request);
  }

  @Test(expected = ConflictException.class)
  public void postEndpointsFruitoriEndpointAttivo() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceDescrittivo("Codice descrittivo");
    fruitoriService.postEndpointsFruitori(1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void postEndpointsFruitoriConCodiceDescrittivoNull() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceDescrittivo(null);
    fruitoriService.postEndpointsFruitori(1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void postEndpointsFruitoriConCodiceDescrittivoBlank() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceDescrittivo("");
    fruitoriService.postEndpointsFruitori(1L, request);
  }

  @Test(expected = NotFoundException.class)
  public void postEndpointsFruitoriSchemaNotFound() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceDescrittivo(PROVA);
    request.setIdSchemaAutenticazione(111L);
    fruitoriService.postEndpointsFruitori(1L, request);
  }

  @Test
  public void postEndpointsFruitori() {
    CreaEndpointFruitoreRequest request = new CreaEndpointFruitoreRequest();
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceOperazione("CUSTOM2");
    request.setCodiceDescrittivo(PROVA);
    request.setIdSchemaAutenticazione(1L);
    EndpointFruitore endpoint = fruitoriService.postEndpointsFruitori(1L, request);
    assertNotNull(endpoint);
    assertNull(endpoint.getCodiceDescrittivo());
  }

  @Test(expected = NotFoundException.class)
  public void putEndpointFruitoreEndpointNotFound() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(PROVA);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    fruitoriService.putEndpointFruitore(1L, 111L, request);
  }

  @Test(expected = NotFoundException.class)
  public void putEndpointFruitoreFruitoreNotFound() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(PROVA);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    fruitoriService.putEndpointFruitore(111L, 1L, request);
  }

  @Test(expected = NotFoundException.class)
  public void putEndpointFruitoreConFruitoreDiverso() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(PROVA);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    fruitoriService.putEndpointFruitore(2L, 1L, request);
  }

  @Test(expected = ConflictException.class)
  public void putEndpointFruitoreConEndpointAttivo() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceDescrittivo("Codice descrittivo - placeholder");
    fruitoriService.putEndpointFruitore(1L, 1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void putEndpointFruitoreConCodiceDescrittivoNull() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceDescrittivo(null);
    fruitoriService.putEndpointFruitore(1L, 1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void putEndpointFruitoreConCodiceDescrittivoBlank() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceDescrittivo("");
    fruitoriService.putEndpointFruitore(1L, 1L, request);
  }

  @Test(expected = NotFoundException.class)
  public void putEndpointFruitoreConSchemaNotFound() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione(CUSTOM);
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceDescrittivo(PROVA);
    request.setIdSchemaAutenticazione(111L);
    fruitoriService.putEndpointFruitore(1L, 1L, request);
  }

  @Test
  public void putEndpointFruitore() {
    AggiornaEndpointFruitoreRequest request = new AggiornaEndpointFruitoreRequest();
    request.setCodiceOperazione("CUSTOM2");
    request.setCodiceTipo(PROVA);
    request.setEndpoint(PROVA);
    request.setCodiceDescrittivo(PROVA);
    request.setIdSchemaAutenticazione(1L);
    EndpointFruitore endpoint = fruitoriService.putEndpointFruitore(1L, 1L, request);
    assertNotNull(endpoint);
    assertNotNull(endpoint.getCodiceTipoEndpoint());
    assertTrue(endpoint.getCodiceTipoEndpoint().equals(PROVA));
  }

  @Test(expected = NotFoundException.class)
  public void deleteEndpointFruitoreEndpointNotFound() {
    fruitoriService.deleteEndpointFruitore(1L, 111L);
  }

  @Test(expected = NotFoundException.class)
  public void deleteEndpointFruitoreConFruitoreDiverso() {
    fruitoriService.deleteEndpointFruitore(2L, 1L);
  }

  @Test
  public void deleteEndpointFruitore() {
    fruitoriService.deleteEndpointFruitore(1L, 1L);
  }

  @Test(expected = UnauthorizedException.class)
  public void postFruitoriAutenticaCredenzialiErrate() {
    CredenzialiAutenticazioneFruitore request = new CredenzialiAutenticazioneFruitore();
    request.setUsername(PROVA);
    request.setPassword(PROVA);
    fruitoriService.postFruitoriAutentica(request);
  }

  @Test(expected = ConflictException.class)
  public void postFruitoriAutenticaFruitoriMultipli() {
    CredenzialiAutenticazioneFruitore request = new CredenzialiAutenticazioneFruitore();
    request.setUsername("utente1");
    request.setPassword("pass1");
    fruitoriService.postFruitoriAutentica(request);
  }

  @Test
  public void postFruitoriAutentica() {
    CredenzialiAutenticazioneFruitore request = new CredenzialiAutenticazioneFruitore();
    request.setUsername("utente4");
    request.setPassword(PASS4);
    Fruitore fruitore = fruitoriService.postFruitoriAutentica(request);
    assertNotNull(fruitore);
    assertNotNull(fruitore.getId());
    assertTrue(fruitore.getId() == 4);
  }

  @Test(expected = BadRequestException.class)
  public void postFruitoriAutenticaClientBlank() {
    CredenzialiAutenticazioneFruitore request = new CredenzialiAutenticazioneFruitore();
    request.setUsername("");
    request.setPassword(PASS4);
    request.setClientId("");
    fruitoriService.postFruitoriAutentica(request);
  }

  @Test(expected = BadRequestException.class)
  public void postFruitoriAutenticaCredenzialiNonFornite() {
    CredenzialiAutenticazioneFruitore request = new CredenzialiAutenticazioneFruitore();
    request.setUsername("");
    request.setPassword(PASS4);
    request.setClientId(PROVA);
    fruitoriService.postFruitoriAutentica(request);
  }
}
