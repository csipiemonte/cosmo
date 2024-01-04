/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.FileShareService;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoAllegatoIndex;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;
import it.csi.cosmo.cosmosoap.integration.mapper.IndexMapper;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.util.CommonUtils;
import it.csi.cosmo.cosmosoap.util.FilesUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class Api2IndexServiceImpl implements Api2IndexService {

  protected static CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "Api2IndexServiceImpl");

  @Autowired
  private Index2Service index2Service;

  @Autowired
  private IndexMapper indexMapper;

  @Autowired
  private FileShareService fileShareService;

  @Override
  public Entity create(String parentIdentifier, Entity entity, byte[] content) {

    CommonUtils.requireString(parentIdentifier, "parentIdentifier");
    CommonUtils.require(entity, "entity");

    return createInternal(parentIdentifier, entity, content);
  }

  private <T extends IndexEntity> T create(String parentIdentifier, T entity, byte[] content) {
    
    entity.setFilename(entity.getFilename().replace("�", "_"));

    if (content == null) {
      return index2Service.create(parentIdentifier, entity);
    } else {
      InputStream inputStream = new ByteArrayInputStream(content);
      return index2Service.create(parentIdentifier, entity, inputStream);
    }
  }

  @Override
  public Entity aggiorna(Entity entity) {
    String methodName = "save";

    CommonUtils.require(entity, "entity");
    CommonUtils.requireString(entity.getUid(), "uuid del documento");

    IndexEntity entityOnIndex = index2Service.find(entity.getUid());

    Entity output;
    if (entity.getIdDocumento() != null && entity.getIdDocumentoPadre() != null) {
      log.info(methodName, "Il documento e' un allegato");

      CosmoAllegatoIndex allegato = (CosmoAllegatoIndex) entityOnIndex;
      allegato.setContent(entity.getContent());
      allegato.setDescrizione(entity.getDescrizione());
      allegato.setEncoding(entity.getEncoding());
      allegato.setFilename(entity.getFilename());
      allegato.setIdDocumentoCosmo(entity.getIdDocumento());
      allegato.setIdDocumentoPadre(entity.getIdDocumentoPadre());
      allegato.setMimeType(entity.getMimeType());
      allegato.setTipoDocumento(entity.getTipoDocumento());

      var resp = index2Service.save(allegato);
      output = indexMapper.toEntity(resp);

    } else if (entity.getIdDocumento() != null && entity.getIdDocumentoPadre() == null) {
      log.info(methodName, "Il documento e' un documento principale");

      CosmoDocumentoIndex documento = (CosmoDocumentoIndex) entityOnIndex;
      documento.setContent(entity.getContent());
      documento.setDescrizione(entity.getDescrizione());
      documento.setEncoding(entity.getEncoding());
      documento.setFilename(entity.getFilename());
      documento.setIdDocumentoCosmo(entity.getIdDocumento());
      documento.setMimeType(entity.getMimeType());
      documento.setTipoDocumento(entity.getTipoDocumento());

      var resp = index2Service.save(documento);
      output = indexMapper.toEntity(resp);

    } else {
      log.info(methodName, "Il documento e' generico");
      entityOnIndex.setContent(entity.getContent());
      entityOnIndex.setEncoding(entity.getEncoding());
      entityOnIndex.setFilename(entity.getFilename());
      entityOnIndex.setMimeType(entity.getMimeType());

      var resp = index2Service.save(entityOnIndex);
      output = indexMapper.toEntity(resp);
    }

    return output;
  }

  @Override
  public void delete(String identifier) {
    index2Service.delete(identifier);
  }

  @Override
  public void move(String source, String targetContainer) {
    CommonUtils.requireString(source, "source");
    CommonUtils.requireString(targetContainer, "targetContainer");

    index2Service.move(source, targetContainer);
  }

  @Override
  public String createFolder(String path) {
    CommonUtils.requireString(path, "path");
    String value = URLDecoder.decode(path, StandardCharsets.UTF_8);
    return index2Service.createFolder(value);
  }

  @Override
  public Folder findFolder(String uuid) {
    CommonUtils.requireString(uuid, "uuid");
    String value = URLDecoder.decode(uuid, StandardCharsets.UTF_8);
    return indexMapper.toFolder(index2Service.findFolder(value));
  }

  @Override
  public FileFormatInfo getFileFormatInfo(String identifier) {
    CommonUtils.requireString(identifier, "identifier");

    var result = index2Service.getFileFormatInfo(identifier);
    return indexMapper.toIndexFileFormatInfo(result);
  }

  @Override
  public Entity estraiBusta(Entity sourceEntity, String targetContainerIdentifier) {
    CommonUtils.requireString(targetContainerIdentifier, "targetContainerIdentifier");
    CommonUtils.require(sourceEntity, "sourceEntity");

    var indexEntity = index2Service.find(sourceEntity.getUid());
    var result = index2Service.estraiBusta(indexEntity, targetContainerIdentifier);

    return indexMapper.toEntity(result);
  }

  @Override
  public VerifyReport verificaFirma(String sourceIdentifier,
      SignatureVerificationParameters parameters) {
    CommonUtils.requireString(sourceIdentifier, "sourceIdentifier");
    CommonUtils.require(parameters, "parameters");

    var isvp = indexMapper.toIndexSignatureVerificationParameters(parameters);

    var result = index2Service.verificaFirma(sourceIdentifier, isvp);

    return indexMapper.toVerifyReport(result);
  }

  @Override
  public SharedLink share(ShareOptions options, String sourceIdentifier, Entity entity) {

    if (options.getFromDate() != null) {
      options.setFromDate(
          options.getFromDate().atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime());
    }
    if (options.getToDate() != null) {
      options.setToDate(
          options.getToDate().atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime());
    }

    var indexOptions = indexMapper.toIndexShareOptions(options);

    CreatedSharedLink result = null;

    if (sourceIdentifier != null) {
      result = index2Service.share(sourceIdentifier, indexOptions);

    } else if (entity != null) {
      IndexEntity indexEntity = index2Service.find(entity.getUid());
      result = index2Service.share(indexEntity, indexOptions);
    }

    return indexMapper.toSharedLink(result);
  }

  @Override
  public ListShareDetail getShares(String sourceIdentifier) {
    CommonUtils.requireString(sourceIdentifier, "sourceIdentifier");

    var result = index2Service.getShares(sourceIdentifier);
    ListShareDetail output = new ListShareDetail();
    output.setListShareDetail(indexMapper.toShareDetails(result));
    return output;
  }

  @Override
  public String copyNode(String sourceIdentifierFrom, String sourceIdentifierTo) {
    CommonUtils.requireString(sourceIdentifierFrom, "sourceIdentifierFrom");
    CommonUtils.requireString(sourceIdentifierTo, "sourceIdentifierTo");

    return index2Service.copyNode(sourceIdentifierFrom, sourceIdentifierTo);
  }

  @Override
  public Entity creaFileIndex(CreaFileRequest body) {
    String methodName = "creaFileIndex";
    Entity documentoIndex = new Entity();
    byte[] content;

    CommonUtils.require(body, "body");

    if (body.getLink() != null) {

      var infoFile = FilesUtils.getResponseHeaderInfo(body.getLink());
      infoFile.setFilename(infoFile.getFilename().replace("�", "_"));
      documentoIndex = buildDocumentoIndex(body.getId(), body.getDescrizione(),
          body.getCodiceTipo(), infoFile.getFilename(), infoFile.getMimeType());

      URL link;
      try {
        link = new URL(body.getLink());
        content = IOUtils.toByteArray(link);
      } catch (IOException e) {
        log.error(methodName, "Errore nel reperimento del contenuto del documento tramite link: " + e.getMessage());
        throw new UnexpectedResponseException("Errore nel reperimento del contenuto del documento tramite link "
            + body.getId());
      }
    } else {
      RetrievedContent retrievedFile = fileShareService.get(body.getUuidContenutoTemporaneo(),
          body.getDtInserimento());
      documentoIndex = buildDocumentoIndex(body.getId(), body.getDescrizione(),
          body.getCodiceTipo(), retrievedFile.getFilename(), retrievedFile.getContentType());
      try {
        content = IOUtils.toByteArray(retrievedFile.getContentStream());
      } catch (IOException e) {
        throw new UnexpectedResponseException("Errore nel reperimento del contenuto del documento "
            + body.getId());
      }
    }
    return createInternal(body.getUuidPratica(), documentoIndex, content);
  }

  private Entity createInternal(String parentIdentifier, Entity entity, byte[] content) {
    String methodName = "createInternal";
    Entity output;

    log.info(methodName, "Inizio chiamata create Internal " + entity.getFilename() + " "
        + entity.getDescrizione() + " " + entity.getMimeType());

    if (entity.getIdDocumento() != null && entity.getIdDocumentoPadre() != null) {
      log.info(methodName, "Il documento e' un allegato");

      CosmoAllegatoIndex allegato = indexMapper.toCosmoAllegatoIndexEntity(entity);
      CosmoAllegatoIndex resp = create(parentIdentifier, allegato, content);
      output = indexMapper.toEntity(resp);

    } else if (entity.getIdDocumento() != null && entity.getIdDocumentoPadre() == null) {
      log.info(methodName, "Il documento e' un documento principale");

      CosmoDocumentoIndex documento = indexMapper.toCosmoDocumentoIndexEntity(entity);
      CosmoDocumentoIndex resp = create(parentIdentifier, documento, content);
      output = indexMapper.toEntity(resp);

    } else {
      log.info(methodName, "Il documento e' generico");

      GenericIndexContent indexEntity = indexMapper.toGenericIndexContent(entity);
      IndexEntity resp = create(parentIdentifier, indexEntity, content);
      output = indexMapper.toEntity(resp);

    }
    log.info(methodName, "Fine create internal");
    return output;
  }

  private Entity buildDocumentoIndex(Long id, String descrizione, String codiceTipo, String filename, String contentType) {
    Entity documentoIndex = new Entity();

    documentoIndex.setDescrizione(descrizione);
    documentoIndex.setFilename(filename);
    documentoIndex.setIdDocumento(id);
    documentoIndex.setMimeType(contentType);
    documentoIndex.setTipoDocumento(codiceTipo);

    return documentoIndex;
  }


  @Override
  public Entity find(String identifier, Boolean withContent) {
    CommonUtils.requireString(identifier, "identifier");

    var resp = index2Service.find(identifier);

    if (null != withContent && Boolean.FALSE.equals(withContent)) {
      if (resp instanceof CosmoAllegatoIndex) {
        return indexMapper.toEntitySenzaContenuto((CosmoAllegatoIndex) resp);

      } else if (resp instanceof CosmoDocumentoIndex) {
        return indexMapper.toEntitySenzaContenuto((CosmoDocumentoIndex) resp);

      } else {
        return indexMapper.toEntitySenzaContenuto(resp);
      }
    } else {
      if (resp instanceof CosmoAllegatoIndex) {
        return indexMapper.toEntity((CosmoAllegatoIndex) resp);

      } else if (resp instanceof CosmoDocumentoIndex) {
        return indexMapper.toEntity((CosmoDocumentoIndex) resp);

      } else {
        return indexMapper.toEntity(resp);
      }
    }
  }

}
