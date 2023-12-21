/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FormTask;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;

public interface PracticeService {

  public ProcessInstanceResponse avviaProcesso(CosmoTPratica pratica);

  public OperazioneAsincrona postPratiche(CreaPraticaRequest body);

  public Pratica putPratiche(String id, Pratica body, Boolean assegnaAttivita);

  public Void deletePraticheIdCommentiIdCommento(String idCommento, String processInstanceId);

  public PaginaCommenti getCommenti(String processInstanceId);

  public Commento getPraticheIdCommentiIdCommento(String idCommento, String processInstanceId);

  public Commento postCommenti(String processInstanceId, Commento body);

  public Pratica getPraticheId(String id);

  public PaginaTask getPraticheIdStoricoTask(String id);

  public PaginaTask getPraticheIdTasks(String id);

  public void deletePraticheId(String id);

  public FormTask getPraticheTaskIdTask(String idTask);

  public RiassuntoStatoPratica getPraticheStatoIdPraticaExt(String idPraticaExt);

  public PaginaTask getPraticheTaskIdTaskSubtasks(String idTask);

  public void creaNotificaDelCommento(Long idPratica, String commento);

  public ProcessInstanceResponse avviaProcesso(String idPratica);

  VariabiliProcessoResponse getVariablesFromHistoryProcess(String processInstanceId,
      Boolean includeProcessVariables);

  void creaVistoGrafico(Long idAttivita);


}
