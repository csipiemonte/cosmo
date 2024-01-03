/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.AssegnazioneStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.EventoStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoAttivita;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoLStoricoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class StoricoPraticaServiceImpl implements StoricoPraticaService {

  /*
   * utile per prevenire lock incrociati coi listener e altre chiamate automatiche sincrone
   */
  private static boolean POLICY_IMMEDIATE_FLUSH = true;

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final String CODICE_STATO_ATTIVITA_IN_CORSO = "IN_CORSO";
  private static final String DESCRIZIONE_STATO_ATTIVITA_IN_CORSO = "In corso";
  private static final String CODICE_STATO_ATTIVITA_TERMINATA = "TERMINATA";
  private static final String DESCRIZIONE_STATO_ATTIVITA_TERMINATA = "Terminata";

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoLStoricoPraticaRepository cosmoLStoricoPraticaRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Override
  @Transactional(readOnly = true)
  public StoricoPratica getStoricoAttivita(Long idPratica) {
    final var method = "getStoricoAttivita";
    logger.info(method, "calcolo storico per la pratica {}", idPratica);

    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    StoricoPratica output = new StoricoPratica();
    List<EventoStoricoPratica> eventi = estraiEventiPratica(pratica);

    eventi.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));

    List<AttivitaStoricoPratica> attivitaList = estraiAttivitaPratica(eventi);

    List<AssegnazioneStoricoPratica> assegnazioniList = estraiAssegnazioniPratica(pratica);

    output.setEventi(eventi);
    output.setAttivita(attivitaList);
    output.setAssegnazioni(assegnazioniList);

    return output;
  }

  /**
   * @param eventi
   * @return
   */
  private List<AssegnazioneStoricoPratica> estraiAssegnazioniPratica(CosmoTPratica pratica) {
    List<AssegnazioneStoricoPratica> output = new LinkedList<>();

    // trova tutte le attivita'
    var listaAttivita = pratica.getAttivita().stream()
        .sorted((e1, e2) -> e1.getDtInserimento().compareTo(e2.getDtInserimento()))
        .collect(Collectors.toList());

    for (var attivita : listaAttivita) {
      // trova tutte le assegnazioni
      var listaAssegnazione = attivita.getCosmoRAttivitaAssegnaziones().stream()
          .sorted((e1, e2) -> e1.getDtInizioVal().compareTo(e2.getDtInizioVal()))
          .collect(Collectors.toList());

      for (var assegnazione : listaAssegnazione) {
        AssegnazioneStoricoPratica mapped = new AssegnazioneStoricoPratica();

        mapped.setAttivita(riferimentoAttivita(attivita));
        mapped.setInizio(offsetDateTime(assegnazione.getDtInizioVal()));
        mapped.setFine(offsetDateTime(assegnazione.getDtFineVal()));
        mapped.setGruppo(riferimentoGruppo(assegnazione.getGruppo()));
        mapped.setUtente(riferimentoUtente(assegnazione.getUtente()));

        output.add(mapped);
      }
    }

    return output;
  }


  private List<EventoStoricoPratica> estraiEventiPratica(CosmoTPratica pratica) {

    return cosmoLStoricoPraticaRepository.findByField(CosmoLStoricoPratica_.pratica, pratica)
        .stream().sorted((e1, e2) -> e1.getDtEvento().compareTo(e2.getDtEvento())).map(this::map)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  private EventoStoricoPratica map(CosmoLStoricoPratica input) {
    if (input == null) {
      return null;
    }
    EventoStoricoPratica output = new EventoStoricoPratica();

    output.setAttivita(riferimentoAttivita(input.getAttivita()));
    output.setFruitore(riferimentoFruitore(input.getFruitore()));
    output.setUtente(riferimentoUtente(input.getUtente()));
    output.setUtenteCoinvolto(riferimentoUtente(input.getUtenteCoinvolto()));
    output.setGruppoCoinvolto(riferimentoGruppo(input.getGruppoCoinvolto()));
    output.setDescrizione(input.getDescrizioneEvento());
    output.setTimestamp(offsetDateTime(input.getDtEvento()));
    output.setTipo(input.getCodiceTipoEvento().name());

    return output;
  }

  private List<AttivitaStoricoPratica> estraiAttivitaPratica(List<EventoStoricoPratica> eventi) {

    Map<Integer, List<EventoStoricoPratica>> map = new HashMap<>();
    eventi.forEach(evento -> {
      if (null != evento && null != evento.getAttivita()) {
        var eventiPerAttivita = map.get(evento.getAttivita().getId());
        if (null == eventiPerAttivita) {
          eventiPerAttivita = new ArrayList<EventoStoricoPratica>();
        }
        eventiPerAttivita.add(evento);
        map.put(evento.getAttivita().getId(), eventiPerAttivita);
      }
    });

    List<AttivitaStoricoPratica> attivitaList = new ArrayList<>();

    map.keySet().forEach(idAttivita -> {
      AttivitaStoricoPratica attivita = new AttivitaStoricoPratica();
      var eventiPerAttivita = map.get(idAttivita);
      eventiPerAttivita.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
      Set<RiferimentoUtente> utentiCoinvolti = new HashSet<>();
      Set<RiferimentoGruppo> gruppiCoinvolti = new HashSet<>();
      LocalDateTime now = LocalDateTime.now();
      ZoneOffset zoneOffSet = ZoneId.of("Europe/Paris").getRules().getOffset(now);
      OffsetDateTime inizio = now.plusYears(1000).atOffset(zoneOffSet);
      OffsetDateTime fine = null;
      for (EventoStoricoPratica evento : eventiPerAttivita) {
        if (inizio.isAfter(evento.getTimestamp())) {
          inizio = evento.getTimestamp();
        }
        if (TipoEventoStoricoPratica.ATTIVITA_COMPLETATA.name()
            .equalsIgnoreCase(evento.getTipo())) {
          fine = evento.getTimestamp();
          attivita.setEsecutore(evento.getUtente());
        } else if (TipoEventoStoricoPratica.ATTIVITA_ANNULLATA.name()
            .equalsIgnoreCase(evento.getTipo())) {
          fine = evento.getTimestamp();
        }
        if (null != evento.getUtenteCoinvolto()) {
          utentiCoinvolti.add(evento.getUtenteCoinvolto());
        }
        if (null != evento.getGruppoCoinvolto()) {
          gruppiCoinvolti.add(evento.getGruppoCoinvolto());
        }
      }

      attivita.setAttivita(eventiPerAttivita.get(0).getAttivita());
      attivita.setInizio(inizio);
      attivita.setFine(fine);
      StatoAttivita stato = new StatoAttivita();

      if (null == attivita.getFine()) {
        stato.setCodice(CODICE_STATO_ATTIVITA_IN_CORSO);
        stato.setDescrizione(DESCRIZIONE_STATO_ATTIVITA_IN_CORSO);
      } else {
        stato.setCodice(CODICE_STATO_ATTIVITA_TERMINATA);
        stato.setDescrizione(DESCRIZIONE_STATO_ATTIVITA_TERMINATA);
      }
      attivita.setStato(stato);

      attivita.setGruppiCoinvolti(List.copyOf(gruppiCoinvolti));
      attivita.setUtentiCoinvolti(List.copyOf(utentiCoinvolti));
      attivitaList.add(attivita);

    });

    return attivitaList;
  }

  private RiferimentoAttivita riferimentoAttivita(CosmoTAttivita attivita) {
    if (attivita == null) {
      return null;
    }
    RiferimentoAttivita output = new RiferimentoAttivita();
    output.setId(attivita.getId().intValue());
    output.setNome(attivita.getNome());
    output.setDescrizione(attivita.getDescrizione());
    output.setDataCancellazione(offsetDateTime(attivita.getDtCancellazione()));
    return output;
  }

  private OffsetDateTime offsetDateTime(Timestamp value) {
    return value == null ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
  }

  private RiferimentoUtente riferimentoUtente(CosmoTUtente utente) {
    if (utente == null) {
      return null;
    }
    RiferimentoUtente output = new RiferimentoUtente();
    output.setId(utente.getId());
    output.setCodiceFiscale(utente.getCodiceFiscale());
    output.setNome(utente.getNome());
    output.setCognome(utente.getCognome());
    return output;
  }

  private RiferimentoFruitore riferimentoFruitore(CosmoTFruitore fruitore) {
    if (null == fruitore) {
      return null;
    }

    RiferimentoFruitore output = new RiferimentoFruitore();
    output.setId(fruitore.getId());
    output.setApiManagerId(fruitore.getApiManagerId());
    output.setNomeApp(fruitore.getNomeApp());
    return output;
  }

  private RiferimentoGruppo riferimentoGruppo(CosmoTGruppo gruppo) {
    if (gruppo == null) {
      return null;
    }
    RiferimentoGruppo output = new RiferimentoGruppo();
    output.setDescrizione(gruppo.getDescrizione());
    output.setCodice(gruppo.getCodice());
    output.setNome(gruppo.getNome());
    output.setId(gruppo.getId());
    return output;
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  @Override
  public void logEvent(CosmoLStoricoPratica entry) {
    final var method = "logEvent";
    logger.debug(method, "registro evento {} per pratica {}", entry.getCodiceTipoEvento(),
        entry.getPratica() != null ? entry.getPratica().getId() : null);

    preProcess(entry);

    cosmoLStoricoPraticaRepository.insert(entry);
    if (POLICY_IMMEDIATE_FLUSH) {
      cosmoLStoricoPraticaRepository.flush();
    }
  }

  protected void preProcess(CosmoLStoricoPratica entry) {
    if (entry.getDtEvento() == null) {
      entry.setDtEvento(Timestamp.from(Instant.now()));
    }

    /*
     * popolo automaticamente utente e/o fruitore solo se nessuno dei due e' stato popolato
     * precedentemente.
     */
    if (entry.getUtente() == null && entry.getFruitore() == null) {

      if (entry.getUtente() == null) {
        UserInfoDTO utenteCorrente = SecurityUtils.getUtenteCorrente();
        if (utenteCorrente != null && Boolean.FALSE.equals(utenteCorrente.getAnonimo())) {
          entry.setUtente(
              cosmoLStoricoPraticaRepository.reference(CosmoTUtente.class, utenteCorrente.getId()));
        }
      }

      if (entry.getFruitore() != null) {
        ClientInfoDTO client = SecurityUtils.getClientCorrente();
        CosmoTFruitore fruitoreEntity =
            Optional.ofNullable(client).filter(c -> Boolean.FALSE.equals(c.getAnonimo()))
            .map(c -> cosmoTFruitoreRepository
                .findOneByField(CosmoTFruitore_.apiManagerId, c.getCodice()).orElse(null))
            .orElse(null);

        if (fruitoreEntity != null) {
          entry.setFruitore(cosmoLStoricoPraticaRepository.reference(CosmoTFruitore.class,
              fruitoreEntity.getId()));
        }
      }
    }
  }

  @Override
  public void logEvent(StoricoPraticaRequest request) {
    ValidationUtils.require(request, "request");
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(request.getIdPratica());
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }
    CosmoLStoricoPratica storicoPratica = new CosmoLStoricoPratica();
    storicoPratica.setPratica(pratica);
    storicoPratica.setCodiceTipoEvento(TipoEventoStoricoPratica.valueOf(request.getCodiceTipoEvento()));
    storicoPratica.setDtEvento(Timestamp.from(Instant.now()));
    storicoPratica.setDescrizioneEvento(request.getDescrizioneEvento());

    logEvent(storicoPratica);

  }
}
