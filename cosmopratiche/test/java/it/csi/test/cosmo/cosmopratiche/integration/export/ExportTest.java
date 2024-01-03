/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.integration.export;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.cosmopratiche.business.service.ImportExportService;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDeploymentsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.EsportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDeploymentWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmopratiche.integration.export.ExportTest.TestConfig;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestDB;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmopratiche.testbed.rest.CosmoCmmnTestFeignClient;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestDB.class, TestConfig.class})
@Transactional
public class ExportTest extends ParentIntegrationTest {

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient(CosmoCmmnTestFeignClient testClient) {
      return new CosmoCmmnFeignClient() {

        @Override
        public ProcessInstanceResponse getProcessInstanceId(String processInstanceId) {
          return null;
        }

        @Override
        public ProcessInstanceResponse getHistoricProcessInstanceId(String processInstanceId) {
          return null;
        }

        @Override
        public TaskResponse getTaskId(String id) {
          return null;
        }

        @Override
        public FormDefinitionsResponseWrapper queryFormDefinitions(String tenantId, String key,
            Boolean latest) {
          return null;
        }

        @Override
        public SimpleForm getFormDefinitionModel(String idFormDefinition) {
          return null;
        }

        @Override
        public byte[] getProcessInstanceDiagram(String processInstanceId) {
          return null;
        }

        @Override
        public byte[] getHistoricProcessInstanceDiagram(String processInstanceId) {
          return null;
        }

        @Override
        public ProcessDefinitionsResponseWrapper listProcessDefinitionsByKey(Boolean latest,
            String key, String tenantId) {
          return testClient.listProcessDefinitionsByKey(latest, key, tenantId);
        }

        @Override
        public JsonNode getProcessDefinitionModel(String processDefinitionId) {
          return testClient.getProcessDefinitionModel(processDefinitionId);
        }

        @Override
        public byte[] getProcessDefinitionResourceData(String processDefinitionId) {
          return testClient.getProcessDefinitionResourceData(processDefinitionId);
        }

        @Override
        public FormDeploymentsResponseWrapper getFormRepositoryDeployments(
            String parentDeploymentId, String tenantId) {
          return testClient.getFormRepositoryDeployments(parentDeploymentId, tenantId);
        }

        @Override
        public FormDefinitionsResponseWrapper getFormRepositoryDefinitionsForFormDeployment(
            String deploymentId, String tenantId) {
          return testClient.getFormRepositoryDefinitionsForFormDeployment(deploymentId, tenantId);
        }

        @Override
        public byte[] getFormDefinitionResourceData(String formDefinitionId) {
          return testClient.getFormDefinitionResourceData(formDefinitionId);
        }

        @Override
        public ProcessDeploymentWrapper createDeployment(MultiValueMap<String, Object> formParams) {
          return null;
        }

      };
    }
  }

  @Autowired
  private ImportExportService importExportService;

  @Test
  public void testEsportazione() throws Exception {

    String codiceTipoPratica = "TestFabio001";

    String tenantId = "r_piemon";

    EsportaTipoPraticaRequest req = new EsportaTipoPraticaRequest();
    req.setCodiceTipoPratica(codiceTipoPratica);
    req.setTenantId(tenantId);
    var exportResult = importExportService.esporta(req);

    File outputfile = new File("C:\\UPAP\\lavoro\\pratica-export.json");

    FileUtils.writeByteArrayToFile(outputfile, exportResult);
  }
}
