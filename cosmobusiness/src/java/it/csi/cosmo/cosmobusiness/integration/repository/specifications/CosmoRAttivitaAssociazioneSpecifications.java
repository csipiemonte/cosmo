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
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione_;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;

/**
 *
 */

public interface CosmoRAttivitaAssociazioneSpecifications {

  static Specification<CosmoRAttivitaAssegnazione> findByAssociazioneMeOMioGruppo(Long idAttivita,
      Integer idUtente, List<Integer> idGruppi) {

    return (Root<CosmoRAttivitaAssegnazione> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoRAttivitaAssegnazione_.cosmoTAttivita).get(CosmoTAttivita_.id),
              idAttivita),
          cb.isNull(root.get(CosmoREntity_.dtFineVal)),
          cb.or(cb.equal(root.get(CosmoRAttivitaAssegnazione_.idUtente), idUtente),
              root.get(CosmoRAttivitaAssegnazione_.idGruppo).in(idGruppi)));

      return cq.where(predicate).getRestriction();
    };
  }

}
