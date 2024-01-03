/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTTemplateFea;
import it.csi.cosmo.common.entities.CosmoTTemplateFea_;
import it.csi.cosmo.common.search.RecursiveFilterHandler;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaTemplateFeaDTO;

/**
 *
 */

public class CosmoTTemplateFeaSearchHandler
implements RecursiveFilterHandler<CosmoTTemplateFea, FiltroRicercaTemplateFeaDTO> {

  @Override
  public Attribute<CosmoTTemplateFea, ?> getDefaultSortField() {
    return CosmoTTemplateFea_.descrizione;
  }

  @Override
  public Predicate applyFilter(FiltroRicercaTemplateFeaDTO filtri, Root<CosmoTTemplateFea> root,
      CriteriaQuery<?> cq, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();
    if (filtri.getIdEnte() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
          root.get(CosmoTTemplateFea_.ente).get(CosmoTEnte_.id), predicate, cb);
    }
    if (filtri.getDescrizione() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDescrizione(),
          root.get(CosmoTTemplateFea_.descrizione), predicate, cb);
    }
    if (filtri.getCodiceTipoPratica() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoPratica(),
          root.get(CosmoTTemplateFea_.tipologiaPratica).get(CosmoDTipoPratica_.codice), predicate, cb);
    }
    if (filtri.getCodiceTipoDocumento() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getCodiceTipoDocumento(),
          root.get(CosmoTTemplateFea_.tipologiaDocumento).get(CosmoDTipoDocumento_.codice), predicate, cb);
    }
    if (filtri.getIdPratica() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getIdPratica(),
          root.get(CosmoTTemplateFea_.cosmoTPratica).get(CosmoTPratica_.id), predicate, cb);
    } else {
      predicate = cb.and(predicate, cb.isNull(root.get(CosmoTTemplateFea_.cosmoTPratica)));
    }

    if (filtri.getCodiciTipoDocumento() != null && !filtri.getCodiciTipoDocumento().isEmpty()) {
      predicate = cb.and(predicate, root.get(CosmoTTemplateFea_.tipologiaDocumento)
          .get(CosmoDTipoDocumento_.codice).in(filtri.getCodiciTipoDocumento()));
    }


    return predicate;
  }

  @Override
  public Order applySort(org.springframework.data.domain.Sort.Order parsed) {
    // usa il sorting di default
    return null;
  }

}
