/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.integration.rest;

import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskQueryRequest;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.HistoricVariablesPage;
import it.csi.cosmo.cosmobusiness.dto.rest.IdentityLink;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/cmmn",
    interceptors = CosmoBusinessFeignClientConfiguration.class,
    configurator = CosmoBusinessCmmnFeignClientConfiguration.class)
public interface CosmoCmmnFeignClient {

  // ----------------------GET----------------------

  @GET
  @Path("/process/runtime/tasks/{taskId}/identitylinks")
  IdentityLink[] getIdentityLinksByTaskId(@PathParam("taskId") String taskId);

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}")
  ProcessInstanceResponse getProcessInstance(@PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}/variables/{variableName}/data")
  byte[] getSerializedVariable(@PathParam("processInstanceId") String processInstanceId,
      @PathParam("variableName") String variableName);

  @GET
  @Path("/process/history/historic-process-instances/{processInstanceId}/variables/{variableName}/data")
  byte[] getSerializedHistoryVariable(@PathParam("processInstanceId") String processInstanceId,
      @PathParam("variableName") String variableName);

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}/variables/{variableName}")
  RestVariable getProcessInstanceVariable(@PathParam("processInstanceId") String processInstanceId,
      @PathParam("variableName") String variableName);

  @GET
  @Path("/process/runtime/tasks")
  TaskResponseWrapper getTasks(@QueryParam("assignee") String assignee,
      @QueryParam("dueBefore") String dueBefore, @QueryParam("dueAfter") String dueAfter,
      @QueryParam("sort") String sort, @QueryParam("size") String size,
      @QueryParam("start") String start, @QueryParam("tenantId") String tenantId);


  @GET
  @Path("/process/runtime/tasks")
  TaskResponseWrapper getTasksByProcessInstanceId(
      @QueryParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/tasks")
  TaskResponseWrapper getPraticheTasksProcessInstanceId(
      @QueryParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}/variables")
  RestVariable[] getProcessInstanceVariables(
      @PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/tasks/{id}")
  TaskResponse getTaskId(@PathParam("id") String id);

  @GET
  @Path("/process/history/historic-task-instances/{taskId}")
  TaskResponse getHistoricTaskId(@PathParam("taskId") String taskId);

  @GET
  @Path("/process/runtime/tasks/{taskId}/variables")
  RestVariable[] getTaskVariables(@PathParam("taskId") String taskId);

  @GET
  @Path("/process/history/historic-process-instances")
  ProcessInstanceWrapper getHistoricProcessInstances(
      @QueryParam("processInstanceId") String processInstanceId,
      @QueryParam("includeProcessVariables") Boolean includeProcessVariables);

  @Deprecated(forRemoval = true)
  @GET
  @Path("/process/history/historic-activity-instances")
  List<Map<String, String>> getPraticheIdStoricoTask(
      @QueryParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/history/historic-activity-instances")
  TaskResponseWrapper getPraticheIdStoricoAttivitaTask(
      @QueryParam("processInstanceId") String processInstanceId,
      @QueryParam("activityType") String activityType);

  @GET
  @Path("/process/runtime/process-instances")
  ProcessInstanceWrapper getProcessInstancesByBusinessKey(
      @QueryParam("businessKey") String businessKey);

  @GET
  @Path("/process/history/historic-process-instances")
  ProcessInstanceWrapper getHistoricProcessInstancesByBusinessKey(
      @QueryParam("businessKey") String businessKey);

  @GET
  @Path("/process/runtime/executions")
  ExecutionWrapper getExecutions(
      @QueryParam("messageEventSubscriptionName") String messageEventSubscriptionName,
      @QueryParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/history/historic-variable-instances")
  HistoricVariablesPage getHistoricVariableInstances(
      @QueryParam("processInstanceId") String processInstanceId);

  // ----------------------POST----------------------

  @POST
  @Path("/process/runtime/tasks")
  TaskResponse postTask(Task task);

  @Deprecated(forRemoval = true)
  @POST
  @Path("/process/runtime/tasks/{id}")
  Map<String, Object> postTaskId(@PathParam("id") String id, Task task);

  @POST
  @Path("/process/runtime/process-instances")
  ProcessInstanceResponse postPratica(Map<String, Object> body);

  @POST
  @Path("/process/query/tasks")
  TaskResponseWrapper postQueryTask(TaskQueryRequest task);

  @POST
  @Path("/process/runtime/tasks/{id}/variables")
  RestVariable[] postTaskVariables(@PathParam("id") String id, List<RestVariable> variables);

  @POST
  @Path("/process/runtime/process-instances")
  ProcessInstanceResponse postProcessInstance(ProcessInstanceCreateRequest body);

  // ----------------------PUT----------------------

  @PUT
  @Path("/process/runtime/tasks/{id}")
  TaskResponse putTask(@PathParam("id") String id, Task task);

  @PUT
  @Path("/process/runtime/process-instances/{processInstanceId}/variables")
  RestVariable[] putProcessInstanceVariables(
      @PathParam("processInstanceId") String processInstanceId, List<RestVariable> variables);

  @Deprecated(forRemoval = true)
  @PUT
  @Path("/process/runtime/process-instances/{idPraticaExt}")
  void putPraticaIdPraticaExt(@PathParam("idPraticaExt") String idPraticaExt,
      Map<String, Object> jsonPayload);

  @PUT
  @Path("/process/runtime/executions/{executionId}")
  ExecutionWrapper putExecution(
      @PathParam("executionId") String executionId,
      ExecutionActionRequest request);

  // ----------------------DELETE----------------------

  @DELETE
  @Path("/process/runtime/tasks/{id}")
  TaskResponse deleteTask(@PathParam("id") String id);

  @DELETE
  @Path("/process/runtime/process-instances/{processInstanceId}/variables/{variableName}")
  void deleteProcessInstanceVariables(@PathParam("processInstanceId") String processInstanceId,
      @PathParam("variableName") String variableName);

}
