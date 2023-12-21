/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmoauthorization.business.service.TipiTagsService;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class TipiTagsServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private TipiTagsService tipiTagsService;


  @Test
  public void getAllTipiTags() {
    List<TipoTag> tipiTags = tipiTagsService.getTipiTags();
    assertFalse("Ricerca tipi tags non nulla", CollectionUtils.isEmpty(tipiTags));
    assertTrue("I tipi di tags totali devono essere 3", tipiTags.size() == 3);
  }

}
