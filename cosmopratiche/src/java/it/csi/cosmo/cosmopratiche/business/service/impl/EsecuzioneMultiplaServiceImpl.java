/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmopratiche.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTAttivitaMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTAttivitaSpecifications;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class EsecuzioneMultiplaServiceImpl implements EsecuzioneMultiplaService {

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoTAttivitaMapper cosmoTAttivitaMapper;

  @Autowired
  private CosmoTPraticheMapper cosmoTPraticheMapper;

  @Deprecated
  @Override
  public List<AttivitaEseguibileMassivamente> getTaskDisponibiliPerUtenteCorrente() {
    var utenteCorrente = SecurityUtils.requireUtenteCorrente();

    var funzionalitaEseguibiliMassivamente = Arrays.asList("APPROVAZIONE", "FIRMA-DOCUMENTI");

    var attivita = cosmoTAttivitaRepository.findAll((root, cq, cb) -> {

      var joinPratica = root.join(CosmoTAttivita_.cosmoTPratica, JoinType.LEFT);
      var joinTFormLogico = root.join(CosmoTAttivita_.formLogico, JoinType.LEFT);
      var joinRIstanzeFormLogico = joinTFormLogico
          .join(CosmoTFormLogico_.cosmoRFormLogicoIstanzaFunzionalitas, JoinType.LEFT);
      var joinTIstanzeFormLogico = joinRIstanzeFormLogico.join(
          CosmoRFormLogicoIstanzaFunzionalita_.cosmoTIstanzaFunzionalitaFormLogico, JoinType.LEFT);
      var joinDFunzionalita = joinTIstanzeFormLogico.join(
          CosmoTIstanzaFunzionalitaFormLogico_.cosmoDFunzionalitaFormLogico,
          JoinType.LEFT);

      Subquery<CosmoTAttivita> subquery = cq.subquery(CosmoTAttivita.class);
      Root<CosmoTAttivita> rootSubquery = subquery.from(CosmoTAttivita.class);
      subquery.select(rootSubquery).where(CosmoTAttivitaSpecifications
          .buildPredicateAttivitaAssegnateAdUtente(rootSubquery, cq, cb, utenteCorrente));

      //@formatter:off
      return cb.and(
          cb.exists(subquery),
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(joinPratica.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(joinTFormLogico.get(CosmoTEntity_.dtCancellazione)),
          cb.and(
              cb.isNotNull(joinTFormLogico.get(CosmoTFormLogico_.eseguibileMassivamente)),
              cb.isTrue(joinTFormLogico.get(CosmoTFormLogico_.eseguibileMassivamente))
              ),
          cb.isNull(joinRIstanzeFormLogico.get(CosmoREntity_.dtFineVal)),
          cb.isNull(joinTIstanzeFormLogico.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(joinDFunzionalita.get(CosmoDEntity_.dtFineVal)),
          joinDFunzionalita.get(CosmoDFunzionalitaFormLogico_.codice).in(funzionalitaEseguibiliMassivamente)
          );
      //@formatter:on
    });

    var out = new ArrayList<AttivitaEseguibileMassivamente>();

    var mappedAttivitaCache = new HashMap<Long, RiferimentoAttivita>();
    var mappedPraticheCache = new HashMap<Long, Pratica>();

    attivita.stream().forEach(att ->
    att.getFormLogico().getCosmoRFormLogicoIstanzaFunzionalitas().stream()
    .filter(r -> r.valido() && r.getCosmoTIstanzaFunzionalitaFormLogico().nonCancellato()
        && r.getCosmoTIstanzaFunzionalitaFormLogico().getCosmoDFunzionalitaFormLogico()
        .valido()
        && funzionalitaEseguibiliMassivamente
        .contains(r.getCosmoTIstanzaFunzionalitaFormLogico()
            .getCosmoDFunzionalitaFormLogico().getCodice()))

    .forEach(filteredTask -> {

      var mapped = new AttivitaEseguibileMassivamente();
      var mappedAttivita = mappedAttivitaCache.computeIfAbsent(att.getId(),
          id -> cosmoTAttivitaMapper.toRiferimentoAttivita(att));
      mapped.setAttivita(mappedAttivita);

      var pratica = att.getCosmoTPratica();
      var mappedPratica = mappedPraticheCache.computeIfAbsent(pratica.getId(),
              id -> cosmoTPraticheMapper.toPracticeLight(pratica, null));
      mapped.setPratica(mappedPratica);

      var mappedFunzionalita = new FunzionalitaEseguibileMassivamente();
      mappedFunzionalita.setCodice(filteredTask.getCosmoTIstanzaFunzionalitaFormLogico()
          .getCosmoDFunzionalitaFormLogico().getCodice());
      mappedFunzionalita.setId(filteredTask.getCosmoTIstanzaFunzionalitaFormLogico().getId());
      mapped.setFunzionalita(mappedFunzionalita);

      out.add(mapped);
    }));

    return out;
  }

  @Override
  public List<RiferimentoAttivita> getTipologieTaskDisponibiliPerUtenteCorrente(
      String filter) {

    GenericRicercaParametricaDTO<FiltroRicercaPraticheDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaPraticheDTO.class);

    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();

    var nomiAttivita = cosmoTAttivitaRepository.findDistinctValues(
        CosmoTPraticaSpecifications.findAttivitaPossibiliByFilters(ricercaParametrica.getFilter(),
            userInfo, userInfo.getEnte() != null ? userInfo.getEnte().getId() : null),
        CosmoTAttivita_.nome.getName());

    return nomiAttivita.stream().filter(a -> !StringUtils.isBlank((String) a)).map(nome -> {
      var out = new RiferimentoAttivita();
      out.setNome((String) nome);
      return out;
    }).sorted((a1, a2) -> a1.getNome().compareTo(a2.getNome()))
        .collect(Collectors.toCollection(LinkedList::new));
  }

}
