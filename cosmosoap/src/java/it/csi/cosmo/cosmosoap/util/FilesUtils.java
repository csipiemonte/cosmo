/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.dto.ResponseHeaderDTO;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */

public abstract class FilesUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "FilesUtils");

  private FilesUtils() {}

  /**
   * Metodo per ottenere gli http response header
   *
   * @param link - stringa che rappresenta il link al documento
   * @return una Map contenente tutti i campi header
   */

  public static ResponseHeaderDTO getResponseHeaderInfo(String link) {
    String method = "responseHeaderInfo";
    ResponseHeaderDTO rh = new ResponseHeaderDTO();

    try {
      URL obj = new URL(link);
      HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
      rh.setSize(conn.getContentLengthLong());
      rh.setMimeType(conn.getContentType());
      String filename = conn.getHeaderField("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
      if (filename == null) {
        filename = UUID.randomUUID().toString();
      }
      rh.setFilename(filename);
    } catch (Exception e) {
      LOGGER.error(method,
          "Errore durante la creazione delle info relative al file a partire dal response header",
          e);
    }
    return rh;
  }

}
