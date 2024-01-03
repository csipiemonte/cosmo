/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import it.csi.cosmo.common.entities.CosmoRPraticaPratica;
import it.csi.cosmo.common.repository.CosmoRRepository;

public interface CosmoRPraticaPraticaRepository
extends CosmoRRepository<CosmoRPraticaPratica, Long> {

  CosmoRPraticaPratica findOneByCosmoTPraticaDaIdAndCosmoTPraticaAIdAndCosmoDTipoRelazionePraticaCodice(
      Long idPraticaDa, Long idPraticaA, String tipoRelazione);
}
