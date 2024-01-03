/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.service.impl;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioniMessaggiNotificheService;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class})
@Transactional
public class ConfigurazioneMessaggiNotificheServiceImplTest {

  @Autowired
  private ConfigurazioniMessaggiNotificheService service;

  @Test
  public void testGetConfigurazioniTipoPratica() {
    String filter =
        "{\"page\":0,\"size\":10,\"filter\":{\"codiceTipoPratica\":{\"eq\":\"TP1\"}}}";
    var result=service.getConfigurazioniMessaggi(filter);
    assertEquals(result.getConfigurazioniMessaggi().size(),2);
  }

  @Test
  public void testGetConfigurazioniEnte() {
    String filter =
        "{\"page\":0,\"size\":10,\"filter\":{\"idEnte\":{\"eq\": 1}}}";
    var result=service.getConfigurazioniMessaggi(filter);
    assertEquals(result.getConfigurazioniMessaggi().size(), 3);
  }
  @Test
  public void testGetConfigurazioniTipoPraticaEnteTipoMessaggio() {
    String filter =
        "{\"page\":0,\"size\":10,\"filter\":{\"codiceTipoPratica\":{\"eq\":\"TP1\"}, \"idEnte\":{\"eq\":\"1\"},\"codiceTipoMessaggio\":{\"eq\":\"INFO\"}}}";
    var result=service.getConfigurazioniMessaggi(filter);
    assertEquals(result.getConfigurazioniMessaggi().size(),1);
  }

  @Test
  public void testGetConfigurazioniTipoMessaggio() {
    String filter =
        "{\"page\":0,\"size\":10,\"fields\":\"id, tipo_messaggio, testo\",\"filter\":{\"codiceTipoMessaggio\":{\"eq\":\"INFO\"}}}";
    var result=service.getConfigurazioniMessaggi(filter);
    assertEquals(result.getConfigurazioniMessaggi().size(), 4);
  }

  @Test
  public void testGetConfigurazioniEmptyString() {
    var result = service.getConfigurazioniMessaggi("");
    assertEquals(result.getConfigurazioniMessaggi().size(), 5);
  }

  @Test
  public void testGetConfigurazioniNoFilter() {
    String filter = "{\"page\":0,\"size\":10,\"fields\":\"id, tipo_messaggio, testo\"}";
    var result = service.getConfigurazioniMessaggi(filter);
    assertEquals(result.getConfigurazioniMessaggi().size(), 5);
  }

  @Test
  public void testGetConfigurazioniId() {
    var result = service.getConfigurazioneMessaggioId(1L);
    assertEquals(result.getId().toString(), String.valueOf(1L));
    assertEquals(result.getTipoMessaggio().getCodice(), "INFO");
    assertEquals(result.getTipoPratica().getCodice(), "TP1");
  }

  @Test(expected = BadRequestException.class)
  public void testGetConfigurazioniIdNotFound() {
    service.getConfigurazioneMessaggioId(50L);
  }

  @Test
  public void testPostConfigurazioneMessaggioNotificaEnte() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test
  public void testPostConfigurazioneMessaggioNotificaEnte2() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(3L);
    request.setTesto("prova");

