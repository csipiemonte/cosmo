/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.testbed.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDeploymentsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDeploymentWrapper;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoPraticheCmmnFeignClientConfiguration;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoPraticheFeignClientConfiguration;


/**
 *
 */

@FeignClient(value = "http://dev-cosmoint.csi.it/cosmocmmn/api",
    interceptors = CosmoPraticheFeignClientConfiguration.class,
    configurator = CosmoPraticheCmmnFeignClientConfiguration.class)
public interface CosmoCmmnTestFeignClient {

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
  @Path("/process/repository/deployments")
  ProcessDeploymentWrapper createDeployment(MultiValueMap<String, Object> formParams);

}


