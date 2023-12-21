/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 */

/**
 *
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ContextAwarePoolExecutor() {
    super();
    this.setQueueCapacity(0);
    this.initialize();
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return super.submit(new ContextAwareCallable(task, getCurrentRequestAttributes()));
  }

  @Override
  public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
    return super.submitListenable(new ContextAwareCallable(task, getCurrentRequestAttributes()));
  }

  @Override
  public Future<?> submit(Runnable task) {
    return super.submit(new ContextAwareCallable(() -> {
      task.run();
      return null;
    }, getCurrentRequestAttributes()));
  }

  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }
}
