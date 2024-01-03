/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.RelazionePraticaUtente;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmopratiche.business.service.CondivisioniPraticheService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaUtenteGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
public class CondivisioniPraticheServiceImpl implements CondivisioniPraticheService {
  
  private static final String CLASS_NAME = CondivisioniPraticheServiceImpl.class.getSimpleName();
  
  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoRPraticaUtenteGruppoRepository praticaUtenteGruppoRepo;

  @Autowired
  private CosmoTPraticaRepository praticaRepo;

  @Autowired
  private CosmoTUtenteRepository utenteRepo;

  @Autowired
  private CosmoTGruppoRepository gruppoRepo;

  @Autowired
  private CosmoTPraticheMapper mapper;

  @Autowired
  private CosmoNotificationsNotificheFeignClient notificheFeignClient;


  @Override
  public CondivisionePratica creaCondivisione(Long idPratica, CreaCondivisionePraticaRequest body) {
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    Timestamp now = Timestamp.from(Instant.now());

    // verifico se ci sono condivisioni in conflitto

    UserInfoDTO utenteCorrente = SecurityUtils.getUtenteCorrente();

    List<CosmoRPraticaUtenteGruppo> giaEsistenti = new LinkedList<>();

    if(body.getIdUtente() != null) {
      giaEsistenti.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id),
              body.getIdUtente()),
          cb.equal(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtenteCondivisore).get(CosmoTUtente_.id),
              utenteCorrente.getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
              idPratica),
          cb.equal(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica)
              .get(CosmoDTipoCondivisionePratica_.codice),
              RelazionePraticaUtente.CONDIVISA.getCodice())
          )
          ));
    } else if(body.getIdGruppo() != null){
      giaEsistenti.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTGruppo).get(CosmoTGruppo_.id),
              body.getIdGruppo()),
          cb.equal(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtenteCondivisore).get(CosmoTUtente_.id),
              utenteCorrente.getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
              idPratica),
          cb.equal(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica)
              .get(CosmoDTipoCondivisionePratica_.codice),
              RelazionePraticaUtente.CONDIVISA.getCodice())
          )
          ));
    } else {
      throw new BadRequestException("Request non corretta, inserire un utente o un gruppo");
    }


    if (!giaEsistenti.isEmpty()) {
      throw new ConflictException("Condivisione gia' attiva");
    }

    // la pratica deve esistere ed essere attiva
    CosmoTPratica pratica = praticaRepo.findOneNotDeleted(idPratica)
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));

    // inserisco la nuova condivisione
    CosmoRPraticaUtenteGruppo newRel = new CosmoRPraticaUtenteGruppo();
    newRel.setCosmoDTipoCondivisionePratica(praticaUtenteGruppoRepo.reference(
        CosmoDTipoCondivisionePratica.class,
        RelazionePraticaUtente.CONDIVISA.getCodice()));
    newRel.setCosmoTPratica(pratica);

    CosmoTUtente utente = null;
    CosmoTGruppo gruppo = null;
    if(body.getIdUtente() !=null) {
      // l'utente deve esistere ed essere attivo
      utente = utenteRepo.findOneNotDeleted(body.getIdUtente())
          .orElseThrow(() -> new NotFoundException("Utente non trovato"));
    } else if( body.getIdGruppo() !=null) {
      gruppo = gruppoRepo.findOneNotDeleted(body.getIdGruppo())
          .orElseThrow(() -> new NotFoundException("Gruppo non trovato"));
    }

    newRel.setCosmoTUtenteCondivisore(
        praticaUtenteGruppoRepo.reference(CosmoTUtente.class, utenteCorrente.getId()));
    newRel.setCosmoTUtente(utente);
    newRel.setCosmoTGruppo(gruppo);
    newRel.setDtInizioVal(now);
    newRel = praticaUtenteGruppoRepo.save(newRel);

    creaNotificaCondivisione(utenteCorrente.getId(),
        utente == null ? null : utente.getCodiceFiscale(),
            gruppo == null ? null : gruppo, pratica,
                TipoNotifica.CONDIVISIONE_PRATICA.getAzione(), false);

    return mapper.toCondivisionePratica(newRel);
  }

  @Override
  public void rimuoviCondivisione(Long idPratica, Long idCondivisione) {
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(idCondivisione, "idCondivisione");

    // ottieni la condivisione e verifica che sia attiva
    CosmoRPraticaUtenteGruppo condivisione =
        praticaUtenteGruppoRepo.findOneActive(idCondivisione).orElseThrow(NotFoundException::new);

    // deve essere di tipo CONDIVISA
    if (!RelazionePraticaUtente.CONDIVISA.getCodice()
        .equals(condivisione.getCosmoDTipoCondivisionePratica().getCodice())) {
      throw new BadRequestException();
    }

    // l'id pratica deve essere gerarchicamente coerente
    if (!condivisione.getCosmoTPratica().getId().equals(idPratica)) {
      throw new NotFoundException();
    }

    UserInfoDTO utenteCorrente = SecurityUtils.getUtenteCorrente();

    // due scenari: l'utente rimuove una condivisione creata da lui oppure rimuove una condivisione
    // in ricezione (associata a lui o ad un suo gruppo).
    if (condivisione.getCosmoTUtenteCondivisore().getId().equals(utenteCorrente.getId())) {
      rimuoviCondivisioneCreataDaMe(condivisione);

      if (condivisione.getCosmoTUtente() != null) {

        creaNotificaCondivisione(utenteCorrente.getId(),
            condivisione.getCosmoTUtente().getCodiceFiscale(), null,
            condivisione.getCosmoTPratica(), TipoNotifica.CONDIVISIONE_PRATICA.getAzione(), true);
      } else if (condivisione.getCosmoTGruppo() != null) {
        creaNotificaCondivisione(utenteCorrente.getId(), null, condivisione.getCosmoTGruppo(),
            condivisione.getCosmoTPratica(), TipoNotifica.CONDIVISIONE_PRATICA.getAzione(), true);
      }

    } else if (condivisione.getCosmoTUtente()!= null && condivisione.getCosmoTUtente().getId().equals(utenteCorrente.getId())) {
      rimuoviCondivisioneInRicezione(condivisione, true);

    } else if (condivisione.getCosmoTGruppo() != null) {

      var gruppo = gruppoRepo.findOneNotDeleted((root, cq, cb) -> {
        ListJoin<CosmoTGruppo, CosmoTUtenteGruppo> joinUtente =
            root.join(CosmoTGruppo_.associazioniUtenti, JoinType.LEFT);

        return cb.and(
            cb.equal(root.get(CosmoTGruppo_.codice), condivisione.getCosmoTGruppo().getCodice()),
            cb.isNotNull(root.get(CosmoTGruppo_.ente)),
            cb.equal(root.get(CosmoTGruppo_.ente).get(CosmoTEnte_.id),
                utenteCorrente.getEnte().getId()),
            cb.isNotNull(joinUtente.get(CosmoTUtenteGruppo_.utente)),
            cb.equal(joinUtente.get(CosmoTUtenteGruppo_.utente).get(CosmoTUtente_.codiceFiscale),
                utenteCorrente.getCodiceFiscale()),
            cb.isNull(joinUtente.get(CosmoTEntity_.dtCancellazione))

            );
      });

      if (gruppo.isPresent()) {
        rimuoviCondivisioneInRicezione(condivisione, false);

        creaNotificaCondivisione(utenteCorrente.getId(), null, gruppo.get(),
            condivisione.getCosmoTPratica(), TipoNotifica.CONDIVISIONE_PRATICA.getAzione(), true);

      }

    } else {
      // questa collaborazione non riguarda l'utente
      throw new ForbiddenException();
    }
  }

  private void rimuoviCondivisioneCreataDaMe(CosmoRPraticaUtenteGruppo entity) {

    Timestamp now = Timestamp.from(Instant.now());

    entity.setDtFineVal(now);
    praticaUtenteGruppoRepo.save(entity);

    rimuoviPreferita(entity);
  }

  private void rimuoviCondivisioneInRicezione(CosmoRPraticaUtenteGruppo entity, boolean personale) {
    // rimuovo anche tutte le altre per la stessa pratica

    Timestamp now = Timestamp.from(Instant.now());

    //@formatter:off
    List<CosmoRPraticaUtenteGruppo> daRimuovere = new LinkedList<>();

    if(personale) {
      daRimuovere.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id), entity.getCosmoTUtente().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id), entity.getCosmoTPratica().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice), RelazionePraticaUtente.CONDIVISA.getCodice())
          )
          ));
    } else {
      daRimuovere.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTGruppo).get(CosmoTGruppo_.id), entity.getCosmoTGruppo().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id), entity.getCosmoTPratica().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice), RelazionePraticaUtente.CONDIVISA.getCodice())
          )
          ));
    }

    //@formatter:on

    daRimuovere.forEach(e -> {
      e.setDtFineVal(now);
      praticaUtenteGruppoRepo.save(e);
    });

    rimuoviPreferita(entity);
  }

  private void rimuoviPreferita(CosmoRPraticaUtenteGruppo entity) {

    Timestamp now = Timestamp.from(Instant.now());

    //@formatter:off
    List<CosmoRPraticaUtenteGruppo> daRimuovere = new LinkedList<>();
    if(entity.getCosmoTUtente() !=null ) {
      daRimuovere.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id), entity.getCosmoTUtente().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id), entity.getCosmoTPratica().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice), RelazionePraticaUtente.PREFERITA.getCodice())
          )));
    } else if(entity.getCosmoTGruppo() != null) {
      List<Long> idsUtenti = entity.getCosmoTGruppo().getAssociazioniUtenti().stream()
          .filter(CosmoTEntity::nonCancellato).map(CosmoTUtenteGruppo::getIdUtente).collect(Collectors.toList());

      daRimuovere.addAll(praticaUtenteGruppoRepo.findAllActive((root, cq, cb) ->
      cb.and(
          root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id).in(idsUtenti),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id), entity.getCosmoTPratica().getId()),
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice), RelazionePraticaUtente.PREFERITA.getCodice())
          )));
    }

    //@formatter:on

    daRimuovere.forEach(e -> {
      e.setDtFineVal(now);
      praticaUtenteGruppoRepo.save(e);
    });
  }

  private void creaNotificaCondivisione(Long idUtenteCondivisore, String cfUtente,
      CosmoTGruppo gruppo, CosmoTPratica pratica, String tipoNotifica,
      boolean rimozioneCondivisione) {
    
    final var method = "creaNotificaCondivisione";

    CosmoTUtente utenteCondivisore = utenteRepo.findOne(idUtenteCondivisore);

    final String nome = utenteCondivisore != null
        ? utenteCondivisore.getNome() + " " + utenteCondivisore.getCognome()
        : "UNKNOW";

    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setIdPratica(pratica.getId());
    String messaggio = "UNKNOW";

    if (rimozioneCondivisione) {
      messaggio =
          String.format(Constants.RIMOZIONE_CONDIVISIONE_PRATICA, nome, pratica.getOggetto());
      if (cfUtente != null) {
        notifica.setUtentiDestinatari(Arrays.asList(cfUtente));
      }

      if (gruppo != null) {
        notifica.setGruppiDestinatari(Arrays.asList(gruppo.getCodice()));
      }
    } else {
      if (cfUtente != null) {
        notifica.setUtentiDestinatari(Arrays.asList(cfUtente));
        messaggio =
            String.format(Constants.CONDIVISIONE_PRATICA_UTENTE, nome, pratica.getOggetto());
      }

      if (gruppo != null) {
        notifica.setGruppiDestinatari(Arrays.asList(gruppo.getCodice()));
        messaggio = String.format(Constants.CONDIVISIONE_PRATICA_GRUPPO, nome,
            gruppo.getDescrizione(), pratica.getOggetto());
      }
    }

    notifica.setMessaggio(messaggio);


    if (SecurityUtils.getUtenteCorrente().getEnte() != null) {
      notifica.setCodiceIpaEnte(SecurityUtils.getUtenteCorrente().getEnte().getTenantId());
    }
    notifica.setPush(Boolean.TRUE);
    notifica.setTipoNotifica(tipoNotifica);

    try {
    CreaNotificheRequest notifiche = new CreaNotificheRequest();
    notifiche.setNotifiche(Arrays.asList(notifica));

    notificheFeignClient.postNotifications(notifiche);
    } catch (Exception e) {
    logger.error(method, "error sending notifications batch", e);
  }

  }


}
