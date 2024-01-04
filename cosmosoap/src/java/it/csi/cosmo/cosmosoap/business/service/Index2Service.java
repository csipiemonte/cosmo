/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.business.service;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
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

public interface Index2Service {

  // operazioni CRUD

  IndexEntity find(String identifier);

  <T extends IndexEntity> T find(String identifier, Class<T> entityClass);

  <T extends IndexEntity> T create(String parentIdentifier, T entity);

  <T extends IndexEntity> T create(String parentIdentifier, T entity, InputStream content);

  <T extends IndexEntity> T save(T entity);

  <T extends IndexEntity> void delete(T entity);

  void delete(String identifier);

  void move(String source, String targetContainer);

  String createFolder(String path);

  IndexFolder findFolder(String uuid);

  <T extends IndexFolder> T findFolder(String uuid, Class<T> entityClass);

  <T extends IndexFolder> T createFolder(String inputPathOrUUID, T entity);

  <T extends IndexFolder> T saveFolder(T entity);

  // ricerca testuale

  List<String> search(String folder, String text);

  // versionamento e checkin/checkout

  <T extends IndexEntity> T checkOut(String identifier, Class<T> entityClass);

  <T extends IndexEntity> T checkOut(T entity);

  <T extends IndexEntity> T checkIn(T entity);

  <T extends IndexEntity> void cancelCheckout(T entity);

  <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(String identifier,
      Class<T> entityClass);

  <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(T entity);

  // informazioni formato file

  IndexFileFormatInfo getFileFormatInfo(byte[] content);

  IndexFileFormatInfo getFileFormatInfo(String identifier);

  // sbustamento documenti firmati

  byte[] estraiBusta(byte[] payload);

  byte[] estraiBusta(String sourceIdentifier);

  byte[] estraiBusta(IndexEntity sourceEntity);

  IndexEntity estraiBusta(String sourceIdentifier, String targetContainerIdentifier);

  <T extends IndexEntity> T estraiBusta(String sourceIdentifier, String targetContainerIdentifier,
      T targetEntity);

  IndexEntity estraiBusta(IndexEntity sourceEntity, String targetContainerIdentifier);

  <T extends IndexEntity> T estraiBusta(IndexEntity sourceEntity, String targetContainerIdentifier,
      T targetEntity);

  // verifica delle firme

  IndexVerifyReport verificaFirma(String sourceIdentifier);

  IndexVerifyReport verificaFirma(IndexEntity sourceEntity);

  IndexVerifyReport verificaFirma(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters);

  IndexVerifyReport verificaFirma(IndexEntity sourceEntity,
      IndexSignatureVerificationParameters parameters);

  CreatedSharedLink share(IndexEntity entity);

  CreatedSharedLink share(IndexEntity entity, IndexShareOptions options);

  CreatedSharedLink share(String sourceIdentifier);

  CreatedSharedLink share(String sourceIdentifier, IndexShareOptions options);

  void unshare(IndexEntity entity);

  void unshare(String sourceIdentifier);

  void unshare(IndexEntity entity, URI link);

  void unshare(String sourceIdentifier, URI link);

  void unshare(IndexEntity entity, String shareId);

  void unshare(String sourceIdentifier, String shareId);

  void unshare(IndexEntity entity, IndexShare share);

  void unshare(String sourceIdentifier, IndexShare share);

  List<IndexShareDetail> getShares(IndexEntity sourceEntity);

  List<IndexShareDetail> getShares(String sourceIdentifier);

  String copyNode(String sourceIdentifierFrom, String sourceIdentifierTo);
}
