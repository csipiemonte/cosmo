/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaAutorizzazioniFruitoriDTO;

/**
 *
 */

public interface CosmoDAutorizzazioniFruitoreSpecifications {

  static Specification<CosmoDAutorizzazioneFruitore> findByFilters(
      FiltroRicercaAutorizzazioniFruitoriDTO filtri, String sort) { // NOSONAR

    return (Root<CosmoDAutorizzazioneFruitore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {

        if (filtri.getCodice() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
              root.get(CosmoDAutorizzazioneFruitore_.codice), predicate, cb);
        }

        if (filtri.getDescrizione() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
              root.get(CosmoDAutorizzazioneFruitore_.descrizione), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
        }
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoDAutorizzazioneFruitore> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier =
        () -> cb.concat(root.get(CosmoDAutorizzazioneFruitore_.codice),
            root.get(CosmoDAutorizzazioneFruitore_.descrizione));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

  public static List<Order> getOrder(String sort, Root<CosmoDAutorizzazioneFruitore> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoDAutorizzazioneFruitore_.descrizione.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoDAutorizzazioneFruitore> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoDAutorizzazioneFruitore_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
