/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.common.util.AsyncUtils;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.exception.NotReadyException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacade;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShare;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShareDetail;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntityVersion;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFileFormatInfo;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexVerifyReport;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerConstants;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;


@Service
public class Index2ServiceImpl implements Index2Service, Monitorable, InitializingBean {

  protected static CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "Index2ServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  private Index2WrapperFacade facade = null;

  @Override
  public void afterPropertiesSet() throws Exception {
    AsyncUtils.when("INIT_INDEX_SERVICE", LoggerConstants.ROOT_LOG_CATEGORY,
        configurazioneService::isReady, this::initFacade, 5000l, null);
  }

  private void initFacade() {

    facade = Index2WrapperFacadeImpl.builder()
        .withEndpointUrl(configurazioneService.getConfig(ParametriApplicativo.ECM_URL).asString())
        .withFruitore(configurazioneService.getConfig(ParametriApplicativo.ECM_FRUITORE).asString())
        .withUsername(configurazioneService.getConfig(ParametriApplicativo.ECM_USERNAME).asString())
        .withPassword(configurazioneService.getConfig(ParametriApplicativo.ECM_PASSWORD).asString())
        .withNomeFisico(
            configurazioneService.getConfig(ParametriApplicativo.ECM_NOME_FISICO).asString())
        .withRootNodeName(
            configurazioneService.getConfig(ParametriApplicativo.ECM_ROOT_NODE_NAME).asString())
        .withRepository(
            configurazioneService.getConfig(ParametriApplicativo.ECM_REPOSITORY).asString())
        .withStreamingEndpointUrl(
            configurazioneService.getConfig(ParametriApplicativo.ECM_STREAMING_URL).asString())
        .build();
  }

