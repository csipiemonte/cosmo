/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface ThumbnailService {

  boolean possibileGenerazioneThumbnail(String mimeType);

  boolean generaThumbnail(InputStream input, String mimeType, OutputStream output);
}
