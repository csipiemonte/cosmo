/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTOtp;
import it.csi.cosmo.common.entities.CosmoTOtp_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

public interface CosmoTOtpSpecifications {

  static Specification<CosmoTOtp> findByUtenteEnte(Long idUtente, Long idEnte) {

    return (Root<CosmoTOtp> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate =
          cb.and(cb.equal(root.get(CosmoTOtp_.utente).get(CosmoTUtente_.id), idUtente),
              cb.equal(root.get(CosmoTOtp_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
              cb.isNull(root.get(CosmoTEntity_.dtCancellazione)));

      return cq.where(predicate).getRestriction();
    };

  }

}
