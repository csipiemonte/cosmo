/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository.specifications;

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
import it.csi.cosmo.common.entities.CosmoDTipoNotifica_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTMessaggioNotifica;
import it.csi.cosmo.common.entities.CosmoTMessaggioNotifica_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaConfigurazioniMessaggiDTO;

/**
 *
 */

public interface CosmoTMessaggioNotificaSpecifications {

  static Specification<CosmoTMessaggioNotifica> findByFilters(
      FiltroRicercaConfigurazioniMessaggiDTO filtri, String sort) {

    return (Root<CosmoTMessaggioNotifica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filtri != null) {

        if (filtri.getId() != null) {
          predicate =
              SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTMessaggioNotifica_.id),
                  predicate, cb);
        }

        if (filtri.getCodiceTipoMessaggio() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoMessaggio(),
              root.get(CosmoTMessaggioNotifica_.cosmoDTipoNotifica).get(CosmoDTipoNotifica_.codice),
              predicate, cb);
        }

        if (filtri.getCodiceTipoPratica() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoPratica(),
              root.get(CosmoTMessaggioNotifica_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice),
              predicate, cb);
        }

        if (filtri.getIdEnte() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
              root.get(CosmoTMessaggioNotifica_.cosmoTEnte).get(CosmoTEnte_.id), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
        }
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }


  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTMessaggioNotifica> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier =
        () -> cb.concat(
            cb.concat(
                root.get(CosmoTMessaggioNotifica_.cosmoDTipoNotifica)
                .get(CosmoDTipoNotifica_.descrizione),
                root.get(CosmoTMessaggioNotifica_.cosmoTEnte).get(CosmoTEnte_.nome)),
            root.get(CosmoTMessaggioNotifica_.cosmoDTipoPratica)
            .get(CosmoDTipoPratica_.descrizione));

        Predicate p1 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

        return cb.and(predicate, p1);
  }

  public static List<Order> getOrder(String sort, Root<CosmoTMessaggioNotifica> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTMessaggioNotifica_.id.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTMessaggioNotifica> root,
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
