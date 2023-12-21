/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Sort;
import it.csi.cosmo.common.entities.CosmoTCallbackFruitore;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository
 */

public interface CosmoTCallbackFruitoreRepository
    extends CosmoTRepository<CosmoTCallbackFruitore, Long> {

  List<CosmoTCallbackFruitore> findByIdIn(Collection<Long> listaIdCallback, Sort sort);

}
