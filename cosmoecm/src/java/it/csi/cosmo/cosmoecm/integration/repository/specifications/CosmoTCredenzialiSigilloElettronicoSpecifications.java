/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

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
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico_;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoecm.dto.FiltroSigilloElettronicoDTO;

/**
 *
 */

public interface CosmoTCredenzialiSigilloElettronicoSpecifications {

  static Specification<CosmoTCredenzialiSigilloElettronico> findByFilters(
      FiltroSigilloElettronicoDTO filter,
      String sort) {
    return (Root<CosmoTCredenzialiSigilloElettronico> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      if (filter != null) {

        if (filter.getId() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getId(),
              root.get(CosmoTCredenzialiSigilloElettronico_.id),
              predicate, cb);
        }

        if (filter.getAlias() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getAlias(),
              root.get(CosmoTCredenzialiSigilloElettronico_.alias),
              predicate, cb);
        }

        if (filter.getDelegatedDomain() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getDelegatedDomain(),
              root.get(CosmoTCredenzialiSigilloElettronico_.delegatedDomain), predicate, cb);
        }

        if (filter.getDelegatedPassword() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getDelegatedPassword(),
              root.get(CosmoTCredenzialiSigilloElettronico_.delegatedPassword), predicate, cb);
        }

        if (filter.getDelegatedUser() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getDelegatedUser(),
              root.get(CosmoTCredenzialiSigilloElettronico_.delegatedUser), predicate, cb);
        }

        if (filter.getOtpPwd() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getOtpPwd(),
              root.get(CosmoTCredenzialiSigilloElettronico_.otpPwd), predicate, cb);
        }

        if (filter.getTipoHsm() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getTipoHsm(),
              root.get(CosmoTCredenzialiSigilloElettronico_.tipoHsm), predicate, cb);
        }

        if (filter.getTipoOtpAuth() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getTipoOtpAuth(),
              root.get(CosmoTCredenzialiSigilloElettronico_.tipoOtpAuth), predicate, cb);
        }

        if (filter.getUtente() != null) {
          predicate = SpecificationUtils.applyFilter(filter.getUtente(),
              root.get(CosmoTCredenzialiSigilloElettronico_.utente), predicate, cb);
        }

        if (filter.getFulltext() != null) {
          predicate = applyFullNameFilter(filter.getFulltext(), predicate, root, cb);
        }

      }

      return cq.where(predicate).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  public static List<Order> getOrder(String sort, Root<CosmoTCredenzialiSigilloElettronico> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTCredenzialiSigilloElettronico_.alias.getName();
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
      Root<CosmoTCredenzialiSigilloElettronico> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTSigilloElettronico_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTCredenzialiSigilloElettronico> root, CriteriaBuilder cb) {

    Supplier<Expression<String>> fullNameSupplier =
        () -> cb.concat(
            cb.concat(
                cb.concat(root.get(CosmoTCredenzialiSigilloElettronico_.alias),
                    root.get(CosmoTCredenzialiSigilloElettronico_.delegatedUser)),
                root.get(CosmoTCredenzialiSigilloElettronico_.delegatedDomain)),
            root.get(CosmoTCredenzialiSigilloElettronico_.utente));

        Predicate p1 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.get(), cb.conjunction(), cb);

        return cb.and(predicate, p1);
  }

}
