/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaProfiliDTO;

/**
 *
 */

public interface CosmoTProfiloSpecifications {

  static Specification<CosmoTProfilo> findByFilters(FiltroRicercaProfiliDTO filtri,
      String sort) { // NOSONAR

    return (Root<CosmoTProfilo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {
        if (filtri.getAssegnabile() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getAssegnabile(),
              root.get(CosmoTProfilo_.assegnabile), predicate, cb);
        }

        if (filtri.getCodice() != null && !filtri.getCodice().isEmpty()) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
              root.get(CosmoTProfilo_.codice), predicate, cb);

        }
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  public static List<Order> getOrder(String sort, Root<CosmoTProfilo> root, CriteriaBuilder cb) {
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTProfilo> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTProfilo_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
