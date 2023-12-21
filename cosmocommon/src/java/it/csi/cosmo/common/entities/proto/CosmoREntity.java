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
public abstract class CosmoREntity
implements IntervalloValiditaEntity, Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "dt_fine_val")
  protected Timestamp dtFineVal;

  @Column(name = "dt_inizio_val", nullable = false)
  protected Timestamp dtInizioVal;

  @Override
  public Timestamp getDtFineVal() {
    return dtFineVal;
  }

  @Override
  public void setDtFineVal(Timestamp dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  @Override
  public Timestamp getDtInizioVal() {
    return dtInizioVal;
  }

  @Override
  public void setDtInizioVal(Timestamp dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

}
