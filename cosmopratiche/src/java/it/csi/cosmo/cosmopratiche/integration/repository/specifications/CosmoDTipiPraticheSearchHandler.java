/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository.specifications;

import java.util.function.Supplier;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.search.RecursiveFilterHandler;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaTipiPraticheDTO;


public class CosmoDTipiPraticheSearchHandler
implements RecursiveFilterHandler<CosmoDTipoPratica, FiltroRicercaTipiPraticheDTO > {

  @Override
  public Attribute<CosmoDTipoPratica, ?> getDefaultSortField() {
    return CosmoDTipoPratica_.descrizione;
  }

  @Override
  public Predicate applyFilter(FiltroRicercaTipiPraticheDTO filtri, Root<CosmoDTipoPratica> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb) {

    Predicate predicate = cb.conjunction();

    if (filtri.getCodice() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
          root.get(CosmoDTipoPratica_.codice), predicate, cb);
    }


    if (filtri.getDescrizione() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
          root.get(CosmoDTipoPratica_.descrizione), predicate, cb);
    }

    if (filtri.getIdEnte() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
          root.get(CosmoDTipoPratica_.cosmoTEnte).get(CosmoTEnte_.id), predicate, cb);
    }

    if (filtri.getFullText() != null) {
      predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
    }



    return predicate;
  }

  @Override
  public Order applySort(org.springframework.data.domain.Sort.Order clause) {
    // usa default sorting
    return null;
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoDTipoPratica> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier = () -> cb
        .concat(root.get(CosmoDTipoPratica_.codice),
            root.get(CosmoDTipoPratica_.descrizione));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

}

