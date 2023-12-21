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
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;

/**
 *
 */

@NoRepositoryBean
public interface CampiTecniciRepository<T extends CampiTecniciEntity, V extends Serializable> {

  boolean isDeleted(T entity);

  Optional<T> findOneNotDeleted(V id);

  Optional<T> findOneDeleted(V id);

  Optional<T> findOneNotDeleted(Specification<T> specification);

  Optional<T> findOneDeleted(Specification<T> specification);

  Page<T> findAllNotDeleted(Specification<T> specification, Pageable page);

  Page<T> findAllNotDeleted(Pageable page);

  List<T> findAllNotDeleted(Specification<T> specification, Sort sort);

  List<T> findAllNotDeleted(Sort sort);

  List<T> findAllNotDeleted(Specification<T> specification);

  List<T> findAllNotDeleted();

  <X> Optional<T> findOneNotDeletedByField(SingularAttribute<T, X> field, X value);

  <X> List<T> findNotDeletedByField(SingularAttribute<T, X> field, X value);

  <X> Page<T> findNotDeletedByField(SingularAttribute<T, X> field, X value, Pageable page);

  <X> List<T> findNotDeletedByField(SingularAttribute<T, X> field, X value, Sort sort);

  Page<T> findDeleted(Specification<T> specification, Pageable page);

  Page<T> findDeleted(Pageable page);

  List<T> findDeleted(Specification<T> specification, Sort sort);

  List<T> findDeleted(Sort sort);

  List<T> findDeleted(Specification<T> specification);

  List<T> findDeleted();

  <X> Optional<T> findOneDeletedByField(SingularAttribute<T, X> field, X value);

  <X> List<T> findDeletedByField(SingularAttribute<T, X> field, X value);

  <X> Page<T> findDeletedByField(SingularAttribute<T, X> field, X value, Pageable page);

  <X> List<T> findDeletedByField(SingularAttribute<T, X> field, X value, Sort sort);

  T deleteLogically(T entity, String codiceAudit);

  T deleteLogically(V id, String codiceAudit);

  T restore(V id);

  T touch(V id, String codiceAudit);

  T touch(T entity, String codiceAudit);
  
  <X> void deleteByField(SingularAttribute<T, X> field, X value);
}

