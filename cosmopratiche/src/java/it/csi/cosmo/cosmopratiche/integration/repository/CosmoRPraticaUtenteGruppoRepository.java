/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.repository.CosmoRRepository;

public interface CosmoRPraticaUtenteGruppoRepository
    extends CosmoRRepository<CosmoRPraticaUtenteGruppo, Long> {

  Page<CosmoRPraticaUtenteGruppo> findByCosmoTUtenteIdAndCosmoDTipoCondivisionePraticaCodiceAndDtFineValIsNull(
      Long idUtente, String codiceTipoCondivisione, Pageable p);

  Page<CosmoRPraticaUtenteGruppo> findByCosmoTUtenteCodiceFiscaleAndCosmoDTipoCondivisionePraticaCodiceAndDtFineValIsNull(
      String codiceFiscale, String codiceTipoCondivisione, Pageable p);

  List<CosmoRPraticaUtenteGruppo> findByCosmoTUtenteIdAndCosmoDTipoCondivisionePraticaCodiceAndCosmoTPraticaIdIn(
      Long idUtente, String codiceTipoCondivisione, Collection<Long> lista);

  List<CosmoRPraticaUtenteGruppo> findByCosmoTUtenteCodiceFiscaleAndCosmoDTipoCondivisionePraticaCodiceAndCosmoTPraticaIdIn(
      String codiceFiscale, String codiceTipoCondivisione, Collection<Long> lista);

}
