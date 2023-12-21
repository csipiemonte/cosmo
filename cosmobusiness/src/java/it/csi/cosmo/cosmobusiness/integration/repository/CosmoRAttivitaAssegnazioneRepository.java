/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.repository.CosmoRRepository;

public interface CosmoRAttivitaAssegnazioneRepository
extends CosmoRRepository<CosmoRAttivitaAssegnazione, Long> {

  void deleteByCosmoTAttivita(CosmoTAttivita attivita);

  List<CosmoRAttivitaAssegnazione> findByCosmoTAttivitaIdAndDtFineValIsNull(Long idAttivita);

  List<CosmoRAttivitaAssegnazione> findByCosmoTAttivitaIn(List<CosmoTAttivita> listaAttivita);

  CosmoRAttivitaAssegnazione findOneByCosmoTAttivitaIdAndDtFineValIsNullAndIdUtente(Long idAttivita, Integer idUtente);

  List<CosmoRAttivitaAssegnazione> findByCosmoTAttivitaIdAndDtFineValIsNullAndUtenteNotNullAndUtenteCodiceFiscaleNotAndUtenteDtCancellazioneNull(
      Long idAttivita, String codiceFiscale);

  List<CosmoRAttivitaAssegnazione> findAllByCosmoTAttivitaLinkAttivitaAndDtFineValIsNotNullAndIdUtenteOrderByDtFineValDesc(
      String taskId, Integer idUtente);

}
