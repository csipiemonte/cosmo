/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.security.model.UserInfoDTO;

/**
 *
 */

public interface CosmoTAttivitaSpecifications {

  public static String[] FUNZIONALITA_ESEGUIBILI_MASSIVAMENTE = {"APPROVAZIONE", "FIRMA-DOCUMENTI"};

  /**
   * Recupero di tutte le attività di una pratica in carico all'utente connesso
   *
   * @param idPratica
   * @return
   */
  static Specification<CosmoTAttivita> findInCaricoAUtente(Long idPratica, UserInfoDTO userInfo) {
    return (Root<CosmoTAttivita> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      Predicate predicate = buildPredicateAttivitaAssegnateAdUtente(root, cq, cb, userInfo);

      predicate = cb.and(predicate,
          cb.equal(root.join(CosmoTAttivita_.cosmoTPratica, JoinType.INNER).get(CosmoTPratica_.id),
              idPratica));

      return cq.where(predicate).distinct(true).getRestriction();
    };
  }

  static Predicate buildPredicateAttivitaAssegnateAdUtente(Root<CosmoTAttivita> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb, UserInfoDTO userInfo) {

    Timestamp now = Timestamp.valueOf(LocalDateTime.now());

    Predicate predicate = cb.or(cb.isNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
        cb.greaterThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now));

    predicate = cb.and(predicate,
        cb.or(
            cb.isNull(root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.INNER)
                .get(CosmoREntity_.dtFineVal.getName())),
            cb.greaterThan(root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.INNER)
                .get(CosmoREntity_.dtFineVal.getName()), now)));

    Predicate predicateAssegnatario =
        cb.equal(root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.INNER)
            .get(CosmoRAttivitaAssegnazione_.idUtente), userInfo.getId());
    predicateAssegnatario = cb.and(predicateAssegnatario,
        cb.equal(root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.INNER)
            .get(CosmoRAttivitaAssegnazione_.assegnatario), true));

    if (userInfo.getGruppi() != null && !userInfo.getGruppi().isEmpty()) {
      In<Integer> inClause =
          cb.in(root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.INNER)
              .get(CosmoRAttivitaAssegnazione_.idGruppo));

      var idGruppi = new ArrayList<Integer>();

      userInfo.getGruppi().forEach(gruppo -> idGruppi.add(gruppo.getId().intValue()));
      idGruppi.forEach(inClause::value);
      Predicate predicateGruppo = cb.and(predicate, inClause);

      predicate = cb.and(predicate, cb.or(predicateAssegnatario, predicateGruppo));
    } else {
      predicate = cb.and(predicate, predicateAssegnatario);
    }

    return predicate;
  }
}
