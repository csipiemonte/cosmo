/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 */


@NoRepositoryBean
public interface ReadOnlyRepository<T, V extends Serializable>
    extends org.springframework.data.repository.Repository<T, V>,
    JpaSpecificationExecutor<T> {

  public abstract T findOne(V primaryKey);

  public abstract long count();

  public abstract org.springframework.data.domain.Page<T> findAll(
      org.springframework.data.domain.Pageable pageable);

  public abstract java.util.List<T> findAll();

  public abstract java.util.List<T> findAll(org.springframework.data.domain.Sort sort);

  public abstract java.util.List<T> findAll(java.lang.Iterable<V> primaryKeys);

}

