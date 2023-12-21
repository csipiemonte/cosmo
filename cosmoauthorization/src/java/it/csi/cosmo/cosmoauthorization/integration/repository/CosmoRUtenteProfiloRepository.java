/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoRUtenteProfilo"
 */

public interface CosmoRUtenteProfiloRepository
extends CosmoRRepository<CosmoRUtenteProfilo, String> {


  CosmoRUtenteProfilo findByCosmoTUtenteAndCosmoTProfiloAndCosmoTEnte(CosmoTUtente utente,
      CosmoTProfilo profilo, CosmoTEnte ente);

  List<CosmoRUtenteProfilo> findByCosmoTUtenteAndCosmoTEnte(CosmoTUtente utente,CosmoTEnte ente);

}
