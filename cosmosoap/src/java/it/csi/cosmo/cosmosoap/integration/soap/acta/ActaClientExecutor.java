/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.acta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ComplexListComparator.Couple;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmosoap.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmosoap.dto.acta.TipologieAggregazione;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaAccessDeniedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModificationFailedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaQueryFailedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaRuntimeException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaTooManyResultsException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.AggregazioneICEViewActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ClassificazioneActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoFisicoActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoSempliceActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.FascicoloRealeAnnualeActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ProtocolloActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.RegistrazioneClassificazioniActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.SerieFascicoliActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.SerieTipologicaDocumentiDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.TitolarioActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.VoceTitolarioActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.VolumeSerieTipologicaDocumentiDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.mapper.ActaPayloadMapper;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.AggregazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ClassificazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoSempliceActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FascicoloRealeAnnualeActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ProtocolloActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.RegistrazioneClassificazioniActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieFascicoliActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieTipologicaDocumenti;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.TitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VoceTitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VolumeSerieTipologicaDocumenti;
import it.csi.cosmo.cosmosoap.integration.soap.acta.object.AcarisException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.parser.ActaBuilder;
import it.csi.cosmo.cosmosoap.integration.soap.acta.parser.ActaContentConverter;
import it.csi.cosmo.cosmosoap.integration.soap.acta.parser.ActaParser;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.archive.EnumArchiveObjectType;
import it.doqui.acta.acaris.archive.EnumFolderObjectType;
import it.doqui.acta.acaris.archive.EnumRelationshipDirectionType;
import it.doqui.acta.acaris.archive.EnumRelationshipObjectType;
import it.doqui.acta.acaris.backoffice.ClientApplicationInfo;
import it.doqui.acta.acaris.backoffice.PrincipalExtResponseType;
import it.doqui.acta.acaris.common.AnnotazioniPropertiesType;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.EnumPropertyFilter;
import it.doqui.acta.acaris.common.EnumQueryOperator;
import it.doqui.acta.acaris.common.EnumStreamId;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.ObjectResponseType;
import it.doqui.acta.acaris.common.PagingResponseType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertiesType;
import it.doqui.acta.acaris.common.PropertyFilterType;
import it.doqui.acta.acaris.common.PropertyType;
import it.doqui.acta.acaris.common.QueryConditionType;
import it.doqui.acta.acaris.common.QueryableObjectType;
import it.doqui.acta.acaris.documentservice.EnumTipoOperazione;
import it.doqui.acta.acaris.documentservice.IdentificatoreDocumento;
import it.doqui.acta.acaris.documentservice.InfoRichiestaCreazione;

