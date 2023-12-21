/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import it.csi.cosmo.common.entities.CosmoDTipoTag_;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTag_;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.search.RecursiveFilterHandler;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaTagsDTO;

/**
 *
 */

public class CosmoTTagSearchHandler
    implements RecursiveFilterHandler<CosmoTTag, FiltroRicercaTagsDTO> {

  @Override
  public Attribute<CosmoTTag, ?> getDefaultSortField() {
    return CosmoTTag_.descrizione;
  }

  @Override
  public Predicate applyFilter(FiltroRicercaTagsDTO filtri, Root<CosmoTTag> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();

    if (filtri.getCodice() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodice(), root.get(CosmoTTag_.codice),
          predicate, cb);
    }

    if (filtri.getDescrizione() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
          root.get(CosmoTTag_.descrizione), predicate, cb);
    }

    if (filtri.getIdEnte() != null) {
      predicate = SpecificationUtils.applyFilter(filtri
          .getIdEnte(),
          root.get(CosmoTTag_.cosmoTEnte).get(CosmoTEnte_.id), predicate, cb);
    }

    if (filtri.getCodiceTipoTag() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoTag(),
          root.get(CosmoTTag_.cosmoDTipoTag).get(CosmoDTipoTag_.codice), predicate, cb);
    }

    if (filtri.getIdGruppo() != null || filtri.getIdUtente() != null) {
      ListJoin <CosmoTTag, CosmoRUtenteGruppoTag> j = root.join(CosmoTTag_.cosmoRUtenteGruppoTags, JoinType.INNER);

      if (filtri.getIdGruppo() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getIdGruppo(),
            j.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo).get(CosmoTUtenteGruppo_.idGruppo),
            predicate, cb);
      }

      if (filtri.getIdUtente() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getIdUtente(),
            j.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo).get(CosmoTUtenteGruppo_.idUtente),
            predicate, cb);
      }
      predicate = cb.and(predicate, cb.isNull(j.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo).get(CosmoTEntity_.dtCancellazione)));
      predicate = cb.and(predicate, cb.isNull(j.get(CosmoREntity_.dtFineVal)));
    }

    if (filtri.getIdPratica() != null ) {
      ListJoin<CosmoTTag, CosmoRPraticaTag> jtp =
          root.join(CosmoTTag_.cosmoRPraticaTags, JoinType.INNER);

      predicate = SpecificationUtils.applyFilter(filtri.getIdPratica(),
          jtp.get(CosmoRPraticaTag_.cosmoTPratica).get(CosmoTPratica_.id), predicate, cb);

      predicate = cb.and(predicate, cb.isNull(jtp.get(CosmoREntity_.dtFineVal)));

    }

    if (filtri.getFullText() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getFullText(),
          cb.concat(root.get(CosmoTTag_.descrizione),
              root.get(CosmoTTag_.cosmoDTipoTag).get(CosmoDTipoTag_.descrizione)),
          predicate,
          cb);
    }


    return predicate;
  }

  @Override
  public Order applySort(org.springframework.data.domain.Sort.Order parsed) {
    // usa default sorting
    return null;
  }


}
