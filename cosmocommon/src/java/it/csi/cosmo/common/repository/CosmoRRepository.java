/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import org.springframework.data.repository.NoRepositoryBean;
import it.csi.cosmo.common.entities.proto.CosmoREntity;

/**
 *
 */

@NoRepositoryBean
public interface CosmoRRepository<T extends CosmoREntity, V extends Serializable>
    extends BasicRepository<T, V>, ExtendedRepository<T, V>, IntervalloValiditaRepository<T, V> {

  T reference(V id);

  <X> X reference(Class<X> type, Object id);

}

