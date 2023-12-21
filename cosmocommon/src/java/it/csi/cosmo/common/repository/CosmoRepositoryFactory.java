/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.repository;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import it.csi.cosmo.common.entities.proto.CosmoCEntity;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;

/**
 *
 */
public class CosmoRepositoryFactory<R extends JpaRepository<T, I>, T, I extends Serializable>
extends JpaRepositoryFactoryBean<R, T, I> {

  public CosmoRepositoryFactory(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

    return new MyRepositoryFactory(entityManager);
  }

  private static class MyRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

    private EntityManager entityManager;

    public MyRepositoryFactory(EntityManager entityManager) {
      super(entityManager);

      this.entityManager = entityManager;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
      return getTargetRepository(metadata, this.entityManager);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation metadata,
        EntityManager em) {

      if (CosmoTEntity.class.isAssignableFrom(metadata.getDomainType())) {
        return new CosmoTRepositoryImpl(metadata, em);
      } else if (CosmoDEntity.class.isAssignableFrom(metadata.getDomainType())) {
        return new CosmoDRepositoryImpl(metadata, em);
      } else if (CosmoREntity.class.isAssignableFrom(metadata.getDomainType())) {
        return new CosmoRRepositoryImpl(metadata, em);
      } else if (CosmoCEntity.class.isAssignableFrom(metadata.getDomainType())) {
        return new CosmoCRepositoryImpl(metadata, em);
      } else if (CosmoLEntity.class.isAssignableFrom(metadata.getDomainType())) {
        return new CosmoLRepositoryImpl(metadata, em);
      } else {
        return new SimpleJpaRepository<>((Class<T>) metadata.getDomainType(), em);
      }
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

      if (CosmoTRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return CosmoTRepositoryImpl.class;
      } else if (CosmoDRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return CosmoDRepositoryImpl.class;
      } else if (CosmoRRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return CosmoRRepositoryImpl.class;
      } else if (CosmoCRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return CosmoCRepositoryImpl.class;
      } else if (CosmoLRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return CosmoLRepositoryImpl.class;
      } else {
        return SimpleJpaRepository.class;
      }
    }
  }
}
