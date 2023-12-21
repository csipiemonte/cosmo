/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.common.entities.CosmoTVariabile;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ProcessVariableEventService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTVariabileRepository;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

@Service
public class ProcessVariableEventServiceImpl implements ProcessVariableEventService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  CosmoTVariabileRepository cosmoTVariabileRepository;


  private static final String VARIABLE_CREATED = "variableCreated";
  private static final String VARIABLE_UPDATED_EVENT = "variableUpdated";
  private static final String VARIABLE_DELETED_EVENT = "variableDeleted";
  private static final String JSON = "json";

  private Map<String, Consumer<ProcessInstanceVariableEventDTO>> handlers;

  public ProcessVariableEventServiceImpl() {
    handlers = new HashMap<>();
    handlers.put(VARIABLE_CREATED, this::variableCreated);
    handlers.put(VARIABLE_UPDATED_EVENT, this::variableUpdated);
    handlers.put(VARIABLE_DELETED_EVENT, this::variableDeleted);

  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  @Override
  public void process(ProcessInstanceVariableEventDTO body) {
    final var method = "process";

    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getMessageType(), "messageType");

    if (StringUtils.isBlank(body.getMessageType())) {
      throw new BadRequestException("MessageType non specificato");
    }

    if (!handlers.containsKey(body.getMessageType())) {
      throw new BadRequestException("Tipologia di messaggio non gestita: " + body.getMessageType());
    }

    try {
      handlers.get(body.getMessageType()).accept(body);

    } catch (Throwable t) { // NOSONAR perche' la rilancio
      logger.error(method, "errore nella ricezione di una variabile di processo: " + t.getMessage());
      throw t;
    }
  }

  private void variableCreated(ProcessInstanceVariableEventDTO body) {

    ValidationUtils.require(body.getBusinessKey(), "businessKey");
    ValidationUtils.require(body.getVariableName(), "variableName");
    ValidationUtils.require(body.getVariableType(), "variableType");

    CosmoTVariabile variabileSaved =
        this.cosmoTVariabileRepository.findByNomeAndCosmoTPraticaIdAndDtCancellazioneIsNull(
            body.getVariableName(), Long.valueOf(body.getBusinessKey()))
        .orElse(null);

    CosmoTVariabile variabile = getCosmoTVariabileFromProcessInstanceVariableEventDTO(body);

    if (variabileSaved == null) {
      logger.info(VARIABLE_CREATED, String.format(ErrorMessages.V_VARIABILE_NON_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
    } else {
      logger.info(VARIABLE_CREATED, String.format(ErrorMessages.V_VARIABILE_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
      variabile.setId(variabileSaved.getId());
      variabile.setDtInserimento(variabileSaved.getDtInserimento());
      variabile.setUtenteInserimento(variabileSaved.getUtenteInserimento());
    }
    this.cosmoTVariabileRepository.save(variabile);
  }

  private void variableUpdated(ProcessInstanceVariableEventDTO body) {
    ValidationUtils.require(body.getBusinessKey(), "process.businessKey");
    ValidationUtils.require(body.getVariableName(), "variableName");

    CosmoTVariabile variabileSaved =
        this.cosmoTVariabileRepository.findByNomeAndCosmoTPraticaIdAndDtCancellazioneIsNull(
            body.getVariableName(), Long.valueOf(body.getBusinessKey()))
        .orElse(null);

    CosmoTVariabile variabile = getCosmoTVariabileFromProcessInstanceVariableEventDTO(body);
    if (variabileSaved == null) {
      logger.info(VARIABLE_UPDATED_EVENT, String.format(ErrorMessages.V_VARIABILE_NON_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
    } else {
      logger.info(VARIABLE_UPDATED_EVENT, String.format(ErrorMessages.V_VARIABILE_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
      variabile.setId(variabileSaved.getId());
      variabile.setDtInserimento(variabileSaved.getDtInserimento());
      variabile.setUtenteInserimento(variabileSaved.getUtenteInserimento());
    }
    this.cosmoTVariabileRepository.save(variabile);
  }

  private void variableDeleted(ProcessInstanceVariableEventDTO body) {
    ValidationUtils.require(body.getBusinessKey(), "process.businessKey");
    ValidationUtils.require(body.getVariableName(), "variableName");

    CosmoTVariabile variabileSaved = this.cosmoTVariabileRepository
        .findByNomeAndCosmoTPraticaIdAndDtCancellazioneIsNull(body.getVariableName(),
            Long.valueOf(body.getBusinessKey()))
        .orElse(null);

    if (variabileSaved == null) {
      logger.info(VARIABLE_DELETED_EVENT, String.format(ErrorMessages.V_VARIABILE_NON_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
      variabileSaved = getCosmoTVariabileFromProcessInstanceVariableEventDTO(body);
    } else {
      logger.info(VARIABLE_DELETED_EVENT, String.format(ErrorMessages.V_VARIABILE_PRESENTE,
          body.getVariableName(), body.getBusinessKey()));
    }
    variabileSaved.setDtCancellazione(Timestamp.from(Instant.now()));
    variabileSaved.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    this.cosmoTVariabileRepository.save(variabileSaved);

  }


  private CosmoTVariabile getCosmoTVariabileFromProcessInstanceVariableEventDTO(
      ProcessInstanceVariableEventDTO body) {
    CosmoTVariabile variabile = new CosmoTVariabile();
    variabile.setCosmoTPratica(
        this.cosmoTPraticaRepository.findOneNotDeleted(Long.valueOf(body.getBusinessKey()))
        .orElseThrow(() -> new NotFoundException(
            "Pratica con id " + body.getBusinessKey() + " non trovata")));

    variabile.setNome(body.getVariableName());
    variabile.setTypeName(body.getVariableType());
    variabile
    .setLongValue(body.getLongVariableValue() != null ? body.getLongVariableValue() : null);
    variabile.setBytearrayValue(
        body.getBytearrayVariableValue() != null ? body.getBytearrayVariableValue() : null);
    variabile.setDoubleValue(
        body.getDoubleVariableValue() != null ? body.getDoubleVariableValue() : null);
    if(JSON.equalsIgnoreCase(body.getVariableType())) {
      variabile.setJsonValue(body.getTextVariableValue() != null ? body.getTextVariableValue() : null);
    }else {
      variabile.setTextValue(body.getTextVariableValue() != null ? body.getTextVariableValue() : null);
    }
    return variabile;
  }
}