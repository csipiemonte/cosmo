/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.sql.Timestamp;
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
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaEntiDTO;

/**
 *
 */

public interface CosmoTEnteSpecifications {

  static Specification<CosmoTEnte> findByEnteValido(Timestamp currentDate) {

    return (Root<CosmoTEnte> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.isNull(root.get(CosmoTEntity_.dtCancellazione));

      return cq.where(predicate).getRestriction();
    };
  }

  static Specification<CosmoTEnte> findByFilters(FiltroRicercaEntiDTO filtri, String sort) { // NOSONAR

    return (Root<CosmoTEnte> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {

        if (filtri.getId() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTEnte_.id),
              predicate, cb);
        }

        if (filtri.getNome() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getNome(), root.get(CosmoTEnte_.nome),
              predicate, cb);
        }

        if (filtri.getCodiceFiscale() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceFiscale(),
              root.get(CosmoTEnte_.codiceFiscale), predicate, cb);
        }

        if (filtri.getCodiceIpa() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceIpa(),
              root.get(CosmoTEnte_.codiceIpa), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
        }
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTEnte> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier =
        () -> cb.concat(cb.concat(root.get(CosmoTEnte_.nome), root.get(CosmoTEnte_.codiceFiscale)),
            root.get(CosmoTEnte_.codiceIpa));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

  public static List<Order> getOrder(String sort, Root<CosmoTEnte> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTEnte_.nome.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTEnte> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTEnte_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
