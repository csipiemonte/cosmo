/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaGruppiDTO;

/**
 *
 */

public interface CosmoTGruppoSpecifications {

  static Specification<CosmoTGruppo> findByFilters(FiltroRicercaGruppiDTO filtri, String sort) {

    return (Root<CosmoTGruppo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.equal(cb.literal(true), cb.literal(true));
      if (filtri != null) {
        if (filtri.getId() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTGruppo_.id),
              predicate, cb);
        }

        if (filtri.getNome() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getNome(), root.get(CosmoTGruppo_.nome),
              predicate, cb);
        }

        if (filtri.getDescrizione() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
              root.get(CosmoTGruppo_.descrizione), predicate, cb);
        }

        if (filtri.getIdEnte() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
              root.get(CosmoTGruppo_.ente).get(CosmoTEnte_.id), predicate, cb);
        }

        if (filtri.getCodice() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
              root.get(CosmoTGruppo_.codice), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getFullText(),
              cb.concat(root.get(CosmoTGruppo_.nome), root.get(CosmoTGruppo_.descrizione)),
              predicate, cb);
        }

        if (filtri.getCodiceTipoPratica() != null) {
          ListJoin<CosmoTGruppo, CosmoRGruppoTipoPratica> joinGruppoTipoPratica =
              root.join(CosmoTGruppo_.cosmoRGruppoTipoPraticas, JoinType.LEFT);
          predicate =
              cb.and(predicate, cb.isNull(joinGruppoTipoPratica.get(CosmoREntity_.dtFineVal)));
          Join<CosmoRGruppoTipoPratica, CosmoDTipoPratica> joinTipoPratica =
              joinGruppoTipoPratica.join(CosmoRGruppoTipoPratica_.cosmoDTipoPratica, JoinType.LEFT);

          if (filtri.getSoloConAssociazioneTipoPratica() == null
              || filtri.getSoloConAssociazioneTipoPratica().getDefined() == null
              || Boolean.FALSE.equals(filtri.getSoloConAssociazioneTipoPratica().getDefined())) {


            Predicate p1 = SpecificationUtils.applyFilter(filtri.getCodiceTipoPratica(),
                joinTipoPratica.get(CosmoDTipoPratica_.codice), cb.conjunction(), cb);
            Predicate p2 =
                cb.and(cb.conjunction(), joinTipoPratica.get(CosmoDTipoPratica_.codice).isNull());

            predicate = cb.and(predicate, cb.or(p1, p2));
          } else {
            predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoPratica(),
                joinTipoPratica.get(CosmoDTipoPratica_.codice), predicate, cb);
          }
        }
      }

      return cq
          .where(predicate)
          .orderBy(getOrder(sort, root, cb))
          .getRestriction();
    };
  }

  private static Predicate applyConCodiceTipoPraticaONulloFilter(StringFilter filter,
      Predicate predicate, Root<CosmoTUtente> root, CriteriaBuilder cb) {

    Function<Boolean, Expression<String>> fullNameSupplier =
        (Boolean nameFirst) -> nameFirst.booleanValue()
        ? cb.concat(cb.concat(root.get(CosmoTUtente_.nome), cb.literal(" ")),
            root.get(CosmoTUtente_.cognome))
            : cb.concat(cb.concat(root.get(CosmoTUtente_.cognome), cb.literal(" ")),
                root.get(CosmoTUtente_.nome));

        Predicate p1 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(true), cb.conjunction(), cb);
        Predicate p2 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(false), cb.conjunction(), cb);

        return cb.and(predicate, cb.or(p1, p2));
  }
  public static List<Order> getOrder(String sort, Root<CosmoTGruppo> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTGruppo_.nome.getName();
    }

    String[] sortSplit = sort.trim().split(",");

    List<Order> ordine = new LinkedList<>();
    for (String sortSplitSingle : sortSplit) {
      Order clause = createSort(sortSplitSingle, root, cb);
      if (clause != null) {
        ordine.add(clause);
      }
    }
    return ordine;
  }

  private static Order createSort(String sortSplitSingle, Root<CosmoTGruppo> root,
      CriteriaBuilder cb) {
    if (sortSplitSingle == null || sortSplitSingle.isEmpty()) {
      return null;
    }
    sortSplitSingle = sortSplitSingle.trim();
    if (sortSplitSingle.isEmpty()) {
      return null;
    }

    String sortField;
    boolean sortDirection;
    if (sortSplitSingle.startsWith("-")) {
      sortDirection = false;
      sortField = sortSplitSingle.substring(1);
    } else if (sortSplitSingle.startsWith("+")) {
      sortDirection = true;
      sortField = sortSplitSingle.substring(1);
    } else {
      sortDirection = true;
      sortField = sortSplitSingle;
    }

    if (FieldUtils.getField(CosmoTGruppo_.class, sortField) != null) {
      if (sortDirection) {
        return cb.asc(root.get(sortField));
      } else {
        return cb.desc(root.get(sortField));
      }
    } else {
      // custom filter
      throw new BadRequestException("Invalid sort field " + sortField);
    }
  }
}
