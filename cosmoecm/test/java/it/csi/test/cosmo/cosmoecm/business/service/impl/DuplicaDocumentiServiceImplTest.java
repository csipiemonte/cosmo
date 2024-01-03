/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.DuplicaDocumentiService;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioneDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.test.cosmo.cosmoecm.business.service.impl.DuplicaDocumentiServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmoecm.testbed.service.PraticaTestbedService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class DuplicaDocumentiServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private DuplicaDocumentiService duplicaDocumentiService;

  @Autowired
  private PraticaTestbedService praticaTestbedService;

  @Autowired
  private CosmoSoapIndexFeignClient mockCosmoSoapIndexFeignClient;

  private CosmoTPratica pratica;

  private SharedLink sharedLink = new SharedLink();

  private Folder folder = new Folder();

  private Entity entity = new Entity();

  @PersistenceContext
  EntityManager em;

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient indexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

  }

  @Before
  public void autentica() throws IOException {
    pratica = praticaTestbedService.salvaPratica();
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test(expected = BadRequestException.class)
  public void preparaDuplicazioneErrorePraticaNonTrovata() {
    duplicaDocumentiService.preparaDuplicazione(1L, 0L, true);
  }

  @Test
  public void preparaDuplicazioneOK() {
    setUpMock();
    duplicaDocumentiService.preparaDuplicazione(1L, 2L, true);
  }

  @Test
  public void preparaDuplicazioneOKNessunDocumentoDaDuplicare() {
    setUpMock();
    duplicaDocumentiService.preparaDuplicazione(pratica.getId(), 2L, true);
  }

  @Test(expected = UnexpectedResponseException.class)
  public void preparaDuplicazioneVerificaNodoPraticaKOEnteCodiceIpaVuoto() {
    em.refresh(pratica);
    pratica.getEnte().setCodiceIpa("");
    duplicaDocumentiService.preparaDuplicazione(1L, pratica.getId(), true);
  }

  @Test(expected = UnexpectedResponseException.class)
  public void preparaDuplicazioneVerificaNodoPraticaKODataCreazionePraticaNulla() {
    em.refresh(pratica);
    pratica.setDataCreazionePratica(null);
    duplicaDocumentiService.preparaDuplicazione(1L, pratica.getId(), true);
  }

  @Test(expected = BadRequestException.class)
  public void duplicaDocumentiErroreDocumentoDaNonEsistente() {
    setUpMock();
    duplicaDocumentiService.duplicaDocumenti(123L, 1L);
  }

  @Test
  public void duplicaDocumenti() {
    setUpMock();
    var result = duplicaDocumentiService.duplicaDocumenti(1L, 2L);
    assertNotNull(result);
  }

  @Test(expected = NotFoundException.class)
  public void duplicaDocumentoErroreDocumentoInesistente() {
    duplicaDocumentiService.duplicaDocumento(0L, 0L, null);
  }

  @Test(expected = BadRequestException.class)
  public void duplicaDocumentoErrorePraticaInesistente() {
    duplicaDocumentiService.duplicaDocumento(123L, 1L, null);
  }

  @Test
  public void duplicaDocumentoOKNessunContenutoDaDuplicare() {
    var relazioni = new RelazioniDocumentoDuplicato();
    var relazione = new RelazioneDocumentoDuplicato();
    relazione.setIdDocumentoDaDuplicare(1L);
    relazioni.setRelazioneDocumenti(List.of(relazione));
    var res = duplicaDocumentiService.duplicaDocumento(1L, 11L, relazioni);
    assertTrue(res.equals(relazioni));
  }

  private void setUpMock() {
    this.sharedLink.setDownloadUri("downloadUri");
    this.folder.setEffectivePath("effectivePath");
    this.folder.setUid("uid");
    this.entity.setUid("uid");
    reset(mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn("uuid");
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(this.sharedLink);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(this.folder);
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(this.entity);
    when(mockCosmoSoapIndexFeignClient.creaFile(any(), any())).thenReturn(this.entity);
  }


}
