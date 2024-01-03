/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEntePK;
import it.csi.cosmo.common.repository.CosmoRRepository;

public interface CosmoRNotificaUtenteEnteRepository
    extends CosmoRRepository<CosmoRNotificaUtenteEnte, CosmoRNotificaUtenteEntePK> {

  List<CosmoRNotificaUtenteEnte> findByCosmoTNotificaIdIn(List<Long> idNotifiche);

  List<CosmoRNotificaUtenteEnte> findByInvioMailAndStatoInvioMailIn(Boolean invioMail, List<String> statoInvioMail);

  @Modifying
  @Query(
      value = "update CosmoRNotificaUtenteEnte u SET dataLettura = NOW() where u.id.idUtente = :idUtente AND u.id.idEnte = :idEnte")
  int markAllNotificationsRead(@Param("idUtente") Long idUtente, @Param("idEnte") Long idEnte);
}
