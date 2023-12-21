/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.async.exception.LongTaskAwaitException;
import it.csi.cosmo.common.async.internal.LongTaskFeedbackBridge;
import it.csi.cosmo.common.util.ExceptionUtils;

public class LongTask<T extends Serializable> {

  private static final long PRE_STEP_DELAY = 100;

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String uuid;

  private String name;

  private LongTaskStatus status;

  private OffsetDateTime startedAt;

  private OffsetDateTime finishedAt;

  private String errorMessage;

  private String errorDetails;

  private List<LongTask<?>> steps;

  private List<LongTaskMessage> messages;

  private T result;

  private Long version;

  private LongTask<?> parent;

  private LongTaskFeedbackBridge bridge;

  private LongTask(LongTask<T> other) {
    this.uuid = other.uuid != null ? other.uuid : null;
    this.name = other.name != null ? other.name : null;
    this.status = other.status != null ? other.status : null;
    this.startedAt =
        other.startedAt != null ? other.startedAt.withYear(other.startedAt.getYear()) : null;
    this.finishedAt =
        other.finishedAt != null ? other.finishedAt.withYear(other.finishedAt.getYear()) : null;
    this.errorMessage = other.errorMessage != null ? other.errorMessage : null;
    this.errorDetails = other.errorDetails != null ? other.errorDetails : null;
    this.result = other.result != null ? other.result : null;
    this.version = other.version != null ? other.version : null;
    this.parent = null;
    this.bridge = null;

    // lock the other object properties
    if (other.steps != null) {
      synchronized (other.steps) {
        this.steps = other.steps != null ? other.steps.stream().map(LongTask::copy)
            .collect(Collectors.toCollection(LinkedList::new)) : null;
      }
    }

    if (other.messages != null) {
      synchronized (other.messages) {
        this.messages = other.messages != null ? other.messages.stream().map(LongTaskMessage::copy)
            .collect(Collectors.toCollection(LinkedList::new)) : null;
      }
    }
  }

  public LongTask<T> copy() {
    return new LongTask<>(this);
  }

  public LongTask() {
    // empty constructor for deserialization
  }

  private LongTask(LongTaskFeedbackBridge bridge, String name) {
    this.startedAt = OffsetDateTime.now();
    this.status = LongTaskStatus.STARTED;
    this.steps = new LinkedList<>();
    this.messages = new LinkedList<>();
    this.version = 0L;
    this.name = name;
    this.bridge = bridge;

    if (this.bridge == null) {
      throw new InvalidParameterException("No bridge adapter");
    }
  }

  public static <T extends Serializable> LongTask<T> start(LongTaskFeedbackBridge bridge,
      String name) {
    LongTask<T> step = new LongTask<>(bridge, name);
    step.uuid = UUID.randomUUID().toString();
    return step;
  }

  public void rename(String name) {
    this.name = name;
    this.updated();
  }

  public <V extends Serializable> LongTask<V> step(String name) {
    LongTask<V> step = new LongTask<>(this.bridge, name);
    step.uuid = UUID.randomUUID().toString();
    step.parent = this;
    synchronized (this.steps) {
      this.steps.add(step);
    }
    this.updated();
    return step;
  }

  public <V extends Serializable> V step(String name, Function<LongTask<V>, V> executor) {
    LongTask<V> step = this.step(name);
    this.updated();
    V resultStep;
    try {
      sleep(PRE_STEP_DELAY);

      resultStep = bridge.applyStep(executor, step);
      this.updated();
      step.complete(resultStep);
      this.updated();
    } catch (Exception e) {
      step.fail(e);
      this.updated();
      throw ExceptionUtils.toChecked(e);
    }
    return resultStep;
  }

