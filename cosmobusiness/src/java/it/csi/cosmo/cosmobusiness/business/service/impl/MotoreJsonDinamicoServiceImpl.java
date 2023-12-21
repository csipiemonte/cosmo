/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.integration.jslt.VirtualJsonNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

@Service
public class MotoreJsonDinamicoServiceImpl implements MotoreJsonDinamicoService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Override
  public JsonNode eseguiMappatura(String specificaMappatura, Object sorgente) {

    final var method = "eseguiMappatura";

    JsonNode nodoInput;

    // verifico il tipo del sorgente
    if (!(sorgente instanceof JsonNode)) {
      nodoInput = VirtualJsonNode.toVirtualJsonNode(sorgente);
    } else {
      nodoInput = (JsonNode) sorgente;
    }

    // costruisco la specifica di trasformazione
    Expression expr;

    if (logger.isDebugEnabled()) {
      logger.debug(method, "costruisco specifica di trasformazione da parametro: {}",
          specificaMappatura);
      try {
        logger.debug(method, "costruisco specifica di trasformazione su input: {}", ObjectUtils
            .getDataMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sorgente));
      } catch (JsonProcessingException e) {
        logger.debug(method,
            "costruisco specifica di trasformazione su input: ??? " + e.getMessage());
      }
    }

    try {
      expr = Parser.compileString(specificaMappatura, SandboxFactory.buildExtensionFunctions());
    } catch (Exception e) {
      logger.error(method, "errore nel parsing della specifica di mappatura", e);
      throw new InternalServerException(
          "Specifica di mappatura non valorizzata correttamente: " + e.getMessage(), e);
    }

    // applica la trasformazione
    JsonNode compiled;

    try {
      compiled = expr.apply(nodoInput);
    } catch (Exception e) {
      logger.error(method, "errore nel parsing della specifica di mappatura", e);
      throw new InternalServerException("Errore nell'esecuzione della mappatura: " + e.getMessage(),
          e);
    }

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato trasformazione: {}", compiled.toString());
    }

    return compiled;
  }

}
