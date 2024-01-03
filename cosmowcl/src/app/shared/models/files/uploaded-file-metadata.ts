/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface UploadedFileMetadata {
  filename: string;
  fileUUID: string;
  contentUUID: string;
  contentHash: string;
  contentType: string;
  uploadHash: string;
  uploadedBy: string;
  uploadedAt: Date;
  contentSize: number;
}
