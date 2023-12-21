/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.util.SearchUtils;

/**
 *
 */

public interface RecursiveFilterHandler<E, F extends RecursiveFilterSpecification<F>> {

  // Class<E> getEntityClass();

  Attribute<E, ?> getDefaultSortField(); // NOSONAR

  Predicate applyFilter(F filtri, Root<E> root, CriteriaQuery<?> cq,
      CriteriaBuilder cb);

  Order applySort(org.springframework.data.domain.Sort.Order parsed);

  default Specification<E> findByFilters(F filtri, String sort) { // NOSONAR

    return (Root<E> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {
        predicate = toPredicate(filtri, root, cq, cb);
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  default Predicate toPredicate(F filtri, Root<E> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();

    if (filtri != null) {
      predicate = applyFilter(filtri, root, cq, cb);
      if (predicate == null) {
        predicate = cb.conjunction();
      }

      if (filtri.getAnd() != null && filtri.getAnd().length > 0) {
        List<Predicate> listOfAndPredicates = Arrays.stream(filtri.getAnd())
            .map(f -> toPredicate(f, root, cq, cb)).collect(Collectors.toList());
        listOfAndPredicates.add(predicate);
        predicate = cb.and(listOfAndPredicates.toArray(new Predicate[0]));
      }

      if (filtri.getOr() != null && filtri.getOr().length > 0) {
        List<Predicate> listOfAndPredicates = Arrays.stream(filtri.getOr())
            .map(f -> toPredicate(f, root, cq, cb)).collect(Collectors.toList());
        listOfAndPredicates.add(predicate);
        predicate = cb.or(listOfAndPredicates.toArray(new Predicate[0]));
      }
    }

    return predicate;
  }

  default List<Order> getOrder(String sort, Root<E> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      Attribute<E, ?> defSortField = getDefaultSortField();
      sort = defSortField != null ? defSortField.getName() : null;
    }

    if (StringUtils.isBlank(sort)) {
      return Collections.emptyList();
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

  default Order createSort(String sortSplitSingle, Root<E> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);

    if (parsed == null) {
      return null;
    }

    // Class<E> entityClass = getEntityClass();
    Order clause = applySort(parsed);
    if (clause != null) {
      return clause;
    }

    // if (FieldUtils.getField(entityClass, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    // } else {
    // throw new BadRequestException("Invalid sort clause '" + sortSplitSingle + "'");
    // }
  }

}
