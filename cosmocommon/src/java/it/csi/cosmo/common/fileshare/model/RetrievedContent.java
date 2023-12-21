/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;

/**
 *
 */

public interface RetrievedContent {

  Path getWorkingFolder();

  InputStream getContentStream();

  String getUploadUUID();

  String getFilename();

  String getContentType();

  Long getContentSize();

  ZonedDateTime getUploadedAt();
}
