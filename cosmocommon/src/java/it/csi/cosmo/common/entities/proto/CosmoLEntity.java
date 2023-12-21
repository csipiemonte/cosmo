/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities.proto;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class CosmoLEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "dt_evento", nullable = false)
  protected Timestamp dtEvento;

  public Timestamp getDtEvento() {
    return dtEvento;
  }

  public void setDtEvento(Timestamp dtEvento) {
    this.dtEvento = dtEvento;
  }

}
