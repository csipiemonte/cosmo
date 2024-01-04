/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.acta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmosoap.business.service.ActaService;
import it.csi.cosmo.cosmosoap.dto.acta.ContestoOperativoUtenteActa;
import it.csi.cosmo.cosmosoap.dto.acta.InformazioniActaEnte;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaUtente;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentoFisicoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentoRequest;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaClientHelper;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoSempliceActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.RegistrazioneClassificazioniActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.parser.ActaParser;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;
import it.doqui.acta.acaris.common.EnumPropertyFilter;
import it.doqui.acta.acaris.common.EnumQueryOperator;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertyFilterType;
import it.doqui.acta.acaris.common.QueryConditionType;
import it.doqui.acta.acaris.common.QueryableObjectType;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class ActaTest extends ParentUnitTest {

  private static final String TEST_CF = "DVNLRD99A01L219J";
  private static final String TEST_REPO_NAME = "RP201209 Regione Piemonte - Agg. 09/2012";
  private static final String TEST_APP_KEY =
      "-35/35/89/-39/-33/-46/35/37/-83/3/-81/16/-101/-123/-123/-58";
  private static final String FILTRO_OGGETTO_LIKE =
      "{\"filter\":{\"oggetto\":{\"lk\":\"Fattura n. 2015-31000 del 05/03/2015\"}}}";

  @Autowired
  private ActaService actaService;


  @Test
  public void testIdentita() {

    var infoEnte = getConfigurazioneActaEnteFittizio();

    var result = actaService.findIdentitaDisponibili(ContestoOperativoUtenteActa.builder()
        .withCodiceFiscaleUtente(TEST_CF).withEnte(infoEnte).build(), creaIdentitaUtente());

    dump("T0 - identita", result);
  }


  @Test
  public void testRicercaProtocolli() throws Exception {

    var client = actaService.getClient(ObjectUtils.toJson(creaIdentitaUtente()));

    PrincipalIdType principalId = new PrincipalIdType();
    principalId
    .setValue(ActaClientHelper.getValidPrincipal(client.getProvider(), client.getContesto()));

    ObjectIdType repositoryId = ActaClientHelper.wrapId(client.getContesto().getRepository());

    QueryableObjectType target = new QueryableObjectType();
    target.setObject("RegistrazioneClassificazioniView");
    List<QueryConditionType> criteria = new LinkedList<>();

    PropertyFilterType filter = new PropertyFilterType();
    filter.setFilterType(EnumPropertyFilter.ALL);

    QueryConditionType criterio = new QueryConditionType();
    criterio.setOperator(EnumQueryOperator.LIKE);
    criterio.setPropertyName("codice");
    criterio.setValue("*312*");
    criteria.add(criterio);

    criterio = new QueryConditionType();
    criterio.setOperator(EnumQueryOperator.LIKE);
    criterio.setPropertyName("codiceAooProtocollante");
    criterio.setValue("*DB2000*");
    criteria.add(criterio);

    var queryResponse = client.getProvider().getOfficialBookService().query(repositoryId,
        principalId, target, filter, criteria, null, 2, 0);

    dump("queryResponse", queryResponse);
    for (var protocollo : queryResponse.getObjects()) {
      var mapped = ActaParser.actaPropertiesToEntity(client.getContesto(),
          protocollo.getProperties(), RegistrazioneClassificazioniActaDefaultImpl.class);
      dump("protocollo", mapped);

      var children = client.getProvider().getNavigationService().getChildren(repositoryId,
          ActaClientHelper.wrapId(mapped.getObjectIdClassificazione()), principalId, filter, 10, 0);

      var childFiltered = children.getObjects().stream().filter(entry -> entry.getProperties()
          .stream()
          .anyMatch(p -> p.getQueryName().getClassName().equals("DocumentoSemplicePropertiesType")
              && p.getQueryName().getPropertyName().equals("objectId") && p.getValue() != null
              && p.getValue().getContent() != null && !p.getValue().getContent().isEmpty()
              && !StringUtils.isBlank(p.getValue().getContent().get(0))))
          .map(raw -> ActaParser.actaPropertiesToEntity(client.getContesto(),
              protocollo.getProperties(), DocumentoSempliceActaDefaultImpl.class))
          .collect(Collectors.toList());

      dump("TEST", childFiltered);
    }
  }


  @Test
  public void testRicercaDocumenti() {

    String parolaChiave = "d2e874a5-83b5-11ea-b60d-fb29051e5afc*";

    var result = actaService.findDocumentiSempliciByParolaChiave(
        ObjectUtils.toJson(creaIdentitaUtente()), parolaChiave);

    for (DocumentoSemplice converted : result) {
      dump("T1 - documento", converted);
    }

  }


  @Test
  public void testContentRetrieve() throws Exception {

    String documentoSempliceId =
        "11c536df38cf3bde3af930c725c63cc930fa27c525cf27de3ccf26fe2cda30f5639c6c9931c8629f789266c86587649b30"
            + "cb78c8609f63873193619b6dcb63cb6c9330ce758a758a758a758a758a758a758a758a758a758a758a758a758a75"
            + "8a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a";

    var documentoFisico = actaService.findDocumentiFisiciByIdDocumentoSemplice(
        ObjectUtils.toJson(creaIdentitaUtente()), documentoSempliceId).get(0);

    dump("documentoFisico", documentoFisico);

    var contenuto = actaService.findContenutoPrimarioByIdDocumentoFisico(
        ObjectUtils.toJson(creaIdentitaUtente()), documentoFisico.getId());

    dump("T2 - contenuto", contenuto);

  }

  private InformazioniActaEnte getConfigurazioneActaEnteFittizio() {

    return InformazioniActaEnte.builder().withAppKey(TEST_APP_KEY)
        .withRepositoryName(TEST_REPO_NAME).build();
  }


  @Test
  public void testRicercaTramiteIndiceClassificazioneEstesa() {

    var indiceClassificazioneEsteso = "C.arc, RP201209.e, Regione Piemonte - Agg. 09/2012.ra, Tit01RPGiunta.t, 14.v, Modulistica personale/DB2000.sfa, DB2000.arm, 1/2021C/DB2000.frc, DB2000.arm";
    var result = actaService.findObjectIdStrutturaAggregativa(
        ObjectUtils.toJson(creaIdentitaUtente()),
        indiceClassificazioneEsteso);
    dump("TEST - objectID", result);
  }

  @Test
  public void ricercaTitolario() {
    var result =
        actaService.getTitolario(ObjectUtils.toJson(creaIdentitaUtente()), "Tit01RPGiunta");
    dump("TEST - objectID", result);
  }

  @Test
  public void ricercaAlberaturaRoot() {
    String filtro = "{\"page\": 0,\"size\":10}";
    var result =
        actaService.ricercaAlberaturaVociTitolario(ObjectUtils.toJson(creaIdentitaUtente()), "6", "", filtro);
    dump("TEST - objectID", result);
  }

  @Test
  public void ricercaAlberaturaLeaf() {
    String filtro = "{\"page\": 1,\"size\":2}";
    var result = actaService
        .ricercaAlberaturaVociTitolario(ObjectUtils.toJson(creaIdentitaUtente()), "6", "566", filtro);
    dump("TEST - objectID", result);
  }


  @Test
  public void ricercaDocumentiFisici() {
    ImportaDocumentiRequest body = new ImportaDocumentiRequest();
    body.setIdPratica(1L);
    List<ImportaDocumentoRequest> documenti = new ArrayList<>();
    ImportaDocumentoRequest documento = new ImportaDocumentoRequest();
    documento.setId(
        "11c536df38cf3bde3af930c725c63cc930fa27c525cf27de3ccf26fe2cda30f5619260c9379e6498789d66ce6187649b30cf78cb6ccb3687679d659d6ccf62c83699349a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a");
    List<ImportaDocumentoFisicoRequest> documentiFisici = new ArrayList<>();
    ImportaDocumentoFisicoRequest documentoFisico = new ImportaDocumentoFisicoRequest();
    documentoFisico.setId("11c536df38cf3bde3aec3cd93cc93afa27c525cf27de3ccf26fe2cda30f561936299369330c8789d66ce6187649b30cf78cb6ccb3687679d659d6ccf62c83699349a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a");
    documentoFisico.setCodiceTipoDocumento("codice 1");
    documentiFisici.add(documentoFisico);
    documento.setDocumentiFisici(documentiFisici);
    documenti.add(documento);
    body.setDocumenti(documenti);
    var docFisico = actaService.findDocumentiFisici(ObjectUtils.toJson(creaIdentitaUtente()), body);
    dump("TEST - documento", docFisico);
  }


  @Test
  public void ricercaDocumentiSemplici() {
    ImportaDocumentiRequest body = new ImportaDocumentiRequest();
    body.setIdPratica(1L);
    List<ImportaDocumentoRequest> documenti = new ArrayList<>();
    ImportaDocumentoRequest documento = new ImportaDocumentoRequest();
    documento.setId(
        "11c536df38cf3bde3af930c725c63cc930fa27c525cf27de3ccf26fe2cda30f5619260c9379e6498789d66ce6187649b30cf78cb6ccb3687679d659d6ccf62c83699349a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a");
    List<ImportaDocumentoFisicoRequest> documentiFisici = new ArrayList<>();
    ImportaDocumentoFisicoRequest documentoFisico = new ImportaDocumentoFisicoRequest();
    documentoFisico.setId("11c536df38cf3bde3aec3cd93cc93afa27c525cf27de3ccf26fe2cda30f561936299369330c8789d66ce6187649b30cf78cb6ccb3687679d659d6ccf62c83699349a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a");
    documentoFisico.setCodiceTipoDocumento("codice 1");
    documentiFisici.add(documentoFisico);
    documento.setDocumentiFisici(documentiFisici);
    documenti.add(documento);
    body.setDocumenti(documenti);
    var docSemplice =
        actaService.findDocumentiSemplici(ObjectUtils.toJson(creaIdentitaUtente()), body);
    dump("T1 - documento", docSemplice);
  }


  @Test
  public void ricercaRegistrazioni() {
    var filter = FILTRO_OGGETTO_LIKE;
    var registrazioni = actaService.findRegistrazioni(ObjectUtils.toJson(creaIdentitaUtente()), filter);
    dump("T1 - registrazioni", registrazioni);
  }

  @Test
  public void ricercaDocumentiSempliciPerProtocollo() {
    var filter = FILTRO_OGGETTO_LIKE;
    var docSemplici = actaService.findDocumentiSempliciPerProtocollo(ObjectUtils.toJson(creaIdentitaUtente()), filter);
    dump("T1 - documentiSempliciPerProtocollo", docSemplici);
  }

  @Test
  public void ricercaDocumentiSempliciPaginati() {
    var filter = FILTRO_OGGETTO_LIKE;
    var docSemplici =
        actaService.findDocumentiSemplici(ObjectUtils.toJson(creaIdentitaUtente()), filter);
    dump("T1 - ricercaDocumentiSempliciPaginati", docSemplici);
  }

  @Test
  public void ricercaProtocolloPerId() {
    var protocollo =
        actaService.findProtocolloById(ObjectUtils.toJson(creaIdentitaUtente()), "42");
    dump("TEST - ricercaProtocolloPerId", protocollo);
  }

  @Test
  public void ricercaClassificazioniByIdDocumentoSemplice() {
    String documentoSempliceId =
        "11c536df38cf3bde3af930c725c63cc930fa27c525cf27de3ccf26fe2cda30f5639c6c9931c8629f789266c86587649b30"
            + "cb78c8609f63873193619b6dcb63cb6c9330ce758a758a758a758a758a758a758a758a758a758a758a758a758a75"
            + "8a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a";
    var classificazioni = actaService
        .findClassificazioniByIdDocumentoSemplice(ObjectUtils.toJson(creaIdentitaUtente()), documentoSempliceId);
    dump("TEST - ricercaClassificazioniByIdDocumentoSemplice", classificazioni);
  }



  private IdentitaUtente creaIdentitaUtente() {
    IdentitaUtente identitaUtente = new IdentitaUtente();
    identitaUtente.setId(
        "05d83cc436c325cb39e331f5619866f5649c639f609d609d669c629b63f578996085669f7a926c8578996c8578996685789e6385669f7a99628578926685668578926485649c7a87649a6485789b67997a8764986685789f6d8a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a");
    identitaUtente.setIdentificativoAOO("240");
    identitaUtente.setIdentificativoNodo("836");
    identitaUtente.setIdentificativoStruttura("821");

    return identitaUtente;
  }

}
