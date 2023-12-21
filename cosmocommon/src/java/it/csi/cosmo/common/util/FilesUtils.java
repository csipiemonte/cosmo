/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 *
 */

public abstract class FilesUtils {

  private FilesUtils() {
    // empty
  }

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static Path getOrCreate(Path root, String subpath) throws IOException {
    Path p = root.resolve(subpath);

    if (!Files.exists(p)) {
      Files.createDirectories(p);
    }

    return p;
  }

  public static void deletePath(Path pathToBeDeleted) throws IOException {
    try (Stream<Path> stream = Files.walk(pathToBeDeleted)) {
      stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
  }

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

}
