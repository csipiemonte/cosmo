/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.rest.PraticheAttivitaApi;
import it.csi.cosmo.cosmopratiche.business.service.PraticaAttivitaService;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

public class PraticheAttivitaApiServiceImpl extends ParentApiImpl implements PraticheAttivitaApi {

  @Autowired
  private PraticaAttivitaService praticaAttivitaService;

  @Override
  public Response postPraticheAttivita(Pratica body, SecurityContext securityContext) {
    Pratica praticaSalvata = praticaAttivitaService.salva(body);
    if (praticaSalvata != null) {
      return Response.ok(praticaSalvata).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putPraticheAttivita(Pratica body, SecurityContext securityContext) {
    Pratica praticaAggiornata = praticaAttivitaService.aggiorna(body);
    if (praticaAggiornata != null) {
      return Response.ok(praticaAggiornata).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putPraticheAttivitaIdAttivita(String idAttivita,
      SecurityContext securityContext) {
    Boolean d = praticaAttivitaService.putAttivitaIdAttivita(idAttivita);
    if (d != null) {
      return Response.ok(d).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getPraticheAttivitaIdAttivita(String idAttivita,
      SecurityContext securityContext) {
    Attivita d = praticaAttivitaService.getPraticheAttivitaIdAttivita(idAttivita);
    if (d != null) {
      return Response.ok(d).build();
    } else {
      throw new NotFoundException("Attivita con ID " + idAttivita + " non trovata");
    }
  }

}
