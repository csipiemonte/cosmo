/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository.specifications;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

public interface CosmoRUtenteGruppoTagSpecifications {

  static Specification<CosmoRUtenteGruppoTag> findAllActiveByGruppiAndTag(List<String> gruppi,
      Long tag) {

    return (Root<CosmoRUtenteGruppoTag> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      Predicate predicate = cb.and(
          cb.isNull(root.get(CosmoREntity_.dtFineVal)),
          cb.equal(root.get(CosmoRUtenteGruppoTag_.cosmoTTag).get(CosmoTTag_.id), tag),
          cb.isNull(root.get(CosmoRUtenteGruppoTag_.cosmoTTag).get(CosmoTEntity_.dtCancellazione)),
          root.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo).get(CosmoTUtenteGruppo_.gruppo)
          .get(CosmoTGruppo_.codice).in(gruppi),
          cb.isNull(root.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo)
              .get(CosmoTEntity_.dtCancellazione)));


      return cq.where(predicate).getRestriction();
    };
  }

}
