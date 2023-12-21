/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.fileshare.IFileShareManager;
import it.csi.cosmo.common.fileshare.exceptions.FileShareUploadExcelException;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.util.FilesUtils;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.business.service.FileShareService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerConstants;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

/**
 * Servizio per la gestione dei file condivisi
 *
 */
@Service
public class FileShareServiceImpl implements FileShareService, Monitorable, DisposableBean {

  private CosmoLogger logger = LoggerFactory.getLogger(LogCategory.FILESHARE_LOG_CATEGORY, "FileShareServiceImpl");

  private IFileShareManager fileShareManager;

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  @Autowired
  private ConfigurazioneService configurazioneService;

  private synchronized IFileShareManager getFileShareManager() {
    if (this.fileShareManager == null) {
      logger.info("destroy", "lazily initializing file share manager");
      this.fileShareManager = IFileShareManager.builder().withLoggingPrefix(LoggerConstants.ROOT_LOG_CATEGORY)
          .withRootPath(configurazioneService.requireConfig(ParametriApplicativo.FILE_SHARE_PATH).asString())
          .build();
    }

    return this.fileShareManager;
  }

  @Override
  public void cleanup() {
    getFileShareManager().cleanup();
  }

  @Override
  public FileUploadResult handleUpload(InputStream stream, String filename, String contentType,
      ClientInfoDTO currentClient) {

    return getFileShareManager().handleUpload(stream, filename, contentType, currentClient.getCodice());
  }

  @Override
  public ServiceStatusDTO checkStatus() {
    // check: esistenza della root

    return ServiceStatusDTO.of(() -> Files.exists(getFileShareManager().getRoot()), ServiceStatusEnum.WARNING)
        .withName("FileShare - disco condiviso").withDetail("shareLocation",
            configurazioneService.getConfig(ParametriApplicativo.FILE_SHARE_PATH).asString())
        .build();
  }

  @Override
  public void destroy() throws Exception {
    if (this.fileShareManager != null) {
      logger.info("destroy", "closing file share manager");
      this.fileShareManager.close();
    }
  }

  @Override
  public CreateUploadSessionResponse createUploadSession(CreateUploadSessionRequest request) {
    return getFileShareManager().createUploadSession(request);
  }

  @Override
  public void handPartUpload(String sessionUUID, Long partNumber, InputStream stream, ClientInfoDTO currentClient) {
    getFileShareManager().handPartUpload(sessionUUID, partNumber, stream, currentClient.getCodice());
  }

  @Override
  public CompleteUploadSessionResponse completeUploadSession(CompleteUploadSessionRequest request) {
    return getFileShareManager().completeUploadSession(request);
  }

  @Override
  public RetrievedContent get(String uuid) {
    return getFileShareManager().get(uuid);
  }

  @Override
  public void cancelUploadSession(String sessionUUID) {
    getFileShareManager().cancelUploadSession(sessionUUID);
  }

  @Override
  public boolean validateExcel(InputStream inputStream) {

    Workbook wb;
    try {
      wb = WorkbookFactory.create(inputStream);
    } catch (InvalidFormatException e) {
      logger.error("validateExcel", "Il formato del file non è valido");
      throw new FileShareUploadExcelException("Il formato del file non è valido", e);
    } catch (IOException e) {
      logger.error("validateExcel", "Errore nella lettura del file");
      throw new FileShareUploadExcelException("Errore nella lettura del file", e);
    }

    verifySheetAndSheetName(wb, "Configurazione", 1, true, true);
    verifySheetAndSheetName(wb, "Utenti", 2, true, false);
    verifySheetAndSheetName(wb, "Profili", 3, false, false);
    verifySheetAndSheetName(wb, "Gruppi", 4, false, false);
    boolean validated = validateSheetDue(wb);

    if (validated) {

      if(wb.getNumberOfSheets()>1)
        validateSheet(wb, 1, true);

      if(wb.getNumberOfSheets()>2)
        validateSheet(wb, 3, false);

      if(wb.getNumberOfSheets()>3)
        validateSheet(wb, 4, false);


    }

    return validated;

  }

