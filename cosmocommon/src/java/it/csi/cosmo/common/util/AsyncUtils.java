/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;

/**
 *
 */

public abstract class AsyncUtils { // NOSONAR

  private AsyncUtils() {
    // private
  }

  private static class AsyncTaskContext {
    long startedAt;
    String taskName;
    Logger logger;
    BooleanSupplier condition;
    Runnable action;
    long interval;
    Long timeout;
    ScheduledExecutorService executor;
    CompletableFuture<Void> future;
  }

  public static Future<Void> when(String taskName, String loggingPrefix, BooleanSupplier condition,
      Runnable action, long interval, Long timeout) {

    AsyncTaskContext context = new AsyncTaskContext();
    context.startedAt = System.currentTimeMillis();
    context.condition = condition;
    context.action = action;
    context.interval = interval;
    context.timeout = timeout;
    context.taskName = taskName;
    context.logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".util." + AsyncUtils.class.getSimpleName());
    context.future = new CompletableFuture<>();
    context.executor = null;

    attempt(context);

    return context.future;
  }

  private static boolean attempt(AsyncTaskContext task) {

    task.logger.debug(String.format("attempting execution for task %s", task.taskName));

    boolean conditionResult = checkCondition(task);
    if (!conditionResult) {
      task.logger
      .debug(String.format("condition for task %s not met, skipping execution", task.taskName));
      reschedule(task);
      return false;
    } else {
      task.logger
      .debug(String.format("condition for task %s was met, executing now", task.taskName));
      execute(task);
      return true;
    }
  }

  private static void reschedule(AsyncTaskContext task) {

    if (task.timeout != null && (System.currentTimeMillis() - task.startedAt) >= task.timeout) {
      String message = String.format("task %s timed out before condition was met", task.taskName);
      task.logger.warn(message);
      task.future.completeExceptionally(new TimeoutException(message));
      if (task.executor != null) {
        task.executor.shutdown();
        task.executor = null;
      }
      return;
    }

    task.logger.debug(String.format("rescheduling attempt for task %s", task.taskName));

    if (task.executor == null) {
      task.executor = Executors.newSingleThreadScheduledExecutor();
    }

    task.executor.schedule(() -> attempt(task), task.interval, TimeUnit.MILLISECONDS);
  }

  private static void execute(AsyncTaskContext task) {

    task.logger.info(String.format("executing action for task %s", task.taskName));
    try {
      task.action.run();
      task.future.complete(null);
    } catch (Throwable t) { // NOSONAR
      task.logger.error(String.format("error executing action for task %s", task.taskName), t);
      task.future.completeExceptionally(t);
    } finally {
      task.logger.info(String.format("finished action for task %s", task.taskName));
      if (task.executor != null) {
        task.executor.shutdown();
        task.executor = null;
      }
    }
  }

  private static boolean checkCondition(AsyncTaskContext task) {

    task.logger.debug(String.format("checking condition for task %s", task.taskName));
    try {
      boolean result = task.condition.getAsBoolean();
      task.logger
      .debug(String.format("condition check for task %s returned %s", task.taskName, result));
      return result;
    } catch (Throwable t) { // NOSONAR
      task.logger.warn(String.format("error checking condition for task %s", task.taskName), t);
      return false;
    }
  }


}