public class ActaClientExecutor {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaClientExecutor.class.getSimpleName());

  private ActaClientContext contesto;

  private ActaProvider facade;

  private static final String OBJECT_ID = "objectId";
  private static final String DTO_ACARIS_TYPE_ARCHIVE_PATH = "it.doqui.acta.actasrv.dto.acaris.type.archive.";
  private static final String GET_CHANGED_PROPERTIES = "getChangedProperties";
  private static final String LOAD_ENTITY = "loadEntity(";
  private static final String DOCUMENTO_SEMPLICE_PROPERTIES_TYPE = "DocumentoSemplicePropertiesType";

  @Generated("SparkTools")
  private ActaClientExecutor(Builder builder) {
    contesto = builder.contesto;
    facade = builder.facade;
  }

  public <T extends EntitaActa> T find(String id, Class<T> targetClass) {
    return loadEntity(OBJECT_ID, id, targetClass);
  }

  public <T extends EntitaActa> T findBy(String property, String value, Class<T> targetClass) {
    return loadEntity(property, value, targetClass);
  }

  public <T extends EntitaActa> T findBy(String property, EnumQueryOperator operator, String value,
      Class<T> targetClass) {
    return loadEntity(property, operator, value, targetClass);
  }

  @SuppressWarnings("unchecked")
  public <T extends EntitaActa> T update(T entity) {

    ObjectIdType repositoryId = new ObjectIdType();
    repositoryId.setValue(contesto.getRepository());

    PrincipalIdType principalId = new PrincipalIdType();
    principalId.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    String modelClassName = ActaParser.findActaModelClassNameForClass(entity.getClass());

    Class<? extends PropertiesType> targetClass;
    try {
      targetClass = (Class<? extends PropertiesType>) Class
          .forName(DTO_ACARIS_TYPE_ARCHIVE_PATH + modelClassName);
    } catch (ActaModelTranslationException | ClassNotFoundException e1) {
      throw new ActaModelTranslationException(
          "Could not create class for model " + entity.getClass().getName(), e1);
    }

    List<PropertyType> propertiesActa =
        ActaBuilder.actaEntityToPropertiesArray(contesto, entity, targetClass);

    List<PropertyType> propertiesToApply =
        getChangedProperties(propertiesActa, modelClassName, entity.getId());

    try {
      if (propertiesToApply.isEmpty()) {

        log.debug("update", "requested update but no property changed. Skipping service call");
        return entity;

      } else {

        ActaClientHelper.logObject("UPDATING PROPERTIES: ID",
            ActaClientHelper.wrapId(entity.getId()));
        ActaClientHelper.logObject("UPDATING PROPERTIES: CHANGE TOKEN",
            ActaClientHelper.getChangeToken(entity));
        ActaClientHelper.logObject("UPDATING PROPERTIES: PROPERTIES", propertiesToApply);

        // simpleResponse =
        facade.getObjectService().updateProperties(repositoryId,
            ActaClientHelper.wrapId(entity.getId()), principalId,
            ActaClientHelper.getChangeToken(entity), propertiesToApply);
      }

    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, "updating document properties");
    }

    return (T) find(entity.getId(), entity.getClass());

  }

  private List<PropertyType> getChangedProperties(List<PropertyType> localProperties,
      String modelClassName, String objectId) {

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName(OBJECT_ID);
    criterion.setValue(objectId);
    criterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(criterion);

    List<PropertyType> propertiesOnActa;
    ObjectResponseType propertiesEntitaAttuale = loadProperties(criteria, modelClassName);
    if (propertiesEntitaAttuale == null) {
      throw new ActaModificationFailedException(
          "Entity not existing on ACTA repository: " + objectId);
    } else {
      propertiesOnActa = propertiesEntitaAttuale.getProperties();
    }

    Predicate<PropertyType> isEmpty = pt -> pt == null || pt.getValue() == null
        || pt.getValue().getContent() == null || pt.getValue().getContent().isEmpty()
        || (pt.getValue().getContent().size() == 1 && (pt.getValue().getContent().get(0) == null
        || pt.getValue().getContent().get(0).isEmpty()));

    BiPredicate<PropertyType, PropertyType> listMatcher = (pt1, pt2) ->
          pt1.getQueryName().getPropertyName().equals(pt2.getQueryName().getPropertyName())
          && pt1.getQueryName().getClassName().equals(pt2.getQueryName().getClassName());

    Predicate<Couple<PropertyType, PropertyType>> areEqualsLists = coppia -> {
      if (isEmpty.test(coppia.getFirst()) && isEmpty.test(coppia.getSecond())) {
        return false;
      }

      boolean equalLists = coppia.getFirst().getValue().getContent().size() == coppia.getSecond()
          .getValue().getContent().size()
          && coppia.getFirst().getValue().getContent()
              .containsAll(coppia.getSecond().getValue().getContent());
      return !equalLists;
    };

    Function<Couple<PropertyType, PropertyType>, PropertyType> mappingListsValues = coppia -> {
      log.info(GET_CHANGED_PROPERTIES,
          "PROPERTY CHANGED: " + coppia.getFirst().getQueryName().getPropertyName());
      log.info(GET_CHANGED_PROPERTIES,
          "LOCAL: " + Arrays.asList(coppia.getFirst().getValue().getContent()));
      log.info(GET_CHANGED_PROPERTIES,
          "REMOTE: " + Arrays.asList(coppia.getSecond().getValue().getContent()));

      return coppia.getFirst();
    };

    return ComplexListComparator.compareLists(localProperties, propertiesOnActa, listMatcher)
        .getElementsInBoth().stream().filter(areEqualsLists)
        .map(mappingListsValues)
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  public <T extends DocumentoActa> T create(String parentId, T payload) {

    ObjectIdType repositoryId = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalId =
        ActaClientHelper.wrapPrincipal(ActaClientHelper.getValidPrincipal(facade, contesto));

    IdentificatoreDocumento documentId;


    Class<? extends PropertiesType> targetClass;
    try {
      targetClass = (Class<? extends PropertiesType>) Class
          .forName(DTO_ACARIS_TYPE_ARCHIVE_PATH
              + ActaParser.findActaModelClassNameForClass(payload.getClass()));
    } catch (ActaModelTranslationException | ClassNotFoundException e1) {
      throw new ActaModelTranslationException(
          "Could not create class for model " + payload.getClass().getName(), e1);
    }

    PropertiesType propertiesActa =
        ActaBuilder.actaEntityToProperties(contesto, payload, targetClass);

    InfoRichiestaCreazione datiCreazione =
        ActaPayloadMapper.map(parentId, payload, propertiesActa, contesto);

    try {
      documentId = facade.getDocumentService().creaDocumento(repositoryId, principalId,
          EnumTipoOperazione.ELETTRONICO, datiCreazione);
    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, "updating document properties",
          e2 -> new ActaModificationFailedException("Error creating document", e2));
    }

    T created = (T) find(documentId.getObjectIdDocumento().getValue(), payload.getClass());
    created.setClassificazionePrincipale(documentId.getObjectIdClassificazione());

    return created;
  }

  @SuppressWarnings("unchecked")
  public <T extends AggregazioneActa> T create(String parentId, T entity) {

    ObjectIdType repositoryId = ActaClientHelper.wrapId(contesto.getRepository());
    PrincipalIdType principalId =
        ActaClientHelper.wrapPrincipal(ActaClientHelper.getValidPrincipal(facade, contesto));
    ObjectIdType parentIdWrapper = ActaClientHelper.wrapId(parentId);

    EnumFolderObjectType targetEnumType;
    PropertiesType properties;

    ObjectIdType ret = null;

    try {
      String modelName = ActaParser.findActaModelClassNameForClass(entity.getClass());
      targetEnumType = EnumFolderObjectType.fromValue(modelName);
      ActaClientHelper.logObject("create: targetEnumType", targetEnumType);

      Class<? extends PropertiesType> targetClass = (Class<? extends PropertiesType>) Class
          .forName(DTO_ACARIS_TYPE_ARCHIVE_PATH + modelName);
      properties = ActaBuilder.actaEntityToProperties(contesto, entity, targetClass);

      ActaClientHelper.logObject("create: properties", properties);

    } catch (Exception e) {
      log.error("create", "Error creating payload: " + e.getMessage(), e);
      throw new ActaModelTranslationException("Error creating payload: " + e.getMessage(), e);
    }

    try {
      ret = facade.getObjectService().createFolder(repositoryId, targetEnumType, principalId,
          properties, parentIdWrapper);

      return (T) loadEntity(OBJECT_ID, ret.getValue(), entity.getClass());

    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, "creating payload",
          e2 -> new ActaModificationFailedException("Error creating item: " + e.getMessage(), e2));
    }
  }

  public <T extends EntitaActa> T loadEntity(String property, String value, Class<T> targetClass) {
    return loadEntity(property, EnumQueryOperator.EQUALS, value, targetClass);
  }

  public <T extends EntitaActa> T loadEntity(String property, EnumQueryOperator operator,
      String value, Class<T> targetClass) {

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName(property);
    criterion.setValue(value);
    criterion.setOperator(operator);
    criteria.add(criterion);

    return loadEntity(criteria, targetClass);
  }

  public <T extends EntitaActa> T loadEntity(List<QueryConditionType> criteria,
      Class<T> targetClass) {

    final var methodName = "loadEntity";
    String modelClassName = ActaParser.findActaModelClassNameForClass(targetClass);

    try {
      ObjectResponseType properties = loadProperties(criteria, modelClassName);
      if (properties == null) {
        return null;
      } else {
        return ActaParser.actaPropertiesToEntity(contesto, properties.getProperties(), targetClass);
      }
    } catch (ActaRuntimeException e) {
      throw e;
    } catch (Exception e) {
      log.error(methodName, LOAD_ENTITY + modelClassName + "): error querying: " + e);
      throw new ActaQueryFailedException(LOAD_ENTITY + modelClassName + "): error querying", e);
    }
  }


  public ObjectResponseType loadProperties(List<QueryConditionType> criteria,
      String modelClassName) {

    ObjectIdType repositoryId = ActaClientHelper.wrapId(contesto.getRepository());

    QueryableObjectType target = new QueryableObjectType();
    target.setObject(modelClassName);
    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    PagingResponseType response;

    try {
      PrincipalIdType principalId = new PrincipalIdType();
      principalId.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

      response = facade.getObjectService().query(repositoryId, principalId, target, filter,
          criteria, null, 2, 0);

      if (response.getObjects().size() > 1) {
        ActaClientHelper.logObject(LOAD_ENTITY + modelClassName + ")", response);

        throw new ActaTooManyResultsException("Requested findOne on loadEntity(" + modelClassName
            + ") but got " + response.getObjects().size() + " results");
      } else if (response.getObjects().size() == 1) {
        ActaClientHelper.logObject("LOADED from loadEntity(" + modelClassName + ")", response);

        return response.getObjects().get(0);
      } else {
        return null;
      }

    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, LOAD_ENTITY + modelClassName + ")",
          e2 -> new ActaQueryFailedException("error loading entity " + modelClassName, e2));
    }
  }

  public FascicoloRealeAnnualeActa findFascicoloRealeAnnualeById(String id) {
    return loadEntity(OBJECT_ID, id, FascicoloRealeAnnualeActaDefaultImpl.class);
  }

  public SerieFascicoliActa findSerieFascicoliByCodice(String codice) {
    return loadEntity("codice", codice, SerieFascicoliActaDefaultImpl.class);
  }

  public VoceTitolarioActa findVoceTitolarioByClassificazioneEstesa(String classificazione) {
    return loadEntity("indiceClassificazioneEstesa", classificazione,
        VoceTitolarioActaDefaultImpl.class);
  }

  public SerieTipologicaDocumenti findSerieTipologicaDocumentiByCodice(String codice) {
    return findSerieTipologicaDocumentiByCodice(codice, SerieTipologicaDocumentiDefaultImpl.class);
  }

  public <T extends SerieTipologicaDocumenti> T findSerieTipologicaDocumentiByCodice(String codice,
      Class<T> targetClass) {
    return loadEntity("codice", codice, targetClass);
  }

  public VolumeSerieTipologicaDocumenti findVolumeSerieTipologicaDocumentiByParoleChiave(
      String paroleChiave) {
    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName("paroleChiave");
    criterion.setValue(paroleChiave);
    criterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(criterion);
    QueryConditionType statusCriterion = new QueryConditionType();
    statusCriterion.setPropertyName("stato");
    statusCriterion.setValue("1");
    statusCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(statusCriterion);
    return loadEntity(criteria, VolumeSerieTipologicaDocumentiDefaultImpl.class);
  }

  public void creaAnnotazioni(String documentId, List<AnnotazioniPropertiesType> annotazioni)
      throws Exception {
    ObjectIdType repositoryId = new ObjectIdType();
    repositoryId.setValue(contesto.getRepository());
    ObjectIdType objectId = new ObjectIdType();
    objectId.setValue(documentId);
    PrincipalIdType principalId = new PrincipalIdType();
    principalId.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));
    for (AnnotazioniPropertiesType annotazione : annotazioni) {
      facade.getManagementService().addAnnotazioni(repositoryId, objectId, principalId,
          annotazione);
    }

  }

  /**
   * @param id
   * @return
   * @throws AcarisException
   */
  public List<DocumentoFisicoActa> findDocumentiFisiciByIdDocumentoSemplice(String id)
      throws Exception {
    List<DocumentoFisicoActa> output = new ArrayList<>();

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    PrincipalIdType principalId = new PrincipalIdType();
    principalId.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    ObjectIdType repositoryId = ActaClientHelper.wrapId(contesto.getRepository());

    var relResult = facade.getRelationshipsService().getObjectRelationships(repositoryId,
        principalId, ActaClientHelper.wrapId(id),
        EnumRelationshipObjectType.DOCUMENT_COMPOSITION_PROPERTIES_TYPE,
        EnumRelationshipDirectionType.SOURCE, filter);

    for (var rel : relResult) {
      if (!rel.getSourceType().equals(EnumArchiveObjectType.DOCUMENTO_SEMPLICE_PROPERTIES_TYPE)
          || !rel.getTargetType().equals(EnumArchiveObjectType.DOCUMENTO_FISICO_PROPERTIES_TYPE)
          || !rel.getSourceId().getValue().equals(id)) {
        continue;
      }

      var docFisicoId = rel.getTargetId();

      var properties =
          facade.getObjectService().getProperties(repositoryId, docFisicoId, principalId, filter);

      var documentoFisico = ActaParser.actaPropertiesToEntity(this.contesto,
          properties.getProperties(), DocumentoFisicoActaDefaultImpl.class);

      documentoFisico.setId(docFisicoId.getValue());

      output.add(documentoFisico);
    }

    return output;
  }

  /**
   * @param id
   * @return
   * @throws AcarisException
   */
  public ContenutoDocumentoFisicoActa findContenutoPrimarioByIdDocumentoFisico(String id)
      throws AcarisException {

    ObjectIdType repositoryId = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalId = new PrincipalIdType();
    principalId.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    var contentStream = facade.getObjectService().getContentStream(repositoryId,
        ActaClientHelper.wrapId(id), principalId, EnumStreamId.PRIMARY);

    return !contentStream.isEmpty() ? ActaContentConverter.acarisContentStreamTypeToContenutoFisico(contentStream.get(0)) : null;
  }

  /**
   * @param paroleChiave
   * @return
   * @throws AcarisException
   */
  public List<DocumentoSempliceActa> findDocumentiSempliciByParolaChiave(String paroleChiave,
      int maxItems, int skipCount) throws AcarisException {

    var objectServicePort = facade.getObjectService();

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    QueryableObjectType target = new QueryableObjectType();
    target.setObject(DOCUMENTO_SEMPLICE_PROPERTIES_TYPE);

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    List<QueryConditionType> criteria =
        ActaClientHelper.getCriteria(new EnumQueryOperator[] {EnumQueryOperator.LIKE},
            new String[] {"paroleChiave"}, new String[] {paroleChiave});

    // con queste impostazioni la paginazione e' gestita direttamente dal sistema
    PagingResponseType pagingResponse;

    // chiamata verso acta
    pagingResponse = objectServicePort.query(repositoryIdType, principalIdType, target, filter,
        criteria, null, maxItems, skipCount);

    List<DocumentoSempliceActa> output = new ArrayList<>();

    for (ObjectResponseType rawObject : pagingResponse.getObjects()) {
      var converted = ActaParser.actaPropertiesToEntity(null, rawObject.getProperties(),
          DocumentoSempliceActaDefaultImpl.class);
      output.add(converted);
    }

    return output;
  }

  /**
   * @param filtri
   * @return
   * @throws AcarisException
   */
  public Page<DocumentoSempliceActa> findDocumentiSemplici(FiltroRicercaDocumentiActaDTO filtri,
      Pageable pageable) {
    String method = "findDocumentiSemplici";

    var objectServicePort = facade.getObjectService();

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    QueryableObjectType target = new QueryableObjectType();
    target.setObject(DOCUMENTO_SEMPLICE_PROPERTIES_TYPE);

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    List<QueryConditionType> criteria = ActaClientHelper.criteriaFromFilters(filtri);

    int maxItems = pageable.getPageSize();
    int skipCount = pageable.getOffset();

    // con queste impostazioni la paginazione e' gestita direttamente dal sistema
    PagingResponseType pagingResponse;

    if (log != null) {
      log.info(method, "eseguo ricerca verso acta con i seguenti parametri:");
      log.info(method, "\t repositoryId = " + ObjectUtils.toJson(repositoryIdType));
      log.info(method, "\t principalId = " + ObjectUtils.toJson(principalIdType));
      log.info(method, "\t target = " + ObjectUtils.toJson(target));
      log.info(method, "\t filter = " + ObjectUtils.toJson(filter));
      log.info(method, "\t criteria = " + ObjectUtils.toJson(criteria));
      log.info(method, "\t navigationLimits = null");
      log.info(method, "\t maxItems = " + ObjectUtils.toJson(maxItems));
      log.info(method, "\t skipCount = " + ObjectUtils.toJson(skipCount));
    }

    // chiamata verso acta
    try {
      pagingResponse = objectServicePort.query(repositoryIdType, principalIdType, target, filter,
          criteria, null, maxItems, skipCount);
    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, "findDocumentiSemplici");
    }

    if (null != log && log.isDebugEnabled()) {
      log.debug(method, "la ricerca verso acta ha restituito i seguenti risultati:");
      log.debug(method, "\t objects = " + ObjectUtils.represent(pagingResponse.getObjects()));
    }

    List<DocumentoSempliceActa> list = new ArrayList<>();

    for (ObjectResponseType rawObject : pagingResponse.getObjects()) {
      var converted = ActaParser.actaPropertiesToEntity(null, rawObject.getProperties(),
          DocumentoSempliceActaDefaultImpl.class);
      list.add(converted);
    }

    int totalSizeEstimate = list.size();
    if (pagingResponse.isHasMoreItems()) {
      totalSizeEstimate = (pageable.getPageSize() * (pageable.getPageNumber() + 1)) + 1;
    }

    return new PageImpl<>(list, pageable, totalSizeEstimate);
  }

  /**
   * @param codiceFiscale
   * @return
   * @throws Exception
   */
  public List<IdentitaActa> findIdentitaByCodiceFiscaleUtente(String codiceFiscale) throws Exception {

    var backofficeServicePort = facade.getBackofficeService();

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    IdAOOType idAOOType = null;
    IdStrutturaType idStrutturaType = null;
    IdNodoType idNodoType = null;

    ClientApplicationInfo clientApplicationInfo = new ClientApplicationInfo();
    clientApplicationInfo.setAppKey(contesto.getApplicationInfo());

    CodiceFiscaleType codiceFiscaleType = new CodiceFiscaleType();
    codiceFiscaleType.setValue(codiceFiscale);

    List<PrincipalExtResponseType> principals =
        backofficeServicePort.getPrincipalExt(repositoryIdType, codiceFiscaleType, idAOOType,
            idStrutturaType, idNodoType, clientApplicationInfo);

    List<IdentitaActa> output = new ArrayList<>();
    for (var principal : principals) {
      //@formatter:off
      output.add(IdentitaActa.builder()
          .withId(principal.getPrincipalId().getValue())
          .withIdentificativoAOO(String.valueOf(principal.getUtente().getAoo().getIdentificatore()))
          .withCodiceAOO(principal.getUtente().getAoo().getCodice())
          .withDescrizioneAOO(principal.getUtente().getAoo().getDescrizione())
          .withIdentificativoNodo(String.valueOf(principal.getUtente().getNodo().getIdentificatore()))
          .withCodiceNodo(principal.getUtente().getNodo().getCodice())
          .withDescrizioneNodo(principal.getUtente().getNodo().getDescrizione())
          .withIdentificativoStruttura(String.valueOf(principal.getUtente().getStruttura().getIdentificatore()))
          .withCodiceStruttura(principal.getUtente().getStruttura().getCodice())
          .withDescrizioneStruttura(principal.getUtente().getStruttura().getDescrizione())
          .build());
      //@formatter:on
    }
    return output;
  }

  /**
   * @param id
   * @return
   * @throws it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException
   */
  public List<ClassificazioneActa> findClassificazioniByIdDocumentoSemplice(String id)
      throws it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException {

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    var parents = this.facade.getNavigationService()
        .getObjectParents(repositoryIdType, ActaClientHelper.wrapId(id), principalIdType, filter)
        .stream()
        .filter(candidate -> candidate.getProperties().stream()
            .anyMatch(p -> OBJECT_ID.equals(p.getQueryName().getPropertyName())
                && "ClassificazionePropertiesType".equals(p.getQueryName().getClassName())))
        .collect(Collectors.toList());

    return parents.stream().map(e -> ActaParser.actaPropertiesToEntity(contesto, e.getProperties(),
        ClassificazioneActaDefaultImpl.class)).collect(Collectors.toList());

  }

  /**
   * @param id
   * @return
   * @throws AcarisException
   */
  public ProtocolloActa findProtocolloById(String id) throws AcarisException {

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("ProtocolloPropertiesType");

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterio = new QueryConditionType();

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    criterio.setOperator(EnumQueryOperator.EQUALS);
    criterio.setPropertyName(OBJECT_ID);
    criterio.setValue(id);
    criteria.add(criterio);

    var queryResponse = facade.getObjectService().query(repositoryIdType, principalIdType, target,
        filter, criteria, null, 2, 0);

    var numResults = queryResponse.getObjects().size();
    if (numResults < 1) {
      return null;
    } else if (numResults > 1) {
      throw new ActaTooManyResultsException("Troppi protocolli associati ad id " + id);
    }

    return ActaParser.actaPropertiesToEntity(contesto,
        queryResponse.getObjects().get(0).getProperties(), ProtocolloActaDefaultImpl.class);
  }

  /**
   * @param filtri
   * @param pageable
   * @return
   */
  public Page<RegistrazioneClassificazioniActa> findRegistrazioni(
      FiltroRicercaDocumentiActaDTO filtri, Pageable pageable) {

    String method = "findRegistrazioni";

    var servicePort = facade.getOfficialBookService();

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("RegistrazioneClassificazioniView");

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    List<QueryConditionType> criteria = ActaClientHelper.criteriaFromFilters(filtri);

    int maxItems = pageable.getPageSize();
    int skipCount = pageable.getOffset();

    // con queste impostazioni la paginazione e' gestita direttamente dal sistema
    PagingResponseType pagingResponse;

    if (log != null) {
      log.info(method, "eseguo ricerca verso acta con i seguenti parametri:");
      log.info(method, "\t repositoryId = " + ObjectUtils.toJson(repositoryIdType));
      log.info(method, "\t principalId = " + ObjectUtils.toJson(principalIdType));
      log.info(method, "\t target = " + ObjectUtils.toJson(target));
      log.info(method, "\t filter = " + ObjectUtils.toJson(filter));
      log.info(method, "\t criteria = " + ObjectUtils.toJson(criteria));
      log.info(method, "\t navigationLimits = null");
      log.info(method, "\t maxItems = " + ObjectUtils.toJson(maxItems));
      log.info(method, "\t skipCount = " + ObjectUtils.toJson(skipCount));
    }

    // chiamata verso acta
    try {
      pagingResponse = servicePort.query(repositoryIdType, principalIdType, target, filter,
          criteria, null, maxItems, skipCount);
    } catch (Exception e) {
      throw ActaClientHelper.wrapException(e, method);
    }

    if (null != log && log.isDebugEnabled()) {
      log.debug(method, "la ricerca verso acta ha restituito i seguenti risultati:");
      log.debug(method, "\t objects = "
          + ObjectUtils.represent(pagingResponse != null ? pagingResponse.getObjects() : null));
    }

    if (pagingResponse == null) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    List<RegistrazioneClassificazioniActa> list = new ArrayList<>();

    for (ObjectResponseType rawObject : pagingResponse.getObjects()) {
      var converted = ActaParser.actaPropertiesToEntity(null, rawObject.getProperties(),
          RegistrazioneClassificazioniActaDefaultImpl.class);
      list.add(converted);
    }

    int totalSizeEstimate = list.size();
    if (pagingResponse.isHasMoreItems()) {
      totalSizeEstimate = (pageable.getPageSize() * (pageable.getPageNumber() + 1)) + 1;
    }

    return new PageImpl<>(list, pageable, totalSizeEstimate);
  }

  /**
   * @param id
   * @return
   * @throws it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException
   */
  public List<DocumentoSempliceActa> findDocumentiSempliciByIdClassificazione(String id)
      throws it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException {
    final var method = "findDocumentiSempliciByIdClassificazione";

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    int pageSize = 10;
    int offset = 0;
    var out = new ArrayList<ObjectResponseType>();

    PagingResponseType parents = null;
    try {
      do {
        parents = this.facade.getNavigationService().getChildren(repositoryIdType,
            ActaClientHelper.wrapId(id), principalIdType, filter, pageSize, offset);
        if (parents != null) {
          out.addAll(parents.getObjects());
        }

        offset += pageSize;
      } while (parents != null && parents.isHasMoreItems());
    } catch (Exception e) {
      var wrapped = ActaClientHelper.wrapException(e, method);
      if (wrapped instanceof ActaAccessDeniedException) {
        // silent fail
        log.warn(method,
            "accesso negato per il client corrente ai children della classificazione " + id);
        return new ArrayList<>();
      }
      throw e;
    }

    return out.stream().filter(candidate -> candidate.getProperties().stream()
        .anyMatch(p -> OBJECT_ID.equals(p.getQueryName().getPropertyName())
            && DOCUMENTO_SEMPLICE_PROPERTIES_TYPE.equals(p.getQueryName().getClassName())))
        .map(e -> ActaParser.actaPropertiesToEntity(contesto, e.getProperties(),
            DocumentoSempliceActaDefaultImpl.class))
        .map(e -> this.find(e.getId(), DocumentoSempliceActaDefaultImpl.class))
        .collect(Collectors.toList());
  }

  /**
   * @param indiceClassificazioneEsteso
   * @return
   * @throws AcarisException
   */
  public String findObjectIDStrutturaAggregativa(
      String indiceClassificazioneEsteso) throws AcarisException {
    final var methodName = "findObjectIDStrutturaAggregativa";
    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());

    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("AggregazioneICEView");

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterio = new QueryConditionType();

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    criterio.setOperator(EnumQueryOperator.EQUALS);
    criterio.setPropertyName("indiceClassificazioneEstesa");
    criterio.setValue(indiceClassificazioneEsteso);
    criteria.add(criterio);
    var queryResponse = facade.getObjectService().query(repositoryIdType, principalIdType, target,
        filter, criteria, null, 2, 0);

    var numResults = queryResponse.getObjects().size();
    if (numResults < 1) {
      log.info(methodName, "Nessuna struttura aggregativa trovata per l'indice di classificazione estesa: " + indiceClassificazioneEsteso);
      return null;
    } else if (numResults > 1) {
      throw new ActaTooManyResultsException(
          "Troppi tipi di aggregazione per l'indice di classificazione esteso "
              + indiceClassificazioneEsteso);
    }

    var properties = ActaParser.actaPropertiesToEntity(contesto,
        queryResponse.getObjects().get(0).getProperties(), AggregazioneICEViewActaDefaultImpl.class);

    var enumTipoAggregazione = Arrays.asList(TipologieAggregazione.values()).stream()
      .filter(enumFilter -> enumFilter.getCodice().equals(properties.getIdTipoAggregazione()))
      .findFirst().orElseThrow(() -> {
        var error = String.format("Tipo di aggregazione %d non presente tra quelli codificati a sistema", properties.getIdTipoAggregazione());
        log.error(methodName, error);
            throw new NotFoundException(error);
    });

    QueryableObjectType targetTipoAggregazione = new QueryableObjectType();
    targetTipoAggregazione.setObject(enumTipoAggregazione.getDescrizione());
    List<QueryConditionType> criteriaTipoAggregazione = new LinkedList<>();
    QueryConditionType criterioTipoAggregazione = new QueryConditionType();
    criterioTipoAggregazione.setPropertyName("ecmUuidNodo");
    criterioTipoAggregazione.setValue(properties.getEcmUuidNodo());
    criterioTipoAggregazione.setOperator(EnumQueryOperator.EQUALS);
    criteriaTipoAggregazione.add(criterioTipoAggregazione);

    var queryResponseTipoAggregazione = facade.getObjectService().query(repositoryIdType,
        principalIdType, targetTipoAggregazione, filter, criteriaTipoAggregazione, null, 2, 0);

    var numResultsAggregazione = queryResponseTipoAggregazione.getObjects().size();
    if (numResultsAggregazione < 1) {
      log.info(methodName, "Nessuna risultato ottenuto per il tipo di aggregazione: " + enumTipoAggregazione.getDescrizione() + " e per il codice ecmUuidNodo " + properties.getEcmUuidNodo());
      return null;
    } else if (numResultsAggregazione > 1) {
      throw new ActaTooManyResultsException(
          "Troppi risultati per il tipo di aggregazione " + enumTipoAggregazione.getDescrizione()
              + " e per il codice ecmUuidNodo " + properties.getEcmUuidNodo());
    }

    return queryResponseTipoAggregazione.getObjects().get(0).getObjectId().getValue();
  }

  /**
   * @param codice: codiceTitolario da ricercare
   * @return
   */
  public TitolarioActa findTitolario(String codice) throws AcarisException {
    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName("codice");
    criterion.setValue(codice);
    criterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(criterion);
    QueryConditionType statusCriterion = new QueryConditionType();
    statusCriterion.setPropertyName("stato");
    statusCriterion.setValue("3");
    statusCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(statusCriterion);
    return loadEntity(criteria, TitolarioActaDefaultImpl.class);
  }

  /**
   * @param chiaveTitolario
   * @param chiavePadre
   * @return
   */
  public Page<VoceTitolarioActa> findVociTitolarioInAlberatura(String chiaveTitolario,
      String chiavePadre, Pageable pageable) throws AcarisException {

    return StringUtils.isEmpty(chiavePadre)
        ? ricercaVociTitolarioRoot(chiaveTitolario, pageable)
        : ricercaVociTitolarioLeaf(chiaveTitolario, chiavePadre, pageable);
  }

  private Page<VoceTitolarioActa> ricercaVociTitolarioRoot(String chiaveTitolario,
      Pageable pageable) throws AcarisException {
    final var method = "ricercaVociTitolarioRoot";
    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());
    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));
    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("VocePropertiesType");

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName("dbKeyTitolario");
    criterion.setValue(chiaveTitolario);
    criterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(criterion);
    QueryConditionType levelCriterion = new QueryConditionType();
    levelCriterion.setPropertyName("primoLivello");
    levelCriterion.setValue("S");
    levelCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(levelCriterion);
    QueryConditionType statusCriterion = new QueryConditionType();
    statusCriterion.setPropertyName("stato");
    statusCriterion.setValue("2");
    statusCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(statusCriterion);

    int maxItems = pageable.getPageSize();
    int skipCount = pageable.getOffset();

    if (log != null) {
      log.info(method, "eseguo ricerca verso acta con i seguenti parametri:");
      log.info(method, "\t repositoryId = " + ObjectUtils.toJson(repositoryIdType));
      log.info(method, "\t principalId = " + ObjectUtils.toJson(principalIdType));
      log.info(method, "\t target = " + ObjectUtils.toJson(target));
      log.info(method, "\t filter = " + ObjectUtils.toJson(filter));
      log.info(method, "\t criteria = " + ObjectUtils.toJson(criteria));
      log.info(method, "\t navigationLimits = null");
      log.info(method, "\t maxItems = " + ObjectUtils.toJson(maxItems));
      log.info(method, "\t skipCount = " + ObjectUtils.toJson(skipCount));
    }

    var pagingResponse = facade.getObjectService().query(repositoryIdType, principalIdType,
        target,
        filter, criteria, null, maxItems, skipCount);

    if (null != log && log.isDebugEnabled()) {
      log.debug(method, "la ricerca verso acta ha restituito i seguenti risultati:");
      log.debug(method, "\t objects = " + ObjectUtils.represent(pagingResponse.getObjects()));
    }

    List<VoceTitolarioActa> voci = new ArrayList<>();

    for (var voce : pagingResponse.getObjects()) {
      var properties = ActaParser.actaPropertiesToEntity(contesto, voce.getProperties(),
          VoceTitolarioActaDefaultImpl.class);
      voci.add(properties);
    }

    int totalSizeEstimate = voci.size();
    if (pagingResponse.isHasMoreItems()) {
      totalSizeEstimate = (pageable.getPageSize() * (pageable.getPageNumber() + 1)) + 1;
    }


    return new PageImpl<>(voci, pageable, totalSizeEstimate);
  }

  private Page<VoceTitolarioActa> ricercaVociTitolarioLeaf(String chiaveTitolario,
      String chiavePadre, Pageable pageable) throws AcarisException {

    final var method = "ricercaVociTitolarioLeaf";

    ObjectIdType repositoryIdType = ActaClientHelper.wrapId(contesto.getRepository());
    PrincipalIdType principalIdType = new PrincipalIdType();
    principalIdType.setValue(ActaClientHelper.getValidPrincipal(facade, contesto));
    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("VocePropertiesType");

    List<QueryConditionType> criteria = new LinkedList<>();
    QueryConditionType criterion = new QueryConditionType();
    criterion.setPropertyName("dbKeyTitolario");
    criterion.setValue(chiaveTitolario);
    criterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(criterion);
    QueryConditionType levelCriterion = new QueryConditionType();
    levelCriterion.setPropertyName("primoLivello");
    levelCriterion.setValue("N");
    levelCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(levelCriterion);
    QueryConditionType statusCriterion = new QueryConditionType();
    statusCriterion.setPropertyName("stato");
    statusCriterion.setValue("2");
    statusCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(statusCriterion);
    QueryConditionType vocePadreCriterion = new QueryConditionType();
    vocePadreCriterion.setPropertyName("objectIdVocePadre");
    vocePadreCriterion.setValue(chiavePadre);
    vocePadreCriterion.setOperator(EnumQueryOperator.EQUALS);
    criteria.add(vocePadreCriterion);

    int maxItems = pageable.getPageSize();
    int skipCount = pageable.getOffset();

    if (log != null) {
      log.info(method, "eseguo ricerca verso acta con i seguenti parametri:");
      log.info(method, "\t repositoryId = " + ObjectUtils.toJson(repositoryIdType));
      log.info(method, "\t principalId = " + ObjectUtils.toJson(principalIdType));
      log.info(method, "\t target = " + ObjectUtils.toJson(target));
      log.info(method, "\t filter = " + ObjectUtils.toJson(filter));
      log.info(method, "\t criteria = " + ObjectUtils.toJson(criteria));
      log.info(method, "\t navigationLimits = null");
      log.info(method, "\t maxItems = " + ObjectUtils.toJson(maxItems));
      log.info(method, "\t skipCount = " + ObjectUtils.toJson(skipCount));
    }

    var pagingResponse = facade.getObjectService().query(repositoryIdType, principalIdType, target,
        filter, criteria, null, maxItems, skipCount);

    if (null != log && log.isDebugEnabled()) {
      log.debug(method, "la ricerca verso acta ha restituito i seguenti risultati:");
      log.debug(method, "\t objects = " + ObjectUtils.represent(pagingResponse.getObjects()));
    }

    List<VoceTitolarioActa> voci = new ArrayList<>();

    for (var voce : pagingResponse.getObjects()) {
      var properties = ActaParser.actaPropertiesToEntity(contesto, voce.getProperties(),
          VoceTitolarioActaDefaultImpl.class);
      voci.add(properties);
    }

    int totalSizeEstimate = voci.size();
    if (pagingResponse.isHasMoreItems()) {
      totalSizeEstimate = (pageable.getPageSize() * (pageable.getPageNumber() + 1)) + 1;
    }

    return new PageImpl<>(voci, pageable, totalSizeEstimate);
  }

  /**
   * Creates builder to build {@link ActaClientExecutor}.
   *
   * @return created builder
   */
  @Generated("SparkTools")
  public static IContestoStage builder() {
    return new Builder();
  }

  @Generated("SparkTools")
  public interface IContestoStage {

    public IFacadeStage withContesto(ActaClientContext contesto);
  }

  @Generated("SparkTools")
  public interface IFacadeStage {

    public IBuildStage withFacade(ActaProvider facade);
  }

  @Generated("SparkTools")
  public interface IBuildStage {

    public ActaClientExecutor build();
  }

  /**
   * Builder to build {@link ActaClientExecutor}.
   */
  @Generated("SparkTools")
  public static final class Builder implements IContestoStage, IFacadeStage, IBuildStage {

    private ActaClientContext contesto;

    private ActaProvider facade;

    private Builder() {}

    @Override
    public IFacadeStage withContesto(ActaClientContext contesto) {
      this.contesto = contesto;
      return this;
    }

    @Override
    public IBuildStage withFacade(ActaProvider facade) {
      this.facade = facade;
      return this;
    }

    @Override
    public ActaClientExecutor build() {
      return new ActaClientExecutor(this);
    }
  }

}
