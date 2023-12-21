/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTUtente"
 */

public interface CosmoTUtenteRepository extends CosmoTRepository<CosmoTUtente, Long> {

  CosmoTUtente findByCodiceFiscale(String codiceFiscale);

  Page<CosmoTUtente> findByCosmoRUtenteEntesCosmoTEnteIdAndDtCancellazioneNullOrderByCognome(
      Long id, Pageable pageable);

  List<CosmoTUtente> findByCodiceFiscaleIn(Collection<String> codiceFiscale);

  List<CosmoTUtente> findByIdIn(List<Long> id);

}
