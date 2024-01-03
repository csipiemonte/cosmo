/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneMetadati;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneMetadati_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;
import it.csi.cosmo.cosmopratiche.business.service.MetadatiService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoCConfigurazioneMetadatiRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class MetadatiServiceImpl implements MetadatiService {

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, "MetadatiServiceImpl");

  @Autowired
  private CosmoTPraticaRepository praticaRepository;

  @Autowired
  private CosmoCConfigurazioneMetadatiRepository configurazioneMetadatiRepository;

  @Autowired
  CosmoTPraticheMapper mapper;

  @Autowired
  private CosmoBusinessPraticheFeignClient businessPraticheFeignClient;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Pratica aggiornaMetadatiPratica(String idPratica) {
    final String methodName = "putMetadatiIdPratica";

    // seleziono la pratica richiesta
    CosmoTPratica pratica = getPratica(idPratica);

    if (StringUtils.isNotBlank(pratica.getMetadati())) {

      if (pratica.getTipo() == null) {
        String messageError =
            String.format(ErrorMessages.TIPO_PRATICA_NON_TROVATA, pratica.getTipo());
        logger.error(methodName, messageError);
        throw new NotFoundException(messageError);
      }

      // prendo le configurazioni dei metadati per il tipo di pratica
      List<CosmoCConfigurazioneMetadati> configurazioneMetadati =
          configurazioneMetadatiRepository.findActiveByField(
              CosmoCConfigurazioneMetadati_.cosmoDTipoPratica, pratica.getTipo());

      if (configurazioneMetadati == null || configurazioneMetadati.isEmpty()) {
        String messageError =
            String.format(ErrorMessages.CONFIGURAZIONE_METADATI_PER_TIPO_PRATICA_NON_TROVATA,
                pratica.getTipo().getCodice());
        logger.error(methodName, messageError);
        throw new NotFoundException(messageError);
      }

      // prendo le variabili di processo associate alla pratica
      VariabiliProcessoResponse variabiliProcessoResponse = businessPraticheFeignClient
          .getPraticheVariabiliProcessInstanceId(getProcessId(pratica.getLinkPratica()));

      if (variabiliProcessoResponse == null) {
        String messageError =
            String.format(ErrorMessages.PROCESSO_NON_TROVATO,
                getProcessId(pratica.getLinkPratica()));
        logger.error(methodName, messageError);
        throw new NotFoundException(messageError);
      }

      try {
        String metadati =
            aggiornaMetadati(ObjectUtils.getDataMapper().readTree(pratica.getMetadati()),
                configurazioneMetadati, variabiliProcessoResponse.getVariabili());

        pratica.setMetadati(metadati);
        pratica = praticaRepository.save(pratica);
      } catch (IOException e) {
        logger.error(methodName, ErrorMessages.PROBLEMI_NELLA_LETTURA_DEI_METADATI, e);
        throw new InternalServerException(ErrorMessages.PROBLEMI_NELLA_LETTURA_DEI_METADATI, e);
      }
    }

    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());
  }

  @Override
  @Transactional(readOnly = true)
  public Pratica aggiornaVariabiliProcesso(String idPratica) {

    // seleziono la pratica richiesta
    CosmoTPratica pratica = getPratica(idPratica);

    if (pratica.getMetadati() == null || pratica.getMetadati().isBlank()) {
      throw new NotFoundException(
          String.format("Non ci sono metadati per la pratica %s", idPratica));
    }

    aggiornaVariabiliProcesso(pratica, pratica.getMetadati());
    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());

  }

  private CosmoTPratica getPratica(String idPratica) {
    final String methodName = "getPratica";

    if (StringUtils.isBlank(idPratica)) {
      logger.error(methodName, ErrorMessages.ID_PRATICA_DEVE_ESSERE_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.ID_PRATICA_DEVE_ESSERE_VALORIZZATO);
    }

    if (!StringUtils.isNumeric(idPratica)) {
      logger.error(methodName, ErrorMessages.ID_PRATICA_DEVE_ESSERE_NUMERICO);
      throw new BadRequestException(ErrorMessages.ID_PRATICA_DEVE_ESSERE_NUMERICO);
    }

    return praticaRepository.findOneNotDeleted(Long.valueOf(idPratica))
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.PRATICA_CON_ID_NON_TROVATA, Long.valueOf(idPratica))));

  }

  private String getProcessId(String linkPratica) {
    final String methodName = "getProcessId";

    final String messageError = "Impossibile trovare informazioni sul processo";

    if (linkPratica == null || linkPratica.isBlank()) {
      logger.error(methodName, messageError);
      throw new NotFoundException(messageError);
    }

    String processInstanceId = linkPratica.replace("/pratiche/", "");

    if (processInstanceId == null || processInstanceId.isBlank()) {
      logger.error(methodName, messageError);
      throw new NotFoundException(messageError);
    }

    return processInstanceId;
  }

  // per ogni configurazione controllo che ci sia una variabile di processo che abbia lo stesso
  // nome della chiave della configurazione nel caso in cui ci sia, aggiorno il valore dei
  // metadati della pratica con quella della variabile di processo, ripercorrendo i metadati a
  // seconda di quanto indicato nel valore della chiave della configurazione
  private String aggiornaMetadati(JsonNode metadati,
      List<CosmoCConfigurazioneMetadati> configurazioneMetadati,
      List<VariabileProcesso> variabiliProcesso) throws JsonProcessingException {

    configurazioneMetadati.forEach(configurazione -> {
      Optional<VariabileProcesso> variabileProcesso = variabiliProcesso.stream()
          .filter(vp -> vp.getName() != null && vp.getName().equals(configurazione.getChiave()))
          .findAny();

      if (variabileProcesso.isPresent()) {
        String[] chiavi = configurazione.getValore().split("\\.");

        if (chiavi != null && chiavi.length > 0) {
          aggiornaValori(metadati, chiavi, 0, variabileProcesso.get(), 0);
        }
      }
    });

    return ObjectUtils.getDataMapper().writeValueAsString(metadati);

  }

  private int aggiornaValori(JsonNode jsonNode, String[] chiavi, int i,
      VariabileProcesso variabileProcesso, int count) {

    for (; i < chiavi.length && jsonNode != null; i++) {

      if (i == (chiavi.length - 1)) {
        return aggiornaValore(jsonNode, chiavi[i], variabileProcesso, count);
      }

      if (jsonNode.isObject() && jsonNode.has(chiavi[i])) {
        jsonNode = jsonNode.get(chiavi[i]);

      } else if (jsonNode.isArray() && jsonNode.findParents(chiavi[i]) != null
          && !jsonNode.findParents(chiavi[i]).isEmpty()) {

        for (JsonNode child : jsonNode.findParents(chiavi[i])) {
          count = aggiornaValori(child, chiavi, i, variabileProcesso, count);
        }
        return count;

      } else {
        jsonNode = null;
      }
    }
    return count;
  }

  private int aggiornaValore(JsonNode jsonNode, String fieldName,
      VariabileProcesso variabileProcesso, int count) {

    if (jsonNode.isObject() && jsonNode.has(fieldName)) {
      valorizzazioneOggetto(jsonNode, fieldName, variabileProcesso.getValue(), 0);

    } else if (jsonNode.isArray() && jsonNode.findParents(fieldName) != null) {

      for (JsonNode child : jsonNode.findParents(fieldName)) {

        if (variabileProcesso.getValue() instanceof ArrayList<?>) {

          List<?> list = (ArrayList<?>) variabileProcesso.getValue();
          if (count < list.size()) {
            valorizzazioneOggetto(child, fieldName, list.get(count), count);
          } else {
            valorizzazioneOggetto(child, fieldName, list.get(list.size() - 1), (list.size() - 1));
          }
          count++;
        } else {
          valorizzazioneOggetto(child, fieldName, variabileProcesso.getValue(), 0);
        }
      }
    }
    return count;
  }

  private int valorizzazioneOggetto(JsonNode parentJsonNode, String fieldName,
      Object variabileProcessoValue, int count) {
    JsonNode fieldValueNode = parentJsonNode.get(fieldName);

    if (fieldValueNode != null) {

      if (!fieldValueNode.isContainerNode() || fieldValueNode.isObject()) {
        setValore(parentJsonNode, fieldName, variabileProcessoValue);

      } else if (fieldValueNode.isArray()) {

        if (variabileProcessoValue instanceof ArrayList<?>) {
          ((ObjectNode) parentJsonNode).replace(fieldName,
              ObjectUtils.getDataMapper().valueToTree(variabileProcessoValue));
        } else {
          List<Object> newList = new ArrayList<>();
          fieldValueNode.forEach(
              child -> newList.add(variabileProcessoValue));

          if (!newList.isEmpty()) {
            ((ObjectNode) parentJsonNode).replace(fieldName,
                ObjectUtils.getDataMapper().valueToTree(newList));
          }
        }
      }
    }
    return count;
  }

  private void setValore(JsonNode parentJsonNode, String fieldName, Object variabileProcessoValue) {
    if (variabileProcessoValue instanceof ArrayList<?>
    && !((ArrayList<?>) variabileProcessoValue).isEmpty()) {

      ((ObjectNode) parentJsonNode).replace(fieldName,
          ObjectUtils.getDataMapper().valueToTree(((ArrayList<?>) variabileProcessoValue).get(0)));
      ((ArrayList<?>) variabileProcessoValue).remove(0);

    } else {
      ((ObjectNode) parentJsonNode).replace(fieldName,
          ObjectUtils.getDataMapper().valueToTree(variabileProcessoValue));
    }
  }

  private void aggiornaVariabiliProcesso(CosmoTPratica pratica, String metadati) {
    final String methodName = "aggiornaVariabili";

    if (pratica.getLinkPratica() == null || pratica.getLinkPratica().isBlank()) {
      logger.error(methodName, ErrorMessages.PROCESS_INSTANCE_ID_NON_VALIDO);
      throw new ConflictException(ErrorMessages.PROCESS_INSTANCE_ID_NON_VALIDO);
    }

    if (pratica.getTipo() == null) {
      String messageError =
          String.format(ErrorMessages.TIPO_PRATICA_NON_TROVATA, pratica.getTipo());
      logger.error(methodName, messageError);
      throw new NotFoundException(messageError);
    }
    List<CosmoCConfigurazioneMetadati> configurazioneMetadati = configurazioneMetadatiRepository
        .findActiveByField(CosmoCConfigurazioneMetadati_.cosmoDTipoPratica, pratica.getTipo());
    if (configurazioneMetadati == null || configurazioneMetadati.isEmpty()) {
      String messageError =
          String.format(ErrorMessages.CONFIGURAZIONE_METADATI_PER_TIPO_PRATICA_NON_TROVATA,
              pratica.getTipo().getCodice());
      logger.error(methodName, messageError);
      throw new NotFoundException(messageError);
    }

    VariabiliProcessoResponse variabiliProcessoResponse = businessPraticheFeignClient
        .getPraticheVariabiliProcessInstanceId(getProcessId(pratica.getLinkPratica()));

    if (variabiliProcessoResponse == null) {
      String messageError =
          String.format(ErrorMessages.PROCESSO_NON_TROVATO, getProcessId(pratica.getLinkPratica()));
      logger.error(methodName, messageError);
      throw new NotFoundException(messageError);
    }

    logger.info(methodName, "invio variabili su flowable");
    try {
      List<VariabileProcesso> variabiliProcesso = creazioneVariabiliDaMetadati(
          ObjectUtils.getDataMapper().readTree(metadati), configurazioneMetadati);

      businessPraticheFeignClient.putPraticheVariabiliProcessInstanceId(
          getProcessId(pratica.getLinkPratica()),
          setVariabiliDaSalvare(variabiliProcessoResponse.getVariabili(),
              variabiliProcesso));
    } catch (Exception e) {
      logger.error(methodName, ErrorMessages.PROBLEMI_NELLA_LETTURA_DEI_METADATI, e);
      throw new InternalServerException(ErrorMessages.PROBLEMI_NELLA_LETTURA_DEI_METADATI, e);
    }

  }

  // per ogni chiave della configurazione, verificare che il json abbia come chiavi i valori della
  // configurazione e creare una variabile di processo che abbia come chiave la chiave della
  // configurazione e come valore il valore individuato nel json
  private List<VariabileProcesso> creazioneVariabiliDaMetadati(JsonNode metadatiJson,
      List<CosmoCConfigurazioneMetadati> configurazioneMetadati) {

    List<VariabileProcesso> variabiliProcesso = new LinkedList<>();

    configurazioneMetadati.forEach(configurazione -> {
      String[] chiavi = configurazione.getValore().split("\\.");

      JsonNode node = metadatiJson;
      List<JsonNode> nodeArray = new LinkedList<>();

      for (String chiave : chiavi) {

        if (node != null && node.isObject() && node.has(chiave)) {
          node = node.get(chiave);

        } else if (node != null && node.isArray()) {
          variabiliProcessoNelCasoDiArray(node.elements(), chiavi,
              ArrayUtils.indexOf(chiavi, chiave), nodeArray);
          node = null;
          break;

        } else {
          node = null;
        }
      }

      if (node != null) {
        variabiliProcesso.add(aggiungiVariabileProcesso(configurazione.getChiave(), node));
      }

      if (nodeArray != null && !nodeArray.isEmpty()) {
        variabiliProcesso.add(aggiungiVariabileProcesso(configurazione.getChiave(), nodeArray));
      }
    });

    return variabiliProcesso;
  }

  private void variabiliProcessoNelCasoDiArray(Iterator<JsonNode> nodeIterator, String[] chiavi,
      int i, List<JsonNode> jsonNodeList) {

    List<JsonNode> jsonNodeArray = new LinkedList<>();

    nodeIterator.forEachRemaining(jsonNodeArray::add);

    for (; i < chiavi.length; i++) {

      jsonNodeArray
      .removeIf(singleElement -> singleElement == null || !singleElement.isContainerNode());

      for (JsonNode jsonNode : jsonNodeArray) {

        if (jsonNode != null && jsonNode.isObject() && jsonNode.has(chiavi[i])) {
          jsonNodeArray.set(jsonNodeArray.indexOf(jsonNode), jsonNode.get(chiavi[i]));
        } else if (jsonNode != null && jsonNode.isArray()) {
          variabiliProcessoNelCasoDiArray(jsonNode.elements(), chiavi, i, jsonNodeList);
        } else {
          jsonNodeArray.set(jsonNodeArray.indexOf(jsonNode), null);
        }
      }
    }

    jsonNodeArray.removeIf(Objects::isNull);

    jsonNodeList.addAll(jsonNodeArray);
  }

  private VariabileProcesso aggiungiVariabileProcesso(String chiave, Object value) {
    VariabileProcesso variabileProcesso = new VariabileProcesso();
    variabileProcesso.setName(chiave);
    variabileProcesso.setValue(value);
    return variabileProcesso;
  }

  // se variabileFlowable ha gia' il nome della variabile aggiornata, viene aggiornato il valore
  // se variabileFlowable non ha il nome della variabile aggiornata, viene aggiunta
  private List<VariabileProcesso> setVariabiliDaSalvare(
      List<VariabileProcesso> variabiliFlowable, List<VariabileProcesso> variabiliAggiornate) {

    if (variabiliFlowable == null || variabiliFlowable.isEmpty()) {
      return variabiliAggiornate;
    }

    List<VariabileProcesso> variabiliFlowableAggiornate = new LinkedList<>();

    variabiliFlowable.forEach(variabileFlowable -> {
      boolean findVariabile = variabiliAggiornate.stream().anyMatch(
          variabileAggiornata -> variabileAggiornata.getName().equals(variabileFlowable.getName()));

      if (findVariabile) {
        variabiliFlowableAggiornate.add(variabileFlowable);
      }
    });
    variabiliAggiornate.forEach(variabileAggiornata -> {

      Optional<VariabileProcesso> findVariabile = variabiliFlowableAggiornate.stream().filter(
          variabileFlowable -> variabileFlowable.getName().equals(variabileAggiornata.getName()))
          .findAny();

      if (findVariabile.isPresent()) {
        findVariabile.get().setValue(variabileAggiornata.getValue());
        findVariabile.get().setType(null);
        findVariabile.get().setScope(null);
      } else {
        variabiliFlowableAggiornate.add(variabileAggiornata);
      }
    });

    return variabiliFlowableAggiornate;
  }


}
