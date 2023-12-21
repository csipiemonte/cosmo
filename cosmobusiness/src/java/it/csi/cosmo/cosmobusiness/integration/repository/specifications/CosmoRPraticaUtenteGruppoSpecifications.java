/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.RelazionePraticaUtente;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;

/**
 *
 */

public interface CosmoRPraticaUtenteGruppoSpecifications {

  static Specification<CosmoRPraticaUtenteGruppo> findAllByAssociazionePreferita(Long idPratica,
      String utenteAttuale) {

    return (Root<CosmoRPraticaUtenteGruppo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
              idPratica),
          cb.isNull(root.get(CosmoREntity_.dtFineVal)),
          cb.equal(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica)
              .get(CosmoDTipoCondivisionePratica_.codice),
              RelazionePraticaUtente.PREFERITA.getCodice()),
          root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.codiceFiscale)
          .in(utenteAttuale).not(),
          cb.isNull(
              root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente)
                  .get(CosmoTEntity_.dtCancellazione)));

      return cq.where(predicate).getRestriction();
    };
  }

}
