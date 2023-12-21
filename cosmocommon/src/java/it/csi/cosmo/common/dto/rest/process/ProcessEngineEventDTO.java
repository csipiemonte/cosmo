/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest.process;

import java.time.OffsetDateTime;

/**
 *
 */

/**
 *
 *
 */
public class ProcessEngineEventDTO {

  private OffsetDateTime timestamp;

  private String messageType;

  private ProcessInstanceDTO process;

  private TaskInstanceDTO task;

  public ProcessEngineEventDTO() {
    // Auto-generated constructor stub
  }

  /**
   * @return the timestamp
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return the messageType
   */
  public String getMessageType() {
    return messageType;
  }

  /**
   * @param messageType the messageType to set
   */
  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  /**
   * @return the process
   */
  public ProcessInstanceDTO getProcess() {
    return process;
  }

  /**
   * @param process the process to set
   */
  public void setProcess(ProcessInstanceDTO process) {
    this.process = process;
  }

  /**
   * @return the task
   */
  public TaskInstanceDTO getTask() {
    return task;
  }

  /**
   * @param task the task to set
   */
  public void setTask(TaskInstanceDTO task) {
    this.task = task;
  }

  @Override
  public String toString() {
    return "ProcessEngineEvent [timestamp=" + timestamp + ", messageType=" + messageType
        + ", process=" + process + ", task=" + task + "]";
  }


}
