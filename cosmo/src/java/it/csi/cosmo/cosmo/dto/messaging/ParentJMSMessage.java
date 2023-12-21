/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.messaging;

import java.io.Serializable;

/**
 *
 */

public class ParentJMSMessage implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4623905630061023359L;

  private String channel;

  public ParentJMSMessage() {
    // NOP
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public String toString() {
    return "ParentJMSMessage [channel=" + channel + "]";
  }

}
