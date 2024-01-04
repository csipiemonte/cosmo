/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.index2;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmosoap.config.Constants;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShare;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShareDetail;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.SharedLinkValueConverter;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2DuplicateNodeException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2Exception;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2NodeLockedException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2NodeNotFoundException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.internal.IndexMapper;
import it.csi.cosmo.cosmosoap.integration.soap.index2.internal.IndexPayload;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntityVersion;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFileFormatInfo;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexModel;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexObject;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexRawProperty;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.converter.IndexValueConverter;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexContentDisposition;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexVerifyReport;
import it.csi.cosmo.cosmosoap.integration.soap.index2.mtom.Index2ContentAttachment;
import it.csi.cosmo.cosmosoap.integration.soap.index2.mtom.Index2DataHandler;
import it.csi.cosmo.cosmosoap.integration.soap.index2.mtom.Index2MtomDownloadHelper;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.index.ecmengine.client.mtom.EcmEngineMtomDelegateImpl;
import it.doqui.index.ecmengine.client.webservices.dto.Node;
import it.doqui.index.ecmengine.client.webservices.dto.OperationContext;
import it.doqui.index.ecmengine.client.webservices.dto.Path;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Aspect;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Content;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.FileFormatInfo;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.FileInfo;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Property;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Version;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.NodeResponse;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.ResultContent;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.ResultProperty;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.SearchParams;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.Document;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.DocumentOperation;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.EnvelopedContent;
import it.doqui.index.ecmengine.client.webservices.engine.EcmEngineWebServiceDelegate;
import it.doqui.index.ecmengine.client.webservices.engine.EcmEngineWebServiceDelegateServiceLocator;
import it.doqui.index.ecmengine.client.webservices.exception.EcmEngineException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoDataExtractedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.mtom.dto.Attachment;
import it.doqui.index.ecmengine.mtom.dto.MtomNode;
import it.doqui.index.ecmengine.mtom.dto.MtomOperationContext;
import it.doqui.index.ecmengine.mtom.dto.SharingInfo;
import it.doqui.index.ecmengine.mtom.exception.MtomClientException;


public class Index2WrapperFacadeImpl implements Index2WrapperFacade {

  private static final String VERIFICA_FIRMA = "VERIFICA FIRMA";

  private static final String SHARE = "CONDIVIDI";

  private static final String UNSHARE = "RIMUOVI CONDIVISIONE";

  private static final String GET_SHARES = "GET SHARES";

  private static final String MIME_TYPE_DEFAULT = "application/octet-stream";

  private static final String ERR_ENTITA_NON_SALVATA =
      "Entita' da estrarre non salvata su Index (UID non valorizzato)";

  private static final String ERR_PAYLOAD_REQUIRED =
      "Il payload di input e' obbligatorio e non deve essere vuoto";

  private static final String ERR_NODO_S_NON_TROVATO = "Nodo %s non trovato";

  private static final String ERR_SOURCE_IDENTIFIER_REQUIRED =
      "L'identificativo (UID o path) sorgente e' obbligatorio";

  private static final String ESTRAI_BUSTA = "ESTRAI BUSTA";

  private static final String SIGNATURE_UNSIGNED = "UNSIGNED";

  private static final String ENTITY_IS_REQUIRED = "La entity e' obbligatoria";

  private static final String ERROR = "ERRORE: ";

  private static final String ERR_COULD_NOT_AUTODETECT_CLASS =
      "Could not auto-detect class for result: ";

  private static final String PATH_SEPARATOR = "/";

  private static final String CM_FOLDER = "cm:folder";

  private static final String CM_CONTENT = "cm:content";

  private static final String CM_NAME = "cm:name";

  private static final String CM_CONTAINS = "cm:contains";

  private static final String METADATI_PREFIXED_NAME = "cm:";

  private static final DateTimeFormatter ISO_DATE_TIME_TO_SECONDS =
      DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private static final boolean DOWNLOAD_POLICY_MTOM = true;

