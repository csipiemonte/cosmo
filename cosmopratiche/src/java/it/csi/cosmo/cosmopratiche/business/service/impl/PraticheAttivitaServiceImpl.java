/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.ATTIVITA_NON_TROVATA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.UTENTE_NON_VALIDO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmopratiche.business.service.PraticaAttivitaService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoRAttivitaAssegnazioneMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTAttivitaMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTAttivitaSpecifications;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class PraticheAttivitaServiceImpl implements PraticaAttivitaService {

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, "PraticheAttivitaServiceImpl");

  private static final String TASK_COMPLETATO = "TASK_COMPLETATO";
  private static final String TASK_AGGIORNATO = "TASK_AGGIORNATO";

  @Autowired
  private CosmoTPraticheMapper praticheMapper;

  @Autowired
  private CosmoTAttivitaMapper attivitaMapper;

  @Autowired
  private CosmoRAttivitaAssegnazioneMapper attivitaAssegnazioneMapper;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoTipoPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoPraticaRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTFormLogicoRepository cosmoTFormLogicoRepository;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Pratica salva(Pratica pratica) {
    final String methodName = "salva";

    if (pratica.getId() != null) {
      logger.error(methodName, ErrorMessages.ID_PRATICA_NON_DEVE_ESSERE_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.ID_PRATICA_NON_DEVE_ESSERE_VALORIZZATO);
    }

    if (pratica.getTipo() == null || StringUtils.isBlank(pratica.getTipo().getCodice())) {
      logger.error(methodName, ErrorMessages.TIPO_PRATICA_DEVE_ESSERE_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.TIPO_PRATICA_DEVE_ESSERE_VALORIZZATO);
    }

    CosmoDTipoPratica tipoPratica = cosmoTipoPraticaRepository
        .findOneActive(pratica.getTipo().getCodice())
        .orElseThrow(it.csi.cosmo.common.exception.NotFoundException::new);

    CosmoTPratica praticaDaSalvare = praticheMapper.toCosmoTPratica(pratica);
    praticaDaSalvare.setTipo(tipoPratica);
    praticaDaSalvare.setDtInserimento(Timestamp.from(Instant.now()));
    praticaDaSalvare.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    praticaDaSalvare.setAttivita(null);

    if (praticaDaSalvare.getId() != null) {
      CosmoTEnte ente= praticaDaSalvare.getEnte();
      List<CosmoTAttivita> attivitaDaSalvare =
          pratica.getAttivita().stream().filter(e -> !e.getEvento().equals(TASK_COMPLETATO))
          .map(e -> attivitaMapper.toCosmoTAttivita(e)).collect(Collectors.toList());
      attivitaDaSalvare.forEach(att -> {att.getCosmoRAttivitaAssegnaziones()
        .forEach(ass -> ass.setDtInizioVal(Timestamp.from(Instant.now())));
      setAttivitaFormLogico(att,ente);
      });

      if (CollectionUtils.isEmpty(attivitaDaSalvare)) {
        attivitaDaSalvare = cosmoTAttivitaRepository.save(attivitaDaSalvare);
        praticaDaSalvare.setAttivita(attivitaDaSalvare);
      }
    }

    praticaDaSalvare = cosmoPraticaRepository.save(praticaDaSalvare);

    logger.info(methodName, "Nuova pratica salvata con id {}", praticaDaSalvare.getId());
    return praticheMapper.toPractice(praticaDaSalvare, null, SecurityUtils.getUtenteCorrente());
  }

  private void setAttivitaFormLogico(CosmoTAttivita attivita, CosmoTEnte ente) {
    List<CosmoTFormLogico> forms = cosmoTFormLogicoRepository
        .findNotDeletedByField(CosmoTFormLogico_.codice, attivita.getFormKey());
    CosmoTFormLogico result = forms.stream()
        .filter(
            form -> form.getCosmoTEnte() != null
            && form.getCosmoTEnte().getId().equals(ente.getId()))
        .findFirst().orElse(
            forms.stream().filter(form -> form.getCosmoTEnte() == null).findFirst().orElse(null));

    if (result == null) {
      throw new NotFoundException();
    }
    attivita.setFormLogico(result);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Pratica aggiorna(Pratica pratica) {
    final String methodName = "aggiorna";

    if (pratica.getId() == null) {
      logger.error(methodName, ErrorMessages.ID_PRATICA_DEVE_ESSERE_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.ID_PRATICA_DEVE_ESSERE_VALORIZZATO);
    }

    CosmoTPratica praticaSuDB = cosmoPraticaRepository.findOne(pratica.getId().longValue());

    if (praticaSuDB == null) {
      String message = String.format(ErrorMessages.PRATICA_CON_ID_NON_TROVATA, pratica.getId());
      logger.error(methodName, message);
      throw new it.csi.cosmo.common.exception.NotFoundException(message);
    }

    if (pratica.getTipo() == null || StringUtils.isBlank(pratica.getTipo().getCodice())) {
      logger.error(methodName, ErrorMessages.TIPO_PRATICA_DEVE_ESSERE_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.TIPO_PRATICA_DEVE_ESSERE_VALORIZZATO);
    }

    praticaSuDB.setLinkPratica(pratica.getLinkPratica());

    praticaSuDB.setDataCambioStato(pratica.getDataCambioStato() != null
        ? Timestamp.valueOf(pratica.getDataCambioStato().toLocalDateTime())
            : null);

    if (pratica.getStato() != null && !StringUtils.isBlank(pratica.getStato().getCodice())) {
      praticaSuDB
      .setStato(cosmoTEnteRepository.reference(CosmoDStatoPratica.class,
          pratica.getStato().getCodice()));
    }

    if (!CollectionUtils.isEmpty(pratica.getAttivita())) {
      List<CosmoTAttivita> attivitaDaSalvare =
          aggiornaAttivitaAndAssegnazioni(pratica.getAttivita(), praticaSuDB);
      if (!CollectionUtils.isEmpty(attivitaDaSalvare)) {
        praticaSuDB.setAttivita(cosmoTAttivitaRepository.save(attivitaDaSalvare));
      }
    }

    praticaSuDB = cosmoPraticaRepository.save(praticaSuDB);

    return praticheMapper.toPractice(praticaSuDB, null, SecurityUtils.getUtenteCorrente());
  }

  private List<CosmoTAttivita> aggiornaAttivitaAndAssegnazioni(List<Attivita> attivita,
      CosmoTPratica praticaSuDB) {
    final String methodName = "aggiornaAttivita";

    attivita.forEach(e -> {

      CosmoTAttivita attivitaDB = cosmoTAttivitaRepository
          .findBycosmoTPraticaIdAndLinkAttivita(praticaSuDB.getId(), e.getLinkAttivita());

      if (attivitaDB != null) {

        List<CosmoRAttivitaAssegnazione> assegnazioniDB = cosmoRAttivitaAssegnazioneRepository
            .findByCosmoTAttivitaIdAndDtFineValIsNull(attivitaDB.getId());

        if (TASK_COMPLETATO.equals(e.getEvento())) {

          aggiornaAttivitaTerminata(attivitaDB, praticaSuDB);

        } else if (TASK_AGGIORNATO.equals(e.getEvento())) {

          List<CosmoRAttivitaAssegnazione> assegnazioniDaSalvare = new ArrayList<>();

          List<CosmoRAttivitaAssegnazione> assegnazioniConvertite = new ArrayList<>();

          e.getAssegnazione().stream().forEach(ass -> {
            CosmoRAttivitaAssegnazione cosmoRAttivitaAssegnazione =
                attivitaAssegnazioneMapper.toCosmoRAttivitaAssegnazione(ass);
            cosmoRAttivitaAssegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
            assegnazioniConvertite.add(cosmoRAttivitaAssegnazione);
          });

          assegnazioniDB.forEach(assegnazioneDB -> {
            Optional<CosmoRAttivitaAssegnazione> ass =
                cercaAssegnazione(assegnazioneDB, assegnazioniConvertite);

            if (ass.isPresent()) {
              assegnazioniDaSalvare.add(assegnazioneDB);
              assegnazioniConvertite.remove(ass.get());
            } else {
              assegnazioneDB.setDtFineVal(Timestamp.from(Instant.now()));
              assegnazioniDaSalvare.add(assegnazioneDB);
            }

          });

          assegnazioniConvertite.forEach(assegnazioniDaSalvare::add);
          assegnazioniDaSalvare.stream().forEach(ass -> ass.setCosmoTAttivita(attivitaDB));
          logger.info(methodName, "Salvataggio assegnazioni per attivita AGGIORNATA ");
          cosmoRAttivitaAssegnazioneRepository.save(assegnazioniDaSalvare);
        }
      }
    });

    return attivita.stream().filter(e -> !e.getEvento().equals(TASK_COMPLETATO)).map(e -> {

      CosmoTAttivita attivitaDB = cosmoTAttivitaRepository
          .findBycosmoTPraticaIdAndLinkAttivita(praticaSuDB.getId(), e.getLinkAttivita());

      if (attivitaDB == null) {
        CosmoTAttivita singolaAttivita = attivitaMapper.toCosmoTAttivita(e);
        singolaAttivita.setDtInserimento(Timestamp.from(Instant.now()));
        singolaAttivita.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
        singolaAttivita.setCosmoTPratica(praticaSuDB);
        CosmoTAttivita attivitaSalvata = cosmoTAttivitaRepository.save(singolaAttivita);
        inserisciAssegnazioni(attivitaSalvata, e);
        if (!CollectionUtils.isEmpty(attivitaSalvata.getCosmoRAttivitaAssegnaziones()))
          cosmoRAttivitaAssegnazioneRepository
          .save(attivitaSalvata.getCosmoRAttivitaAssegnaziones());

        return attivitaSalvata;

      } else {
        attivitaDB.setDescrizione(e.getDescrizione());
        attivitaDB.setLinkAttivita(e.getLinkAttivita());
        attivitaDB.setLinkAttivitaEsterna(e.getLinkAttivitaEsterna());
        attivitaDB.setNome(e.getNome());

        attivitaDB.setDtUltimaModifica(Timestamp.from(Instant.now()));
        attivitaDB.setUtenteUltimaModifica(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
        logger.info(methodName, "Salvataggio attivita COMPLETATA ");
        attivitaDB = cosmoTAttivitaRepository.save(attivitaDB);

        return attivitaDB;
      }

    }).collect(Collectors.toList());

  }

  private void aggiornaAttivitaTerminata(CosmoTAttivita attivitaDB, CosmoTPratica praticaSuDB) {

    var now = Timestamp.from(Instant.now());

    // termina le attivita' figlie
    List<CosmoTAttivita> subtasks =
        cosmoTAttivitaRepository.findByField(CosmoTAttivita_.parent, attivitaDB);
    for (CosmoTAttivita subtask : subtasks) {
      aggiornaAttivitaTerminata(subtask, praticaSuDB);
    }

    attivitaDB.setDtCancellazione(now);
    attivitaDB.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    List<CosmoRAttivitaAssegnazione> assegnazioniDB = cosmoRAttivitaAssegnazioneRepository
        .findByCosmoTAttivitaIdAndDtFineValIsNull(attivitaDB.getId());

    assegnazioniDB.forEach(assegnazioneDB -> assegnazioneDB.setDtFineVal(now));
    cosmoRAttivitaAssegnazioneRepository.save(assegnazioniDB);
  }

  private CosmoTAttivita inserisciAssegnazioni(CosmoTAttivita cosmoAttivita, Attivita attivita) {

    List<CosmoRAttivitaAssegnazione> assegnazioniDaSalvare = new ArrayList<>();

    attivita.getAssegnazione().stream().forEach(ass -> {
      CosmoRAttivitaAssegnazione cosmoRAttivitaAssegnazione =
          attivitaAssegnazioneMapper.toCosmoRAttivitaAssegnazione(ass);
      cosmoRAttivitaAssegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
      assegnazioniDaSalvare.add(cosmoRAttivitaAssegnazione);
    });
    assegnazioniDaSalvare.stream().forEach(ass -> ass.setCosmoTAttivita(cosmoAttivita));
    cosmoAttivita.setCosmoRAttivitaAssegnaziones(assegnazioniDaSalvare);
    return cosmoAttivita;
  }

  private Optional<CosmoRAttivitaAssegnazione> cercaAssegnazione(
      CosmoRAttivitaAssegnazione assegnazioneDB,
      List<CosmoRAttivitaAssegnazione> assegnazione) {

    return assegnazione.stream()
        .filter(ass -> (ass.getIdGruppo() == null && assegnazioneDB.getIdGruppo() == null
        || (ass.getIdGruppo() != null
        && ass.getIdGruppo().equals(assegnazioneDB.getIdGruppo())))

            && (ass.getIdUtente() == null && assegnazioneDB.getIdUtente() == null
            || (ass.getIdUtente() != null
            && ass.getIdUtente().equals(assegnazioneDB.getIdUtente())))

            && (ass.getAssegnatario() == null && assegnazioneDB.getAssegnatario() == null
            || (ass.getAssegnatario() != null
            && ass.getAssegnatario().equals(assegnazioneDB.getAssegnatario()))))
        .findFirst();

  }

  @Override
  @Transactional
  public Boolean putAttivitaIdAttivita(String idAttivita) {

    Long parsedIdUtente;
    try {
      parsedIdUtente = SecurityUtils.getUtenteCorrente().getId();
    } catch (Exception e) {
      throw new BadRequestException(UTENTE_NON_VALIDO);
    }

    CosmoTAttivita att =
        cosmoTAttivitaRepository.findBylinkAttivitaAndDtCancellazioneIsNull("tasks/" + idAttivita);

    if (att == null) {
      throw new it.csi.cosmo.common.exception.NotFoundException(
          String.format(ATTIVITA_NON_TROVATA, idAttivita));
    }

    List<CosmoRAttivitaAssegnazione> ass = cosmoRAttivitaAssegnazioneRepository
        .findByCosmoTAttivitaIdAndDtFineValIsNullAndAssegnatarioIsTrue(att.getId());
    if (!ass.isEmpty()) {
      return false;
    }


    assegnaAttivita(parsedIdUtente, att);
    return true;
  }

  private CosmoRAttivitaAssegnazione assegnaAttivita(Long parsedIdUtente, CosmoTAttivita att) {
    CosmoRAttivitaAssegnazione assegnazione = new CosmoRAttivitaAssegnazione();
    assegnazione.setAssegnatario(true);
    assegnazione.setCosmoTAttivita(att);
    assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
    assegnazione.setIdUtente(parsedIdUtente.intValue());
    return cosmoRAttivitaAssegnazioneRepository.save(assegnazione);
  }

  @Override
  public Attivita getPraticheAttivitaIdAttivita(String linkAttivita) {
    CosmoTAttivita att = cosmoTAttivitaRepository
        .findBylinkAttivitaAndDtCancellazioneIsNull("tasks/" + linkAttivita);
    if (att != null) {
      return attivitaMapper.toAttivita(att, null);
    }
    return null;
  }

  @Override
  public List<Attivita> getAttivitaIdPratica(String idPratica) {

    if (null == idPratica) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "idPratica");
      logger.error("getAttivitaIdPratica", parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }

    return attivitaMapper.toAttivita(cosmoTAttivitaRepository
        .findAll(CosmoTAttivitaSpecifications.findInCaricoAUtente(Long.valueOf(idPratica),
            SecurityUtils.getUtenteCorrente())),
        SecurityUtils.getUtenteCorrente());
  }

}
