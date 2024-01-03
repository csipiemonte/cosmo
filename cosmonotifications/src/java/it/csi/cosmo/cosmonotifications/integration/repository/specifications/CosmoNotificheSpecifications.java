/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.integration.repository.specifications;

import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.common.entities.CosmoTNotifica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;

public abstract class CosmoNotificheSpecifications {

  private CosmoNotificheSpecifications() {
    // private
  }

  public static Specification<CosmoTNotifica> findByCodiceFiscaleAndCodiceIpa(String codiceFiscale,
      String codiceIpaEnte) {

    return (Root<CosmoTNotifica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      ListJoin<CosmoTNotifica, CosmoRNotificaUtenteEnte> joinRNotificaUtente =
          root.join(CosmoTNotifica_.cosmoRNotificaUtenteEntes, JoinType.LEFT);

      Join<CosmoRNotificaUtenteEnte, CosmoTUtente> joinUtente =
          joinRNotificaUtente.join(CosmoRNotificaUtenteEnte_.cosmoTUtente, JoinType.LEFT);

      Join<CosmoRNotificaUtenteEnte, CosmoTEnte> joinEnte =
          joinRNotificaUtente.join(CosmoRNotificaUtenteEnte_.cosmoTEnte, JoinType.LEFT);

      Predicate predicate =
          cb.and(cb.equal(joinUtente.get(CosmoTUtente_.codiceFiscale), codiceFiscale),
              cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), codiceIpaEnte));

      if (CosmoTNotifica.class == cq.getResultType()) {
        root.fetch(CosmoTNotifica_.cosmoTPratica, JoinType.LEFT);
        root.fetch(CosmoTNotifica_.cosmoTFruitore, JoinType.LEFT);
      }
      return cq.where(predicate)
          .getRestriction();
    };
  }

  public static Specification<CosmoTNotifica> findNonLetteByCodiceFiscaleAndCodiceIpa(
      String codiceFiscale,
      String codiceIpaEnte) {

    return (Root<CosmoTNotifica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      ListJoin<CosmoTNotifica, CosmoRNotificaUtenteEnte> joinRNotificaUtente =
          root.join(CosmoTNotifica_.cosmoRNotificaUtenteEntes, JoinType.LEFT);

      Join<CosmoRNotificaUtenteEnte, CosmoTUtente> joinUtente =
          joinRNotificaUtente.join(CosmoRNotificaUtenteEnte_.cosmoTUtente, JoinType.LEFT);

      Join<CosmoRNotificaUtenteEnte, CosmoTEnte> joinEnte =
          joinRNotificaUtente.join(CosmoRNotificaUtenteEnte_.cosmoTEnte, JoinType.LEFT);

      Predicate predicate = cb.and(
          cb.equal(joinUtente.get(CosmoTUtente_.codiceFiscale), codiceFiscale),
          cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), codiceIpaEnte),
          cb.isNull(joinRNotificaUtente.get(CosmoRNotificaUtenteEnte_.dataLettura))
          );

      return cq.where(predicate)
          .distinct(true)
          .getRestriction();
    };
  }

  public static Specification<CosmoRNotificaUtenteEnte> findStatoLetturaByCodiceFiscaleAndCodiceIpa(
      String codiceFiscale, String codiceIpaEnte, Collection<Long> idNotifiche) {

    return (Root<CosmoRNotificaUtenteEnte> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Join<CosmoRNotificaUtenteEnte, CosmoTNotifica> joinNotifica =
          root.join(CosmoRNotificaUtenteEnte_.cosmoTNotifica, JoinType.LEFT);
      Join<CosmoRNotificaUtenteEnte, CosmoTUtente> joinUtente =
          root.join(CosmoRNotificaUtenteEnte_.cosmoTUtente, JoinType.LEFT);
      Join<CosmoRNotificaUtenteEnte, CosmoTEnte> joinEnte =
          root.join(CosmoRNotificaUtenteEnte_.cosmoTEnte, JoinType.LEFT);

      Predicate predicate = cb.and(
          cb.equal(joinUtente.get(CosmoTUtente_.codiceFiscale), codiceFiscale),
          cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), codiceIpaEnte),
          joinNotifica.get(CosmoTNotifica_.id).in(idNotifiche)
          );

      root.fetch(CosmoRNotificaUtenteEnte_.cosmoTNotifica);
      root.fetch(CosmoRNotificaUtenteEnte_.cosmoTUtente);
      root.fetch(CosmoRNotificaUtenteEnte_.cosmoTEnte);

      return cq.where(predicate)
          .distinct(true)
          .getRestriction();
    };
  }
}
