/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service.impl;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
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
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmonotifications.business.service.HelperService;
import it.csi.cosmo.cosmonotifications.config.ErrorMessages;
import it.csi.cosmo.cosmonotifications.config.ParametriApplicativo;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.DecodificaHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaHelperDTO;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaHelperModaleDTO;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.PageInfo;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoDHelperModaleMapper;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoDHelperPaginaMapper;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoDHelperTabMapper;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoTHelperMapper;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoDCustomFormFormioRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoDHelperModaleRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoDHelperPaginaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoDHelperTabRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTHelperRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.specifications.CosmoTHelperSpecifications;
import it.csi.cosmo.cosmonotifications.security.SecurityUtils;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;
/**
 * Implementazione del servizio per la gestione degli helper
 */
@Service
@Transactional
public class HelperServiceImpl implements HelperService {

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY, "HelperServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTHelperRepository cosmoTHelperRepository;

  @Autowired
  private CosmoTHelperMapper cosmoTHelperMapper;

  @Autowired
  private CosmoDHelperPaginaRepository cosmoDHelperPaginaRepository;

  @Autowired
  private CosmoDHelperPaginaMapper cosmoDHelperPaginaMapper;

  @Autowired
  private CosmoDHelperTabRepository cosmoDHelperTabRepository;

  @Autowired
  private CosmoDHelperTabMapper cosmoDHelperTabMapper;

  @Autowired
  private CosmoDHelperModaleRepository cosmoDHelperModaleRepository;

  @Autowired
  private CosmoDCustomFormFormioRepository cosmoDCustomFormFormioRepository;

  @Autowired
  private CosmoDHelperModaleMapper cosmoDHelperModaleMapper;

  private static Map<String, Boolean> exportPolicyVerifications = new HashMap<>();

  private static final String CODICE = "codice";

  private static final String DESCRIZIONE = "descrizione";

  private static final String CUSTOM_FORM = "customForm";

  @Override
  public HelperResponse getHelpers(String filter) {
    HelperResponse output = new HelperResponse();

    GenericRicercaParametricaDTO<FiltroRicercaHelperDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaHelperDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTHelper> pageResult =
        cosmoTHelperRepository.findAll(
            CosmoTHelperSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTHelper> entitiesSuDB = pageResult.getContent();

    List<Helper> dtos = new LinkedList<>();
    entitiesSuDB.forEach(e -> dtos.add(cosmoTHelperMapper.toDTO(e)));
    output.setHelpers(dtos);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(dtos, Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageResult.getNumber());
    pageInfo.setPageSize(pageResult.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageResult.getTotalElements()));
    pageInfo.setTotalPages(pageResult.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public void deleteHelper(String id) {
    CosmoTHelper helper = getHelperById(id);

    if (helper != null) {
      helper.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
      helper.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      cosmoTHelperRepository.save(helper);
    }

  }

  @Override
  public Helper getHelper(String id) {
    CosmoTHelper helper = getHelperById(id);

    return cosmoTHelperMapper.toDTO(helper);
  }

  private void validation(Helper body) {
    ValidationUtils.require(body, "Helper");
    ValidationUtils.require(body.getCodicePagina(), "Pagina helper");
    ValidationUtils.require(body.getCodicePagina().getCodice(), "codice pagina helper");
    ValidationUtils.require(body.getHtml(), "Html helper");
  }

  @Override
  public Helper postHelper(Helper body) {
    validation(body);

    checkParametriHelper(body);

    CosmoTHelper helper = cosmoTHelperMapper.toRecord(body);
    helper.setDtInserimento(Timestamp.valueOf(LocalDateTime.now()));
    helper.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    helper = cosmoTHelperRepository.save(helper);

    return cosmoTHelperMapper.toDTO(helper);
  }

  @Override
  public Helper putHelper(String id, Helper body) {
    validation(body);

    checkParametriHelper(body);

    CosmoTHelper helper = getHelperById(id);

    if (helper != null) {
      if (null != body.getCodicePagina()
          && !StringUtils.isBlank(body.getCodicePagina().getCodice())) {
        CosmoDHelperPagina helperPagina = new CosmoDHelperPagina();
        helperPagina.setCodice(body.getCodicePagina().getCodice());
        helper.setCosmoDHelperPagina(helperPagina);
      }

      if (null != body.getCodiceTab() && !StringUtils.isBlank(body.getCodiceTab().getCodice())) {
        CosmoDHelperTab helperTab = new CosmoDHelperTab();
        helperTab.setCodice(body.getCodiceTab().getCodice());
        helper.setCosmoDHelperTab(helperTab);
      }

      if (null != body.getCodiceForm() && !StringUtils.isBlank(body.getCodiceForm().getCodice())) {
        CosmoDCustomFormFormio helperCustomForm = new CosmoDCustomFormFormio();
        helperCustomForm.setCodice(body.getCodiceForm().getCodice());
        helper.setCosmoDCustomFormFormio(helperCustomForm);
      }

      if (null != body.getCodiceModale()
          && !StringUtils.isBlank(body.getCodiceModale().getCodice())) {
        CosmoDHelperModale helperModale = new CosmoDHelperModale();
        helperModale.setCodice(body.getCodiceModale().getCodice());
        helper.setCosmoDHelperModale(helperModale);
      }

      helper.setHtml(body.getHtml());
      helper.setDtUltimaModifica(Timestamp.valueOf(LocalDateTime.now()));
      helper.setUtenteUltimaModifica(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      helper = cosmoTHelperRepository.save(helper);
    }

    return cosmoTHelperMapper.toDTO(helper);
  }

  private CosmoTHelper getHelperById(String id) {
    final String methodName = "getHelperById";

    if (StringUtils.isBlank(id) && !StringUtils.isNumeric(id)) {
      logger.error(methodName, ErrorMessages.HELPER_ID_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.HELPER_ID_NON_VALORIZZATO);
    }

    return cosmoTHelperRepository.findOneNotDeleted(Long.valueOf(id))
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.HELPER_ID_INESISTENTE, id)));
  }

