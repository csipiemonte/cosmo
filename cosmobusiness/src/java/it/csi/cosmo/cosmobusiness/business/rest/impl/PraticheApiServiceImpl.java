/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmobusiness.business.rest.PraticheApi;
import it.csi.cosmo.cosmobusiness.business.service.AttivazioneSistemaEsternoService;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.cosmo.cosmobusiness.business.service.InformazioniPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.LavorazionePraticaService;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.dto.exception.FlowableVariableHandlingException;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FormTask;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPayloadAttivazioneSistemaEsternoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

public class PraticheApiServiceImpl extends ParentApiImpl implements PraticheApi {

  @Autowired
  private PracticeService praticheService;

  @Autowired
  private LavorazionePraticaService lavorazionePraticaService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private InformazioniPraticaService informazioniPraticaService;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private AttivazioneSistemaEsternoService attivazioneSistemaEsternoService;

  @Autowired
  private ChiamataEsternaService chiamataEsternaService;

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Override
  public Response deletePraticheIdCommentiIdCommento(String idCommento, String id,
      SecurityContext securityContext) {
    Void d = praticheService.deletePraticheIdCommentiIdCommento(idCommento, id);
    if (d != null) {
      return Response.ok(d).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getCommenti(String id, SecurityContext securityContext) {

    PaginaCommenti c = praticheService.getCommenti(id);
    return Response.ok(c).build();
  }

  @Override
  public Response getPraticheIdCommentiIdCommento(String idCommento, String id,
      SecurityContext securityContext) {
    Commento c = praticheService.getPraticheIdCommentiIdCommento(idCommento, id);
    return Response.ok(c).build();
  }

  @Override
  public Response postCommenti(String id, Commento body, SecurityContext securityContext) {
    Commento c = praticheService.postCommenti(id, body);
    return Response.status(201).entity(c).build();
  }

  @Override
  public Response postPratiche(CreaPraticaRequest body, SecurityContext securityContext) {
    OperazioneAsincrona c = praticheService.postPratiche(body);
    return Response.status(201).entity(c).build();
  }

  @Override
  public Response putPraticheId(String id, Pratica body, Boolean aggiornaAttivita,
      SecurityContext securityContext) {
    Pratica p = praticheService.putPratiche(id, body, aggiornaAttivita);
    return Response.ok(p).build();
  }

  @Override
  public Response getPraticheId(String id, SecurityContext securityContext) {
    Pratica p = praticheService.getPraticheId(id);
    return Response.ok(p).build();
  }

  @Override
  public Response getPraticheIdStoricoTask(String id, SecurityContext securityContext) {
    PaginaTask p = praticheService.getPraticheIdStoricoTask(id);
    return Response.ok(p).build();
  }

  @Secured("ADMIN_PRAT")
  @Override
  public Response deletePraticheId(String id, SecurityContext securityContext) {
    praticheService.deletePraticheId(id);
    return Response.noContent().build();
  }

  @Override
  public Response getPraticheIdTask(String id, SecurityContext securityContext) {
    PaginaTask p = praticheService.getPraticheIdTasks(id);
    return Response.ok(p).build();
  }

  @Override
  public Response getPraticheTaskIdTask(String idTask, SecurityContext securityContext) {
    FormTask p = praticheService.getPraticheTaskIdTask(idTask);
    return Response.ok(p).build();
  }

  @Override
  public Response getPraticheStatoIdPraticaExt(String idPraticaExt,
      SecurityContext securityContext) {
    RiassuntoStatoPratica p = praticheService.getPraticheStatoIdPraticaExt(idPraticaExt);
    return Response.ok(p).build();
  }

  @Override
  public Response getPraticheVariabiliProcessInstanceId(String processInstanceId,
      SecurityContext securityContext) {
    var variables = this.cosmoCmmnFeignClient.getProcessInstanceVariables(processInstanceId);
    var response = new VariabiliProcessoResponse();
    response.setProcessInstanceId(processInstanceId);
    if (variables != null) {
      Arrays.asList(variables).forEach(restVariable -> {
        VariabileProcesso variabile = new VariabileProcesso();
        if (restVariable != null) {
          variabile.setName(restVariable.getName());
          variabile.setScope(restVariable.getScope());
          variabile.setType(restVariable.getType());
          variabile.setValue(restVariable.getValue());
          if (restVariable.getValue() == null && restVariable.getValueUrl() != null) {
            variabile.setValue(this.getSerializedVariabileValue(processInstanceId, restVariable));
          }
          response.getVariabili().add(variabile);
        }
      });
    }
    return Response.ok(response).build();
  }

  private Object getSerializedVariabileValue(String processInstanceId, RestVariable restVariable) {
    var methodName = "getSerializedVariabileValue";
    Object value = null;
    byte[] raw =
        this.cosmoCmmnFeignClient.getSerializedVariable(processInstanceId, restVariable.getName());
    if (raw != null) {
      ByteArrayInputStream bis = new ByteArrayInputStream(raw);
      try (ObjectInput in = new ObjectInputStream(bis)) {
        value = in.readObject();
      } catch (IOException | ClassNotFoundException e) {
        logger.error(methodName, e.getMessage());
        throw new FlowableVariableHandlingException(e);
      }
    }
    return value;
  }

  /*
   * chiamato quando clicco 'SALVA' sulla lavorazione task
   */
  @Override
  public Response putPraticheVariabiliProcessInstanceId(String processInstanceId,
      List<VariabileProcesso> body, SecurityContext securityContext) {
    RestVariable[] response =
        taskService.putPraticheVariabiliProcessInstanceId(processInstanceId, body);
    return Response.ok(response).build();
  }

  @Override
  public Response getPraticheTaskIdTaskSubtasks(String idTask, SecurityContext securityContext) {
    return Response.ok(praticheService.getPraticheTaskIdTaskSubtasks(idTask)).build();
  }

  @Override
  public Response deletePraticheVariabiliProcessInstanceId(String processInstanceId,
      String variableName, SecurityContext securityContext) {

    this.cosmoCmmnFeignClient.deleteProcessInstanceVariables(processInstanceId, variableName);

    return Response.noContent().build();
  }

  @Override
  public Response postPraticaAssegna(Long idPratica, AssegnaAttivitaRequest body,
      SecurityContext securityContext) {

    return Response.ok(lavorazionePraticaService.postPraticaAssegna(idPratica, body)).build();
  }

  @Override
  public Response postPraticaAttivitaAssegna(Long idPratica, Long idAttivita,
      AssegnaAttivitaRequest body, SecurityContext securityContext) {

    return Response
        .ok(lavorazionePraticaService.postPraticaAttivitaAssegna(idPratica, idAttivita, body))
        .build();
  }

  @Override
  public Response postPraticaAttivitaAssegnami(Long idPratica, Long idAttivita,
      SecurityContext securityContext) {

    return Response
        .ok(lavorazionePraticaService.postPraticaAttivitaAssegnaAMe(idPratica, idAttivita)).build();
  }

  @Override
  public Response postPraticaAttivitaConferma(Long idPratica, Long idAttivita, Task body,
      SecurityContext securityContext) {

    return Response
        .ok(lavorazionePraticaService.postPraticaAttivitaConferma(idPratica, idAttivita, body))
        .build();
  }

  @Override
  public Response postPraticaAttivitaSalva(Long idPratica, Long idAttivita, Task body,
      SecurityContext securityContext) {

    return Response
        .ok(lavorazionePraticaService.postPraticaAttivitaSalva(idPratica, idAttivita, body))
        .build();
  }

  @Override
  public Response getPraticheIdInfo(String idPratica, String idDocumento,
      SecurityContext securityContext) {
    return Response.ok(informazioniPraticaService.getInfoPratica(idPratica, idDocumento)).build();
  }

  @Override
  public Response getPraticaAttivitaAsePayload(Long idPratica, Long idAttivita,
      SecurityContext securityContext) {

    GetPayloadAttivazioneSistemaEsternoResponse result =
        attivazioneSistemaEsternoService.getPayloadAttivazioneSistemaEsterno(idPratica, idAttivita);

    return Response.ok(result).build();
  }

  @Override
  public Response postPraticaAttivitaAseInvia(Long idPratica, Long idAttivita,
      SecurityContext securityContext) {

    RiferimentoOperazioneAsincrona result =
        attivazioneSistemaEsternoService.inviaAttivazioneSistemaEsterno(idPratica, idAttivita);

    return Response.status(201).entity(result).build();
  }

  @Override
  public Response postPraticaChiamataEstermaInvia(Long idPratica, InviaChiamataEsternaRequest body,
      SecurityContext securityContext) {

    RiferimentoOperazioneAsincrona result =
        chiamataEsternaService.inviaChiamataEsternaDaProcesso(idPratica, body);

    return Response.status(201).entity(result).build();
  }

  @Override
  public Response getPraticheIdElaborazioneContesto(Long id, SecurityContext securityContext) {

    var response = informazioniPraticaService.getContestoInformazioniPratica(id);
    return Response.ok(response).build();
  }

  @Override
  public Response postPraticheIdElaborazione(Long id, GetElaborazionePraticaRequest body,
      SecurityContext securityContext) {

    var response = informazioniPraticaService.elaboraInformazioniPratica(id, body);
    return Response.ok(response).build();
  }

  @Override
  public Response patchPraticheVariabiliProcessInstanceId(String processInstanceId,
      List<VariabileProcesso> body, SecurityContext securityContext) {

    return null;
  }


  @Override
  public Response getPraticheHistoryVariables(String processInstanceId,
      Boolean includeProcessVariables, SecurityContext securityContext) {

    var response =
        praticheService.getVariablesFromHistoryProcess(processInstanceId, includeProcessVariables);
    return Response.ok(response).build();
  }

}
