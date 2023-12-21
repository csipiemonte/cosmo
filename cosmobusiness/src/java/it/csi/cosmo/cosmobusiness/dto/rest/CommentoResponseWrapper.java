/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.rest;

import java.io.Serializable;

/**
 *
 */

public class CommentoResponseWrapper implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String message;
  private String author;
  private String id;
  private String time;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "CommentoResponseWrapper [message=" + message + ", author=" + author + ", id=" + id
        + ", time=" + time + "]";
  }

}
