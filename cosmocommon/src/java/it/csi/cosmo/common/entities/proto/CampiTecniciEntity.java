/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities.proto;

import java.sql.Timestamp;


public interface CampiTecniciEntity {

  Timestamp getDtCancellazione();

  void setDtCancellazione(Timestamp dtCancellazione);

  Timestamp getDtInserimento();

  void setDtInserimento(Timestamp dtInserimento);

  Timestamp getDtUltimaModifica();

  void setDtUltimaModifica(Timestamp dtUltimaModifica);

  String getUtenteCancellazione();

  void setUtenteCancellazione(String utenteCancellazione);

  String getUtenteInserimento();

  void setUtenteInserimento(String utenteInserimento);

  String getUtenteUltimaModifica();

  void setUtenteUltimaModifica(String utenteUltimaModifica);

  default boolean nonCancellato() {
    return !cancellato();
  }

  default boolean cancellato() {
    return getDtCancellazione() != null;
  }
}
