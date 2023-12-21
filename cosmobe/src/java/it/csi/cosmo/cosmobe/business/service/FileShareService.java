/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.service;

import java.io.InputStream;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.security.model.ClientInfoDTO;

public interface FileShareService {

  FileUploadResult handleUpload(InputStream stream, String filename, String mimeType,
      ClientInfoDTO currentClient);

  void cleanup();

  CreateUploadSessionResponse createUploadSession(CreateUploadSessionRequest request);

  void handPartUpload(String sessionUUID, Long partNumber, InputStream stream,
      ClientInfoDTO currentClient);

  CompleteUploadSessionResponse completeUploadSession(CompleteUploadSessionRequest request);

  void cancelUploadSession(String sessionUUID);

  RetrievedContent get(String uuid);

  void handleUploadExcel(InputStream inputStream, String fileName);

  boolean validateExcel(InputStream body);

}
