/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

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
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.entities.CosmoTTemplateReport;
import it.csi.cosmo.common.entities.CosmoTTemplateReport_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoecm.dto.FiltroRicercaTemplateDTO;

/**
 *
 */

public interface CosmoTTemplateReportSpecifications {

  static Specification<CosmoTTemplateReport> findByFilters(FiltroRicercaTemplateDTO filtri,
      String sort) {

    return (Root<CosmoTTemplateReport> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null && filtri.getIdEnte() != null) {
        if (filtri.getIdEnte().size() > 1) {
          Predicate subPredicate = cb.conjunction();

          if (filtri.getIdEnte().containsKey(FilterCriteria.DEFINED.getCodice())) {
            if (Boolean.parseBoolean(filtri.getIdEnte().get(FilterCriteria.DEFINED.getCodice()))) {
              subPredicate =
                  cb.and(cb.isNotNull(root.get(CosmoTTemplateReport_.idEnte)));
            } else {
              subPredicate =
                  cb.and(cb.isNull(root.get(CosmoTTemplateReport_.idEnte)));
            }
          }

          if (filtri.getIdEnte().containsKey(FilterCriteria.EQUALS.getCodice())) {
            subPredicate = cb.or(subPredicate, cb.equal(root.get(CosmoTTemplateReport_.idEnte),
                filtri.getIdEnte().get(FilterCriteria.EQUALS.getCodice())));
          }
          predicate = cb.and(predicate, subPredicate);

        } else {
          predicate = SpecificationUtils.applyNumericFilter(filtri.getIdEnte(),
              root.get(CosmoTTemplateReport_.idEnte), predicate, cb);
        }
      }

      if (filtri != null && filtri.getCodiceTipoPratica() != null) {
        predicate = SpecificationUtils.applyStringFilter(filtri.getCodiceTipoPratica(),
            root.get(CosmoTTemplateReport_.codiceTipoPratica), predicate, cb);
      }

      if (filtri != null && filtri.getCodice() != null) {
        predicate = SpecificationUtils.applyStringFilter(filtri.getCodice(),
            root.get(CosmoTTemplateReport_.codice), predicate, cb);
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  public static List<Order> getOrder(String sort, Root<CosmoTTemplateReport> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTTemplateReport_.codice.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTTemplateReport> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTTemplateReport_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