  @Override
  public List<CodiceTab> getCodiciTab(String codicePagina) {
    List<CodiceTab> codiciTab = new LinkedList<>();
    List<CosmoDHelperTab> codiciTabDB =
        cosmoDHelperTabRepository.findAllActive((Root<CosmoDHelperTab> root, CriteriaQuery<?> cq,
            CriteriaBuilder cb) -> cb.and(cb.equal(
                root.get(CosmoDHelperTab_.cosmoDHelperPagina).get(CosmoDHelperPagina_.codice),
                codicePagina))
            );

    codiciTabDB.forEach(ct -> codiciTab.add(cosmoDHelperTabMapper.toDTO(ct)));
    return codiciTab;

  }

  @Override
  public List<CodicePagina> getCodiciPagina() {
    List<CodicePagina> codiciPagina = new LinkedList<>();
    List<CosmoDHelperPagina> codicePaginaDB = cosmoDHelperPaginaRepository.findAllActive();

    codicePaginaDB.forEach(cp -> codiciPagina.add(cosmoDHelperPaginaMapper.toDTO(cp)));
    return codiciPagina;
  }

  @Override
  public DecodificaHelper getDecodifica(String pagina, String tab, String form) {
    ValidationUtils.require(pagina, "pagina");
    DecodificaHelper dh = new DecodificaHelper();

    if (!StringUtils.isEmpty(form)) {
      var decodifica = cosmoDCustomFormFormioRepository.findOneActive(form);
      if (decodifica.isPresent()) {
        dh.setCodice(decodifica.get().getCodice());
        dh.setDescrizione(decodifica.get().getDescrizione());
      }
    } else if (!StringUtils.isEmpty(tab)) {
      var decodifica = cosmoDHelperTabRepository.findOneActive(tab);
      if (decodifica.isPresent()) {
        dh.setCodice(decodifica.get().getCodice());
        dh.setDescrizione(decodifica.get().getDescrizione());
      }
    } else {
      var decodifica = cosmoDHelperPaginaRepository.findOneActive(pagina);
      if (decodifica.isPresent()) {
        dh.setCodice(decodifica.get().getCodice());
        dh.setDescrizione(decodifica.get().getDescrizione());
      }
    }

    return dh;
  }

