/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import org.flowable.rest.service.api.runtime.task.TaskQueryRequest;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.EventiService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.EventoMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.TaskMapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;

@Service
public class EventiServiceImpl implements EventiService {


  @Autowired
  CosmoCmmnFeignClient cmmClient;

  @Autowired
  TaskMapper taskMapper;

  @Autowired
  EventoMapper eventoMapper;

  // -------------------------- AUX -----------------------------------


  private Date strToDate(String data) {
    Date d = new Date();
    try {
      d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d;
  }


  // -------------------------- GET -----------------------------------

  @Override
  public PaginaEventi getEventi(String nome, String utente, String dtCreazionePrima, String dtCreazioneDopo,
      String dtScadenzaPrima, String dtScadenzaDopo, Boolean sospeso, String ente, Integer inizio,
      Integer dimensionePagina) {

    TaskQueryRequest req = new TaskQueryRequest();
    req.setAssignee(utente);
    req.setName(nome);
    req.setCreatedBefore(strToDate(dtCreazionePrima));
    req.setCreatedAfter(strToDate(dtCreazioneDopo));
    req.setDueBefore(strToDate(dtScadenzaPrima));
    req.setDueAfter(strToDate(dtScadenzaDopo));
    req.setActive(sospeso != null ? !sospeso : null);
    req.setTenantId(ente);
    req.setSize(dimensionePagina);
    req.setStart(inizio);
    TaskResponseWrapper response = cmmClient.postQueryTask(req);
    PaginaEventi p = new PaginaEventi();

    p.setElementi(
        response.getData()
        .stream()
        .map(tr -> eventoMapper.toEvento(taskMapper.toTask(tr))).collect(Collectors.toList())
        );

    return p;
  }



  @Override
  public Evento getEventiId(String id) {
    try {
      return eventoMapper.toEvento(taskMapper.toTask(cmmClient.getTaskId(id)));
    } catch (HttpClientErrorException e1) {
      throw new NotFoundException(e1);
    }
  }

  // -------------------------- POST ----------------------------------


  @Override
  public Evento postEvento(Evento body) {

    ValidationUtils.validaAnnotations(body);

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      throw new BadRequestException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
    }

    Task task = new Task();
    task.setAssignee(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    task.setName(body.getNome());
    task.setDueDate(body.getDtScadenza());
    task.setTenantId(SecurityUtils.getUtenteCorrente().getEnte().getTenantId());
    task.setDescription(body.getDescrizione());
    TaskResponse response = cmmClient.postTask(task);
    return eventoMapper.toEvento(taskMapper.toTask(response));
  }


  // -------------------------- PUT -----------------------------------


  @Override
  public Evento putEvento(String id, Evento body) {

    ValidationUtils.validaAnnotations(body);

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      throw new BadRequestException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
    }

    Task task = new Task();
    task.setName(body.getNome());
    task.setDueDate(body.getDtScadenza());
    task.setDescription(body.getDescrizione());

    TaskResponse response = cmmClient.putTask(id, task);
    return eventoMapper.toEvento(taskMapper.toTask(response));
  }

  @Override
  public void deleteEvento(String id) {
    cmmClient.deleteTask(id);
  }
}
