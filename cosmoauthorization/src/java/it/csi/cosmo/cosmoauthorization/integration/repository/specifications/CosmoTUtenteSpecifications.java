/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import edu.emory.mathcs.backport.java.util.Arrays;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte_;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaUtentiDTO;

/**
 *
 */

public interface CosmoTUtenteSpecifications {

  static Specification<CosmoTUtente> findByFilters(Long idEnte, FiltroRicercaUtentiDTO filtri,
      String sort) { // NOSONAR

    return (Root<CosmoTUtente> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.conjunction();

      ListJoin<CosmoTUtente, CosmoRUtenteEnte> joinUtenteEnte = null;
      Join<CosmoRUtenteEnte, CosmoTEnte> joinEnte = null;

      boolean filterByEnte = (idEnte != null) || (filtri != null && filtri.getIdEnte() != null)
          || (filtri != null && filtri.getCodiceIpaEnte() != null)
          || (filtri != null && filtri.getCodiceFiscaleEnte() != null)
          || (filtri != null && filtri.getEmail() != null);

      if (filterByEnte) {
        joinUtenteEnte = root.join(CosmoTUtente_.cosmoRUtenteEntes, JoinType.LEFT);
        predicate = cb.and(predicate, cb.or(cb.isNull(joinUtenteEnte.get(CosmoREntity_.dtFineVal)),
            cb.greaterThan(joinUtenteEnte.get(CosmoREntity_.dtFineVal),
                Timestamp.valueOf(LocalDateTime.now()))));
        joinEnte = joinUtenteEnte.join(CosmoRUtenteEnte_.cosmoTEnte, JoinType.LEFT);
      }

      if (idEnte != null) {
        predicate = cb.and(predicate, cb.equal(joinEnte.get(CosmoTEnte_.id), idEnte));
      }

      if (filtri != null) {
        if (filtri.getEmail() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getEmail(),
              joinUtenteEnte.get(CosmoRUtenteEnte_.email), predicate, cb);
        }

        if (filtri.getId() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTUtente_.id),
              predicate, cb);
        }

