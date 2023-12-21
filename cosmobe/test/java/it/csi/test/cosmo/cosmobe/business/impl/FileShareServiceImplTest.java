/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobe.business.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.business.service.FileShareService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.test.cosmo.cosmobe.testbed.config.CosmoUnitTest;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoUnitTest.class } )
public class FileShareServiceImplTest {



  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private ConfigurazioneService configurazioneService;


  @Test
  @Ignore("da provare solo in locale")
  public void uploadExcelWithValidation() throws IOException {

    Resource excelFile = new ClassPathResource("prova_excel_with_validation.xlsx");

    assertTrue(fileShareService.validateExcel(excelFile.getInputStream()));

    fileShareService.handleUploadExcel(excelFile.getInputStream(), "prova_excel_with_validation.xlsx");



    String excelDirectory =
        configurazioneService.requireConfig
        (ParametriApplicativo.FILE_SHARE_PATH).asString()+
        "/"+configurazioneService.requireConfig(ParametriApplicativo.UPLOAD_EXCEL_PATH).asString()+
        "/"+String.valueOf(LocalDate.now().getYear())+
        "/"+String.valueOf(LocalDate.now().getMonthValue())+
        "/"+String.valueOf(LocalDate.now().getDayOfMonth());

    File file = new File(excelDirectory+"/prova_excel_with_validation.xlsx");
    assertTrue(file.exists());


    Path path = FileSystems.getDefault().getPath(excelDirectory+"/prova_excel_with_validation.xlsx");
    Files.delete(path);


  }


  @Test
  @Ignore("da provare solo in locale")
  public void uploadExcelWithoutValidation() throws IOException {

    Resource excelFile = new ClassPathResource("prova_excel_without_validation.xlsx");

    assertFalse(fileShareService.validateExcel(excelFile.getInputStream()));

    fileShareService.handleUploadExcel(excelFile.getInputStream(), "prova_excel_without_validation.xlsx");


    String excelDirectory =
        configurazioneService.requireConfig
        (ParametriApplicativo.FILE_SHARE_PATH).asString()+
        "/"+configurazioneService.requireConfig(ParametriApplicativo.UPLOAD_EXCEL_PATH).asString()+
        "/"+String.valueOf(LocalDate.now().getYear())+
        "/"+String.valueOf(LocalDate.now().getMonthValue())+
        "/"+String.valueOf(LocalDate.now().getDayOfMonth());

    File file = new File(excelDirectory+"/prova_excel_without_validation.xlsx");
    assertTrue(file.exists());

    Path path = FileSystems.getDefault().getPath(excelDirectory+"/prova_excel_without_validation.xlsx");
    Files.delete(path);


  }

}
