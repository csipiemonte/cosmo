/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository.specifications;

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
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaVariabiliProcessoDTO;

/**
 *
 */

public interface CosmoTVariabileProcessoSpecifications {



  static Specification<CosmoTVariabileProcesso> findByFilters(
      FiltroRicercaVariabiliProcessoDTO filtri, String sort) {

    return (Root<CosmoTVariabileProcesso> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(root.get(CosmoTEntity_.utenteCancellazione)));

      if (filtri != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getLabelFiltro(),
            root.get(CosmoTVariabileProcesso_.nomeVariabile), predicate, cb);

        predicate = SpecificationUtils.applyFilter(filtri.getNomeFiltro(),
            root.get(CosmoTVariabileProcesso_.nomeVariabileFlowable), predicate, cb);

        predicate = SpecificationUtils.applyFilter(filtri.getNomeEnte(),
            root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.cosmoTEnte)
            .get(CosmoTEnte_.nome),
            predicate, cb);

        predicate = SpecificationUtils.applyFilter(filtri.getDescrizioneTipoPratica(),
            root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.descrizione),
            predicate, cb);
      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };


  }


  public static List<Order> getOrder(String sort, Root<CosmoTVariabileProcesso> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTVariabileProcesso_.nomeVariabile.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTVariabileProcesso> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTVariabileProcesso.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
