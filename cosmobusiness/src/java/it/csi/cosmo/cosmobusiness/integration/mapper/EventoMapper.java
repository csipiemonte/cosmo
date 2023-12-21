/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

/**
 *
 */
@Component
public class EventoMapper {

  public Evento toEvento(Task t) {
    Evento e = new Evento();
    e.setDescrizione(t.getDescription());
    e.setDtCreazione(t.getCreateTime());
    e.setDtScadenza(t.getDueDate());
    // e.setEnte(t.getTenantId());
    e.setId(t.getId());
    e.setNome(t.getName());
    // e.setUtente(t.getAssignee());
    if (t.getPriority() != null)
      e.setPriorita(t.getPriority().toString());
    e.setSospeso(t.isSuspended());
    return e;
  }

}
