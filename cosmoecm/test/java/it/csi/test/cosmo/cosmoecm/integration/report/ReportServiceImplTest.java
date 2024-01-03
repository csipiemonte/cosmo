/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmoecm.integration.report;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
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
import org.springframework.util.Assert;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmoecm.business.service.ReportService;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.test.cosmo.cosmoecm.integration.report.ReportServiceImplTest.ReportServiceImplTestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestDB;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestDB.class, ReportServiceImplTestConfig.class})
@Transactional
public class ReportServiceImplTest extends ParentIntegrationTest {

  private static final boolean OPEN_RESULTS = true;

  protected static CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "ReportService");

  @Configuration
  public static class ReportServiceImplTestConfig {

    @Bean
    @Primary
    public CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient() {
      return Mockito.mock(CosmoBusinessPraticheFeignClient.class);
    }
  }

  @Autowired
  private ReportService service;

  @Autowired
  private CosmoBusinessPraticheFeignClient mockCosmoBusinessPraticheFeignClient;

  @Test
  public void serviceShouldExist() {
    Assert.notNull(service, "service should exist");
  }

  @Test
  public void testReportGeneration() throws IOException {
    reset(mockCosmoBusinessPraticheFeignClient);

    Collection<AttivitaFruitore> dataBeans = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      AttivitaFruitore att1 = new AttivitaFruitore();
      att1.setNome("Nome attivita " + i);
      att1.setDescrizione("Descrizione attivita " + i);
      dataBeans.add(att1);
    }

    Map<String, Object> outputElaborazione = new HashMap<>();
    outputElaborazione.put("attivita", dataBeans);

    when(mockCosmoBusinessPraticheFeignClient.postPraticheIdElaborazione(any(), any()))
        .thenReturn(outputElaborazione);

    var req = new RichiediGenerazioneReportRequest();
    req.setAutore("INTEGRATION TEST");
    req.setTitolo("template di test");
    req.setCodiceTemplate("TEST0");
    req.setFormato("pdf");
    req.setIdPratica(1L);
    req.setMappaturaParametri("{\"attivita\" : .pratica.attivita }");
    req.setNomeFile("test.pdf");
    req.setSovrascrivi(false);

    byte[] pdf = service.generaReportDaProcessoInMemory(req).getCompilato();
    assertTrue(pdf.length > 0);
    log.info("test", "generato pdf: " + pdf.length + " bytes");

    // decomment to see the file:
    if (OPEN_RESULTS) {
      File file = new java.io.File("C:\\UPAP\\test.pdf");
      FileUtils.writeByteArrayToFile(file, pdf);
      Desktop.getDesktop().open(file);
    }
  }


}
