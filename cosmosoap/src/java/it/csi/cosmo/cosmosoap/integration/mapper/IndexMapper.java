/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.mapper;

import java.net.URI;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoAllegatoIndex;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.Lock;
import it.csi.cosmo.cosmosoap.dto.rest.ShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;
import it.csi.cosmo.cosmosoap.dto.rest.Version;
import it.csi.cosmo.cosmosoap.dto.rest.WorkingCopy;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShareDetail;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.LockableAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.VersionableAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.WorkingCopyAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFileFormatInfo;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexVerifyReport;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {DateFormatsMapper.class})
public interface IndexMapper {

  Folder toFolder(IndexFolder input);

  FileFormatInfo toIndexFileFormatInfo(IndexFileFormatInfo input);

  ShareDetail toShareDetail(IndexShareDetail input);

  List<ShareDetail> toShareDetails(List<IndexShareDetail> input);

  @Mapping(target = "descrizione", ignore = true)
  @Mapping(target = "tipoDocumento", ignore = true)
  @Mapping(target = "idDocumento", ignore = true)
  @Mapping(target = "idDocumentoPadre", ignore = true)
  @Mapping(target = "lockable", ignore = true)
  @Mapping(target = "versionable", ignore = true)
  @Mapping(target = "workingCopy", ignore = true)
  Entity toEntity(IndexEntity input);

  @Mapping(target = "idDocumento", source = "idDocumentoCosmo")
  Entity toEntity(CosmoAllegatoIndex input);

  @Mapping(target = "idDocumento", source = "idDocumentoCosmo")
  @Mapping(target = "idDocumentoPadre", ignore = true)
  Entity toEntity(CosmoDocumentoIndex input);

  @Mapping(target = "descrizione", ignore = true)
  @Mapping(target = "tipoDocumento", ignore = true)
  @Mapping(target = "idDocumento", ignore = true)
  @Mapping(target = "idDocumentoPadre", ignore = true)
  @Mapping(target = "lockable", ignore = true)
  @Mapping(target = "versionable", ignore = true)
  @Mapping(target = "workingCopy", ignore = true)
  @Mapping(target = "content", ignore = true)
  Entity toEntitySenzaContenuto(IndexEntity input);

  @Mapping(target = "idDocumento", source = "idDocumentoCosmo")
  @Mapping(target = "content", ignore = true)
  Entity toEntitySenzaContenuto(CosmoAllegatoIndex input);

  @Mapping(target = "idDocumento", source = "idDocumentoCosmo")
  @Mapping(target = "content", ignore = true)
  @Mapping(target = "idDocumentoPadre", ignore = true)
  Entity toEntitySenzaContenuto(CosmoDocumentoIndex input);

  @Mapping(target = "withFilename", source = "filename")
  @Mapping(target = "withMimeType", source = "mimeType")
  @Mapping(target = "withEncoding", source = "encoding")
  @Mapping(target = "withContent", source = "content")
  GenericIndexContent toGenericIndexContent(Entity input);

  @Mapping(target = "withFilename", source = "filename")
  @Mapping(target = "withTipoDocumento", source = "tipoDocumento")
  @Mapping(target = "withMimeType", source = "mimeType")
  @Mapping(target = "withEncoding", source = "encoding")
  @Mapping(target = "withContent", source = "content")
  @Mapping(target = "withDescrizione", source = "descrizione")
  @Mapping(target = "withNumeroDocumento", source = "idDocumento")
  @Mapping(target = "withNumeroDocumentoPadre", source = "idDocumentoPadre")
  CosmoAllegatoIndex toCosmoAllegatoIndexEntity(Entity input);

  @Mapping(target = "withFilename", source = "filename")
  @Mapping(target = "withTipoDocumento", source = "tipoDocumento")
  @Mapping(target = "withMimeType", source = "mimeType")
  @Mapping(target = "withEncoding", source = "encoding")
  @Mapping(target = "withContent", source = "content")
  @Mapping(target = "withDescrizione", source = "descrizione")
  @Mapping(target = "withNumeroDocumento", source = "idDocumento")
  CosmoDocumentoIndex toCosmoDocumentoIndexEntity(Entity input);

  Lock toLock(LockableAspect input);

  Version toVersion(VersionableAspect input);

  WorkingCopy toWorkingCopy(WorkingCopyAspect input);

  SharedLink toSharedLink(CreatedSharedLink input);

  @Mapping(target = "withFilename", source = "filename")
  @Mapping(target = "withContentDisposition", source = "contentDisposition")
  @Mapping(target = "withSource", source = "source")
  @Mapping(target = "withFromDate", source = "fromDate")
  @Mapping(target = "withToDate", source = "toDate")
  IndexShareOptions toIndexShareOptions(ShareOptions input);

  @Mapping(target = "withVerifyCertificateList", source = "verifyCertificateList")
  IndexSignatureVerificationParameters toIndexSignatureVerificationParameters(
      SignatureVerificationParameters input);

  VerifyReport toVerifyReport(IndexVerifyReport input);

  default String map(URI value) {
    return value == null ? null : value.toString();

  }

}
