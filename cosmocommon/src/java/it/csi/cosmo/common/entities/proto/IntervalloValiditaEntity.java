/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities.proto;

import java.sql.Timestamp;
import java.time.Instant;


public interface IntervalloValiditaEntity {

  Timestamp getDtFineVal();

  void setDtFineVal(Timestamp dtFineVal);

  Timestamp getDtInizioVal();

  void setDtInizioVal(Timestamp dtInizioVal);

  default boolean nonValido() {
    return nonValido(Timestamp.from(Instant.now()));
  }

  default boolean valido() {
    return valido(Timestamp.from(Instant.now()));
  }

  default boolean nonValido(Timestamp atTimestamp) {
    return !valido(atTimestamp);
  }

  default boolean valido(Timestamp atTimestamp) {
    if (atTimestamp == null) {
      throw new IllegalArgumentException();
    }
    Timestamp dtIn = getDtInizioVal();
    if (dtIn == null) {
      return false;
    }
    if (atTimestamp.before(dtIn)) {
      return false;
    }
    Timestamp dtFin = getDtFineVal();
    if (dtFin == null) {
      return true;
    }
    return dtFin.after(atTimestamp);
  }
}
