/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.dto.rest;

import java.io.Serializable;

public class WebsocketPayload implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type;
  private String message;
  private String title;
  private Long notificationId;
  private String url;
  private String urlDescription;
  private PayloadOptions options;

  public String getType() {
    return type;
  }


  public void setType(String type) {
    this.type = type;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  public PayloadOptions getOptions() {
    return options;
  }


  public void setOptions(PayloadOptions options) {
    this.options = options;
  }

  @Override
  public String toString() {
    return "WebsocketPayload [type=" + type + ", message=" + message + ", options=" + options + "]";
  }


  /**
   * @return the notificationId
   */
  public Long getNotificationId() {
    return notificationId;
  }


  /**
   * @param notificationId the notificationId to set
   */
  public void setNotificationId(Long notificationId) {
    this.notificationId = notificationId;
  }


  public String getUrl() {
    return url;
  }


  public void setUrl(String url) {
    this.url = url;
  }


  public String getUrlDescription() {
    return urlDescription;
  }


  public void setUrlDescription(String urlDescription) {
    this.urlDescription = urlDescription;
  }

}


class PayloadOptions implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long timeOut;

  public Long getTimeOut() {
    return timeOut;
  }

  public void setTimeOut(Long timeOut) {
    this.timeOut = timeOut;
  }

  @Override
  public String toString() {
    return "PayloadOptions [timeOut=" + timeOut + "]";
  }

}
