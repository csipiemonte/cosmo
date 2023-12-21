/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTUtenteGruppo"
 */

public interface CosmoTUtenteGruppoRepository extends CosmoTRepository<CosmoTUtenteGruppo, Long> {

  CosmoTUtenteGruppo findOneByGruppoAndUtenteAndDtCancellazioneNull(
      CosmoTGruppo gruppo, CosmoTUtente utente);

  List<CosmoTUtenteGruppo>findByUtente(CosmoTUtente utente);


}
