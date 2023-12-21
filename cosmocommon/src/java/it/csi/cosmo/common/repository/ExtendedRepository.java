/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 */


@NoRepositoryBean
public interface ExtendedRepository<T, V extends Serializable> {

  <X> Optional<T> findOneByField(SingularAttribute<T, X> field, X value);

  <X> List<T> findByField(SingularAttribute<T, X> field, X value);

  <X> Page<T> findByField(SingularAttribute<T, X> field, X value, Pageable page);

  <X> List<T> findByField(SingularAttribute<T, X> field, X value, Sort sort);

  List<Object> findDistinctValues(Specification<T> spec, String columnName);
}

