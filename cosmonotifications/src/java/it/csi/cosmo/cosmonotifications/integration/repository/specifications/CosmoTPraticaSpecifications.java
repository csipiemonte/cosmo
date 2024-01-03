/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;

/**
 *
 */

public interface CosmoTPraticaSpecifications {


  static Specification<CosmoTPratica> findByChiaveFruitoreEsterno(String idPratica,
      String codiceIpaEnte, Long idFruitore) {

    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      //@formatter:off
      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoTPratica_.idPraticaExt), idPratica),
          cb.equal(root.get(CosmoTPratica_.fruitore).get(CosmoTFruitore_.id), idFruitore),
          cb.equal(root.get(CosmoTPratica_.ente).get(CosmoTEnte_.codiceIpa), codiceIpaEnte));
      //@formatter:on

      return cq.where(predicate).getRestriction();
    };
  }

}
