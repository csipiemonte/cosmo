/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;


@Service
@Transactional
public class StatoPraticaServiceImpl extends StatoPraticaWrapper implements StatoPraticaService {

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Override
  public AggiornaStatoPraticaRequest getPratica(CosmoTPratica pratica) {
    var ret = fetchPratica(pratica);
    enrichUtentiPratica(ret);
    return ret;
  }

  @Override
  public List<AttivitaFruitore> getAttivita(CosmoTPratica pratica) {
    var ret = fetchAttivita(pratica);
    enrichUtentiAttivita(ret);
    return ret;
  }

  @Override
  public List<DocumentoFruitore> getDocumenti(CosmoTPratica pratica) {
    return fetchDocumenti(pratica);
  }


  @Override
  public List<MessaggioFruitore> getMessaggi(CosmoTPratica pratica) {
    var ret = fetchMessaggiPratica(pratica.getProcessInstanceId());
    enrichUtentiMessaggi(ret);
    return ret;
  }

  @Override
  public Map<String, Object> getVariabiliProcesso(CosmoTPratica pratica) {

    return getVariabiliProcessoByProcessInstanceId((pratica.getProcessInstanceId()));
  }

  @Override
  public Map<String, Object> getVariabiliProcessoByProcessInstanceId(String pid) {
    ValidationUtils.require(pid, "processInstanceId");

    // RestVariable[] rawVariables = cosmoCmmnFeignClient.getProcessInstanceVariables(pid);
    var response = cosmoCmmnFeignClient.getHistoricProcessInstances(pid, true);
    List<RestVariable> rawVariables = response.getData().get(0).getVariables();

    Map<String, Object> output = new HashMap<>();
    for (var variable : rawVariables) {
      output.put(variable.getName(), variable.getValue());
    }

    return output;
  }
}