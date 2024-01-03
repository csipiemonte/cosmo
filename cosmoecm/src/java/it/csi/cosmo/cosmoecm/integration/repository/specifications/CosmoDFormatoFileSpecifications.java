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
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDFormatoFile_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoecm.dto.FiltroRicercaFormatoFileDTO;

/**
 *
 */

public interface CosmoDFormatoFileSpecifications {

  static Specification<CosmoDFormatoFile> findByFilters(FiltroRicercaFormatoFileDTO filtri, String sort) { // NOSONAR

    return (Root<CosmoDFormatoFile> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {

        if (filtri.getCodice() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodice(), root.get(CosmoDFormatoFile_.codice),
              predicate, cb);
        }

        if (filtri.getDescrizione() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(), root.get(CosmoDFormatoFile_.descrizione),
              predicate, cb);
        }

        if (filtri.getMimeType() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getMimeType(),
              root.get(CosmoDFormatoFile_.mimeType), predicate, cb);
        }

        if (filtri.getSupportaAnteprima() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getSupportaAnteprima(),
              root.get(CosmoDFormatoFile_.supportaAnteprima), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getFullText(),
              cb.concat(root.get(CosmoDFormatoFile_.codice), root.get(CosmoDFormatoFile_.descrizione)),
              predicate, cb);
        }

      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  public static List<Order> getOrder(String sort, Root<CosmoDFormatoFile> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoDFormatoFile_.descrizione.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoDFormatoFile> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoDFormatoFile_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