  protected static final CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "Index2WrapperFacadeImpl");

  private Random random = new Random();

  private String rootNodeName;

  private String endpointUrl;

  private String streamingEndpointUrl;

  private OperationContext operationContext = null;

  private OperationContext tempTenantOperationContext = null;

  private MtomOperationContext mtomOperationContext = null;

  private EcmEngineWebServiceDelegate service = null;

  private EcmEngineMtomDelegateImpl mtomDelegate = null;

  private Map<String, Class<? extends IndexEntity>> knownModels = new HashMap<>();

  private Map<String, Class<? extends IndexFolder>> knownFolderModels = new HashMap<>();

  private static AtomicLong operationCounter = new AtomicLong(0);

  private static Map<Class<? extends IndexValueConverter<?>>, IndexValueConverter<?>> converterCache =
      new HashMap<>();

  private Index2WrapperFacadeImpl(Builder builder) {

    if (StringUtils.isAnyBlank(builder.endpointUrl, builder.rootNode, builder.username,
        builder.password, builder.fruitore, builder.nomeFisico, builder.repository)) {
      throw invalidParameters("Missing parameters required for wrapper facade instantiation");
    }

    streamingEndpointUrl = builder.streamingEndpointUrl;
    endpointUrl = builder.endpointUrl;
    rootNodeName = builder.rootNode;

    operationContext = new OperationContext();
    operationContext.setUsername(builder.username);
    operationContext.setPassword(builder.password);
    operationContext.setFruitore(builder.fruitore);
    operationContext.setNomeFisico(builder.nomeFisico);
    operationContext.setRepository(builder.repository);

    mtomOperationContext = new MtomOperationContext();
    mtomOperationContext.setFruitore(builder.fruitore);
    mtomOperationContext.setNomeFisico(builder.nomeFisico);
    mtomOperationContext.setPassword(builder.password);
    mtomOperationContext.setRepository(builder.repository);
    mtomOperationContext.setUsername(builder.username);

    tempTenantOperationContext = new OperationContext();
    tempTenantOperationContext.setUsername("admin@temp");
    tempTenantOperationContext.setPassword("admin");
    tempTenantOperationContext.setFruitore(builder.fruitore);
    tempTenantOperationContext.setNomeFisico(builder.nomeFisico);
    tempTenantOperationContext.setRepository(builder.repository);

    if (!rootNodeName.startsWith(PATH_SEPARATOR)) {
      rootNodeName = PATH_SEPARATOR + rootNodeName;
    }

    try {
      loadRegisteredIndexModels(this.getClass().getClassLoader(), knownModels, knownFolderModels,
          "it.csi." + Constants.PRODUCT + "." + Constants.COMPONENT_NAME);
    } catch (IOException e) {
      throw wrap("Error loading known IndexModel definitions", e);
    }
  }

  // HELPERS

  private synchronized EcmEngineWebServiceDelegate getEcmengineManagementInterfaceService() {
    if (service == null) {
      try {
        EcmEngineWebServiceDelegateServiceLocator ecmEngineWebServiceDelegateServiceLocator =
            new EcmEngineWebServiceDelegateServiceLocator();
        service = ecmEngineWebServiceDelegateServiceLocator
            .getEcmEngineManagement(new URL(endpointUrl + "?wsdl"));

      } catch (Exception e) {
        throw wrap("Error creating INDEX service", e);
      }
    }

    return service;
  }

  private synchronized EcmEngineMtomDelegateImpl getEcmengineMtomDelegate() {
    if (mtomDelegate == null) {
      String method = "getEcmengineMtomDelegate";

      if (log.isDebugEnabled()) {
        log.debug(method, "building index2 MTOM delegate");
      }

      try {
        String urlServiceCxfServices = streamingEndpointUrl;
        if (log.isDebugEnabled()) {
          log.debug(method, "urlServiceCxfServices: " + urlServiceCxfServices);
        }

        mtomDelegate = new EcmEngineMtomDelegateImpl(urlServiceCxfServices);

        return mtomDelegate;
      } catch (Exception e) {
        throw wrap("error building index2 MTOM delegate", e);
      }
    }

    return mtomDelegate;
  }

  public EcmEngineWebServiceDelegate getExecutor() {
    return getEcmengineManagementInterfaceService();
  }

  public EcmEngineMtomDelegateImpl getMtomExecutor() {
    return getEcmengineMtomDelegate();
  }

  public OperationContext getOperationContext() {
    return operationContext;
  }


  public MtomOperationContext getMtomOperationContext() {
    return mtomOperationContext;
  }

  private static void wrap(Runnable runnable, String operationName, Object... params) {
    wrap(() -> {
      runnable.run();
      return null;
    }, operationName, params);
  }

  private static <T> T wrap(Callable<T> callable, String operationName, Object... params) {
    Long operationId = operationCounter.incrementAndGet();
    final String separator = "===========================================================";
    final String virtualMethodName = "wrap";
    final String prefix = "[" + operationName + " #" + operationId + "] ";

    log.info(virtualMethodName, separator);
    log.info(virtualMethodName, prefix + "START");
    if (log.isDebugEnabled()) {
      for (Object param : params) {
        log.debug(virtualMethodName, prefix + "parameter: " + ObjectUtils.represent(param));
      }
    }

    T result = null;

    try {
      result = callable.call();
      if (log.isDebugEnabled()) {
        log.debug(virtualMethodName, prefix + "RESULT: " + ObjectUtils.represent(result));
      }
      return result;

    } catch (Index2Exception | InvalidParameterException e) {
      log.error(virtualMethodName, prefix + ERROR + e.getClass().getName() + " - " + e.getMessage()
      + " - " + getErrorMessage(e), e);
      throw e;

    } catch (RuntimeException e) {
      log.error(virtualMethodName, prefix + ERROR + e.getClass().getName() + " - " + e.getMessage()
      + " - " + getErrorMessage(e), e);
      throw wrap("Errore di runtime nell'operazione " + operationName + " #" + operationId, e);

    } catch (Exception e) {
      log.error(virtualMethodName, prefix + ERROR + e.getClass().getName() + " - " + e.getMessage()
      + " - " + getErrorMessage(e), e);
      throw wrap("Errore generico nell'operazione " + operationName + " #" + operationId, e);

    } finally {
      log.info(virtualMethodName, prefix + "END");
      log.info(virtualMethodName, separator);
    }
  }

  // PUBLIC METHODS

  @Override
  public List<String> search(String parent, String text) {
    return wrap(() -> doSearch(parent, text), "SEARCH", parent, text);
  }

  @Override
  public String createFolder(String path) {
    return wrap(() -> doCreateFolder(path), "CREATE FOLDER", path);
  }

  @Override
  public <T extends IndexEntity> T create(String inputPathOrUUID, T entity) {
    return wrap(() -> doCreate(inputPathOrUUID, entity), "CREATE FILE", inputPathOrUUID, entity);
  }

  @Override
  public <T extends IndexEntity> T create(String inputPathOrUUID, T entity, InputStream content) {
    return wrap(() -> doCreate(inputPathOrUUID, entity, content), "CREATE FILE", inputPathOrUUID,
        entity);
  }

  @Override
  public <T extends IndexFolder> T createFolder(String inputPathOrUUID, T entity) {
    return wrap(() -> doCreateFolder(inputPathOrUUID, entity), "CREATE FOLDER", inputPathOrUUID,
        entity);
  }

  @Override
  public <T extends IndexEntity> T save(T entity) {
    return wrap(() -> doSave(entity), "SAVE FILE", entity);
  }

  @Override
  public <T extends IndexFolder> T saveFolder(T entity) {
    return wrap(() -> doSaveFolder(entity), "SAVE FOLDER", entity);
  }

  @Override
  public <T extends IndexEntity> T find(String uuid, Class<T> entityClass) {
    return wrap(() -> doFind(uuid, entityClass), "FIND FILE", uuid, entityClass);
  }

  @Override
  public <T extends IndexFolder> T findFolder(String uuid, Class<T> entityClass) {
    return wrap(() -> doFindFolder(uuid, entityClass), "FIND FOLDER", uuid, entityClass);
  }

  @Override
  public IndexFolder findFolder(String uuid) {
    return wrap(() -> doFindFolder(uuid), "FIND FOLDER", uuid);
  }

  @Override
  public IndexEntity find(String uuid) {
    return wrap(() -> doFind(uuid), "FIND RAW FILE", uuid);
  }

  @Override
  public <T extends IndexEntity> boolean delete(T entity) {
    return wrap(() -> doDelete(entity), "DELETE", entity);
  }

  @Override
  public boolean delete(String identifier) {
    return wrap(() -> doDelete(identifier), "DELETE", identifier);
  }

  @Override
  public void restore(String identifier) {
    wrap(() -> doRestore(identifier), "RESTORE", identifier);
  }

  @Override
  public void move(String uuidNode, String uuidTo) {
    wrap(() -> doMove(uuidNode, uuidTo), "MOVE", uuidNode, uuidTo);
  }

  @Override
  public <T extends IndexEntity> T checkOut(String identifier, Class<T> entityClass) {
    return wrap(() -> doCheckOut(identifier, entityClass), "CHECK OUT", identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> T checkOut(T entity) {
    return wrap(() -> doCheckOut(entity), "CHECK OUT", entity);
  }

  @Override
  public <T extends IndexEntity> T checkIn(T entity) {
    return wrap(() -> doCheckIn(entity), "CHECK IN", entity);
  }

  @Override
  public <T extends IndexEntity> void cancelCheckout(T entity) {
    wrap(() -> doCancelCheckOut(entity), "CANCEL CHECK OUT", entity);
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(String identifier,
      Class<T> entityClass) {
    return wrap(() -> doGetPreviousVersions(identifier, entityClass), "GET PREVIOUS VERSIONS",
        identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(T entity) {
    return wrap(() -> doGetPreviousVersions(entity), "GET PREVIOUS VERSIONS", entity);
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(byte[] content) {
    return wrap(() -> doGetFileFormatInfo(content), "GET FILE FORMAT INFO");
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(String identifier) {
    return wrap(() -> doGetFileFormatInfo(identifier), "GET FILE FORMAT INFO", identifier);
  }

  @Override
  public byte[] estraiBusta(String sourceIdentifier) {
    return wrap(() -> doEstraiBusta(sourceIdentifier), ESTRAI_BUSTA, sourceIdentifier);
  }

  @Override
  public byte[] estraiBusta(IndexEntity sourceEntity) {
    return wrap(() -> doEstraiBusta(sourceEntity), ESTRAI_BUSTA, sourceEntity);
  }

  @Override
  public byte[] estraiBusta(byte[] payload) {
    return wrap(() -> doEstraiBusta(payload), ESTRAI_BUSTA);
  }

  @Override
  public IndexEntity estraiBusta(String sourceIdentifier, String targetContainerIdentifier) {
    return wrap(() -> doEstraiBusta(sourceIdentifier, targetContainerIdentifier, null),
        ESTRAI_BUSTA, sourceIdentifier, targetContainerIdentifier);
  }

  @Override
  public <T extends IndexEntity> T estraiBusta(String sourceIdentifier,
      String targetContainerIdentifier, T targetEntity) {
    return wrap(() -> doEstraiBusta(sourceIdentifier, targetContainerIdentifier, targetEntity),
        ESTRAI_BUSTA, sourceIdentifier, targetContainerIdentifier, targetEntity);
  }

  @Override
  public IndexEntity estraiBusta(IndexEntity sourceEntity, String targetContainerIdentifier) {
    return wrap(() -> doEstraiBusta(sourceEntity, targetContainerIdentifier, null), ESTRAI_BUSTA,
        sourceEntity, targetContainerIdentifier);
  }

  @Override
  public <T extends IndexEntity> T estraiBusta(IndexEntity sourceEntity,
      String targetContainerIdentifier, T targetEntity) {
    return wrap(() -> doEstraiBusta(sourceEntity, targetContainerIdentifier, targetEntity),
        ESTRAI_BUSTA, sourceEntity, targetContainerIdentifier, targetEntity);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier) {
    return wrap(() -> doVerificaFirma(sourceIdentifier, null), VERIFICA_FIRMA, sourceIdentifier);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity) {
    return wrap(() -> doVerificaFirma(sourceEntity, null), VERIFICA_FIRMA, sourceEntity);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters) {
    return wrap(() -> doVerificaFirma(sourceIdentifier, parameters), VERIFICA_FIRMA,
        sourceIdentifier, parameters);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity,
      IndexSignatureVerificationParameters parameters) {
    return wrap(() -> doVerificaFirma(sourceEntity, parameters), VERIFICA_FIRMA, sourceEntity,
        parameters);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity) {
    return wrap(() -> doShare(entity, null), SHARE, entity);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity, IndexShareOptions options) {
    return wrap(() -> doShare(entity, options), SHARE, entity, options);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier) {
    return wrap(() -> doShare(sourceIdentifier, null), SHARE, sourceIdentifier);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier, IndexShareOptions options) {
    return wrap(() -> doShare(sourceIdentifier, options), SHARE, sourceIdentifier, options);
  }

  @Override
  public void unshare(IndexEntity entity) {
    wrap(() -> doUnshare(entity), UNSHARE, entity);
  }

  @Override
  public void unshare(String sourceIdentifier) {
    wrap(() -> doUnshare(sourceIdentifier), UNSHARE, sourceIdentifier);
  }

  @Override
  public void unshare(IndexEntity entity, URI link) {
    wrap(() -> doUnshare(entity, link), UNSHARE, entity, link);
  }

  @Override
  public void unshare(String sourceIdentifier, URI link) {
    wrap(() -> doUnshare(sourceIdentifier, link), UNSHARE, sourceIdentifier, link);
  }

  @Override
  public void unshare(IndexEntity entity, String shareId) {
    wrap(() -> doUnshare(entity, shareId), UNSHARE, entity, shareId);
  }

  @Override
  public void unshare(String sourceIdentifier, String shareId) {
    wrap(() -> doUnshare(sourceIdentifier, shareId), UNSHARE, sourceIdentifier, shareId);
  }

  @Override
  public void unshare(IndexEntity entity, IndexShare share) {
    wrap(() -> doUnshare(entity, share), UNSHARE, entity, share);
  }

  @Override
  public void unshare(String sourceIdentifier, IndexShare share) {
    wrap(() -> doUnshare(sourceIdentifier, share), UNSHARE, sourceIdentifier, share);
  }

  @Override
  public List<IndexShareDetail> getShares(IndexEntity sourceEntity) {
    return wrap(() -> doGetShares(sourceEntity), GET_SHARES, sourceEntity);
  }

  @Override
  public List<IndexShareDetail> getShares(String sourceIdentifier) {
    return wrap(() -> doGetShares(sourceIdentifier), GET_SHARES, sourceIdentifier);
  }

  @Override
  public String copyNode(String sourceIdentifierFrom, String sourceIdentifierTo) {
    return wrap(() -> doCopyNode(sourceIdentifierFrom, sourceIdentifierTo), "COPY NODE",
        sourceIdentifierFrom, sourceIdentifierTo);
  }

  // IMPLEMENTATIONS


  private String requireExistingEntityUUID(String sourceIdentifier) {
    if (StringUtils.isBlank(sourceIdentifier)) {
      throw invalidParameters(ERR_SOURCE_IDENTIFIER_REQUIRED);
    }

    sourceIdentifier = toUUID(sourceIdentifier);

    if (sourceIdentifier == null) {
      throw wrap(String.format(ERR_NODO_S_NON_TROVATO, sourceIdentifier),
          Index2NodeNotFoundException.class);
    }

    return sourceIdentifier;
  }

  private void requireSavedEntity(IndexEntity sourceEntity) {
    if (sourceEntity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }
    if (StringUtils.isBlank(sourceEntity.getUid())) {
      throw invalidParameters(ERR_ENTITA_NON_SALVATA);
    }
  }

  private void requireUnsavedEntity(IndexEntity entity) {
    if (entity == null) {
      throw invalidParameters("Entity e' richiesta");
    }
    if (!StringUtils.isBlank(entity.getUid())) {
      throw invalidParameters("Entity deve essere un oggetto non ancora salvato");
    }
  }

  private List<IndexShareDetail> doGetShares(IndexEntity sourceEntity) {
    requireSavedEntity(sourceEntity);
    return doGetShares(sourceEntity.getUid());
  }

  private List<IndexShareDetail> doGetShares(String sourceIdentifier) {
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);

    it.doqui.index.ecmengine.mtom.dto.Node node = new it.doqui.index.ecmengine.mtom.dto.Node();
    node.setUid(sourceIdentifier);

    SharingInfo[] shares;
    List<IndexShareDetail> output = new LinkedList<>();

    try {
      shares = getEcmengineMtomDelegate().getSharingInfos(node, this.mtomOperationContext);
    } catch (Exception e) {
      throw wrap("Errore nel reperimento degli share per il documento " + sourceIdentifier, e);
    }

    for (SharingInfo share : shares) {
      output.add(map(share));
    }

    return output;
  }

  private String doCopyNode(String sourceIdentifierFrom, String sourceIdentifierTo) {

    if (StringUtils.isBlank(sourceIdentifierFrom) || StringUtils.isBlank(sourceIdentifierTo)) {
      throw invalidParameters(ERR_SOURCE_IDENTIFIER_REQUIRED);
    }

    Node from = new Node();
    from.setUid(sourceIdentifierFrom);

    Node to = new Node();
    to.setUid(sourceIdentifierTo);

    try {
      Node copy =
          getEcmengineManagementInterfaceService().copyNode(from, to, this.operationContext);
      return copy.getUid();
    } catch (RemoteException e) {
      throw wrap("Errore nella duplicazione del nodo " + sourceIdentifierFrom + " al nodo "
          + sourceIdentifierTo, e);
    }
  }

  private IndexShareDetail map(SharingInfo share) {
    IndexShareDetail output = new IndexShareDetail();

    output.setContentDisposition(share.getResultContentDisposition());
    output.setContentPropertyPrefixedName(share.getContentPropertyPrefixedName());
    output.setFromDate(SharedLinkValueConverter.offsetDateTime(share.getFromDate()));
    output.setResultPropertyPrefixedName(share.getResultPropertyPrefixedName());
    output.setSource(SharedLinkValueConverter.scope(share.getSource()));
    output.setToDate(SharedLinkValueConverter.offsetDateTime(share.getToDate()));
    output.setDownloadUri(URI.create(share.getSharedLink()));

    return output;
  }

  private void doUnshare(IndexEntity sourceEntity) {
    requireSavedEntity(sourceEntity);
    doUnshare(sourceEntity.getUid());
  }

  private void doUnshare(String sourceIdentifier) {
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);

    it.doqui.index.ecmengine.mtom.dto.Node node = new it.doqui.index.ecmengine.mtom.dto.Node();
    node.setUid(sourceIdentifier);

    try {
      getEcmengineMtomDelegate().retainDocument(node, this.mtomOperationContext);
    } catch (Exception e) {
      throw wrap(
          "Errore nel retain del document (eliminazione link di condivisione) per il documento "
              + sourceIdentifier,
              e);
    }
  }

  private void doUnshare(IndexEntity sourceEntity, IndexShare share) {
    requireSavedEntity(sourceEntity);
    doUnshare(sourceEntity.getUid(), share.getContentHash());
  }

  private void doUnshare(IndexEntity sourceEntity, URI link) {
    requireSavedEntity(sourceEntity);
    doUnshare(sourceEntity.getUid(), link.toString());
  }

  private void doUnshare(IndexEntity sourceEntity, String shareId) {
    requireSavedEntity(sourceEntity);
    doUnshare(sourceEntity.getUid(), shareId);
  }

  private void doUnshare(String sourceIdentifier, IndexShare share) {
    doUnshare(sourceIdentifier, share.getContentHash());
  }

  private void doUnshare(String sourceIdentifier, URI link) {
    doUnshare(sourceIdentifier, link.toString());
  }

  private void doUnshare(String sourceIdentifier, String link) {
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);

    if (link == null) {
      throw wrap("Il link da eliminare e' richiesto e non puo' essere nullo");
    }

    it.doqui.index.ecmengine.mtom.dto.Node node = new it.doqui.index.ecmengine.mtom.dto.Node();
    node.setUid(sourceIdentifier);

    SharingInfo sharingInfo = new SharingInfo();
    sharingInfo.setSharedLink(link);

    try {
      getEcmengineMtomDelegate().removeSharedLink(node, sharingInfo, this.mtomOperationContext);
    } catch (Exception e) {
      throw wrap(
          "Errore nell' eliminazione del link di condivisione per il documento " + sourceIdentifier,
          e);
    }
  }

  private CreatedSharedLink doShare(String sourceIdentifier, IndexShareOptions options) {
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);
    return doShareForIdentifier(sourceIdentifier, options);
  }

  private CreatedSharedLink doShare(IndexEntity sourceEntity, IndexShareOptions options) {
    requireSavedEntity(sourceEntity);
    return doShareForIdentifier(sourceEntity.getUid(), options);
  }

  private CreatedSharedLink doShareForIdentifier(String sourceIdentifier,
      IndexShareOptions options) {
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);

    if (options == null) {
      options = IndexShareOptions.builder().build();
    }

    it.doqui.index.ecmengine.mtom.dto.Node node = new it.doqui.index.ecmengine.mtom.dto.Node();
    node.setUid(sourceIdentifier);
    SharingInfo params = new SharingInfo();
    params.setContentPropertyPrefixedName(CM_CONTENT);
    params.setResultPropertyPrefixedName(CM_NAME);
    params.setSource(options.getSource() != null ? options.getSource().name().toLowerCase()
        : IndexShareScope.INTERNET.name().toLowerCase());
    params.setFromDate(options.getFromDate() != null
        ? options.getFromDate().truncatedTo(ChronoUnit.SECONDS).format(ISO_DATE_TIME_TO_SECONDS)
            : null);
    params.setToDate(options.getToDate() != null
        ? options.getToDate().truncatedTo(ChronoUnit.SECONDS).format(ISO_DATE_TIME_TO_SECONDS)
            : null);

    if (IndexContentDisposition.INLINE == options.getContentDisposition()) {
      String contentDisposition = "inline";
      if (!StringUtils.isBlank(options.getFilename())) {
        contentDisposition += "; filename=\"" + options.getFilename() + "\"";
      }

      params.setResultContentDisposition(contentDisposition);
    } else if (!StringUtils.isBlank(options.getFilename())) {
      String contentDisposition = "attachment; filename=\"" + options.getFilename() + "\"";
      params.setResultContentDisposition(contentDisposition);
    }

    try {
      String shareLink =
          getEcmengineMtomDelegate().shareDocument(node, params, this.mtomOperationContext);

      CreatedSharedLink output = new CreatedSharedLink();
      output.setDownloadUri(URI.create(shareLink));
      output.setFromDate(options.getFromDate());
      output.setToDate(options.getToDate());
      output.setSource(options.getSource());
      output.setContentDisposition(options.getContentDisposition());
      output.setFilename(options.getFilename());

      return output;

    } catch (Exception e) {
      throw wrap(
          "Errore nella generazione del link per la condivisione del documento " + sourceIdentifier,
          e);
    }
  }

  private IndexVerifyReport doVerificaFirma(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters) {
    if (StringUtils.isBlank(sourceIdentifier)) {
      throw invalidParameters(ERR_SOURCE_IDENTIFIER_REQUIRED);
    }

    sourceIdentifier = toUUID(sourceIdentifier);

    if (sourceIdentifier == null) {
      throw wrap(String.format(ERR_NODO_S_NON_TROVATO, sourceIdentifier),
          Index2NodeNotFoundException.class);
    }

    return doVerificaFirmaForIdentifier(sourceIdentifier, parameters);
  }

  private IndexVerifyReport doVerificaFirma(IndexEntity sourceEntity,
      IndexSignatureVerificationParameters parameters) {
    if (sourceEntity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }
    if (StringUtils.isBlank(sourceEntity.getUid())) {
      throw invalidParameters(ERR_ENTITA_NON_SALVATA);
    }
    return doVerificaFirmaForIdentifier(sourceEntity.getUid(), parameters);
  }

  private IndexVerifyReport doVerificaFirmaForIdentifier(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters) {
    if (StringUtils.isBlank(sourceIdentifier)) {
      throw invalidParameters(ERR_SOURCE_IDENTIFIER_REQUIRED);
    }

    sourceIdentifier = toUUID(sourceIdentifier);

    if (parameters == null) {
      parameters = IndexSignatureVerificationParameters.builder().build();
    }

    it.doqui.index.ecmengine.mtom.dto.VerifyParameter param =
        new it.doqui.index.ecmengine.mtom.dto.VerifyParameter();
    param.setVerificationDate(new Date()); // NOSONAR
    param.setVerificationType(Boolean.TRUE.equals(parameters.isVerifyCertificateList()) ? 1 : 0);

    it.doqui.index.ecmengine.mtom.dto.Document doc1 =
        new it.doqui.index.ecmengine.mtom.dto.Document();
    doc1.setUid(sourceIdentifier);

    it.doqui.index.ecmengine.mtom.dto.DocumentOperation operation =
        new it.doqui.index.ecmengine.mtom.dto.DocumentOperation();
    operation.setReturnData(false);
    operation.setTempStore(true);
    doc1.setOperation(operation);

    doc1.setContentPropertyPrefixedName(CM_CONTENT);

    try {
      it.doqui.index.ecmengine.mtom.dto.VerifyReport result = this.getEcmengineMtomDelegate()
          .verifySignedDocument(doc1, null, param, this.mtomOperationContext);

      IndexMapper mapper = new IndexMapper();
      return mapper.map(result);
    } catch (Exception e) {
      throw wrap("Errore nella verifica della firma", e);
    }
  }

  private byte[] doEstraiBusta(byte[] payload) {
    if (payload == null || payload.length < 1) {
      throw invalidParameters(ERR_PAYLOAD_REQUIRED);
    }

    return sbustaToBytes(payload);
  }

  private byte[] doEstraiBusta(String sourceIdentifier) {
    if (StringUtils.isBlank(sourceIdentifier)) {
      throw invalidParameters(ERR_SOURCE_IDENTIFIER_REQUIRED);
    }

    sourceIdentifier = toUUID(sourceIdentifier);

    if (sourceIdentifier == null) {
      throw wrap(String.format(ERR_NODO_S_NON_TROVATO, sourceIdentifier),
          Index2NodeNotFoundException.class);
    }

    return sbustaToBytes(sourceIdentifier);
  }

  private byte[] doEstraiBusta(IndexEntity sourceEntity) {
    if (sourceEntity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }
    if (StringUtils.isBlank(sourceEntity.getUid())) {
      throw invalidParameters(ERR_ENTITA_NON_SALVATA);
    }
    return sbustaToBytes(sourceEntity.getUid());
  }

  private <T extends IndexEntity> T doEstraiBusta(String sourceIdentifier,
      String targetContainerIdentifier, T targetEntity) {

    // sourceIdentifier esiste, targetContainerIdentifier esiste ed e' una cartella
    sourceIdentifier = requireExistingEntityUUID(sourceIdentifier);

    return doEstraiBusta(doFind(sourceIdentifier), targetContainerIdentifier, targetEntity);
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doEstraiBusta(IndexEntity sourceEntity,
      String targetContainerIdentifier, T targetEntity) {

    // sourceEntity esiste ed e' salvata, targetContainerIdentifier esiste ed e' una cartella
    requireSavedEntity(sourceEntity);
    targetContainerIdentifier = requireExistingEntityUUID(targetContainerIdentifier);

    // targetEntity se non null deve essere non salvato
    if (targetEntity != null) {
      requireUnsavedEntity(targetEntity);
    }

    final String method = "doEstraiBusta";
    boolean createEntity = (targetEntity == null);
    boolean assignFilename = createEntity || StringUtils.isBlank(targetEntity.getFilename());

    // nota: funziona passando EnvelopedContent ma bisogna trasferire i bytes
    // estrai documento dalla busta in contenuto temporaneo su index
    Document extractionResult = sbustaToTemp(sourceEntity.getUid());

    if (createEntity) {
      targetEntity = (T) new GenericIndexContent();
    }

    if (assignFilename) {
      targetEntity.setFilename(sourceEntity.getFilename() + "-estratto");
    }
    if (StringUtils.isBlank(targetEntity.getMimeType())) {
      targetEntity.setMimeType(MIME_TYPE_DEFAULT);
    }

    // ora analizzo il file temporaneo
    try {
      FileFormatInfo[] tempFileFormats =
          retrieveFileFormatInfo(extractionResult.getUid(), this.tempTenantOperationContext);
      if (tempFileFormats != null && tempFileFormats.length > 0) {
        FileFormatInfo tempFileFormat = tempFileFormats[0];
        if (!StringUtils.isBlank(tempFileFormat.getMimeType())) {
          targetEntity.setMimeType(tempFileFormat.getMimeType());
        }
        if (assignFilename && !StringUtils.isBlank(tempFileFormat.getTypeExtension())) {
          targetEntity
          .setFilename(targetEntity.getFilename() + "." + tempFileFormat.getTypeExtension());
        }
      }
    } catch (Exception e) {
      throw wrap("Errore nell'analisi del file temporaneo sbustato", e);
    }

    log.debug(method, "creo nodo destinazione dentro cartella {} con nome {} e mimetype {}",
        targetContainerIdentifier, targetEntity.getFilename(), targetEntity.getMimeType());

    try {
      return this.doCreate(targetContainerIdentifier, targetEntity, extractionResult.getUid());
    } catch (Exception e) {
      throw wrap("Errore nello spostamento del contenuto estratto dal nodo temporaneo", e);
    }

  }

  /*
   * private void spostaContenutoTemporaneo(String sourceUUID, String targetUUID) throws
   * RemoteException { final String method = "spostaContenutoTemporaneo"; sourceUUID =
   * strip(sourceUUID); targetUUID = strip(targetUUID);
   *
   * var nodeTempSource = new Node(sourceUUID); var nodeTempDest = new Node(targetUUID); var
   * property = new Property();
   *
   * property.setPrefixedName(CM_CONTENT);
   *
   * log.debug(method, "muovo contenuto da nodo temporaneo [{}] a nodo destinazione [{}]",
   * nodeTempSource.getUid(), nodeTempDest.getUid());
   *
   * getEcmengineManagementInterfaceService().moveContentFromTemp(nodeTempSource, nodeTempDest,
   * property, operationContext); }
   */

  private Document sbustaToTemp(String uuid) {
    final String method = "sbustaToTemp";
    uuid = strip(uuid);
    Document doc = new Document();

    var operation = new DocumentOperation();
    operation.setReturnData(false);
    operation.setTempStore(true);

    doc.setOperation(operation);
    doc.setUid(uuid);
    doc.setContentPropertyPrefixedName(CM_CONTENT);

    Document extractionResult;
    log.debug(method, "avvio estrazione contenuto da UID {} con busta in nodo temporaneo", uuid);
    try {
      extractionResult = getEcmengineManagementInterfaceService().extractDocumentFromEnvelope(doc,
          operationContext);
    } catch (Exception e) {
      throw wrap("Errore nell'estrazione da UID con busta in nodo temporaneo", e);
    }

    return extractionResult;
  }

  private byte[] sbustaToBytes(String uuid) {
    final String method = "sbustaToBytes";
    uuid = strip(uuid);
    Document doc = new Document();

    var operation = new DocumentOperation();
    operation.setReturnData(true);
    operation.setTempStore(false);

    doc.setOperation(operation);
    doc.setUid(uuid);
    doc.setContentPropertyPrefixedName(CM_CONTENT);

    Document extractionResult;
    log.debug(method, "avvio estrazione contenuto da UID {} con busta in bytes", uuid);
    try {
      extractionResult = getEcmengineManagementInterfaceService().extractDocumentFromEnvelope(doc,
          operationContext);
    } catch (Exception e) {
      throw wrap("Errore nell'estrazione da UID con busta in bytes", e);
    }

    return extractionResult.getBuffer();
  }

  private byte[] sbustaToBytes(byte[] content) {
    final String method = "sbustaToTemp";

    // nota: funziona passando EnvelopedContent ma bisogna trasferire i bytes
    EnvelopedContent ec = new EnvelopedContent();
    ec.setData(content);
    ec.setStore(false);

    // estrai documento dalla busta in contenuto temporaneo su index
    Document extractionResult;
    log.debug(method, "avvio estrazione contenuto dalla busta in nodo temporaneo");
    try {
      extractionResult = getEcmengineManagementInterfaceService().extractDocumentFromEnvelope(ec,
          operationContext);
    } catch (Exception e) {
      throw wrap("Errore nell'estrazione del documento dalla busta", e);
    }

    return extractionResult.getBuffer();
  }

  private IndexFileFormatInfo doGetFileFormatInfo(byte[] content) {
    FileFormatInfo[] fileFormatInfo;
    String signatureType;

    try {
      fileFormatInfo = retrieveFileFormatInfo(content);
    } catch (Exception e) {
      throw wrap("fetching of fileFormatInfo data failed", e);
    }

    try {
      signatureType = retrieveSignatureType(content);
    } catch (Exception e) {
      throw wrap("fetching of signatureType data failed", e);
    }

    return map(fileFormatInfo, signatureType);
  }

  private IndexFileFormatInfo doGetFileFormatInfo(String identifierInput) {
    final String identifier = strip(identifierInput);
    FileFormatInfo[] fileFormatInfo;
    String signatureType;

    try {
      fileFormatInfo = retrieveFileFormatInfo(identifier);
    } catch (Exception e) {
      throw wrap("fetching of fileFormatInfo data failed", e);
    }

    try {
      signatureType = retrieveSignatureType(identifier);
    } catch (Exception e) {
      throw wrap("fetching of signatureType data failed", e);
    }

    return map(fileFormatInfo, signatureType);
  }

  private IndexFileFormatInfo map(FileFormatInfo[] fileFormatInfoList, String signatureType) {
    boolean isSigned =
        !StringUtils.isBlank(signatureType) && !signatureType.equalsIgnoreCase(SIGNATURE_UNSIGNED);
    boolean hasFileFormatInfo = fileFormatInfoList.length > 0;
    FileFormatInfo fileFormatInfo = hasFileFormatInfo ? fileFormatInfoList[0] : null;

    //@formatter:off
    return IndexFileFormatInfo.builder()
        .withDescription(hasFileFormatInfo ? fileFormatInfo.getDescription() : null)
        .withFormatVersion(hasFileFormatInfo ? fileFormatInfo.getFormatVersion() : null)
        .withMimeType(hasFileFormatInfo && fileFormatInfo.getMimeType() != null ?
            Arrays.asList(fileFormatInfo.getMimeType().split(",")).stream().map(String::strip)
            .collect(Collectors.toCollection(LinkedList::new)).toArray(new String[] {}) : new String[] {})
        .withPuid(hasFileFormatInfo ? fileFormatInfo.getPuid() : null)
        .withTypeExtension(hasFileFormatInfo ? fileFormatInfo.getTypeExtension() : null)
        .withSigned(isSigned)
        .withSignatureType(isSigned ? signatureType.toUpperCase() : null)
        .build();
    //@formatter:on
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> List<IndexEntityVersion<T>> doGetPreviousVersions(T entity) {
    if (entity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    return (List<IndexEntityVersion<T>>) (List<?>) doGetPreviousVersions(entity.getUid(),
        entity.getClass());
  }

  private <T extends IndexEntity> List<IndexEntityVersion<T>> doGetPreviousVersions(
      String identifier, Class<T> entityClass) {

    String method = "getPreviousVersions";
    identifier = strip(identifier);

    if (identifier == null) {
      throw invalidParameters("Identifier is required");
    }
    if (entityClass == null) {
      throw invalidParameters("Entity class is required");
    }

    String original = identifier;
    identifier = toUUID(identifier);

    List<IndexEntityVersion<T>> output = new LinkedList<>(); // sorting is important

    if (identifier == null) {
      log.warn(method, "requested previous versions of missing item " + original);
      return output;
    }

    Version[] rawVersions;

    try {
      Node node = new Node();
      node.setUid(identifier);

      rawVersions = getEcmengineManagementInterfaceService().getAllVersions(node, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error getting previous versions for item " + identifier, e);
    }

    for (Version v : rawVersions) {
      output.add(map(v, entityClass));
    }

    return output;
  }

  private <T extends IndexEntity> IndexEntityVersion<T> map(Version raw, Class<T> classs) {
    IndexEntityVersion<T> output = new IndexEntityVersion<>();

    output.setCreatedDate(raw.getCreatedDate() != null
        ? ZonedDateTime.ofInstant(raw.getCreatedDate().toInstant(), ZoneId.systemDefault())
            : null);
    output.setCreator(raw.getCreator());
    output.setDescription(raw.getDescription());
    output.setUid(raw.getVersionedNode() != null ? raw.getVersionedNode().getUid() : null);
    output.setVersionLabel(raw.getVersionLabel());

    output.setProperties(new ArrayList<>());
    if (raw.getVersionProperties() != null) {
      for (Property p : raw.getVersionProperties()) {
        IndexRawProperty outputProp = new IndexRawProperty();
        outputProp.setMultivalue(p.isMultivalue());
        outputProp.setPrefixedName(p.getPrefixedName());
        outputProp.setValues(p.getValues());
        output.getProperties().add(outputProp);
      }
    }

    // set proxy binding
    setIndexBindingProxy(output, new IndexBindingProxyForVersion<T>(this, classs, output.getUid()));

    return output;
  }

  private <T extends IndexEntity> void doCancelCheckOut(T entity) {
    String method = "doCancelCheckOut";

    if (entity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    try {
      Node node = new Node();
      node.setUid(entity.getUid());
      getEcmengineManagementInterfaceService().cancelCheckOutContent(node, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error cancelling content checkout for node " + entity.getUid(), e);
    }

    log.debug(method, "cancelled checkout content [" + entity.getUid() + "]");
  }

  private <T extends IndexEntity> T doCheckOut(String identifier, Class<T> entityClass) {
    String method = "doCheckOut";
    identifier = strip(identifier);

    if (identifier == null) {
      throw invalidParameters("Identifier is required");
    }
    if (entityClass == null) {
      throw invalidParameters("Entity class is required");
    }

    String original = identifier;
    identifier = toUUID(identifier);

    if (identifier == null) {
      log.warn(method, "requested checkout of missing item " + original);
      return null;
    }

    Node checkoutResult;

    try {
      Node node = new Node();
      node.setUid(identifier);
      checkoutResult =
          getEcmengineManagementInterfaceService().checkOutContent(node, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error checking out content " + identifier, e);
    }

    log.debug(method, "checked out content from source [ {} ] to new node [ {} ]", identifier,
        checkoutResult.getUid());

    return doFind(checkoutResult.getUid(), entityClass);
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doCheckOut(T entity) {
    String method = "doCheckOut";

    if (entity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    Node checkoutResult;

    try {
      Node node = new Node();
      node.setUid(entity.getUid());
      checkoutResult =
          getEcmengineManagementInterfaceService().checkOutContent(node, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error checking out content " + entity.getUid(), e);
    }

    log.debug(method, "checked out content from source [" + entity.getUid() + "] to new node ["
        + checkoutResult.getUid() + "]");

    return (T) doFind(checkoutResult.getUid(), entity.getClass());
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doCheckIn(T entity) {
    String method = "doCheckIn";

    if (entity == null) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    Node checkinResult;

    try {
      Node node = new Node();
      node.setUid(entity.getUid());
      checkinResult =
          getEcmengineManagementInterfaceService().checkInContent(node, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error checking in content " + entity.getUid(), e);
    }

    log.debug(method, "checked in content from source [" + entity.getUid() + "] to new node ["
        + checkinResult.getUid() + "]");

    return (T) doFind(checkinResult.getUid(), entity.getClass());
  }

  private String toUUID(String identifier) {
    String method = "toUUID";
    if (identifier == null) {
      return null;
    }

    identifier = strip(identifier);

    if (!isUUID(identifier)) {
      String before = identifier;
      log.debug(method, "passed path, transforming to uuid [" + before + "]");
      identifier = getUid(parsePath(identifier));
      log.debug(method,
          "passed path, transformed to uuid [" + before + "] -> [" + identifier + "]");
    }

    return identifier;
  }

  @SuppressWarnings({"unchecked"})
  private <T extends IndexFolder> T doCreateFolder(String inputPathOrUUID, T entity) {
    inputPathOrUUID = strip(inputPathOrUUID);

    String[] parsed = getParentUUIDAndFilename(inputPathOrUUID, entity.getFoldername());
    String effectiveFilename = parsed[1];
    String effectiveParentUUID = parsed[0];

    entity.setFoldername(effectiveFilename);

    IndexPayload metadati = entityToPayload(entity);

    try {
      Node node = new Node();
      node.setUid(effectiveParentUUID);

      Content content = new Content();
      content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
      content.setModelPrefixedName(metadati.getModelPrefixedName());
      content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
      content.setEncoding(metadati.getEnconding());
      content.setMimeType(metadati.getMimeType());
      content.setTypePrefixedName(metadati.getTypePrefixedName());
      content.setPrefixedName(metadati.getPrefixedName()); // non generato random
      content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));
      content.setAspects(metadati.getIndexAspects().toArray(new Aspect[] {}));

      Node resultIndex =
          getEcmengineManagementInterfaceService().createContent(node, content, operationContext);

      setUid(entity, resultIndex.getUid());

      return (T) doFindFolder(entity.getUid(), entity.getClass());

    } catch (Exception e) {
      throw wrap("Error creating content in folder " + effectiveParentUUID, e);
    }
  }

  private String doCreateFolder(String path) {
    path = strip(path);
    return createRelativePathIfMissing(path);
  }

  private List<String> doSearch(String parent, String text) {
    parent = strip(parent);
    if (StringUtils.isBlank(parent)) {
      throw invalidParameters("Parent folder path or UUID to base search is required");
    }

    if (isUUID(parent)) {
      parent = getPath(parent);
    } else {
      parent = parsePath(parent);
    }

    if (StringUtils.isBlank(parent)) {
      throw invalidParameters("Parent folder to base search is missing");
    }

    int pageSize = 50;

    List<String> output = new LinkedList<>();
    String query = "PATH:\"" + parent + "//*\"";

    if (!StringUtils.isBlank(text)) {
      query += " AND TEXT:\"" + text + "\"";
    }

    SearchParams sp = new SearchParams();
    sp.setLuceneQuery(query);
    sp.setLimit(0);
    sp.setPageIndex(0);
    sp.setPageSize(pageSize);
    sp.setDualQuery(true);

    final String method = "search";
    log.debug(method, "executing lucene search with query " + query);

    try {

      NodeResponse nr = doSearchPage(sp, output);

      while (output.size() < nr.getTotalResults()) {
        sp.setPageIndex(sp.getPageIndex() + 1);
        doSearchPage(sp, output);
      }

      log.debug(method, "loaded in total " + output.size() + " results");
      return output;

    } catch (Exception e) {
      throw wrap("Error searching fullText with query [" + text + "]", e);
    }
  }

  private NodeResponse doSearchPage(SearchParams sp, List<String> output) throws RemoteException {
    NodeResponse nr =
        getEcmengineManagementInterfaceService().luceneSearchNoMetadata(sp, operationContext);
    Node[] nodes = nr.getNodeArray();
    log.debug("search", "got " + nodes.length + " results in page " + sp.getPageIndex() + ", total "
        + nr.getTotalResults());
    for (Node node : nodes) {
      if (output.contains(node.getUid())) {
        throw wrap("DUPLICATE SEARCH RESULT IN DIFFERENT PAGE: " + node.getUid());
      }

      output.add(node.getUid());
    }
    return nr;
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doCreate(String inputPathOrUUID, T entity)
      throws RemoteException {
    inputPathOrUUID = strip(inputPathOrUUID);

    String[] parsed = getParentUUIDAndFilename(inputPathOrUUID, entity.getFilename());
    String effectiveFilename = parsed[1];
    String effectiveParentUUID = parsed[0];

    entity.setFilename(effectiveFilename);

    IndexPayload metadati = entityToPayload(entity);

    String mimeType = metadati.getMimeType();
    if (StringUtils.isBlank(mimeType)) {
      mimeType = MIME_TYPE_DEFAULT;
    }

    Node node = new Node();
    node.setUid(effectiveParentUUID);

    Content content = new Content();
    content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
    content.setModelPrefixedName(metadati.getModelPrefixedName());
    content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
    content.setContent(entity.getContent());
    content.setEncoding(metadati.getEnconding());
    content.setMimeType(mimeType);
    content.setTypePrefixedName(metadati.getTypePrefixedName());
    content.setPrefixedName(metadati.getPrefixedName());
    // content.setPrefixedName("cm:" + UUID.randomUUID().toString());
    content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));
    content.setAspects(metadati.getIndexAspects().toArray(new Aspect[] {}));

    Node resultIndex = null;
    while (resultIndex == null) {
      try {
        resultIndex =
            getEcmengineManagementInterfaceService().createContent(node, content, operationContext);
      } catch (Exception e) {
        if (wrap(e) instanceof Index2DuplicateNodeException) {
          renameWithPostfix(metadati.getPrefixedName(), generateRandomStringPostfix(6), content,
              entity);
        } else {
          throw e;
        }
      }
    }

    setUid(entity, resultIndex.getUid());

    return (T) doFind(entity.getUid(), entity.getClass());
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doCreate(String inputPathOrUUID, T entity,
      String tempContentUUID) throws RemoteException {
    inputPathOrUUID = strip(inputPathOrUUID);

    String[] parsed = getParentUUIDAndFilename(inputPathOrUUID, entity.getFilename());
    String effectiveFilename = parsed[1];
    String effectiveParentUUID = parsed[0];

    entity.setFilename(effectiveFilename);

    IndexPayload metadati = entityToPayload(entity);

    String mimeType = metadati.getMimeType();
    if (StringUtils.isBlank(mimeType)) {
      mimeType = MIME_TYPE_DEFAULT;
    }

    Node node = new Node();
    node.setUid(effectiveParentUUID);

    Content content = new Content();
    content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
    content.setModelPrefixedName(metadati.getModelPrefixedName());
    content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
    // content.setContent(entity.getContent());
    content.setEncoding(metadati.getEnconding());
    content.setMimeType(mimeType);
    content.setTypePrefixedName(metadati.getTypePrefixedName());
    content.setPrefixedName(metadati.getPrefixedName());
    // content.setPrefixedName("cm:" + UUID.randomUUID().toString());
    content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));
    content.setAspects(metadati.getIndexAspects().toArray(new Aspect[] {}));

    Node sourceNode = new Node();
    sourceNode.setUid(tempContentUUID);

    Node resultIndex = null;
    while (resultIndex == null) {
      try {
        resultIndex = getEcmengineManagementInterfaceService().createContentFromTemporaney(node,
            content, operationContext, sourceNode);
      } catch (Exception e) {
        if (wrap(e) instanceof Index2DuplicateNodeException) {
          renameWithPostfix(metadati.getPrefixedName(), generateRandomStringPostfix(6), content,
              entity);
        } else {
          throw e;
        }
      }
    }

    setUid(entity, resultIndex.getUid());

    return (T) doFind(entity.getUid(), entity.getClass());
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doCreate(String inputPathOrUUID, T entity,
      InputStream contentStream) {
    inputPathOrUUID = strip(inputPathOrUUID);

    String[] parsed = getParentUUIDAndFilename(inputPathOrUUID, entity.getFilename());
    String effectiveFilename = parsed[1];
    String effectiveParentUUID = parsed[0];

    entity.setFilename(effectiveFilename);

    IndexPayload metadati = entityToPayload(entity);

    String mimeType = metadati.getMimeType();
    if (StringUtils.isBlank(mimeType)) {
      mimeType = MIME_TYPE_DEFAULT;
    }

    Node node = new Node();
    node.setUid(effectiveParentUUID);

    Content content = new Content();
    content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
    content.setModelPrefixedName(metadati.getModelPrefixedName());
    content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
    content.setContent(new byte[1]);
    content.setEncoding(metadati.getEnconding());
    content.setMimeType(mimeType);
    content.setTypePrefixedName(metadati.getTypePrefixedName());
    content.setPrefixedName(metadati.getPrefixedName());
    // content.setPrefixedName("cm:" + UUID.randomUUID().toString());
    content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));
    content.setAspects(metadati.getIndexAspects().toArray(new Aspect[] {}));

    Node resultIndex = null;

    log.info("doCreate", "node: " + ObjectUtils.toJson(node));
    log.info("doCreate", "payload: " + ObjectUtils.toJson(content));

    while (resultIndex == null) {
      try {
        resultIndex =
            getEcmengineManagementInterfaceService().createContent(node, content, operationContext);
      } catch (Exception e) {
        if (wrap(e) instanceof Index2DuplicateNodeException) {
          renameWithPostfix(metadati.getPrefixedName(), generateRandomStringPostfix(6), content,
              entity);
        } else {
          throw wrap("Error pre-creating node for MTOM upload", e);
        }
      }
    }

    EcmEngineMtomDelegateImpl motmDelegate = getEcmengineMtomDelegate();

    MtomNode inNode = new MtomNode();
    inNode.setUid(resultIndex.getUid());
    inNode.setPrefixedName(CM_CONTENT);

    Attachment inAttachment = new Attachment();
    inAttachment.attachmentDataHandler =
        new Index2DataHandler(contentStream, metadati.getMimeType());
    inAttachment.fileName = effectiveFilename;
    inAttachment.fileType = metadati.getMimeType();

    MtomNode mtomNode;
    try {
      mtomNode = motmDelegate.directUploadMethod(inAttachment, inNode, mtomOperationContext);
    } catch (MtomClientException e) {
      throw wrap("Errore uploading content via MTOM on pre-created node", e);
    }

    setUid(entity, mtomNode.getUid());

    return (T) doFind(mtomNode.getUid(), entity.getClass());
  }

  private String addNamePostfix(String name, String postfix) {
    if (!name.contains(".")) {
      return name + "-" + postfix + "";
    }
    int position = name.lastIndexOf(".");
    String onlyName = name.substring(0, position);
    String onlyExt = name.substring(position + 1);
    return onlyName + "-" + postfix + "." + onlyExt;
  }

  private void renameWithPostfix(String name, String postfix, Content target, IndexEntity entity) {
    if (name.startsWith(METADATI_PREFIXED_NAME)) {
      name = name.substring(METADATI_PREFIXED_NAME.length());
    }
    String newName = addNamePostfix(name, postfix);
    target.setPrefixedName(METADATI_PREFIXED_NAME + newName);
    entity.setFilename(newName);

    if (target.getProperties() != null) {
      for (Property property : target.getProperties()) {
        if (property.getPrefixedName().equals(CM_NAME)) {
          property.setValues(new String[] {newName});
        }
      }
    }
  }

  private String generateRandomStringPostfix(int len) {
    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] symbols = upper.toCharArray();
    char[] buf = new char[len];
    for (int idx = 0; idx < buf.length; ++idx)
      buf[idx] = symbols[random.nextInt(symbols.length)];
    return new String(buf);
  }

  private String[] getParentUUIDAndFilename(String inputPathOrUUID, String filenameOnEntity) {
    inputPathOrUUID = strip(inputPathOrUUID);

    String methodName = "getParentUUIDAndFilename";

    String[] result;

    if (isUUID(inputPathOrUUID)) {
      // input is UUID
      log.debug(methodName, "input is UUID");
      result = getParentUUIDAndFilenameFromUUID(inputPathOrUUID, filenameOnEntity);
    } else {
      // input is PATH
      log.debug(methodName, "input is PATH");
      result = getParentUUIDAndFilenameFromPath(inputPathOrUUID, filenameOnEntity);
    }

    log.debug(methodName, "resulting parent UUID = " + result[0]);
    log.debug(methodName, "resulting filename = " + result[1]);

    return result;
  }

  private String[] getParentUUIDAndFilenameFromUUID(String inputPathOrUUID,
      String filenameOnEntity) {
    inputPathOrUUID = strip(inputPathOrUUID);

    String methodName = "getParentUUIDAndFilenameFromUUID";
    boolean haveFilenameInEntity = !StringUtils.isEmpty(filenameOnEntity);

    String effectiveFilename = null;
    String effectiveParentUUID = null;

    if (haveFilenameInEntity) {
      log.debug(methodName, "have filename in entity");
      effectiveParentUUID = inputPathOrUUID;
      effectiveFilename = filenameOnEntity;
    } else {
      log.debug(methodName, "not have filename in entity");
      throw invalidParameters("Error creating document: filename is required when input is UUID");
    }

    return new String[] {effectiveParentUUID, effectiveFilename};
  }

  private String[] getParentUUIDAndFilenameFromPath(String inputPathOrUUID,
      String filenameOnEntity) {
    inputPathOrUUID = strip(inputPathOrUUID);

    String methodName = "getParentUUIDAndFilenameFromPath";
    boolean haveFilenameInEntity = !StringUtils.isEmpty(filenameOnEntity);

    String effectiveFilename = null;
    String effectiveParentUUID = null;

    if (haveFilenameInEntity) {
      log.debug(methodName, "have filename in entity");

      effectiveFilename = filenameOnEntity;
      if (parsePath(inputPathOrUUID).endsWith("/cm:" + effectiveFilename)
          || parsePath(inputPathOrUUID).endsWith(PATH_SEPARATOR + effectiveFilename)) {
        log.debug(methodName, "redundant filename in path, extracting");
        String[] splitted = splitOnLastToken(inputPathOrUUID);
        if (splitted.length < 1) {
          throw wrap("Invalid path provided: " + inputPathOrUUID);
        }
        effectiveParentUUID = createRelativePathIfMissing(splitted[0]);
        effectiveFilename = cleanFileName(splitted[1]);
      } else {
        log.debug(methodName, "no redundant filename in path");
        effectiveParentUUID = createRelativePathIfMissing(inputPathOrUUID);
      }
    } else {
      log.debug(methodName, "not have filename in entity");

      String[] splitted = splitOnLastToken(inputPathOrUUID);
      if (splitted.length < 1) {
        throw wrap("Invalid path provided: " + inputPathOrUUID);
      }
      effectiveParentUUID = createRelativePathIfMissing(splitted[0]);
      effectiveFilename = cleanFileName(splitted[1]);
    }

    return new String[] {effectiveParentUUID, effectiveFilename};
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doSave(T entity) {
    String uuid = entity.getUid();

    if (StringUtils.isEmpty(uuid)) {
      throw invalidParameters("UUID is required for content update on entity " + entity);
    }

    Node node = new Node();
    node.setUid(uuid);

    Content content = new Content();

    IndexPayload metadati = entityToPayload(entity);

    boolean updateContent = entity.isContentChanged();

    content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
    content.setModelPrefixedName(metadati.getModelPrefixedName());
    content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
    content.setEncoding(metadati.getEnconding());
    content.setMimeType(metadati.getMimeType());
    content.setTypePrefixedName(metadati.getTypePrefixedName());
    // content.setPrefixedName(metadati.getPrefixedName()); // NULL ?
    content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));

    content.setContent(new byte[] {});
    content.setAspects(new Aspect[0]);

    setUid(entity, uuid);

    try {

      getEcmengineManagementInterfaceService().updateMetadata(node, content, operationContext);

      if (updateContent) {
        content.setContent(entity.getContent());
        getEcmengineManagementInterfaceService().updateContentData(node, content, operationContext);
      }

      return (T) doFind(entity.getUid(), entity.getClass());

    } catch (Exception e) {
      throw wrap("Error updating content in folder " + uuid, e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexFolder> T doSaveFolder(T entity) {
    String uuid = entity.getUid();

    if (StringUtils.isEmpty(uuid)) {
      throw invalidParameters("UUID is required for content update on entity " + entity);
    }

    Node node = new Node();
    node.setUid(uuid);

    Content content = new Content();

    IndexPayload metadati = entityToPayload(entity);

    content.setContentPropertyPrefixedName(metadati.getContentPropertyPrefixedName());
    content.setModelPrefixedName(metadati.getModelPrefixedName());
    content.setParentAssocTypePrefixedName(metadati.getParentAssocTypePrefixedName());
    content.setTypePrefixedName(metadati.getTypePrefixedName());
    // content.setPrefixedName(metadati.getPrefixedName()); // NULL ?
    content.setProperties(metadati.getIndexProperties().toArray(new Property[] {}));

    content.setContent(new byte[] {});
    content.setAspects(new Aspect[0]);

    setUid(entity, uuid);

    try {
      getEcmengineManagementInterfaceService().updateMetadata(node, content, operationContext);
      return (T) doFindFolder(entity.getUid(), entity.getClass());
    } catch (Exception e) {
      throw wrap("Error updating folder metadata " + uuid, e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexEntity> T doFind(String uuid, Class<T> entityClass) {
    uuid = strip(uuid);
    if (StringUtils.isEmpty(uuid)) {
      throw invalidParameters("UUID is required for entity retrieval");
    }

    uuid = toUUID(uuid);

    if (uuid == null) {
      return null;
    }

    ResultContent resultContent = this.getResultContent(uuid);

    if (CM_FOLDER.equals(resultContent.getTypePrefixedName())) {
      throw wrap("Requested node " + uuid + " is not an entity but a folder");
    }

    if (entityClass == null) {
      entityClass = (Class<T>) getCorrespondingKnownClass(resultContent);
      if (entityClass == null) {
        entityClass = (Class<T>) GenericIndexContent.class;
      }
    }

    return payloadToEntity(resultContent, entityClass);
  }

  @SuppressWarnings("unchecked")
  private <T extends IndexFolder> T doFindFolder(String uuid, Class<T> entityClass) {
    uuid = strip(uuid);
    uuid = toUUID(uuid);

    if (uuid == null) {
      return null;
    }

    ResultContent resultContent = this.getResultContent(uuid);

    if (entityClass == null) {
      entityClass = (Class<T>) getCorrespondingKnownFolderClass(resultContent);
      if (entityClass == null) {
        entityClass = (Class<T>) GenericIndexFolder.class;
      }
    }

    return payloadToFolder(resultContent, entityClass);
  }

  private IndexFolder doFindFolder(String uuid) {
    return this.doFindFolder(uuid, GenericIndexFolder.class);
  }

  private IndexEntity doFind(String uuid) {
    uuid = strip(uuid);
    return this.doFind(uuid, null);
  }

  @SuppressWarnings("unchecked")
  protected <T extends IndexEntity> T doFindVersion(String uuid, Class<T> entityClass) {
    uuid = strip(uuid);
    if (StringUtils.isEmpty(uuid)) {
      throw invalidParameters("UUID is required for entity retrieval");
    }

    uuid = toUUID(uuid);

    if (uuid == null) {
      return null;
    }

    ResultContent resultContent = this.getResultContentForVersion(uuid);

    if (entityClass == null) {
      entityClass = (Class<T>) getCorrespondingKnownClass(resultContent);
      if (entityClass == null) {
        throw wrap(ERR_COULD_NOT_AUTODETECT_CLASS + uuid);
      }
    }

    return payloadToEntity(resultContent, entityClass, true, uuid);
  }

  private <T extends IndexEntity> boolean doDelete(T entity) {
    if (entity == null || StringUtils.isEmpty(entity.getUid())) {
      throw invalidParameters("UID is required to delete entity " + entity);
    }

    try {
      Node node = new Node();
      node.setUid(entity.getUid());
      getEcmengineManagementInterfaceService().deleteContent(node, operationContext);
      return true;

    } catch (NoSuchNodeException e) {
      log.warn("delete", "Richiesta eliminazione di nodo mancante: " + entity.getUid() + " ("
          + e.getErrorMessage() + ")");
      return false;

    } catch (Exception e) {
      throw wrap("Error deleting node " + entity.getUid(), e);
    }
  }


  private boolean doDelete(String identifier) {
    identifier = strip(identifier);
    identifier = toUUID(identifier);

    if (identifier == null) {
      log.warn("delete", "requested deletion of missing entity: " + identifier);
      return false;
    }

    return deleteNode(identifier);
  }

  private void doRestore(String identifier) {
    identifier = strip(identifier);
    identifier = toUUID(identifier);

    restoreContent(identifier);
  }

  private void doMove(String uuidNode, String uuidTo) {
    uuidNode = strip(uuidNode);
    uuidTo = strip(uuidTo);
    uuidNode = toUUID(uuidNode);
    uuidTo = toUUID(uuidTo);

    try {
      Node node = new Node();
      node.setUid(uuidNode);
      Node nodeTo = new Node();
      nodeTo.setUid(uuidTo);
      getEcmengineManagementInterfaceService().moveNode(node, nodeTo, operationContext);
    } catch (Exception e) {
      throw wrap("Error moving node " + uuidNode + " to " + uuidTo, e);
    }
  }

  // HELPERS

  private String retrieveSignatureType(String identifier) {
    if (StringUtils.isBlank(identifier)) {
      throw invalidParameters("UUID is required for signatureType retrieval");
    }
    identifier = toUUID(identifier);
    Node node = new Node();
    node.setUid(identifier);

    Content content = new Content();
    content.setContentPropertyPrefixedName(CM_CONTENT);

    try {
      return getEcmengineManagementInterfaceService().getSignatureType(node, content,
          operationContext);
    } catch (RemoteException e) {
      throw wrap("Error getting file signature type", e);
    }
  }

  private FileFormatInfo[] retrieveFileFormatInfo(String identifier) {
    return retrieveFileFormatInfo(identifier, operationContext);
  }

  private FileFormatInfo[] retrieveFileFormatInfo(String identifier, OperationContext oc) {
    if (StringUtils.isBlank(identifier)) {
      throw invalidParameters("UUID is required for fileFormatInfo retrieval");
    }
    identifier = toUUID(identifier);
    Node node = new Node();
    node.setUid(identifier);

    Content content = new Content();
    content.setContentPropertyPrefixedName(CM_CONTENT);

    try {
      return getEcmengineManagementInterfaceService().getFileFormatInfo(node, content, oc);
    } catch (RemoteException e) {
      throw wrap("Error getting file format info", e);
    }
  }

  private String retrieveSignatureType(byte[] content) {
    if (content == null || content.length < 1) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    try {
      return getEcmengineManagementInterfaceService().getSignatureType(content, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error getting file signature type", e);
    }
  }

  private FileFormatInfo[] retrieveFileFormatInfo(byte[] content) {
    if (content == null || content.length < 1) {
      throw invalidParameters(ENTITY_IS_REQUIRED);
    }

    FileInfo fileInfo = new FileInfo();
    fileInfo.setContents(content);

    try {
      return getEcmengineManagementInterfaceService().getFileFormatInfo(fileInfo, operationContext);
    } catch (RemoteException e) {
      throw wrap("Error getting file format info", e);
    }
  }

  private String createRelativePathIfMissing(String path) {
    path = parsePath(path);

    final String method = "createRelativePathIfMissing";
    log.debug(method, "createRelativePath: checking node " + path);
    String existingUUID = getNode(path);
    if (existingUUID == null) {
      log.debug(method, "createRelativePath: node " + path + " is missing.");

      if (pathEquals(path, rootNodeName)) {
        throw wrap("Root node is missing for path " + path);
      }

      if (path.contains(PATH_SEPARATOR) && path.lastIndexOf(PATH_SEPARATOR) != 0) {
        int lastSlash = path.lastIndexOf(PATH_SEPARATOR);
        String lastPathToken = path.substring(lastSlash + 1);
        String parentAbsolutePath = path.substring(0, lastSlash);
        log.debug(method, "createRelativePath: resolving parent node " + parentAbsolutePath);
        String parentUUID = createRelativePathIfMissing(parentAbsolutePath);
        log.debug(method, "createRelativePath: resolved parent node " + parentAbsolutePath
            + " to UUID " + parentUUID);
        log.debug(method,
            "createRelativePath: creating node " + lastPathToken + " into node " + parentUUID);
        existingUUID = createFolder(parentUUID, lastPathToken);
        log.debug(method, "createRelativePath: created required node " + existingUUID);

      } else {
        throw wrap("Root folder is missing for path " + path);
      }
    }

    return existingUUID;
  }

  private boolean pathEquals(String p1, String p2) {
    if (StringUtils.isEmpty(p1) || StringUtils.isEmpty(p2)) {
      return false;
    }

    p1 = PATH_SEPARATOR + p1.trim() + PATH_SEPARATOR;
    p2 = PATH_SEPARATOR + p2.trim() + PATH_SEPARATOR;

    return p1.replaceAll("\\/{2,}", "\\/").equals(p2.replaceAll("\\/{2,}", "\\/"));
  }

  private String parsePath(String path) {
    // path is like: '/documenti/2019/'

    log.trace("parsePath", "parsePath: [" + path + "] -> [...]");


    path = path.toLowerCase();

    while (path.contains(PATH_SEPARATOR + PATH_SEPARATOR)) {
      path = path.replace(PATH_SEPARATOR + PATH_SEPARATOR, PATH_SEPARATOR);
    }

    if (StringUtils.isBlank(path)) {
      path = "";
    }

    path = strip(path);

    if (!path.startsWith(PATH_SEPARATOR)) {
      path = PATH_SEPARATOR + path;
    }

    if ((path + PATH_SEPARATOR).startsWith(rootNodeName + PATH_SEPARATOR)) {
      path = PATH_SEPARATOR + path.substring(rootNodeName.length());
    }

    while (path.contains("//")) {
      path = path.replace("//", PATH_SEPARATOR);
    }

    while (path.endsWith(PATH_SEPARATOR)) {
      path = path.substring(0, path.length() - 1);
    }

    path = path.replace(PATH_SEPARATOR, "/cm:");
    path = path.replace("/cm:cm:", "/cm:");

    while (path.contains("//")) {
      path = path.replace("//", PATH_SEPARATOR);
    }

    while (path.endsWith(PATH_SEPARATOR)) {
      path = path.substring(0, path.length() - 1);
    }

    path = PATH_SEPARATOR + rootNodeName + PATH_SEPARATOR + path.trim();

    while (path.contains("//")) {
      path = path.replace("//", PATH_SEPARATOR);
    }

    while (path.endsWith(PATH_SEPARATOR)) {
      path = path.substring(0, path.length() - 1);
    }

    log.trace("parsePath", "parsePath: [...] -> [" + path + "]");

    return path;
  }

  private String getNode(String xPathQuery) {
    String result = null;
    try {
      SearchParams searchParams = new SearchParams();
      searchParams.setXPathQuery(xPathQuery);
      searchParams.setLimit(1);

      result = getEcmengineManagementInterfaceService().nodeExists(searchParams, operationContext);
    } catch (NoDataExtractedException e) {
      // node is missing. return null
      return null;
    } catch (Exception e) {
      throw wrap("Error getting node " + xPathQuery, e);
    }

    return result;
  }

  private static String[] splitOnLastToken(String raw) {
    if (raw == null) {
      return new String[] {};
    }
    while (raw.endsWith(PATH_SEPARATOR)) {
      raw = raw.substring(0, raw.length() - 1);
    }
    if (!raw.contains(PATH_SEPARATOR)) {
      return new String[] {};
    } else {
      int index = raw.lastIndexOf(PATH_SEPARATOR);
      return new String[] {raw.substring(0, index), raw.substring(index + 1)};
    }
  }

  private static String cleanFileName(String raw) {
    if (raw == null) {
      return raw;
    }
    raw = raw.trim();
    while (raw.startsWith(PATH_SEPARATOR)) {
      raw = raw.substring(1);
    }
    while (raw.endsWith(PATH_SEPARATOR)) {
      raw = raw.substring(0, raw.length() - 1);
    }
    if (raw.startsWith("cm:")) {
      raw = raw.substring(3);
    }
    return raw;
  }

  private ResultContent getResultContent(String uuid) {
    ResultContent resultIndex = null;
    try {
      Node node = new Node();
      node.setUid(uuid);
      resultIndex =
          getEcmengineManagementInterfaceService().getContentMetadata(node, operationContext);

    } catch (Exception e) {
      throw wrap("Error getting result content for node " + uuid, e);
    }

    return resultIndex;
  }

  private ResultContent getResultContentForVersion(String uuid) {
    ResultContent resultIndex = null;
    try {
      Node node = new Node();
      node.setUid(uuid);
      resultIndex =
          getEcmengineManagementInterfaceService().getVersionMetadata(node, operationContext);

    } catch (Exception e) {
      throw wrap("Error getting result content for node version " + uuid, e);
    }

    return resultIndex;
  }

  protected Attachment retrieveContentAttachment(String uuid, String contentPropertyPrefixedName) {
    try {
      MtomNode mtomNode = new MtomNode();
      mtomNode.setUid(uuid);
      mtomNode.setPrefixedName(contentPropertyPrefixedName);
      return getEcmengineMtomDelegate().downloadMethod(mtomNode, mtomOperationContext);

    } catch (Exception e) {
      throw wrap("Error retrieving content attachment via MTOM from node " + uuid, e);
    }
  }

  protected Index2ContentAttachment retrieveAttachmentDTO(String uuid,
      String contentPropertyPrefixedName) {

    try {
      Attachment raw = retrieveContentAttachment(uuid, contentPropertyPrefixedName);

      //@formatter:off
      return Index2ContentAttachment.builder()
          .withContentType(raw.fileType)
          .withFileName(raw.fileName)
          .withFileSize(raw.fileSize)
          .withContentStream(raw.attachmentDataHandler.getInputStream())
          .build();
      //@formatter:on

    } catch (Exception e) {
      throw wrap("Error retrieving content attachment DTO via MTOM from node " + uuid, e);
    }
  }

  protected byte[] retrieveContentData(String uuid, String contentPropertyPrefixedName) {
    String method = "retrieveContentData";
    byte[] result = null;
    try {
      if (DOWNLOAD_POLICY_MTOM) {
        Attachment downloadResult = retrieveContentAttachment(uuid, contentPropertyPrefixedName);
        log.info(method, "downloading index content {} via MTOM", uuid);

        result = Index2MtomDownloadHelper.downloadWithTimeout(
            downloadResult.attachmentDataHandler.getInputStream(),
            Index2MtomDownloadHelper.getTimeoutSecondsBySize(downloadResult.fileSize),
            TimeUnit.SECONDS);

        log.info(method, "downloaded index content {} via MTOM", uuid);
      } else {
        Node node = new Node();
        node.setUid(uuid);
        Content content = new Content();
        content.setContentPropertyPrefixedName(contentPropertyPrefixedName);

        log.info(method, "downloading index content {} via WS", uuid);
        result = getEcmengineManagementInterfaceService().retrieveContentData(node, content,
            operationContext);
        log.info(method, "downloaded index content {} via WS", uuid);
      }

    } catch (Exception e) {
      throw wrap("Error retrieving content data from node " + uuid, e);
    }
    return result;
  }

  protected byte[] retrieveVersionContentData(String uuid, String contentPropertyPrefixedName) {
    byte[] result = null;
    try {
      Node node = new Node();
      node.setUid(uuid);
      Content content = new Content();
      content.setContentPropertyPrefixedName(contentPropertyPrefixedName);
      result = getEcmengineManagementInterfaceService().retrieveVersionContentData(node, content,
          operationContext);
    } catch (Exception e) {
      throw wrap("Error retrieving content data from node version " + uuid, e);
    }
    return result;
  }

  private String createFolder(String uuidNodeParent, String nameFolder) {
    String result = null;

    if (!nameFolder.startsWith("cm:")) {
      nameFolder = "cm:" + nameFolder;
    }

    Node nodeParent = new Node(uuidNodeParent);

    Content folder = new Content();
    folder.setParentAssocTypePrefixedName(CM_CONTAINS);
    folder.setPrefixedName(nameFolder);
    folder.setModelPrefixedName("cm:contentmodel");
    folder.setTypePrefixedName(CM_FOLDER);

    try {
      result = getEcmengineManagementInterfaceService()
          .createContent(nodeParent, folder, operationContext).getUid();
    } catch (Exception e) {
      throw wrap("Error creating folder " + nameFolder, e);
    }
    return result;
  }

  private boolean deleteNode(String uuid) {
    uuid = toUUID(uuid);

    try {
      Node node = new Node();
      node.setUid(uuid);
      getEcmengineManagementInterfaceService().deleteContent(node, operationContext);
      return true;
    } catch (NoSuchNodeException e) {
      log.warn("deleteNode",
          "Richiesta eliminazione di nodo mancante: " + uuid + " (" + e.getErrorMessage() + ")");
      return false;

    } catch (Exception e) {
      throw wrap("Error deleting node " + uuid, e);
    }
  }

  private void restoreContent(String uuid) {
    try {
      Node node = new Node();
      node.setUid(uuid);
      getEcmengineManagementInterfaceService().restoreContent(node, operationContext);
    } catch (Exception e) {
      throw wrap("Error restoring content for node " + uuid, e);
    }
  }

  private static String toPrefixedPropertyName(String raw, IndexModel modelAnnotation) {
    if (raw.contains(":")) {
      return raw;
    } else {
      if (StringUtils.isEmpty(modelAnnotation.prefix())) {
        throw invalidParameters("Expected prefix value on IndexModel annotation is missing");
      }

      return modelAnnotation.prefix() + ":" + raw;
    }
  }

  private static IndexModel getModelFromClass(Class<?> classs) {
    return classs.getAnnotation(IndexModel.class);
  }

  private static Set<Field> findFields(Class<?> classs, Class<? extends Annotation> ann) {
    Set<Field> set = new HashSet<>();
    Class<?> c = classs;
    while (c != null) {
      for (Field field : c.getDeclaredFields()) {
        if (field.isAnnotationPresent(ann)) {
          set.add(field);
        }
      }
      c = c.getSuperclass();
    }
    return set;
  }

  private static IndexPropertyDataType getDataTypeFromClass(Class<?> classs) {

    if (String.class.isAssignableFrom(classs)) {
      return IndexPropertyDataType.TEXT;
    }

    if (Number.class.isAssignableFrom(classs)) {
      return IndexPropertyDataType.INT;
    }

    if (java.util.Date.class.isAssignableFrom(classs) || Calendar.class.isAssignableFrom(classs)
        || Temporal.class.isAssignableFrom(classs)) {
      return IndexPropertyDataType.DATE;
    }

    return IndexPropertyDataType.TEXT;
  }

  // NOTA: LA SCRITTURA DI ARRAY NON E' ANCORA STATA TESTATA
  @SuppressWarnings("unchecked")
  private static String[] getConvertedValueFromClassAndType(IndexPropertyDataType type,
      Object value, IndexProperty fieldAnnotation) {
    if (value == null) {
      return null;
    }

    if (value.getClass().isArray()) {
      Object[] valueAsArray = (Object[]) value;
      String[] outputArray = new String[valueAsArray.length];
      for (int i = 0; i < valueAsArray.length; i++) {
        outputArray[i] =
            getConvertedValueFromClassAndType(type, valueAsArray[i], fieldAnnotation)[0];
      }
      return outputArray;
    }

    if (fieldAnnotation != null && fieldAnnotation.converter() != null
        && fieldAnnotation.converter().length > 0) {
      return new String[] {
          ((IndexValueConverter<Object>) getConverterInstance(fieldAnnotation.converter()[0]))
          .serialize(value)};
    }

    if (type == IndexPropertyDataType.TEXT) {
      return new String[] {value.toString()};
    } else if (type == IndexPropertyDataType.INT) {
      return new String[] {((Number) value).toString()};
    } else if (type == IndexPropertyDataType.BOOLEAN) {
      return new String[] {((Boolean) value).toString()};
    } else if (type == IndexPropertyDataType.DATE) {
      if (value instanceof ZonedDateTime) {
        return new String[] {DateTimeFormatter.ISO_ZONED_DATE_TIME.format((ZonedDateTime) value)};
      } else if (value instanceof LocalDateTime) {
        return new String[] {DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) value)};
      } else if (value instanceof LocalDate) {
        return new String[] {DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) value)};
      } else if (value instanceof Temporal) {
        return new String[] {String.valueOf(Instant.from((Temporal) value).getEpochSecond())};
      } else {
        throw invalidParameters(
            "tipo di property non riconosciuta: " + type + ":" + value.getClass().getName());
      }
    }
    throw invalidParameters("tipo di property non riconosciuta: " + type);
  }

  private static IndexValueConverter<?> getConverterInstance(
      Class<? extends IndexValueConverter<?>> clazz) {
    if (clazz == null) {
      throw new InvalidParameterException();
    }
    if (converterCache.containsKey(clazz)) {
      return converterCache.get(clazz);
    }
    IndexValueConverter<?> instance;
    try {
      instance = clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw wrap("Impossibile istanziare il converter di tipo " + clazz.getName());
    }
    converterCache.put(clazz, instance);
    return instance;
  }

  private static Object getConvertedValueToClassAndType(String[] values, Class<?> targetClass,
      IndexProperty fieldAnnotation) {

    if (targetClass.isArray()) {

      Object[] outputArray = (Object[]) java.lang.reflect.Array
          .newInstance(targetClass.getComponentType(), values.length);
      int i = 0;
      for (String value : values) {
        outputArray[i++] = getConvertedValueToClassAndType(new String[] {value},
            targetClass.getComponentType(), fieldAnnotation);
      }
      return outputArray;
    }

    if (values == null || values.length < 1) {
      return null;
    }

    String value = values[0];

    if (fieldAnnotation != null && fieldAnnotation.converter() != null
        && fieldAnnotation.converter().length > 0) {
      return getConverterInstance(fieldAnnotation.converter()[0]).parse(value);
    }

    if (StringUtils.isEmpty(value)) {
      return null;
    }

    if (String.class.isAssignableFrom(targetClass)) {
      return value;
    } else if (Long.class.isAssignableFrom(targetClass)) {
      return Long.valueOf(value);
    } else if (Integer.class.isAssignableFrom(targetClass)) {
      return Integer.valueOf(value);
    } else if (Boolean.class.isAssignableFrom(targetClass)) {
      return Boolean.valueOf(value);
    } else if (LocalDate.class.isAssignableFrom(targetClass)) {
      ZonedDateTime parsedWhole = ZonedDateTime.parse(value);
      return parsedWhole.toLocalDate();
    } else if (ZonedDateTime.class.isAssignableFrom(targetClass)) {
      return ZonedDateTime.parse(value);
    } else if (LocalDateTime.class.isAssignableFrom(targetClass)) {
      ZonedDateTime parsedWhole = ZonedDateTime.parse(value);
      return parsedWhole.toLocalDateTime();
    } else {
      throw invalidParameters("Invalid target property class: " + targetClass.getName());
    }
  }

  private static boolean isUUID(String candidate) {
    // es. 2ff8b06a-77af-11e9-9bed-0f3a76de050f
    return candidate != null && strip(candidate).matches("([a-z0-9]{2,}\\-){3,}([a-z0-9]{2,})");
  }

  private static String strip(String raw) {
    if (raw == null) {
      return null;
    }
    return raw.strip().replaceAll("\\s", "").strip();
  }

  private <T extends IndexEntity> T payloadToEntity(ResultContent payload, Class<T> classs) {
    return payloadToEntity(payload, classs, false, null);
  }

  private <T extends IndexEntity> T payloadToEntity(ResultContent payload, Class<T> classs,
      boolean isVersion, String versionUUID) {
    if (payload == null) {
      return null;
    }
    if (classs == null) {
      throw invalidParameters("Class is required");
    }

    final String method = "payloadToEntity";
    log.debug(method, "parsing index payload to class " + classs.getName());

    IndexModel indexModel = getModelFromClass(classs);
    Set<Field> propertiesFields = findFields(classs, IndexProperty.class);
    Set<Field> aspectsFields = findFields(classs, IndexAspect.class);

    T output = create(classs);
    for (Field field : aspectsFields) {
      setFieldOnEntity(output, field.getName(), create(field.getType()));
    }

    if (log.isDebugEnabled()) {
      log.debug(method, "model = {}", resolveModel(indexModel));
      log.debug(method, "type = {}", resolveType(indexModel));
    }

    String filename = payload.getPrefixedName();
    if (filename.startsWith(METADATI_PREFIXED_NAME)) {
      filename = filename.substring(METADATI_PREFIXED_NAME.length());
    }

    setUid(output, payload.getUid());
    output.setEncoding(payload.getEncoding());
    output.setMimeType(payload.getMimeType());
    setFieldOnEntity(output, "remoteName", filename);

    // bind properties
    for (ResultProperty indexProperty : payload.getProperties()) {
      parsePayloadProperty(indexProperty, propertiesFields, aspectsFields, indexModel, output);
    }

    // set proxy binding
    if (!isVersion) {
      setIndexBindingProxy(output, new IndexBindingProxy(this));
    } else {
      setIndexBindingProxy(output, new IndexBindingProxyForVersion<T>(this, classs, versionUUID));
    }
    return output;
  }

  private <T> T create(Class<T> classs) {
    try {
      return classs.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw wrap("Error instantiating " + classs.getName(), e);
    }
  }

  private <T> boolean parsePayloadProperty(ResultProperty indexProperty,
      Set<Field> propertiesFields, Set<Field> aspectsFields, IndexModel indexModel, T output) {

    String propertyName = indexProperty.getPrefixedName();
    boolean assigned = false;

    for (Field field : propertiesFields) {
      IndexProperty fieldAnnotation = field.getAnnotation(IndexProperty.class);

      if (propertyMatches(field, fieldAnnotation, indexModel, propertyName)) {

        // found property on class

        checkRequiredConstraint(indexProperty.getValues(), fieldAnnotation, field, indexModel);
        convertAndSetField(output, field, indexProperty.getValues(), fieldAnnotation);

        assigned = true;
      }
    }

    for (Field aspectField : aspectsFields) {
      if (parsePayloadToAspectField(indexProperty, aspectField, indexModel, output, propertyName)) {
        assigned = true;
      }
    }

    if (!assigned) {
      log.debug("payloadToEntity",
          "Index property not mapped on IndexEntity: " + indexProperty.getPrefixedName() + " = "
              + (indexProperty.getValues().length > 0 ? indexProperty.getValues()[0] : "null"));
    }

    return assigned;
  }

  private <T> boolean parsePayloadToAspectField(ResultProperty indexProperty, Field aspectField,
      IndexModel indexModel, T output, String propertyName) {
    Set<Field> aspectPropertiesFields = findFields(aspectField.getType(), IndexProperty.class);
    boolean assigned = false;

    for (Field field : aspectPropertiesFields) {
      IndexProperty fieldAnnotation = field.getAnnotation(IndexProperty.class);

      if (propertyMatches(field, fieldAnnotation, indexModel, propertyName)) {
        // found property on class

        checkRequiredConstraint(indexProperty.getValues(), fieldAnnotation, field, indexModel);

        Object aspectProperty = readField(aspectField, output);
        convertAndSetField(aspectProperty, field, indexProperty.getValues(), fieldAnnotation);

        assigned = true;
      }
    }

    return assigned;
  }

  private static String mergeName(IndexAspect annotationOnField, IndexAspect annotationOnType) {
    if (annotationOnField != null && !StringUtils.isEmpty(annotationOnField.value())) {
      return annotationOnField.value();
    } else if (annotationOnType != null) {
      return annotationOnType.value();
    } else {
      return null;
    }
  }

  private static Object readField(Field field, Object object) {
    try {
      return FieldUtils.readField(field, object, true);
    } catch (IllegalAccessException e) {
      throw wrap(String.format("Error reading value of field %s on model %s", field.getName(),
          object.getClass().getName()), e);
    }
  }

  private void convertAndSetField(Object output, Field field, String[] value,
      IndexProperty fieldAnnotation) {
    try {
      FieldUtils.writeField(output, field.getName(),
          getConvertedValueToClassAndType(value, field.getType(), fieldAnnotation), true);

    } catch (Exception e) {
      throw wrap("Error setting value on field " + field.getName() + " on model "
          + output.getClass().getName(), e);
    }
  }

  private void checkRequiredConstraint(String[] value, IndexProperty fieldAnnotation, Field field,
      IndexModel indexModel) {

    if (value.length < 1 || StringUtils.isEmpty(value[0]) && fieldAnnotation.required()) {
      throw wrap("Required index property field is empty on Index entity: "
          + getPropertyName(field, fieldAnnotation, indexModel));
    }
  }

  private static String getPropertyName(Field field, IndexProperty fieldAnnotation,
      IndexModel indexModel) {

    String name = fieldAnnotation.value();
    if (name == null || name.isEmpty()) {
      name = field.getName();
    }
    return toPrefixedPropertyName(name, indexModel);
  }

  private static boolean propertyMatches(Field field, IndexProperty fieldAnnotation,
      IndexModel indexModel, String propertyName) {

    return getPropertyName(field, fieldAnnotation, indexModel).equals(propertyName);
  }

  private <T extends IndexFolder> T payloadToFolder(ResultContent payload, Class<T> classs) {
    if (payload == null) {
      return null;
    }
    if (classs == null) {
      throw invalidParameters("Class is required");
    }

    final String method = "payloadToFolder";
    log.debug(method, "parsing index payload to class " + classs.getName());

    IndexModel indexModel = getModelFromClass(classs);
    Set<Field> propertiesFields = findFields(classs, IndexProperty.class);
    Set<Field> aspectsFields = findFields(classs, IndexAspect.class);

    T output = create(classs);
    for (Field field : aspectsFields) {
      setFieldOnEntity(output, field.getName(), create(field.getType()));
    }

    log.debug(method, "model = " + resolveModel(indexModel));
    log.debug(method, "type = " + resolveType(indexModel));

    String filename = payload.getPrefixedName();
    if (filename.startsWith(METADATI_PREFIXED_NAME)) {
      filename = filename.substring(METADATI_PREFIXED_NAME.length());
    }

    setUid(output, payload.getUid());

    // output.setFoldername(filename);
    setFieldOnEntity(output, "remoteName", filename);

    // bind properties
    for (ResultProperty indexProperty : payload.getProperties()) {
      parsePayloadProperty(indexProperty, propertiesFields, aspectsFields, indexModel, output);
    }

    // set proxy binding
    setIndexBindingProxy(output, new IndexFolderBindingProxy(this));

    return output;
  }

  private static void setUid(Object entity, String uid) {
    setFieldOnEntity(entity, "uid", uid);
  }

  private static void setIndexBindingProxy(Object entity, IndexBindingProxy value) {
    setFieldOnEntity(entity, "indexBindingProxy", value);
  }

  private static void setIndexBindingProxy(Object entity, IndexFolderBindingProxy value) {
    setFieldOnEntity(entity, "indexBindingProxy", value);
  }

  private static IndexPayload entityToPayload(IndexEntity entity) {
    if (entity == null) {
      return null;
    }

    String methodName = "entityToPayload";

    log.debug(methodName, "parsing entity " + entity.getClass().getName());

    String filename = entity.getFilename();
    if (!filename.startsWith(METADATI_PREFIXED_NAME)) {
      filename = METADATI_PREFIXED_NAME + filename;
    }

    if (entity.getContent() != null && entity.getContent().length > 0
        && StringUtils.isEmpty(entity.getMimeType())) {
      String derivedMimeType = getMimetype(entity.getContent());
      entity.setMimeType(derivedMimeType);
    }

    IndexPayload metadati = new IndexPayload();

    metadati.setContent(entity.getContent());
    metadati.setContentPropertyPrefixedName(CM_CONTENT);
    metadati.setParentAssocTypePrefixedName(CM_CONTAINS);
    metadati.setEnconding(entity.getEncoding() != null ? entity.getEncoding() : "UTF-8");
    metadati.setPrefixedName(filename);
    metadati.setMimeType(entity.getMimeType());

    IndexModel indexModel = getModelFromClass(entity.getClass());
    List<Property> properties = new LinkedList<>();
    List<Aspect> aspects = new LinkedList<>();

    try {
      parseRootEntityToProperties(entity, properties, aspects);
    } catch (Exception e) {
      throw wrap("Error parsing IndexEntity document", e);
    }

    metadati.setModelPrefixedName(resolveModel(indexModel));
    metadati.setTypePrefixedName(resolveType(indexModel));
    metadati.setIndexProperties(properties);
    metadati.setIndexAspects(aspects);

    return metadati;
  }

  private static void parseRootEntityToProperties(IndexObject entity, List<Property> properties,
      List<Aspect> aspects) {
    String methodName = "parseRootEntityToProperties";

    IndexModel indexModel = getModelFromClass(entity.getClass());
    if (indexModel == null) {
      throw invalidParameters("Supplied entity is not annotated as IndexModel");
    }

    log.debug(methodName, "model = " + resolveModel(indexModel));
    log.debug(methodName, "type = " + resolveType(indexModel));

    parseEntityToProperties(indexModel, entity, properties, aspects);
  }

  private static void parseEntityToProperties(IndexModel indexModel, Object entity,
      List<Property> properties, List<Aspect> aspects) {
    String methodName = "parseEntityToProperties";

    log.debug(methodName, "parsing properties from entity "
        + (entity == null ? "null" : entity.getClass().getName()));

    if (entity == null) {
      return;
    }

    Set<Field> propertiesFields = findFields(entity.getClass(), IndexProperty.class);
    Set<Field> aspectsFields = findFields(entity.getClass(), IndexAspect.class);

    for (Field field : propertiesFields) {
      IndexProperty fieldAnnotation = field.getAnnotation(IndexProperty.class);
      if (fieldAnnotation.readOnly()) {
        continue;
      }

      parseEntityProperty(entity, field, fieldAnnotation, indexModel, properties);
    }

    for (Field aspectField : aspectsFields) {
      IndexAspect aspectFieldAnnotation = aspectField.getAnnotation(IndexAspect.class);
      if (aspectFieldAnnotation == null) {
        continue;
      }

      parseEntityAspect(entity, aspectField, aspectFieldAnnotation, indexModel, properties,
          aspects);
    }
  }

  private static void parseEntityAspect(Object entity, Field aspectField,
      IndexAspect aspectFieldAnnotation, IndexModel indexModel, List<Property> properties,
      List<Aspect> aspects) {

    String methodName = "parseEntityAspect";

    IndexAspect aspectAnnotation = aspectField.getType().getAnnotation(IndexAspect.class);

    String mergedAspectName = mergeName(aspectFieldAnnotation, aspectAnnotation);

    if (aspectFieldAnnotation != null && aspectFieldAnnotation.declared()) {
      log.debug(methodName, "explicitly adding declared aspect " + mergedAspectName);

      Aspect versionableAspect = new Aspect();
      versionableAspect.setPrefixedName(mergedAspectName);
      aspects.add(versionableAspect);
    } else {
      log.debug(methodName,
          "skipping aspect " + mergedAspectName + " because it is not marked as declared");
    }

    Object aspectObject = readField(aspectField, entity);
    if (aspectObject != null) {
      parseEntityToProperties(indexModel, aspectObject, properties, aspects);
    }
  }

  private static void parseEntityProperty(Object entity, Field field, IndexProperty fieldAnnotation,
      IndexModel indexModel, List<Property> properties) {

    Object value = readField(field, entity);

    IndexPropertyDataType type = getDataTypeFromClass(field.getType());
    String[] convertedValues = getConvertedValueFromClassAndType(type, value, fieldAnnotation);

    String name = getPropertyName(field, fieldAnnotation, indexModel);

    if (convertedValues == null || convertedValues.length < 1
        || StringUtils.isEmpty(convertedValues[0])) {
      if (fieldAnnotation.required()) {
        throw wrap("Required index property field is empty on local model: " + name);
      }
    } else {
      log.debug("parseEntityProperty", "property " + name + "(" + type
          + (fieldAnnotation.required() ? ", required" : "") + ") = " + value);

      Property property = new Property();
      property.setPrefixedName(toPrefixedPropertyName(name, indexModel));
      property.setDataType(type.getValue());
      property.setValues(convertedValues);
      property.setMultivalue(false);

      addPropertyToProperties(property, properties);
    }
  }

  private static void addPropertyToProperties(Property property, List<Property> properties) {
    if (property != null) {
      // check for duplicates/overrides
      if (properties.stream()
          .anyMatch(p -> p.getPrefixedName().equals(property.getPrefixedName()))) {
        throw wrap("MULTIPLE VALUES FOR PROPERTY " + property.getPrefixedName());
      }

      properties.add(property);
    }
  }

  private static IndexPayload entityToPayload(IndexFolder entity) {
    if (entity == null) {
      return null;
    }

    String methodName = "entityToPayload";

    log.debug(methodName, "parsing folder entity " + entity.getClass().getName());

    String filename = entity.getFoldername();
    if (!filename.startsWith(METADATI_PREFIXED_NAME)) {
      filename = METADATI_PREFIXED_NAME + filename;
    }

    IndexPayload metadati = new IndexPayload();

    metadati.setParentAssocTypePrefixedName(CM_CONTAINS);
    metadati.setModelPrefixedName("cm:contentmodel");
    metadati.setTypePrefixedName(CM_FOLDER);
    metadati.setPrefixedName(filename);

    IndexModel indexModel = getModelFromClass(entity.getClass());
    List<Property> properties = new LinkedList<>();
    List<Aspect> aspects = new LinkedList<>();

    try {
      parseRootEntityToProperties(entity, properties, aspects);
    } catch (Exception e) {
      throw wrap("Error parsing IndexFolder document", e);
    }

    metadati.setModelPrefixedName(resolveModel(indexModel));
    metadati.setTypePrefixedName(resolveType(indexModel));
    metadati.setIndexProperties(properties);
    metadati.setIndexAspects(aspects);

    return metadati;
  }

  private String getUid(String path) {
    SearchParams arg01 = new SearchParams();
    arg01.setXPathQuery(path);
    try {
      return getEcmengineManagementInterfaceService().getUid(arg01, operationContext).getUid();
    } catch (NoDataExtractedException e) {
      return null;
    } catch (Exception e) {
      throw wrap("Error getting uid for path " + path, e);
    }
  }

  private String getPath(String uid) {
    Node arg0 = new Node(uid);
    try {
      Path[] paths = getEcmengineManagementInterfaceService().getPaths(arg0, operationContext);
      if (paths.length < 1) {
        return null;
      } else {
        return paths[0].getPath();
      }
    } catch (NoDataExtractedException e) {
      return null;
    } catch (Exception e) {
      throw wrap("Error getting path for uid " + uid, e);
    }
  }

  private static String resolveModel(IndexModel model) {
    String raw = model.model();
    if (raw.contains(":")) {
      return raw;
    } else {
      if (StringUtils.isEmpty(model.prefix())) {
        throw invalidParameters("Prefix is required on index model " + model);
      } else {
        return (model.prefix() + ":" + raw).replace("::", ":");
      }
    }
  }

  private static String resolveType(IndexModel model) {
    String raw = model.type();
    if (raw.contains(":")) {
      return raw;
    } else {
      if (StringUtils.isEmpty(model.prefix())) {
        throw invalidParameters("Prefix is required on index model " + model);
      } else {
        return (model.prefix() + ":" + raw).replace("::", ":");
      }
    }
  }

  private static void setFieldOnEntity(Object entity, String fieldName, Object value) {

    try {
      FieldUtils.writeField(entity, fieldName, value, true);

    } catch (Exception e) {
      throw wrap("Error setting " + fieldName + " on model " + entity.getClass().getName(), e);
    }

  }

  private static String getMimetype(byte[] data) {
    String mimeType = null;
    try {
      InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
      mimeType = URLConnection.guessContentTypeFromStream(is);
      if (mimeType == null) {
        mimeType = MIME_TYPE_DEFAULT;
      }
    } catch (IOException e) {
      throw wrap("Error getting mime type", e);
    }
    return mimeType;
  }

  @SuppressWarnings("unchecked")
  private void loadRegisteredIndexModels(ClassLoader cl,
      Map<String, Class<? extends IndexEntity>> knownClasses,
      Map<String, Class<? extends IndexFolder>> knownFolderClasses, String completePackageName)
          throws IOException {
    String packagePath = completePackageName.replace(".", PATH_SEPARATOR);

    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] resources;

    // scansiono il percorso specificato, ricercando tutte le classi compilate che vi appartengono
    resources = resolver.getResources("classpath*:" + packagePath + "/**/*.class");
    for (Resource resource : resources) {
      String resourceURI = resource.getURI().toString();

      // costruisco il nome della classe Java a partire dal percorso relativo all'interno del
      // package specificato
      // in questo modo posso supportare anche le classi nei vari sub-package
      String subclassPath =
          resourceURI.substring(resourceURI.indexOf(packagePath) + packagePath.length() + 1);
      subclassPath = subclassPath.substring(0, subclassPath.length() - 6);
      String className = completePackageName + "." + subclassPath.replace(PATH_SEPARATOR, ".");

      try {
        Class<?> foundClass = Class.forName(className);

        if (foundClass.isAnnotationPresent(IndexModel.class)) {
          IndexModel annotation = foundClass.getAnnotation(IndexModel.class);
          if (IndexEntity.class.isAssignableFrom(foundClass)) {
            knownClasses.put(resolveType(annotation) + "@" + resolveModel(annotation),
                (Class<? extends IndexEntity>) foundClass);
          } else if (IndexFolder.class.isAssignableFrom(foundClass)) {
            knownFolderClasses.put(resolveType(annotation) + "@" + resolveModel(annotation),
                (Class<? extends IndexFolder>) foundClass);
          }

        }

      } catch (Exception e) {
        log.error("loadRegisteredIndexModels",
            "error registering IndexModel " + className + ": class is not available.", e);
      }
    }

    knownClasses.size();
  }

  private Class<? extends IndexEntity> getCorrespondingKnownClass(ResultContent payload) {
    String key = payload.getTypePrefixedName() + "@" + payload.getModelPrefixedName();
    if (knownModels.containsKey(key)) {
      return knownModels.get(key);
    } else {
      return null;
    }
  }

  private Class<? extends IndexFolder> getCorrespondingKnownFolderClass(ResultContent payload) {
    String key = payload.getTypePrefixedName() + "@" + payload.getModelPrefixedName();
    if (knownFolderModels.containsKey(key)) {
      return knownFolderModels.get(key);
    } else {
      return null;
    }
  }

  // BUILDER

  /**
   * Creates builder to build {@link Index2WrapperFacadeImpl}.
   *
   * @return created builder
   */
  public static IEndpointUrlStage builder() {
    return new Builder();
  }

  public interface IEndpointUrlStage {

    public IFruitoreStage withEndpointUrl(String endpointUrl);
  }

  public interface IFruitoreStage {

    public IUsernameStage withFruitore(String fruitore);
  }

  public interface IUsernameStage {

    public IPasswordStage withUsername(String username);
  }

  public interface IPasswordStage {

    public INomeFisicoStage withPassword(String password);
  }

  public interface INomeFisicoStage {

    public IRootNodeNameStage withNomeFisico(String nomeFisico);
  }

  public interface IRootNodeNameStage {

    public IRepositoryStage withRootNodeName(String rootNodeName);
  }

  public interface IRepositoryStage {

    public IBuildStage withRepository(String repository);
  }

  public interface IBuildStage {

    public IBuildStage withStreamingEndpointUrl(String streamingEndpointUrl);

    public Index2WrapperFacadeImpl build();
  }

  /**
   * Builder to build {@link Index2WrapperFacadeImpl}.
   */
  public static final class Builder implements IEndpointUrlStage, IFruitoreStage, IUsernameStage,
  IPasswordStage, INomeFisicoStage, IRootNodeNameStage, IRepositoryStage, IBuildStage {

    private String endpointUrl;

    private String fruitore;

    private String username;

    private String password;

    private String nomeFisico;

    private String repository;

    private String rootNode;

    private String streamingEndpointUrl;

    private Builder() {}

    @Override
    public IBuildStage withStreamingEndpointUrl(String streamingEndpointUrl) {
      this.streamingEndpointUrl = streamingEndpointUrl;
      return this;
    }

    @Override
    public IFruitoreStage withEndpointUrl(String endpointUrl) {
      this.endpointUrl = endpointUrl;
      return this;
    }

    @Override
    public IUsernameStage withFruitore(String fruitore) {
      this.fruitore = fruitore;
      return this;
    }

    @Override
    public IPasswordStage withUsername(String username) {
      this.username = username;
      return this;
    }

    @Override
    public INomeFisicoStage withPassword(String password) {
      this.password = password;
      return this;
    }

    @Override
    public IRootNodeNameStage withNomeFisico(String nomeFisico) {
      this.nomeFisico = nomeFisico;
      return this;
    }

    @Override
    public IRepositoryStage withRootNodeName(String rootNodeName) {
      rootNode = rootNodeName;
      return this;
    }

    @Override
    public IBuildStage withRepository(String repository) {
      this.repository = repository;
      return this;
    }

    @Override
    public Index2WrapperFacadeImpl build() {
      return new Index2WrapperFacadeImpl(this);
    }
  }

  @Override
  public boolean testResources() {
    try {
      return getEcmengineManagementInterfaceService().testResources();
    } catch (RemoteException e) {
      throw wrap("Error in testResources", e);
    }
  }

  private static String getErrorMessage(Throwable e) {
    if (e == null) {
      return "Errore generico";
    }

    if (e instanceof EcmEngineException) {
      return ((EcmEngineException) e).getErrorMessage();
    } else {
      return e.getMessage();
    }
  }

  private static RuntimeException invalidParameters(String message) {
    throw new java.security.InvalidParameterException(message);
  }

  private static Index2Exception wrap(String message) {
    return wrap(message, Index2Exception.class);
  }

  private static Index2Exception wrap(Exception e) {
    return wrap(e.getMessage(), e);
  }

  private static Index2Exception wrap(String message,
      Class<? extends Index2Exception> targetClass) {

    return instantiateException(targetClass, message, null);
  }

  private static Index2Exception wrap(String message, Exception e) {
    return wrap(message, e, null);
  }

  private static Index2Exception wrap(String message, Exception e,
      Class<? extends Index2Exception> targetClass) {
    if (e instanceof Index2Exception) {
      return (Index2Exception) e;
    }

    if (message == null) {
      message = "Si e' verificato un errore imprevisto";
    }

    if (targetClass == null) {
      targetClass = autoDetectExceptionClass(e);
    }

    if (e instanceof EcmEngineException) {
      String msg = ((EcmEngineException) e).getErrorMessage();
      return instantiateException(targetClass, message + " (" + msg + ")", e);

    } else {

      return instantiateException(targetClass, message, e);
    }
  }

  private static Class<? extends Index2Exception> autoDetectExceptionClass(Exception e) {
    if (e instanceof EcmEngineException) {
      if (e instanceof NoSuchNodeException) {
        return Index2NodeNotFoundException.class;
      }

      String msg = ((EcmEngineException) e).getErrorMessage();
      if (msg != null) {
        if (msg.endsWith("Backend services error: NODE_LOCKED_ERROR")) {
          return Index2NodeLockedException.class;
        } else if (msg.contains("Cannot create child node. Duplicate child:")) {
          return Index2DuplicateNodeException.class;
        }
      }
    }

    return Index2Exception.class;
  }

  private static <T extends Index2Exception> Index2Exception instantiateException(
      Class<T> targetClass, String message, Throwable t) {

    try {
      if (t != null) {
        return targetClass.getDeclaredConstructor(String.class, Throwable.class)
            .newInstance(message, t);
      } else if (message != null) {
        return targetClass.getDeclaredConstructor(String.class).newInstance(message);
      } else {
        return targetClass.getDeclaredConstructor().newInstance();
      }

    } catch (Throwable t2) { // NOSONAR
      log.error("instantiateException", "Errore nella creazione dinamica dell'eccezione:", t);

      StringBuilder msg = new StringBuilder().append("Errore imprevisto. Dettagli: ");
      if (message != null) {
        msg.append("Message=").append(message);
      }
      if (t != null) {
        msg.append("Exception=").append(t.getClass().getName());
      }
      return new Index2Exception(msg.toString());
    }
  }

  enum IndexPropertyDataType {
    TEXT("d:text"), INT("d:int"), DATE("d:date"), BOOLEAN("d:boolean");

    private String value;

    private IndexPropertyDataType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public class IndexBindingProxy {

    protected Index2WrapperFacadeImpl serviceProxy;

    public IndexBindingProxy(Index2WrapperFacadeImpl serviceProxy) {
      this.serviceProxy = serviceProxy;
    }

    public Index2ContentAttachment resolveContentAttachment(IndexEntity callingEntity) {
      return wrap(() -> doResolveContentAttachment(callingEntity),
          "LAZY LOAD ENTITY CONTENT ATTACHMENT");
    }

    private Index2ContentAttachment doResolveContentAttachment(IndexEntity callingEntity) {
      if (StringUtils.isEmpty(callingEntity.getUid())) {
        return null;
      }

      final String method = "resolveContentAttachment";
      log.debug(method,
          "IndexBindingProxy: resolving content attachment for node " + callingEntity.getUid());
      return serviceProxy.retrieveAttachmentDTO(callingEntity.getUid(), CM_CONTENT);
    }

    public byte[] resolveContent(IndexEntity callingEntity) {
      return wrap(() -> doResolveContent(callingEntity), "LAZY LOAD ENTITY CONTENT");
    }

    private byte[] doResolveContent(IndexEntity callingEntity) {
      if (StringUtils.isEmpty(callingEntity.getUid())) {
        return new byte[] {};
      }

      final String method = "resolveContent";
      log.debug(method, "IndexBindingProxy: resolving content for node " + callingEntity.getUid());
      return serviceProxy.retrieveContentData(callingEntity.getUid(), CM_CONTENT);
    }

    public String resolvePath(IndexEntity callingEntity) {
      return wrap(() -> doResolvePath(callingEntity), "LAZY RESOLVE ENTITY PATH");
    }

    private String doResolvePath(IndexEntity callingEntity) {
      final String method = "doResolvePath";
      if (StringUtils.isEmpty(callingEntity.getUid())) {
        return null;
      }
      log.debug(method, "IndexBindingProxy: resolving path for node " + callingEntity.getUid());
      return serviceProxy.getPath(callingEntity.getUid());
    }
  }

  public class IndexFolderBindingProxy {

    protected Index2WrapperFacadeImpl serviceProxy;

    public IndexFolderBindingProxy(Index2WrapperFacadeImpl serviceProxy) {
      this.serviceProxy = serviceProxy;
    }

    public String resolvePath(IndexFolder callingEntity) {
      return wrap(() -> doResolvePath(callingEntity), "LAZY RESOLVE FOLDER PATH");
    }

    private String doResolvePath(IndexFolder callingEntity) {
      final String method = "doResolvePath";
      if (StringUtils.isEmpty(callingEntity.getUid())) {
        return null;
      }
      log.debug(method,
          "IndexFolderBindingProxy: resolving path for node " + callingEntity.getUid());
      return serviceProxy.getPath(callingEntity.getUid());
    }
  }

  public class IndexBindingProxyForVersion<T extends IndexEntity> extends IndexBindingProxy {

    protected String versionUUID;
    protected Class<T> classs;

    public IndexBindingProxyForVersion(Index2WrapperFacadeImpl serviceProxy, Class<T> classs,
        String versionUUID) {
      super(serviceProxy);
      this.classs = classs;
      this.versionUUID = versionUUID;
    }

    public T resolveVersionEntity(String uuid) {
      return wrap(() -> doFindVersion(uuid, classs), "LAZY LOAD ENTITY VERSION", uuid);
    }

    @Override
    public byte[] resolveContent(IndexEntity callingEntity) {
      return wrap(() -> doResolveVersionContent(callingEntity), "LAZY LOAD ENTITY VERSION CONTENT");
    }

    private byte[] doResolveVersionContent(IndexEntity callingEntity) {
      if (StringUtils.isEmpty(callingEntity.getUid())) {
        return new byte[] {};
      }

      log.debug("resolveContent", "IndexBindingProxy: resolving content for node "
          + callingEntity.getUid() + " at version " + versionUUID);
      return serviceProxy.retrieveVersionContentData(versionUUID, CM_CONTENT);
    }
  }

}
