/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.stardas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDStatoSmistamento;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import it.csi.cosmo.cosmoecm.business.service.StardasService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.InformazioneAggiuntivaSmistamento;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.stardas.EsitoRichiestaSmistamentoDocumento;
import it.csi.cosmo.cosmoecm.dto.stardas.StatoSmistamento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoSmistamentoRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapStardasFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.Result;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;
import it.csi.test.cosmo.cosmoecm.integration.stardas.SmistaDocumentoTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestDB;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmoecm.testbed.service.StardasTestbedService;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestDB.class, TestConfig.class})
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class SmistaDocumentoTest extends ParentIntegrationTest {

  public static final String TEST_ROOT = "test/SmistaDocumentoTest";

  public static final RichiediSmistamentoRequest BODY = new RichiediSmistamentoRequest();

  private static final List<CodiceTipologiaDocumento> TIPI_DOCUMENTO = new LinkedList<>();

  private static final String IDENTIFICATIVO_EVENTO = "identificativo_evento_test";

  private static final String TEST_UUID_SMISTAMENTO = "b89318b4-50f4-11eb-ae93-0242ac130002";

  private static final String MESSAGGIO_SMISTAMENTO_OK = "Smistamento completato con successo";

  protected static CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "SmistaDocumentoTest");

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient() {
      return Mockito.mock(CosmoBusinessPraticheFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoNotificationsNotificheGlobaliFeignClient cosmoNotificationsNotificheGlobaliFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheGlobaliFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoSoapStardasFeignClient cosmoSoapStardasFeignClient() {
      return Mockito.mock(CosmoSoapStardasFeignClient.class);
    }
  }

  @Autowired
  private CosmoBusinessPraticheFeignClient mockCosmoBusinessPraticheFeignClient;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient mockCosmoNotificationsNotificheGlobaliFeignClient;

  @Autowired
  private CosmoSoapStardasFeignClient mockCosmoSoapStardasFeignClient;

  @PersistenceContext
  EntityManager em;

  @Autowired
  private StardasService stardasService;

  @Autowired
  StardasTestbedService stardasTestbedService;

  @Autowired
  private CosmoDStatoSmistamentoRepository cosmoDStatoSmistamentoRepository;

  private CosmoTPratica pratica;

  @Before
  public void setup() throws IOException {
    pratica = stardasTestbedService.salvaPratica(true);
    BODY.setIdentificativoMessaggio(IDENTIFICATIVO_EVENTO);
    CodiceTipologiaDocumento tipoDoc1 = new CodiceTipologiaDocumento();
    tipoDoc1.setCodice("doc_stardas");
    CodiceTipologiaDocumento tipoDoc2 = new CodiceTipologiaDocumento();
    tipoDoc2.setCodice("doc_stardas_princ");
    tipoDoc2.setCodicePadre("doc_stardas_alleg");
    TIPI_DOCUMENTO.add(tipoDoc1);
    TIPI_DOCUMENTO.add(tipoDoc2);
    BODY.setTipiDocumento(TIPI_DOCUMENTO);
  }

  @After
  public void cleanup() {
    logger.info("cleanup", "DELETING TEST FOLDER");
  }

  @Test
  public void richiediSmistamento() {
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    em.refresh(pratica);
    var documentiDaSmistare =
        stardasService.recuperaDocumentiDaSmistare(pratica.getId(),
            Constants.PRINCIPALI_E_ALLEGATI);
    assertEquals(documentiDaSmistare.size(), 1);
  }

  @Test
  public void richiediSmistamentoNoDoc() {
    pratica.setDocumenti(null);
    em.refresh(pratica);
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    em.refresh(pratica);
    var documentiDaSmistare = stardasService
        .recuperaDocumentiDaSmistare(pratica.getId(),
            Constants.PRINCIPALI_E_ALLEGATI);
    assertEquals(documentiDaSmistare.size(), 1);
  }

  @Test(expected = BadRequestException.class)
  public void richiediSmistamentoNoIdPratica() {
    stardasService.richiediSmistamento(null, BODY);
  }

  @Test(expected = BadRequestException.class)
  public void richiediSmistamentoNoIdEvento() {
    stardasService.richiediSmistamento(pratica.getId().toString(), null);
  }

  @Test(expected = NumberFormatException.class)
  public void richiediSmistamentoIdPraticaNoNum() {
    stardasService.richiediSmistamento("BAD_ID_PRATICA", BODY);
  }

  @Test
  public void smistaDocumento() {
    setUpMock();

    var esito = stardasService.smistaDocumento(pratica.getDocumenti().get(0));
    assertNotNull(esito);
    assertTrue((esito.getCodice().equalsIgnoreCase(Constants.ESITO_SMISTAMENTO_OK)) || esito.getCodice().matches(Constants.REGEX_CODICE_ESITO_WARNING));
  }

  @Test
  public void impostaEsitoSmistamentoPost() {

    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
    em.refresh(pratica.getDocumenti().get(0));
    String messageUUID =pratica.getDocumenti().get(0).getCosmoRSmistamentoDocumentos().stream().findFirst().orElse(new CosmoRSmistamentoDocumento()).getMessageUuid();
    stardasService.creaEsitoSmistamento(pratica.getDocumenti().get(0).getId(),messageUUID);
  }

  @Test
  public void impostaEsitoSmistamentoPut() {
    var request = prepareEsitoSmistamento(StatoSmistamento.ERR_CALLBACK);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        false);
    em.refresh(pratica.getDocumenti().get(0));
    String messageUUID =pratica.getDocumenti().get(0).getCosmoRSmistamentoDocumentos().stream().findFirst().orElse(new CosmoRSmistamentoDocumento()).getMessageUuid();
    stardasService.creaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), messageUUID);
  }

  @Test(expected = ConflictException.class)
  public void impostaEsitoSmistamentoNoStatoSmistamento() {
    var request = prepareEsitoSmistamento(null);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        false);
  }

  @Test(expected = InternalServerException.class)
  public void impostaEsitoSmistamentoPostNoPrimoSmistamento() {
    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        false);
  }

  @Test(expected = ConflictException.class)
  public void impostaEsitoSmistamentoPutPrimoSmistamento() {
    var request = prepareEsitoSmistamento(StatoSmistamento.ERR_SMISTAMENTO);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
  }

  @Test(expected = BadRequestException.class)
  public void impostaEsitoSmistamentoNoRequest() {

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), null,
        true);
  }


  @Test(expected = BadRequestException.class)
  public void impostaEsitoSmistamentoNoEsito() {

    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    request.setEsito(null);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
  }


  @Test(expected = ConflictException.class)
  public void impostaEsitoSmistamentoTroppiEsiti() {

    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    request.getEsito().add(request.getEsito().get(0));

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
  }

  @Test(expected = BadRequestException.class)
  public void impostaEsitoSmistamentoNoCodiceEsito() {

    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    request.getEsito().get(0).setCode(null);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
  }


  @Test(expected = BadRequestException.class)
  public void impostaEsitoSmistamentoNoMessageUuid() {

    var request = prepareEsitoSmistamento(StatoSmistamento.IN_SMISTAMENTO);

    request.setMessageUUID(null);

    stardasService.impostaEsitoSmistamento(pratica.getDocumenti().get(0).getId(), request,
        true);
  }

  @Test
  public void salvaEsitoRichiestaSmistamento() {
    em.refresh(pratica);
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    var documento = pratica.getDocumenti().get(0);
    em.refresh(documento);
    EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento =
        new EsitoRichiestaSmistamentoDocumento(Constants.ESITO_SMISTAMENTO_OK,
            MESSAGGIO_SMISTAMENTO_OK, TEST_UUID_SMISTAMENTO);
    stardasService.salvaEsitoRichiestaSmistamento(esitoRichiestaSmistamento, documento);
  }


  @Test(expected = InternalServerException.class)
  public void salvaEsitoRichiestaSmistamentoNoRichiesta() {
    em.refresh(pratica);
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    var documento = pratica.getDocumenti().get(0);
    em.refresh(documento);
    stardasService.salvaEsitoRichiestaSmistamento(null, documento);
  }

  @Test(expected = InternalServerException.class)
  public void salvaEsitoRichiestaSmistamentoNoDocumento() {
    em.refresh(pratica);
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento =
        new EsitoRichiestaSmistamentoDocumento(Constants.ESITO_SMISTAMENTO_OK,
            MESSAGGIO_SMISTAMENTO_OK, TEST_UUID_SMISTAMENTO);
    stardasService.salvaEsitoRichiestaSmistamento(esitoRichiestaSmistamento, null);
  }

  private EsitoSmistaDocumentoRequest prepareEsitoSmistamento(StatoSmistamento statoSmistamento) {
    em.refresh(pratica);
    assertEquals(pratica.getDocumenti().size(), 1);
    stardasService.richiediSmistamento(pratica.getId().toString(), BODY);
    var documento = pratica.getDocumenti().get(0);
    em.refresh(documento);
    assertEquals(documento.getCosmoRSmistamentoDocumentos().size(), 1);
    var relazioneSmistamento = documento.getCosmoRSmistamentoDocumentos().get(0);

    if (null != statoSmistamento) {
      CosmoDStatoSmistamento statoSmistamentoDb = this.cosmoDStatoSmistamentoRepository
          .findOneActive(statoSmistamento.getCodice()).orElse(null);

      relazioneSmistamento.setCosmoDStatoSmistamento(statoSmistamentoDb);
    }
    relazioneSmistamento.setMessageUuid(TEST_UUID_SMISTAMENTO);
    stardasTestbedService.salvaRelazioneSmistamento(relazioneSmistamento);

    var request = new EsitoSmistaDocumentoRequest();
    Esito esito = new Esito();
    esito.setCode(Constants.ESITO_SMISTAMENTO_OK);
    esito.setTitle(MESSAGGIO_SMISTAMENTO_OK);
    List<Esito> esiti = new ArrayList<>();
    esiti.add(esito);
    request.setEsito(esiti);
    request.setMessageUUID(TEST_UUID_SMISTAMENTO);
    request.setTipoTrattamento("Tipo trattamento test");
    var info1 = new InformazioneAggiuntivaSmistamento();
    info1.setCodInformazione("info 1");
    info1.setValore("valore 1");
    var listInfoAggiuntive = new ArrayList<InformazioneAggiuntivaSmistamento>();
    listInfoAggiuntive.add(info1);
    request.setInformazioniAggiuntive(listInfoAggiuntive);

    return request;
  }


  private void setUpMock() {
    reset(mockCosmoBusinessPraticheFeignClient);

    when(mockCosmoBusinessPraticheFeignClient.getPraticheIdInfo(any(), any()))
    .thenReturn(creaDTO());

    reset(mockCosmoNotificationsNotificheGlobaliFeignClient);

    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());

    reset(mockCosmoSoapStardasFeignClient);

    when(mockCosmoSoapStardasFeignClient.smistamentoDocumento(any()))
    .thenReturn(documentoSmistato());
  }

  private InformazioniPratica creaDTO() {
    InformazioniPratica ritorno = new InformazioniPratica();
    ritorno.setId(pratica.getId().toString());
    ritorno.setCodiceIpaEnte(pratica.getEnte().getCodiceFiscale());
    ritorno.setDescrizione(pratica.getEnte().getNome());

    TipoPraticaFruitore tipo = new TipoPraticaFruitore();
    if (pratica.getTipo()!=null) {
      tipo.setCodice(pratica.getTipo().getCodice());
      tipo.setDescrizione(pratica.getTipo().getDescrizione());
    } else {
      tipo.setCodice("Tipo pratica");
      tipo.setDescrizione("Descrizione del tipo pratica");
    }

    ritorno.setTipo(tipo);
    ritorno.setOggetto(pratica.getOggetto());

    StatoPraticaFruitore stato = new StatoPraticaFruitore();
    if (pratica.getStato()!=null) {
      stato.setCodice(pratica.getStato().getCodice());
      stato.setDescrizione(pratica.getStato().getDescrizione());
    } else {
      stato.setCodice("Stato pratica");
      stato.setDescrizione("Descrizione stato pratica");
    }
    ritorno.setStato(stato);

    ritorno.setRiassunto(pratica.getRiassunto());

    UtenteFruitore utenteCreazione = new UtenteFruitore();
    utenteCreazione.setCodiceFiscale(pratica.getUtenteCreazionePratica());
    utenteCreazione.setCognome("Cognome");
    utenteCreazione.setNome("Nome");
    ritorno.setUtenteCreazione(utenteCreazione);

    ritorno.setDataCreazione(OffsetDateTime.now().minusHours(2));
    ritorno.setDataFine(OffsetDateTime.now().plusHours(5));
    ritorno.setDataCambioStato(OffsetDateTime.now());
    ritorno.setDataAggiornamento(OffsetDateTime.now());
    ritorno.setMetadati("{\"prova\":{\"esempio\":\"prova d'esempio\"}}");

    AttivitaFruitore attivita = new AttivitaFruitore();
    attivita.setNome("Nome attivita");
    attivita.setDescrizione("Descrizione attivita");

    AssegnazioneFruitore assegnazione = new AssegnazioneFruitore();
    assegnazione.setUtente(utenteCreazione);
    assegnazione.setGruppo("Gruppo assegnazione");
    attivita.setAssegnazione(Arrays.asList(assegnazione));

    MessaggioFruitore messaggio = new MessaggioFruitore();
    messaggio.setMessaggio("Messaggio");
    messaggio.setUtente(utenteCreazione);
    messaggio.setTimestamp(OffsetDateTime.now().minusHours(1));
    attivita.setMessaggiCollaboratori(Arrays.asList(messaggio));

    ritorno.setAttivita(Arrays.asList(attivita));

    MessaggioFruitore commento = new MessaggioFruitore();
    commento.setMessaggio("Commento");
    commento.setTimestamp(OffsetDateTime.now().minusHours(1).minusMinutes(10));
    commento.setUtente(utenteCreazione);
    ritorno.setCommenti(Arrays.asList(commento));

    DocumentoFruitore documento = new DocumentoFruitore();
    documento.setId("1");
    documento.setTitolo("Titolo documento");
    documento.setDescrizione("Descrizione documento");

    TipoDocumentoFruitore tipoDoc = new TipoDocumentoFruitore();
    tipoDoc.setCodice("Codice tipo doc");
    tipoDoc.setDescrizione("Descrizione tipo doc");
    documento.setTipo(tipoDoc);

    StatoDocumentoFruitore statoDoc = new StatoDocumentoFruitore();
    statoDoc.setCodice("Codice stato doc");
    statoDoc.setDescrizione("Descrizione stato doc");
    documento.setStato(statoDoc);

    documento.setAutore("DEMO 20");

    ritorno.setDocumentoPerSmistamento(documento);

    return ritorno;
  }

  private SmistaDocumentoResponse documentoSmistato() {

    SmistaDocumentoResponse output = new SmistaDocumentoResponse();
    output.setMessageUUID(UUID.randomUUID().toString());

    Result result = new Result();
    result.setCodice(Constants.ESITO_SMISTAMENTO_OK);
    result.setMessaggio(MESSAGGIO_SMISTAMENTO_OK);
    output.setResult(result);
    return output;

  }
}