  public <V extends Serializable> V step(String name, Consumer<LongTask<V>> executor) {
    LongTask<V> step = this.step(name);
    this.updated();

    try {
      sleep(PRE_STEP_DELAY);

      bridge.applyStep(e -> {
        executor.accept(e);
        return null;
      }, step);

      this.updated();
      step.complete();
      this.updated();
    } catch (Exception e) {
      step.fail(e);
      this.updated();
      throw ExceptionUtils.toChecked(e);
    }
    return null;
  }

  public <V extends Serializable> V step(String name, Supplier<V> executor) {
    LongTask<V> step = this.step(name);
    this.updated();
    V resultStep;
    try {
      sleep(PRE_STEP_DELAY);

      resultStep = bridge.applyStep(e -> {
        return executor.get();
      }, step);

      this.updated();
      step.complete(resultStep);
      this.updated();
    } catch (Exception e) {
      step.fail(e);
      this.updated();
      throw ExceptionUtils.toChecked(e);
    }
    return resultStep;
  }

  public void step(String name, Runnable executor) {
    LongTask<T> step = this.step(name);
    this.updated();
    try {
      sleep(PRE_STEP_DELAY);

      bridge.applyStep(e -> {
        executor.run();
        return null;
      }, step);

      this.updated();
      step.complete();
      this.updated();
    } catch (Exception e) {
      step.fail(e);
      this.updated();
      throw ExceptionUtils.toChecked(e);
    }
  }

  public T complete() {
    return this.complete(null);
  }

  public T complete(T result) {

    this.updated();
    for (LongTask<?> children : this.steps) {
      if (children.status == LongTaskStatus.STARTED) {
        children.complete();
      }
    }
    this.finishedAt = OffsetDateTime.now();
    this.result = result;
    this.status = LongTaskStatus.COMPLETED;
    this.updated(true);
    return result;
  }

  public void fail(String message) {
    this.fail(message, null);
  }

  public void fail(Throwable t) {
    this.fail(null, t);
  }

  public void fail(String message, Throwable t) {
    this.updated();
    for (LongTask<?> children : this.steps) {
      if (children.status == LongTaskStatus.STARTED) {
        children.fail(message, t);
      }
    }

    if (StringUtils.isBlank(message)) {
      if (t != null && t instanceof LongTaskAwaitException) {
        message = ((LongTaskAwaitException) t).getTask().getErrorMessage();
      } else if (t != null) {
        message = t.getMessage();
      }

      if (StringUtils.isBlank(message)) {
        message = "Operazione fallita";
      }
    }

    this.finishedAt = OffsetDateTime.now();
    this.errorMessage = message;
    if (t != null) {
      this.errorDetails = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(t);
    }
    this.status = LongTaskStatus.FAILED;
    this.updated(true);
  }

  public void debug(String text) {
    synchronized (this.messages) {
      this.messages.add(LongTaskMessage.debug(text));
    }
    this.updated();
  }

  public void info(String text) {
    synchronized (this.messages) {
      this.messages.add(LongTaskMessage.info(text));
    }
    this.updated();
  }

  public void warn(String text) {
    synchronized (this.messages) {
      this.messages.add(LongTaskMessage.warn(text));
    }
    this.updated();
  }

  public void error(String text) {
    synchronized (this.messages) {
      this.messages.add(LongTaskMessage.error(text));
    }
    this.updated();
  }

  public void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void updated() {
    this.updated(false);
  }

  private void updated(boolean immediate) {
    this.version++;
    if (this.parent == null) {
      bridge.updated(this, immediate);
    } else {
      this.parent.updated(immediate);
    }
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Long getVersion() {
    return version;
  }

  public T getResult() {
    return result;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public List<LongTaskMessage> getMessages() {
    return messages;
  }

  public LongTask<?> getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

  public LongTaskStatus getStatus() {
    return status;
  }

  public OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public OffsetDateTime getFinishedAt() {
    return finishedAt;
  }

  public List<LongTask<?>> getSteps() {
    return steps;
  }

  public String getUuid() {
    return uuid;
  }

  public enum LongTaskStatus {
    STARTED, COMPLETED, FAILED
  }

}
