/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.CallbackDelibereService;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackAggiornamentoDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackCreazioneDeliberaRequest;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class CallbackDelibereServiceImplTest {
  
  @Autowired
  private CallbackDelibereService callbackDelibereService;
  
  @Test(expected = NotFoundException.class)
  public void postCallbackCreazioneDeliberaInviaPraticaNotFound() {
    InviaCallbackCreazioneDeliberaRequest body = new InviaCallbackCreazioneDeliberaRequest();
    body.setIdPratica(111L);
    callbackDelibereService.postCallbackCreazioneDeliberaInvia(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void postCallbackCreazioneDeliberaInviaPraticaConFruitoreNull() {
    InviaCallbackCreazioneDeliberaRequest body = new InviaCallbackCreazioneDeliberaRequest();
    body.setIdPratica(12L);
    callbackDelibereService.postCallbackCreazioneDeliberaInvia(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void postCallbackCreazioneDeliberaInvia() {
    InviaCallbackCreazioneDeliberaRequest body = new InviaCallbackCreazioneDeliberaRequest();
    body.setIdPratica(1L);
    callbackDelibereService.postCallbackCreazioneDeliberaInvia(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void postCallbackCreazioneDeliberaSchedula() {
    InviaCallbackCreazioneDeliberaRequest body = new InviaCallbackCreazioneDeliberaRequest();
    body.setIdPratica(1L);
    callbackDelibereService.postCallbackCreazioneDeliberaSchedula(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void postCallbackAggiornamentoDeliberaInvia() {
    InviaCallbackAggiornamentoDeliberaRequest body = new InviaCallbackAggiornamentoDeliberaRequest();
    body.setIdPratica(1L);
    body.setKey("key");
    callbackDelibereService.postCallbackAggiornamentoDeliberaInvia(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void postCallbackAggiornamentoDeliberaSchedula() {
    InviaCallbackAggiornamentoDeliberaRequest body = new InviaCallbackAggiornamentoDeliberaRequest();
    body.setIdPratica(1L);
    body.setKey("key");
    callbackDelibereService.postCallbackAggiornamentoDeliberaSchedula(body);
  }
  
  
}