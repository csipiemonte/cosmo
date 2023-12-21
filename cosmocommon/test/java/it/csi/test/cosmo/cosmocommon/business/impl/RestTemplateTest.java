/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.util.RestTemplateUtils;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class RestTemplateTest extends ParentUnitTest {

  @Test
  public void testRT() {
    RestTemplate rt = RestTemplateUtils.builder().build();
    
    
  }
}
