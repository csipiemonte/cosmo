/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmopratiche.business.rest.PraticheApi;
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticaService;
import it.csi.cosmo.cosmopratiche.business.service.CondivisioniPraticheService;
import it.csi.cosmo.cosmopratiche.business.service.FormsService;
import it.csi.cosmo.cosmopratiche.business.service.PracticeService;
import it.csi.cosmo.cosmopratiche.business.service.PraticaAttivitaService;
import it.csi.cosmo.cosmopratiche.business.service.RelazionePraticaPraticaService;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;

public class PraticheApiServiceImpl extends ParentApiImpl implements PraticheApi {

  @Autowired
  private PracticeService praticheService;

  @Autowired
  private PraticaAttivitaService praticaAttivitaService;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CondivisioniPraticheService condivisioniPraticheService;

  @Autowired
  private FormsService formsService;

  @Autowired
  private RelazionePraticaPraticaService relazionePraticaPraticaService;

  @Autowired
  private CaricamentoPraticaService caricamentoPraticaService;

  @Override
  public Response deletePraticheIdPratica(String idPratica, SecurityContext securityContext) {
    praticheService.deletePracticesId(idPratica);
    return Response.noContent().build();
  }

  @Override
  public Response getPraticheIdPratica(String idPratica, Boolean annullata,
      SecurityContext securityContext) {
    Pratica d = praticheService.getPracticesId(idPratica, annullata);
    return Response.ok(d).build();
  }

  @Override
  public Response postPratiche(CreaPraticaRequest body, SecurityContext securityContext) {
    Pratica d = praticheService.postPractices(body);
    return Response.status(201).entity(d).build();
  }


  @Override
  public Response putPraticheIdPratica(String idPratica, Pratica body, Boolean aggiornaAttivita,
      SecurityContext securityContext) {
    Pratica d = praticheService.putPracticesId(idPratica, body, aggiornaAttivita);
    return Response.ok(d).build();
  }

  @Override
  public Response getPraticheStatoIdPraticaExt(String idPraticaExt,
      SecurityContext securityContext) {
    RiassuntoStatoPratica d = praticheService.getPraticheStatoIdPraticaExt(idPraticaExt);
    return Response.ok(d).build();
  }

  @Override
  public Response getPraticheTaskIdTaskSubtasks(String idTask, SecurityContext securityContext) {
    return Response.ok(praticheService.getPraticheTaskIdTaskSubtasks(idTask)).build();
  }

  @Override
  public Response getPratiche(String filter, Boolean export, SecurityContext securityContext) {
    return Response.ok(praticheService.getPratiche(filter, export)).build();
  }

  @Override
  public Response getPraticheStato(String tipoPratica, SecurityContext securityContext) {
    return Response.ok(praticheService.getStatiPerTipo(tipoPratica)).build();
  }

  @Override
  public Response getVisibilitaPraticaById(Long idPratica, SecurityContext securityContext) {
    Pratica pratica = praticheService.getVisibilitaPraticaById(idPratica);
    return Response.ok(pratica).build();
  }


  @Override
  public Response getVisibilitaPraticaByTask(String idTask, SecurityContext securityContext) {
    Pratica pratica = praticheService.getVisibilitaPraticaByIdTask(idTask);
    return Response.ok(pratica).build();
  }


  @Override
  public Response getPraticheIdPraticaStorico(Integer idPratica, SecurityContext securityContext) {
    StoricoPratica storico = storicoPraticaService.getStoricoAttivita(idPratica.longValue());
    return Response.ok(storico).build();
  }

  @Override
  public Response getAttivitaIdPratica(String idPratica, SecurityContext securityContext) {
    List<Attivita> attivita = praticaAttivitaService.getAttivitaIdPratica(idPratica);
    return Response.ok(attivita).build();
  }

  @Secured(authenticated = true)
  @Override
  public Response deletePraticheIdPraticaCondivisioneIdCondivisione(Long idPratica,
      Long idCondivisione, SecurityContext securityContext) {

    condivisioniPraticheService.rimuoviCondivisione(idPratica, idCondivisione);
    return Response.noContent().build();
  }

