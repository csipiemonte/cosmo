/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.TaskApi;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

public class TaskApiServiceImpl extends ParentApiImpl implements TaskApi {

  @Autowired
  private TaskService taskService;

  @Override
  public Response getTaskId(String id, SecurityContext securityContext) {
    Task e = taskService.getTaskId(id);
    return Response.ok(e).build();
  }

  @Override
  public Response putTaskId(String id, Task body, SecurityContext securityContext) {
    Task e = taskService.putTask(id, body);
    return Response.ok(e).build();
  }

  @Override
  public Response getTask(String dueAfter, String sort, String size, String start, String dueBefore,
      SecurityContext securityContext) {
    PaginaTask e = taskService.getTask(dueAfter, sort, size, start, dueBefore);
    return Response.ok(e).build();
  }

  @Override
  public Response postTaskId(String id, Task body, SecurityContext securityContext) {
    Task e = taskService.postTaskId(id, body);
    return Response.status(201).entity(e).build();
  }

  @Override
  public Response postTask(Task body, SecurityContext securityContext) {
    TaskResponse e = taskService.postTask(body);
    return Response.status(201).entity(e).build();
  }

  @Override
  public Response getTaskIdtaskComments(String idtask, SecurityContext securityContext) {
    return Response.ok(taskService.getTaskIdtaskComments(idtask)).build();
  }

  @Override
  public Response postTaskIdtaskComments(String idtask, Commento body, Boolean reply,
      SecurityContext securityContext) {
    return Response.ok(taskService.postTaskIdtaskComments(idtask, body, reply)).build();
  }
}
