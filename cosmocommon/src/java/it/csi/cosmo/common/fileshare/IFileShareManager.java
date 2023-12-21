/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;


/**
 *
 */

public interface IFileShareManager {

  void close() throws IOException;

  void cleanup();

  FileUploadResult handleUpload(InputStream stream, String filename, String contentType,
      String principalIdentifier);

  Path getRoot();

  RetrievedContent get(String fileUUID);

  RetrievedContent get(String fileUUID, LocalDate referenceDate);

  void delete(RetrievedContent file);

  void testResources();

  CreateUploadSessionResponse createUploadSession(CreateUploadSessionRequest request);

  FileUploadResult handPartUpload(String sessionUUID, Long partNumber, InputStream stream,
      String principalIdentifier);

  CompleteUploadSessionResponse completeUploadSession(CompleteUploadSessionRequest request);

  void cancelUploadSession(String sessionUUID);

  public static DefaultFileShareManagerImpl.Builder builder() {
    return DefaultFileShareManagerImpl.builder();
  }
}
