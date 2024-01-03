/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository.specifications;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio_;
import it.csi.cosmo.common.entities.CosmoDHelperModale;
import it.csi.cosmo.common.entities.CosmoDHelperModale_;
import it.csi.cosmo.common.entities.CosmoDHelperPagina;
import it.csi.cosmo.common.entities.CosmoDHelperPagina_;
import it.csi.cosmo.common.entities.CosmoDHelperTab;
import it.csi.cosmo.common.entities.CosmoDHelperTab_;
import it.csi.cosmo.common.entities.CosmoTHelper;
import it.csi.cosmo.common.entities.CosmoTHelper_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmonotifications.config.Constants;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaHelperDTO;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaHelperModaleDTO;

/**
 *
 */

public interface CosmoTHelperSpecifications {
  static Specification<CosmoTHelper> findByFilters(FiltroRicercaHelperDTO filtri, String sort) {

    return (Root<CosmoTHelper> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Join<CosmoTHelper, CosmoDHelperPagina> joinHelperCodicePagina =
          root.join(CosmoTHelper_.helperPagina, JoinType.INNER);


      Predicate predicate = cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(root.get(CosmoTEntity_.utenteCancellazione)));

      if (filtri != null && filtri.getId() != null) {
        predicate = SpecificationUtils.applyFilter(filtri
            .getId(),
            root.get(CosmoTHelper_.id), predicate, cb);
      }

      if (filtri != null && filtri.getCodicePagina() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getCodicePagina(),
            joinHelperCodicePagina.get(CosmoDHelperPagina_.codice), predicate, cb);
      }
      if (filtri != null && filtri.getCodiceTab() != null) {
        Join<CosmoTHelper, CosmoDHelperTab> joinHelperCodiceTab =
            root.join(CosmoTHelper_.helperTab, JoinType.INNER);
        predicate = SpecificationUtils.applyFilter(filtri.getCodiceTab(),
            joinHelperCodiceTab.get(CosmoDHelperTab_.codice), predicate, cb);
      }
      if (filtri != null && filtri.getCodiceForm() != null) {
        Join<CosmoTHelper, CosmoDCustomFormFormio> joinHelperCodiceForm =
            root.join(CosmoTHelper_.helperForm, JoinType.INNER);
        predicate = SpecificationUtils.applyFilter(filtri.getCodiceForm(),
            joinHelperCodiceForm.get(CosmoDCustomFormFormio_.codice), predicate, cb);
      }
      if (filtri != null && filtri.getCodiceModale() != null) {
        Join<CosmoTHelper, CosmoDHelperModale> joinHelperCodiceModale =
            root.join(CosmoTHelper_.helperModale, JoinType.INNER);
        predicate = SpecificationUtils.applyFilter(filtri.getCodiceModale(),
            joinHelperCodiceModale.get(CosmoDHelperModale_.codice), predicate, cb);
      }
      if (StringUtils.isBlank(sort)) {
        return cq.where(predicate).orderBy(cb.asc(root.get(CosmoTHelper_.id))).getRestriction();
      } else {
        List<Order> ordine =
            getOrder(sort, root, joinHelperCodicePagina, cb);
        return cq.where(predicate).orderBy(ordine).getRestriction();
      }

    };
  }

  private static List<Order> getOrder(String sort, Root<CosmoTHelper> root,
      Join<CosmoTHelper, CosmoDHelperPagina> join,
      CriteriaBuilder cb) {
    String[] sortSplit = sort.trim().split(Constants.SPLIT_REGEX);

    List<Order> ordine = new LinkedList<>();
    for (String sortSplitSingle : sortSplit) {

      String[] sortFieldSplit = sortSplitSingle.trim().split(" ");
      boolean sortDirection = sortFieldSplit[1].equals("ASC");

      if (sortFieldSplit[0].equalsIgnoreCase(Constants.SORT.CODICE_PAGINA)) {
        if (sortDirection) {
          ordine.add(cb.asc(join.get(CosmoDHelperPagina_.codice)));
        } else {
          if (join != null) {
            ordine.add(cb.desc(join.get(CosmoDHelperPagina_.codice)));
          }
        }
      }

      if (sortFieldSplit[0].equalsIgnoreCase(Constants.SORT.DESCRIZIONE)) {

        if (sortDirection) {
          ordine.add(cb.asc(join.get(CosmoDHelperPagina_.descrizione)));
        } else {
          ordine.add(cb.desc(join.get(CosmoDHelperPagina_.descrizione)));
        }
      }

      if (sortFieldSplit[0].equalsIgnoreCase(Constants.SORT.CODICE_MODALE)) {
        if (sortDirection) {
          ordine.add(cb.asc(root.get(CosmoTHelper_.helperModale).get(CosmoDHelperModale_.codice)));
        } else {
          if (join != null) {
            ordine.add(cb.desc(root.get(CosmoTHelper_.helperModale).get(CosmoDHelperModale_.codice)));
          }
        }
      }
    }


    return ordine;
  }

  static Specification<CosmoDHelperModale> findModaliByFilters(FiltroRicercaHelperModaleDTO filtri) {
    return (Root<CosmoDHelperModale> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.and(cb.isNull(root.get(CosmoDEntity_.dtFineVal)));

      if (filtri != null && filtri.getCodicePagina() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getCodicePagina(),
            root.get(CosmoDHelperModale_.helperPagina).get(CosmoDHelperPagina_.codice), predicate,
            cb);
      }

      if (filtri != null && filtri.getCodiceTab() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getCodiceTab(),
            root.get(CosmoDHelperModale_.helperTab).get(CosmoDHelperTab_.codice), predicate, cb);
      }
        return cq.where(predicate).orderBy(cb.asc(root.get(CosmoDHelperModale_.codice))).getRestriction();
    };
  }
}
