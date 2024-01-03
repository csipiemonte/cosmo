/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.RelazionePraticaUtente;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmopratiche.business.service.UtentiService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaUtenteGruppoRepository;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;

@Service
@Transactional
public class UtentiServiceImpl implements UtentiService {

  @Autowired
  private CosmoRPraticaUtenteGruppoRepository praticaUtenteRepo;

  @Autowired
  private CosmoTPraticaRepository praticaRepo;

  @Autowired
  private CosmoTPraticheMapper mapper;

  // gestione preferiti

  @Override
  public Pratica putUtentiPratichePreferiteIdPratica(String idPratica) {
    return setPreferita(Long.valueOf(idPratica));
  }

  @Override
  public Void deleteUtentiPratichePreferiteIdPratica(String idPratica) {
    unsetPreferita(Long.valueOf(idPratica));
    return null;
  }

  private Pratica setPreferita(Long idPratica) {
    // verifico che ci sia un utente corrente e che abbia un ID
    UserInfoDTO utente = SecurityUtils.getUtenteCorrente();
    if (utente.getId() == null) {
      throw new UnauthorizedException();
    }

    Timestamp now = Timestamp.from(Instant.now());
    RelazionePraticaUtente tipoRelazione = RelazionePraticaUtente.PREFERITA;

    // ottengo la pratica (deve essere valida)
    CosmoTPratica pratica =
        praticaRepo.findOneNotDeleted(idPratica).orElseThrow(NotFoundException::new);

    // verifico se e' marcata come preferita
    List<CosmoRPraticaUtenteGruppo> preferiteAttuali =
        getRelazionePreferitaPraticaUtente(idPratica, utente.getId(), tipoRelazione);

    if (preferiteAttuali.isEmpty()) {
      // non e' attualmente una preferita
      CosmoRPraticaUtenteGruppo newRel = new CosmoRPraticaUtenteGruppo();
      newRel.setCosmoDTipoCondivisionePratica(praticaUtenteRepo
          .reference(CosmoDTipoCondivisionePratica.class, tipoRelazione.getCodice()));
      newRel.setCosmoTPratica(pratica);
      newRel.setCosmoTUtente(praticaUtenteRepo.reference(CosmoTUtente.class, utente.getId()));
      newRel.setDtInizioVal(now);
      praticaUtenteRepo.save(newRel);
    }

    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());
  }

  private Pratica unsetPreferita(Long idPratica) {
    // verifico che ci sia un utente corrente e che abbia un ID
    UserInfoDTO utente = SecurityUtils.getUtenteCorrente();
    if (utente.getId() == null) {
      throw new UnauthorizedException();
    }

    Timestamp now = Timestamp.from(Instant.now());
    RelazionePraticaUtente tipoRelazione = RelazionePraticaUtente.PREFERITA;

    // ottengo la pratica (deve essere valida)
    CosmoTPratica pratica =
        praticaRepo.findOneNotDeleted(idPratica).orElseThrow(NotFoundException::new);

    // verifico se e' marcata come preferita
    List<CosmoRPraticaUtenteGruppo> preferiteAttuali =
        getRelazionePreferitaPraticaUtente(idPratica, utente.getId(), tipoRelazione);

    if (!preferiteAttuali.isEmpty()) {
      // elimino le relazioni
      preferiteAttuali.forEach(r -> {
        r.setDtFineVal(now);
        praticaUtenteRepo.save(r);
      });
    }

    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());
  }

  private List<CosmoRPraticaUtenteGruppo> getRelazionePreferitaPraticaUtente(Long idPratica,
      Long idUtente,
      RelazionePraticaUtente tipoRelazione) {
    //@formatter:off
    return praticaUtenteRepo.findAllActive((root, cq, cb) ->
    cb.and(
        cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id), idUtente),
        cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id), idPratica),
        cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice), tipoRelazione.getCodice())
        )
        );
    //@formatter:on
  }

}
