/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 */

@NoRepositoryBean
public abstract class ExtendedRepositoryImpl<T, V extends Serializable>
extends SimpleJpaRepository<T, V> implements ExtendedRepository<T, V> {

  private EntityManager entityManager;

  public ExtendedRepositoryImpl(JpaEntityInformation<T, V> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  public ExtendedRepositoryImpl(final Class<T> entityClass, final EntityManager entityManager) {
    super(entityClass, entityManager);
    this.entityManager = entityManager;
  }

  protected SimpleJpaRepository<T, V> getRepository() {
    return this;
  }

  public List<Object> findDistinctValues(Specification<T> spec, String columnName) {
    return getQueryFindDistinctValues(spec, columnName).getResultList();
  }

  protected TypedQuery<Object> getQueryFindDistinctValues(Specification<T> spec,
      final String distinctColumnName) {

    CriteriaBuilder builder2 = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Object> query = builder2.createQuery(Object.class);

    Root<T> root = query.from(this.getDomainClass());
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    Predicate predicate = spec.toPredicate(root, query, builder);
    if (predicate != null) {
      query.where(predicate);
    }

    if (null != distinctColumnName) {
      query.distinct(true);
      query.multiselect(root.get(distinctColumnName));
    }

    // We order by the distinct column, Asc
    query.orderBy(builder.asc(root.get(distinctColumnName)));

    return this.entityManager.createQuery(query);
  }

  @Override
  public <X> Optional<T> findOneByField(SingularAttribute<T, X> field, X value) {
    Page<T> candidates = this.findByField(field, value, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public <X> List<T> findByField(SingularAttribute<T, X> field, X value) {
    return getRepository().findAll(buildSingleFieldSpecification(field, value));
  }

  @Override
  public <X> Page<T> findByField(SingularAttribute<T, X> field, X value, Pageable page) {
    return getRepository().findAll(buildSingleFieldSpecification(field, value), page);
  }

  @Override
  public <X> List<T> findByField(SingularAttribute<T, X> field, X value, Sort sort) {
    return getRepository().findAll(buildSingleFieldSpecification(field, value), sort);
  }

  protected <X> Specification<T> buildSingleFieldSpecification(SingularAttribute<T, X> field,
      X value) {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      return cb.equal(root.get(field), value);
    };
  }

}

