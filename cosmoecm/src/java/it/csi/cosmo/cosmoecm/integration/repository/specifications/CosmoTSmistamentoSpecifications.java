/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTSmistamento;
import it.csi.cosmo.common.entities.CosmoTSmistamento_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

public interface CosmoTSmistamentoSpecifications {

  static Specification<CosmoTSmistamento> findSmistamentoPratica(Long idPratica,
      String identificativoEvento) {

    return (Root<CosmoTSmistamento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      /*
       * Predicate intervalloValidita =
       * cb.or(cb.isNull(root.get(CosmoTEntity_.dtInserimento.getName())),
       * cb.greaterThan(root.get(CosmoTEntity_.dtInserimento.getName()), now),
       * cb.and(cb.isNotNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
       * cb.lessThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now)));
       */
      Predicate intervalloValidita =
          cb.or(cb.isNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
              cb.lessThanOrEqualTo(root.get(CosmoTEntity_.dtInserimento.getName()), now),
              cb.greaterThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now));
      Predicate predicate = cb.and(intervalloValidita, cb
          .and(
              cb.equal(root.get(CosmoTSmistamento_.utilizzato), false),
              cb.equal(root.get(CosmoTSmistamento_.cosmoTPratica).get(CosmoTPratica_.id), idPratica),
              cb.equal(root.get(CosmoTSmistamento_.identificativoEvento), identificativoEvento)));

      return cq.where(predicate).getRestriction();
    };
  }
}
