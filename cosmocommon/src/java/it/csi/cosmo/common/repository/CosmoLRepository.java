/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;

/**
 *
 */

@NoRepositoryBean
public interface CosmoLRepository<T extends CosmoLEntity, V extends Serializable>
    extends InsertOnlyRepository<T, V>, ExtendedRepository<T, V> {

  T reference(V id);

  <X> X reference(Class<X> type, Object id);

  void purge(Specification<T> specification);

  void purge(Specification<T> specification, int batchSize, int maxElements);

}

