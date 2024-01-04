/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;

/**
 *
 */

public interface Api2IndexService {

  Entity find(String identifier, Boolean withContent);

  Entity create(String parentIdentifier, Entity entity, byte[] content);

  Entity aggiorna(Entity entity);

  void delete(String identifier);

  void move(String source, String targetContainer);

  String createFolder(String path);

  Folder findFolder(String uuid);

  FileFormatInfo getFileFormatInfo(String identifier);

  Entity estraiBusta(Entity sourceEntity, String targetContainerIdentifier);

  VerifyReport verificaFirma(String sourceIdentifier, SignatureVerificationParameters parameters);

  SharedLink share(ShareOptions options, String sourceIdentifier, Entity entity);

  ListShareDetail getShares(String sourceIdentifier);

  String copyNode(String sourceIdentifierFrom, String sourceIdentifierTo);

  Entity creaFileIndex(CreaFileRequest body);
}
