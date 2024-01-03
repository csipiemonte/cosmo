/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmopratiche.integration.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDeploymentsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDeploymentWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/cmmn",
    interceptors = CosmoPraticheFeignClientConfiguration.class,
    configurator = CosmoPraticheCmmnFeignClientConfiguration.class)
public interface CosmoCmmnFeignClient {

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}")
  ProcessInstanceResponse getProcessInstanceId(
      @PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/history/historic-process-instances/{processInstanceId}")
  ProcessInstanceResponse getHistoricProcessInstanceId(
      @PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/tasks/{id}")
  TaskResponse getTaskId(@PathParam("id") String id);

  @GET
  @Path("/form/form-repository/form-definitions")
  FormDefinitionsResponseWrapper queryFormDefinitions(@QueryParam("tenantId") String tenantId,
      @QueryParam("key") String key, @QueryParam("latest") Boolean latest);

  @GET
  @Path("/form/form-repository/form-definitions/{idFormDefinition}/model")
  SimpleForm getFormDefinitionModel(@PathParam("idFormDefinition") String idFormDefinition);

  @GET
  @Path("/process/runtime/process-instances/{processInstanceId}/diagram")
  byte[] getProcessInstanceDiagram(@PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/runtime/historic-process-instances/{processInstanceId}/diagram")
  byte[] getHistoricProcessInstanceDiagram(
      @PathParam("processInstanceId") String processInstanceId);

  @GET
  @Path("/process/repository/process-definitions")
  ProcessDefinitionsResponseWrapper listProcessDefinitionsByKey(
      @QueryParam("latest") Boolean latest, @QueryParam("key") String key,
      @QueryParam("tenantId") String tenantId);

  @GET
  @Path("/process/repository/process-definitions/{processDefinitionId}/model")
  JsonNode getProcessDefinitionModel(@PathParam("processDefinitionId") String processDefinitionId);

  @GET
  @Path("/process/repository/process-definitions/{processDefinitionId}/resourcedata")
  byte[] getProcessDefinitionResourceData(
      @PathParam("processDefinitionId") String processDefinitionId);

  @GET
  @Path("/form/form-repository/deployments")
  FormDeploymentsResponseWrapper getFormRepositoryDeployments(
      @QueryParam("parentDeploymentId") String parentDeploymentId,
      @QueryParam("tenantId") String tenantId);

  @GET
  @Path("/form/form-repository/form-definitions")
  FormDefinitionsResponseWrapper getFormRepositoryDefinitionsForFormDeployment(
      @QueryParam("deploymentId") String deploymentId, @QueryParam("tenantId") String tenantId);

  @GET
  @Path("/form/form-repository/form-definitions/{formDefinitionId}/resourcedata")
  byte[] getFormDefinitionResourceData(@PathParam("formDefinitionId") String formDefinitionId);

  @Produces(MediaType.MULTIPART_FORM_DATA)
  @POST
  @Path("/process/repository/deployments-m2m")
  ProcessDeploymentWrapper createDeployment(MultiValueMap<String, Object> formParams);

}
