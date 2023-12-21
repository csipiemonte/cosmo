/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK_;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiBatchService;
import it.csi.cosmo.cosmoauthorization.dto.exception.UtentiBatchException;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;

@Service
@Transactional
public class UtentiBatchServiceImpl implements UtentiBatchService {

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoCConfigurazioneEnteRepository configurazioneEnteRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTProfiloRepository cosmoTProfiloRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  CosmoRUtenteEnteRepository cosmoRUtenteEnteRepository;

  @Autowired
  CosmoTUtenteGruppoRepository cosmoTUtenteGruppoRepository;

  @Autowired
  CosmoRUtenteProfiloRepository cosmoRUtenteProfiloRepository;


  @Override
  public Optional<CosmoTEnte> findEnteByCodiceFiscaleOrCodiceIpa(String codice) {

    Optional<CosmoTEnte> optionalEnte = findEnteByFieldEqualsIgnoreCase(CosmoTEnte_.codiceFiscale, codice, null);

    if (optionalEnte.isPresent())
      return optionalEnte;

    optionalEnte = findEnteByFieldEqualsIgnoreCase(CosmoTEnte_.codiceIpa, codice, null);

    return optionalEnte;
  }


  @Override
  public Optional<CosmoCConfigurazioneEnte> findConfigurazioneEnteByIdEnteAndChiave(Long idEnte,
      String chiave) {
    CosmoCConfigurazioneEnte configurazione = configurazioneEnteRepository.findOne(
        (root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                chiave),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                idEnte),
            cb.isNull(root.get(CosmoCEntity_.dtFineVal))));

    return Optional.ofNullable(configurazione);

  }

  private Optional<CosmoTEnte> findEnteByFieldEqualsIgnoreCase(SingularAttribute<CosmoTEnte, String> field,
      String value, Long excludeId) {
    return cosmoTEnteRepository.findAllNotDeleted((root, queru, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value.toUpperCase());
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTEnte_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  @Override
  public Optional<CosmoTUtente> findUtenteByFieldEqualsIgnoreCase(SingularAttribute<CosmoTUtente, String> field,
      String value, Long excludeId) {
    return this.cosmoTUtenteRepository.findAll((root, queru, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value.toUpperCase());
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTUtente_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  @Override
  public Optional<CosmoTProfilo> findProfiloByFieldEqualsIgnoreCase(SingularAttribute<CosmoTProfilo, String> field,
      String value, Long excludeId) {
    return this.cosmoTProfiloRepository.findAllNotDeleted((root, queru, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value.toUpperCase());
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTProfilo_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  @Override
  public Optional<CosmoTGruppo> findGruppoByCodiceAndEnte(String codice, CosmoTEnte ente) {
    return Optional.ofNullable(this.cosmoTGruppoRepository.findByCodiceAndEnte(codice, ente));
  }



  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void saveUtenteBatch(String codiceFiscale, String nome, String cognome, String telefono, String mail,
      String dataInizioValidita, String dataFineValidita,
      CosmoTEnte ente, CosmoCConfigurazioneEnte configurazioneEnte) {

    CosmoTUtente utente = saveUtente(codiceFiscale, nome, cognome);
    saveUtenteEnte(utente, ente, mail, telefono, dataInizioValidita, dataFineValidita);

    List<CosmoRUtenteProfilo> profiliUtenteSuDB =
        this.cosmoRUtenteProfiloRepository.findByCosmoTUtenteAndCosmoTEnte(utente, ente);
    // se non ci sono profili associati all'utente oppure se sono tutti disabilitati
    // attivo/inserisco
    // quello
    // di default
    if (profiliUtenteSuDB.isEmpty()
        || !profiliUtenteSuDB.stream().noneMatch(IntervalloValiditaEntity::valido)) {
      Optional<CosmoRUtenteProfilo> optionalProfiloUtenteDefault =
          profiliUtenteSuDB.stream().filter(tempProfiloUtenteSuDB -> tempProfiloUtenteSuDB
              .getCosmoTProfilo().getCodice().equals(configurazioneEnte.getValore())).findAny();

      manageProfiloDefault(utente, ente, configurazioneEnte, optionalProfiloUtenteDefault);
    }


  }


  private void manageProfiloDefault(CosmoTUtente utente, CosmoTEnte ente,
      CosmoCConfigurazioneEnte configurazioneEnte,
      Optional<CosmoRUtenteProfilo> optionalProfiloUtenteDefault) {

    CosmoTProfilo profiloDefault = this
        .findProfiloByFieldEqualsIgnoreCase(CosmoTProfilo_.codice, configurazioneEnte.getValore(),
            null)
        .orElseThrow(() -> new UtentiBatchException(
            "Profilo " + configurazioneEnte.getValore() + " non registrato"));

    CosmoRUtenteProfilo profiloUtenteDefault =
        optionalProfiloUtenteDefault.isPresent() ? optionalProfiloUtenteDefault.get()
            : new CosmoRUtenteProfilo();
    profiloUtenteDefault.setCosmoTEnte(ente);
    profiloUtenteDefault.setCosmoTProfilo(profiloDefault);
    profiloUtenteDefault.setCosmoTUtente(utente);
    profiloUtenteDefault.setDtInizioVal(Timestamp.from(Instant.now()));
    profiloUtenteDefault.setDtFineVal(null);
    this.cosmoRUtenteProfiloRepository.save(profiloUtenteDefault);

  }

  private CosmoTUtente saveUtente(String codiceFiscale,String nome,String cognome) {

    Optional<CosmoTUtente> utenteOptional = this.findUtenteByFieldEqualsIgnoreCase(CosmoTUtente_.codiceFiscale,
        codiceFiscale, null);
    CosmoTUtente utente = utenteOptional.isEmpty() ? new CosmoTUtente() : utenteOptional.get();

    utente.setNome(nome);
    utente.setCognome(cognome);
    utente.setCodiceFiscale(codiceFiscale);
    utente.setDtCancellazione(null);
    utente.setUtenteCancellazione(null);

    utente = this.cosmoTUtenteRepository.save(utente);

    return utente;


  }

  private void saveUtenteEnte(CosmoTUtente utente, CosmoTEnte ente, String mail, String telefono,
      String dataInizioValidita, String dataFineValidita) {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Timestamp timestampDataValidita = null;
    Timestamp timestampDataFineValidita = null;

    try {
      Date parsedDataValidita;
      parsedDataValidita = StringUtils.isNotEmpty(dataInizioValidita) ? sdf.parse(dataInizioValidita)
          : new Date();
      timestampDataValidita = new Timestamp(parsedDataValidita.getTime());
      if (StringUtils.isNotEmpty(dataFineValidita)) {
        Date parsedDataFineValidita;
        parsedDataFineValidita = sdf.parse(dataFineValidita);
        timestampDataFineValidita = new Timestamp(parsedDataFineValidita.getTime());
      }

    } catch (ParseException e) {
      throw new UtentiBatchException(e.getMessage());
    }

    List<CosmoRUtenteEnte> utenteEntes = utente.getCosmoRUtenteEntes() != null ? utente.getCosmoRUtenteEntes()
        : new ArrayList<>();
    Optional<CosmoRUtenteEnte> associazioneEnteUtenteOptional = utenteEntes.stream()
        .filter(tempEnteUtente -> tempEnteUtente.getCosmoTEnte().getId().equals(ente.getId()))
        .findFirst();
    CosmoRUtenteEnte utenteEnte = associazioneEnteUtenteOptional.isEmpty() ? new CosmoRUtenteEnte()
        : associazioneEnteUtenteOptional.get();

    if(utenteEnte.getId()==null) {
      CosmoRUtenteEntePK pk = new CosmoRUtenteEntePK();
      pk.setIdEnte(ente.getId());
      pk.setIdUtente(utente.getId());
      utenteEnte.setId(pk);
    }




    utenteEnte.setTelefono(telefono);
    utenteEnte.setEmail(mail);

    if(utenteEnte.getDtInizioVal()==null)
      utenteEnte.setDtInizioVal(timestampDataValidita);

    utenteEnte.setDtFineVal(timestampDataFineValidita);
    utenteEnte.setCosmoTEnte(ente);
    utenteEnte.setCosmoTUtente(utente);
    this.cosmoRUtenteEnteRepository.save(utenteEnte);
  }



  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void saveProfiliUtente(CosmoTUtente utente, CosmoTEnte ente,
      CosmoCConfigurazioneEnte configurazioneEnte, List<CosmoTProfilo> profili) {

    List<CosmoRUtenteProfilo>profiliUtenteSuDB = this.cosmoRUtenteProfiloRepository.findByCosmoTUtenteAndCosmoTEnte(utente, ente);

    profiliUtenteSuDB.stream().forEach(tempProfiloUtenteSuDB -> {
      if (profili.stream().filter(tempNewProfilo -> tempNewProfilo.getId()
          .equals(tempProfiloUtenteSuDB.getCosmoTProfilo().getId())).findAny().isEmpty()) {
        if(tempProfiloUtenteSuDB.getDtFineVal()==null) {
          tempProfiloUtenteSuDB.setDtFineVal(Timestamp.from(Instant.now()));
          this.cosmoRUtenteProfiloRepository.save(tempProfiloUtenteSuDB);
        }
      }

    });


    profili.stream().forEach(temp -> {
      Optional<CosmoRUtenteProfilo> optionalAssoc = profiliUtenteSuDB.stream().filter(tempProfiloUtenteSuDB -> tempProfiloUtenteSuDB.getCosmoTProfilo().getId().equals(temp.getId())).findAny();
      CosmoRUtenteProfilo assoc = optionalAssoc.isPresent()?optionalAssoc.get() : new CosmoRUtenteProfilo();
      assoc.setCosmoTEnte(ente);
      assoc.setCosmoTProfilo(temp);
      assoc.setCosmoTUtente(utente);
      assoc.setDtInizioVal(Timestamp.from(Instant.now()));
      assoc.setDtFineVal(null);
      this.cosmoRUtenteProfiloRepository.save(assoc);

    });


    // se la lista è vuota attivo/inserisco quello di default
    if (profili.isEmpty()) {
      Optional<CosmoRUtenteProfilo> optionalProfiloUtenteDefault =
          profiliUtenteSuDB.stream().filter(tempProfiloUtenteSuDB -> tempProfiloUtenteSuDB
              .getCosmoTProfilo().getCodice().equals(configurazioneEnte.getValore())).findAny();
      this.manageProfiloDefault(utente, ente, configurazioneEnte, optionalProfiloUtenteDefault);
    }




  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void saveGruppiUtente(CosmoTUtente utente, List<CosmoTGruppo> gruppi) {

    List<CosmoTUtenteGruppo>gruppiUtenteSuDB = this.cosmoTUtenteGruppoRepository.findByUtente(utente);

    gruppiUtenteSuDB.stream().forEach(tempGruppoUtenteSuDB -> {
      if(gruppi.stream().filter(tempNewGruppo -> tempNewGruppo.getId().equals(tempGruppoUtenteSuDB.getGruppo().getId())).findAny().isEmpty()) {
        if(tempGruppoUtenteSuDB.getDtCancellazione()==null) {
          tempGruppoUtenteSuDB.setDtCancellazione(Timestamp.from(Instant.now()));
          this.cosmoTUtenteGruppoRepository.save(tempGruppoUtenteSuDB);
        }
      }

    });



    gruppi.stream().forEach(temp -> {
      Optional<CosmoTUtenteGruppo> optionalAssoc = gruppiUtenteSuDB.stream().filter(tempGruppoUtenteSuDB -> tempGruppoUtenteSuDB.getGruppo().getId().equals(temp.getId())).findAny();
      CosmoTUtenteGruppo assoc = optionalAssoc.isPresent()?optionalAssoc.get() : new CosmoTUtenteGruppo();
      assoc.setGruppo(temp);
      assoc.setUtente(utente);
      assoc.setIdUtente(utente.getId());
      assoc.setIdGruppo(temp.getId());
      assoc.setDtCancellazione(null);
      assoc.setUtenteCancellazione(null);
      this.cosmoTUtenteGruppoRepository.save(assoc);

    });





  }


}