  private void verifySheetAndSheetName(Workbook wb, String sheetName, int sheetNumber, boolean required,
      boolean caseSensitive) {

    if (required && (wb.getNumberOfSheets() < sheetNumber)) {
      logger.error("verifySheetAndSheetName", "Foglio " + sheetNumber + " obbligatorio");
      throw new FileShareUploadExcelException("Foglio " + sheetNumber + " obbligatorio");
    }

    if (wb.getNumberOfSheets() >= sheetNumber) {
      Sheet sheet = wb.getSheetAt(sheetNumber - 1);

      if (!caseSensitive
          && (StringUtils.isEmpty(sheet.getSheetName()) || !sheet.getSheetName().equalsIgnoreCase(sheetName))) {
        logger.error("verifySheetAndSheetName", "Nome foglio " + sheetNumber + " non valido");
        throw new FileShareUploadExcelException("Nome foglio " + sheetNumber + " non valido");

      }

      if (caseSensitive && (StringUtils.isEmpty(sheet.getSheetName()) || !sheet.getSheetName().equals(sheetName))) {
        logger.error("verifySheetAndSheetName", "Foglio " + sheetNumber + " obbligatorio");
        throw new FileShareUploadExcelException( "Foglio " + sheetName + " obbligatorio");
      }

    }
  }

  private void validateSheet(Workbook wb, int sheetNumber, boolean required) {

    logger.info("validateSheet", "Inizio validazione sheet " + sheetNumber);

    Sheet sheet = wb.getSheetAt(sheetNumber - 1);

    Iterator<Row> sheetRowsIterator = sheet.iterator();

    int rowNum = 0;
    boolean almostOneLine = false;
    while (sheetRowsIterator.hasNext()) {
      almostOneLine = true;
      rowNum++;
      Row sheetCurrentRow = sheetRowsIterator.next();

      if (!isRowEmpty(sheetCurrentRow)
          && StringUtils.isEmpty(getCellValue(sheetCurrentRow.getCell(0)))) {
        logger.error("validateSheet", "Foglio " + sheetNumber + ", Riga " + rowNum + ", Cella 1 obbligatoria");
        throw new FileShareUploadExcelException(
            "Foglio " + sheetNumber + ", Riga " + rowNum + ", Cella 1 obbligatoria");
      }

    }

    if (required && !almostOneLine) {
      logger.error("validateSheet", "Foglio " + sheetNumber + ", Riga " + (rowNum + 1) + ", Cella 1 obbligatoria");
      throw new FileShareUploadExcelException(
          "Foglio " + sheetNumber + ", Riga " + (rowNum + 1) + ", Cella 1 obbligatoria");

    }

    logger.info("validateSheet", "Fine validazione sheet " + sheetNumber);

  }