  @Secured(authenticated = true)
  @Override
  public Response postPraticheIdPraticaCondivisioni(Long idPratica,
      CreaCondivisionePraticaRequest body, SecurityContext securityContext) {

    CondivisionePratica response = condivisioniPraticheService.creaCondivisione(idPratica, body);
    return Response.status(201).entity(response).build();
  }

  @Override
  public Response getPraticheTaskIdTaskForm(String idTask, SecurityContext securityContext) {
    SimpleForm response = formsService.getPraticheTaskIdTaskForm(idTask);
    return Response.ok(response).build();
  }

  @Override
  public Response getPraticheIdPraticaDiagramma(Integer idPratica,
      SecurityContext securityContext) {

    byte[] responseBody = praticheService.getPraticheIdPraticaDiagramma(idPratica);

    ResponseBuilder response = Response.status(HttpStatus.OK.value()).entity(responseBody);

    response.header("Content-Disposition",
        "inline; filename=diagramma-pratica-" + idPratica + ".png");

    response.header("Content-Type", "image/png");

    return response.build();
  }

  @Override
  public Response getPraticheTipiRelazionePraticaPratica(SecurityContext securityContext) {
    List<TipoRelazionePraticaPratica> response =
        relazionePraticaPraticaService.getTipiRelazionePraticaPratica();
    return Response.ok(response).build();
  }

  @Override
  public Response getPraticheIdPraticaRelazioni(Integer idPratica,
      SecurityContext securityContext) {
    List<PraticaInRelazione> response =
        relazionePraticaPraticaService.getPraticheInRelazione(idPratica.longValue());
    return Response.ok(response).build();
  }

  @Override
  public Response putPraticheInRelazione(String idPratica, String tipoRelazione,
      List<BigDecimal> body, SecurityContext securityContext) {
    Pratica response =
        relazionePraticaPraticaService.creaAggiornaRelazioni(idPratica, tipoRelazione, body);
    return Response.ok(response).build();
  }

  @Override
  public Response postPraticheIdPraticaStorico(Integer idPratica, StoricoPraticaRequest body,
      SecurityContext securityContext) {
    storicoPraticaService.logEvent(body);
    return Response.ok().build();
  }



  @Override
  public Response getPraticheFileStatiCaricamento(SecurityContext securityContext) {
    var response = caricamentoPraticaService.getStatiCaricamento();
    return Response.ok(response).build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response postPraticheFile(CaricamentoPraticaRequest body,
      SecurityContext securityContext) {
    var response = caricamentoPraticaService.creaCaricamentoPratica(body);
    return Response.ok(response).build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response putPraticheFileId(String id, CaricamentoPraticaRequest body,
      SecurityContext securityContext) {
    var response = caricamentoPraticaService.aggiornaCaricamentoPratica(Long.valueOf(id), body);
    return Response.ok(response).build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response getPraticheFile(String filter, SecurityContext securityContext) {
    return Response.ok(caricamentoPraticaService.getCaricamentoPratiche(filter)).build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response getPraticheFileCaricamentoInBozza(String filter,
      SecurityContext securityContext) {
    return Response.ok(caricamentoPraticaService.getCaricamentoPraticheCaricamentoInBozza(filter))
        .build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response deletePraticheFileId(String id, SecurityContext securityContext) {
    caricamentoPraticaService.deleteCaricamentoPratiche(Long.valueOf(id));
    return Response.noContent().build();
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response getPraticheFileId(String id, String filter, Boolean export,
      SecurityContext securityContext) {
    return Response.ok(caricamentoPraticaService.getCaricamentoPraticheId(id, filter, export))
        .build();
  }

  @Override
  public Response getPraticheFilePath(String dataInserimento,
      SecurityContext securityContext) {
    return Response.ok(caricamentoPraticaService.getPathElaborazioni(dataInserimento)).build();
  }

  @Override
  public Response putPraticheFilePathCancellato(String path,
      SecurityContext securityContext) {
    caricamentoPraticaService.deletePathFile(path);
    return Response.noContent().build();
  }

  @Override
  public Response getPraticheNoLink(SecurityContext securityContext) {
    return Response.ok(praticheService.getPraticheNoLink()).build();
  }
}
