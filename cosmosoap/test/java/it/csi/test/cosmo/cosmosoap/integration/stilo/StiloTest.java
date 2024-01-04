/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.stilo;

import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.cosmosoap.business.service.StiloService;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.AddUdOutput;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.StiloAllegato;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.UpdUdOutput;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.NewUD;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.UDDaAgg;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class StiloTest extends ParentUnitTest {

  @Autowired
  StiloService stiloService;


  @Ignore
  @Test
  public void testAddUdSenzaDoc() throws JAXBException {

    InputStream stream =
        StiloTest.class.getResourceAsStream("/stilo/request-inserimento-senza-doc.xml");

    JAXBContext jaxbContext = JAXBContext.newInstance(NewUD.class);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    NewUD newUD = (NewUD) jaxbUnmarshaller.unmarshal(stream);

    List<StiloAllegato> allegati = new ArrayList<>();

    AddUdOutput outputUD = this.stiloService.addUd(newUD, allegati);

    assertNotNull(outputUD);

    assertNotNull(outputUD.getOutputUD());
    assertNotNull(outputUD.getOutputUD().getIdUD());
  }


  @Test
  public void testAddUdConDoc() throws JAXBException {

    InputStream stream =
        StiloTest.class.getResourceAsStream("/stilo/request-inserimento-con-doc.xml");

    JAXBContext jaxbContext = JAXBContext.newInstance(NewUD.class);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    NewUD newUD = (NewUD) jaxbUnmarshaller.unmarshal(stream);

    List<StiloAllegato> allegati = getStiloAllegati();

    AddUdOutput outputUD = this.stiloService.addUd(newUD, allegati);

    assertNotNull(outputUD);

    assertNotNull(outputUD.getOutputUD());
    assertNotNull(outputUD.getOutputUD().getIdUD());
  }

  @Test
  public void testUpdUd() throws JAXBException {
    InputStream stream =
        StiloTest.class.getResourceAsStream("/stilo/request-addallegato.xml");

    JAXBContext jaxbContext = JAXBContext.newInstance(UDDaAgg.class);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    UDDaAgg udDaAgg = (UDDaAgg) jaxbUnmarshaller.unmarshal(stream);

    List<StiloAllegato> allegati = this.getStiloAllegati();

    UpdUdOutput updUdOutput = this.stiloService.updUd(udDaAgg, allegati);

    assertNotNull(updUdOutput);

    assertNotNull(updUdOutput.getOutputUD());
    assertNotNull(updUdOutput.getOutputUD().getIdUD());

  }

  private List<StiloAllegato> getStiloAllegati() {

    /*
     * List<StiloAllegato> allegati = new ArrayList<>(); StiloAllegato allegatoPng = new
     * StiloAllegato(); allegatoPng.setContentId("allegato1");
     * allegatoPng.setContentType("image/jpeg");
     * allegatoPng.setContent(StiloTest.class.getResourceAsStream("/stilo/NewUD_p1.png"));
     * allegati.add(allegatoPng);
     */
    List<StiloAllegato> allegati = new ArrayList<>();
    StiloAllegato allegatoPdf = new StiloAllegato();
    allegatoPdf.setContentId("allegato2");
    allegatoPdf.setContentType("application/pdf");
    allegatoPdf.setContent(StiloTest.class
        .getResourceAsStream("/stilo/CATALOGO_SERVIZI_Specifiche_Tecniche_WS_v3.0.pdf"));
    allegati.add(allegatoPdf);



    return allegati;
  }

}
