/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.index2;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2Exception;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2RollbackFailedException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.internal.IndexTransactionalOperationContext;
import it.csi.cosmo.cosmosoap.integration.soap.index2.internal.IndexTransactionalOperationContext.Operation;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntityVersion;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFileFormatInfo;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexVerifyReport;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;


public abstract class Index2TransactionalWrapperFacadeImpl
implements Index2TransactionalWrapperFacade {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "Index2TransactionalWrapperFacadeImpl");

  private Index2WrapperFacade proxy;


  private Index2TransactionalWrapperFacadeImpl(Builder builder) {
    proxy = builder.proxy;
  }

  ThreadLocal<IndexTransactionalOperationContext> currentContextHolder =
      ThreadLocal.withInitial(() -> {
        log.debug("currentContextHolder", "creating new index transactional context for thread");
        return IndexTransactionalOperationContext.create();
      });

  private IndexTransactionalOperationContext getCurrentTransactionalContext() {
    return currentContextHolder.get();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * it.csi.cosmo.cosmosoap.integration.soap.index2.Index2TransactionHandler#withTransaction(java.
   * util.concurrent.Callable)
   */
  @Override
  public <T> T withTransaction(Callable<T> callable) {
    this.startTransaction();
    T result;

    try {
      result = callable.call();
      this.commit();

      return result;

    } catch (Exception e) {
      try {
        this.rollback();
      } catch (Index2RollbackFailedException rfe) {
        log.warn("withTransaction", "INDEX ROLLBACK FAILED: " + rfe.getMessage(), rfe);
      }

      throw new Index2Exception("Error while executing callable in index transactional context", e);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see
   * it.csi.cosmo.cosmosoap.integration.soap.index2.Index2TransactionHandler#withTransaction(java.
   * lang.Runnable)
   */
  @Override
  public void withTransaction(Runnable runnable) {
    this.startTransaction();

    try {
      runnable.run();
      this.commit();

    } catch (Exception e) {
      try {
        this.rollback();
      } catch (Index2RollbackFailedException rfe) {
        log.warn("withTransaction", "INDEX ROLLBACK FAILED: " + rfe.getMessage(), rfe);
      }

      throw new Index2Exception("Error while executing runnable in index transactional context", e);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see it.csi.cosmo.cosmosoap.integration.soap.index2.Index2TransactionHandler#startTransaction()
   */
  @Override
  public void startTransaction() {
    getCurrentTransactionalContext().clear();
    getCurrentTransactionalContext().setStatus(true);
  }

  /*
   * (non-Javadoc)
   *
   * @see it.csi.cosmo.cosmosoap.integration.soap.index2.Index2TransactionHandler#commit()
   */
  @Override
  public void commit() {
    getCurrentTransactionalContext().clear();
    getCurrentTransactionalContext().setStatus(false);
  }

  /*
   * (non-Javadoc)
   *
   * @see it.csi.cosmo.cosmosoap.integration.soap.index2.Index2TransactionHandler#rollback()
   */
  @Override
  public void rollback() throws Index2RollbackFailedException {

    IndexTransactionalOperationContext rollbackContext =
        IndexTransactionalOperationContext.create();

    try {
      rollback(getCurrentTransactionalContext(), rollbackContext);
    } catch (Exception e) {
      throw new Index2RollbackFailedException("Error rollbacking changes on INDEX repository", e);
    } finally {
      getCurrentTransactionalContext().clear();
      getCurrentTransactionalContext().setStatus(false);
    }
  }

  private void rollback(IndexTransactionalOperationContext context,
      IndexTransactionalOperationContext rollbackContext) {
    final String method = "rollback";
    log.debug(method, "attempting rollback for index transactional operation context with "
        + context.getOperations().size() + " operations");

    for (Operation operation : context.getOperationsInRollbackOrder()) {
      switch (operation.getType()) {

        case CREATE:
          log.debug(method, "removing created content " + operation.getUuid());
          proxy.delete(operation.getUuid());
          rollbackContext.registerDelete(operation.getUuid());
          break;

        case DELETE:
          log.debug(method, "restoring deleted content " + operation.getUuid());
          proxy.restore(operation.getUuid());
          rollbackContext.registerCreate(operation.getUuid());
          break;

        case UPDATE:
          if (operation.getEntityBefore() != null) {
            log.debug(method, "restoring edited document content " + operation.getUuid());
            proxy.save(operation.getEntityBefore());
            rollbackContext.registerUpdate(operation.getEntityAfter(), operation.getEntityBefore());
          } else {
            log.debug(method, "restoring edited folder metadata " + operation.getUuid());
            proxy.saveFolder(operation.getFolderBefore());
            rollbackContext.registerUpdate(operation.getFolderAfter(), operation.getFolderBefore());
          }
          break;
      }
    }
  }

  /**
   * Creates builder to build {@link Index2TransactionalWrapperFacadeImpl}.
   *
   * @return created builder
   */

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link Index2TransactionalWrapperFacadeImpl}.
   */

  public static final class Builder {

    private Index2WrapperFacade proxy;

    private Builder() {}

    public Builder withProxy(Index2WrapperFacade proxy) {
      this.proxy = proxy;
      return this;
    }

    // public Index2TransactionalWrapperFacadeImpl build() {
    // return new Index2TransactionalWrapperFacadeImpl(this);
    // }
  }

  @Override
  public IndexEntity find(String identifier) {
    return proxy.find(identifier);
  }

  @Override
  public <T extends IndexEntity> T find(String identifier, Class<T> entityClass) {
    return proxy.find(identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> T create(String parentIdentifier, T entity) {
    T result = proxy.create(parentIdentifier, entity);

    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerCreate(result);
    }

    return result;
  }

  @Override
  public <T extends IndexEntity> T save(T entity) {
    IndexEntity before = proxy.find(entity.getUid());

    T result = proxy.save(entity);
    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerUpdate(before, result);
    }

    return result;
  }

  @Override
  public <T extends IndexEntity> boolean delete(T entity) {
    IndexEntity before = proxy.find(entity.getUid());

    boolean result = proxy.delete(entity);

    if (result && before != null && getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerDelete(before);
    }

    return result;
  }

  @Override
  public boolean delete(String identifier) {
    IndexEntity before = proxy.find(identifier);

    boolean result = proxy.delete(identifier);

    if (result && before != null && getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerDelete(before);
    }

    return result;
  }

  @Override
  public void restore(String identifier) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void move(String source, String targetContainer) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends IndexFolder> T findFolder(String uuid, Class<T> entityClass) {
    return proxy.findFolder(uuid, entityClass);
  }

  @Override
  public String createFolder(String path) {
    String result = proxy.createFolder(path);
    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerCreate(result);
    }

    return result;
  }

  @Override
  public <T extends IndexFolder> T createFolder(String inputPathOrUUID, T entity) {
    T result = proxy.createFolder(inputPathOrUUID, entity);
    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerCreate(result);
    }
    return result;
  }

  @Override
  public <T extends IndexFolder> T saveFolder(T entity) {
    @SuppressWarnings("unchecked")
    T before = (T) findFolder(entity.getUid(), entity.getClass());

    T result = proxy.saveFolder(entity);
    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerUpdate(before, result);
    }
    return result;
  }

  @Override
  public boolean testResources() {
    return proxy.testResources();
  }

  @Override
  public List<String> search(String parent, String text) {
    return proxy.search(parent, text);
  }

  @Override
  public <T extends IndexEntity> T checkOut(String identifier, Class<T> entityClass) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends IndexEntity> T checkOut(T entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends IndexEntity> T checkIn(T entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends IndexEntity> void cancelCheckout(T entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(String identifier,
      Class<T> entityClass) {
    return proxy.getPreviousVersions(identifier, entityClass);
  }

  @Override
  public <T extends IndexEntity> List<IndexEntityVersion<T>> getPreviousVersions(T entity) {
    return proxy.getPreviousVersions(entity);
  }

  @Override
  public <T extends IndexEntity> T create(String parentIdentifier, T entity, InputStream content) {
    T result = proxy.create(parentIdentifier, entity, content);

    if (getCurrentTransactionalContext().isStatus()) {
      getCurrentTransactionalContext().registerCreate(result);
    }

    return result;
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(byte[] content) {
    return proxy.getFileFormatInfo(content);
  }

  @Override
  public IndexFileFormatInfo getFileFormatInfo(String identifier) {
    return proxy.getFileFormatInfo(identifier);
  }

  @Override
  public byte[] estraiBusta(String sourceIdentifier) {
    return proxy.estraiBusta(sourceIdentifier);
  }

  @Override
  public byte[] estraiBusta(IndexEntity sourceEntity) {
    return proxy.estraiBusta(sourceEntity);
  }

  @Override
  public IndexEntity estraiBusta(String sourceIdentifier, String targetIdentifier) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IndexEntity estraiBusta(IndexEntity sourceEntity, String targetIdentifier) {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] estraiBusta(byte[] payload) {
    return proxy.estraiBusta(payload);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier) {
    return proxy.verificaFirma(sourceIdentifier);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity) {
    return proxy.verificaFirma(sourceEntity);
  }

  @Override
  public IndexVerifyReport verificaFirma(String sourceIdentifier,
      IndexSignatureVerificationParameters parameters) {
    return proxy.verificaFirma(sourceIdentifier, parameters);
  }

  @Override
  public IndexVerifyReport verificaFirma(IndexEntity sourceEntity,
      IndexSignatureVerificationParameters parameters) {
    return proxy.verificaFirma(sourceEntity, parameters);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity) {
    return proxy.share(entity);
  }

  @Override
  public CreatedSharedLink share(IndexEntity entity, IndexShareOptions options) {
    return proxy.share(entity, options);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier) {
    return proxy.share(sourceIdentifier);
  }

  @Override
  public CreatedSharedLink share(String sourceIdentifier, IndexShareOptions options) {
    return proxy.share(sourceIdentifier, options);
  }

  @Override
  public void unshare(IndexEntity entity) {
    proxy.unshare(entity);
  }

  @Override
  public void unshare(String sourceIdentifier) {
    proxy.unshare(sourceIdentifier);
  }

  @Override
  public void unshare(IndexEntity entity, URI link) {
    proxy.unshare(entity, link);
  }

  @Override
  public void unshare(String sourceIdentifier, URI link) {
    proxy.unshare(sourceIdentifier, link);
  }

}
