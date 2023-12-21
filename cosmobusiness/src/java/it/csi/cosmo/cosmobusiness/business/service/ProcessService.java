/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.AvanzamentoProcessoRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;

public interface ProcessService {

  /**
   * @param pratica
   * @param body
   */
  boolean registraCambioStato(CosmoTPratica pratica, String nuovoStatoRaw, Instant timestamp,
      boolean explicit);

  /**
   * @param attivita
   * @param task
   * @return
   */
  boolean aggiornaAssegnazioni(CosmoTAttivita attivita, TaskInstanceDTO task, CosmoTEnte ente,
      Map<String, Object> variabili);

  /**
   * @param attivita
   * @param assegnazioni
   * @param ente
   * @return
   */
  boolean aggiornaAssegnazioni(CosmoTAttivita attivita,
      List<CosmoRAttivitaAssegnazione> assegnazioni, CosmoTEnte ente);

  /**
   * @param attivita
   * @param assegnazioni
   * @param ente
   * @param fruitore
   * @return
   */
  boolean aggiornaAssegnazioni(CosmoTAttivita attivita,
      List<CosmoRAttivitaAssegnazione> assegnazioni, CosmoTEnte ente, CosmoTFruitore fruitore);

  /**
   * @param attivita
   */
  void tentaNotificaCreazioneTask(CosmoTAttivita attivita);

  /**
   * @param cosmoTAttivita
   */
  void aggiornaAttivitaTerminata(CosmoTAttivita cosmoTAttivita);

  /**
   * @param cosmoTAttivita
   */
  void aggiornaAttivitaAnnullata(CosmoTAttivita cosmoTAttivita);

  /**
   * @param attivitaDB
   * @param notifica
   */
  void aggiornaAttivitaAnnullata(CosmoTAttivita attivitaDB, boolean notifica,
      CosmoTFruitore fruitore);

  /**
   * @param pratica
   * @param task
   * @return
   */
  CosmoTAttivita importaNuovoTask(CosmoTPratica pratica, TaskInstanceDTO task,
      Map<String, Object> variabili);

  CosmoTAttivita importaNuovaAttivita(CosmoTPratica pratica, CosmoTAttivita attivita,
      CosmoTFruitore fruitore);

  /**
   * @param notifica
   */
  void accodaNotifica(CreaNotificaRequest notifica);

  /**
   * @param attivita
   * @param cf
   * @return
   */
  CosmoRAttivitaAssegnazione buildAssegnazioneDiretta(CosmoTAttivita attivita, String cf);

  /**
   * @param ente
   * @param attivita
   * @param codiceGruppo
   * @return
   */
  CosmoRAttivitaAssegnazione buildAssegnazioneGruppo(CosmoTEnte ente, CosmoTAttivita attivita,
      String codiceGruppo);

  /**
   * @param codice
   * @param codiceTipoPratica
   * @return
   */
  CosmoDStatoPratica getStatoPratica(String codice, String codiceTipoPratica);

  /**
   * @param pratica
   * @param timestamp
   */
  void terminaPratica(CosmoTPratica pratica, OffsetDateTime timestamp);

  void terminaPratica(CosmoTPratica pratica, OffsetDateTime timestamp, CosmoTFruitore fruitore);

  /**
   * @param pratica
   * @param timestamp
   */
  void annullaPratica(CosmoTPratica pratica, OffsetDateTime timestamp);

  void annullaPratica(CosmoTPratica pratica, OffsetDateTime timestamp, CosmoTFruitore fruitore);

  void avanzaProcesso(AvanzamentoProcessoRequest body);

}
