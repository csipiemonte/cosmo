/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;

/**
 *
 */

@NoRepositoryBean
public interface IntervalloValiditaRepository<T extends IntervalloValiditaEntity, V extends Serializable> {

  boolean isActive(T entity);

  boolean isActive(T entity, Timestamp at);
  
  Optional<T> findOneActive(V id);

  Optional<T> findOneNotActive(V id);

  Page<T> findAllActive(Specification<T> specification, Pageable page);

  Page<T> findAllActive(Pageable page);

  List<T> findAllActive(Specification<T> specification, Sort sort);

  List<T> findAllActive(Sort sort);

  List<T> findAllActive(Specification<T> specification);

  List<T> findAllActive();

  <X> Optional<T> findOneActiveByField(SingularAttribute<T, X> field, X value);

  <X> List<T> findActiveByField(SingularAttribute<T, X> field, X value);

  <X> Page<T> findActiveByField(SingularAttribute<T, X> field, X value, Pageable page);

  <X> List<T> findActiveByField(SingularAttribute<T, X> field, X value, Sort sort);

  Page<T> findNotActive(Specification<T> specification, Pageable page);

  Page<T> findNotActive(Pageable page);

  List<T> findNotActive(Specification<T> specification, Sort sort);

  List<T> findNotActive(Sort sort);

  List<T> findNotActive(Specification<T> specification);

  List<T> findNotActive();

  <X> Optional<T> findOneNotActiveByField(SingularAttribute<T, X> field, X value);

  <X> List<T> findNotActiveByField(SingularAttribute<T, X> field, X value);

  <X> Page<T> findNotActiveByField(SingularAttribute<T, X> field, X value, Pageable page);

  <X> List<T> findNotActiveByField(SingularAttribute<T, X> field, X value, Sort sort);

  T deactivate(T entity);

  T deactivate(V id);

  T activate(T entity);

  T activate(V id);
}

