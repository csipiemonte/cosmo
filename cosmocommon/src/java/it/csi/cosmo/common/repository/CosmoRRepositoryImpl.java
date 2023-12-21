/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import it.csi.cosmo.common.entities.proto.CosmoREntity;

/**
 *
 */

@NoRepositoryBean
public class CosmoRRepositoryImpl<T extends CosmoREntity, V extends Serializable>
extends IntervalloValiditaRepositoryImpl<T, V> implements CosmoRRepository<T, V> {

  protected RepositoryInformation metadata;
  protected EntityManager em;

  @SuppressWarnings("unchecked")
  public CosmoRRepositoryImpl(RepositoryInformation metadata, EntityManager em) {

    super((Class<T>) metadata.getDomainType(), em);
    this.metadata = metadata;
    this.em = em;
  }

  @Override
  public T reference(V id) {
    return em.getReference(this.getDomainClass(), id);
  }

  @Override
  public <X> X reference(Class<X> type, Object id) {
    return em.getReference(type, id);
  }
}

