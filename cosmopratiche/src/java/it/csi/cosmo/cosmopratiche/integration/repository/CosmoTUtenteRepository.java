/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoTUtenteRepository extends CosmoTRepository<CosmoTUtente, Long> {

  @Query("select u.id from CosmoTUtente u where u.codiceFiscale = :cf")
  long id(@Param("cf") String codiceFiscale);

  CosmoTUtente findByCodiceFiscale(String cf);
}
