/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.entities.CosmoCConfigurazione_;

/**
 *
 */

public interface CosmoCConfigurazioneSpecifications {

  static Specification<CosmoCConfigurazione> findByChiaveValida(String chiave) {

    return (Root<CosmoCConfigurazione> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.from(Instant.now());

      Predicate predicate = cb.and (
          cb.equal(root.get(CosmoCConfigurazione_.chiave), chiave),
          cb.lessThanOrEqualTo(root.get(CosmoCConfigurazione_.dtInizioVal),
              now),
          cb.or(
              cb.greaterThanOrEqualTo(root.get(CosmoCConfigurazione_.dtFineVal), now),
              cb.isNull(root.get(CosmoCConfigurazione_.dtFineVal))));

      return cq.where ( predicate ).getRestriction ();
    };
  }

}
