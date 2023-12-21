/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository.specifications;

import java.util.function.Supplier;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.search.RecursiveFilterHandler;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.FiltroRicercaFormLogiciDTO;

/**
 *
 */

public class CosmoTFormLogicoSearchHandler
implements RecursiveFilterHandler<CosmoTFormLogico, FiltroRicercaFormLogiciDTO> {

  @Override
  public Attribute<CosmoTFormLogico, ?> getDefaultSortField() {
    return CosmoTFormLogico_.descrizione;
  }

  @Override
  public Predicate applyFilter(FiltroRicercaFormLogiciDTO filtri, Root<CosmoTFormLogico> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb) {

    Predicate predicate = cb.conjunction();
    if (filtri.getId() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTFormLogico_.id),
          predicate, cb);
    }

    if (filtri.getCodice() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
          root.get(CosmoTFormLogico_.codice), predicate, cb);
    }

    if (filtri.getDescrizione() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
          root.get(CosmoTFormLogico_.descrizione), predicate, cb);
    }

    if (filtri.getFullText() != null) {
      predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
    }

    if (filtri.getIdEnte() != null) {
      predicate = applyIdEnteFilter(filtri.getIdEnte(), predicate, root, cb);
    }

    return predicate;
  }

  @Override
  public Order applySort(org.springframework.data.domain.Sort.Order clause) {
    // usa default sorting
    return null;
  }

  private static Predicate applyIdEnteFilter(LongFilter filter, Predicate predicate,
      Root<CosmoTFormLogico> root, CriteriaBuilder cb) {

    Join<CosmoTFormLogico, CosmoTEnte> join =
        root.join(CosmoTFormLogico_.cosmoTEnte, JoinType.INNER);

    Predicate p1 = cb.equal(join.get(CosmoTEnte_.id), filter.getEquals());


    return cb.and(predicate, p1);
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTFormLogico> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier = () -> cb
        .concat(root.get(CosmoTFormLogico_.codice), root.get(CosmoTFormLogico_.descrizione));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

}