  @Override
  public List<CodiceModale> getModali(String filter) {
    List<CodiceModale> output = new ArrayList<>();

    GenericRicercaParametricaDTO<FiltroRicercaHelperModaleDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaHelperModaleDTO.class);

    ValidationUtils.require(ricercaParametrica.getFilter(), "filter");
    ValidationUtils.require(ricercaParametrica.getFilter().getCodicePagina(), "codice pagina helper");

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoDHelperModale> pageResult =
        cosmoDHelperModaleRepository.findAll(
            CosmoTHelperSpecifications
            .findModaliByFilters(ricercaParametrica.getFilter()), paging);

    List<CosmoDHelperModale> entitiesSuDB = pageResult.getContent();

    entitiesSuDB.forEach(e -> {
      var helperModaleDTO = cosmoDHelperModaleMapper.toDTO(e);
      helperModaleDTO.setCodicePagina(e.getCosmoDHelperPagina().getCodice());
      if (null != e.getCosmoDHelperTab()) {
        helperModaleDTO.setCodiceTab(e.getCosmoDHelperTab().getCodice());
      }
      output.add(helperModaleDTO);
    });

    return output;
  }


  @Override
  public byte[] getExport(String id) {
    final var methodName = "getExport";
    ValidationUtils.require(id, "id");
    logger.info(methodName, "Ricerca helper per id: %s", id);

    var helper = cosmoTHelperRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(() -> {
      logger.error(methodName, "Helper non presente per i dati passati");
      return new NotFoundException("Helper non presente per i dati passati");
    });

    logger.info(methodName, "Inizio esportazione in formato json per l'helper richiesto");
    try {
      logger.info(methodName, "Fine esportazione");
      return doExport(helper);
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }
  }


  private byte[] doExport(CosmoTHelper helper) throws Exception {

    Object helperCopy = applyExportPolicy(helper, CosmoTHelper.class, buildPolicyForHelperExport());

    Map<String, Object> export = new HashMap<>();
    export.put("helper", helperCopy);

    return ObjectUtils.toJson(export).getBytes(StandardCharsets.UTF_8);
  }

  public ExportPolicy<CosmoTHelper> buildPolicyForHelperExport() {

    var exportSpecForHelper = new ExportPolicy<CosmoTHelper>();

    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.helperPagina, ExportPolicy.clone(buildPolicyForHelperPaginaExport()));
    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.helperTab, ExportPolicy.clone(buildPolicyForHelperTabExport()));
    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.helperModale,
        ExportPolicy.clone(buildPolicyForHelperModaleExport()));
    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.helperForm,
        ExportPolicy.clone(buildPolicyForCustomFormExport()));
    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.html, ExportPolicy.copy());
    exportSpecForHelper.fieldsPolicy.put(CosmoTHelper_.id, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpecForHelper.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoTHelper_.class, exportSpecForHelper);

    return exportSpecForHelper;
  }

  public ExportPolicy<CosmoDCustomFormFormio> buildPolicyForCustomFormExport() {

    var exportSpecForTipoCustomForm = new ExportPolicy<CosmoDCustomFormFormio>();

    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.codice,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.descrizione,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.customForm,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.ignore());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoDTipoPratica,
        ExportPolicy.ignore());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoTHelpers,
        ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoDCustomFormFormio_.class, exportSpecForTipoCustomForm);

    exportSpecForTipoCustomForm =
        exportSpecForTipoCustomForm.withPreExport((entity, map) -> map.put("codiceTipoPratica",
            entity.getCosmoDTipoPratica() != null ? entity.getCosmoDTipoPratica().getCodice()
                : null));

    return exportSpecForTipoCustomForm;
  }

  public ExportPolicy<CosmoDHelperPagina> buildPolicyForHelperPaginaExport() {

    var exportSpecForHelperPagina = new ExportPolicy<CosmoDHelperPagina>();

    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDHelperPagina_.codice, ExportPolicy.copy());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDHelperPagina_.descrizione, ExportPolicy.copy());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDHelperPagina_.cosmoDHelperTabs, ExportPolicy.ignore());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDHelperPagina_.cosmoDHelperModales,
        ExportPolicy.ignore());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDHelperPagina_.cosmoTHelpers,
        ExportPolicy.ignore());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.ignore());
    exportSpecForHelperPagina.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoDHelperPagina_.class, exportSpecForHelperPagina);

    return exportSpecForHelperPagina;
  }

  public ExportPolicy<CosmoDHelperTab> buildPolicyForHelperTabExport() {

    var exportSpecForHelperTab = new ExportPolicy<CosmoDHelperTab>();

    exportSpecForHelperTab.fieldsPolicy.put(CosmoDHelperTab_.codice, ExportPolicy.copy());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDHelperTab_.descrizione, ExportPolicy.copy());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDHelperTab_.cosmoDHelperPagina, ExportPolicy.ignore());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDHelperTab_.cosmoDHelperModales,
        ExportPolicy.ignore());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDHelperPagina_.cosmoTHelpers,
        ExportPolicy.ignore());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.ignore());
    exportSpecForHelperTab.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoDHelperTab_.class, exportSpecForHelperTab);

    return exportSpecForHelperTab;
  }

  public ExportPolicy<CosmoDHelperModale> buildPolicyForHelperModaleExport() {

    var exportSpecForHelperModale = new ExportPolicy<CosmoDHelperModale>();

    exportSpecForHelperModale.fieldsPolicy.put(CosmoDHelperModale_.codice, ExportPolicy.copy());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDHelperModale_.descrizione,
        ExportPolicy.copy());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDHelperModale_.helperTab,
        ExportPolicy.ignore());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDHelperModale_.helperPagina,
        ExportPolicy.ignore());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDHelperModale_.cosmoTHelpers,
        ExportPolicy.ignore());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.ignore());
    exportSpecForHelperModale.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoDHelperModale_.class, exportSpecForHelperModale);
    return exportSpecForHelperModale;

  }

  private static class ExportPolicy<T> {
    String uuid = UUID.randomUUID().toString();
    boolean copy;
    boolean clone;
    HashMap<Attribute<?, ?>, ExportPolicy<?>> fieldsPolicy = new HashMap<>();
    BiConsumer<T, Map<String, Object>> preExport = null;

    public static <T> ExportPolicy<T> ignore() {
      ExportPolicy<T> out = new ExportPolicy<>();
      out.copy = false;
      out.clone = false;
      return out;
    }

    public static <T> ExportPolicy<T> copy() {
      ExportPolicy<T> out = new ExportPolicy<>();
      out.copy = true;
      out.clone = false;
      return out;
    }

    public ExportPolicy() {
      this.copy = false;
    }

    public static <T> ExportPolicy<T> clone(ExportPolicy<T> withPolicy) {
      if (!exportPolicyVerifications.containsKey(withPolicy.uuid)) {
        throw new BadConfigurationException("Cannot clone unverified policy: " + withPolicy);
      }
      ExportPolicy<T> out = new ExportPolicy<>();
      out.copy = false;
      out.clone = true;
      out.fieldsPolicy.putAll(withPolicy.fieldsPolicy);
      out.preExport = withPolicy.preExport;
      exportPolicyVerifications.put(out.uuid, true);
      return out;
    }

    public ExportPolicy<T> withPreExport(BiConsumer<T, Map<String, Object>> preExport) {
      this.preExport = preExport;
      return this;
    }
  }

  private <T> void checkPolicyIsComplete(Class<?> metamodelType, ExportPolicy<T> policy) {
    for (Field field : metamodelType.getDeclaredFields()) {
      var found = false;
      for (Attribute<?, ?> specifiedAttribute : policy.fieldsPolicy.keySet()) {
        if (specifiedAttribute.getName().equals(field.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new RuntimeException(
            "Invalid policy: missing specification for field " + field.getName());
      }
    }
    exportPolicyVerifications.put(policy.uuid, true);
  }

  @SuppressWarnings("unchecked")
  private <T extends Object> Object applyExportPolicy(Object root, Class<?> type,
      ExportPolicy<T> policy) throws Exception {

    if (policy.clone && !exportPolicyVerifications.containsKey(policy.uuid)) {
      throw new RuntimeException("Rejected unverified policy: " + policy);
    }



    Map<String, Object> copy = new HashMap<>();
    copy.put("_class", root.getClass().getName());



    for (Entry<Attribute<?, ?>, ExportPolicy<?>> fieldPolicy : policy.fieldsPolicy.entrySet()) {

      if (fieldPolicy.getValue().copy) {



        var value = FieldUtils.readField(root, fieldPolicy.getKey().getName(), true);
        if (fieldPolicy.getKey().getName().equalsIgnoreCase("html")) {
          value = value.toString().replaceAll("\"", "'");
        }
        if (value == null) {
          continue;
        }

        copy.put(fieldPolicy.getKey().getName(), value);

      } else if (fieldPolicy.getValue().clone) {

        var value = FieldUtils.readField(root, fieldPolicy.getKey().getName(), true);
        if (value == null) {
          continue;
        }
        value = initializeAndUnproxy(value);

        if (isCollection(value.getClass())) {

          // check orig type
          var field = root.getClass().getDeclaredField(fieldPolicy.getKey().getName());

          Collection<Object> clonedCollection = createCollectionFromType(field);

          for (Object item : (Collection<?>) value) {
            item = initializeAndUnproxy(item);

            var clonedItem = applyExportPolicy(item, item.getClass(), fieldPolicy.getValue());
            if (!isSoftDeleted(item)) {
              clonedCollection.add(clonedItem);
            }
          }

          copy.put(fieldPolicy.getKey().getName(), clonedCollection);


        } else {
          var cloned = applyExportPolicy(value, value.getClass(), fieldPolicy.getValue());
          if (!isSoftDeleted(value)) {
            copy.put(fieldPolicy.getKey().getName(), cloned);
          }

        }

      }

    }

    if (policy.preExport != null) {
      policy.preExport.accept((T) root, copy);
    }

    return copy;
  }

  @SuppressWarnings("unchecked")
  public <T> T initializeAndUnproxy(T entity) {
    if (entity == null) {
      return null;
    }

    if (entity instanceof HibernateProxy) {
      entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
    }
    return entity;
  }

  private boolean isCollection(Class<?> clazz) {
    return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
  }

  public Collection<Object> createCollectionFromType(Field field) {
    var fieldType = field.getType();
    if (List.class.isAssignableFrom(fieldType)) {
      return new ArrayList<>();
    }
    if (Set.class.isAssignableFrom(fieldType)) {
      return new HashSet<>();
    }
    throw new RuntimeException("Unexpected collecion class: " + fieldType.getName());
  }

  private boolean isSoftDeleted(Object entity) {
    entity = initializeAndUnproxy(entity);
    if (entity instanceof CosmoTEntity) {
      return ((CosmoTEntity) entity).cancellato();
    } else if (entity instanceof CosmoREntity) {
      return ((CosmoREntity) entity).nonValido();
    } else if (entity instanceof CosmoDEntity) {
      return ((CosmoDEntity) entity).nonValido();
    }
    return false;
  }

  @Override
  public HelperImportResult postImport(HelperImportRequest body) {
    final var methodName = "postImport";
    ValidationUtils.require(body, "body");
    HelperImportResult hir = new HelperImportResult();
    hir.setStatus("OK");
    logger.info(methodName, "Inizio import helper");
    try {
      doImport(body);
    } catch (Exception e) {
      hir.setStatus("KO");
      hir.setReason(e.getMessage());
    }
    logger.info(methodName, String.format("Fine import helper con risultato: %s", hir.getStatus()));
    return hir;

  }

  private void doImport(HelperImportRequest body) throws Exception {
    final var methodName = "doImport";
    logger.info(methodName, "Lettura file json contenente l'helper da importare");

    JsonNode input = ObjectUtils.fromJson(new String(body.getFile(), StandardCharsets.UTF_8), JsonNode.class);

    JsonNode helper = input.get("helper");
    if (helper == null) {
      logger.error(methodName, "formattazione file non corretta");
      throw new BadRequestException("Il file fornito non e' un export helper valido");
    }

    var codicePagina = helper.get("helperPagina");
    var helperPagina = cosmoDHelperPaginaRepository
        .findOneActiveByField(CosmoDHelperPagina_.codice, formatJsonField(codicePagina.get(CODICE)))
        .orElseThrow(() -> {
          String message = "Non esiste una pagina con codice "
              + formatJsonField(codicePagina.get(CODICE)) + " per cui creare un helper";
          logger.error(methodName, message);
          throw new BadRequestException(message);
        });

    var codiceTab = helper.get("helperTab");
    CosmoDHelperTab helperTab = null;
    if (null != codiceTab) {
      helperTab = cosmoDHelperTabRepository.findOneActiveByField(CosmoDHelperTab_.codice, formatJsonField(codiceTab.get(CODICE)))
          .orElseThrow(() -> {
            String message = "Non esiste un tab con codice " + formatJsonField(codiceTab.get(CODICE))
            + " per cui creare un helper";
            logger.error(methodName, message);
            throw new BadRequestException(message);
          });

      if (!helperTab.getCosmoDHelperPagina().getCodice().equals(helperPagina.getCodice())) {
        String message = "Non esiste un tab con codice " + formatJsonField(codiceTab.get(CODICE))
        + " associato alla pagina con codice " + formatJsonField(codicePagina.get(CODICE))
        + " per cui creare un helper";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }
    }

    var codiceModale = helper.get("helperModale");
    CosmoDHelperModale helperModale = null;
    if (null != codiceModale) {
      helperModale = cosmoDHelperModaleRepository.findOneActiveByField(CosmoDHelperModale_.codice,
          formatJsonField(codiceModale.get(CODICE))).orElseThrow(() -> {
            String message = "Non esiste un modale con codice "
                + formatJsonField(codiceModale.get(CODICE)) + " per cui creare un helper";
            logger.error(methodName, message);
            throw new BadRequestException(message);
          });

      if (null == codiceTab) {
        if (!helperModale.getCosmoDHelperPagina().getCodice().equals(helperPagina.getCodice())) {
          String message =
              "Non esiste un modale con codice " + formatJsonField(codiceModale.get(CODICE))
              + " associato alla pagina con codice " + formatJsonField(codicePagina.get(CODICE))
              + " per cui creare un helper";
          logger.error(methodName, message);
          throw new BadRequestException(message);
        }
      } else {
        if (!helperModale.getCosmoDHelperPagina().getCodice().equals(helperPagina.getCodice())
            && !helperModale.getCosmoDHelperTab().getCodice().equals(helperTab.getCodice())) {
          String message = "Non esiste un modale tab con codice "
              + formatJsonField(codiceModale.get(CODICE)) + " associato alla pagina con codice "
              + formatJsonField(codicePagina.get(CODICE)) + " e associato alla tab con codice "
              + formatJsonField(codiceTab.get(CODICE)) + " per cui creare un helper";
          logger.error(methodName, message);
          throw new BadRequestException(message);
        }
      }
    }

    var codiceForm = helper.get("helperForm");
    Optional<CosmoDCustomFormFormio> helperCustomForm = Optional.empty();
    if (null != codiceForm) {
      helperCustomForm = cosmoDCustomFormFormioRepository.findOneActiveByField(CosmoDCustomFormFormio_.codice, formatJsonField(codiceForm.get(CODICE)));
    }
    var form = helperCustomForm.isPresent() ? helperCustomForm.get() : null;

    var findHelper = cosmoTHelperRepository
        .findOneNotDeleted((Root<CosmoTHelper> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
          Predicate p =  cb.and(
              cb.equal(root.get(CosmoTHelper_.helperPagina).get(CosmoDHelperPagina_.codice),
                  formatJsonField(codicePagina.get(CODICE))));
          if (null != codiceTab) {
            p = cb.and(p, cb.equal(root.get(CosmoTHelper_.helperTab).get(CosmoDHelperTab_.codice),
                formatJsonField(codiceTab.get(CODICE))));
          }

          if (null != codiceModale) {
            p = cb.and(p,
                cb.equal(root.get(CosmoTHelper_.helperModale).get(CosmoDHelperModale_.codice),
                    formatJsonField(codiceModale.get(CODICE))));
          }

          if (null != codiceForm) {
            p = cb.and(p,
                cb.equal(root.get(CosmoTHelper_.helperForm).get(CosmoDCustomFormFormio_.codice),
                    formatJsonField(codiceForm.get(CODICE))));
          }
          return p;
        });
    logger.info(methodName, "L'helper e' gia' presente a sistema? " + findHelper.isPresent());

    if (findHelper.isPresent()) {
      logger.info(methodName, "Inizio aggiornamento helper");

      var root = findHelper.get();
      root.setHtml(formatJsonField(helper.get("html")));
      cosmoTHelperRepository.save(root);

      if (null != form) {
        form.setDescrizione(formatJsonField(codiceForm.get(DESCRIZIONE)));
        String customFormJsonString = replaceForm(formatJsonField(codiceForm.get(CUSTOM_FORM)));
        form.setCustomForm(customFormJsonString);
        cosmoDCustomFormFormioRepository.save(form);
      }

    } else {
      logger.info(methodName, "Inizio inserimento helper");

      if (null != codiceForm) {

        if (null == form) {
          form = new CosmoDCustomFormFormio();
          form.setCodice(formatJsonField(codiceForm.get(CODICE)));
          form.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
        }

        form.setDescrizione(formatJsonField(codiceForm.get(DESCRIZIONE)));

        String customFormJsonString = replaceForm(formatJsonField(codiceForm.get(CUSTOM_FORM)));
        form.setCustomForm(customFormJsonString);
        form = cosmoDCustomFormFormioRepository.save(form);
      }


      CosmoTHelper nuovoHelper = new CosmoTHelper();
      nuovoHelper.setCosmoDHelperPagina(helperPagina);
      nuovoHelper.setCosmoDHelperTab(helperTab);
      nuovoHelper.setCosmoDHelperModale(helperModale);
      nuovoHelper.setCosmoDCustomFormFormio(form);
      nuovoHelper.setHtml(formatJsonField(helper.get("html")));
      cosmoTHelperRepository.save(nuovoHelper);
    }

  }

  private String replaceForm(String input) {
    return input.replace("\\n", "").replace("\\\"", "\"").replace("\\\"", "/\"").replace("\\", "")
        .replace("/\"", "\\\"");
  }

  private String formatJsonField(JsonNode jsonField) {
    return jsonField.toString().replaceAll("(^\")|(\"$)", "");
  }

  private void checkParametriHelper(Helper body) {
    if (!StringUtils.isBlank(body.getCodicePagina().getCodice())) {
      cosmoDHelperPaginaRepository.findOneActiveByField(CosmoDHelperPagina_.codice, body.getCodicePagina().getCodice()).orElseThrow(
          () -> new NotFoundException(String.format(ErrorMessages.HELPER_PAGINA_NON_TROVATO, body.getCodicePagina().getCodice())));
    }

    if(body.getCodiceTab() != null && body.getCodiceTab().getCodice() != null && !StringUtils.isBlank(body.getCodiceTab().getCodice())) {
      cosmoDHelperTabRepository.findOneActiveByField(CosmoDHelperTab_.codice, body.getCodiceTab().getCodice()).orElseThrow(
          () -> new NotFoundException(String.format(ErrorMessages.HELPER_TAB_NON_TROVATO, body.getCodiceTab().getCodice())));
    }

    if(body.getCodiceModale() != null && body.getCodiceModale().getCodice() != null && !StringUtils.isBlank(body.getCodiceModale().getCodice())) {
      cosmoDHelperModaleRepository.findOneActiveByField(CosmoDHelperModale_.codice, body.getCodiceModale().getCodice()).orElseThrow(
          () -> new NotFoundException(String.format(ErrorMessages.HELPER_MODALE_NON_TROVATO, body.getCodiceModale().getCodice())));
    }

    if(body.getCodiceForm() != null && !StringUtils.isBlank(body.getCodiceForm().getCodice())) {
      cosmoDCustomFormFormioRepository.findOneActiveByField(CosmoDCustomFormFormio_.codice, body.getCodiceForm().getCodice()).orElseThrow(
          () -> new NotFoundException(String.format(ErrorMessages.HELPER_CUSTOM_FORM_NON_TROVATO, body.getCodiceForm().getCodice())));
    }
  }
}
