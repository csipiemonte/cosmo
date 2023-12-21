/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;

/**
 *
 */

@NoRepositoryBean
public class CosmoLRepositoryImpl<T extends CosmoLEntity, V extends Serializable>
    extends ExtendedRepositoryImpl<T, V> implements CosmoLRepository<T, V> {

  protected RepositoryInformation metadata;
  protected EntityManager em;

  @SuppressWarnings("unchecked")
  public CosmoLRepositoryImpl(RepositoryInformation metadata, EntityManager em) {

    super((Class<T>) metadata.getDomainType(), em);
    this.metadata = metadata;
    this.em = em;
  }

  @Override
  public T insert(T entity) {
    assertNewEntity(entity);
    return this.save(entity);
  }

  @Override
  public List<T> insertAll(Iterable<T> entities) {
    entities.forEach(this::assertNewEntity);
    return this.save(entities);
  }

  protected void assertNewEntity(T entity) {
    Object identifier = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    if (identifier != null) {
      throw new PersistenceException("Cannnot insert an entity with a specified ID.");
    }
  }

  @Override
  public T reference(V id) {
    return em.getReference(this.getDomainClass(), id);
  }

  @Override
  public <X> X reference(Class<X> type, Object id) {
    return em.getReference(type, id);
  }

  @Override
  public void purge(Specification<T> specification) {
    this.purge(specification, 20, 0);
  }

  @Override
  public void purge(Specification<T> specification, int batchSize, int maxElements) {
    var pageRequest = new PageRequest(0, batchSize);
    int count = 0;
    do {
      var found = this.findAll(specification, pageRequest);
      if (!found.hasContent()) {
        break;
      }
      this.deleteInBatch(found.getContent());
      count += found.getNumberOfElements();
      if (maxElements > 0 && count >= maxElements) {
        break;
      }
    } while (true);
  }
}

