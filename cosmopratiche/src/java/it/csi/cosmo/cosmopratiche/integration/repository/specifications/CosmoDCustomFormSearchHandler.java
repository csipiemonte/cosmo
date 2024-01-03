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
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.search.RecursiveFilterHandler;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaCustomFormDTO;

/**
 *
 */

public class CosmoDCustomFormSearchHandler
implements RecursiveFilterHandler<CosmoDCustomFormFormio, FiltroRicercaCustomFormDTO> {

  @Override
  public Attribute<CosmoDCustomFormFormio, ?> getDefaultSortField() {
    return CosmoDCustomFormFormio_.descrizione;
  }

  @Override
  public Predicate applyFilter(FiltroRicercaCustomFormDTO filtri, Root<CosmoDCustomFormFormio> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb) {

    Predicate predicate = cb.conjunction();

    if (filtri.getCodice() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodice(),
          root.get(CosmoDCustomFormFormio_.codice), predicate, cb);
    }

    if (filtri.getDescrizione() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
          root.get(CosmoDCustomFormFormio_.descrizione), predicate, cb);
    }

    if (filtri.getFullText() != null) {
      predicate = applyFullNameFilter(filtri.getFullText(), predicate, root, cb);
    }

    if (filtri.getSenzaAssociazioneConTipoPratica() != null
        && filtri.getSenzaAssociazioneConTipoPratica().getDefined() != null
        && Boolean.TRUE.equals(filtri.getSenzaAssociazioneConTipoPratica().getDefined())) {
      predicate = cb.and(predicate, cb.isNull(
          root.get(CosmoDCustomFormFormio_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice)));
    }

    return predicate;
  }

  @Override
  public Order applySort(org.springframework.data.domain.Sort.Order clause) {
    // usa default sorting
    return null;
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoDCustomFormFormio> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier = () -> cb
        .concat(root.get(CosmoDCustomFormFormio_.codice),
            root.get(CosmoDCustomFormFormio_.descrizione));

    Predicate p1 =
        SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

    return cb.and(predicate, p1);
  }

}
