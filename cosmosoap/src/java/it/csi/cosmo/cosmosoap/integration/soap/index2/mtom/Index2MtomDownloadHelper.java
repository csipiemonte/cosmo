/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.mtom;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2DownloadTimeoutException;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */

public abstract class Index2MtomDownloadHelper {

  protected static final CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "Index2MtomDownloadHelper");

  private static final long DEFAULT_TIMEOUT = 300;

  private Index2MtomDownloadHelper() {
    // helper class
  }

  public static long getTimeoutSecondsBySize(Long size) {
    if (size == null || size <= 0L) {
      return DEFAULT_TIMEOUT;
    } else if (size < 1024 * 1024) {
      // meno di 1MB
      return 60L;
    } else if (size < 30 * 1024 * 1024) {
      // meno di 30 MB
      return 2 * 60L;
    } else {
      // meno di 30 MB
      return 5 * 60L;
    }
  }

  public static byte[] downloadWithTimeout(InputStream inputStream, long timeout, TimeUnit timeUnit)
      throws InterruptedException, ExecutionException {
    final String method = "downloadWithTimeout";
    try {
      log.debug(method, "downloading content via MTOM stream with {} {} timeout", timeout,
          timeUnit.name());

      ExecutorService executor = Executors.newFixedThreadPool(1);
      List<Future<Object>> results =
          executor.invokeAll(Arrays.asList(inputStream::readAllBytes), timeout, timeUnit);

      executor.shutdown();

      return (byte[]) results.get(0).get();
    } catch (CancellationException e) {
      log.error(method, "TIMEOUT downloading content via inputstream", e);
      throw new Index2DownloadTimeoutException("Content download via InputStream (MTOM) timed out",
          e);
    } catch (Exception e) {
      log.error(method, "error downloading content via inputstream with timeout", e);
      throw e;
    }
  }

}
