/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async.model;

import java.time.OffsetDateTime;
import java.util.List;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;

public class LongTaskPersistableEntry {

  private String uuid;

  private String name;

  private LongTaskStatus status;

  private OffsetDateTime startedAt;

  private OffsetDateTime finishedAt;

  private String errorMessage;

  private String errorDetails;

  private List<LongTaskPersistableEntry> steps;

  private List<LongTaskMessage> messages;

  private String result;

  private Long version;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LongTaskStatus getStatus() {
    return status;
  }

  public void setStatus(LongTaskStatus status) {
    this.status = status;
  }

  public OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public OffsetDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public void setErrorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
  }

  public List<LongTaskPersistableEntry> getSteps() {
    return steps;
  }

  public void setSteps(List<LongTaskPersistableEntry> steps) {
    this.steps = steps;
  }

  public List<LongTaskMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<LongTaskMessage> messages) {
    this.messages = messages;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return "LongTaskPersistableEntry [uuid=" + uuid + ", name=" + name + ", status=" + status
        + ", startedAt=" + startedAt + ", finishedAt=" + finishedAt + ", errorMessage="
        + errorMessage + ", errorDetails=" + errorDetails + ", steps=" + steps + ", messages="
        + messages + ", result=" + result + ", version=" + version + "]";
  }


}
