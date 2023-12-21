/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;

/**
 *
 */

@NoRepositoryBean
public abstract class IntervalloValiditaRepositoryImpl<T extends IntervalloValiditaEntity, V extends Serializable>
    extends ExtendedRepositoryImpl<T, V> implements IntervalloValiditaRepository<T, V> {

  public IntervalloValiditaRepositoryImpl(JpaEntityInformation<T, V> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public IntervalloValiditaRepositoryImpl(final Class<T> entityClass,
      final EntityManager entityManager) {
    super(entityClass, entityManager);
  }

  @Override
  protected SimpleJpaRepository<T, V> getRepository() {
    return this;
  }

  @Override
  public boolean isActive(T entity) {
    return isActive(entity, Timestamp.valueOf(LocalDateTime.now()));
  }

  @Override
  public boolean isActive(T entity, Timestamp at) {
    return (entity.getDtInizioVal() != null && !entity.getDtInizioVal().after(at))
        && (entity.getDtFineVal() == null || entity.getDtFineVal().after(at));
  }

  @Override
  public <X> Optional<T> findOneActiveByField(SingularAttribute<T, X> field, X value) {
    Page<T> candidates = this.findActiveByField(field, value, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public <X> Optional<T> findOneNotActiveByField(SingularAttribute<T, X> field, X value) {
    Page<T> candidates = this.findNotActiveByField(field, value, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }
  
  @Override
  public <X> List<T> findActiveByField(SingularAttribute<T, X> field, X value) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildActiveCriteria()));
  }

  @Override
  public <X> Page<T> findActiveByField(SingularAttribute<T, X> field, X value, Pageable page) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildActiveCriteria()), page);
  }

  @Override
  public <X> List<T> findActiveByField(SingularAttribute<T, X> field, X value, Sort sort) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildActiveCriteria()), sort);
  }

  @Override
  public <X> List<T> findNotActiveByField(SingularAttribute<T, X> field, X value) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotActiveCriteria()));
  }

  @Override
  public <X> Page<T> findNotActiveByField(SingularAttribute<T, X> field, X value, Pageable page) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotActiveCriteria()), page);
  }

  @Override
  public <X> List<T> findNotActiveByField(SingularAttribute<T, X> field, X value, Sort sort) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotActiveCriteria()), sort);
  }

  @Override
  public Optional<T> findOneActive(V id) {

    T found = getRepository().findOne(id);
    if (found != null && isActive(found)) {
      return Optional.of(found);
    }
    return Optional.empty();
  }

  @Override
  public Optional<T> findOneNotActive(V id) {

    T found = getRepository().findOne(id);
    if (found != null && !isActive(found)) {
      return Optional.of(found);
    }
    return Optional.empty();
  }

  @Override
  public Page<T> findAllActive(Specification<T> specification, Pageable page) {
    return getRepository().findAll(Specifications.where(buildActiveCriteria()).and(specification),
        page);
  }

  @Override
  public Page<T> findAllActive(Pageable page) {
    return getRepository().findAll(buildActiveCriteria(), page);
  }

  @Override
  public List<T> findAllActive(Specification<T> specification, Sort sort) {
    return getRepository().findAll(Specifications.where(buildActiveCriteria()).and(specification),
        sort);
  }

  @Override
  public List<T> findAllActive(Sort sort) {
    return getRepository().findAll(buildActiveCriteria(), sort);
  }

  @Override
  public List<T> findAllActive(Specification<T> specification) {
    return getRepository().findAll(Specifications.where(buildActiveCriteria()).and(specification));
  }

  @Override
  public List<T> findAllActive() {
    return getRepository().findAll(buildActiveCriteria());
  }

  @Override
  public Page<T> findNotActive(Specification<T> specification, Pageable page) {
    return getRepository()
        .findAll(Specifications.where(buildNotActiveCriteria()).and(specification), page);
  }

  @Override
  public Page<T> findNotActive(Pageable page) {
    return getRepository().findAll(buildNotActiveCriteria(), page);
  }

  @Override
  public List<T> findNotActive(Specification<T> specification, Sort sort) {
    return getRepository()
        .findAll(Specifications.where(buildNotActiveCriteria()).and(specification), sort);
  }

  @Override
  public List<T> findNotActive(Sort sort) {
    return getRepository().findAll(buildNotActiveCriteria(), sort);
  }

  @Override
  public List<T> findNotActive(Specification<T> specification) {
    return getRepository()
        .findAll(Specifications.where(buildNotActiveCriteria()).and(specification));
  }

  @Override
  public List<T> findNotActive() {
    return getRepository().findAll(buildNotActiveCriteria());
  }

  @Override
  public T deactivate(V id) {
    T found = getRepository().findOne(id);
    if (found != null) {
      found.setDtFineVal(new Timestamp(System.currentTimeMillis()));
      found = getRepository().save(found);
      getRepository().flush();
      return found;
    }
    throw new EntityNotFoundException("Entity with id " + id + " to delete logically not found");
  }

  @Override
  public T deactivate(T entity) {
    if (entity != null) {
      entity.setDtFineVal(new Timestamp(System.currentTimeMillis()));
      entity = getRepository().save(entity);
      getRepository().flush();
    }
    return entity;
  }

  @Override
  public T activate(V id) {
    T found = getRepository().findOne(id);
    if (found != null) {
      found.setDtInizioVal(new Timestamp(System.currentTimeMillis()));
      found.setDtFineVal(null);
      found = getRepository().save(found);
      getRepository().flush();
      return found;
    }
    throw new EntityNotFoundException("Entity with id " + id + " to delete logically not found");
  }

  @Override
  public T activate(T entity) {
    if (entity != null) {
      entity.setDtInizioVal(new Timestamp(System.currentTimeMillis()));
      entity.setDtFineVal(null);
      entity = getRepository().save(entity);
      getRepository().flush();
    }
    return entity;
  }

  private Specification<T> buildActiveCriteria() {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      //@formatter:off
      return cb.and (
          cb.isNotNull(root.get(CosmoDEntity_.dtInizioVal.getName())),
          cb.lessThanOrEqualTo(root.get(CosmoDEntity_.dtInizioVal.getName()), now),
          cb.or(
              cb.isNull(root.get(CosmoDEntity_.dtFineVal.getName())),
              cb.greaterThanOrEqualTo(root.get(CosmoDEntity_.dtFineVal.getName()), now)
              ));
      //@formatter:off
    };
  }

  private Specification<T> buildNotActiveCriteria() {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      //@formatter:off
      return cb.or (
          cb.isNull(root.get(CosmoDEntity_.dtInizioVal.getName())),
          cb.greaterThan(root.get(CosmoDEntity_.dtInizioVal.getName()), now),
          cb.and(
              cb.isNotNull(root.get(CosmoDEntity_.dtFineVal.getName())),
              cb.lessThan(root.get(CosmoDEntity_.dtFineVal.getName()), now)
              ));
      //@formatter:off
    };
  }
}

