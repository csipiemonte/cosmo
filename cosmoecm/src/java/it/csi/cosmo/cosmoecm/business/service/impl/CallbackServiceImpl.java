/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.CosmoDStatoCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoTCallbackFruitore;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.StatoCallbackFruitore;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoCallback;
import it.csi.cosmo.cosmoecm.business.service.CallbackService;
import it.csi.cosmo.cosmoecm.business.service.FruitoriService;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTCallbackFruitoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEndpointFruitoreRepository;

/**
 *
 */

@Service
@Transactional
public class CallbackServiceImpl implements CallbackService {

  @Autowired
  FruitoriService fruitoriService;

  @Autowired
  CosmoTCallbackFruitoreRepository cosmoTCallbackFruitoreRepository;

  @Autowired
  CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Override
  public SchedulazioneCallbackResponse schedulaInvioAsincrono(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, String codiceSegnale) {

    CosmoTCallbackFruitore callback = costruisciIstanzaCallback(operazione, idFruitore, payload,
        parametri, StatoCallbackFruitore.SCHEDULATO, codiceSegnale);

    callback = cosmoTCallbackFruitoreRepository.save(callback);

    SchedulazioneCallbackResponse response = new SchedulazioneCallbackResponse();
    StatoCallback stato = new StatoCallback();
    stato.setCodice(callback.getStato().getCodice());
    response.setUuid("cosmo-cb-" + String.format("%06d", callback.getId()));
    response.setStato(stato);
    return response;
  }

  protected CosmoTCallbackFruitore costruisciIstanzaCallback(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, StatoCallbackFruitore stato,
      String codiceSegnale) {

    if (StringUtils.isBlank(codiceSegnale)) {
      codiceSegnale = operazione.name();
    }

    CosmoTCallbackFruitore callback = new CosmoTCallbackFruitore();

    callback.setDtInserimento(Timestamp.from(Instant.now()));
    callback.setEndpoint(fruitoriService.getEndpoint(operazione, idFruitore, null));
    if (payload != null) {
      callback.setPayload(ObjectUtils.getDataMapper().convertValue(payload, JsonNode.class));
    }
    callback.setStato(cosmoTEndpointFruitoreRepository.reference(CosmoDStatoCallbackFruitore.class,
        stato.name()));
    callback.setTentativiInvio(new ArrayList<>());
    callback.setParametri(parametri);
    callback.setCodiceSegnale(codiceSegnale);

    return callback;
  }

}

