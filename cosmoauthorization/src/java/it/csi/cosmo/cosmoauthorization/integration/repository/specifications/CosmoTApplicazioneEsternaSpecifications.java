/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna_;
import it.csi.cosmo.common.entities.CosmoTApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoTApplicazioneEsterna_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

public interface CosmoTApplicazioneEsternaSpecifications {

  static Specification<CosmoTApplicazioneEsterna> findAllByCosmoREnteApplicazioneEsternasCosmoTEnteIdNotEqualAndDtCancellazioneNullOrderByDescrizione(
      Long idEnte) {

    return (Root<CosmoTApplicazioneEsterna> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Subquery<Long> subquery = cq.subquery(Long.class);
      Root<CosmoREnteApplicazioneEsterna> subRoot =
          subquery.from(CosmoREnteApplicazioneEsterna.class);

      ListJoin<CosmoREnteApplicazioneEsterna, CosmoTFunzionalitaApplicazioneEsterna> join =
          subRoot.join(CosmoREnteApplicazioneEsterna_.cosmoTFunzionalitaApplicazioneEsternas,
              JoinType.INNER);

      Predicate enteApplicazionePred = cb.and(
          cb.equal(
              subRoot.get(CosmoREnteApplicazioneEsterna_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
          cb.equal(join.get(CosmoTFunzionalitaApplicazioneEsterna_.principale), Boolean.TRUE),
          cb.isNull(join.get(CosmoTEntity_.dtCancellazione))
          );

      subquery.select(subRoot.get(CosmoREnteApplicazioneEsterna_.cosmoTApplicazioneEsterna)
          .get(CosmoTApplicazioneEsterna_.id)).where(enteApplicazionePred);

      Predicate predicate =
          cb.and(
              cb.in(root.get(CosmoTApplicazioneEsterna_.id)).value(subquery).not(),
              cb.isNull(root.get(CosmoTEntity_.dtCancellazione)));

      return cq.where(predicate).orderBy(cb.asc(root.get(CosmoTApplicazioneEsterna_.descrizione)))
          .getRestriction();
    };
  }

  static Specification<CosmoTApplicazioneEsterna> findAppValideOrderByDescrizione(
      Long idEnte) {

    return (Root<CosmoTApplicazioneEsterna> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      ListJoin<CosmoTApplicazioneEsterna, CosmoREnteApplicazioneEsterna> joinApplicazioneEnteApplicazione =
          root.join(CosmoTApplicazioneEsterna_.cosmoREnteApplicazioneEsternas, JoinType.INNER);

      ListJoin<CosmoREnteApplicazioneEsterna, CosmoTFunzionalitaApplicazioneEsterna> joinEnteApplicazioneFunzionalita =
          joinApplicazioneEnteApplicazione.join(CosmoREnteApplicazioneEsterna_.cosmoTFunzionalitaApplicazioneEsternas, JoinType.INNER);

      Predicate predicate = cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(joinApplicazioneEnteApplicazione.get(CosmoREnteApplicazioneEsterna_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
          cb.or(cb.isNull(joinApplicazioneEnteApplicazione.get(CosmoREntity_.dtFineVal)),
              cb.greaterThan(joinApplicazioneEnteApplicazione.get(CosmoREntity_.dtFineVal),
                  Timestamp.valueOf(LocalDateTime.now()))),
          cb.equal(joinEnteApplicazioneFunzionalita
              .get(CosmoTFunzionalitaApplicazioneEsterna_.principale), Boolean.TRUE),
          cb.isNull(joinEnteApplicazioneFunzionalita.get(CosmoTEntity_.dtCancellazione))
          );

      return cq.where(predicate).orderBy(cb.asc(root.get(CosmoTApplicazioneEsterna_.descrizione)))
          .getRestriction();
    };
  }
}
