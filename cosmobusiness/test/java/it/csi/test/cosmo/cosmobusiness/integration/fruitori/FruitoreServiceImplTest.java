/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.cosmobusiness.business.service.FruitoriService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreVariabileRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnAsyncFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, FruitoreServiceImplTest.FruitoreServiceTestConfig.class})
@Transactional
public class FruitoreServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class FruitoreServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoCmmnAsyncFeignClient cosmoCmmnAsyncFeignClient() {
      return Mockito.mock(CosmoCmmnAsyncFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;
  
  @Autowired
  private CosmoCmmnAsyncFeignClient cosmoCmmnAsyncFeignClient;
  
  @Autowired
  private FruitoriService fruitoriService;
  
  private static final String CODICE_SEGNALE = "codiceSegnale";
  private static final String R_PIEMON = "r_piemon";
  private static final String STATO = "stato";
  private static final String SYSTEM = "System";
  private static final String OGGETTO = "oggetto";
  private static final String PROVA = "PROVA";
  private static final String RIASSUNTO = "Riassunto";
  private static final String CODICE_GRUPPO = "DEMO2021";
  private static final String LINK_ATTIVITA = "tasks/877505";
  private static final String NOME_ATTIVITA = "Richiesta di collaborazione";
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest(),TestConstants.buildClientAutenticatoTest());
  }
  
  @Test(expected = NotFoundException.class)
  public void getPraticaNotFound() {
    fruitoriService.getPratica("3");
  }
  
  @Test
  public void getPraticaErroreInvioSegnale() {
    fruitoriService.getPratica("5");
  }
  
  @Test(expected = FeignClientClientErrorException.class)
  public void inviaSegnaleProcessoFeignClientClientErrorException() {
    setupMockInviaSegnaleFeignClientClientErrorException();
    InviaSegnaleFruitoreRequest request = new InviaSegnaleFruitoreRequest();
    request.setCodiceSegnale(CODICE_SEGNALE);
    List<InviaSegnaleFruitoreVariabileRequest> variabili = new ArrayList<>();
    InviaSegnaleFruitoreVariabileRequest variabile = new InviaSegnaleFruitoreVariabileRequest();
    variabili.add(variabile);
    request.setVariabili(variabili);
    fruitoriService.inviaSegnaleProcesso("5", request);
  }
  
  @Test(expected = FeignClientServerErrorException.class)
  public void inviaSegnaleProcessoFeignClientServerErrorException() {
    setupMockInviaSegnaleFeignClientServerErrorException();
    InviaSegnaleFruitoreRequest request = new InviaSegnaleFruitoreRequest();
    request.setCodiceSegnale(CODICE_SEGNALE);
    List<InviaSegnaleFruitoreVariabileRequest> variabili = new ArrayList<>();
    request.setVariabili(variabili);
    fruitoriService.inviaSegnaleProcesso("5", request);
  }
  
  @Test(expected = InternalServerException.class)
  public void inviaSegnaleProcessoInternalServerException() {
    setupMockInviaSegnaleInternalServerException();
    InviaSegnaleFruitoreRequest request = new InviaSegnaleFruitoreRequest();
    request.setCodiceSegnale(CODICE_SEGNALE);
    fruitoriService.inviaSegnaleProcesso("5", request);
  }
  
  @Test
  public void inviaSegnaleProcesso() {
    setupMockInviaSegnale();
    InviaSegnaleFruitoreRequest request = new InviaSegnaleFruitoreRequest();
    request.setCodiceSegnale(CODICE_SEGNALE);
    fruitoriService.inviaSegnaleProcesso("5", request);
  }
  
  @Test(expected = ConflictException.class)
  public void postPraticheEsterneEsistente() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("5");
    body.setTipoPratica(PROVA);
    body.setStato(STATO);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheEsterneTipoPraticaNotFound() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP1111");
    body.setStato(STATO);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheEsterneTipoPraticaNotCreabileDaServizio() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP6");
    body.setStato(STATO);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheEsterneTipoPraticaNotDisponibilePerEnte() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP7");
    body.setStato(STATO);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheEsterneAssegnazioneGruppoEUtente() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    body.setRiassunto(RIASSUNTO);
    
    List<CreaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    CreaPraticaEsternaAttivitaFruitoreRequest request = new CreaPraticaEsternaAttivitaFruitoreRequest();
    
    List<CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazione.setUtente("AAAAAA00B77B000F");
    assegnazione.setGruppo(CODICE_GRUPPO);
    assegnazioni.add(assegnazione);
    request.setAssegnazione(assegnazioni);
    
    attivita.add(request);
    body.setAttivita(attivita);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test
  public void postPraticheEsterneAssegnazioneUtente() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    body.setRiassunto(RIASSUNTO);
    
    List<CreaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    CreaPraticaEsternaAttivitaFruitoreRequest request = new CreaPraticaEsternaAttivitaFruitoreRequest();
    
    List<CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazioni.add(assegnazione);
    assegnazione.setUtente("AAAAAA00B77B000F");
    request.setAssegnazione(assegnazioni);
    
    attivita.add(request);
    body.setAttivita(attivita);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test
  public void postPraticheEsterneAssegnazioneUtenteNonTrovato() {
    CreaPraticaEsternaFruitoreRequest body = new CreaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setIdPraticaExt("54");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    body.setUtenteCreazionePratica(SYSTEM);
    body.setLinkPratica("link");
    body.setOggetto(OGGETTO);
    body.setRiassunto(RIASSUNTO);
    
    List<CreaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    CreaPraticaEsternaAttivitaFruitoreRequest request = new CreaPraticaEsternaAttivitaFruitoreRequest();
    
    List<CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazioni.add(assegnazione);
    assegnazione.setUtente("AAAABA00B77B000F");
    request.setAssegnazione(assegnazioni);
    
    attivita.add(request);
    body.setAttivita(attivita);
    fruitoriService.postPraticheEsterne(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void putPraticheEsternePraticaNotFound() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    fruitoriService.putPraticheEsterne("15", body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putPraticheEsterneTipoPraticaNotFound() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP1111");
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putPraticheEsterneTipoPraticaNonCreabileDaServizio() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP6");
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putPraticheEsterneTipoPraticaNonDisponibilePerEnte() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP7");
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test
  public void putPraticheEsterneTerminaPratica() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    body.setDataFinePratica(OffsetDateTime.now());
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test
  public void putPraticheEsterneConAssegnazioniDaCancellare() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    
    List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    AggiornaPraticaEsternaAttivitaFruitoreRequest request = new AggiornaPraticaEsternaAttivitaFruitoreRequest();
    
    List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazioni.add(assegnazione);
    assegnazione.setUtente("AAAABA00B77B000F");
    request.setAssegnazione(assegnazioni);
    
    attivita.add(request);
    body.setAttivita(attivita);
    
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test(expected = BadRequestException.class)
  public void putPraticheEsterneSenzaUtenteOGruppo() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    
    List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    AggiornaPraticaEsternaAttivitaFruitoreRequest request = new AggiornaPraticaEsternaAttivitaFruitoreRequest();
    
    List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazioni.add(assegnazione);
    request.setAssegnazione(assegnazioni);
    request.setLinkAttivita(LINK_ATTIVITA);
    request.setNome(NOME_ATTIVITA);
    
    attivita.add(request);
    body.setAttivita(attivita);
    
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test
  public void putPraticheEsterneAssegnazioneGruppo() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    body.setRiassunto(RIASSUNTO);
    body.setLinkPratica("link");
    body.setTipoPratica("TP1");
    body.setStato(PROVA);
    
    List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    AggiornaPraticaEsternaAttivitaFruitoreRequest request = new AggiornaPraticaEsternaAttivitaFruitoreRequest();
    
    List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazione.setGruppo(CODICE_GRUPPO);
    assegnazioni.add(assegnazione);
    request.setAssegnazione(assegnazioni);
    request.setLinkAttivita(LINK_ATTIVITA);
    request.setNome(NOME_ATTIVITA);
    
    attivita.add(request);
    body.setAttivita(attivita);
    
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test
  public void putPraticheEsterne() {
    AggiornaPraticaEsternaFruitoreRequest body = new AggiornaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    
    List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
    AggiornaPraticaEsternaAttivitaFruitoreRequest request = new AggiornaPraticaEsternaAttivitaFruitoreRequest();
    
    List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioni = new ArrayList<>();
    AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazione = new AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
    assegnazione.setGruppo(CODICE_GRUPPO);
    assegnazioni.add(assegnazione);
    request.setAssegnazione(assegnazioni);
    request.setLinkAttivita(LINK_ATTIVITA);
    request.setNome(NOME_ATTIVITA);
    
    attivita.add(request);
    body.setAttivita(attivita);
    
    fruitoriService.putPraticheEsterne("1", body);
  }
  
  @Test(expected = NotFoundException.class)
  public void deletePraticheEsternePraticaNotFound() {
    EliminaPraticaEsternaFruitoreRequest body = new EliminaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    fruitoriService.deletePraticheEsterne("15", body);
  }
  
  @Test
  public void deletePraticheEsterne() {
    EliminaPraticaEsternaFruitoreRequest body = new EliminaPraticaEsternaFruitoreRequest();
    body.setCodiceIpaEnte(R_PIEMON);
    fruitoriService.deletePraticheEsterne("1", body);
  }
  
  @Test(expected = InternalServerException.class)
  public void getEndpointNotFound() {
    fruitoriService.getEndpoint(OperazioneFruitore.CUSTOM , 1L, "codice");
  }
  
  @Test
  public void getEndpointConCodice() {
    fruitoriService.getEndpoint(OperazioneFruitore.CUSTOM , 1L, "Codice descrittivo");
  }
  
  @Test
  public void getEndpointConCodiceNull() {
    fruitoriService.getEndpoint(OperazioneFruitore.CUSTOM , 2L, null);
  }
  
  private void setupMockInviaSegnaleInternalServerException() {
    reset(cosmoCmmnAsyncFeignClient);
    RuntimeException exception = new RuntimeException();
    when(cosmoCmmnAsyncFeignClient.inviaSegnaleProcesso(any(), any())).thenThrow(exception);
  }

  private void setupMockInviaSegnaleFeignClientServerErrorException() {
    reset(cosmoCmmnAsyncFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientServerErrorException exception = new FeignClientServerErrorException(null, new HttpServerErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnAsyncFeignClient.inviaSegnaleProcesso(any(), any())).thenThrow(exception);
  }

  private void setupMockInviaSegnaleFeignClientClientErrorException() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.putProcessInstanceVariables(any(), any())).thenReturn(null);
    
    reset(cosmoCmmnAsyncFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientClientErrorException exception = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnAsyncFeignClient.inviaSegnaleProcesso(any(), any())).thenThrow(exception);
  }
  
  private void setupMockInviaSegnale() {
    reset(cosmoCmmnAsyncFeignClient);
    RiferimentoOperazioneAsincrona operazione = new RiferimentoOperazioneAsincrona();
    when(cosmoCmmnAsyncFeignClient.inviaSegnaleProcesso(any(), any())).thenReturn(operazione);
  }
}