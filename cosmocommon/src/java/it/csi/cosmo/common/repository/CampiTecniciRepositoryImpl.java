/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.sql.Timestamp;
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
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

@NoRepositoryBean
public abstract class CampiTecniciRepositoryImpl<T extends CampiTecniciEntity, V extends Serializable>
    extends ExtendedRepositoryImpl<T, V> implements CampiTecniciRepository<T, V> {

  public CampiTecniciRepositoryImpl(JpaEntityInformation<T, V> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public CampiTecniciRepositoryImpl(final Class<T> entityClass, final EntityManager entityManager) {
    super(entityClass, entityManager);
  }

  @Override
  protected SimpleJpaRepository<T, V> getRepository() {
    return this;
  }

  @Override
  public boolean isDeleted(T entity) {
    return entity.getDtCancellazione() != null;
  }

  @Override
  public <X> Optional<T> findOneNotDeletedByField(SingularAttribute<T, X> field, X value) {
    Page<T> candidates = this.findNotDeletedByField(field, value, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public <X> Optional<T> findOneDeletedByField(SingularAttribute<T, X> field, X value) {
    Page<T> candidates = this.findDeletedByField(field, value, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public Optional<T> findOneNotDeleted(Specification<T> specification) {
    Page<T> candidates = this.findAllNotDeleted(specification, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public Optional<T> findOneDeleted(Specification<T> specification) {
    Page<T> candidates = this.findDeleted(specification, new PageRequest(0, 2));
    if (candidates == null || candidates.getTotalElements() < 1) {
      return Optional.empty();
    } else if (candidates.getTotalElements() > 1) {
      throw new IncorrectResultSizeDataAccessException(1, (int) candidates.getTotalElements());
    } else {
      return Optional.of(candidates.getContent().get(0));
    }
  }

  @Override
  public <X> List<T> findNotDeletedByField(SingularAttribute<T, X> field, X value) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotDeletedCriteria()));
  }

  @Override
  public <X> Page<T> findNotDeletedByField(SingularAttribute<T, X> field, X value, Pageable page) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotDeletedCriteria()), page);
  }

  @Override
  public <X> List<T> findNotDeletedByField(SingularAttribute<T, X> field, X value, Sort sort) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildNotDeletedCriteria()), sort);
  }

  @Override
  public <X> List<T> findDeletedByField(SingularAttribute<T, X> field, X value) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildDeletedCriteria()));
  }

  @Override
  public <X> Page<T> findDeletedByField(SingularAttribute<T, X> field, X value, Pageable page) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildDeletedCriteria()), page);
  }

  @Override
  public <X> List<T> findDeletedByField(SingularAttribute<T, X> field, X value, Sort sort) {
    return getRepository().findAll(Specifications.where(buildSingleFieldSpecification(field, value))
        .and(buildDeletedCriteria()), sort);
  }

  @Override
  public Optional<T> findOneNotDeleted(V id) {
    T found = getRepository().findOne(id);
    if (found == null || isDeleted(found)) {
      return Optional.empty();
    } else {
      return Optional.of(found);
    }
  }

  @Override
  public Optional<T> findOneDeleted(V id) {
    T found = getRepository().findOne(id);
    if (found != null && isDeleted(found)) {
      return Optional.of(found);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Page<T> findAllNotDeleted(Specification<T> specification, Pageable page) {
    return getRepository()
        .findAll(Specifications.where(buildNotDeletedCriteria()).and(specification), page);
  }

  @Override
  public Page<T> findAllNotDeleted(Pageable page) {
    return getRepository().findAll(buildNotDeletedCriteria(), page);
  }

  @Override
  public List<T> findAllNotDeleted(Specification<T> specification, Sort sort) {
    return getRepository()
        .findAll(Specifications.where(buildNotDeletedCriteria()).and(specification), sort);
  }

  @Override
  public List<T> findAllNotDeleted(Sort sort) {
    return getRepository().findAll(buildNotDeletedCriteria(), sort);
  }

  @Override
  public List<T> findAllNotDeleted(Specification<T> specification) {
    return getRepository()
        .findAll(Specifications.where(buildNotDeletedCriteria()).and(specification));
  }

  @Override
  public List<T> findAllNotDeleted() {
    return getRepository().findAll(buildNotDeletedCriteria());
  }

  @Override
  public Page<T> findDeleted(Specification<T> specification, Pageable page) {
    return getRepository().findAll(Specifications.where(buildDeletedCriteria()).and(specification),
        page);
  }

  @Override
  public Page<T> findDeleted(Pageable page) {
    return getRepository().findAll(buildDeletedCriteria(), page);
  }

  @Override
  public List<T> findDeleted(Specification<T> specification, Sort sort) {
    return getRepository().findAll(Specifications.where(buildDeletedCriteria()).and(specification),
        sort);
  }

  @Override
  public List<T> findDeleted(Sort sort) {
    return getRepository().findAll(buildDeletedCriteria(), sort);
  }

  @Override
  public List<T> findDeleted(Specification<T> specification) {
    return getRepository().findAll(Specifications.where(buildDeletedCriteria()).and(specification));
  }

  @Override
  public List<T> findDeleted() {
    return getRepository().findAll(buildDeletedCriteria());
  }

  @Override
  public T deleteLogically(V id, String codiceAudit) {
    T found = getRepository().findOne(id);
    if (found != null) {
      found.setDtCancellazione(new Timestamp(System.currentTimeMillis()));
      found.setUtenteCancellazione(codiceAudit);
      found = getRepository().save(found);
      getRepository().flush();
      return found;
    }
    throw new EntityNotFoundException("Entity with id " + id + " to delete logically not found");
  }

  @Override
  public T deleteLogically(T entity, String codiceAudit) {
    if (entity != null) {
      entity.setDtCancellazione(new Timestamp(System.currentTimeMillis()));
      entity.setUtenteCancellazione(codiceAudit);
      entity = getRepository().save(entity);
      getRepository().flush();
    }
    return entity;
  }

  @Override
  public T restore(V id) {
    T found = getRepository().findOne(id);
    if (found != null) {
      found.setDtCancellazione(null);
      found.setUtenteCancellazione(null);
      T result = getRepository().save(found);
      getRepository().flush();
      return result;
    } else {
      throw new EntityNotFoundException("Entity with id " + id + " to restore not found");
    }
  }

  @Override
  public T touch(V id, String codiceAudit) {
    T found = getRepository().findOne(id);
    if (found != null) {
      return this.touch(found, codiceAudit);
    }
    throw new EntityNotFoundException("Entity with id " + id + " to touch not found");
  }

  @Override
  public T touch(T entity, String codiceAudit) {
    entity.setDtUltimaModifica(new Timestamp(System.currentTimeMillis()));
    entity.setUtenteUltimaModifica(codiceAudit);
    entity = getRepository().save(entity);
    getRepository().flush();
    return entity;
  }

  @Override
  public <X> void deleteByField(SingularAttribute<T, X> field, X value) {
    var found = this.findByField(field, value);
    getRepository().delete(found);
  }

  private Specification<T> buildNotDeletedCriteria() {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      return cb.isNull(root.get(CosmoTEntity_.dtCancellazione.getName()));
    };
  }

  private Specification<T> buildDeletedCriteria() {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      return cb.isNotNull(root.get(CosmoTEntity_.dtCancellazione.getName()));
    };
  }

  @Override
  protected <X> Specification<T> buildSingleFieldSpecification(SingularAttribute<T, X> field,
      X value) {
    return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      return cb.equal(root.get(field), value);
    };
  }
}

