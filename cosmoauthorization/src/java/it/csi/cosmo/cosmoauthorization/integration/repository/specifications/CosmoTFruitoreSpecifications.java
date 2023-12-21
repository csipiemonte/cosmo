/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaFruitoriDTO;

/**
 *
 */

public interface CosmoTFruitoreSpecifications {

  static Specification<CosmoTFruitore> findByFilters(FiltroRicercaFruitoriDTO filtri, String sort,
      Long idEnte) { // NOSONAR

    return (Root<CosmoTFruitore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {

        if (filtri.getId() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTFruitore_.id),
              predicate, cb);
        }

        if (filtri.getApiManagerId() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getApiManagerId(),
              root.get(CosmoTFruitore_.apiManagerId), predicate, cb);
        }

        if (filtri.getNomeApp() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getNomeApp(),
              root.get(CosmoTFruitore_.nomeApp), predicate, cb);
        }

        if (filtri.getUrl() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getUrl(), root.get(CosmoTFruitore_.url),
              predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
        }
      }

      if (idEnte != null) {
        // filtra per visibilita' ente su fruitore
        //@formatter:off
        Timestamp now = Timestamp.from(Instant.now());
        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<CosmoRFruitoreEnte> subRoot = subquery.from(CosmoRFruitoreEnte.class);
        subquery.select(subRoot.get(CosmoRFruitoreEnte_.cosmoTFruitore).get(CosmoTFruitore_.id))
            .where(
            cb.and(
                cb.equal(subRoot.get(CosmoRFruitoreEnte_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
                cb.isNotNull(subRoot.get(CosmoREntity_.dtInizioVal)),
                cb.lessThanOrEqualTo(subRoot.get(CosmoREntity_.dtInizioVal), now),
                cb.or(
                    cb.isNull(subRoot.get(CosmoREntity_.dtFineVal)),
                    cb.greaterThan(subRoot.get(CosmoREntity_.dtFineVal), now)
                )
            ));

        predicate = cb.and(predicate, cb.in(root.get(CosmoTFruitore_.id)).value(subquery));
        //@formatter:on
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTFruitore> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier = () -> cb.concat(
        cb.concat(root.get(CosmoTFruitore_.nomeApp), root.get(CosmoTFruitore_.apiManagerId)),
        root.get(CosmoTFruitore_.url));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

  public static List<Order> getOrder(String sort, Root<CosmoTFruitore> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTFruitore_.nomeApp.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTFruitore> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTFruitore_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
