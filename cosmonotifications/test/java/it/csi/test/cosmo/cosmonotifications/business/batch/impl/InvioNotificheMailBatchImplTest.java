/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.batch.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.cosmonotifications.business.batch.InvioNotificheMailBatch;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class})
@Transactional
public class InvioNotificheMailBatchImplTest {
  
  @Autowired
  private InvioNotificheMailBatch invioNotificheMailBatch;
  
  @Test
  public void isEnabled() {
    boolean result = invioNotificheMailBatch.isEnabled();
    assertNotNull(result);
    assertTrue(result);
  }
  
  @Test
  public void execute() {
    BatchExecutionContext context = new BatchExecutionContext();
    invioNotificheMailBatch.execute(context);
  }
}