    service.creaConfigurazioneMessaggio(request);
  }


  @Test
  public void testPostConfigurazioneMessaggioNotificaEnteTipoPratica() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("TP3");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test
  public void testPostConfigurazioneMessaggioNotificaEnteTipoPratica2() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("COMMENTO");
    request.setIdEnte(1L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("TP2");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaUniqueException() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("TP1");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaUniqueException2() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(2L);
    request.setTesto("prova");

    service.creaConfigurazioneMessaggio(request);
  }


  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaWrongEnteTipoPratica() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(2L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("TP1");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaWrongEnte() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(50L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("TP1");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaWrongTipoPratica() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("prova");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test(expected = BadRequestException.class)
  public void testPostConfigurazioneMessaggioNotificaWrongTipoMessaggio() {
    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("prova");
    request.setIdEnte(1L);
    request.setTesto("prova");
    request.setCodiceTipoPratica("prova");

    service.creaConfigurazioneMessaggio(request);
  }

  @Test
  public void testPutConfigurazioneMessaggioNotifica() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova prova");

    var result = service.modificaConfigurazioneMessaggio(1L, request);
    assertEquals(result.getTipoPratica(), null);
    assertEquals(result.getId().toString(), String.valueOf(1L));
    assertEquals(result.getEnte().getId().toString(), String.valueOf(1L));
    assertEquals(result.getTipoMessaggio().getCodice(), "INFO");
    assertEquals(result.getTesto(), "prova prova");
  }

  @Test
  public void testPutConfigurazioneMessaggioNotifica2() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("COMMENTO");
    request.setIdEnte(2L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP7");

    var result = service.modificaConfigurazioneMessaggio(1L, request);
    assertEquals(result.getTipoPratica().getCodice(), "TP7");
    assertEquals(result.getId().toString(), String.valueOf(1L));
    assertEquals(result.getEnte().getId().toString(), String.valueOf(2L));
    assertEquals(result.getTipoMessaggio().getCodice(), "COMMENTO");
    assertEquals(result.getTesto(), "prova prova");
  }

  @Test
  public void testPutConfigurazioneMessaggioNotifica3() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("COMMENTO");
    request.setIdEnte(2L);
    request.setTesto("prova prova");

    var result = service.modificaConfigurazioneMessaggio(1L, request);
    assertEquals(result.getTipoPratica(), null);
    assertEquals(result.getId().toString(), String.valueOf(1L));
    assertEquals(result.getEnte().getId().toString(), String.valueOf(2L));
    assertEquals(result.getTipoMessaggio().getCodice(), "COMMENTO");
    assertEquals(result.getTesto(), "prova prova");
  }

  @Test
  public void testPutConfigurazioneMessaggioNotifica4() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP1");

    var result = service.modificaConfigurazioneMessaggio(1L, request);
    assertEquals(result.getTipoPratica().getCodice(), "TP1");
    assertEquals(result.getId().toString(), String.valueOf(1L));
    assertEquals(result.getEnte().getId().toString(), String.valueOf(1L));
    assertEquals(result.getTipoMessaggio().getCodice(), "INFO");
    assertEquals(result.getTesto(), "prova prova");
  }

  @Test
  public void testPutConfigurazioneMessaggioNotifica5() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("COMMENTO");
    request.setIdEnte(1L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP2");

    var result = service.modificaConfigurazioneMessaggio(2L, request);
    assertEquals(result.getTipoPratica().getCodice(), "TP2");
    assertEquals(result.getId().toString(), String.valueOf(2L));
    assertEquals(result.getEnte().getId().toString(), String.valueOf(1L));
    assertEquals(result.getTipoMessaggio().getCodice(), "COMMENTO");
    assertEquals(result.getTesto(), "prova prova");
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaUniqueException() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(2L);
    request.setTesto("prova prova");

    service.modificaConfigurazioneMessaggio(1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaUniqueException2() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(2L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP2");

    service.modificaConfigurazioneMessaggio(1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaUniqueException3() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("COMMENTO");
    request.setIdEnte(1L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP1");

    service.modificaConfigurazioneMessaggio(1L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaUniqueException4() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(2L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP7");

    service.modificaConfigurazioneMessaggio(5L, request);
  }


  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaEnteNotFound() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(50L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP7");

    service.modificaConfigurazioneMessaggio(5L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaTipoPraticaNotFound() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("INFO");
    request.setIdEnte(1L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("prova");

    service.modificaConfigurazioneMessaggio(5L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaTipoMessaggioNotFound() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("prova");
    request.setIdEnte(1L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP1");

    service.modificaConfigurazioneMessaggio(5L, request);
  }

  @Test(expected = BadRequestException.class)
  public void testPutConfigurazioneMessaggioNotificaWrongEnteTipoPratica() {

    var request = new ConfigurazioneMessaggioNotificaRequest();

    request.setCodiceTipoMessaggio("prova");
    request.setIdEnte(2L);
    request.setTesto("prova prova");
    request.setCodiceTipoPratica("TP1");

    service.modificaConfigurazioneMessaggio(1L, request);
  }

  @Test
  public void testDeleteConfigurazioneMessaggioNotifica() {
    service.eliminaConfigurazioneMessaggio(1L);
  }

  @Test(expected = BadRequestException.class)
  public void testDeleteConfigurazioneMessaggioNotificaNotFound() {
    service.eliminaConfigurazioneMessaggio(50L);
  }



}
