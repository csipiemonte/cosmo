/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import java.util.List;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;

public interface TaskService {

  Task getTaskId(String id);

  Task putTask(String id, Task body);

  PaginaTask getTask(String dueAfter, String sort, String size, String start, String dueBefore);

  Task postTaskId(String id, Task body);

  TaskResponse postTask(Task body);

  Commento postTaskIdtaskComments(String idtask, Commento body, Boolean reply);

  PaginaCommenti getTaskIdtaskComments(String idtask);

  RestVariable[] putPraticheVariabiliProcessInstanceId(String processInstanceId,
      List<VariabileProcesso> body);

}
