/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.model;

import java.time.OffsetDateTime;

/**
 *
 */

public class LongTaskMessage {

  public enum LongTaskMessageType {
    DEBUG, INFO, WARNING, ERROR
  }

  public static LongTaskMessage debug(String text) {
    LongTaskMessage output = new LongTaskMessage();
    output.type = LongTaskMessageType.DEBUG;
    output.text = text;
    return output;
  }

  public static LongTaskMessage info(String text) {
    LongTaskMessage output = new LongTaskMessage();
    output.type = LongTaskMessageType.INFO;
    output.text = text;
    return output;
  }

  public static LongTaskMessage warn(String text) {
    LongTaskMessage output = new LongTaskMessage();
    output.type = LongTaskMessageType.WARNING;
    output.text = text;
    return output;
  }

  public static LongTaskMessage error(String text) {
    LongTaskMessage output = new LongTaskMessage();
    output.type = LongTaskMessageType.ERROR;
    output.text = text;
    return output;
  }
  
  private OffsetDateTime timestamp = OffsetDateTime.now();
  private String text;
  private LongTaskMessageType type;

  public String getText() {
    return text;
  }

  public LongTaskMessageType getType() {
    return type;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public LongTaskMessage(LongTaskMessage other) {
    this.text = other.text != null ? other.text : null;
    this.type = other.type != null ? other.type : null;
    this.timestamp = other.timestamp != null ? other.timestamp : null;
  }

  public LongTaskMessage copy() {
    return new LongTaskMessage(this);
  }

  public LongTaskMessage() {
    // NOP
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @param type the type to set
   */
  public void setType(LongTaskMessageType type) {
    this.type = type;
  }
}
