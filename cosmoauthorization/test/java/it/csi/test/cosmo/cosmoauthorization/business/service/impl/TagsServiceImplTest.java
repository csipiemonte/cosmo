/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.TagsService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTipoTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagsResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class TagsServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private TagsService tagsService;

  @Test
  public void getTag() {
    TagResponse tag = tagsService.getTagById(1L);

    assertNotNull("Tag non nullo", tag.getTag());
  }

  @Test
  public void getAllTags() {
    String filtro = "{}";
    TagsResponse tags = tagsService.getTags(filtro);
    assertFalse("Ricerca tags non nulla", CollectionUtils.isEmpty(tags.getElementi()));
    assertTrue("I tag totali devono essere 5", tags.getElementi().size() == 5);
  }

  @Test
  public void getTagByCodice() {
    String filtro = "{\"filter\":{\"codice\": {\"eq\":\"AOO-TO\"}},\"page\": 0,\"size\":10}";
    TagsResponse tags = tagsService.getTags(filtro);
    assertFalse("Ricerca tags non nulla", CollectionUtils.isEmpty(tags.getElementi()));
    assertTrue(
        "Il tag deve essere unico, con codice AOO-TO",
        tags.getElementi().size() == 1 && tags.getElementi().get(0).getCodice().equals("AOO-TO"));
  }

  @Test
  public void getTagByDescrizione() {
    String filtro =
        "{\"filter\":{\"descrizione\": {\"ci\":\"Direttore\"}},\"page\": 0,\"size\":10}";
    TagsResponse tags = tagsService.getTags(filtro);
    assertFalse("Ricerca tags non nulla", CollectionUtils.isEmpty(tags.getElementi()));
    assertTrue("Devono essere presenti 2 tag", tags.getElementi().size() == 2);
  }

  @Test
  public void getTagByTipoTagAndEnte() {
    String filtro =
        "{\"filter\":{\"codiceTipoTag\": {\"eq\":\"DIR\"}, \"codiceIpaEnte\":{\"eq\":\"r_piemon\"}},\"page\": 0,\"size\":10}";

    TagsResponse tags = tagsService.getTags(filtro);
    assertFalse("Ricerca tags non nulla", CollectionUtils.isEmpty(tags.getElementi()));
    assertTrue("Devono essere presenti due tag di direzione", tags.getElementi().size() == 2);
  }

  @Test
  public void getTagByIdGruppoAndIdUtente() {
    String filtro =
        "{\"filter\":{\"idGruppo\": {\"eq\":\"1\"}, \"idUtente\":{\"eq\":\"1\"}},\"page\": 0,\"size\":10}";

    TagsResponse tags = tagsService.getTags(filtro);
    assertFalse("Ricerca tags non nulla", CollectionUtils.isEmpty(tags.getElementi()));
    assertTrue("Il tag deve essere unico", tags.getElementi().size() == 1);
  }

  @Test
  public void modificaTag() {

    TagResponse tag = tagsService.getTagById(1L);

    AggiornaTagRequest tagDaAggiornare = new AggiornaTagRequest();
    tagDaAggiornare.setDescrizione("Nuova Descrizione");
    tagDaAggiornare.setCodice("Nuovo");
    AggiornaTipoTagRequest newTipoTag = new AggiornaTipoTagRequest();
    newTipoTag.setCodice("NUOVO");
    tagDaAggiornare.setTipoTag(newTipoTag);
    RiferimentoEnte newEnte = new RiferimentoEnte();
    newEnte.setCodiceIpa("cmto");
    newEnte.setCodiceFiscale("01907990012");
    newEnte.setId(3L);
    tagDaAggiornare.setEnte(newEnte);

    TagResponse tagAggiornato = tagsService.updateTag(1L, tagDaAggiornare);

    assertNotNull("Il Tag non deve essere nullo", tagAggiornato);
    assertTrue("Descrizione aggiornata con successo",
        !tag.getTag().getDescrizione().equals(tagAggiornato.getTag().getDescrizione())
            && !tag.getTag().getCodice().equals(tagAggiornato.getTag().getCodice()) && !tag.getTag()
                .getTipoTag().getCodice().equals(tagAggiornato.getTag().getTipoTag().getCodice()));
  }

  @Test(expected = NotFoundException.class)
  public void eliminaTag() {
    tagsService.deleteTag(1L);
    tagsService.getTagById(1L);
  }

  @Test
  public void creaTag() {
    CreaTagRequest body = new CreaTagRequest();
    body.setCodice("codice");
    body.setDescrizione("descrizione");
    RiferimentoEnte ente = new RiferimentoEnte();
    ente.setCodiceFiscale("01907990012");
    ente.setCodiceIpa("cmto");
    ente.setId(3L);
    body.setEnte(ente);
    AggiornaTipoTagRequest tipoTag = new AggiornaTipoTagRequest();
    tipoTag.setCodice("1");
    tipoTag.setDescrizione("1");
    tipoTag.setLabel("label");
    body.setTipoTag(tipoTag);
    RiferimentoUtente ru = new RiferimentoUtente();
    ru.setCodiceFiscale("AAAAAA00B77B000F");
    ru.setId(1L);
    tagsService.postTag(body);
  }

  @Test(expected = NotFoundException.class)
  public void deleteTagNotFound() {
    tagsService.deleteTag(111L);
  }
  
  @Test(expected = ConflictException.class)
  public void postTagCodiceEsistente() {
   CreaTagRequest body = new CreaTagRequest();
   body.setCodice("DIR-ACQ");
   tagsService.postTag(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateTagNotFound() {
    AggiornaTagRequest body = new AggiornaTagRequest();
    body.setCodice("prova");
    tagsService.updateTag(111L, body);
  }
  
  @Test(expected = ConflictException.class)
  public void updateTagConCodiceEsistente() {
    AggiornaTagRequest body = new AggiornaTagRequest();
    body.setCodice("DIR-ACQ");
    tagsService.updateTag(2L, body);
  }
  
  
  
  
  
  
  
  
  
  
  
  
}
