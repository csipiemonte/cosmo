/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 */


@NoRepositoryBean
public interface InsertOnlyRepository<T, V extends Serializable>
    extends ReadOnlyRepository<T, V> {

  public abstract T insert(T entity);

  public abstract java.util.List<T> insertAll(java.lang.Iterable<T> entities);

  public abstract void flush();
}

