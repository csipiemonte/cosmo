/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository.specifications;

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
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.FiltroRicercaIstanzeFormLogiciDTO;

/**
 *
 */

public interface CosmoTIstanzaFunzionalitaFormLogicoSpecifications {

  static Specification<CosmoTIstanzaFunzionalitaFormLogico> findByFilters(
      FiltroRicercaIstanzeFormLogiciDTO filtri, String sort) { // NOSONAR

    return (Root<CosmoTIstanzaFunzionalitaFormLogico> root, CriteriaQuery<?> cq,
        CriteriaBuilder cb) -> {

          Predicate predicate = cb.conjunction();

          if (filtri != null) {

            if (filtri.getId() != null) {
              predicate = SpecificationUtils.applyFilter(filtri.getId(),
                  root.get(CosmoTIstanzaFunzionalitaFormLogico_.id),
                  predicate, cb);
            }

            if (filtri.getCodice() != null) {
              predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
                  root.get(CosmoTIstanzaFunzionalitaFormLogico_.cosmoDFunzionalitaFormLogico)
                  .get(CosmoDFunzionalitaFormLogico_.codice),
                  predicate, cb);
            }

            if (filtri.getDescrizione() != null) {
              predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
                  root.get(CosmoTIstanzaFunzionalitaFormLogico_.descrizione), predicate, cb);
            }
          }

          return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
        };
  }

  public static List<Order> getOrder(String sort, Root<CosmoTIstanzaFunzionalitaFormLogico> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoDFunzionalitaFormLogico_.descrizione.getName();
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

  private static Order createSort(String sortSplitSingle,
      Root<CosmoTIstanzaFunzionalitaFormLogico> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTIstanzaFunzionalitaFormLogico_.class,
        parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
