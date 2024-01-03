/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.fileshare.IFileShareManager;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerConstants;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per la gestione del fileshare
 */
@Service
public class FileShareServiceImpl implements FileShareService, Monitorable, DisposableBean {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.FILESHARE_LOG_CATEGORY, "FileShareServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  private IFileShareManager fileShareManager;

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public RetrievedContent get(String fileUUID, LocalDate referenceDate) {
    return getFileShareManager().get(fileUUID, referenceDate);
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void delete(RetrievedContent file) {
    getFileShareManager().delete(file);
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public FileUploadResult saveFromMemory(InputStream stream, String filename, String contentType,
      String principal) {

    return getFileShareManager().handleUpload(stream, filename, contentType, principal);
  }

  private synchronized IFileShareManager getFileShareManager() {
    if (this.fileShareManager == null) {
      logger.info("getFileShareManager", "lazily initializing file share manager");
      this.fileShareManager = IFileShareManager.builder()
          .withLoggingPrefix(LoggerConstants.ROOT_LOG_CATEGORY)
          .withRootPath(
              configurazioneService.requireConfig(ParametriApplicativo.FILE_SHARE_PATH).asString())
          .build();
    }

    return this.fileShareManager;
  }

  @Override
  public ServiceStatusDTO checkStatus() {
    // check: esistenza della root

    return ServiceStatusDTO
        .of(() -> Files.exists(getFileShareManager().getRoot()), ServiceStatusEnum.DOWN)
        .withName("FileShare - disco condiviso")
        .withDetail("shareLocation",
            configurazioneService.getConfig(ParametriApplicativo.FILE_SHARE_PATH).asString())
        .build();
  }

  @Override
  public void destroy() throws Exception {
    if (this.fileShareManager != null) {
      logger.info("destroy", "closing file share manager");
      this.fileShareManager.close();
    }
  }

}
