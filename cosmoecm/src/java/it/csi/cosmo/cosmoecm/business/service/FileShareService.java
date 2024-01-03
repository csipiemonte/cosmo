/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.io.InputStream;
import java.time.LocalDate;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;

/**
 *
 */

public interface FileShareService {

  RetrievedContent get(String fileUUID, LocalDate referenceDate);

  void delete(RetrievedContent fileUUID);

  FileUploadResult saveFromMemory(InputStream stream, String filename, String contentType,
      String principal);
}