        if (filtri.getNome() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getNome(), root.get(CosmoTUtente_.nome),
              predicate, cb);
        }

        if (filtri.getCognome() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCognome(),
              root.get(CosmoTUtente_.cognome), predicate, cb);
        }

        if (filtri.getIdEnte() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
              joinEnte.get(CosmoTEnte_.id), predicate, cb);
        }

        if (filtri.getCodiceIpaEnte() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceIpaEnte(),
              joinEnte.get(CosmoTEnte_.codiceIpa), predicate, cb);
        }

        if (filtri.getCodiceFiscaleEnte() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceFiscaleEnte(),
              joinEnte.get(CosmoTEnte_.codiceFiscale), predicate, cb);
        }

        if (filtri.getCodiceFiscale() != null) {
          predicate = SpecificationUtils.applyFilter(filtri.getCodiceFiscale(),
              root.get(CosmoTUtente_.codiceFiscale), predicate, cb);
        }

        if (filtri.getFullText() != null) {
          predicate = applyFullTextFilter(filtri.getFullText(), predicate, root, cb);
        }

        if (filtri.getFullName() != null) {
          predicate = applyFullNameFilter(filtri.getFullName(), predicate, root, cb);
        }

        if (filtri.getProfili() != null && !filtri.getProfili().isEmpty()) {
          ListJoin<CosmoTUtente, CosmoRUtenteProfilo> joinUtenteProfilo = null;
          Join<CosmoRUtenteProfilo, CosmoTProfilo> joinProfilo = null;
          Join<CosmoRUtenteProfilo, CosmoTEnte> joinProfiloEnte = null;
          joinUtenteProfilo = root.join(CosmoTUtente_.cosmoRUtenteProfilos, JoinType.INNER);
          predicate = cb.and(predicate, cb.isNull(joinUtenteProfilo.get(CosmoREntity_.dtFineVal)));
          joinProfilo = joinUtenteProfilo.join(CosmoRUtenteProfilo_.cosmoTProfilo, JoinType.INNER);
          joinProfiloEnte = joinUtenteProfilo.join(CosmoRUtenteProfilo_.cosmoTEnte, JoinType.INNER);


          if (idEnte != null) {
            predicate = cb.and(predicate, cb.equal(joinProfiloEnte.get(CosmoTEnte_.id), idEnte));
          }

          if (filtri.getIdEnte() != null) {
            predicate = SpecificationUtils.applyFilter(filtri.getIdEnte(),
                joinProfiloEnte.get(CosmoTEnte_.id), predicate, cb);
          }

          if (filtri.getCodiceIpaEnte() != null) {
            predicate = SpecificationUtils.applyFilter(filtri.getCodiceIpaEnte(),
                joinProfiloEnte.get(CosmoTEnte_.codiceIpa), predicate, cb);
          }

          if (filtri.getCodiceFiscaleEnte() != null) {
            predicate = SpecificationUtils.applyFilter(filtri.getCodiceFiscaleEnte(),
                joinProfiloEnte.get(CosmoTEnte_.codiceFiscale), predicate, cb);
          }

          for (StringFilter filtro : filtri.getProfili()) {
            predicate = SpecificationUtils.applyFilter(filtro,
                joinProfilo.get(CosmoTProfilo_.codice), predicate, cb);
          }
        }

        if (filtri.getNeiTag() != null && !filtri.getNeiTag().isEmpty()) {
          ListJoin<CosmoTUtente, CosmoTUtenteGruppo> joinUtenteGruppo =
              root.join(CosmoTUtente_.cosmoTUtenteGruppos);

          ListJoin<CosmoTUtenteGruppo, CosmoRUtenteGruppoTag> joinUtenteGruppoTag =
              joinUtenteGruppo.join(CosmoTUtenteGruppo_.cosmoRUtenteGruppoTags);

          predicate = cb.and(predicate,
              cb.and(joinUtenteGruppoTag.get(CosmoREntity_.dtFineVal).isNull(),
                  joinUtenteGruppoTag.get(CosmoRUtenteGruppoTag_.cosmoTTag).get(CosmoTTag_.codice)
                  .in(filtri.getNeiTag()),
                  joinUtenteGruppoTag.get(CosmoRUtenteGruppoTag_.cosmoTTag)
                  .get(CosmoTEntity_.dtCancellazione).isNull()));
        }

        if (filtri.getNeiGruppi() != null && !filtri.getNeiGruppi().isEmpty()) {
          ListJoin<CosmoTUtente, CosmoTUtenteGruppo> joinUtenteGruppo =
              root.join(CosmoTUtente_.cosmoTUtenteGruppos, JoinType.INNER);

          ListJoin<CosmoTUtenteGruppo, CosmoRUtenteGruppoTag> joinUtenteGruppoTag =
              joinUtenteGruppo.join(CosmoTUtenteGruppo_.cosmoRUtenteGruppoTags, JoinType.LEFT);

          Join<CosmoRUtenteGruppoTag, CosmoTTag> joinUtenteGruppoTagTag =
              joinUtenteGruppoTag.join(CosmoRUtenteGruppoTag_.cosmoTTag, JoinType.LEFT);

          List<Predicate> predicateGruppi = new ArrayList<>();
          for (var gruppoTag : filtri.getNeiGruppi()) {

            if (gruppoTag.getNomeTag() != null && gruppoTag.getNomeTag().length > 0) {
              predicateGruppi.add(cb.and(
                  joinUtenteGruppoTag.get(CosmoREntity_.dtFineVal).isNull(),
                  joinUtenteGruppo.get(CosmoTEntity_.dtCancellazione).isNull(),
                  cb.equal(
                      joinUtenteGruppo.get(CosmoTUtenteGruppo_.gruppo).get(CosmoTGruppo_.codice),
                      gruppoTag.getNomeGruppo()),
                  joinUtenteGruppo.get(CosmoTUtenteGruppo_.gruppo)
                  .get(CosmoTEntity_.dtCancellazione).isNull(),
                  joinUtenteGruppoTagTag.get(CosmoTTag_.codice)
                  .in(Arrays.asList(gruppoTag.getNomeTag())),
                  joinUtenteGruppoTagTag.get(CosmoTEntity_.dtCancellazione).isNull()));

            } else {
              predicateGruppi.add(
                  cb.and(
                      joinUtenteGruppo.get(CosmoTEntity_.dtCancellazione).isNull(), cb.equal(
                          joinUtenteGruppo.get(CosmoTUtenteGruppo_.gruppo).get(CosmoTGruppo_.codice),
                          gruppoTag.getNomeGruppo()),
                      joinUtenteGruppo.get(CosmoTUtenteGruppo_.gruppo)
                          .get(CosmoTEntity_.dtCancellazione).isNull()));
            }

          }
          predicate = cb.and(predicate,
              cb.or(predicateGruppi.toArray(new Predicate[predicateGruppi.size()])));
        }

      }

      return cq.where(predicate).distinct(true).orderBy(getOrder(sort, root, cb)).getRestriction();
    };
  }

  private static Predicate applyFullNameFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTUtente> root, CriteriaBuilder cb) {

    Function<Boolean, Expression<String>> fullNameSupplier =
        (Boolean nameFirst) -> nameFirst.booleanValue()
        ? cb.concat(cb.concat(root.get(CosmoTUtente_.nome), cb.literal(" ")),
            root.get(CosmoTUtente_.cognome))
            : cb.concat(cb.concat(root.get(CosmoTUtente_.cognome), cb.literal(" ")),
                root.get(CosmoTUtente_.nome));

        Predicate p1 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(true), cb.conjunction(), cb);
        Predicate p2 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(false), cb.conjunction(), cb);

        return cb.and(predicate, cb.or(p1, p2));
  }

  private static Predicate applyFullTextFilter(StringFilter filter, Predicate predicate,
      Root<CosmoTUtente> root, CriteriaBuilder cb) {

    Function<Boolean, Expression<String>> fullNameSupplier =
        (Boolean nameFirst) -> nameFirst.booleanValue()
        ? cb.concat(cb.concat(root.get(CosmoTUtente_.nome), cb.literal(" ")),
            root.get(CosmoTUtente_.cognome))
            : cb.concat(cb.concat(root.get(CosmoTUtente_.cognome), cb.literal(" ")),
                root.get(CosmoTUtente_.nome));

        Predicate p1 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(true), cb.conjunction(), cb);
        Predicate p2 =
            SpecificationUtils.applyFilter(filter, fullNameSupplier.apply(false), cb.conjunction(), cb);
        Predicate p3 = SpecificationUtils.applyFilter(filter, root.get(CosmoTUtente_.codiceFiscale),
            cb.conjunction(), cb);

        return cb.and(predicate, cb.or(p1, p2, p3));
  }

  public static List<Order> getOrder(String sort, Root<CosmoTUtente> root, CriteriaBuilder cb) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTGruppo_.nome.getName();
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

  private static Order createSort(String sortSplitSingle, Root<CosmoTUtente> root,
      CriteriaBuilder cb) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return null;
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return null;
    }

    if (FieldUtils.getField(CosmoTUtente_.class, parsed.getProperty()) != null) {
      return parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty()));
    }
    return null;
  }

}
