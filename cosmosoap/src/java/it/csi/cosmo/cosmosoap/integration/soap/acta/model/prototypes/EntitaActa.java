/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;


/**
 *
 */

public abstract class EntitaActa {

  @ActaProperty(propertyName = "objectId", updatable = false)
  protected String id;

  @ActaProperty(propertyName = "dbKey", updatable = false)
  protected String dbKey;

  @ActaProperty(propertyName = "changeToken", updatable = false)
  protected LocalDateTime changeToken;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDbKey() {
    return dbKey;
  }

  public void setDbKey(String dbKey) {
    this.dbKey = dbKey;
  }

  public LocalDateTime getChangeToken() {
    return changeToken;
  }

  public void setChangeToken(LocalDateTime changeToken) {
    this.changeToken = changeToken;
  }

}
