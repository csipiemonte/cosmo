/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.CustomCallbackService;
import it.csi.cosmo.cosmobusiness.business.service.FruitoriService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbackResponse;
import it.csi.cosmo.cosmobusiness.integration.mapper.CallbackFruitoriMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class CustomCallbackServiceImpl implements CustomCallbackService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private FruitoriService fruitoriService;

  @Autowired
  private CosmoTFruitoreRepository fruitoreRepository;

  @Autowired
  private CallbackFruitoriMapper callbackFruitoriMapper;

  @Autowired
  private CosmoCmmnFeignClient cmmnFeignClient;
  @Override
  public CustomCallbackResponse getCustomEndopoint(String apiManagerId, String codiceDescrittivo,
      String processInstanceId) {
    String methodName = "getCustomEndopoint";

    ValidationUtils.require(apiManagerId, "apiManagerId del fruitore");
    ValidationUtils.require(codiceDescrittivo, "codice indentificativo dell'endpoint custom");

    CosmoTFruitore fruitore =
        fruitoreRepository.findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, apiManagerId)
        .orElseThrow(() -> {
          String errorMessage =
              String.format(ErrorMessages.FRUITORE_NON_TROVATO, apiManagerId);
          logger.error(methodName, errorMessage);
          throw new NotFoundException(errorMessage);
        });

    if (fruitore.getUrl() == null || fruitore.getUrl().isBlank()) {
      String errorMessage = String.format(ErrorMessages.URL_FRUITORE_NON_DEFINITA, apiManagerId);
      logger.error(methodName, errorMessage);
      throw new BadRequestException(errorMessage);
    }

    CosmoTEndpointFruitore endpoint = fruitoriService.getEndpoint(OperazioneFruitore.CUSTOM,
        fruitore.getId(), codiceDescrittivo);

    CustomCallbackResponse output = new CustomCallbackResponse();
    output
    .setSchemaAutenticazione(callbackFruitoriMapper.toDTO(endpoint.getSchemaAutenticazione()));
    output.setCodiceDescrittivo(endpoint.getCodiceDescrittivo());
    output.setMetodoHttp(endpoint.getMetodoHttp());
    output.setCodiceTipoEndpoint(endpoint.getCodiceTipoEndpoint());
    output.setUrl(setUrl(processInstanceId, fruitore.getUrl() + endpoint.getEndpoint()));

    return output;

  }

  private String setUrl(String processInstanceId, String url) {

    if (processInstanceId == null || processInstanceId.isBlank()) {
      return url;
    }

    Matcher m = Pattern.compile("\\{\\$(.*?)\\$\\}").matcher(url);

    while (m.find()) {
      String variabile = m.group(1);

      RestVariable variable =
          cmmnFeignClient.getProcessInstanceVariable(processInstanceId, variabile);
      if (variable != null && variable.getValue() != null) {
        url = url.replace("{$" + variabile + "$}", variable.getValue().toString());
      } else {
        String errorMessage =
            String.format(ErrorMessages.VALORE_VARIABILE_NON_TROVATO, variabile);
        logger.error("setUrl", errorMessage);
        throw new NotFoundException(errorMessage);
      }
    }

    return url;
  }
}
