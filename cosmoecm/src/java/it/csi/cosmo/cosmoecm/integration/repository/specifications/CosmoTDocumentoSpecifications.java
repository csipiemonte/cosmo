/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDFormatoFile_;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento_;
import it.csi.cosmo.common.entities.CosmoDStatoInvioStilo_;
import it.csi.cosmo.common.entities.CosmoDStatoSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoDStatoSigilloElettronico_;
import it.csi.cosmo.common.entities.CosmoDStatoSmistamento;
import it.csi.cosmo.common.entities.CosmoDStatoSmistamento_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoFirmato_;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRInvioStiloDocumento;
import it.csi.cosmo.common.entities.CosmoRInvioStiloDocumentoPK_;
import it.csi.cosmo.common.entities.CosmoRInvioStiloDocumento_;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento_;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.StatoInvioStilo;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.dto.DocumentiDaFirmareDTO;
import it.csi.cosmo.cosmoecm.dto.StatoSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiDTO;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiInvioStiloDTO;
import it.csi.cosmo.cosmoecm.dto.stardas.StatoSmistamento;

/**
 *
 */

public interface CosmoTDocumentoSpecifications {


  static Specification<CosmoTDocumento> findDocumentiInvioStilo(
      FiltroRicercaDocumentiInvioStiloDTO filtri) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Predicate predicate = cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(root.get(CosmoTEntity_.utenteCancellazione)));

      predicate = SpecificationUtils.applyFilter(filtri.getIdPratica(),
          root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), predicate, cb);

      predicate = SpecificationUtils.applyFilter(filtri.getTipologiaDocumento(),
          root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.codice), predicate, cb);



      if (filtri.getIdUd()!=null && !filtri.getIdUd().isEmpty()) {

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<CosmoRInvioStiloDocumento> subRoot =
            subquery.from(CosmoRInvioStiloDocumento.class);

        Predicate invioStiloPred = cb.and(
            SpecificationUtils.applyFilter(filtri.getIdUd(),
                subRoot.get(CosmoRInvioStiloDocumento_.id).get(CosmoRInvioStiloDocumentoPK_.idUd),
                predicate, cb),

            cb.or(cb.isNull(subRoot.get(CosmoREntity_.dtFineVal)),
                cb.greaterThan(subRoot.get(CosmoREntity_.dtFineVal), now)),

            cb.or(
                cb.equal(subRoot.get(CosmoRInvioStiloDocumento_.cosmoDStatoInvioStilo)
                    .get(CosmoDStatoInvioStilo_.codice), StatoInvioStilo.INVIATO.getCodice()),
                cb.equal(
                    subRoot.get(CosmoRInvioStiloDocumento_.cosmoDStatoInvioStilo)
                    .get(CosmoDStatoInvioStilo_.codice),
                    StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()),
                cb.greaterThan(subRoot.get(CosmoRInvioStiloDocumento_.numeroRetry), 4))
            );

        subquery
        .select(
            subRoot.get(CosmoRInvioStiloDocumento_.cosmoTDocumento).get(CosmoTDocumento_.id))
        .where(invioStiloPred);

        predicate = cb.and(predicate, cb.in(root.get(CosmoTDocumento_.id)).value(subquery).not());

      }


      return cq.where(predicate).distinct(true).getRestriction();
    };
  }

  static Specification<CosmoTDocumento> findByChiaveFruitoreEsterno(String idDocumento,
      String idPratica, String codiceIpaEnte, Long idFruitore) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Join<CosmoTDocumento, CosmoTPratica> joinPratica =
          root.join(CosmoTDocumento_.pratica, JoinType.LEFT);

      //@formatter:off
      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoTDocumento_.idDocumentoExt), idDocumento),
          cb.equal(joinPratica.get(CosmoTPratica_.idPraticaExt), idPratica),
          cb.equal(joinPratica.get(CosmoTPratica_.fruitore).get(CosmoTFruitore_.id), idFruitore),
          cb.equal(joinPratica.get(CosmoTPratica_.ente).get(CosmoTEnte_.codiceIpa), codiceIpaEnte));
      //@formatter:on

      return cq.where(predicate).getRestriction();
    };
  }

  static Specification<CosmoTDocumento> findByStato(StatoDocumento stato) {
    return findByStato(stato, null, null);
  }

  static Specification<CosmoTDocumento> findByStato(StatoDocumento stato, LocalDate after,
      Integer numMaxRetries) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Join<CosmoTDocumento, CosmoDStatoDocumento> joinStato =
          root.join(CosmoTDocumento_.stato, JoinType.LEFT);

      var predicate = cb.equal(joinStato.get(CosmoDStatoDocumento_.codice), stato.name());

      if (after != null) {
        predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get(CosmoTEntity_.dtInserimento),
            Timestamp.valueOf(after.atStartOfDay())));
      }

      if (numMaxRetries != null) {
        predicate = cb.and(predicate, cb.or(
            cb.isNull(root.get(CosmoTDocumento_.numeroTentativiAcquisizione)),
            cb.lessThan(root.get(CosmoTDocumento_.numeroTentativiAcquisizione), numMaxRetries)));
      }

      return predicate;
    };
  }

  static Specification<CosmoTDocumento> findByFiltri(FiltroRicercaDocumentiDTO filtri,
      String sort) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      ListJoin<CosmoTDocumento, CosmoTContenutoDocumento> joinDocumentoContenuto = null;
      Join<CosmoTContenutoDocumento, CosmoDFormatoFile> joinContenutoDocFormatoFile = null;

      Predicate predicate = cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(root.get(CosmoTEntity_.utenteCancellazione)));

      if (filtri != null && filtri.getIdPratica() != null) {
        predicate = SpecificationUtils.applyNumericFilter(filtri.getIdPratica(),
            root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), predicate, cb);
      }

      if (filtri != null && filtri.getIdParent() != null) {
        predicate = SpecificationUtils.applyNumericFilter(filtri.getIdParent(),
            root.get(CosmoTDocumento_.documentoPadre).get(CosmoTDocumento_.id), predicate, cb);
      }

      if (filtri != null && filtri.getTipo() != null) {
        predicate = SpecificationUtils.applyStringFilter(filtri.getTipo(),
            root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.codice), predicate, cb);
      }

      if (filtri != null && filtri.getNomeFile() != null
          && filtri.getNomeFile().containsKey(FilterCriteria.EQUALS.getCodice())) {
        joinDocumentoContenuto = root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);
        joinDocumentoContenuto.on(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.TEMPORANEO.toString()));
        predicate = cb.and(predicate,
            cb.equal(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.nomeFile),
                filtri.getNomeFile().get(FilterCriteria.EQUALS.getCodice())));

      }

      if (filtri != null && filtri.getTitoloNomeFile() != null && filtri.getTitoloNomeFile()
          .containsKey(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())) {
        joinDocumentoContenuto = root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);
        joinDocumentoContenuto.on(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.TEMPORANEO.toString()));

        Predicate nomeFilePredicate =
            cb.or(
                cb.like(cb.lower(root.get(CosmoTDocumento_.titolo)),
                    "%" + ((String) filtri.getTitoloNomeFile()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase() + "%"),
                cb.like(cb.lower(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.nomeFile)),
                    "%" + ((String) filtri.getTitoloNomeFile()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase()
                    + "%"));
        predicate = cb.and(predicate, nomeFilePredicate);

      }

      if (filtri != null && filtri.getFormato() != null
          && filtri.getFormato().containsKey(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())) {
        joinDocumentoContenuto = root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);
        joinDocumentoContenuto.on(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.TEMPORANEO.toString(),
                TipoContenutoDocumento.SBUSTATO.toString()));
        joinContenutoDocFormatoFile =
            joinDocumentoContenuto.join(CosmoTContenutoDocumento_.formatoFile, JoinType.INNER);

        Predicate contenutoFormatoPredicate =
            cb.or(
                cb.like(cb.lower(joinContenutoDocFormatoFile.get(CosmoDFormatoFile_.descrizione)),
                    "%" + ((String) filtri.getFormato()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase() + "%"),
                cb.like(cb.lower(joinContenutoDocFormatoFile.get(CosmoDFormatoFile_.mimeType)),
                    "%" + ((String) filtri.getFormato()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase()
                    + "%"));


        root.fetch(CosmoTDocumento_.documentiFigli, JoinType.INNER);

        predicate = cb.and(predicate, contenutoFormatoPredicate);

      }

      if (filtri != null && filtri.getCodiceTipoFirma() != null) {

        var joinDocumentoContenuti = root.join(CosmoTDocumento_.contenuti, JoinType.INNER);
        joinDocumentoContenuti.on(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.FIRMATO.toString()));

        if (filtri.getCodiceTipoFirma().containsKey(FilterCriteria.NOT_IN.getCodice())) {

          var subquery = cq.subquery(Long.class);
          var subroot = subquery.from(CosmoTContenutoDocumento.class);
          subquery.select(
              subroot.get(CosmoTContenutoDocumento_.documentoPadre).get(CosmoTDocumento_.id));
          var subpredicate = cb.and(cb.equal(
              subroot.get(CosmoTContenutoDocumento_.tipo).get(CosmoDTipoContenutoDocumento_.codice),
              TipoContenutoDocumento.FIRMATO.toString()));
          subquery.where(subpredicate);

          predicate = cb.and(predicate,
              cb.or(
                  cb.not(
                      joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipoContenutoFirmato)
                      .get(CosmoDTipoContenutoFirmato_.codice)
                      .in(filtri.getCodiceTipoFirma()
                          .get(FilterCriteria.NOT_IN.getCodice()))),
                  cb.not(cb.in(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.documentoPadre)
                      .get(CosmoTDocumento_.id)).value(subquery))));

        } else {

          predicate = SpecificationUtils.applyStringFilter(filtri.getCodiceTipoFirma(),
              joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipoContenutoFirmato)
              .get(CosmoDTipoContenutoFirmato_.codice),
              predicate, cb);
          predicate = cb.and(predicate,
              cb.isNull(joinDocumentoContenuti.get(CosmoTEntity_.dtCancellazione)));

        }


      }

      if (StringUtils.isBlank(sort)) {
        return cq.where(predicate).distinct(true).orderBy(cb.asc(root.get(CosmoTDocumento_.id)))
            .getRestriction();
      } else {
        List<Order> ordine = getOrder(sort, root, joinDocumentoContenuto, cb);
        return cq.where(predicate).distinct(true).orderBy(ordine).getRestriction();
      }
    };
  }

  private static List<Order> getOrder(String sort, Root<CosmoTDocumento> root,
      ListJoin<CosmoTDocumento, CosmoTContenutoDocumento> joinDocumentoContenuto,
      CriteriaBuilder cb) {
    String[] sortSplit = sort.trim().split(Constants.SPLIT_REGEX);

    List<Order> ordine = new LinkedList<>();
    for (String sortSplitSingle : sortSplit) {

      String[] sortFieldSplit = sortSplitSingle.trim().split(" ");
      boolean sortDirection = sortFieldSplit[1].equals("ASC");

      if (sortFieldSplit[0].equalsIgnoreCase(Constants.SORT.TITOLO_NOME_FILE)) {

        if (sortDirection) {
          ordine.add(cb.asc(root.get(CosmoTDocumento_.titolo)));
          if (joinDocumentoContenuto != null) {
            ordine.add(cb.asc(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.nomeFile)));
          }
        } else {
          ordine.add(cb.desc(root.get(CosmoTDocumento_.titolo)));
          if (joinDocumentoContenuto != null) {
            ordine.add(cb.asc(joinDocumentoContenuto.get(CosmoTContenutoDocumento_.nomeFile)));
          }
        }
      }

      if (sortFieldSplit[0].equalsIgnoreCase(Constants.SORT.ULTIMA_MODIFICA)) {

        if (sortDirection) {
          ordine.add(cb.asc(root.get(CosmoTEntity_.dtUltimaModifica)));
        } else {
          ordine.add(cb.desc(root.get(CosmoTEntity_.dtUltimaModifica)));
        }
      }
    }
    return ordine;
  }

  static Specification<CosmoTDocumento> findByStatoSmistamento(String codiceStatoSmistamento,
      Long idPratica) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      ListJoin<CosmoTDocumento, CosmoRSmistamentoDocumento> joinDocumentoSmistamenti = null;
      Join<CosmoRSmistamentoDocumento, CosmoDStatoSmistamento> joinStatoSmistamento = null;
      joinDocumentoSmistamenti =
          root.join(CosmoTDocumento_.cosmoRSmistamentoDocumentos, JoinType.INNER);
      joinStatoSmistamento = joinDocumentoSmistamenti
          .join(CosmoRSmistamentoDocumento_.cosmoDStatoSmistamento, JoinType.INNER);

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      Predicate predicate = cb.or(cb.isNull(root.get(CosmoTEntity_.dtInserimento.getName())),
          cb.greaterThan(root.get(CosmoTEntity_.dtInserimento.getName()), now),
          cb.and(cb.isNotNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
              cb.lessThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now)));

      predicate = cb.and(predicate, cb
          .equal(joinStatoSmistamento.get(CosmoDStatoSmistamento_.codice), codiceStatoSmistamento));

      if (null != idPratica) {
        predicate = cb.and(predicate,
            cb.equal(root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), idPratica));
      }

      return cq.where(predicate).getRestriction();
    };
  }

  /**
   * Recupero dei documenti da smistare
   *
   * @param idPratica
   * @param principaliOAllegati 1 = SOLO_PRINCIPALI, 2 = SOLO_ALLEGATI, 3 = PRINCIPALI_E_ALLEGATI
   * @param maxNumRetry
   * @return
   */
  static Specification<CosmoTDocumento> findDaSmistare(Long idPratica, int principaliOAllegati,
      Integer maxNumRetry) {
    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      ListJoin<CosmoTDocumento, CosmoRSmistamentoDocumento> joinDocumentoSmistamenti = null;
      Join<CosmoRSmistamentoDocumento, CosmoDStatoSmistamento> joinStatoSmistamento = null;
      joinDocumentoSmistamenti =
          root.join(CosmoTDocumento_.cosmoRSmistamentoDocumentos, JoinType.INNER);
      joinStatoSmistamento = joinDocumentoSmistamenti
          .join(CosmoRSmistamentoDocumento_.cosmoDStatoSmistamento, JoinType.INNER);

      Predicate predicate = cb.or(cb.isNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
          cb.greaterThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now));

      switch (principaliOAllegati) {
        case Constants.SOLO_PRINCIPALI:
          predicate = cb.and(predicate, cb.isNull(root.get(CosmoTDocumento_.documentoPadre)));
          break;
        case Constants.SOLO_ALLEGATI:
          predicate = cb.and(predicate, cb.isNotNull(root.get(CosmoTDocumento_.documentoPadre)));
          break;
        case Constants.PRINCIPALI_E_ALLEGATI:
          break;
        default:
          String error = "Indicazione su allegati o principali non trovata";
          throw new InternalServerException(error);
      }


      if (null != idPratica) {
        predicate = cb.and(predicate,
            cb.equal(root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), idPratica));
      }

      if (null != maxNumRetry) {

        List<String> codiciSmistamento = new ArrayList<>();
        // codiciSmistamento.add(StatoSmistamento.ERR_CALLBACK.getCodice());
        codiciSmistamento.add(StatoSmistamento.ERR_SMISTAMENTO.getCodice());


        predicate = cb.and(predicate,
            cb.or(
                cb.and(
                    cb.equal(joinStatoSmistamento.get(CosmoDStatoSmistamento_.codice),
                        StatoSmistamento.DA_SMISTARE.getCodice()),
                    cb.or(
                        cb.isNull(
                            joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry)),
                        cb.lessThanOrEqualTo(
                            joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry),
                            maxNumRetry))),
                cb.and(
                    joinStatoSmistamento.get(CosmoDStatoSmistamento_.codice).in(codiciSmistamento),
                    cb.or(
                        cb.isNull(
                            joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry)),
                        cb.lessThan(
                            joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry),
                            maxNumRetry))))


            // cb.or(cb.isNull(joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry)),
            // cb.lessThan(joinDocumentoSmistamenti.get(CosmoRSmistamentoDocumento_.numeroRetry),
            // maxNumRetry))
            );
      } else {

        In<String> inClause = cb.in(joinStatoSmistamento.get(CosmoDStatoSmistamento_.codice));
        List<String> codiciSmistamento = new ArrayList<>();
        codiciSmistamento.add(StatoSmistamento.DA_SMISTARE.getCodice());
        // codiciSmistamento.add(StatoSmistamento.ERR_CALLBACK.getCodice());
        codiciSmistamento.add(StatoSmistamento.ERR_SMISTAMENTO.getCodice());
        codiciSmistamento.forEach(inClause::value);
        predicate = cb.and(predicate, inClause);

      }

      // verifica sforamento numero massimo di retry

      return cq.where(predicate).getRestriction();
    };
  }

  static Specification<CosmoTDocumento> findDocumentiDaFirmare(FiltroRicercaDocumentiDTO filtri,
      DocumentiDaFirmareDTO filtriDaFirmare) {

    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      ListJoin<CosmoTDocumento, CosmoTContenutoDocumento> joinDocumentoContenuti =
          root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);

      Join<CosmoTContenutoDocumento, CosmoDFormatoFile> joinContenutoDocFormatoFile = null;

      Predicate predicate = cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(root.get(CosmoTEntity_.utenteCancellazione)),
          cb.isTrue(root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.firmabile)),
          cb.isNull(joinDocumentoContenuti.get(CosmoTEntity_.dtCancellazione)));

      if (filtriDaFirmare != null && Boolean.FALSE.equals(filtriDaFirmare.getTutti())
          && !filtriDaFirmare.getTipologieDocumenti().isEmpty()) {
        predicate = cb.and(predicate, root.get(CosmoTDocumento_.tipo)
            .get(CosmoDTipoDocumento_.codice).in(filtriDaFirmare.getTipologieDocumenti()));
      }

      if (filtri != null && filtri.getIdPratica() != null) {
        predicate = SpecificationUtils.applyNumericFilter(filtri.getIdPratica(),
            root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), predicate, cb);
      }

      if (filtri != null && filtri.getIdParent() != null) {
        predicate = SpecificationUtils.applyNumericFilter(filtri.getIdParent(),
            root.get(CosmoTDocumento_.documentoPadre).get(CosmoTDocumento_.id), predicate, cb);
      }

      if (filtri != null && filtri.getTipo() != null) {
        predicate = SpecificationUtils.applyStringFilter(filtri.getTipo(),
            root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.codice), predicate, cb);
      }

      if (filtri != null && filtri.getTitoloNomeFile() != null && filtri.getTitoloNomeFile()
          .containsKey(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())) {
        joinDocumentoContenuti.on(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.FIRMATO.toString()));

        Predicate nomeFilePredicate =
            cb.or(
                cb.like(cb.lower(root.get(CosmoTDocumento_.titolo)),
                    "%" + ((String) filtri.getTitoloNomeFile()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase() + "%"),
                cb.like(cb.lower(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.nomeFile)),
                    "%" + ((String) filtri.getTitoloNomeFile()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase()
                    + "%"));
        predicate = cb.and(predicate, nomeFilePredicate);
      }

      if (filtri != null && filtri.getFormato() != null
          && filtri.getFormato().containsKey(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())) {
        joinDocumentoContenuti = root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);
        joinDocumentoContenuti.on(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.FIRMATO.toString()));
        joinContenutoDocFormatoFile =
            joinDocumentoContenuti.join(CosmoTContenutoDocumento_.formatoFile, JoinType.INNER);

        Predicate contenutoFormatoPredicate =
            cb.or(
                cb.like(cb.lower(joinContenutoDocFormatoFile.get(CosmoDFormatoFile_.descrizione)),
                    "%" + ((String) filtri.getFormato()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase() + "%"),
                cb.like(cb.lower(joinContenutoDocFormatoFile.get(CosmoDFormatoFile_.mimeType)),
                    "%" + ((String) filtri.getFormato()
                        .get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase()
                    + "%"));



        // root.fetch(CosmoTDocumento_.documentiFigli, JoinType.INNER);

        predicate = cb.and(predicate, contenutoFormatoPredicate);

      }

      if (filtri != null && filtri.getCodiceTipoFirma() != null) {

        joinDocumentoContenuti = root.join(CosmoTDocumento_.contenuti, JoinType.INNER);
        joinDocumentoContenuti.on(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipo)
            .get(CosmoDTipoContenutoDocumento_.codice)
            .in(TipoContenutoDocumento.ORIGINALE.toString(),
                TipoContenutoDocumento.FIRMATO.toString()));

        if (filtri.getCodiceTipoFirma().containsKey(FilterCriteria.NOT_IN.getCodice())) {

          var subquery = cq.subquery(Long.class);
          var subroot = subquery.from(CosmoTContenutoDocumento.class);
          subquery.select(
              subroot.get(CosmoTContenutoDocumento_.documentoPadre).get(CosmoTDocumento_.id));
          var subpredicate = cb.and(
              subroot.get(CosmoTContenutoDocumento_.tipoContenutoFirmato)
              .get(CosmoDTipoContenutoFirmato_.codice)
              .in(
                      filtri.getCodiceTipoFirma().get(FilterCriteria.NOT_IN.getCodice())));
          subquery.where(subpredicate);

          predicate = cb.and(predicate,
              cb.not(cb.in(joinDocumentoContenuti.get(CosmoTContenutoDocumento_.documentoPadre)
                  .get(CosmoTDocumento_.id))
                  .value(subquery)));

        } else {

          predicate = SpecificationUtils.applyStringFilter(filtri.getCodiceTipoFirma(),
              joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipoContenutoFirmato)
              .get(CosmoDTipoContenutoFirmato_.codice),
              predicate, cb);
          predicate = cb.and(predicate,
              cb.isNull(joinDocumentoContenuti.get(CosmoTEntity_.dtCancellazione)));

        }


      }


      return cq.where(predicate).distinct(true).orderBy(cb.asc(root.get(CosmoTDocumento_.id)))
          .getRestriction();
    };
  }

  /**
   * Recupero dei documenti su cui apporre il sigillo elettronico
   *
   * @return
   */
  static Specification<CosmoTDocumento> findDaSigillare() {
    return (Root<CosmoTDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      Timestamp now = Timestamp.valueOf(LocalDateTime.now());

      ListJoin<CosmoTDocumento, CosmoRSigilloDocumento> joinDocumentoSigilli = null;
      Join<CosmoRSigilloDocumento, CosmoDStatoSigilloElettronico> joinStatoSigillo = null;
      joinDocumentoSigilli = root.join(CosmoTDocumento_.cosmoRSigilloDocumentos, JoinType.INNER);
      joinStatoSigillo = joinDocumentoSigilli
          .join(CosmoRSigilloDocumento_.cosmoDStatoSigilloElettronico, JoinType.INNER);

      Predicate predicate = cb.or(cb.isNull(root.get(CosmoTEntity_.dtCancellazione.getName())),
          cb.greaterThan(root.get(CosmoTEntity_.dtCancellazione.getName()), now));


      In<String> inClause = cb.in(joinStatoSigillo.get(CosmoDStatoSigilloElettronico_.codice));
      List<String> codiciSigillo = new ArrayList<>();
      codiciSigillo.add(StatoSigilloElettronico.DA_SIGILLARE.getCodice());
      codiciSigillo.forEach(inClause::value);
      predicate = cb.and(predicate, inClause);

      return cq.where(predicate).getRestriction();
    };
  }

}
