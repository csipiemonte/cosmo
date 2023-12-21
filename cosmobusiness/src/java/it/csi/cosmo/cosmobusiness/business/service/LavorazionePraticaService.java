/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import java.io.Serializable;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

public interface LavorazionePraticaService {

  /**
   * @param idPratica
   * @param body
   * @return RiferimentoOperazioneAsincrona
   */
  public RiferimentoOperazioneAsincrona postPraticaAttivitaConferma(Long idPratica, Long idAttivita,
      Task body);

  /**
   * @param idPratica
   * @param body
   * @return RiferimentoOperazioneAsincrona
   */
  public RiferimentoOperazioneAsincrona postPraticaAttivitaSalva(Long idPratica, Long idAttivita,
      Task body);

  /**
   * @param idPratica
   * @param idAttivita
   * @param body
   * @return
   */
  public AssegnaAttivitaResponse postPraticaAttivitaAssegna(Long idPratica, Long idAttivita,
      AssegnaAttivitaRequest body);

  /**
   * 
   * @param idPratica
   * @param idAttivita
   * @return
   */
  public AssegnaAttivitaResponse postPraticaAttivitaAssegnaAMe(Long idPratica, Long idAttivita);

  /**
   * @param idPratica
   * @param body
   * @return
   */
  public AssegnaAttivitaResponse postPraticaAssegna(Long idPratica, AssegnaAttivitaRequest body);

  /**
   * @param idPratica
   * @param attivitaInput
   * @param body
   * @param isSubtask
   * @param verificaLock
   * @param task
   */
  void eseguiLavorazioneAttivita(Long idPratica, CosmoTAttivita attivitaInput, Task body,
      boolean verificaLock, LongTask<Serializable> task);

  /**
   * @param idPratica
   * @param attivitaInput
   * @param body
   * @param isSubtask
   * @param verificaLock
   * @param task
   */
  void eseguiConfermaAttivita(Long idPratica, CosmoTAttivita attivitaInput, Task body,
      boolean verificaLock, LongTask<Serializable> task);
}