  private Index2WrapperFacade requireFacade() {
    if (facade == null) {
      throw new NotReadyException(
          "Index client still not initialized. Is configuration service ready?");
    }
    return facade;
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(String identifier,
      Class<T> entityClass) {
    return requireFacade().getPreviousVersions(identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(T entity) {
    return requireFacade().getPreviousVersions(entity);
  }

  @Override
  public <T extends IndexEntity> void cancelCheckout(T entity) {
    requireFacade().cancelCheckout(entity);
  }

  @Override
  public <T extends IndexEntity> T checkOut(String identifier, Class<T> entityClass) {
    return requireFacade().checkOut(identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> T checkOut(T entity) {
    return requireFacade().checkOut(entity);
  }

  @Override
  public <T extends IndexEntity> T checkIn(T entity) {
    return requireFacade().checkIn(entity);
  }

  @Override
  public IndexEntity find(String identifier) {
    return requireFacade().find(identifier);
  }

  @Override
  public <T extends IndexEntity> T find(String identifier, Class<T> entityClass) {
    return requireFacade().find(identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> T create(String parentIdentifier, T entity) {
    return requireFacade().create(parentIdentifier, entity);
  }

  @Override
  public <T extends IndexEntity> T create(String parentIdentifier, T entity, InputStream content) {
    return requireFacade().create(parentIdentifier, entity, content);
  }

  @Override
  public <T extends IndexEntity> T save(T entity) {
    return requireFacade().save(entity);
  }

  @Override
  public <T extends IndexEntity> void delete(T entity) {
    requireFacade().delete(entity);
  }

  @Override
  public void delete(String identifier) {
    requireFacade().delete(identifier);
  }

  @Override
  public void move(String source, String targetContainer) {
    requireFacade().move(source, targetContainer);
  }

  @Override
  public String createFolder(String path) {
    return requireFacade().createFolder(path);
  }

  @Override
  public <T extends IndexFolder> T findFolder(String uuid, Class<T> entityClass) {
    return requireFacade().findFolder(uuid, entityClass);
  }

  @Override
  public IndexFolder findFolder(String uuid) {
    return requireFacade().findFolder(uuid);
  }

  @Override
  public <T extends IndexFolder> T createFolder(String inputPathOrUUID, T entity) {
    return requireFacade().createFolder(inputPathOrUUID, entity);
  }

  @Override
  public <T extends IndexFolder> T saveFolder(T entity) {
    return requireFacade().saveFolder(entity);
  }

  @Override
  public List<String> search(String folder, String text) {
    return requireFacade().search(folder, text);
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(byte[] content) {
    return requireFacade().getFileFormatInfo(content);
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(String identifier) {
    return requireFacade().getFileFormatInfo(identifier);
  }

  @Override
  public byte[] estraiBusta(String sourceIdentifier) {
    return requireFacade().estraiBusta(sourceIdentifier);
  }

  @Override
  public byte[] estraiBusta(IndexEntity sourceEntity) {
    return requireFacade().estraiBusta(sourceEntity);
  }

  @Override
  public byte[] estraiBusta(byte[] payload) {
    return requireFacade().estraiBusta(payload);
  }

  @Override
  public IndexEntity estraiBusta(String sourceIdentifier, String targetContainerIdentifier) {
    return requireFacade().estraiBusta(sourceIdentifier, targetContainerIdentifier);
  }

  @Override
  public IndexEntity estraiBusta(IndexEntity sourceEntity, String targetContainerIdentifier) {
    return requireFacade().estraiBusta(sourceEntity, targetContainerIdentifier);
  }

  @Override
  public <T extends IndexEntity> T estraiBusta(String sourceIdentifier,
      String targetContainerIdentifier, T targetEntity) {
    return requireFacade().estraiBusta(sourceIdentifier, targetContainerIdentifier, targetEntity);
  }

  @Override
  public <T extends IndexEntity> T estraiBusta(IndexEntity sourceEntity,
      String targetContainerIdentifier, T targetEntity) {
    return requireFacade().estraiBusta(sourceEntity, targetContainerIdentifier,
        targetEntity);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier) {
    return requireFacade().verificaFirma(sourceIdentifier, null);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity) {
    return requireFacade().verificaFirma(sourceEntity, null);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters) {
    return requireFacade().verificaFirma(sourceIdentifier, parameters);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity,
      IndexSignatureVerificationParameters parameters) {
    return requireFacade().verificaFirma(sourceEntity, parameters);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity) {
    return requireFacade().share(entity);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity, IndexShareOptions options) {
    return requireFacade().share(entity, options);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier) {
    return requireFacade().share(sourceIdentifier);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier, IndexShareOptions options) {
    return requireFacade().share(sourceIdentifier, options);
  }

  @Override
  public void unshare(IndexEntity entity) {
    requireFacade().unshare(entity);
  }

  @Override
  public void unshare(String sourceIdentifier) {
    requireFacade().unshare(sourceIdentifier);
  }

  @Override
  public void unshare(IndexEntity entity, URI link) {
    requireFacade().unshare(entity, link);
  }

  @Override
  public void unshare(String sourceIdentifier, URI link) {
    requireFacade().unshare(sourceIdentifier, link);
  }

  @Override
  public void unshare(IndexEntity entity, String shareId) {
    requireFacade().unshare(entity, shareId);
  }

  @Override
  public void unshare(String sourceIdentifier, String shareId) {
    requireFacade().unshare(sourceIdentifier, shareId);
  }

  @Override
  public void unshare(IndexEntity entity, IndexShare share) {
    requireFacade().unshare(entity, share);
  }

  @Override
  public void unshare(String sourceIdentifier, IndexShare share) {
    requireFacade().unshare(sourceIdentifier, share);
  }

  public Index2WrapperFacade getFacade() {
    return requireFacade();
  }

  @Override
  public ServiceStatusDTO checkStatus() {

    return ServiceStatusDTO.of(() -> {
      this.getFacade().testResources();
      return true;
    }, ServiceStatusEnum.DOWN)
        .withName("INDEX repository documentale")
        .withDetail("endpointUrl",
            configurazioneService.getConfig(ParametriApplicativo.ECM_URL).asString())
        .withDetail("streamingEndpointUrl",
            configurazioneService.getConfig(ParametriApplicativo.ECM_STREAMING_URL).asString())
        .withDetail("fruitore",
            configurazioneService.getConfig(ParametriApplicativo.ECM_FRUITORE).asString())
        .withDetail("username",
            configurazioneService.getConfig(ParametriApplicativo.ECM_USERNAME).asString())
        .withDetail("repository",
            configurazioneService.getConfig(ParametriApplicativo.ECM_REPOSITORY).asString())
        .build();
  }

  @Override
  public List<IndexShareDetail> getShares(IndexEntity sourceEntity) {
    return requireFacade().getShares(sourceEntity);
  }

  @Override
  public List<IndexShareDetail> getShares(String sourceIdentifier) {
    return requireFacade().getShares(sourceIdentifier);
  }

  @Override
  public String copyNode(String sourceIdentifierFrom, String sourceIdentifierTo) {
    return requireFacade().copyNode(sourceIdentifierFrom, sourceIdentifierTo);
  }
}
