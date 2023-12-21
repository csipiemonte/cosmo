/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Supplier;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UseCaseDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmobusiness.business.rest.impl.PraticheApiServiceImpl;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.test.cosmo.cosmobusiness.business.CreaPraticaIntegrationTest.CreaPraticaIntegrationTestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmobusiness.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.PraticaTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.UtenteTestbedService;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class, CreaPraticaIntegrationTestConfig.class})
@Transactional()
public class CreaPraticaIntegrationTest extends ParentIntegrationTest {

  @Configuration
  public static class CreaPraticaIntegrationTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoPraticheFeignClient cosmoPraticheFeignClient() {
      return Mockito.mock(CosmoPraticheFeignClient.class);
    }
  }

  @Autowired
  private EnteTestbedService enteTestbedService;

  @Autowired
  private UtenteTestbedService utenteTestbedService;

  @Autowired
  private CosmoCmmnFeignClient mockCosmoCmmnFeignClient;

  @Autowired
  private CosmoPraticheFeignClient mockCosmoPraticheFeignClient;

  @Autowired
  private PraticaTestbedService praticaTestbedService;

  private CosmoTEnte ente;

  private CosmoTUtente utente;

  private CosmoDTipoPratica tipoPratica;

  @Override
  protected void autentica() {

    utente = utenteTestbedService.creaUtente();

    ente = enteTestbedService.creaEnte();

    tipoPratica = praticaTestbedService.creaTipoPratica(ente);

    utenteTestbedService.associaAdEnte(utente, ente);

    //@formatter:off
    var user = UserInfoDTO.builder()
        .withNome(utente.getNome())
        .withCognome(utente.getCognome())
        .withCodiceFiscale(utente.getCodiceFiscale())
        .withEnte(EnteDTO.builder()
            .withId(ente.getId())
            .withNome(ente.getNome())
            .withTenantId(ente.getCodiceIpa())
            .build())
        .withProfilo(ProfiloDTO.builder()
            .withCodice("P" + ente.getId())
            .withDescrizione("Profilo Unit Test")
            .withUseCases(Arrays.asList(
                UseCaseDTO.builder().withCodice("UC0").build()
                ))
            .build())
        .withAnonimo(false).build();
    //@formatter:on

    autentica(user, null);

    resetMock();
  }

  Supplier<CreaPraticaRequest> requestBuilder = () -> {
    var body = new CreaPraticaRequest();
    body.setCodiceTipo(tipoPratica.getCodice());
    body.setOggetto("[IntegrationTest] Oggetto " + UUID.randomUUID().toString());
    body.setRiassunto("Riassunto della pratica " + UUID.randomUUID().toString()
        + " generato da integration test");
    return body;
  };

  private void resetMock() {
    reset(mockCosmoCmmnFeignClient);
    reset(mockCosmoPraticheFeignClient);

    var praticaDB = praticaTestbedService.creaPratica(ente, null, tipoPratica);

    var praticaCreata = new it.csi.cosmo.cosmopratiche.dto.rest.Pratica();
    praticaCreata.setId(praticaDB.getId().intValue());

    when(mockCosmoPraticheFeignClient.postPratiche(any())).thenReturn(praticaCreata);

    var processoCreato = new ProcessInstanceResponse();
    processoCreato.setId("1892352");
    when(mockCosmoCmmnFeignClient.postProcessInstance(any())).thenReturn(processoCreato);
  }

  @Test
  public void shouldUpdateEventData() {
    resetMock();

    var api = buildResource(PraticheApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    var response = api.postPratiche(requestBody, null);
    assertNotNull(response);

    var responseEntity = (OperazioneAsincrona) response.getEntity();
    assertNotNull(responseEntity.getUuid());
    /*
     * verify(mockCosmoPraticheFeignClient, times(1)) .postPratiche(argThat(new
     * LambdaMatcher<>(calledWith ->
     *
     * Objects.equals(requestBody.getCodiceTipo(), calledWith.getCodiceTipologia()) )));
     */
  }

}
