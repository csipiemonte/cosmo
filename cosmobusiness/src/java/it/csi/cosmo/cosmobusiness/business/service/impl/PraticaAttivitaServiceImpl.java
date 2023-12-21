/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;
import it.csi.cosmo.cosmobusiness.business.service.PraticaAttivitaService;
import it.csi.cosmo.cosmobusiness.dto.exception.FlowableEventMessageException;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoAuthorizationUtentiFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPratichePraticheAttivitaFeignClient;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

/**
 *
 */

@Service
public class PraticaAttivitaServiceImpl implements PraticaAttivitaService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "PraticheAttivitaServiceImpl");

  private static final String TASK_AGGIORNATO = "TASK_AGGIORNATO";

  @Autowired
  private CosmoPratichePraticheAttivitaFeignClient cosmoPratichePraticheAttivitaFeignClient;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheFeignClient;

  @Autowired
  private CosmoAuthorizationUtentiFeignClient cosmoAuthorizationUtentiFeignClient;

  public PraticaAttivitaServiceImpl() {
    // NOP
  }

  @Override
  @Deprecated(forRemoval = true) // non utilizzato
  public Pratica salvaPraticaAttivita(MapMessage mapMessage) {
    final String methodName = "salvaPraticaAttivita";

    Pratica pratica = mapPraticaFromMapMessage(mapMessage);

    if (pratica.getId() != null) {
      logger.info(methodName, "Aggiorno pratica con id: " + pratica.getId());
      return cosmoPratichePraticheAttivitaFeignClient.putPraticheAttivita(pratica);
    }

    throw new InternalServerException(
        "Ricevuto messaggio JMS per salvataggio attivita senza id pratica");
  }

  @Deprecated(forRemoval = true) // per favore riscrivere con POJO java
  private Pratica mapPraticaFromMapMessage(MapMessage mapMessage) {
    final String methodName = "mapPraticaFromMapMessage";

    try {
      Pratica pratica = new Pratica();
      if (StringUtils.isNumeric(mapMessage.getString("idPratica"))) {
        String idPratica = mapMessage.getString("idPratica");
        pratica = cosmoPraticheFeignClient.getPraticheIdPratica(idPratica, null);
        if (pratica == null) {
          throw new NotFoundException("Pratica non trovata " + idPratica);
        }
      }

      var tipo = new TipoPratica();
      tipo.setCodice(mapMessage.getString("tipoPratica"));
      pratica.setTipo(tipo);
      pratica.setCodiceIpaEnte(mapMessage.getString("codiceIpaEnte"));
      pratica.setOggetto(mapMessage.getString("oggetto"));
      pratica.setIdPraticaExt(mapMessage.getString("idPraticaExt"));
      pratica.setLinkPratica(mapMessage.getString("linkPratica"));

      if (StringUtils.isNotBlank(mapMessage.getString("dataFinePratica"))) {
        pratica.setDataFinePratica(stringToOffsetDateTime(mapMessage.getString("dataFinePratica")));
      }

      pratica.setDataCambioStato(stringToOffsetDateTime(mapMessage.getString("dataCambioStato")));

      if (StringUtils.isNotBlank(mapMessage.getString("stato"))) {
        StatoPratica stato = new StatoPratica();
        stato.setCodice(mapMessage.getString("stato"));
        pratica.setStato(stato);
      }

      List<Attivita> attivita;

      if (StringUtils.isNotBlank(mapMessage.getString("attivita"))) {
        attivita = Arrays.asList(ObjectUtils.getDataMapper()
            .readValue(mapMessage.getString("attivita"), Attivita[].class));
      } else {
        attivita = new LinkedList<>();
      }

      if (StringUtils.isNotBlank(mapMessage.getString("assegnazione"))) {
        List<Assegnazione> assegnazione = Arrays.asList(ObjectUtils.getDataMapper()
            .readValue(mapMessage.getString("assegnazione"), Assegnazione[].class));

        assegnazione.forEach(a -> {
          attivita.stream().forEach(act -> act.getAssegnazione().add(a));
        });
      }

      pratica.setAttivita(attivita);

      return pratica;
    } catch (NumberFormatException | JMSException e) {
      String errorMessage = "Error nel messaggio inviato: " + e.getMessage();
      logger.error(methodName, errorMessage);
      throw new FlowableEventMessageException(errorMessage, e);
    } catch (IOException e) {
      String errorMessage = "Errore nella lettura dell'attivita: " + e.getMessage();
      logger.error(methodName, errorMessage);
      throw new FlowableEventMessageException(errorMessage, e);
    }
  }

  private OffsetDateTime stringToOffsetDateTime(String str) {
    if (str == null) {
      return null;
    }
    return OffsetDateTime.parse(str);
  }

  @Override
  public boolean getAttivitaAncoraNonAggiornata(MapMessage mapMessage) {

    final String methodName = "getAttivitaAncoraNonAggiornata";

    List<Attivita> attivita = new ArrayList<>();
    List<Assegnazione> assegnazione = new ArrayList<>();
    String idPratica;
    ObjectMapper dataMapper = ObjectUtils.getDataMapper();
    dataMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    dataMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    try {

      idPratica = mapMessage.getString("idPratica");
      if (StringUtils.isNotBlank(mapMessage.getString("attivita"))) {
        attivita =
            Arrays.asList(dataMapper.readValue(mapMessage.getString("attivita"), Attivita[].class));
      }

      if (StringUtils.isNotBlank(mapMessage.getString("assegnazione"))) {
        assegnazione = Arrays.asList(ObjectUtils.getDataMapper()
            .readValue(mapMessage.getString("assegnazione"), Assegnazione[].class));
      }
    } catch (JMSException e) {
      String errorMessage = "Errore nel messaggio inviato: " + e.getMessage();
      logger.error(methodName, errorMessage);
      throw new FlowableEventMessageException(errorMessage);
    } catch (IOException e) {
      String errorMessage = "Errore nella lettura dell'attivita: " + e.getMessage();
      logger.error(methodName, errorMessage);
      throw new FlowableEventMessageException(errorMessage);
    }

    if (CollectionUtils.isEmpty(attivita) || StringUtils.isBlank(idPratica)) {
      return false;
    }

    List<Attivita> attivitaAggiornate = attivita.stream()
        .filter(attivitaSingola -> attivitaSingola.getEvento().equals(TASK_AGGIORNATO))
        .collect(Collectors.toList());


    if (CollectionUtils.isEmpty(attivitaAggiornate)) {
      return false;
    }

    Pratica pratica = cosmoPraticheFeignClient.getPraticheIdPratica(idPratica, null);

    if (pratica == null || CollectionUtils.isEmpty(pratica.getAttivita())) {
      return false;
    }


    for (Attivita attivitaAggiornata : attivitaAggiornate) {
      attivitaAggiornata.setAssegnazione(assegnazione);
      for (Attivita attivitaSuDB : pratica.getAttivita()) {
        if (attivitaSuDB.getLinkAttivita().equals(attivitaAggiornata.getLinkAttivita())) {
          String nuovoAssegnatario = getAssegnatario(attivitaAggiornata);
          if (!nuovoAssegnatario.equals("")) {
            UtenteResponse utentiCodiceFiscale =
                cosmoAuthorizationUtentiFeignClient.getUtentiCodiceFiscale(nuovoAssegnatario);
            if (utentiCodiceFiscale != null) {
              nuovoAssegnatario = "" + utentiCodiceFiscale.getUtente().getId();
            } else {
              nuovoAssegnatario = "";
            }
          }
          String vecchioAssegnatario = getAssegnatario(attivitaSuDB);
          if (!nuovoAssegnatario.equals("") && !vecchioAssegnatario.equals(nuovoAssegnatario)) {
            return true;
          }
        }

      }
    }

    return false;
  }

  private String getAssegnatario(Attivita attivitaAggiornata) {
    Optional<Assegnazione> assegnazione = attivitaAggiornata.getAssegnazione().stream()
        .filter(ass -> ass.isAssegnatario() && (ass.getCampiTecnici() == null
        || ass.getCampiTecnici() != null && ass.getCampiTecnici().getDtFineVal() == null))
        .findFirst();
    if (assegnazione.isPresent()) {
      return assegnazione.get().getIdUtente();
    }
    return "";
  }
}
