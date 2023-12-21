/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import it.csi.cosmo.common.async.BatchProcessingQueue;
import it.csi.cosmo.common.util.AsyncUtils;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class AsyncUtilsTest extends ParentUnitTest {

  @Test
  public void testWhen() throws InterruptedException, ExecutionException {

    Future<Void> result = AsyncUtils.when("TEST_WHEN", "cosmo.cosmotest",
        () -> Math.random() < 0.1, () -> dump("CONDITION MET!"), 100l, 10 * 1000l);


    dump("WHEN result", result.get());
  }

  @org.junit.Ignore("manual because time consuming")
  @Test
  public void testBatchProcessingQueue() throws Exception {
    //@formatter:off
    BatchProcessingQueue<Long> queue = BatchProcessingQueue.<Long>builder()
        .withMaxSize(null)
        .withDispatchInterval(3000)
        .withParallel(false)
        .withFixedDelay(true)
        .withConsumer(raw -> {
          log("CONSUMER CALLED with {} STARTED", raw);
          raw.forEach(i -> {
            log("consuming " + i);
            sleep(Math.round(Math.random() * 1200));
          });
          log("CONSUMER CALLED with {} ENDED", raw);
        })
        .build();
    //@formatter:on
    
    for (Long i = 0L; i < 30; i++) {
      log("submitting " + i);
      queue.submit(i);
      sleep(Math.round(Math.random() * 1000));
    }

    log("shutting down");
    queue.shutdown(30, TimeUnit.SECONDS, true);
  }

}
