/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTUtente;

public interface UtentiBatchService {



  /**
   * Metodo che verifica se esiste un ente in base al search field
   *
   * @param codice il valore con cui eseguire la search
   * @return l'optional  con l'ente
   */
  Optional<CosmoTEnte> findEnteByCodiceFiscaleOrCodiceIpa(
      String codice);



  /**
   * Metodo che gestisce il save/update di un utente
   *
   * @param codiceFiscale
   * @param nome
   * @param cognome
   * @param telefono
   * @param mail
   * @param dataInizioValidita
   * @param configurazioneEnte
   * @param dataFineValidita
   * @param profili
   * @param gruppi
   * @param idEnte
   * @return void
   */
  void saveUtenteBatch(String codiceFiscale, String nome, String cognome, String telefono, String mail,
      String dataInizioValidita, String dataFineValidit, CosmoTEnte ente,
      CosmoCConfigurazioneEnte configurazioneEnte);


  /**
   * Metodo che verifica se esiste un profilo in base al search field
   *
   * @param codice il valore con cui eseguire la search
   * @return l'optional  con il profilo
   */
  Optional<CosmoTProfilo> findProfiloByFieldEqualsIgnoreCase(SingularAttribute<CosmoTProfilo, String> field,
      String value, Long excludeId);

  /**
   * Metodo che verifica se esiste un gruppo in base a nome e ente
   *
   * @param nome
   * @param ente
   * @return l'optional  con il gruppo
   */
  Optional<CosmoTGruppo> findGruppoByCodiceAndEnte(String codice, CosmoTEnte ente);


  /**
   * Metodo che verifica se esiste un utente in base al search field
   *
   * @param field
   * @return l'optional  con l'utente
   */
  Optional<CosmoTUtente> findUtenteByFieldEqualsIgnoreCase(SingularAttribute<CosmoTUtente, String> field,
      String value, Long excludeId);


  /**
   * Metodo che gestisce l'inserimento/aggiornamento dei profili dell'utente in relazione all'ente
   *
   * @param field
   * @return l'optional  con l'utente
   */

  void saveProfiliUtente(CosmoTUtente utente, CosmoTEnte ente,
      CosmoCConfigurazioneEnte configurazioneEnte, List<CosmoTProfilo> profili);


  /**
   * Metodo che gestisce l'inserimento/aggiornamento dei gruppi dell'utente
   *
   * @param field
   * @return l'optional  con l'utente
   */
  void saveGruppiUtente(CosmoTUtente utente, List<CosmoTGruppo> gruppi);


  /**
   * Metodo che verifica se esiste una configurazione ente in base a idEnte e chiave
   *
   * @param idEnte idEnte con cui eseguire la search
   * @param chiave chiave con cui eseguire la search
   * @return l'optional con l'ente
   */

  Optional<CosmoCConfigurazioneEnte> findConfigurazioneEnteByIdEnteAndChiave(Long idEnte,
      String chiave);



}
