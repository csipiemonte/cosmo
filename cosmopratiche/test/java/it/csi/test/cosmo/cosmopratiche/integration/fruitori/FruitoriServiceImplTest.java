/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmopratiche.integration.fruitori;


import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmopratiche.business.rest.impl.FruitoriApiServiceImpl;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoTag;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmopratiche.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmopratiche.testbed.service.FruitoreTestbedService;
import it.csi.test.cosmo.cosmopratiche.testbed.service.UtenteTestbedService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class FruitoriServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private FruitoreTestbedService fruitoreTestbedService;

  @Autowired
  private EnteTestbedService enteTestbedService;

  @Autowired
  private UtenteTestbedService utenteTestbedService;

  private CosmoTEnte ente;

  private CosmoTUtente utente;

  @Override
  protected void autentica() {
    var fruitore = fruitoreTestbedService.creaFruitore();

    var client = ClientInfoDTO.builder().withCodice(fruitore.getApiManagerId())
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();

    //    ente = enteTestbedService.creaEnte();
    ente = enteTestbedService.getEnte("r_piemon");
    fruitoreTestbedService.associaAdEnte(fruitore, ente);

    utente = utenteTestbedService.creaUtente();
    utenteTestbedService.associaAdEnte(utente, ente);

    autentica(null, client);
  }

  private CreaPraticaFruitoreRequest creaPratica() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("TP1");
    request.setIdPratica("1");
    request.setOggetto("Con Tag");
    request.setUtenteCreazionePratica("AAAAAA00A00A000A");
    return request;
  }

  @Test
  public void postPraticaConUnTag() {

    CreaPraticaFruitoreRequest request = creaPratica();

    List<TagRidotto> tags = new LinkedList<>();
    TagRidotto tag = new TagRidotto();
    tag.setCodice("DIR-ACQ");
    tag.setDescrizione("Direttore acquisti");
    TipoTag tipoTag = new TipoTag();
    tipoTag.setCodice("DIR");
    tipoTag.setDescrizione("Direzione");
    tag.setTipoTag(tipoTag);

    tags.add(tag);
    request.setTags(tags);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var response = api.postPraticheFruitori(null, null, request, null);
    assertNotNull(response);

    var responseEntity = (CreaPraticaFruitoreResponse) response.getEntity();
    assertNotNull(responseEntity);
    // assertEquals(responseEntity.getTags().size(), 1);
  }

  @Test(expected = BadRequestException.class)
  public void postPraticaConUnTagAltroEnte() {

    CreaPraticaFruitoreRequest request = creaPratica();

    List<TagRidotto> tags = new LinkedList<>();
    TagRidotto tag = new TagRidotto();
    tag.setCodice("DIR-VEN");
    tag.setDescrizione("Direttore vendite");
    TipoTag tipoTag = new TipoTag();
    tipoTag.setCodice("DIR");
    tipoTag.setDescrizione("Direzione");
    tag.setTipoTag(tipoTag);

    tags.add(tag);
    request.setTags(tags);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var response = api.postPraticheFruitori(null, null, request, null);
    assertNotNull(response);
  }

  @Test
  public void postPraticaConDueTag() {

    CreaPraticaFruitoreRequest request = creaPratica();

    List<TagRidotto> tags = new LinkedList<>();
    TagRidotto tag = new TagRidotto();
    tag.setCodice("DIR-ACQ");
    tag.setDescrizione("Direttore acquisti");
    TipoTag tipoTag = new TipoTag();
    tipoTag.setCodice("DIR");
    tipoTag.setDescrizione("Direzione");
    tag.setTipoTag(tipoTag);

    tags.add(tag);

    TagRidotto tag2 = new TagRidotto();
    tag2.setCodice("AOO-TO");
    tag2.setDescrizione("Area Organizzativa Omogenea Torino");
    TipoTag tipoTag2 = new TipoTag();
    tipoTag2.setCodice("AOO");
    tipoTag2.setDescrizione("AOO");
    tag2.setTipoTag(tipoTag2);

    tags.add(tag2);
    request.setTags(tags);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var response = api.postPraticheFruitori(null, null, request, null);
    assertNotNull(response);

    var responseEntity = (CreaPraticaFruitoreResponse) response.getEntity();
    assertNotNull(responseEntity);
    // assertEquals(responseEntity.getTags().size(), 2);
  }

  @Test
  public void postPraticaConDueTagTipoErratoInUnCaso() {

    CreaPraticaFruitoreRequest request = creaPratica();

    List<TagRidotto> tags = new LinkedList<>();
    TagRidotto tag = new TagRidotto();
    tag.setCodice("DIR-ACQ");
    tag.setDescrizione("Direttore acquisti");
    TipoTag tipoTag = new TipoTag();
    tipoTag.setCodice("DIR");
    tipoTag.setDescrizione("Direzione");
    tag.setTipoTag(tipoTag);

    tags.add(tag);

    TagRidotto tag2 = new TagRidotto();
    tag2.setCodice("AOO-TO");
    tag2.setDescrizione("Area Organizzativa Omogenea Torino");
    tag2.setTipoTag(tipoTag);

    tags.add(tag2);
    request.setTags(tags);

    var api = buildResource(FruitoriApiServiceImpl.class);
    var response = api.postPraticheFruitori(null, null, request, null);
    assertNotNull(response);

    var responseEntity = (CreaPraticaFruitoreResponse) response.getEntity();
    assertNotNull(responseEntity);
    // assertEquals(responseEntity.getTags().size(), 2);
    // assertNull(responseEntity.getTags().get(0).getWarning());
    // assertNotNull(responseEntity.getTags().get(1).getWarning());
  }


  @Test
  public void postPraticaSenzaTag() {

    var api = buildResource(FruitoriApiServiceImpl.class);
    var response = api.postPraticheFruitori(null, null, creaPratica(), null);
    assertNotNull(response);

    var responseEntity = (CreaPraticaFruitoreResponse) response.getEntity();
    assertNotNull(responseEntity);
    // assertTrue(responseEntity.getTags().isEmpty());
  }
  
  @Test
  public void getPraticheFruitori() {
    
    var api = buildResource(FruitoriApiServiceImpl.class);
    String filter = parse(
        "{'codiceFiscaleUtente':'AAAAAA00B77B000F', 'codiceIpaEnte':'r_piemon', 'page': 0,'size':10}");
    var response = api.getFruitoriPratiche(filter, null, null, getSecurityContext());
    assertNotNull(response);
  }
  
  protected String parse(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.replace("'", "\"");
  }
}
