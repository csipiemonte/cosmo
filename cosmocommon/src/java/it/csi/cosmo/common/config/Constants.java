/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Classe di utility contenente tutte le costanti
 */
public class Constants {

  public static final String PRODUCT = "cosmo";

  public static final String HEADERS_PREFIX = "X-Cosmo-";

  /**
   * Utility classes, which are collections of static members, are not meant to be instantiated.
   * Even abstract utility classes, which can be extended, should not have public constructors.
   */
  private Constants() {
    throw new IllegalStateException();
  }

  public static class FILESHARE {

    private FILESHARE() {
      throw new IllegalStateException();
    }

    public static final String FILESHARE_PROTOCOL = "cosmoshare";

    public static final String POINTER_FILE_PREFIX = "pointer-";

    public static final Charset FS_ENCODING = StandardCharsets.UTF_8;

    public static final String PATH_SEPARATOR = "/";

    public static final String TRANSFER_PATH = "transfer";

    public static final String CONTENT_PATH = "content";

    public static final String TEMP_PATH = "temp";

    public static final String DICT_PATH = "dict";

    public static final String SESSIONS_PATH = "sessions";

    public static final String SESSIONS_METADATA_SUBPATH = "metadata";

    public static final String SESSIONS_COMPLETED_MARKER_SUBPATH = "completed";

    public static final String SESSIONS_PARTS_SUBPATH = "parts";

    public static final String META_PATH = DICT_PATH + PATH_SEPARATOR + "meta";

    public static final String UPLOAD_HASH_PATH = DICT_PATH + PATH_SEPARATOR + "upload-hash";

    public static final String CONTENT_POINTERS_PATH =
        DICT_PATH + PATH_SEPARATOR + "content-pointers";

    public static final String WORKING_FOLDER_FORMAT = "yyyy-MM-dd";
  }

  public static class FEIGN {

    private FEIGN() {
      throw new IllegalStateException();
    }

    public static final String FEIGN_MARKER_HEADER = HEADERS_PREFIX + "Feign";

    public static final String M2M_MARKER_HEADER = HEADERS_PREFIX + "M2M";

    public static final String FEIGN_RAY_ID_HEADER = HEADERS_PREFIX + "Feign-Ray-Id";

    public static final String FEIGN_SOURCE_RAY_HEADER = HEADERS_PREFIX + "Source-Ray-Id";
  }
}
