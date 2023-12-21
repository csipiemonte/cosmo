/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/

package it.csi.test.cosmo.cosmobusiness.business;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.CallbackStatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class CallbackStatoPraticaServiceImplTest {

  @Autowired
  private CallbackStatoPraticaService callbackStatoPraticaService;

  @Ignore("da rivedere")
  @Test
  public void getStatoPratica() {
    callbackStatoPraticaService.getStatoPratica(2L);
  }

  @Test(expected = NotFoundException.class)
  public void getStatoPraticaNotFound() {
    callbackStatoPraticaService.getStatoPratica(111L);
  }

  @Test(expected = NotFoundException.class)
  public void postCallbackStatoPraticaInviaPraticaConFruitoreNull() {
    InviaCallbackStatoPraticaRequest body = new InviaCallbackStatoPraticaRequest();
    body.setIdPratica(4L);
    callbackStatoPraticaService.postCallbackStatoPraticaInvia(body);
  }

  @Test(expected = InternalServerException.class)
  public void postCallbackStatoPraticaInvia() {
    InviaCallbackStatoPraticaRequest body = new InviaCallbackStatoPraticaRequest();
    body.setIdPratica(2L);
    callbackStatoPraticaService.postCallbackStatoPraticaInvia(body);
  }

  @Test(expected = InternalServerException.class)
  public void postCallbackStatoPraticaSchedula() {
    SchedulaCallbackStatoPraticaRequest body = new SchedulaCallbackStatoPraticaRequest();
    body.setIdPratica(2L);
    callbackStatoPraticaService.postCallbackStatoPraticaSchedula(body);
  }
}