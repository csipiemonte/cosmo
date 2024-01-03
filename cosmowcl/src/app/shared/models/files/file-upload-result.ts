/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { UploadedFileMetadata } from './uploaded-file-metadata';
/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface FileUploadResult{
  metadata: UploadedFileMetadata;
  location: string;
}
