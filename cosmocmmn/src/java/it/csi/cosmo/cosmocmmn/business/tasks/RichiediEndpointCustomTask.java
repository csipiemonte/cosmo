/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbacksRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessCustomCallbackFeignClient;


public class RichiediEndpointCustomTask extends ParentFlowableTask {

  /**
   * parametro fornito in input da process design
   */
  private Expression endpoint;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    try {
      CustomCallbacksRequest params =
          ObjectUtils.getDataMapper().readValue(((String) endpoint.getValue(execution)),
              CustomCallbacksRequest.class);

      if (!params.isEmpty()) {

        CosmoBusinessCustomCallbackFeignClient feignClient =
            this.getBean(CosmoBusinessCustomCallbackFeignClient.class);

        params.forEach(param -> {
          CustomCallbackResponse result =
              feignClient.getCustomCallback(param.getApiManagerId(), param.getCodiceDescrittivo(),
                  execution.getProcessInstanceId());

          execution.setVariable(param.getNomeVariabile(),
              ObjectUtils.getDataMapper().convertValue(result, JsonNode.class));
        });
      }


    } catch (JsonProcessingException e) {
      logger.error(method, "Errore nel parsing del json", e);
      throw new BadRequestException("Errore nel parsing del json");
    }
  }

}
