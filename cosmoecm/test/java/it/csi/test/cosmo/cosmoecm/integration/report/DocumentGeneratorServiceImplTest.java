/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmoecm.integration.report;

import static org.junit.Assert.assertTrue;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmoecm.business.service.DocumentGeneratorService;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperField;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperParameter;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperReportConfig;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperResource;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;
import it.csi.cosmo.cosmoecm.integration.jasper.model.JasperResourceType;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestDB;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestDB.class})
@Transactional
public class DocumentGeneratorServiceImplTest extends ParentIntegrationTest {

  private static final boolean OPEN_RESULTS = false;

  protected static CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "DocumentGeneratorServiceImplTest");

  @Autowired
  private DocumentGeneratorService service;

  @Test
  public void serviceShouldExist() {
    Assert.notNull(service, "service should exist");
  }

  @Ignore("Da lanciare manualmente")
  @Test
  public void testSubreportFromCode() throws IOException {
    ContestoCreazioneDocumento contesto = null;

    final var codiceTemplate = "jasper-prova-man-000";

    Map<String, Object> map = new HashMap<>();
    map.put("parameter1", "parameterValue1");
    map.put("parameter2", "parameterValue2");
    map.put("image001",
        service.loadImage(null, codiceTemplate, "jasper-resource-001.png", contesto));
    map.put("subreportAttivita",
        service.loadSubreport(null, "jasper-prova-man-000-subreport-000", contesto));

    Collection<AttivitaFruitore> dataBeans = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      AttivitaFruitore att1 = new AttivitaFruitore();
      att1.setNome("Nome attivita " + i);
      att1.setDescrizione("Descrizione attivita " + i);
      dataBeans.add(att1);
    }

    map.put("attivita", dataBeans);

    byte[] pdf = service.render(codiceTemplate, null, map, ExportFormat.PDF, contesto);
    assertTrue(pdf.length > 0);
    log.info("testDocumentGeneratorServiceImpl", "generato pdf: " + pdf.length + " bytes");

    // decomment to see the file:
    if (OPEN_RESULTS) {
      File file = new java.io.File("C:\\UPAP\\test.pdf");
      FileUtils.writeByteArrayToFile(file, pdf);
      Desktop.getDesktop().open(file);
    }
  }

  @SuppressWarnings("unused")
  @Ignore("Da lanciare manualmente")
  @Test
  public void testDocumentGeneratorServiceImpl() throws IOException {
    ContestoCreazioneDocumento contesto = null;

    Map<String, Object> map = new HashMap<>();
    map.put("parameter1", "parameterValue1");
    map.put("parameter2", "parameterValue2");
    map.put("image001",
        service.loadImage(null, "jasper-junit-001", "jasper-resource-001.png", contesto));

    Collection<PojoForJasper> dataBeans =
        Arrays.asList(new PojoForJasper("key1", "Value 1"), new PojoForJasper("key2", "Value 2"),
            new PojoForJasper("key3", "Value 3"), new PojoForJasper("key4", "Value 4"));

    byte[] pdf = service.render("jasper-junit-001", dataBeans, map, ExportFormat.PDF, contesto);
    assertTrue(pdf.length > 0);
    log.info("testDocumentGeneratorServiceImpl", "generato pdf: " + pdf.length + " bytes");

    byte[] docx = service.render("jasper-junit-001", dataBeans, map, ExportFormat.DOCX, contesto);
    assertTrue(docx.length > 0);
    log.info("testDocumentGeneratorServiceImpl", "generato docx: " + docx.length + " bytes");

    byte[] odt = service.render("jasper-junit-001", dataBeans, map, ExportFormat.ODT, contesto);
    assertTrue(odt.length > 0);
    log.info("testDocumentGeneratorServiceImpl", "generato odt: " + odt.length + " bytes");

    // decomment to see the file:
    if (OPEN_RESULTS) {
      File file = new java.io.File("C:\\UPAP\\test.pdf");
      FileUtils.writeByteArrayToFile(file, pdf);
      Desktop.getDesktop().open(file);
    }
  }

  @Ignore("Da lanciare manualmente")
  @Test
  public void testDocumentGeneratorByAnnotation() throws IOException {

    ContestoCreazioneDocumento contesto = null;

    ObjectMapper mapper = new ObjectMapper();

    JasperTestReport reportPojo = new JasperTestReport();
    reportPojo.setPojos(
        Arrays.asList(new PojoForJasper("key1", "Value 1"), new PojoForJasper("key2", "Value 2"),
            new PojoForJasper("key3", "Value 3"), new PojoForJasper("key4", "Value 4")));

    System.out.println(mapper.writeValueAsString(reportPojo));

    byte[] pdf = service.render(reportPojo, contesto);
    assertTrue(pdf.length > 0);
    log.info("testDocumentGeneratorByAnnotation", "generato pdf: " + pdf.length + " bytes");

    // decomment to see the file:

    if (OPEN_RESULTS) {
      File file = new java.io.File("C:\\UPAP\\test.pdf");
      FileUtils.writeByteArrayToFile(file, pdf);
      Desktop.getDesktop().open(file);
    }
  }

  @JasperReportConfig(value = "jasper-junit-002",
      resources = {
          @JasperResource(key = "image001", value = "jasper-resource-001.png",
              type = JasperResourceType.IMAGE),
          @JasperResource(key = "image002", value = "jasper-resource-001.png",
              type = JasperResourceType.IMAGE)})
  public static class JasperTestReport {

    @JasperParameter("parameter1")
    private String paramOne = "param 1";

    @JasperParameter
    private String parameter2 = "param 2";

    @JasperField("data")
    private List<PojoForJasper> pojos;

    public String getParamOne() {
      return paramOne;
    }

    public void setParamOne(String paramOne) {
      this.paramOne = paramOne;
    }

    public String getParameter2() {
      return parameter2;
    }

    public void setParameter2(String parameter2) {
      this.parameter2 = parameter2;
    }

    public List<PojoForJasper> getPojos() {
      return pojos;
    }

    public void setPojos(List<PojoForJasper> pojos) {
      this.pojos = pojos;
    }

  }

  @JasperReportConfig(value = "jasper-junit-001")
  public static class PojoForJasper {

    @JasperField
    private String name;

    private String value;

    @JasperParameter
    private String parameter3 = "param 3";

    public PojoForJasper(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }

    public String getParameter3() {
      return parameter3;
    }

    public void setParameter3(String parameter3) {
      this.parameter3 = parameter3;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

}
