/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.TagsService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTipoTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaTagsDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Tag;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagsResponse;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTTagMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTUtenteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoTagRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteGruppoTagRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTTagSearchHandler;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

@Service
@Transactional
public class TagsServiceImpl implements TagsService {

  private static final String CLASS_NAME = TagsServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  private CosmoDTipoTagRepository cosmoDTipoTagRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTTagMapper cosmoTTagMapper;

  @Autowired
  private CosmoTUtenteMapper cosmoTUtenteMapper;

  @Autowired
  private CosmoRUtenteGruppoTagRepository cosmoRUtenteGruppoTagRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTag(Long id) {
    String methodName = "deleteTag";
    ValidationUtils.require(id, "id tag");

    CosmoTTag tagDaEliminare = cosmoTTagRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.T_TAG_NON_TROVATO, id)));

    tagDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    tagDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    tagDaEliminare = cosmoTTagRepository.save(tagDaEliminare);

    logger.info(methodName, "Tag con id {} eliminato", tagDaEliminare.getId());
  }

  @Override
  public TagsResponse getTags(String filter) {
    TagsResponse output = new TagsResponse();

    GenericRicercaParametricaDTO<FiltroRicercaTagsDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaTagsDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTTag> pageResult = cosmoTTagRepository.findAllNotDeleted(new CosmoTTagSearchHandler()
        .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTTag> entitiesSuDB = pageResult.getContent();

    List<Tag> tags = new LinkedList<>();

    for (var entity : entitiesSuDB) {

      Tag tag = cosmoTTagMapper.toDTO(entity);

      var assUtenteGruppoTag = cosmoRUtenteGruppoTagRepository.findActiveByField(
          CosmoRUtenteGruppoTag_.cosmoTTag,
          entity);

      var usersList = assUtenteGruppoTag.stream().map(ass -> {
        return cosmoTUtenteMapper.toRiferimentoDTO(ass.getCosmoTUtenteGruppo().getUtente());

          }).collect(Collectors.toList());

      tag.setUtenti(usersList);
      tags.add(tag);

    }

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(tags,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    output.setElementi(tags);


    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageResult.getNumber());
    pageInfo.setPageSize(pageResult.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageResult.getTotalElements()));
    pageInfo.setTotalPages(pageResult.getTotalPages());

    output.setPageInfo(pageInfo);
    return output;
  }

  @Override
  public TagResponse getTagById(Long id) {
    TagResponse output = new TagResponse();
    ValidationUtils.require(id, "id tag");

    CosmoTTag tag = cosmoTTagRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.T_TAG_NON_TROVATO, id)));

    output.setTag(cosmoTTagMapper.toDTO(tag));

    return output;
  }

  @Override
  public TagResponse postTag(CreaTagRequest body) {
    String methodName = "postTag";
    TagResponse output = new TagResponse();
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);


    // controlla che non ci siano conflitti
    findByFieldEqualsIgnoreCase(CosmoTTag_.codice, body.getCodice().trim(), null)
        .ifPresent(other -> {
          throw new ConflictException(
              String.format(ErrorMessages.T_TAG_CODE_GIA_ESISTENTE, body.getCodice()));
        });


    CosmoTTag entity = new CosmoTTag();

    CosmoTEnte ente = new CosmoTEnte();

    ente.setCodiceFiscale(body.getEnte().getCodiceFiscale());
    ente.setCodiceIpa(body.getEnte().getCodiceIpa());
    ente.setId(body.getEnte().getId());
    ente.setNome(body.getEnte().getNome());

    var tipoTag = creaTipoTag(body.getTipoTag());
    entity.setCosmoDTipoTag(tipoTag);
    entity.setCodice(body.getCodice());
    entity.setDescrizione(body.getDescrizione());
    entity.setCosmoTEnte(ente);

    var tagSalvato = cosmoTTagRepository.save(entity);
    logger.info(methodName, "Tag con id {} creato", tagSalvato.getId());

    output.setTag(cosmoTTagMapper.toDTO(entity));

    return output;
  }

  @Override
  public TagResponse updateTag(Long id, AggiornaTagRequest body) {
    String methodName = "updateTag";
    ValidationUtils.require(id, "id tag");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTTag tagDaAggiornare = cosmoTTagRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.T_TAG_NON_TROVATO, id)));

    // controlla che non ci siano conflitti
    findByFieldEqualsIgnoreCase(CosmoTTag_.codice, body.getCodice().trim(), id).ifPresent(other -> {
      throw new ConflictException(
          String.format(ErrorMessages.T_TAG_CODE_GIA_ESISTENTE, body.getCodice()));
    });


    if (!tagDaAggiornare.getCosmoTEnte().getId().equals(body.getEnte().getId())) {
      CosmoTEnte ente = new CosmoTEnte();

      ente.setCodiceFiscale(body.getEnte().getCodiceFiscale());
      ente.setCodiceIpa(body.getEnte().getCodiceIpa());
      ente.setId(body.getEnte().getId());
      ente.setNome(body.getEnte().getNome());

      tagDaAggiornare.setCosmoTEnte(ente);

    }
    tagDaAggiornare.setCodice(body.getCodice());
    tagDaAggiornare.setDescrizione(body.getDescrizione());

    var tagAggiornato = cosmoTTagRepository.save(tagDaAggiornare);
    logger.info(methodName, "Tag con id {} aggiornato", tagDaAggiornare.getId());

    if (!tagDaAggiornare.getCosmoDTipoTag().getCodice()
        .equalsIgnoreCase(body.getTipoTag().getCodice())) {
      var tipoTagAggiornato = creaTipoTag(body.getTipoTag());
      tagAggiornato.setCosmoDTipoTag(tipoTagAggiornato);
    }
    TagResponse output = new TagResponse();
    output.setTag(cosmoTTagMapper.toDTO(tagAggiornato));
    return output;
  }

  private Optional<CosmoTTag> findByFieldEqualsIgnoreCase(
      SingularAttribute<CosmoTTag, String> field, String value, Long excludeId) {
    return cosmoTTagRepository.findAllNotDeleted((root, query, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value.toUpperCase());
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTTag_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  private CosmoDTipoTag creaTipoTag(AggiornaTipoTagRequest tipoTag) {

    Timestamp now = Timestamp.from(Instant.now());
    CosmoDTipoTag tt = new CosmoDTipoTag();
    tt.setCodice(tipoTag.getCodice());
    tt.setDescrizione(tipoTag.getDescrizione());
    tt.setLabel(tipoTag.getLabel());
    tt.setDtInizioVal(now);

    return cosmoDTipoTagRepository.save(tt);
  }


}
