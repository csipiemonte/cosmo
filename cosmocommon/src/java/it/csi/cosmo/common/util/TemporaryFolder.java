/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.exception.FilesystemException;

/**
 *
 */

public class TemporaryFolder implements AutoCloseable {

  private Logger logger;

  private Path root;

  private Path temporaryPath;

  public static TemporaryFolder create(Path root, String loggerPrefix) {
    return new TemporaryFolder(root, loggerPrefix);
  }

  private TemporaryFolder(Path root, String loggerPrefix) {
    logger = Logger.getLogger(loggerPrefix + ".util.TemporaryFolder");
    this.root = root;
    this.createOnDisk();
  }

  public Path getTemporaryPath() {
    return temporaryPath;
  }

  private void createOnDisk() {
    try {
      this.temporaryPath = FilesUtils.getOrCreate(root, UUID.randomUUID().toString());
      logger.debug("created temporary folder " + this.temporaryPath.toString());
    } catch (IOException e) {
      throw new FilesystemException(
          "Error creating temporary upload folder", e);
    }
  }

  @Override
  public void close() {
    if (this.temporaryPath == null) {
      return;
    }
    logger.debug("deleting temporary folder " + this.temporaryPath.toString());
    try {
      FilesUtils.deletePath(this.temporaryPath);
    } catch (IOException e) {
      logger.error("Error deleting temporary upload folder " + this.temporaryPath.toString()
      + ": " + e.getMessage(), e);
    }
  }

}
