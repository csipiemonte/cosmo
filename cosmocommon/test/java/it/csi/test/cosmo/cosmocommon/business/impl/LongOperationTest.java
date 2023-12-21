/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Ignore;
import org.junit.Test;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.model.LongTaskAwaitCallbacks;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.async.store.LongTaskStorageAdapter;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class LongOperationTest extends ParentUnitTest {

  private static long DELAY_FACTOR = 1000L;

  private LongTaskExecutor buildExecutor() {
    //@formatter:off
    return LongTaskExecutor.builder()
        .withLogger(new CosmoLogger("cosmo.cosmounit", this.getClass().getSimpleName()))
        .withStorageAdapter(buildSerializingAdapter())
        .build();
    //@formatter:on
  }

  private LongTaskFuture<String> buildLongTask(LongTaskExecutor executor) {
    return executor.start("mega async operation", task -> {
      log("doing things at root level");
      task.sleep(5 * DELAY_FACTOR);
      log("did things at root level");
      task.step("first step", step -> {
        log("doing things at first step");
        task.sleep(15 * DELAY_FACTOR);
        log("did things at first step");
      });
      log("preparing step 2");
      task.sleep(30 * DELAY_FACTOR);
      task.step("first step", step -> {
        log("doing things at second step");
        task.sleep(15 * DELAY_FACTOR);
        log("did things at second step");
      });
      log("done with root operation");
      task.sleep(5 * DELAY_FACTOR);
      return "123";
    });
  }

  @Test
  public void testLongTaskBasic() throws InterruptedException, ExecutionException {

    var executor = buildExecutor();
    
    LongTaskFuture<String> asyncOperation = executor.start("mega async operation", task -> {
      log("doing things at root level");
      task.sleep(100);
      log("did things at root level");
      task.step("first step", step -> {
        log("doing things at first step");
        task.sleep(100);
        log("did things at first step");
      });
      log("preparing step 2");
      task.sleep(100);
      task.step("first step", step -> {
        log("doing things at second step");
        task.sleep(100);
        log("did things at second step");
      });
      log("done with root operation");
      return "123";
    });

    dump("asyncOperation waiting for task", asyncOperation.getTask().getUuid());
    var getResult = asyncOperation.getFuture().get();
    dump("asyncOperation result", getResult);
    
    var recovered = executor.get(asyncOperation.getTask().getUuid());
    var recoveredResult = ObjectUtils.fromJson(recovered.getResult(), String.class);
    dump("recovered task result", recoveredResult);
    
    assertEquals(getResult, recoveredResult);
  }

  @Ignore("manual only")
  @Test
  public void testLongTaskWatch() throws InterruptedException, ExecutionException {

    var executor = buildExecutor();

    LongTaskFuture<String> asyncOperation = buildLongTask(executor);

    dump("asyncOperation waiting for task", asyncOperation.getTask().getUuid());

    Future<LongTaskPersistableEntry> watcher =
        executor.watch(asyncOperation.getTask().getUuid(), 1800L, new LongTaskAwaitCallbacks() {

          @Override
          public void completed(LongTaskPersistableEntry task) {
            dump("COMPLETED", task);
          }

          @Override
          public void failed(LongTaskPersistableEntry task) {
            dump("FAILED", task);
          }

          @Override
          public void timedOut(LongTaskPersistableEntry task) {
            dump("TIMED OUT", task);
          }

          @Override
          public void updated(LongTaskPersistableEntry task) {
            dump("UPDATED", task);
          }

          @Override
          public void finalizing(LongTaskPersistableEntry task) {
            dump("FINALIZING", task);
          }
        });

    var watchResult = watcher.get();
    dump("watch result", watchResult);

    var getResult = asyncOperation.getFuture().get();
    dump("asyncOperation result", getResult);
  }

  private LongTaskStorageAdapter buildSerializingAdapter() {
    return new LongTaskStorageAdapter() {
      Map<String, String> map = new HashMap<>();
      
      @Override
      public void save(LongTaskPersistableEntry task) {
        // dump("SAVING TASK", task);
        String payload = ObjectUtils.toJson(task);
        map.put(task.getUuid(), payload);
      }

      @Override
      public LongTaskPersistableEntry get(String taskUUID) {

        return Optional.ofNullable(map.getOrDefault(taskUUID, null))
            .map(str -> ObjectUtils.fromJson(str, LongTaskPersistableEntry.class))
            .orElse(null);
      }

      @Override
      public void delete(String taskUUID) {
        map.remove(taskUUID);
      }
    };
  }

  private LongTaskStorageAdapter buildErroringOnWriteAdapter() {
    return new LongTaskStorageAdapter() {
      
      @Override
      public void save(LongTaskPersistableEntry task) {
        dump("NOT SAVING TASK", task);
        throw new InternalServerException("some random error");
      }

      @Override
      public LongTaskPersistableEntry get(String taskUUID) {

        return null;
      }

      @Override
      public void delete(String taskUUID) {
        // NOP
      }

    };
  }
}