  private boolean validateSheetDue(Workbook wb) {

    logger.info("validateSheetDue", "Inizio validazione sheet 2");

    if (wb.getNumberOfSheets() < 2) {
      logger.error("validateSheetDue", "Foglio 2 obbligatorio");
      throw new FileShareUploadExcelException("Foglio 2 obbligatorio");
    }

    Sheet sheet = wb.getSheetAt(1);

    if (sheet.getLastRowNum() >= configurazioneService.requireConfig(ParametriApplicativo.UPLOAD_EXCEL_PATH_MAXROWS)
        .asInteger())
      return false;

    if (StringUtils.isEmpty(sheet.getSheetName()) || !sheet.getSheetName().equalsIgnoreCase("Utenti")) {
      logger.error("validateSheetDue", "Nome foglio 2 non valido");
      throw new FileShareUploadExcelException("Nome foglio 2 non valido");
    }

    Iterator<Row> sheetDueRowsIterator = sheet.iterator();
    int rowNum = 0;
    boolean almostOneLine = false;
    while (sheetDueRowsIterator.hasNext()) {
      almostOneLine = true;
      Row sheetDueCurrentRow = sheetDueRowsIterator.next();
      if (!isRowEmpty(sheetDueCurrentRow)) {
        for (int i = 0; i <= 6; i++) {

          Cell currentCell = sheetDueCurrentRow.getCell(i);
          if ((i == 0 || i == 1 || i == 2 || i == 4)
              && (currentCell == null || StringUtils.isEmpty(getCellValue(currentCell)))) {
            logger.error("validateSheetDue", "Foglio 2, Riga " + (rowNum + 1) + ", Cella " + (i + 1) + " obbligatoria");
            throw new FileShareUploadExcelException(
                "Foglio 2, Riga " + (rowNum + 1) + ", Cella " + (i + 1) + " obbligatoria");
          }

        }
      }
      rowNum++;

    }

    if (!almostOneLine) {
      logger.error("validateSheetDue", "Foglio 2 vuoto");
      throw new FileShareUploadExcelException("Foglio 2 vuoto");
    }

    logger.info("validateSheetDue", "Fine validazione sheet 2");
    return true;

  }

  private String getCellValue(Cell cell) {

    int cellType = cell.getCellType();

    switch (cellType) {
      case Cell.CELL_TYPE_NUMERIC:
        double doubleVal = cell.getNumericCellValue();
        return String.valueOf(doubleVal);
      case Cell.CELL_TYPE_STRING:
        return cell.getStringCellValue();
      case Cell.CELL_TYPE_ERROR:
        return String.valueOf(cell.getErrorCellValue());
      case Cell.CELL_TYPE_BLANK:
        return "";
      case Cell.CELL_TYPE_FORMULA:
        return cell.getCellFormula();
      case Cell.CELL_TYPE_BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
    }
    return "";
  }

  private static boolean isRowEmpty(Row row) {
    boolean isEmpty = true;
    DataFormatter dataFormatter = new DataFormatter();

    if (row != null) {
      for (Cell cell : row) {
        if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
          isEmpty = false;
          break;
        }
      }
    }

    return isEmpty;
  }

  @Override
  public void handleUploadExcel(InputStream inputStream, String fileName) {

    Path workingFolder = this
        .getExcelPath(configurazioneService.requireConfig(ParametriApplicativo.FILE_SHARE_PATH).asString());

    try {
      this.saveFile(inputStream, fileName, workingFolder);
    } catch (IOException e) {
      throw new FileShareUploadExcelException("Error saving excel file", e);

    }
  }

  public Path getExcelPath(String rootPath) {
    Path p = Paths.get(rootPath);
    p = p.normalize();
    if (!Files.exists(p)) {
      throw new FileShareUploadExcelException("FileShare root path is missing [" + p.toString() + "]");
    }

    Path utenze = getOrCreate(p,
        configurazioneService.requireConfig(ParametriApplicativo.UPLOAD_EXCEL_PATH).asString());

    String year = String.valueOf(LocalDate.now().getYear());
    String month = String.valueOf(LocalDate.now().getMonthValue());
    String day = String.valueOf(LocalDate.now().getDayOfMonth());

    Path tmp1 = getOrCreate(utenze, year);
    Path tmp2 = getOrCreate(tmp1, month);
    Path excelPath = getOrCreate(tmp2, day);

    return excelPath;

  }

  private Path getOrCreate(Path root, String subpath) {
    try {
      return FilesUtils.getOrCreate(root, subpath);
    } catch (Exception e) {
      throw new FileShareUploadExcelException("Error provisioning folder [" + subpath + "]: " + e.getMessage(), e);
    }
  }

  private void saveFile(InputStream inputStream, String fileName, Path target) throws IOException {

    File file = new File(target.toString(), fileName);

    if (file.exists())
      throw new FileShareUploadExcelException("File " + fileName + " already exists");

    try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
      int read;
      byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    }

  }

}
