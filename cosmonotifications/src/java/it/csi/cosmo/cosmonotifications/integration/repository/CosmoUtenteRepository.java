/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoUtenteRepository extends CosmoTRepository<CosmoTUtente, Long> {

  // @Query("select u.cosmoTNotificas from CosmoTUtente as u where u.id = :id")
  // @Query("select u.cosmoTNotificas from CosmoTUtente u join u.cosmoTNotificas n where u.id = :id order by n.arrivo desc")
  @Query("select n from CosmoTUtente u join u.cosmoRNotificaUtenteEntes r join r.cosmoTNotifica n where u.id = :id order by n.arrivo desc")
  Page<CosmoTNotifica> selectNotificheByUtenteId(@Param("id") Long id, Pageable p);

  Page<CosmoTNotifica> getCosmoTNotificasById(@Param("id") Long id, Pageable p);

  CosmoTUtente findByCodiceFiscale(String cf);

  List<CosmoTUtente> findByCodiceFiscaleIn(Collection<String> cf);

  List<CosmoTUtente> findByIdIn(List<Long> id);


}
