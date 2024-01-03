/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestDB;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentUnitTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {CosmoEcmUnitTestDB.class})
@Transactional
public class DocumentoRepositoryTest extends ParentUnitTest {

  @Autowired
  private CosmoTDocumentoRepository repository;

  @Autowired
  private CosmoTPraticaRepository praticaRepository;

  private CosmoTDocumento creaDocumento() {
    CosmoTDocumento entity = new CosmoTDocumento();
    entity.setAutore("AUTORE " + UUID.randomUUID().toString());
    entity.setDescrizione("descrizione " + UUID.randomUUID().toString());
    entity.setPratica(praticaRepository.findAllNotDeleted().stream().findFirst().orElseThrow());
    entity.setStato(
        repository.reference(CosmoDStatoDocumento.class, StatoDocumento.ELABORATO.name()));
    entity.setTitolo("TITOLO " + UUID.randomUUID().toString());
    return repository.saveAndFlush(entity);
  }

  @Test
  public void testFindByField() {
    var originale = creaDocumento();

    assertTrue(StringUtils.isNotBlank(originale.getTitolo()));

    var foundByTitolo = repository.findByField(CosmoTDocumento_.titolo, originale.getTitolo());
    assertEquals(1, foundByTitolo.size());
    assertEquals(originale.getTitolo(), foundByTitolo.get(0).getTitolo());

    assertEquals(1,
        repository.findNotDeletedByField(CosmoTDocumento_.titolo, originale.getTitolo()).size());
    assertEquals(1,
        repository.findNotDeletedByField(CosmoTDocumento_.titolo, originale.getTitolo(),
            new Sort(CosmoTDocumento_.id.getName())).size());
    assertEquals(1,
        repository.findNotDeletedByField(CosmoTDocumento_.titolo, originale.getTitolo(),
            new PageRequest(0, 10)).getTotalElements());
    assertTrue(repository.findByField(CosmoTDocumento_.titolo, originale.getTitolo() + "-another")
        .isEmpty());
  }

  @Test
  public void testCancellazioneLogica() {

    Long id = creaDocumento().getId();
    assertNotNull(id);

    CosmoTDocumento entity;

    repository.deleteLogically(id, SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    entity = repository.findOneNotDeleted(id).orElse(null);
    assertNull(entity);
    entity = repository.findOne(id);
    assertTrue(entity.getDtCancellazione() != null);
    assertTrue(!StringUtils.isBlank(entity.getUtenteCancellazione()));

    repository.restore(id);
    entity = repository.findOneNotDeleted(id).orElse(null);
    assertNotNull(entity);
    assertTrue(entity.getDtCancellazione() == null);
    assertTrue(StringUtils.isBlank(entity.getUtenteCancellazione()));

    repository.deleteLogically(id, SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    entity = repository.findAllNotDeleted().stream().filter(o -> o.getId().equals(id)).findFirst()
        .orElse(null);
    assertNull(entity);
    entity = repository.findOne(id);
    assertTrue(entity.getDtCancellazione() != null);
    assertTrue(!StringUtils.isBlank(entity.getUtenteCancellazione()));

    repository.restore(id);

    entity = repository.findAllNotDeleted().stream().filter(o -> o.getId().equals(id)).findFirst()
        .orElse(null);
    assertNotNull(entity);
    assertTrue(entity.getDtCancellazione() == null);
    assertTrue(StringUtils.isBlank(entity.getUtenteCancellazione()));

  }

}
