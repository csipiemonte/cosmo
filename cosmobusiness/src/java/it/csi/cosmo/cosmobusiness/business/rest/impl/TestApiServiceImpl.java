/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.TestApi;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackStatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.mapper.OperazioniAsincroneMapper;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;

public class TestApiServiceImpl extends ParentApiImpl implements TestApi {

  @Autowired
  private AsyncTaskService service;

  @Autowired
  protected OperazioniAsincroneMapper operazioniAsincroneMapper;

  @Autowired
  protected CallbackStatoPraticaService fruitoriCallbackService;

  @Override
  public Response testAsync() {
    var userOut = SecurityUtils.getUtenteCorrente().getCodiceFiscale();

    var result = service.start("operazione asincrona di test", task -> {
      task.info("outside you are " + userOut);
      task.info("in root you are " + SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      task.info("check preliminari ...");
      task.sleep(1000);
      task.step("first step", step -> {
        task.info("in step 1 you are " + SecurityUtils.getUtenteCorrente().getCodiceFiscale());
        task.sleep(1000);
        return 111;
      });
      task.sleep(10000);
      task.step("second step", step -> {
        task.info("in step 2 you are " + SecurityUtils.getUtenteCorrente().getCodiceFiscale());
        task.sleep(1000);

        step.step("second step random messages", substep -> {
          task.info("in step 2, substep 1 you are "
              + SecurityUtils.getUtenteCorrente().getCodiceFiscale());

          task.sleep(1000);
          return "step2MessagesResult";
        });
        return "step2result";
      });
      task.info(
          "in root at the end you are " + SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      return "123";
    });
    
    var output = new OperazioneAsincrona();
    output.setUuid(result.getTaskId());
    return Response.ok(output).build();
  }

  @Override
  public Response testCallback(Long idPratica) {
    var body = fruitoriCallbackService.getStatoPraticaPerCallback(idPratica);
    return Response.ok(body).build();
  }


  @Override
  public Response testCallbackSend(InviaCallbackStatoPraticaRequest request) {
    InvioCallbackResponse body = fruitoriCallbackService.postCallbackStatoPraticaInvia(request);
    return Response.status(201).entity(body).build();
  }
}
