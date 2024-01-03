/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository.specifications;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.DoubleFilter;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDStatoPratica_;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione_;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaPratica;
import it.csi.cosmo.common.entities.CosmoRPraticaPratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTag_;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.CosmoTVariabile;
import it.csi.cosmo.common.entities.CosmoTVariabile_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.SpecificationUtils;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheFruitoriDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.GroupsCriteria;
import it.csi.cosmo.cosmopratiche.dto.rest.GroupsDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.TipologiaStatiPraticaDaAssociareDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.ValoreVariabile;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileProcessoDTO;
import it.csi.cosmo.cosmopratiche.security.UseCase;

/**
 *
 */

public interface CosmoTPraticaSpecifications {

  static Specification<CosmoTPratica> findByChiaveFruitoreEsterno(String idPratica,
      String codiceIpaEnte, Long idFruitore) {

    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      //@formatter:off
      Predicate predicate = cb.and(
          cb.equal(root.get(CosmoTPratica_.idPraticaExt), idPratica),
          cb.equal(root.get(CosmoTPratica_.fruitore).get(CosmoTFruitore_.id), idFruitore),
          cb.equal(root.get(CosmoTPratica_.ente).get(CosmoTEnte_.codiceIpa), codiceIpaEnte));
      //@formatter:on

      return cq.where(predicate).getRestriction();
    };
  }

  static Specification<CosmoTAttivita> findAttivitaPossibiliByFilters(
      FiltroRicercaPraticheDTO filtri, UserInfoDTO userInfo, Long idEnte) {

    return (Root<CosmoTAttivita> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {

      QueryContext<CosmoTAttivita> ctx =
          buildContext(root, cq, cb, idEnte, userInfo, filtri, new LinkedList<>(), null);

      Subquery<Long> subquery = cq.subquery(Long.class);
      Root<CosmoTPratica> rootSubquery = subquery.from(CosmoTPratica.class);
      var subQueryContext = subqueryContext(ctx, rootSubquery);

      //@formatter:off
      subquery.select(
          rootSubquery.get(CosmoTPratica_.id)
          )
      .where(
          buildPredicateRicercaPratiche(subQueryContext, null)
          );
      //@formatter:on

      var joinPratica = root.join(CosmoTAttivita_.cosmoTPratica);

      //@formatter:off
      return cb.and(
          buildPredicateEntitaValida(ctx, root),
          buildPredicateEntitaValida(ctx, joinPratica),
          buildPredicateAttivitaEseguibileMassivamenteDaUtenteCorrente(ctx, null),
          cb.in(joinPratica.get(CosmoTPratica_.id)).value(subquery)
          );
      //@formatter:on
    };
  }

  private static Predicate buildPredicateRicercaPratiche(QueryContext<CosmoTPratica> ctx,
      String sort) {

    return ctx.builder.and(
        // filtro per ente
        buildPredicateAppartenenzaEnte(ctx),

        // condizioni di visibilita' generiche
        buildPredicatePraticaVisibileAdUtente(ctx),

        // filtri custom (da frontend)
        buildPredicateFiltriUtente(ctx),

        // gruppi richiesti
        buildPredicateGruppi(ctx),

        // condizione per solo le pratiche associabili
        buildPredicatePraticheAssociabili(ctx),

        // condizione per la ricerca attivita' eseguibili massivamente
        buildPredicateContieneAttivitaEseguibiliMassivamenteDaUtenteCorrente(ctx),

        buildPredicatePerVariabiliProcesso(ctx, sort));
  }


  static Specification<CosmoTPratica> findByFilters(FiltroRicercaPraticheDTO filtri,
      UserInfoDTO userInfo, Long idEnte, String sort) {

    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      QueryContext<CosmoTPratica> ctx =
          buildContext(root, cq, cb, idEnte, userInfo, filtri, new LinkedList<>(), null);

      //@formatter:off
      Predicate predicate = buildPredicateRicercaPratiche(ctx, sort);

      /*
       * Join fetch should be applied only for query to fetch the "data", not for "count" query to
       * do pagination. Handled this by checking the criteriaQuery.getResultType(), if it's long
       * that means query is for count so not appending join fetch else append it.
       */
      if (Long.class != cq.getResultType() && long.class != cq.getResultType()) {
        root.fetch(CosmoTPratica_.stato, JoinType.LEFT);
        root.fetch(CosmoTPratica_.tipo, JoinType.LEFT);
        root.fetch(CosmoTPratica_.ente, JoinType.LEFT);
        root.fetch(CosmoTPratica_.fruitore, JoinType.LEFT);

        /*
         * la linea seguente e' stata commentata perche' sembrava una buona idea e invece era una
         * pessima idea
         *
         * applicare una fetch paginata E la fetch di una children collection non e' possibile a
         * livello di query, di conseguenza hibernate carica in memoria TUTTO IL DATASET risultante
         * dalla join e poi applica la paginazione in-memory.
         *
         * se ci sono 500 pratiche e ognuna ha 5 attivita' vengono caricati in memoria e
         * ordinati/paginati 2500 record
         *
         * per riferimento futuro, se vedete nella console il messaggio
         * "HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!"
         * vuol dire che hibernate sta facendo questa cosa orrenda
         *
         * per riferimento ancora piu' futuro, col passaggio ad hibernate >= 5.2.3 e' disponibile
         * una opzione di configurazione per lanciare eccezione al posto di warning in questo caso
         */
        // root.fetch(CosmoTPratica_.attivita, JoinType.LEFT);
      }

      return cq.where(predicate).orderBy(getOrder(ctx, sort)).getRestriction();
    };
  }

  static Specification<CosmoTPratica> findByCondizioniDiVisibilita(Long idPratica, Long idEnte,
      UserInfoDTO userInfo) {

    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      QueryContext<CosmoTPratica> ctx = buildContext(root, cq, cb, idEnte, userInfo, null,new LinkedList<>(),null);

      //@formatter:off
      return cb.and(
          cb.equal(root.get(CosmoTPratica_.id), idPratica),
          buildPredicateAppartenenzaEnte(ctx),
          buildPredicatePraticaVisibileAdUtente(ctx)
          );
      //@formatter:on
    };
  }

  static Specification<CosmoTPratica> findByCondizioniDiVisibilitaSuAttivita(Long idAttivita,
      Long idEnte, UserInfoDTO userInfo) {

    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      QueryContext<CosmoTPratica> ctx =
          buildContext(root, cq, cb, idEnte, userInfo, null, new LinkedList<>(),null);

      //@formatter:off
      return cb.and(
          buildPredicateAppartenenzaEnte(ctx),
          buildPredicateContieneSpecificaAttivitaVisibileAdUtente(ctx, idAttivita)
          );
      //@formatter:on
    };
  }

  private static Predicate buildPredicateTutteConAvanzamento(QueryContext<CosmoTPratica> ctx,
      Map<String, String> tutte) {

    //@formatter:off
    return buildPredicateAvanzamentoLavorazione(ctx, tutte);
    //@formatter:on
  }

  private static Predicate buildPredicatePreferiteConAvanzamento(QueryContext<CosmoTPratica> ctx,
      Map<String, String> preferite) {

    //@formatter:off
    return ctx.builder.and(
        buildPredicatePreferita(ctx),
        buildPredicateAvanzamentoLavorazione(ctx, preferite)
        );
    //@formatter:on
  }

  private static Predicate buildPredicateDaLavorareConAvanzamento(QueryContext<CosmoTPratica> ctx,
      Map<String, String> daLavorare) {

    //@formatter:off
    return ctx.builder.and(
        buildPredicateDaLavorare(ctx),
        buildPredicateAvanzamentoLavorazione(ctx, daLavorare)
        );
    //@formatter:on
  }

  private static Predicate buildPredicateInEvidenzaConAvanzamento(QueryContext<CosmoTPratica> ctx,
      Map<String, String> inEvidenza) {

    //@formatter:off
    return ctx.builder.and(
        buildPredicateInEvidenza(ctx, true),
        buildPredicateAvanzamentoLavorazione(ctx, inEvidenza)
        );
    //@formatter:on
  }

  /*
   * costruisce il filtro per appartenenza all'ente corrente
   *
   * condizione di visibilita' A - ente: la pratica deve essere gestita dall'ente selezionato
   * durante l'accesso (ente associato al profilo utente);
   */
  private static Predicate buildPredicateAppartenenzaEnte(QueryContext<CosmoTPratica> ctx) {
    if (ctx.idEnte != null) {
      Join<CosmoTPratica, CosmoTEnte> joinEnte = ctx.root.join(CosmoTPratica_.ente, JoinType.LEFT);

      return ctx.builder.equal(joinEnte.get(CosmoTEnte_.id), ctx.idEnte);
    }

    return ctx.builder.conjunction();
  }

  /*
   * costruisce il filtro di visibilita' di una attivita per un utente
   *
   * una attivita' e' visibile ad un utente se (in OR):
   *
   * - e' amministratore
   *
   * - e' attualmente assegnata all'utente
   *
   * - e' attualmente assegnata ad un gruppo di cui l'utente fa parte
   */
  private static Predicate buildPredicateContieneSpecificaAttivitaVisibileAdUtente(
      QueryContext<CosmoTPratica> ctx, Long idAttivita) {

    boolean isAdmin =
        (ctx.utenteCorrente != null && ctx.utenteCorrente.hasAuthority(UseCase.ADMIN_PRAT_CODE));

    var root = ctx.root;
    var cq = ctx.query;
    var cb = ctx.builder;

    Join<CosmoTPratica, CosmoTAttivita> joinAttivita =
        root.join(CosmoTPratica_.attivita, JoinType.INNER);
    Join<CosmoTPratica, CosmoTEnte> joinPraticaEnte = root.join(CosmoTPratica_.ente, JoinType.LEFT);

    Subquery<CosmoTAttivita> subquery = null;

    if (!isAdmin) {
      subquery = cq.subquery(CosmoTAttivita.class);
      Root<CosmoTAttivita> rootSubquery = subquery.from(CosmoTAttivita.class);
      subquery.select(rootSubquery).where(
          cb.equal(rootSubquery.get(CosmoTAttivita_.id), idAttivita),
          cb.or(
              buildPredicateAttivitaAssegnataUtenteCorrente(subqueryContext(ctx, rootSubquery),
                  true),
              buildPredicateAttivitaAssegnataGruppoUtente(subqueryContext(ctx, rootSubquery),
                  true)));
    }

    return cb.and(cb.equal(joinAttivita.get(CosmoTAttivita_.id), idAttivita),
        ctx.idEnte != null ? cb.equal(joinPraticaEnte.get(CosmoTEnte_.id), ctx.idEnte)
            : cb.isTrue(cb.literal(true)),
            isAdmin ? cb.conjunction() : cb.exists(subquery));
  }

  /*
   * costruisce il filtro principale di visibilita' di una pratica ad un utente
   *
   * una pratica e' visibile ad un utente se (in OR):
   *
   * - l'utente a' amministratore (ha il cdu ADMIN_PRAT)
   *
   * - e' stata creata da quell'utente
   *
   * - contiene una attivita' che e' oppure e' stata assegnata direttamente all'utente
   *
   * - contiene una attivita' che e' oppure e' stata assegnata ad un gruppo a cui l'utente
   * appartiene (al momento?)
   *
   * - e' stata condivisa ("in evidenza") con l'utente, anche in passato
   *
   * - l'utente e' legato ad un gruppo, il quale e' supervisore per la tipologia della pratica
   * (buildPredicateMonitoraggio)
   *
   *
   */
  private static Predicate buildPredicatePraticaVisibileAdUtente(QueryContext<CosmoTPratica> ctx) {

    if (ctx.utenteCorrente != null && !isAnOperation(ctx)
        && ctx.utenteCorrente.hasAuthority(UseCase.ADMIN_PRAT_CODE)) {

      return ctx.builder.isTrue(ctx.builder.literal(true));
    }

    //@formatter:off
    return ctx.builder.or(
        buildPredicatePraticaCreataDaUtenteCorrente(ctx),
        buildPredicateContieneAttivitaAssegnateGruppoUtente(ctx, false),
        buildPredicateContieneAttivitaAssegnateUtenteCorrente(ctx, false),
        buildPredicateInEvidenza(ctx, true),
        buildPredicateMonitoraggio(ctx, true)
        );
    //@formatter:on
  }

  private static boolean isAnOperation(QueryContext<CosmoTPratica> ctx) {

    if (ctx.filtri != null && Boolean.TRUE.equals(ctx.filtri.isTuttePratiche())
        && ctx.utenteCorrente.hasAuthority(UseCase.ADMIN_PRAT_CODE)) {

      return false;
    }

    return ctx.filtri != null && ctx.filtri.getDaAssociareA() != null
        && ctx.filtri.getDaAssociareA().getEquals() != null;

  }

  /*
   * costruisce il filtro con i gruppi di appartenenza e gli stati di avanzamento
   */
  private static Predicate buildPredicateGruppi(QueryContext<CosmoTPratica> ctx) {
    GroupsDTO gruppi = ctx.filtri != null ? ctx.filtri.getGroups() : null;

    Predicate predicate = ctx.builder.conjunction();

    // filtra per gruppi se specificato
    if (gruppi != null) {
      List<Predicate> predicatesGruppi = new ArrayList<>();

      if (gruppi.getTutte() != null) {
        predicatesGruppi.add(buildPredicateTutteConAvanzamento(ctx, gruppi.getTutte()));
      }
      if (gruppi.getPreferite() != null) {
        predicatesGruppi.add(buildPredicatePreferiteConAvanzamento(ctx, gruppi.getPreferite()));
      }
      if (gruppi.getInEvidenza() != null) {
        predicatesGruppi.add(buildPredicateInEvidenzaConAvanzamento(ctx, gruppi.getInEvidenza()));
      }
      if (gruppi.getDaLavorare() != null) {
        predicatesGruppi.add(buildPredicateDaLavorareConAvanzamento(ctx, gruppi.getDaLavorare()));
      }
      if (!predicatesGruppi.isEmpty()) {
        predicate =
            ctx.builder.and(predicate, ctx.builder.or(predicatesGruppi.toArray(new Predicate[0])));
      }
    }

    return predicate;
  }

  /*
   * costruisce il filtro con i parametri di input
   */
  private static Predicate buildPredicateFiltriUtente(QueryContext<CosmoTPratica> ctx) {

    var cb = ctx.builder;
    var root = ctx.root;
    var filtri = ctx.filtri;

    Predicate predicate = cb.conjunction();

    if (filtri == null) {
      return predicate;
    }

    // join con tipo pratica, ente, rStatoTipo e rStatoTipo - stato


    Join<CosmoTPratica, CosmoDStatoPratica> joinStato =
        root.join(CosmoTPratica_.stato, JoinType.LEFT);

    Join<CosmoTPratica, CosmoDTipoPratica> joinTipo = root.join(CosmoTPratica_.tipo, JoinType.LEFT);

    if (filtri.getId() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getId(), root.get(CosmoTPratica_.id),
          predicate, cb);
    }

    // applica filtri utente (tipologia, stato, oggetto)
    if (filtri.getTipologia() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getTipologia(),
          joinTipo.get(CosmoDTipoPratica_.codice), predicate, cb);
    }
    if (filtri.getStato() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getStato(),
          joinStato.get(CosmoDStatoPratica_.codice), predicate, cb);
    }

    if (filtri.getTipologia() == null && filtri.getStato() == null
        && filtri.getDaAssociareA() != null && filtri.getTipologieStatiDaAssociare() != null
        && !filtri.getTipologieStatiDaAssociare().isEmpty()) {

      predicate = cb.and(predicate, getPredicatePerTipologieStatiDaAssociare(cb,
          filtri.getTipologieStatiDaAssociare(), joinTipo, joinStato));
    }

    if (filtri.getTipologia() != null && filtri.getStato() == null
        && filtri.getDaAssociareA() != null && filtri.getTipologieStatiDaAssociare() != null
        && !filtri.getTipologieStatiDaAssociare().isEmpty()) {

      Predicate predicateStati = getPredicatePerStatiDaAssociare(filtri.getTipologia(),
          filtri.getTipologieStatiDaAssociare(), joinStato);

      if (predicateStati != null) {
        predicate = cb.and(predicate, predicateStati);
      }
    }

    // nel filtro per oggetto faccio split delle parole
    if (filtri.getOggetto() != null) {
      if (!StringUtils.isBlank(filtri.getOggetto().getContainsIgnoreCase())) {
        filtri.getOggetto().setContainsIgnoreCase(
            filtri.getOggetto().getContainsIgnoreCase().replaceAll("\\s+", "%"));
      }
      if (!StringUtils.isBlank(filtri.getOggetto().getContains())) {
        filtri.getOggetto().setContains(filtri.getOggetto().getContains().replaceAll("\\s+", "%"));
      }
      predicate = SpecificationUtils.applyFilter(filtri.getOggetto(),
          root.get(CosmoTPratica_.oggetto), predicate, cb);
    }

    // nel filtro per riassunto faccio split delle parole
    if (filtri.getRiassunto() != null) {
      if (!StringUtils.isBlank(filtri.getRiassunto().getContainsIgnoreCase())) {
        filtri.getRiassunto().setContainsIgnoreCase(
            filtri.getRiassunto().getContainsIgnoreCase().replaceAll("\\s+", "%"));
      }
      if (!StringUtils.isBlank(filtri.getRiassunto().getContains())) {
        filtri.getRiassunto()
        .setContains(filtri.getRiassunto().getContains().replaceAll("\\s+", "%"));
      }
      predicate = SpecificationUtils.applyFilter(filtri.getRiassunto(),
          root.get(CosmoTPratica_.riassuntoTestuale), predicate, cb);
    }

    // applica filtri date (con range opportuni)
    if (filtri.getDataAperturaPratica() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDataAperturaPratica(),
          root.get(CosmoTEntity_.dtInserimento), predicate, cb);
    }
    if (filtri.getDataUltimaModifica() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDataUltimaModifica(),
          root.get(CosmoTEntity_.dtUltimaModifica), predicate, cb);
    }
    if (filtri.getDataUltimoCambioStato() != null) {
      predicate = SpecificationUtils.applyFilter(filtri.getDataUltimoCambioStato(),
          root.get(CosmoTPratica_.dataCambioStato), predicate, cb);
    }

    // applico filtri legacy (verranno dismessi)
    applyLegacyFilters(ctx, predicate);

    return predicate;
  }



  private static void applyLegacyFilters(QueryContext<CosmoTPratica> ctx, Predicate predicate) {
    var cb = ctx.builder;
    var root = ctx.root;
    var filtri = ctx.filtri;

    if (filtri != null) {
      if (filtri.getDataAperturaPraticaDa() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataAperturaPraticaDa(),
            root.get(CosmoTEntity_.dtInserimento), predicate, cb);
      }
      if (filtri.getDataAperturaPraticaA() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataAperturaPraticaA(),
            root.get(CosmoTEntity_.dtInserimento), predicate, cb);
      }
      if (filtri.getDataUltimaModificaDa() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataUltimaModificaDa(),
            root.get(CosmoTEntity_.dtUltimaModifica), predicate, cb);
      }
      if (filtri.getDataUltimaModificaA() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataUltimaModificaA(),
            root.get(CosmoTEntity_.dtUltimaModifica), predicate, cb);
      }
      if (filtri.getDataUltimoCambioStatoDa() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataUltimoCambioStatoDa(),
            root.get(CosmoTPratica_.dataCambioStato), predicate, cb);
      }
      if (filtri.getDataUltimoCambioStatoA() != null) {
        predicate = SpecificationUtils.applyFilter(filtri.getDataUltimoCambioStatoA(),
            root.get(CosmoTPratica_.dataCambioStato), predicate, cb);
      }
    }
  }

  /*
   * costruisce il filtro per la categoria 'PREFERITE'
   *
   * deve esserci una relazione VALIDA in cosmo_r_pratica_utente con codice =
   * Constants.PRATICHE.PREFERITA per l'utente corrente
   *
   * condizione di visibilita' C - preferenza: l'utente ha inserito la pratica tra le sue preferite
   * (per l'id_utente esiste un record di cosmo_r_pratica_utente con tipo_rel = 'preferita');
   */
  private static Predicate buildPredicatePreferita(QueryContext<CosmoTPratica> ctx) {
    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    //@formatter:off
    Subquery<CosmoRPraticaUtenteGruppo> subquery = ctx.query.subquery(CosmoRPraticaUtenteGruppo.class);
    Root<CosmoRPraticaUtenteGruppo> subqueryRoot = subquery.from(CosmoRPraticaUtenteGruppo.class);

    subquery
    .select(subqueryRoot)
    .where(
        ctx.builder.equal(
            subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id),
            ctx.utenteCorrente.getId()
            ),
        ctx.builder.equal(
            subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
            ctx.root.get(CosmoTPratica_.id)
            ),
        ctx.builder.equal(
            subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice),
            Constants.PRATICHE.PREFERITA
            ),
        buildPredicateRelazioneValida(ctx, subqueryRoot)
        );
    //@formatter:on

    return ctx.builder.exists(subquery);
  }

  /*
   * costruisce il filtro per la categoria 'IN EVIDENZA'
   *
   * deve esserci una relazione VALIDA in cosmo_r_pratica_utente con codice =
   * Constants.PRATICHE.CONDIVISA per l'utente corrente
   *
   * con 'validaOra' = true corrisponde alla condizione di visibilita' D - evidenza: la pratica e'
   * stata posta in evidenza per l'utente (per l'id_utente esiste un record VALIDO di
   * cosmo_r_pratica_utente con tipo_rel = 'condivisa' e dt_fine_val NULLA);
   */
  private static Predicate buildPredicateInEvidenza(QueryContext<CosmoTPratica> ctx,
      boolean validaOra) {
    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    var idsGruppi =
        ctx.utenteCorrente.getGruppi().stream().map(GruppoDTO::getId).collect(Collectors.toList());

    //@formatter:off
    Subquery<CosmoRPraticaUtenteGruppo> subquery = ctx.query.subquery(CosmoRPraticaUtenteGruppo.class);
    Root<CosmoRPraticaUtenteGruppo> subqueryRoot = subquery.from(CosmoRPraticaUtenteGruppo.class);


    Predicate utentiOGruppi = null;
    if(idsGruppi.isEmpty()) {
      utentiOGruppi = ctx.builder.equal(
          subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id),
          ctx.utenteCorrente.getId()
          );
    }else {
      utentiOGruppi =  ctx.builder.or(
          ctx.builder.equal(
              subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.id),
              ctx.utenteCorrente.getId()
              ),
          subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTGruppo).get(CosmoTGruppo_.id).in(idsGruppi)
          );
    }


    subquery
    .select(subqueryRoot)
    .where(
        utentiOGruppi,
        ctx.builder.equal(
            subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
            ctx.root.get(CosmoTPratica_.id)
            ),
        ctx.builder.equal(
            subqueryRoot.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica).get(CosmoDTipoCondivisionePratica_.codice),
            Constants.PRATICHE.CONDIVISA
            ),
        validaOra ?
            buildPredicateRelazioneValida(ctx, subqueryRoot) : ctx.builder.conjunction()
        );
    //@formatter:on

    return ctx.builder.exists(subquery);
  }

  /*
   * costruisce il filtro per la categoria 'DA LAVORARE'
   *
   * deve avere una attivita' assegnata direttamente all'utente oppure assegnata ad uno dei gruppi a
   * cui l'utente appartiene
   */
  private static Predicate buildPredicateDaLavorare(QueryContext<CosmoTPratica> ctx) {

    //@formatter:off
    return ctx.builder.or(
        buildPredicateContieneAttivitaAssegnateUtenteCorrente(ctx, true),
        buildPredicateContieneAttivitaAssegnateGruppoUtente(ctx, true)
        );
    //@formatter:on
  }

  /*
   * costruisce il filtro per assegnata ad uno dei gruppi a cui l'utente appartiene
   *
   * l'utente deve avere almeno un gruppo valido (con ID not null), deve esistere una attivita non
   * cancellata (dt_cancellazione null) con una assegnazione valida (dtFineVal nulla o nel futuro)
   * per cui rAttivitaAssegnazione.idGruppo in (id gruppi utente)
   *
   * con 'validaOra' = false corrisponde alla condizione di visibilita' B3 - gruppo assegnatario:
   * l'utente fa parte/ha fatto parte di un gruppo assegnatario della pratica (esistenza di un
   * record di cosmo_r_attivita_assegnazione, non considerare la condizione di validita'
   * dell'assegnazione e del task, per i gruppi di appartenenza dell'id_utente,
   * cosmo_t_utente_gruppo, non considerare la condizione di validita');
   *
   * con 'validaOra' = true corrisponde alla condizione di visibilita' E2 - gruppo assegnatario
   * corrente: l'utente appartiene a un gruppo assegnatario della pratica (esistenza di un record
   * valido di cosmo_r_attivita_assegnazione per i gruppi di appartenenza dell'id_utente,
   * cosmo_t_utente_gruppo VALIDI e cosmo_t_attivita VALIDO ovvero con dt_cancellazione NULLA).
   */
  private static Predicate buildPredicateContieneAttivitaAssegnateGruppoUtente(
      QueryContext<CosmoTPratica> ctx, boolean validaOra) {

    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    var root = ctx.root;
    var cb = ctx.builder;



    // subquery sulla tAttivita
    Subquery<CosmoTAttivita> subquery = ctx.query.subquery(CosmoTAttivita.class);
    Root<CosmoTAttivita> rootSubquery = subquery.from(CosmoTAttivita.class);

    // join attivita - pratica
    Join<CosmoTAttivita, CosmoTPratica> joinPratica =
        rootSubquery.join(CosmoTAttivita_.cosmoTPratica, JoinType.INNER);

    //@formatter:off
    subquery
    .select(rootSubquery)
    .where(
        cb.equal(
            joinPratica.get(CosmoTPratica_.id),
            root.get(CosmoTPratica_.id)
            ),
        buildPredicateAttivitaAssegnataGruppoUtente(subqueryContext(ctx, rootSubquery), validaOra),
        validaOra ?
            buildPredicateEntitaValida(ctx, rootSubquery) : cb.conjunction()
        );
    //@formatter:on
    return cb.exists(subquery);

  }

  /*
   * costruisce il filtro per assegnata ad uno dei gruppi a cui l'utente appartiene
   *
   * deve esistere una attivita con una assegnazione per cui rAttivitaAssegnazione.idGruppo
   * corrisponde ad un gruppo a cui l'utente e' stato assegnato
   */
  private static Predicate buildPredicateAttivitaAssegnataGruppoUtente(
      QueryContext<CosmoTAttivita> ctx, boolean validaOra) {

    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    var cb = ctx.builder;

    // join attivita - rAttivitaAssegnazione
    ListJoin<CosmoTAttivita, CosmoRAttivitaAssegnazione> joinAssegnazione =
        ctx.root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.LEFT);

    // join rAttivitaAssegnazione - tGruppo
    Join<CosmoRAttivitaAssegnazione, CosmoTGruppo> joinGruppo =
        joinAssegnazione.join(CosmoRAttivitaAssegnazione_.gruppo, JoinType.LEFT);

    // join tGruppo - rUtenteGruppo
    ListJoin<CosmoTGruppo, CosmoTUtenteGruppo> joinGruppoAssegnazioneUtentiGruppo =
        joinGruppo.join(CosmoTGruppo_.associazioniUtenti, JoinType.LEFT);

    if (ctx.filtri != null && Boolean.TRUE.equals(ctx.filtri.isTuttePratiche())
        && ctx.utenteCorrente.hasAuthority(UseCase.ADMIN_PRAT_CODE)) {
      //@formatter:off
      return cb.and(
          validaOra ?
              cb.and(
                  buildPredicateRelazioneValida(ctx, joinAssegnazione),
                  buildPredicateEntitaValida(ctx, joinGruppo),
                  buildPredicateEntitaValida(ctx, joinGruppoAssegnazioneUtentiGruppo)
                  ) : cb.conjunction()
          );
      //@formatter:on
    }


    // join rUtenteGruppo - tUtente
    Join<CosmoTUtenteGruppo, CosmoTUtente> joinAssegnazioneGruppoUtente =
        joinGruppoAssegnazioneUtentiGruppo.join(CosmoTUtenteGruppo_.utente, JoinType.LEFT);

    //@formatter:off
    return cb.and(
        cb.equal(
            joinAssegnazioneGruppoUtente.get(CosmoTUtente_.id),
            ctx.utenteCorrente.getId()
            ),
        validaOra ?
            cb.and(
                buildPredicateRelazioneValida(ctx, joinAssegnazione),
                buildPredicateEntitaValida(ctx, joinGruppo),
                buildPredicateEntitaValida(ctx, joinGruppoAssegnazioneUtentiGruppo)
                ) : cb.conjunction()
        );

    //@formatter:on
  }

  /*
   * costruisce il filtro per contiene attivita' assegnate ad utente corrente.
   *
   * deve contenere almeno una attivita' valida che sia correntemente assegnata all'utente corrente
   *
   * con 'validaOra' = false corrisponde alla condizione di visibilita' B2 - utente assegnatario:
   * l'utente e'/e' stato assegnatario di un task della pratica (esistenza per l'id_utente di un
   * record di cosmo_r_attivita_assegnazione con assegnatario = true, non considerare la condizione
   * di validita' dell'assegnazione e del task);
   *
   * con 'validaOra' = true corrisponde alla condizione di visibilita' E1 - utente assegnatario
   * corrente: l'utente e' assegnatario di un task della pratica (esistenza per l'id_utente di un
   * record VALIDO di cosmo_r_attivita_assegnazione con assegnatario = true e cosmo_t_attivita
   * VALIDO ovvero con dt_cancellazione NULLA);
   */
  private static Predicate buildPredicateContieneAttivitaAssegnateUtenteCorrente(
      QueryContext<CosmoTPratica> ctx, boolean validaOra) {
    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    var root = ctx.root;
    var cb = ctx.builder;

    Subquery<CosmoTAttivita> subquery = ctx.query.subquery(CosmoTAttivita.class);
    Root<CosmoTAttivita> rootSubquery = subquery.from(CosmoTAttivita.class);

    // join attivita - pratica
    Join<CosmoTAttivita, CosmoTPratica> joinPratica =
        rootSubquery.join(CosmoTAttivita_.cosmoTPratica, JoinType.INNER);

    //@formatter:off
    subquery
    .select(rootSubquery)
    .where(
        cb.equal(
            joinPratica.get(CosmoTPratica_.id),  root.get(CosmoTPratica_.id)
            ),
        validaOra ?
            buildPredicateEntitaValida(ctx, rootSubquery) : cb.conjunction(),
            buildPredicateAttivitaAssegnataUtenteCorrente(subqueryContext(ctx, rootSubquery), validaOra)
        );
    //@formatter:on

    return cb.exists(subquery);
  }

  /*
   * costruisce il filtro per attivita' assegnata ad utente corrente.
   *
   * deve avere dt_cancellazione NULL (quindi valida), essere assegnata direttamente all'utente
   * corrente con assegnatario = True e con dt_fine_val assegnazione NULL oppure successiva alla
   * data corrente
   */
  private static Predicate buildPredicateAttivitaAssegnataUtenteCorrente(
      QueryContext<CosmoTAttivita> ctx, boolean validaOra) {

    if (ctx.utenteCorrente == null || ctx.utenteCorrente.getId() == null) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    // join attivita - rAttivitaAssegnazione
    ListJoin<CosmoTAttivita, CosmoRAttivitaAssegnazione> subAttivitaAssegnazione =
        ctx.root.join(CosmoTAttivita_.cosmoRAttivitaAssegnaziones, JoinType.LEFT);

    if (ctx.filtri != null && Boolean.TRUE.equals(ctx.filtri.isTuttePratiche())
        && ctx.utenteCorrente.hasAuthority(UseCase.ADMIN_PRAT_CODE)) {
      //@formatter:off
      return ctx.builder.and(
          ctx.builder.isTrue(
              subAttivitaAssegnazione.get(CosmoRAttivitaAssegnazione_.assegnatario)
              ),
          validaOra ?
              buildPredicateRelazioneValida(ctx, subAttivitaAssegnazione) : ctx.builder.conjunction()
          );
      //@formatter:on
    }


    //@formatter:off
    return ctx.builder.and(
        ctx.builder.equal(
            subAttivitaAssegnazione.get(CosmoRAttivitaAssegnazione_.idUtente),
            ctx.utenteCorrente.getId()
            ),
        ctx.builder.isTrue(
            subAttivitaAssegnazione.get(CosmoRAttivitaAssegnazione_.assegnatario)
            ),
        validaOra ?
            buildPredicateRelazioneValida(ctx, subAttivitaAssegnazione) : ctx.builder.conjunction()
        );
    //@formatter:on
  }

  /*
   * costruisce il filtro per le pratiche create dall'utente corrente
   *
   * condizione di visibilita' B1 - creatore pratica: l'utente ha inserito la pratica
   * (cosmo_t_pratica.utente_inserimento utente_crezione_pratica);
   */
  private static Predicate buildPredicatePraticaCreataDaUtenteCorrente(
      QueryContext<CosmoTPratica> ctx) {
    if (ctx.utenteCorrente == null || StringUtils.isBlank(ctx.utenteCorrente.getCodiceFiscale())) {
      return ctx.builder.isTrue(ctx.builder.literal(false));
    }

    //@formatter:off
    return ctx.builder.equal(ctx.root.get(CosmoTPratica_.utenteCreazionePratica),
        ctx.utenteCorrente.getCodiceFiscale());

    //@formatter:on
  }

  /*
   * costruisce il filtro (in OR) per gli stati Conclusa, In corso, Annullata
   */
  private static Predicate buildPredicateAvanzamentoLavorazione(QueryContext<CosmoTPratica> ctx,
      Map<String, String> categoria) {

    Predicate predicate = ctx.builder.conjunction();

    List<Predicate> avanzamentoPredicates = new ArrayList<>();

    if (categoria != null) {
      if (Boolean.TRUE.toString()
          .equalsIgnoreCase(categoria.getOrDefault(GroupsCriteria.CONCLUSE.getCodice(), null))) {
        avanzamentoPredicates.add(buildPredicatePraticaConclusa(ctx));
      }

      if (Boolean.TRUE.toString()
          .equalsIgnoreCase(categoria.getOrDefault(GroupsCriteria.IN_CORSO.getCodice(), null))) {
        avanzamentoPredicates.add(buildPredicatePraticaInCorso(ctx));
      }

      if (Boolean.TRUE.toString()
          .equalsIgnoreCase(categoria.getOrDefault(GroupsCriteria.ANNULLATE.getCodice(), null))) {
        avanzamentoPredicates.add(buildPredicatePraticaAnnullata(ctx));
      }
    }

    if (!avanzamentoPredicates.isEmpty()) {
      //@formatter:off
      predicate = ctx.builder.and(
          predicate,
          ctx.builder.or(
              avanzamentoPredicates.toArray(new Predicate[0])
              )
          );
      //@formatter:on
    }

    return predicate;
  }

  /*
   * filtra per pratica CONCLUSA (dt_cancellazione NULL AND data_fine_pratica NOT NULL)
   */
  private static Predicate buildPredicatePraticaConclusa(QueryContext<CosmoTPratica> ctx) {
    //@formatter:off
    return ctx.builder.and(
        ctx.builder.isNull(ctx.root.get(CosmoTEntity_.dtCancellazione)),
        ctx.builder.isNotNull(ctx.root.get(CosmoTPratica_.dataFinePratica))
        );
    //@formatter:on
  }

  /*
   * filtra per pratica IN CORSO (dt_cancellazione NULL AND data_fine_pratica NULL)
   */
  private static Predicate buildPredicatePraticaInCorso(QueryContext<CosmoTPratica> ctx) {
    //@formatter:off
    return ctx.builder.and(
        ctx.builder.isNull(ctx.root.get(CosmoTEntity_.dtCancellazione)),
        ctx.builder.isNull(ctx.root.get(CosmoTPratica_.dataFinePratica))
        );
    //@formatter:on
  }

  /*
   * filtra per pratica ANNULLATA (dt_cancellazione NOT NULL)
   */
  private static Predicate buildPredicatePraticaAnnullata(QueryContext<CosmoTPratica> ctx) {
    //@formatter:off
    return ctx.builder.and(
        ctx.builder.isNotNull(ctx.root.get(CosmoTEntity_.dtCancellazione))
        );
    //@formatter:on
  }

  /*
   * condizione di validita' per una entity di classe.
   *
   * dt_cancellazione deve essere null
   *
   * da verificare, la dt_cancellazione non dovrebbe mai venire valorizzata con timestamp
   * successivi, quindi la condizione IS NOT NULL dovrebbe essere abbastanza anche da sola
   */
  private static <T extends CosmoTEntity> Predicate buildPredicateEntitaValida(QueryContext<?> ctx,
      Path<T> root) {
    return ctx.builder.isNull(root.get(CosmoTEntity_.dtCancellazione));
  }

  /*
   * condizione di validita' per una entity di relazione.
   *
   * dt_fine_val deve essere null oppure nel futuro
   *
   * da verificare, la dt_fine_val non dovrebbe mai venire valorizzata con timestamp successivi,
   * quindi la condizione IS NOT NULL dovrebbe essere abbastanza anche da sola
   */
  private static <T extends CosmoREntity> Predicate buildPredicateRelazioneValida(
      QueryContext<?> ctx, Path<T> root) {
    return ctx.builder.or(ctx.builder.isNull(root.get(CosmoREntity_.dtFineVal)),
        ctx.builder.greaterThan(root.get(CosmoREntity_.dtFineVal), ctx.queryTime));
  }

  /*
   * condizione di validita' per una entity di decodifica.
   *
   * dt_fine_val deve essere null oppure nel futuro
   *
   * da verificare, la dt_fine_val non dovrebbe mai venire valorizzata con timestamp successivi,
   * quindi la condizione IS NOT NULL dovrebbe essere abbastanza anche da sola
   */
  private static <T extends CosmoDEntity> Predicate buildPredicateDecodificaValida(
      QueryContext<?> ctx, Path<T> root) {
    return ctx.builder.or(ctx.builder.isNull(root.get(CosmoDEntity_.dtFineVal)),
        ctx.builder.greaterThan(root.get(CosmoDEntity_.dtFineVal), ctx.queryTime));
  }

  /*
   * costruisce il filtro per le pratiche associabili
   */
  private static Predicate buildPredicatePraticheAssociabili(QueryContext<CosmoTPratica> ctx) {

    var cb = ctx.builder;
    var root = ctx.root;
    var filtri = ctx.filtri;
    var cq = ctx.query;

    Predicate predicate = cb.conjunction();

    if (filtri == null || filtri.getDaAssociareA() == null
        || filtri.getDaAssociareA().getEquals() == null) {
      return predicate;
    }

    Subquery<Long> subQueryDa = cq.subquery(Long.class);
    Root<CosmoRPraticaPratica> subRootDa = subQueryDa.from(CosmoRPraticaPratica.class);

    subQueryDa.select(subRootDa.get(CosmoRPraticaPratica_.cosmoTPraticaDa).get(CosmoTPratica_.id))
    .where(getSubPredicate(cb, subRootDa, filtri.getDaAssociareA().getEquals()));

    Subquery<Long> subQueryA = cq.subquery(Long.class);
    Root<CosmoRPraticaPratica> subRootA = subQueryA.from(CosmoRPraticaPratica.class);

    subQueryA.select(subRootA.get(CosmoRPraticaPratica_.cosmoTPraticaA).get(CosmoTPratica_.id))
    .where(getSubPredicate(cb, subRootA, filtri.getDaAssociareA().getEquals()));

    return cb.and(cb.notEqual(root.get(CosmoTPratica_.id), filtri.getDaAssociareA().getEquals()),
        cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
        cb.in(root.get(CosmoTPratica_.id)).value(subQueryDa).not(),
        cb.in(root.get(CosmoTPratica_.id)).value(subQueryA).not());

  }

  /*
   * costruisce il filtro per i task contenuti
   */
  private static Predicate buildPredicateContieneAttivitaEseguibiliMassivamenteDaUtenteCorrente(
      QueryContext<CosmoTPratica> ctx) {

    var cb = ctx.builder;
    var root = ctx.root;
    var filtri = ctx.filtri;
    var cq = ctx.query;

    Predicate predicate = cb.conjunction();

    if (filtri == null || filtri.getEsecuzioneMultipla() == null
        || !filtri.getEsecuzioneMultipla().booleanValue()) {
      return predicate;
    }

    Subquery<Long> subquery = cq.subquery(Long.class);
    Root<CosmoTAttivita> rootSubquery = subquery.from(CosmoTAttivita.class);

    var subQueryContext = subqueryContext(ctx, rootSubquery);

    //@formatter:off
    subquery.select(
        rootSubquery.get(CosmoTAttivita_.cosmoTPratica).get(CosmoTPratica_.id)
        )
    .where(
        buildPredicateAttivitaEseguibileMassivamenteDaUtenteCorrente(subQueryContext, filtri.getTaskMassivo())
        );
    //@formatter:on

    return cb.and(cb.in(root.get(CosmoTPratica_.id)).value(subquery));
  }

  private static Predicate buildPredicateAttivitaEseguibileMassivamenteDaUtenteCorrente(
      QueryContext<CosmoTAttivita> ctx, String codiceFunzionalita) {

    Root<CosmoTAttivita> root = ctx.root;
    CriteriaBuilder cb = ctx.builder;

    var joinTFormLogico = root.join(CosmoTAttivita_.formLogico, JoinType.LEFT);

    //@formatter:off
    return cb.and(
        buildPredicateEntitaValida(ctx, root),
        buildPredicateEntitaValida(ctx, joinTFormLogico),
        cb.and(
            cb.isNull(joinTFormLogico.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinTFormLogico.get(CosmoTFormLogico_.eseguibileMassivamente)),
            cb.isTrue(joinTFormLogico.get(CosmoTFormLogico_.eseguibileMassivamente))
            ),
        StringUtils.isBlank(codiceFunzionalita) ? cb.conjunction() :
          cb.equal(root.get(CosmoTAttivita_.nome), codiceFunzionalita),
          cb.or(
              buildPredicateAttivitaAssegnataUtenteCorrente(ctx, true),
              buildPredicateAttivitaAssegnataGruppoUtente(ctx, true)
              )
        );
    //@formatter:on
  }

  private static List<Order> getOrder(QueryContext<CosmoTPratica> ctx, String sort) {
    if (StringUtils.isBlank(sort)) {
      sort = CosmoTGruppo_.nome.getName();
    }

    String[] sortSplit = sort.trim().split(",");

    List<Order> ordine = ctx.ordine;
    for (String sortSplitSingle : sortSplit) {
      List<Order> clause = createSort(ctx, sortSplitSingle);
      if (clause != null && !clause.isEmpty()) {
        ordine.addAll(clause);
      }
    }
    return ordine;
  }

  private static List<Order> createSort(QueryContext<CosmoTPratica> ctx, String sortSplitSingle) {
    if (StringUtils.isBlank(sortSplitSingle)) {
      return Collections.emptyList();
    }

    org.springframework.data.domain.Sort.Order parsed =
        SearchUtils.parseOrderClause(sortSplitSingle);
    if (parsed == null) {
      return Collections.emptyList();
    }

    List<Order> custom = createCustomSort(ctx, parsed);
    if (!custom.isEmpty()) {
      return custom;
    }

    var cb = ctx.builder;
    var root = ctx.root;

    if (FieldUtils.getField(CosmoTPratica_.class, parsed.getProperty()) != null) {
      return Arrays.asList(parsed.isAscending() ? cb.asc(root.get(parsed.getProperty()))
          : cb.desc(root.get(parsed.getProperty())));
    } else {
      return Collections.emptyList();
    }
  }

  private static List<Order> createCustomSort(QueryContext<CosmoTPratica> ctx, // NOSONAR
      org.springframework.data.domain.Sort.Order parsed) {
    var cb = ctx.builder;
    var root = ctx.root;

    List<Order> ordine = new LinkedList<>();
    if (parsed.getProperty().equalsIgnoreCase(Constants.SORT.DATA_CREAZIONE_PRATICA)) {
      if (parsed.getDirection().isAscending()) {
        ordine.add(cb.asc(root.get(CosmoTEntity_.dtInserimento)));
        ordine.add(cb.asc(root.get(CosmoTEntity_.dtUltimaModifica)));
        ordine.add(cb.asc(root.get(CosmoTPratica_.dataCambioStato)));
      } else {
        ordine.add(cb.desc(root.get(CosmoTEntity_.dtInserimento)));
        ordine.add(cb.asc(root.get(CosmoTEntity_.dtUltimaModifica)));
        ordine.add(cb.asc(root.get(CosmoTPratica_.dataCambioStato)));
      }
    }

    if (parsed.getProperty().equalsIgnoreCase(Constants.SORT.DATA_AGGIORNAMENTO_PRATICA)) {
      if (parsed.getDirection().isAscending()) {
        ordine.add(cb.asc(root.get(CosmoTEntity_.dtUltimaModifica)));
        ordine.add(cb.asc(root.get(CosmoTPratica_.dataCambioStato)));
      } else {
        ordine.add(cb.desc(root.get(CosmoTEntity_.dtUltimaModifica)));
        ordine.add(cb.asc(root.get(CosmoTPratica_.dataCambioStato)));
      }
    }

    if (parsed.getProperty().equalsIgnoreCase(CosmoTPratica_.stato.getName())) {
      if (parsed.getDirection().isAscending()) {
        ordine.add(cb.asc(root.get(CosmoTPratica_.stato).get(CosmoDStatoPratica_.descrizione)));
      } else {
        ordine.add(cb.desc(root.get(CosmoTPratica_.stato).get(CosmoDStatoPratica_.descrizione)));
      }
    }

    if (parsed.getProperty().equalsIgnoreCase(Constants.SORT.TIPOLOGIA)
        || parsed.getProperty().equalsIgnoreCase(CosmoTPratica_.tipo.getName())) {
      if (parsed.getDirection().isAscending()) {
        ordine.add(cb.asc(root.get(CosmoTPratica_.tipo).get(CosmoDTipoPratica_.descrizione)));
      } else {
        ordine.add(cb.desc(root.get(CosmoTPratica_.tipo).get(CosmoDTipoPratica_.descrizione)));
      }
    }

    return ordine;
  }

  private static Predicate getSubPredicate(CriteriaBuilder cb, Root<CosmoRPraticaPratica> subRoot,
      Long idPratica) {
    return cb.and(cb.or(
        cb.equal(subRoot.get(CosmoRPraticaPratica_.cosmoTPraticaDa).get(CosmoTPratica_.id),
            idPratica),
        cb.equal(subRoot.get(CosmoRPraticaPratica_.cosmoTPraticaA).get(CosmoTPratica_.id),
            idPratica)),
        cb.isNull(subRoot.get(CosmoREntity_.dtFineVal)));
  }

  private static Predicate buildPredicatePerVariabiliProcesso(QueryContext<CosmoTPratica> ctx,
      String sort) {

    Predicate predicate = ctx.builder.conjunction();

    if (ctx.filtri != null && ctx.filtri.getTipologia() != null
        && ctx.filtri.getVariabiliProcesso() != null
        && !ctx.filtri.getVariabiliProcesso().isEmpty()) {

      return ctx.builder.and(predicate, getPredicatePerVariabiliProcesso(ctx, sort));
    }
    return predicate;
  }

  private static Predicate getPredicatePerVariabiliProcesso(QueryContext<CosmoTPratica> ctx,
      String sort) {

    CriteriaBuilder cb = ctx.builder;
    Root<CosmoTPratica> root = ctx.root;
    List<VariabileProcessoDTO> variabiliProcesso = ctx.filtri.getVariabiliProcesso();

    List<Order> ordine = new LinkedList<>();

    Map<String, String> sortMap = new HashMap<>();
    if (sort != null && !sort.isBlank()) {
      sortMap = Arrays.stream(sort.trim().split(",")).map(s -> s.trim().split(" "))
          .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : ""));
    }

    Predicate predicate = cb.conjunction();

    for (VariabileProcessoDTO variabileProcesso : variabiliProcesso) {

      ListJoin<CosmoTPratica, CosmoTVariabile> joinVariabile =
          root.join(CosmoTPratica_.variabili, JoinType.INNER);

      Predicate predicateVariabile = cb.and(
          cb.equal(joinVariabile.get(CosmoTVariabile_.nome), variabileProcesso.getNomeVariabile()),
          cb.isNull(joinVariabile.get(CosmoTEntity_.dtCancellazione)),
          getPredicateForValoreVariabile(variabileProcesso, joinVariabile, cb));

      predicate = cb.and(predicate, predicateVariabile);

      if (!sortMap.isEmpty() && sortMap.containsKey(variabileProcesso.getNomeVariabile())) {

        if (variabileProcesso.getSingolo() != null) {
          createOrderForVariables(ordine, sortMap.get(variabileProcesso.getNomeVariabile()),
              joinVariabile, cb, variabileProcesso, variabileProcesso.getSingolo());

        } else if (variabileProcesso.getRangeA() != null) {
          createOrderForVariables(ordine, sortMap.get(variabileProcesso.getNomeVariabile()),
              joinVariabile, cb, variabileProcesso, variabileProcesso.getRangeA());

        } else if (variabileProcesso.getRangeDa() != null) {
          createOrderForVariables(ordine, sortMap.get(variabileProcesso.getNomeVariabile()),
              joinVariabile, cb, variabileProcesso, variabileProcesso.getRangeDa());
        }
      }

    }
    ctx.ordine = ordine;

    return predicate;
  }

  static Predicate getPredicateForValoreVariabile(VariabileProcessoDTO variabileProcessoDTO,
      Join<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb) {

    Predicate predicate = cb.conjunction();

    if (variabileProcessoDTO.getSingolo() != null) {

      predicate =
          gestisciValoreVariabileSingolo(variabileProcessoDTO, predicate, joinVariabile, cb);

    }

    else {
      if (variabileProcessoDTO.getRangeDa() != null) {

        predicate = gestisciValoreVariabileRange(variabileProcessoDTO,
            variabileProcessoDTO.getRangeDa(), predicate, joinVariabile, cb);

      }

      if (variabileProcessoDTO.getRangeA() != null) {

        predicate = gestisciValoreVariabileRange(variabileProcessoDTO,
            variabileProcessoDTO.getRangeA(), predicate, joinVariabile, cb);

      }
    }

    return predicate;

  }

  static Predicate gestisciValoreVariabileRange(VariabileProcessoDTO variabileProcessoDTO,
      ValoreVariabile range, Predicate predicate,
      Join<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb) {

    if (variabileProcessoDTO.getAlberaturaJson() != null) {
      List<String> nomiVariabile =
          Arrays.asList(variabileProcessoDTO.getAlberaturaJson().split("[.]"));

      predicate = gestisciValoreVariabileJson(range, nomiVariabile, predicate, joinVariabile, cb);

    } else {
      predicate = gestisciValoreVariabileNoJson(range, predicate, joinVariabile, cb);
    }

    return predicate;
  }

  static Predicate gestisciValoreVariabileSingolo(VariabileProcessoDTO variabileProcessoDTO,
      Predicate predicate, Join<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb) {

    ValoreVariabile singolo = variabileProcessoDTO.getSingolo();

    if (variabileProcessoDTO.getAlberaturaJson() != null) {

      List<String> nomiVariabile =
          Arrays.asList(variabileProcessoDTO.getAlberaturaJson().split("[.]"));

      predicate = gestisciValoreVariabileJson(singolo, nomiVariabile, predicate, joinVariabile, cb);

    } else {
      predicate = gestisciValoreVariabileNoJson(singolo, predicate, joinVariabile, cb);
    }

    return predicate;

  }

  private static Predicate gestisciValoreVariabileJson(ValoreVariabile valore,
      List<String> nomiVariabile, Predicate predicate,
      Join<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb) {

    if (valore.getVariabileString() != null) {
      Expression<String> arrFunc = null;

      if (valore.getVariabileString().getDefined() != null) {
        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_STRING, String.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(FilterCriteria.DEFINED.getCodice()), cb.literal(""),
            cb.literal(valore.getVariabileString().getDefined()), cb.literal(nomiVariabile));

      } else {
        var search = getMapString(valore.getVariabileString());
        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_STRING, String.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(new ArrayList<>(search.keySet()).get(0)),
            cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(false),
            cb.literal(nomiVariabile));
      }

      predicate = cb.or(SpecificationUtils.applyFilter(valore.getVariabileString(),
          cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_STRING, String.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile)),
          predicate, cb), cb.isNotNull(arrFunc));

    } else if (valore.getVariabileNumerica() != null) {

      Expression<Double> arrFunc = null;

      if (valore.getVariabileNumerica().getDefined() != null) {
        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_NUMERIC, Double.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(FilterCriteria.DEFINED.getCodice()), cb.literal(0), cb.literal(""),
            cb.literal(valore.getVariabileNumerica().getDefined()), cb.literal(nomiVariabile));

      } else {
        var search = getMapNumber(valore.getVariabileNumerica());

        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_NUMERIC, Double.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(new ArrayList<>(search.keySet()).get(0)),
            cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(""),
            cb.literal(false), cb.literal(nomiVariabile));
      }

      predicate = cb.or(SpecificationUtils.applyFilter(valore.getVariabileNumerica(),
          cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_NUMERIC, Double.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile)),
          predicate, cb), cb.isNotNull(arrFunc));

    } else if (valore.getVariabileBoolean() != null) {

      Expression<Timestamp> arrFunc =
          cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_BOOLEAN, Timestamp.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(FilterCriteria.EQUALS.getCodice()),
              cb.literal(valore.getVariabileBoolean().getEquals()), cb.literal(nomiVariabile));

      predicate = cb.or(SpecificationUtils.applyFilter(valore.getVariabileBoolean(),
          cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_BOOLEAN, Boolean.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile)),
          predicate, cb), cb.isNotNull(arrFunc));

    } else if (valore.getVariabileData() != null) {

      Expression<Timestamp> arrFunc = null;

      if (valore.getVariabileData().getDefined() != null) {
        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_DATE, Timestamp.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(FilterCriteria.DEFINED.getCodice()),
            cb.literal(Timestamp.valueOf(LocalDateTime.now())), cb.literal(""),
            cb.literal(valore.getVariabileData().getDefined()), cb.literal(nomiVariabile));

      } else {
        var search = getMapDate(valore.getVariabileData());
        arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_DATE, Timestamp.class,
            joinVariabile.get(CosmoTVariabile_.jsonValue),
            cb.literal(new ArrayList<>(search.keySet()).get(0)),
            cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(""),
            cb.literal(false), cb.literal(nomiVariabile));
      }

      predicate = cb.or(SpecificationUtils.applyFilter(valore.getVariabileData(),
          cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_DATE, Timestamp.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile)),
          predicate, cb), cb.isNotNull(arrFunc));
    }

    return predicate;

  }

  private static Predicate gestisciValoreVariabileNoJson(ValoreVariabile valore,
      Predicate predicate, Join<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb) {

    if (valore.getVariabileBoolean() != null) {
      predicate = cb.and(predicate, cb.equal(joinVariabile.get(CosmoTVariabile_.textValue),
          valore.getVariabileBoolean().getEquals().toString()));

    } else if (valore.getVariabileData() != null) {

      predicate = SpecificationUtils.applyFilter(valore.getVariabileData(),
          joinVariabile.get(CosmoTVariabile_.textValue).as(Timestamp.class), predicate, cb);

    } else if (valore.getVariabileNumerica() != null) {
      if (valore.getVariabileNumerica().getDefined() != null
          && !valore.getVariabileNumerica().getDefined().booleanValue()) {
        predicate = cb.and(
            SpecificationUtils.applyFilter(valore.getVariabileNumerica(),
                joinVariabile.get(CosmoTVariabile_.longValue).as(Double.class), predicate, cb),
            SpecificationUtils.applyFilter(valore.getVariabileNumerica(),
                joinVariabile.get(CosmoTVariabile_.doubleValue), predicate, cb));
      } else {
        predicate = cb.or(
            SpecificationUtils.applyFilter(valore.getVariabileNumerica(),
                joinVariabile.get(CosmoTVariabile_.longValue).as(Double.class), predicate, cb),
            SpecificationUtils.applyFilter(valore.getVariabileNumerica(),
                joinVariabile.get(CosmoTVariabile_.doubleValue), predicate, cb));
      }
    } else if (valore.getVariabileString() != null) {
      if (valore.getVariabileString().getDefined() != null
          && !valore.getVariabileString().getDefined().booleanValue()) {
        predicate = cb.and(
            SpecificationUtils.applyFilter(valore.getVariabileString(),
                joinVariabile.get(CosmoTVariabile_.textValue), predicate, cb),
            SpecificationUtils.applyFilter(valore.getVariabileString(),
                cb.function("encode", String.class,
                    joinVariabile.get(CosmoTVariabile_.bytearrayValue), cb.literal("escape")),
                predicate, cb));
      } else {
        predicate = cb.or(
            SpecificationUtils.applyFilter(valore.getVariabileString(),
                joinVariabile.get(CosmoTVariabile_.textValue), predicate, cb),
            SpecificationUtils.applyFilter(valore.getVariabileString(),
                cb.function("encode", String.class,
                    joinVariabile.get(CosmoTVariabile_.bytearrayValue), cb.literal("escape")),
                predicate, cb));
      }
    }

    return predicate;
  }

  private static void createOrderForVariables(List<Order> ordine, String direction,
      ListJoin<CosmoTPratica, CosmoTVariabile> joinVariabile, CriteriaBuilder cb,
      VariabileProcessoDTO variabileProcesso, ValoreVariabile valoreVariabile) {


    if (variabileProcesso.getAlberaturaJson() != null) {

      List<String> nomiVariabile =
          Arrays.asList(variabileProcesso.getAlberaturaJson().split("[.]"));

      if (valoreVariabile.getVariabileNumerica() != null) {

        var funcJson = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_NUMERIC,
            Double.class, joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile));

        addOrder(cb, funcJson, ordine, direction);

        Expression<Double> arrFunc = null;

        if (valoreVariabile.getVariabileNumerica().getDefined() != null) {
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_NUMERIC, Double.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(FilterCriteria.DEFINED.getCodice()), cb.literal(0), cb.literal(""),
              cb.literal(valoreVariabile.getVariabileNumerica().getDefined()),
              cb.literal(nomiVariabile));

        } else {
          var search = getMapNumber(valoreVariabile.getVariabileNumerica());
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_NUMERIC, Double.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(new ArrayList<>(search.keySet()).get(0)),
              cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(""),
              cb.literal(false),
              cb.literal(nomiVariabile));
        }

        addOrder(cb, arrFunc, ordine, direction);
      } else if (valoreVariabile.getVariabileData() != null) {
        var funcJson =
            cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_DATE, Timestamp.class,
                joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile));

        addOrder(cb, funcJson, ordine, direction);

        Expression<Timestamp> arrFunc = null;

        if (valoreVariabile.getVariabileData().getDefined() != null) {
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_DATE, Timestamp.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(FilterCriteria.DEFINED.getCodice()),
              cb.literal(Timestamp.valueOf(LocalDateTime.now())),
              cb.literal(""),
              cb.literal(valoreVariabile.getVariabileData().getDefined()),
              cb.literal(nomiVariabile));

        } else {
          var search = getMapDate(valoreVariabile.getVariabileData());
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_DATE, Timestamp.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(new ArrayList<>(search.keySet()).get(0)),
              cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(""),
              cb.literal(false), cb.literal(nomiVariabile));
        }
        addOrder(cb, arrFunc, ordine, direction);
      } else if (valoreVariabile.getVariabileBoolean() != null) {

        var funcJson =
            cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_BOOLEAN, Boolean.class,
                joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile));

        addOrder(cb, funcJson, ordine, direction);

        Expression<Boolean> arrFunc =
            cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_BOOLEAN, Boolean.class,
                joinVariabile.get(CosmoTVariabile_.jsonValue),
                cb.literal(FilterCriteria.EQUALS.getCodice()),
                cb.literal(valoreVariabile.getVariabileBoolean().getEquals()),
                cb.literal(nomiVariabile));

        addOrder(cb, arrFunc, ordine, direction);


      } else if (valoreVariabile.getVariabileString() != null) {

        var funcJson = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_VALUE_STRING,
            String.class, joinVariabile.get(CosmoTVariabile_.jsonValue), cb.literal(nomiVariabile));

        addOrder(cb, funcJson, ordine, direction);

        Expression<String> arrFunc = null;

        if (valoreVariabile.getVariabileString().getDefined() != null) {
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_STRING, String.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(FilterCriteria.DEFINED.getCodice()), cb.literal(""),
              cb.literal(valoreVariabile.getVariabileString().getDefined()),
              cb.literal(nomiVariabile));

        } else {
          var search = getMapString(valoreVariabile.getVariabileString());
          arrFunc = cb.function(Constants.POSTGRESQL_FUNCTIONS.GET_JSON_ARRAY_STRING, String.class,
              joinVariabile.get(CosmoTVariabile_.jsonValue),
              cb.literal(new ArrayList<>(search.keySet()).get(0)),
              cb.literal(search.get(new ArrayList<>(search.keySet()).get(0))), cb.literal(false),
              cb.literal(nomiVariabile));
        }
        addOrder(cb, arrFunc, ordine, direction);

      }

    } else {

      if (valoreVariabile.getVariabileNumerica() != null) {
        addOrder(cb, joinVariabile.get(CosmoTVariabile_.doubleValue), ordine, direction);
        addOrder(cb, joinVariabile.get(CosmoTVariabile_.longValue), ordine, direction);

      } else {
        addOrder(cb, joinVariabile.get(CosmoTVariabile_.textValue), ordine, direction);

        if (valoreVariabile.getVariabileString() != null) {
          addOrder(cb, joinVariabile.get(CosmoTVariabile_.bytearrayValue), ordine, direction);
        }
      }
    }

  }

  private static void addOrder(CriteriaBuilder cb, Expression<?> expression, List<Order> ordine,
      String direction) {

    if (direction.equalsIgnoreCase("ASC")) {
      ordine.add(cb.asc(expression));
    } else {
      ordine.add(cb.desc(expression));
    }

  }

  private static Predicate getPredicatePerTipologieStatiDaAssociare(CriteriaBuilder cb,
      List<TipologiaStatiPraticaDaAssociareDTO> tipologieStatiDaAssociare,
      Join<CosmoTPratica, CosmoDTipoPratica> joinTipo,
      Join<CosmoTPratica, CosmoDStatoPratica> joinStato) {
    Predicate predicate = cb.disjunction();

    for (var tipologiaStatiDaAssociare : tipologieStatiDaAssociare) {

      if (tipologiaStatiDaAssociare.getTipologia() != null
          && !tipologiaStatiDaAssociare.getTipologia().isBlank()) {

        if (tipologiaStatiDaAssociare.getStati() == null
            || tipologiaStatiDaAssociare.getStati().isEmpty()) {
          predicate = cb.or(predicate, cb.equal(joinTipo.get(CosmoDTipoPratica_.codice),
              tipologiaStatiDaAssociare.getTipologia()));

        } else {
          predicate = cb.or(predicate, cb.and(
              cb.equal(joinTipo.get(CosmoDTipoPratica_.codice),
                  tipologiaStatiDaAssociare.getTipologia()),
              joinStato.get(CosmoDStatoPratica_.codice).in(tipologiaStatiDaAssociare.getStati())));
        }
      }
    }
    return predicate;
  }

  private static Predicate getPredicatePerStatiDaAssociare(StringFilter tipologia,
      List<TipologiaStatiPraticaDaAssociareDTO> tipologieStatiDaAssociare,
      Join<CosmoTPratica, CosmoDStatoPratica> joinStato) {


    if (tipologia != null && tipologia.getEquals() != null && !tipologia.getEquals().isBlank()) {
      var stato = tipologieStatiDaAssociare.stream()
          .filter(item -> tipologia.getEquals().equals(item.getTipologia())).findFirst();

      if (stato.isPresent() && stato.get() != null && !stato.get().getStati().isEmpty()) {
        return joinStato.get(CosmoDStatoPratica_.codice).in(stato.get().getStati());
      }
    }
    return null;
  }

  private static Map<String, String> getMapString(StringFilter value) {
    Map<String, String> output = new HashMap<>();
    if (value.getEquals() != null) {
      output.putIfAbsent(FilterCriteria.EQUALS.getCodice(), value.getEquals());
    } else if (value.getNotEquals() != null) {
      output.putIfAbsent(FilterCriteria.NOT_EQUALS.getCodice(), value.getNotEquals());
    } else if (value.getContainsIgnoreCase() != null) {
      output.putIfAbsent(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice(),
          value.getContainsIgnoreCase());
    } else if (value.getNotContainsIgnoreCase() != null) {
      output.putIfAbsent(FilterCriteria.NOT_CONTAINS_IGNORE_CASE.getCodice(),
          value.getNotContainsIgnoreCase());
    } else if (value.getStartsWithIgnoreCase() != null) {
      output.putIfAbsent(FilterCriteria.STARTS_WITH_IGNORE_CASE.getCodice(),
          value.getStartsWithIgnoreCase());
    } else if (value.getEndsWithIgnoreCase() != null) {
      output.putIfAbsent(FilterCriteria.ENDS_WITH_IGNORE_CASE.getCodice(),
          value.getEndsWithIgnoreCase());
    }
    return output;
  }


  private static Map<String, Double> getMapNumber(DoubleFilter value) {
    Map<String, Double> output = new HashMap<>();
    if (value.getEquals() != null) {
      output.putIfAbsent(FilterCriteria.EQUALS.getCodice(), value.getEquals());
    } else if (value.getNotEquals() != null) {
      output.putIfAbsent(FilterCriteria.NOT_EQUALS.getCodice(), value.getNotEquals());
    } else if (value.getGreaterThan() != null) {
      output.putIfAbsent(FilterCriteria.GREATER_THAN.getCodice(), value.getGreaterThan());
    } else if (value.getGreaterThanOrEqualTo() != null) {
      output.putIfAbsent(FilterCriteria.GREATER_THAN_EQUALS.getCodice(),
          value.getGreaterThanOrEqualTo());
    } else if (value.getLessThan() != null) {
      output.putIfAbsent(FilterCriteria.LESS_THAN.getCodice(), value.getLessThan());
    } else if (value.getLessThanOrEqualTo() != null) {
      output.putIfAbsent(FilterCriteria.LESS_THAN_EQUALS.getCodice(),
          value.getLessThanOrEqualTo());
    }
    return output;
  }

  private static Map<String, Timestamp> getMapDate(DateFilter value) {
    Map<String, Timestamp> output = new HashMap<>();
    if (value.getEquals() != null) {
      output.putIfAbsent(FilterCriteria.EQUALS.getCodice(),
          Timestamp.valueOf(value.getEquals().atStartOfDay()));
    } else if (value.getNotEquals() != null) {
      output.putIfAbsent(FilterCriteria.NOT_EQUALS.getCodice(),
          Timestamp.valueOf(value.getNotEquals().atStartOfDay()));
    } else if (value.getGreaterThan() != null) {
      output.putIfAbsent(FilterCriteria.GREATER_THAN.getCodice(),
          Timestamp.valueOf(value.getGreaterThan().atStartOfDay()));
    } else if (value.getGreaterThanOrEqualTo() != null) {
      output.putIfAbsent(FilterCriteria.GREATER_THAN_EQUALS.getCodice(),
          Timestamp.valueOf(value.getGreaterThanOrEqualTo().atStartOfDay()));
    } else if (value.getLessThan() != null) {
      output.putIfAbsent(FilterCriteria.LESS_THAN.getCodice(),
          Timestamp.valueOf(value.getLessThan().atStartOfDay()));
    } else if (value.getLessThanOrEqualTo() != null) {
      output.putIfAbsent(FilterCriteria.LESS_THAN_EQUALS.getCodice(),
          Timestamp.valueOf(value.getLessThanOrEqualTo().atStartOfDay()));
    }
    return output;
  }
  /*
   * HELPER CLASSES and methods
   */

  /*
   * contesto che indica uno snapshot delle informazioni per la query
   */

  public static class QueryContext<T> {
    FiltroRicercaPraticheDTO filtri;
    Long idEnte;
    Timestamp queryTime;
    Root<T> root;
    CriteriaQuery<?> query;
    CriteriaBuilder builder;
    UserInfoDTO utenteCorrente;
    List<Order> ordine;
    FiltroRicercaPraticheFruitoriDTO filtriFruitore;
  }

  /*
   * helper per creare un container delle informazioni di contesto per la query
   */
  private static <T> QueryContext<T> buildContext(Root<T> root, CriteriaQuery<?> cq,
      CriteriaBuilder cb, Long idEnte, UserInfoDTO userInfo, FiltroRicercaPraticheDTO filtri,
      List<Order> ordine, FiltroRicercaPraticheFruitoriDTO filtriFruitore) {

    QueryContext<T> output = new QueryContext<>();
    output.filtri = filtri;
    output.queryTime = Timestamp.from(Instant.now());
    output.root = root;
    output.builder = cb;
    output.query = cq;
    output.idEnte = idEnte;
    output.utenteCorrente = userInfo;
    output.ordine = ordine;
    output.filtriFruitore = filtriFruitore;
    return output;
  }

  private static <T> QueryContext<T> subqueryContext(QueryContext<?> parent, Root<T> root) {

    QueryContext<T> output = new QueryContext<>();
    output.queryTime = parent.queryTime;
    output.utenteCorrente = parent.utenteCorrente;
    output.idEnte = parent.idEnte;
    output.filtri = parent.filtri;
    output.root = root;
    output.builder = parent.builder;
    output.query = parent.query;
    output.ordine = parent.ordine;
    output.filtriFruitore = parent.filtriFruitore;
    return output;
  }


  /*
   * l'utente e' legato ad un gruppo, il quale e' supervisore per la tipologia della pratica
   * (buildPredicateMonitoraggio) costruisce il filtro per il monitoraggio delle pratiche da
   * supervisore della tipologia pratica
   *
   *
   */
  private static Predicate buildPredicateMonitoraggio(QueryContext<CosmoTPratica> ctx,
      boolean validaOra) {
    var cb = ctx.builder;

    //@formatter:off
    Subquery<String> subquery = ctx.query.subquery(String.class);
    Root<CosmoDTipoPratica> subqueryRoot = subquery.from(CosmoDTipoPratica.class);

    ListJoin<CosmoDTipoPratica, CosmoRGruppoTipoPratica> joinGruppoTipoPratica =
        subqueryRoot.join(CosmoDTipoPratica_.cosmoRGruppoTipoPraticas, JoinType.INNER);

    Join<CosmoRGruppoTipoPratica, CosmoTGruppo> joinGruppoTipoPraticaGruppo =
        joinGruppoTipoPratica.join(CosmoRGruppoTipoPratica_.cosmoTGruppo, JoinType.INNER);

    Join<CosmoTGruppo, CosmoTUtenteGruppo> joinGruppoUtenteGruppo =
        joinGruppoTipoPraticaGruppo.join(CosmoTGruppo_.associazioniUtenti, JoinType.INNER);

    //@formatter:off
    subquery
    .select(subqueryRoot.get(CosmoDTipoPratica_.codice))
    .where(
        cb.and(cb.isTrue(joinGruppoTipoPratica.get(CosmoRGruppoTipoPratica_.supervisore))),
        validaOra ? buildPredicateDecodificaValida(ctx, subqueryRoot) : cb.conjunction(),
            validaOra ? buildPredicateEntitaValida(ctx, joinGruppoUtenteGruppo) : cb.conjunction(),
                validaOra ? buildPredicateRelazioneValida(ctx, joinGruppoTipoPratica) : cb.conjunction(),
                    validaOra ? buildPredicateEntitaValida(ctx, joinGruppoTipoPraticaGruppo) : cb.conjunction(),
                        cb.equal(joinGruppoUtenteGruppo.get(CosmoTUtenteGruppo_.idUtente), ctx.utenteCorrente.getId())
        );

    //@formatter:on

    return cb.and(ctx.root.get(CosmoTPratica_.tipo).get(CosmoDTipoPratica_.codice).in(subquery));
  }

  static Specification<CosmoTPratica> findByFiltersFruitori(FiltroRicercaPraticheFruitoriDTO filtri,
      UserInfoDTO userInfo, Long idEnte, String sort) {
    return (Root<CosmoTPratica> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      QueryContext<CosmoTPratica> ctx =
          buildContext(root, cq, cb, idEnte, userInfo, null, new LinkedList<>(), filtri);

      Predicate predicate = buildPredicateRicercaPraticheFruitori(ctx);

      if (Long.class != cq.getResultType() && long.class != cq.getResultType()) {
        root.fetch(CosmoTPratica_.stato, JoinType.LEFT);
        root.fetch(CosmoTPratica_.tipo, JoinType.LEFT);
        root.fetch(CosmoTPratica_.ente, JoinType.LEFT);
        root.fetch(CosmoTPratica_.fruitore, JoinType.LEFT);
      }

      return cq.where(predicate).orderBy(cb.desc(root.get(CosmoTEntity_.dtInserimento)),
          cb.desc(root.get(CosmoTEntity_.dtUltimaModifica))).getRestriction();
    };
 }

    static Predicate buildPredicateRicercaPraticheFruitori(QueryContext<CosmoTPratica> ctx) {
      return ctx.builder.and(

          buildPredicateAppartenenzaEnte(ctx),
          buildPredicatePraticaVisibileAdUtenteFruitori(ctx),
          buildPredicateFiltriUtenteFruitore(ctx),
          buildPredicateGruppiFruitore(ctx));
    }

    private static Predicate buildPredicateGruppiFruitore(QueryContext<CosmoTPratica> ctx) {

      Predicate predicate = ctx.builder.conjunction();

      // filtra per gruppi se specificato

        List<Predicate> predicatesGruppi = new ArrayList<>();
        predicatesGruppi.add(buildPredicateDaLavorareConAvanzamentoFruitori(ctx));
        if (!predicatesGruppi.isEmpty()) {
          predicate =
              ctx.builder.and(predicate, ctx.builder.or(predicatesGruppi.toArray(new Predicate[0])));
        }

      return predicate;
    }

    private static Predicate buildPredicateDaLavorareConAvanzamentoFruitori(QueryContext<CosmoTPratica> ctx) {

      //@formatter:off
      return ctx.builder.and(
          buildPredicateDaLavorare(ctx),
          buildPredicateAvanzamentoLavorazioneFruitori(ctx)
          );
      //@formatter:on
    }

    private static Predicate buildPredicateAvanzamentoLavorazioneFruitori(QueryContext<CosmoTPratica> ctx) {

      Predicate predicate = ctx.builder.conjunction();

      List<Predicate> avanzamentoPredicates = new ArrayList<>();

      avanzamentoPredicates.add(buildPredicatePraticaInCorso(ctx));

      if (!avanzamentoPredicates.isEmpty()) {
        //@formatter:off
        predicate = ctx.builder.and(
            predicate,
            ctx.builder.or(
                avanzamentoPredicates.toArray(new Predicate[0])
                )
            );
        //@formatter:on
      }

      return predicate;
    }

    static Predicate buildPredicatePraticaVisibileAdUtenteFruitori(
        QueryContext<CosmoTPratica> ctx) {
      return ctx.builder.or(
          buildPredicatePraticaCreataDaUtenteCorrente(ctx),
          buildPredicateContieneAttivitaAssegnateGruppoUtente(ctx, false),
          buildPredicateContieneAttivitaAssegnateUtenteCorrente(ctx, false),
          buildPredicateInEvidenza(ctx, true),
          buildPredicateMonitoraggio(ctx, true)
          );
    }

    static Predicate buildPredicateFiltriUtenteFruitore(QueryContext<CosmoTPratica> ctx) {
      var cb = ctx.builder;
      var root = ctx.root;
      var filtriFruitore = ctx.filtriFruitore;

      Predicate predicate = cb.conjunction();

      if (filtriFruitore == null) {
        return predicate;
      }

      if(filtriFruitore.getCodiceIpaEnte() != null) {
      predicate = cb.and(
          cb.equal(root.get(CosmoTPratica_.ente).get(CosmoTEnte_.codiceIpa), filtriFruitore.getCodiceIpaEnte()));
      }
      if (filtriFruitore.getCodiceTipoPratica() != null) {
        Join<CosmoTPratica, CosmoDTipoPratica> joinTipo = root.join(CosmoTPratica_.tipo, JoinType.LEFT);
        predicate = cb.and(cb.equal(joinTipo.get(CosmoDTipoPratica_.codice), filtriFruitore.getCodiceTipoPratica()));
      }

      if(filtriFruitore.getCodiceTag() != null) {
      Join<CosmoTPratica, CosmoRPraticaTag> joinTag = root.join(CosmoTPratica_.cosmoRPraticaTags);
      predicate = cb.and(cb.equal(joinTag.get(CosmoRPraticaTag_.cosmoTTag).get(CosmoTTag_.codice), filtriFruitore.getCodiceTag()));
      }

      if(filtriFruitore.getApiManagerIdFruitore() != null) {
        predicate = cb.and(cb.equal(root.get(CosmoTPratica_.fruitore).get(CosmoTFruitore_.apiManagerId), filtriFruitore.getApiManagerIdFruitore()));
      }
      return predicate;
    }
}
