/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
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
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.FilesUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmo.business.service.ClusterService;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.FileShareService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.dto.EsitoEnum;
import it.csi.cosmo.cosmo.dto.rest.FileDocumentiZipUnzipRequest;
import it.csi.cosmo.cosmo.dto.rest.InfoFile;
import it.csi.cosmo.cosmo.integration.rest.CosmoEcmFormatiFileClient;
import it.csi.cosmo.cosmo.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmo.security.SecurityUtils;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerConstants;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;

/**
 * Servizio per la gestione dei file condivisi
 *
 */
@Service
public class FileShareServiceImpl implements FileShareService, Monitorable, DisposableBean {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.FILESHARE_LOG_CATEGORY, "FileShareServiceImpl");

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private static final int MAX_DAYS_KEEP_PRATICHE = 30;

  private IFileShareManager fileShareManager;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoEcmFormatiFileClient cosmoEcmFormatiFileClient;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheFeignClient;

  @Autowired
  private ClusterService clusterService;

  private synchronized IFileShareManager getFileShareManager() {
    if (this.fileShareManager == null) {
      logger.info("destroy", "lazily initializing file share manager");
      this.fileShareManager = IFileShareManager.builder()
          .withLoggingPrefix(LoggerConstants.ROOT_LOG_CATEGORY)
          .withRootPath(
              configurazioneService.requireConfig(ParametriApplicativo.FILE_SHARE_PATH).asString())
          .build();
    }

    return this.fileShareManager;
  }

  @Scheduled(cron = "0 0 4 * * ?")
  public void at4am() {
    String method = "at4am";
    if (clusterService == null || !clusterService.isMaster()) {
      logger.debug(method, "skipping scheduled operation because this is not the master instance");
      return;
    }

    logger.info(method, "executing scheduled operation for 4 am");
    try {
      getFileShareManager().cleanup();
    } catch (Throwable t) { // NOSONAR
      logger.error(method, "error in scheduled operation: " + t.getMessage(), t);
    }

    try {
      cleanPratichePath();
    } catch (Throwable t) { // NOSONAR
      logger.error(method, "error while cleaining Pratiche path: " + t.getMessage(), t);
    }
  }

  @Override
  public void cleanup() {
    getFileShareManager().cleanup();
  }

  @Override
  public FileUploadResult handleUpload(InputStream stream, String filename, String contentType,
      UserInfoDTO currentUser) {

    return getFileShareManager().handleUpload(stream, filename, contentType,
        currentUser.getCodiceFiscale());
  }

  @Override
  public ServiceStatusDTO checkStatus() {
    // check: esistenza della root

    return ServiceStatusDTO
        .of(() -> Files.exists(getFileShareManager().getRoot()), ServiceStatusEnum.WARNING)
        .withName("FileShare - disco condiviso")
        .withDetail("shareLocation",
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
  public void handPartUpload(String sessionUUID, Long partNumber, InputStream stream,
      UserInfoDTO currentUser) {
    getFileShareManager().handPartUpload(sessionUUID, partNumber, stream,
        currentUser.getCodiceFiscale());
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
    } catch (InvalidFormatException | POIXMLException | IllegalArgumentException e) {
      logger.error("validateExcel", "Il formato del file non e' valido", e);
      throw new FileShareUploadExcelException("Il formato del file non e' valido", e);
    } catch (IOException e) {
      logger.error("validateExcel", "Errore nella lettura del file", e);
      throw new FileShareUploadExcelException("Errore nella lettura del file", e);
    }



    return validateSheet(wb);
  }

  private boolean validateSheet(Workbook wb) {

    final String methodName = "validateSheet";

    logger.info(methodName, "Inizio validazione sheet ");

    Sheet sheet = wb.getSheetAt(0);

    if (sheet == null) {
      logger.error(methodName, "Foglio vuoto");
      throw new FileShareUploadExcelException("Foglio vuoto");
    }

    Row firstRow = sheet.getRow(0);

    if (isRowEmpty(firstRow)) {
      throw new BadRequestException("Prima riga vuota");
    }

    Integer maxRows = getUploadMaxRowsConfigParam();


    if (maxRows == null || (maxRows + 1) <= sheet.getLastRowNum()) {
      throw new BadRequestException(
          "Il file excel contiene un numero di pratiche superiore a quello massimo consentito"
              + (maxRows != null ? ": " + maxRows : ""));
    }

    validaPrimaRigaPratiche(firstRow);

    Integer validaMaxRows = getValidaMaxRowsConfigParam();

    if (validaMaxRows == null || (validaMaxRows + 1) <= sheet.getLastRowNum()) {
      return false;
    }

    boolean almostOneLine = false;


    for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {


      Row sheetCurrentRow = sheet.getRow(rowNum);

      if (!isRowEmpty(sheetCurrentRow)) {
        almostOneLine = true;
        validatePraticaExcelRow(sheetCurrentRow);
      }

    }

    if (!almostOneLine) {
      logger.error(methodName, "Foglio vuoto");
      throw new FileShareUploadExcelException("Foglio vuoto");

    }

    logger.info(methodName, "Fine validazione sheet");
    return true;
  }

  private void validaPrimaRigaPratiche(Row currentRow) {



    if (currentRow.getCell(0) == null
        || !currentRow.getCell(0).getStringCellValue().trim().contains("CF INITIATOR")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 1 deve essere uguale a CF INITIATOR");
    }

    if (currentRow.getCell(1) == null
        || !currentRow.getCell(1).getStringCellValue().trim().contains("OGGETTO PRATICA")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 2 deve essere uguale a OGGETTO PRATICA");
    }

    if (currentRow.getCell(2) == null
        || !currentRow.getCell(2).getStringCellValue().trim().contains("TIPOLOGIA PRATICA")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 3 deve essere uguale a TIPOLOGIA PRATICA");
    }

    if (currentRow.getCell(3) == null
        || !currentRow.getCell(3).getStringCellValue().trim().contains("IDENTIFICATIVO PRATICA")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 4 deve essere uguale a IDENTIFICATIVO PRATICA");
    }

    if (currentRow.getCell(4) == null
        || !currentRow.getCell(4).getStringCellValue().trim().contains("RIASSUNTO")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 5 deve essere uguale a RIASSUNTO (Testo o MD)");
    }

    if (currentRow.getCell(5) == null
        || !currentRow.getCell(5).getStringCellValue().trim().contains("METADATI PRATICA")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 6 deve essere uguale a METADATI PRATICA (Json)");
    }

    if (currentRow.getCell(6) == null
        || !currentRow.getCell(6).getStringCellValue().trim().contains("TAGS")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 7 deve essere uguale a TAGS (Json)");
    }

    if (currentRow.getCell(7) == null
        || !currentRow.getCell(7).getStringCellValue().trim().contains("FILE DOCUMENTI")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 8 deve essere uguale a FILE DOCUMENTI");
    }
  }

  private Integer getValidaMaxRowsConfigParam() {
    Integer maxRows = null;
    try {

      maxRows = Integer.valueOf(configurazioneService
          .requireConfigFromAuthorization(ParametriApplicativo.UPLOAD_PRATICHE_EXCEL_VALIDA_MAXROWS)
          .getValue());

    } catch (Exception e) {

      maxRows = configurazioneService
          .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_EXCEL_VALIDA_MAXROWS).asInteger();

    }

    return maxRows;
  }

  private Integer getUploadMaxRowsConfigParam() {
    Integer maxRows = null;
    try {

      maxRows = Integer.valueOf(configurazioneService
          .requireConfigFromAuthorization(ParametriApplicativo.UPLOAD_PRATICHE_EXCEL_MAXROWS)
          .getValue());

    } catch (Exception e) {

      maxRows = configurazioneService
          .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_EXCEL_MAXROWS)
          .asInteger();

    }

    return maxRows;
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



  @Override
  public String handleUploadExcel(InputStream inputStream, String fileName) {

    String subPath = formatFolderName(fileName);
    Path workingFolder = this.getExcelPath(configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString(), subPath);

    try {
      this.saveFile(inputStream, fileName, workingFolder);
    } catch (IOException e) {
      throw new FileShareUploadExcelException("Error salvataggio file excel", e);

    }


    return subPath;
  }



  /*
   * Il path creato sarà rootPath/anno/mese/giorno/nomeFile_idUtente_idEnte
   */
  private Path getExcelPath(String rootPath, String subPath) {
    Path p = Paths.get(rootPath);
    p = p.normalize();
    if (!Files.exists(p)) {
      throw new FileShareUploadExcelException(
          "FileShare root path is missing [" + p.toString() + "]");
    }


    Path excelPath = getOrCreate(p, subPath);


    return excelPath;

  }

  private String formatFolderName(String fileName) {
    int dot = fileName.lastIndexOf(".");

    if (dot > -1) {
      fileName = fileName.substring(0, dot);

      if (SecurityUtils.getUtenteCorrente() != null
          && SecurityUtils.getUtenteCorrente().getEnte() != null) {

        Long idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();
        Long idUtente = SecurityUtils.getUtenteCorrente().getId();

        fileName = fileName.concat("_" + idUtente.toString() + "_" + idEnte.toString());
      }
    }



    String year = String.valueOf(LocalDate.now().getYear());
    String month =
        LocalDate.now().getMonthValue() < 10 ? "0" + String.valueOf(LocalDate.now().getMonthValue())
        : String.valueOf(LocalDate.now().getMonthValue());
    String day =
        LocalDate.now().getDayOfMonth() < 10 ? "0" + String.valueOf(LocalDate.now().getDayOfMonth())
        : String.valueOf(LocalDate.now().getDayOfMonth());

    return Paths.get(year, month, day, fileName).toString();

  }

  private Path getOrCreate(Path root, String subpath) {
    try {
      return FilesUtils.getOrCreate(root, subpath);
    } catch (Exception e) {
      throw new FileShareUploadExcelException(
          "Error provisioning folder [" + subpath + "]: " + e.getMessage(), e);
    }
  }

  private void saveFile(InputStream inputStream, String fileName, Path target) throws IOException {

    File file = new File(target.toString(), fileName);

    if (file.exists())
      throw new FileShareUploadExcelException("Il file " + fileName + " è già stato caricato");

    try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
      int read;
      byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    }

  }

  @Override
  public void verifyFileDocumentiZipUploadSession(
      it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionRequest body) {

    Long maxSize = configurazioneService
        .requireConfigFromAuthorization(ParametriApplicativo.UPLOADSESSION_DOCUMENTIZIP_MAX_SIZE)
        .asLong();

    if (body.getSize() > maxSize)
      throw new BadRequestException("Dimensione massima (" + maxSize + " byte) file zip superata");


    int page = 0;
    int size = 20;
    boolean searchFinished = false;
    boolean mimeTypeFound = false;
    while (!searchFinished) {
      String filterFormatiFile =
          "{\"filter\":{\"descrizione\":{\"eq\":\"Archivio ZIP\"}}, \"page\":" + page + ",\"size\":"
              + size + "}";
      FormatoFileResponse formatoFileResponse =
          this.cosmoEcmFormatiFileClient.getFormatiFile(filterFormatiFile);


      if (formatoFileResponse.getFormatiFile().stream()
          .anyMatch(temp -> temp.getMimeType().equalsIgnoreCase(body.getMimeType()))) {
        searchFinished = true;
        mimeTypeFound = true;

      }

      if (formatoFileResponse.getPageInfo().getTotalPages() == page)
        searchFinished = true;

      else
        page++;

    }

    if (!mimeTypeFound)
      throw new BadRequestException(body.getMimeType() + " non valido");



  }

  @Override
  public EsitoEnum manageZipFile(String sessionUUID, String folderName) {

    EsitoEnum esito = EsitoEnum.NON_SALVATO;

    RetrievedContent zip = this.get(sessionUUID);
    Path tempFilePath;
    Path folderPath;
    Path tempFolderPath;
    try {
      Path rootPath = Paths.get(configurazioneService
          .requireConfigFromAuthorization(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH)
          .asString());

      LocalDate currentDate = LocalDate.now();

      folderPath = getOrCreate(rootPath, folderName);


      if (Paths.get(folderPath.toString(), zip.getFilename()).toFile().exists())
        throw new BadRequestException(
            zip.getFilename() + " già salvato nella directory " + folderPath.toString());

      tempFolderPath = Files.createTempDirectory(folderPath, "tmp");
      tempFilePath = Files.createTempFile(tempFolderPath, "tmp", ".zip");
      FileUtils.copyInputStreamToFile(zip.getContentStream(), tempFilePath.toFile());
    } catch (IOException e) {
      throw new InternalServerException("Error creating directory " + folderName, e);
    }

    try (ZipFile zipFile = new ZipFile(tempFilePath.toFile())) {

      boolean excelDocumentiDaCaricareFound = false;
      Enumeration<? extends ZipEntry> entries = zipFile.entries();


      while (entries.hasMoreElements() && !excelDocumentiDaCaricareFound) {

        ZipEntry zipEntry = entries.nextElement();

        if (zipEntry.isDirectory()) {
          throw new BadRequestException("Lo zip deve contenere solo singoli file");
        } else {
          String fileNameWithExtestion = zipEntry.getName();
          String fileNameWithoutExtestion = FilenameUtils.removeExtension(fileNameWithExtestion);
          if (fileNameWithoutExtestion.equals("documentiDaCaricare")
              || fileNameWithoutExtestion.equals("documenti_da_caricare")) {
            TikaInputStream tikaIS = TikaInputStream.get(zipFile.getInputStream(zipEntry));
            var tikaDetector = new Tika().detect(tikaIS);
            if (tikaDetector.equalsIgnoreCase("application/vnd.ms-excel")
                || tikaDetector.equalsIgnoreCase(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
              excelDocumentiDaCaricareFound = true;
              esito = validateExcelDocumentiDaCaricare(zipFile.getInputStream(zipEntry));
            }
          }

        }
      }
      if (!excelDocumentiDaCaricareFound)
        throw new BadRequestException(
            "L'archivio zip deve contenere un file excel con nome documentiDaCaricare oppure documenti_da_caricare");


    } catch (ZipException e) {
      throw new InternalServerException("Error reading zip stream", e);
    } catch (IOException e1) {
      throw new InternalServerException("Error reading zip stream", e1);
    } finally {
      try {
        if (!esito.equals(EsitoEnum.NON_SALVATO))
          Files.move(tempFilePath, Paths.get(folderPath.toString(), zip.getFilename()));
        else
          Files.deleteIfExists(tempFilePath);

        Files.deleteIfExists(tempFolderPath);
      } catch (IOException e) {
        logger.error("postFileDocumentiZipUploadSessionComplete", e.getMessage());
      }
    }
    return esito;
  }

  private EsitoEnum validateExcelDocumentiDaCaricare(InputStream inputStream) {

    Workbook wb = null;
    try {
      wb = WorkbookFactory.create(inputStream);
    } catch (InvalidFormatException e) {
      throw new FileShareUploadExcelException("Il formato del file non e' valido", e);
    } catch (IOException e) {
      throw new FileShareUploadExcelException("Errore nella lettura del file", e);
    }

    Sheet sheet = wb.getSheetAt(0);

    Row firstRow = sheet.getRow(0);

    if (isRowEmpty(firstRow)) {
      throw new BadRequestException("Prima riga vuota");
    }

    validaPrimaRigaDocumenti(firstRow);


    if (sheet.getLastRowNum() > configurazioneService.requireConfigFromAuthorization(
        ParametriApplicativo.UPLOADSESSION_DOCUMENTIZIP_EXCEL_MAX_ROWS).asInteger())
      return EsitoEnum.SENZA_VALIDAZIONE;



    boolean almostOneLine = false;
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {

      Row currentRow = sheet.getRow(i);
      if (!this.isRowEmpty(currentRow)) {
        validateExcelRow(currentRow);
        almostOneLine = true;

      }


    }

    if (sheet.getLastRowNum() < 1 || !almostOneLine)
      throw new BadRequestException("Il file excel deve contenere almeno una riga");

    return EsitoEnum.CON_VALIDAZIONE;


  }

  private void validateExcelRow(Row currentRow) {

    int rowNumber = currentRow.getRowNum();

    String nomeFile =
        currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue().trim() : null;
    String codiceTipo =
        currentRow.getCell(1) != null ? currentRow.getCell(1).getStringCellValue().trim() : null;
    String identificativoPratica =
        currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue().trim() : null;

    if (StringUtils.isEmpty(nomeFile))
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " nomeFile obbligatorio");

    if (StringUtils.isEmpty(codiceTipo))
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " codiceTipo obbligatorio");

    if (StringUtils.isEmpty(identificativoPratica))
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " identificativoPratica obbligatorio");


  }

  private void validatePraticaExcelRow(Row currentRow) {

    int rowNumber = currentRow.getRowNum();


    String cfInitiator =
        currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue().trim() : null;
    String oggettoPratica =
        currentRow.getCell(1) != null ? currentRow.getCell(1).getStringCellValue().trim() : null;
    String tipologiaPratica =
        currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue().trim() : null;
    String identificativoPratica =
        currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim() : null;


    if (StringUtils.isEmpty(cfInitiator)) {
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " cfInitiator obbligatorio");
    }

    if (StringUtils.isEmpty(oggettoPratica)) {
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " oggetto pratica obbligatorio");
    }

    if (StringUtils.isEmpty(tipologiaPratica)) {
      throw new BadRequestException(
          "File excel non valido, riga " + (rowNumber + 1) + " codice tipo pratica obbligatorio");
    }


    if (StringUtils.isEmpty(identificativoPratica)) {
      throw new BadRequestException("File excel non valido, riga " + (rowNumber + 1)
          + " identificativo pratica obbligatorio");
    }

  }

  private void validaPrimaRigaDocumenti(Row currentRow) {



    if (currentRow.getCell(0) == null
        || !currentRow.getCell(0).getStringCellValue().trim().contains("NOME FILE")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 1 deve essere uguale a NOME FILE");
    }

    if (currentRow.getCell(1) == null
        || !currentRow.getCell(1).getStringCellValue().trim().contains("CODICE TIPO DOCUMENTO")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 2 deve essere uguale a CODICE TIPO DOCUMENTO");
    }

    if (currentRow.getCell(2) == null
        || !currentRow.getCell(2).getStringCellValue().trim().contains("IDENTIFICATIVO PRATICA")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 3 deve essere uguale a IDENTIFICATIVO PRATICA");
    }

    if (currentRow.getCell(3) == null
        || !currentRow.getCell(3).getStringCellValue().trim().contains("NOME FILE PADRE")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 4 deve essere uguale a NOME FILE PADRE");
    }

    if (currentRow.getCell(4) == null
        || !currentRow.getCell(4).getStringCellValue().trim().contains("TITOLO")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 5 deve essere uguale a TITOLO");
    }

    if (currentRow.getCell(5) == null
        || !currentRow.getCell(5).getStringCellValue().trim().contains("DESCRIZIONE")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 6 deve essere uguale a DESCRIZIONE");
    }

    if (currentRow.getCell(6) == null
        || !currentRow.getCell(6).getStringCellValue().trim().contains("AUTORE")) {
      throw new FileShareUploadExcelException(
          "Riga iniziale: cella 7 deve essere uguale a AUTORE");
    }
  }

  private boolean isRowEmpty(Row row) {
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
  public FileUploadResult unzipFile(FileDocumentiZipUnzipRequest body) {

    ValidationUtils.require("pathFile", body.getPathFile());
    ValidationUtils.require("nomeFile", body.getNomeFile());
    ValidationUtils.require("utente", body.getUtente());

    FileUploadResult result = null;
    File zip = Paths.get(configurazioneService
        .requireConfigFromAuthorization(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString(),
        body.getPathFile()).toFile();
    if (!zip.exists()) {
      throw new NotFoundException("File non trovato");
    }
    try (ZipFile zipFile = new ZipFile(zip)) {


      Enumeration<? extends ZipEntry> entries = zipFile.entries();


      while (entries.hasMoreElements()) {

        ZipEntry zipEntry = entries.nextElement();

        if (zipEntry.isDirectory()) {
          throw new BadRequestException("Lo zip deve contenere solo singoli file");
        } else {
          if (body.getNomeFile().equals(zipEntry.getName())) {
            TikaInputStream tikaIS = TikaInputStream.get(zipFile.getInputStream(zipEntry));
            var tikaDetector = new Tika().detect(tikaIS);
            result = getFileShareManager().handleUpload(zipFile.getInputStream(zipEntry),
                body.getNomeFile(), tikaDetector, body.getUtente());
          }

        }
      }


    } catch (ZipException e) {
      throw new InternalServerException("Error reading zip stream", e);
    } catch (IOException e1) {
      throw new InternalServerException("Error reading zip stream", e1);
    }

    return result;
  }

  @Override
  public List<InfoFile> getFilePratiche(String path) {

    File folder = Paths.get(configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString(), path).toFile();

    if (!folder.exists()) {
      throw new BadRequestException("Path non trovato");
    }

    return Arrays.asList(folder.listFiles()).stream().map(file -> {
      try {
        var infoFile = new InfoFile();
        infoFile.setNomeFile(file.getName());
        infoFile.setDimensione(Files.size(file.toPath()));
        infoFile.setMimeType(new Tika().detect(file));

        return infoFile;
      } catch (IOException e) {

        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());


  }

  @Override
  public void deletePratichePath(String path) {

    Path folder = Paths.get(configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString(), path);

    if (Files.exists(folder)) {
      deletePath(folder);
    }

  }

  private void deletePath(Path path) {
    final String methodName = "deletePath";
    try {
      logger.info(methodName, "Cancellazione path " + path.toString());
      FilesUtils.deletePath(path);
    } catch (IOException e) {
      logger.error(methodName,
          "Errore durante la cancellazione del path " + path.toString() + ": " + e.getMessage(), e);
    }

  }

  private void notificaCancellazionePath(String path) {
    final String methodName = "cleanPratichePath";
    try {
      cosmoPraticheFeignClient.putPraticheFilePathCancellato(path);
    } catch (Exception e) {
      logger.error(methodName, "errore rimozione pathFile " + e.getMessage());
    }
  }


  private void cleanPratichePath() {

    final String methodName = "cleanPratichePath";

    logger.info(methodName, "Inizio esecuzione");

    LocalDate now = LocalDate.now().minusDays(MAX_DAYS_KEEP_PRATICHE);

    Path path = Paths.get(configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString());

    List<String> paths = null;
    try {
      paths = cosmoPraticheFeignClient.getPraticheFilePath(now.toString());
    } catch (Exception e) {
      logger.error(methodName, "Errore ottenimento path da eliminare: " + e.getMessage(), e);
    }

    if (paths != null) {
      for (var subpath : paths) {
        if (subpath != null && !subpath.isBlank()) {
          Path pathDaCancellare = Paths.get(path.toString(), subpath);
          if (Files.exists(pathDaCancellare)) {
            deletePath(pathDaCancellare);
            notificaCancellazionePath(subpath);
          }

        }
      }
    }

    logger.info(methodName, "Fine esecuzione");
  }




}
