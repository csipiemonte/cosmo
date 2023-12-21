/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.business.service.MailService;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class MailServiceImplTest extends ParentIntegrationTest {
  
  @Autowired
  private MailService mailService;
  
  private static final String PROVA = "prova";
  private static final String TO = "email@email.it";
  
  @Test
  public void inviaMailAssistenza() throws InterruptedException, ExecutionException {
    Future<CosmoMailDeliveryResult> future = mailService.inviaMailAssistenza(PROVA, PROVA);
  
    CosmoMailDeliveryResult result = future.get();
    
    log("RESULT: " + ObjectUtils.represent(result));
  }
  
  @Test
  public void inviaMailUtentiBatch() throws InterruptedException, ExecutionException {
    Future<CosmoMailDeliveryResult> future = mailService.inviaMailUtentiBatch(TO, PROVA, PROVA);
  
    CosmoMailDeliveryResult result = future.get();
    
    log("RESULT: " + ObjectUtils.represent(result));
  }
}
