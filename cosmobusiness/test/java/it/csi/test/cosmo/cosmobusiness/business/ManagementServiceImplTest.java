/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.flowable.rest.service.api.management.JobResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmobusiness.business.service.ManagementService;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadletterJobResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnManagementFeignClient;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, ManagementServiceImplTest.ManagementServiceTestConfig.class})
@Transactional
public class ManagementServiceImplTest {
  
  @Configuration
  public static class ManagementServiceTestConfig {
    
    @Bean
    @Primary
    public CosmoCmmnManagementFeignClient cosmoCmmnManagementFeignClient() {
      return Mockito.mock(CosmoCmmnManagementFeignClient.class);
    }
  }
  
  @Autowired
  private ManagementService managementService;
  
  @Autowired
  private CosmoCmmnManagementFeignClient cosmoCmmnManagementFeignClient;
  
  @Test
  public void getDeadLetterJobs() {
    setupMockGetDeadLetter();
    managementService.getDeadLetterJobs();
  }
  
  @Test
  public void moveDeadLetterJob() {
    setupMockMoveDeadLetter();
    DeadLetterJobAction action = new DeadLetterJobAction();
    action.setAction("action");
    managementService.moveDeadLetterJob("1", action);
  }
  
  private void setupMockMoveDeadLetter() {
    reset(cosmoCmmnManagementFeignClient);
    doNothing().when(cosmoCmmnManagementFeignClient).moveDeadletterJobs(any(), any());
  }

  private void setupMockGetDeadLetter() {
    reset(cosmoCmmnManagementFeignClient);
    DeadletterJobResponseWrapper wrapper = new DeadletterJobResponseWrapper();
    List<JobResponse> data = new ArrayList<>();
    JobResponse job = new JobResponse();
    job.setProcessInstanceId("1");
    job.setRetries(2);
    job.setDueDate(new Date());
    JobResponse job2 = new JobResponse();
    job2.setProcessInstanceId("111");
    job2.setRetries(2);
    job2.setDueDate(new Date());
    job2.setTenantId("r_piemon");
    JobResponse job3 = new JobResponse();
    job3.setProcessInstanceId("112");
    job3.setRetries(2);
    job3.setDueDate(new Date());
    data.add(job);
    data.add(job2);
    data.add(job3);
    wrapper.setData(data);
    when(cosmoCmmnManagementFeignClient.getDeadletterJobs(any(), any(), any(), any(), any())).thenReturn(wrapper);
  }

}
