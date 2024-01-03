/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoCConfigurazioneRepository;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestDB;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentUnitTest;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoCConfigurazioneTestRepository;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {CosmoEcmUnitTestDB.class})
@Transactional
public class ConfigurazioneRepositoryTest extends ParentUnitTest {

  @Autowired
  private CosmoCConfigurazioneTestRepository testRepository;

  @Autowired
  private CosmoCConfigurazioneRepository repository;

  @Test
  public void test() {
    String id = "user.pref.version";
    LocalDateTime now = LocalDateTime.now();

    CosmoCConfigurazione entity = repository.findOne(id);
    entity.setDtInizioVal(Timestamp.valueOf(now.minusDays(5)));
    entity.setDtFineVal(null);
    testRepository.save(entity);

    entity = repository.findOneActive(id).orElse(null);
    assertNotNull(entity);
    assertTrue(entity.getDtFineVal() == null);
    assertTrue(entity.getDtInizioVal().before(Timestamp.valueOf(now)));

    entity.setDtInizioVal(Timestamp.valueOf(now.plusDays(5)));
    entity.setDtFineVal(null);
    testRepository.save(entity);

    entity = repository.findOneActive(id).orElse(null);
    assertNull(entity);
    entity = repository.findOne(id);
    assertTrue(entity.getDtFineVal() == null);
    assertTrue(entity.getDtInizioVal().after(Timestamp.valueOf(now)));

    entity.setDtInizioVal(Timestamp.valueOf(now.minusDays(5)));
    entity.setDtFineVal(Timestamp.valueOf(now.plusDays(5)));
    testRepository.save(entity);

    entity = repository.findOneActive(id).orElse(null);
    assertNotNull(entity);
    assertTrue(entity.getDtFineVal().after(Timestamp.valueOf(now)));
    assertTrue(entity.getDtInizioVal().before(Timestamp.valueOf(now)));

    entity.setDtInizioVal(Timestamp.valueOf(now.minusDays(5)));
    entity.setDtFineVal(Timestamp.valueOf(now.minusDays(2)));
    testRepository.save(entity);

    entity = repository.findOneActive(id).orElse(null);
    assertNull(entity);
    entity = repository.findOne(id);
    assertTrue(entity.getDtFineVal().before(Timestamp.valueOf(now)));
    assertTrue(entity.getDtInizioVal().before(Timestamp.valueOf(now)));

  }

}
