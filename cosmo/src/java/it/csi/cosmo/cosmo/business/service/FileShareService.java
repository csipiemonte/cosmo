/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service;

import java.io.InputStream;
import java.util.List;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.dto.EsitoEnum;
import it.csi.cosmo.cosmo.dto.rest.FileDocumentiZipUnzipRequest;
import it.csi.cosmo.cosmo.dto.rest.InfoFile;

public interface FileShareService {

  FileUploadResult handleUpload(InputStream stream, String filename, String mimeType,
      UserInfoDTO currentUser);

  void cleanup();

  CreateUploadSessionResponse createUploadSession(CreateUploadSessionRequest request);

  void handPartUpload(String sessionUUID, Long partNumber, InputStream stream,
      UserInfoDTO currentUser);

  CompleteUploadSessionResponse completeUploadSession(CompleteUploadSessionRequest request);

  void cancelUploadSession(String sessionUUID);

  RetrievedContent get(String uuid);

  void verifyFileDocumentiZipUploadSession(
      it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionRequest body);

  EsitoEnum manageZipFile(String sessionUUID, String folderName);

  boolean validateExcel(InputStream input);

  String handleUploadExcel(InputStream input, String fileName);

  FileUploadResult unzipFile(FileDocumentiZipUnzipRequest body);

  List<InfoFile> getFilePratiche(String path);

  void deletePratichePath(String path);


}
