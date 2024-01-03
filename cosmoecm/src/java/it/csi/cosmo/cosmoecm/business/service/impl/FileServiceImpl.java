/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

  private static final String CLASS_NAME = FileServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private DocumentoService documentoService;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Override
  public FileContent downloadDocumento(String id) {
    String methodName = "downloadDocumento";

    if (StringUtils.isBlank(id)) {
      String message =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "id del documento");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    CosmoTDocumento documento = cosmoTDocumentoRepository.findOne(Long.valueOf(id));

    if (documento == null) {
      String message = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(id));
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    var contenuto = documento.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenuto != null) {
      return fromContenuto(contenuto);
    }

    contenuto = documento.findContenuto(TipoContenutoDocumento.TEMPORANEO);
    if (contenuto != null) {
      return fromContenuto(contenuto);
    }

    logger.error(methodName, ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
    throw new NotFoundException(ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
  }

  @Override
  public void deleteFileOnFileSystem(String uuid) {
    String methodName = "deleteFileOnFileSystem";

    if (StringUtils.isBlank(uuid)) {
      var msg = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "uuid del file");
      logger.error(methodName, msg);
      throw new BadRequestException(msg);
    }

    RetrievedContent fileOnFileSystem = documentoService.get(uuid);

    if (fileOnFileSystem != null) {
      documentoService.delete(fileOnFileSystem);
      logger.info(methodName, String.format(ErrorMessages.FS_FILE_ELIMINATO_CORRETTAMENTE, uuid));
    }
  }

  @Override
  public FileContent previewDocumento(String id) {
    String methodName = "previewDocumento";

    if (StringUtils.isBlank(id)) {
      String message =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "id del documento");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    CosmoTDocumento documento = cosmoTDocumentoRepository.findOne(Long.valueOf(id));

    if (documento == null) {
      String message = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(id));
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    var contenuto = documento.findContenuto(TipoContenutoDocumento.SBUSTATO);
    if (contenuto != null) {
      return fromContenuto(contenuto);
    }

    contenuto = documento.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenuto != null) {
      return fromContenuto(contenuto);
    }

    contenuto = documento.findContenuto(TipoContenutoDocumento.TEMPORANEO);
    if (contenuto != null) {
      return fromContenuto(contenuto);
    }

    throw new NotFoundException(ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
  }

  private FileContent fromContenuto(CosmoTContenutoDocumento contenuto) {
    final var methodName = "fromContenuto";
    FileContent output = new FileContent();

    if (!TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {

      var indexEntity = indexFeignClient.getFile(contenuto.getUuidNodo(), true);

      if (indexEntity == null || indexEntity.getContent() == null) {
        logger.error(methodName, ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
        throw new NotFoundException(ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
      }

      output.setContent(indexEntity.getContent());

      if (contenuto.getFormatoFile() == null
          || StringUtils.isBlank(contenuto.getFormatoFile().getMimeType())) {
        output.setMimeType(indexEntity.getMimeType());
      } else {
        output.setMimeType(contenuto.getFormatoFile().getMimeType());
      }

      if (StringUtils.isBlank(contenuto.getNomeFile())) {
        output.setFileName(indexEntity.getFilename());
      } else {
        output.setFileName(contenuto.getNomeFile());
      }

    } else {
      return fromContenutoTemporaneo(contenuto);
    }

    return output;
  }


  private FileContent fromContenutoTemporaneo(CosmoTContenutoDocumento contenuto) {
    final var methodName = "fromContenutoTemporaneo";
    FileContent output = new FileContent();

    try {
      RetrievedContent file = documentoService.get(contenuto.getUuidNodo());
      output.setContent(file.getContentStream().readAllBytes());
      output.setMimeType(file.getContentType());

      if (StringUtils.isBlank(contenuto.getNomeFile())) {
        output.setFileName(file.getFilename());
      } else {
        output.setFileName(contenuto.getNomeFile());
      }
    } catch (IOException e) {
      logger.error(methodName,
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, contenuto.getUuidNodo()));
      throw new NotFoundException(
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, contenuto.getUuidNodo()));
    }

    return output;
  }


}
