/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmobusiness.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaApprovaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaVariabiliProcessoRequest;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class EsecuzioneMultiplaServiceImplTest {
  
  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;
  
  @Test
  public void postEsecuzioneMultiplaApprova() {
    EsecuzioneMultiplaApprovaRequest request = new EsecuzioneMultiplaApprovaRequest();
    request.setApprovazione(true);
    request.setMandareAvantiProcesso(true);
    esecuzioneMultiplaService.postEsecuzioneMultiplaApprova(request );
  }
  
  @Test(expected = BadRequestException.class)
  public void postEsecuzioneMultiplaApprovaNumMaxApprovazioni() {
    EsecuzioneMultiplaApprovaRequest request = new EsecuzioneMultiplaApprovaRequest();
    request.setApprovazione(true);
    request.setMandareAvantiProcesso(true);
    
    List<AttivitaEseguibileMassivamente> tasks = new ArrayList<>();
    AttivitaEseguibileMassivamente attivita1 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita2 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita3 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita4 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita5 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita6 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita7 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita8 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita9 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita10 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita11 = new AttivitaEseguibileMassivamente();
    tasks.add(attivita1);
    tasks.add(attivita2);
    tasks.add(attivita3);
    tasks.add(attivita4);
    tasks.add(attivita5);
    tasks.add(attivita6);
    tasks.add(attivita7);
    tasks.add(attivita8);
    tasks.add(attivita9);
    tasks.add(attivita10);
    tasks.add(attivita11);
    
    request.setTasks(tasks);
    esecuzioneMultiplaService.postEsecuzioneMultiplaApprova(request );
  }
  
  @Test
  public void postEsecuzioneMultiplaVariabiliProcesso() {
    EsecuzioneMultiplaVariabiliProcessoRequest request = new EsecuzioneMultiplaVariabiliProcessoRequest();
    request.setMandareAvantiProcesso(true);
    esecuzioneMultiplaService.postEsecuzioneMultiplaVariabiliProcesso(request );
  }
  
  @Test(expected = BadRequestException.class)
  public void postEsecuzioneMultiplaVariabiliProcessoNumMaxApprovazioni() {
    EsecuzioneMultiplaVariabiliProcessoRequest request = new EsecuzioneMultiplaVariabiliProcessoRequest();
    request.setMandareAvantiProcesso(true);
    
    List<AttivitaEseguibileMassivamente> tasks = new ArrayList<>();
    AttivitaEseguibileMassivamente attivita1 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita2 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita3 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita4 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita5 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita6 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita7 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita8 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita9 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita10 = new AttivitaEseguibileMassivamente();
    AttivitaEseguibileMassivamente attivita11 = new AttivitaEseguibileMassivamente();
    tasks.add(attivita1);
    tasks.add(attivita2);
    tasks.add(attivita3);
    tasks.add(attivita4);
    tasks.add(attivita5);
    tasks.add(attivita6);
    tasks.add(attivita7);
    tasks.add(attivita8);
    tasks.add(attivita9);
    tasks.add(attivita10);
    tasks.add(attivita11);
    
    request.setTasks(tasks);
    esecuzioneMultiplaService.postEsecuzioneMultiplaVariabiliProcesso(request );
  }
}