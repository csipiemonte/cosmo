/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository.specifications;

import java.sql.Timestamp;
import java.util.Arrays;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoDStatoCaricamentoPratica_;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.StatoCaricamentoPratica;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaCaricamentoPraticheDTO;

/**
 *
 */

public interface CosmoTCaricamentoPraticaSpecifications {

  static Specification<CosmoTCaricamentoPratica> findByEnteFiltersIdParentNull(Long idEnte,
      Long idUtente,
      FiltroRicercaCaricamentoPraticheDTO filter) {
    return (Root<CosmoTCaricamentoPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate =
          cb.and(
              cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
              cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTUtente).get(CosmoTUtente_.id),
                  idUtente),
              cb.isNull(root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
                  .get(CosmoDEntity_.dtFineVal)),
              cb.notEqual(
                  root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
                  .get(CosmoDStatoCaricamentoPratica_.codice),
                  StatoCaricamentoPratica.CARICAMENTO_IN_BOZZA.getCodice()),
              cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
              cb.isNull(root.get(CosmoTCaricamentoPratica_.cosmoTCaricamentoPratica)));


      if (filter != null) {

        if (filter.getCodiceStatoCaricamentoPratica() != null) {

          predicate = SpecificationUtils.applyFilter(filter.getCodiceStatoCaricamentoPratica(),
              root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
              .get(CosmoDStatoCaricamentoPratica_.codice),
              predicate, cb);
        }

        if (filter.getNomeFile() != null) {

          predicate = SpecificationUtils.applyFilter(filter.getNomeFile(),
              root.get(CosmoTCaricamentoPratica_.nomeFile), predicate, cb);
        }

      }


      return cq.where(predicate).distinct(true)
          .orderBy(cb.desc(root.get(CosmoTCaricamentoPratica_.id))).getRestriction();
    };
  }

  static Specification<CosmoTCaricamentoPratica> findByEnteAndCaricamentoInBozza(Long idEnte,
      Long idUtente) {
    return (Root<CosmoTCaricamentoPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
          cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTUtente).get(CosmoTUtente_.id),
              idUtente),
          cb.isNull(root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
              .get(CosmoDEntity_.dtFineVal)),
          cb.equal(
              root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
              .get(CosmoDStatoCaricamentoPratica_.codice),
              StatoCaricamentoPratica.CARICAMENTO_IN_BOZZA.getCodice()),
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)));


      return cq.where(predicate).distinct(true)
          .orderBy(cb.desc(root.get(CosmoTCaricamentoPratica_.id))).getRestriction();
    };
  }

  static Specification<CosmoTCaricamentoPratica> findByEnteWithIdParentNotNullSortDesc(Long idEnte,
      Long idUtente,
      Long idParent) {
    return (Root<CosmoTCaricamentoPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate = cb
          .and(cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTEnte).get(CosmoTEnte_.id), idEnte),
              cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTUtente).get(CosmoTUtente_.id),
                  idUtente),
              cb.isNull(root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
                  .get(CosmoDEntity_.dtFineVal)),
              cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
              cb.equal(root.get(CosmoTCaricamentoPratica_.cosmoTCaricamentoPratica)
                  .get(CosmoTCaricamentoPratica_.id), idParent),
              cb.isNull(root.get(CosmoTCaricamentoPratica_.cosmoTCaricamentoPratica)
                  .get(CosmoTEntity_.dtCancellazione)));





      return cq.where(predicate).distinct(true)
          .orderBy(cb.asc(root.get(CosmoTCaricamentoPratica_.id))).getRestriction();
    };
  }

  static Specification<CosmoTCaricamentoPratica> findAllActiveElaborazioneCompletataAndInErrore(
      Timestamp date) {
    return (Root<CosmoTCaricamentoPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate = cb.and(cb.lessThan(root.get(CosmoTEntity_.dtInserimento), date),
          cb.isNotNull(root.get(CosmoTCaricamentoPratica_.pathFile)),
          root
          .get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
          .get(CosmoDStatoCaricamentoPratica_.codice)
          .in(Arrays.asList(StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA.getCodice(),
              StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA_CON_ERRORE.getCodice(),
              StatoCaricamentoPratica.ERRORE_ELABORAZIONE.getCodice())));


      return cq.where(predicate).distinct(true).getRestriction();
    };
  }

  static Specification<CosmoTCaricamentoPratica> findAllActiveByPathFile(String path) {
    return (Root<CosmoTCaricamentoPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {


      Predicate predicate = cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(root.get(CosmoTCaricamentoPratica_.pathFile), path),
          root.get(CosmoTCaricamentoPratica_.cosmoDStatoCaricamentoPratica)
          .get(CosmoDStatoCaricamentoPratica_.codice)
          .in(Arrays.asList(StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA.getCodice(),
              StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA_CON_ERRORE.getCodice(),
              StatoCaricamentoPratica.ERRORE_ELABORAZIONE.getCodice())));


      return cq.where(predicate).distinct(true).getRestriction();
    };
  }

}
