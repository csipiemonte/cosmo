/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.batch.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.fileshare.exceptions.FileShareUploadExcelException;
import it.csi.cosmo.cosmoauthorization.business.batch.UtentiBatch;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmoauthorization.business.service.LockService;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiBatchService;
import it.csi.cosmo.cosmoauthorization.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.UtentiBatchError;

@Service
public class UtentiBatchImpl extends ParentBatchImpl implements UtentiBatch {




  @Autowired
  private UtentiBatchService utentiBatchService;
  
  @Autowired
  private LockService lockService;

  @Autowired
  ConfigurazioneEnteService configurazioneEnteService;

  private static final String PROFILO_UTENTE_DEFAULT = "profilo.utente.default";
  
  public static final String JOB_LOCK_RESOURCE_CODE = "UTENTI_JOB_LOCK";

  @Override
  public boolean isEnabled() {

    return configurazioneService != null
        && configurazioneService.requireConfig(ParametriApplicativo.BATCH_UTENTI_ENABLE).asBool();
  }
  
  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void execute(BatchExecutionContext context) {

    String method = "execute";
    logger.info(method, "inizio batch utenti");
    
    this.lockBatchUtenti(context);

    logger.info(method, "fine batch utenti");

  }

  private List<Path> lockBatchUtenti(BatchExecutionContext context) {
  //@formatter:off
    return this.lockService.executeLocking(lock -> manageExcelFileInsideLock(lock, context),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
    
  }
  
  private List<Path> manageExcelFileInsideLock(CosmoTLock lock, BatchExecutionContext context) {
    
    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura file utenti senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura file utenti con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }
    
    List<Path> filesUtenti = getFilesUtenti();

    filesUtenti.forEach(temp -> manageExcelFile(temp, context));
    
    return filesUtenti;
  }

  private void manageExcelFile(Path filePath, BatchExecutionContext context) {
    var methodName = "manageExcelFile";

    Workbook wb = null;
    try {
      wb = WorkbookFactory.create(Files.newInputStream(filePath));
    } catch (InvalidFormatException e) {
      throw new FileShareUploadExcelException("Il formato del file non e' valido", e);
    } catch (IOException e) {
      throw new FileShareUploadExcelException("Errore nella lettura del file", e);
    }

    List<UtentiBatchError> errors = new ArrayList<>();
    boolean enteOK = true;
    try {

      validateWorkbook(wb);

      String codiceEnte = getCellValue(wb.getSheetAt(0).getRow(0).getCell(0));
      Optional<CosmoTEnte> optionalEnte = this.utentiBatchService.findEnteByCodiceFiscaleOrCodiceIpa(codiceEnte);
      Optional<CosmoCConfigurazioneEnte> optionalConfigurazioneEnte = Optional.empty();

      if (optionalEnte.isPresent()) {
        optionalConfigurazioneEnte =
            this.utentiBatchService.findConfigurazioneEnteByIdEnteAndChiave(
                optionalEnte.get().getId(), PROFILO_UTENTE_DEFAULT);
      }

      enteOK =
          manageSheetConfigurazione(optionalEnte, optionalConfigurazioneEnte, codiceEnte,
              wb.getSheetAt(0), filePath, context);


      if (enteOK) {
        manageSheetUtenti(optionalEnte.get(), optionalConfigurazioneEnte.get(), wb, filePath,
            errors, context);
        manageSheetProfili(optionalEnte.get(), optionalConfigurazioneEnte.get(), wb, filePath,
            errors, context);
        manageSheetGruppi(optionalEnte.get(), codiceEnte, wb, filePath, errors, context);
      }

    } catch (Exception e) {
      context.reportWarning("Errore su elaborazione file " + filePath.getFileName(), e);
      logger.error(methodName, e.getMessage());
    }

    finally {
      try {
        if (enteOK)
          manageEmail(filePath, wb, errors);
      }
      catch (Exception e) {
        context.reportWarning("Errore su invio mail file " + filePath.getFileName(), e);
        logger.error(methodName, e.getMessage());
      }
      try {
        deleteFile(filePath);
      } catch (IOException e) {
        context.reportWarning("Errore su cancellazione file " + filePath.getFileName(), e);
        logger.error(methodName, e.getMessage());
      }
    }

  }

  private void deleteFile(Path filePath) throws IOException {

    Files.delete(filePath);

  }

  private boolean manageSheetConfigurazione(Optional<CosmoTEnte> optionalEnte,
      Optional<CosmoCConfigurazioneEnte> optionalConfigurazioneEnte, String codiceEnte,
      Sheet sheetConfigurazione, Path filePath, BatchExecutionContext context) {

    if (optionalEnte.isEmpty()) {

      if (sheetConfigurazione.getRow(1) != null && sheetConfigurazione.getRow(1).getCell(0) != null
          && this.cellNotEmpty(sheetConfigurazione.getRow(1).getCell(0))) {
        sendEnteErrorMail(filePath, codiceEnte, getCellValue(sheetConfigurazione.getRow(1).getCell(0)));
      }
      context.reportWarning(String.format(ErrorMessages.E_ENTE_CODICE_NON_TROVATO, codiceEnte));
      return false;

    }
    if (optionalConfigurazioneEnte.isEmpty()
        || optionalConfigurazioneEnte.get().getValore() == null) {
      context
      .reportWarning("Configurazione ente " + codiceEnte + " non presente");
      if (sheetConfigurazione.getRow(1) != null && sheetConfigurazione.getRow(1).getCell(0) != null
          && this.cellNotEmpty(sheetConfigurazione.getRow(1).getCell(0))) {

        sendConfigurazioneEnteErrorMail(filePath, codiceEnte,
            getCellValue(sheetConfigurazione.getRow(1).getCell(0)));
      }

      return false;
    }



    String nomeProfilo = optionalConfigurazioneEnte.get().getValore();
    Optional<CosmoTProfilo> profiloTemp = this.utentiBatchService
        .findProfiloByFieldEqualsIgnoreCase(CosmoTProfilo_.codice, nomeProfilo, null);

    if (profiloTemp.isEmpty()) {
      if (sheetConfigurazione.getRow(1) != null && sheetConfigurazione.getRow(1).getCell(0) != null
          && this.cellNotEmpty(sheetConfigurazione.getRow(1).getCell(0))) {
        sendConfigurazioneEnteProfiloErrorMail(filePath, codiceEnte, nomeProfilo,
            getCellValue(sheetConfigurazione.getRow(1).getCell(0)));
      }

      context.reportWarning(
          "Configurazione ente " + codiceEnte + " profilo" + nomeProfilo + " non presente");
      return false;
    }



    return true;
  }


  private void manageSheetUtenti(CosmoTEnte ente, CosmoCConfigurazioneEnte configurazioneEnte,
      Workbook wb, Path temp, List<UtentiBatchError> errors,
      BatchExecutionContext context) {

    Sheet utenti = wb.getSheetAt(1);
    Iterator<Row> iteratorRowUtenti = utenti.rowIterator();
    while (iteratorRowUtenti.hasNext()) {
      Row utenteRow = iteratorRowUtenti.next();
      if(!this.isRowEmpty(utenteRow))
        try {
          manageUtente(ente, configurazioneEnte, wb, utenteRow, temp, errors, context);
        } catch (Exception e) {
          UtentiBatchError utentiBatchError = new UtentiBatchError();
          String codiceFiscale = getCellValue(utenteRow.getCell(0));
          utentiBatchError.setCodiceFiscaleUtente(codiceFiscale);
          utentiBatchError.setErrror(e.getMessage());
          errors.add(utentiBatchError);
          context.reportWarning("Errore su elaborazione file " + temp.getFileName() + " per utente "
              + codiceFiscale, e);
          logger.error("manageSheetUtenti", e.getMessage());
        }

    }

  }

  private void manageUtente(CosmoTEnte ente, CosmoCConfigurazioneEnte configurazioneEnte,
      Workbook wb, Row utenteRow, Path temp, List<UtentiBatchError> errors,
      BatchExecutionContext context) {

    String codiceFiscale = getCellValue(utenteRow.getCell(0));
    String nome = getCellValue(utenteRow.getCell(1));
    String cognome = getCellValue(utenteRow.getCell(2));
    String telefono = getCellValue(utenteRow.getCell(3));
    String mail = getCellValue(utenteRow.getCell(4));
    String dataInizioValidita = getCellValue(utenteRow.getCell(5));
    String dataFineValidita = getCellValue(utenteRow.getCell(6));
    this.utentiBatchService.saveUtenteBatch(codiceFiscale, nome, cognome, telefono, mail, dataInizioValidita,
        dataFineValidita, ente, configurazioneEnte);

  }

  private void manageSheetProfili(CosmoTEnte cosmoTEnte,
      CosmoCConfigurazioneEnte cosmoCConfigurazioneEnte, Workbook wb, Path temp,
      List<UtentiBatchError> errors,
      BatchExecutionContext context) {

    if (wb.getNumberOfSheets() >= 3 && wb.getSheetAt(2) != null) {
      Iterable<Row> iterableProfili = () -> wb.getSheetAt(2).iterator();
      Stream<Row> profiliRows = StreamSupport.stream(iterableProfili.spliterator(), false);
      profiliRows.filter(tempProfiloRow -> !this.isRowEmpty(tempProfiloRow)).forEach(tempProfiloRow -> {
        List<CosmoTProfilo> profili = new ArrayList<>();
        String codiceFiscale = getCellValue(tempProfiloRow.getCell(0));
        Optional<CosmoTUtente> utente = this.utentiBatchService
            .findUtenteByFieldEqualsIgnoreCase(CosmoTUtente_.codiceFiscale, codiceFiscale, null);
        if (utente.isPresent()) {
          int profiliCount = tempProfiloRow.getLastCellNum();
          for (int i = 1; i < profiliCount; i++) {
            if (this.cellNotEmpty(tempProfiloRow.getCell(i))) {
              Optional<CosmoTProfilo> profiloTemp = this.utentiBatchService
                  .findProfiloByFieldEqualsIgnoreCase(CosmoTProfilo_.codice,
                      getCellValue(tempProfiloRow.getCell(i)), null);
              if (profiloTemp.isPresent()) {
                if (profili.stream()
                    .filter(t -> t.getCodice().equals(profiloTemp.get().getCodice())).findAny()
                    .isEmpty())
                  profili.add(profiloTemp.get());

              } else {
                UtentiBatchError erroreProfilo = new UtentiBatchError();
                erroreProfilo.setCodiceFiscaleUtente(getCellValue(tempProfiloRow.getCell(0)));
                String profilo = getCellValue(tempProfiloRow.getCell(i));
                erroreProfilo.setErrror(
                    "Profilo " + profilo + " non registrato");
                errors.add(erroreProfilo);
                context.reportWarning(
                    "Profilo " + profilo + " non registrato");

              }
            }

          }
          try {
            manageProfiliUtente(profili, utente.get(), cosmoTEnte, cosmoCConfigurazioneEnte);
          } catch (Exception e) {
            UtentiBatchError utentiBatchError = new UtentiBatchError();
            utentiBatchError.setCodiceFiscaleUtente(codiceFiscale);
            utentiBatchError.setErrror(e.getMessage());
            errors.add(utentiBatchError);
            context.reportWarning("Errore su elaborazione file " + temp.getFileName() + " per utente "
                + codiceFiscale + " per inserimento profili", e);
            logger.error("manageSheetProfili", e.getMessage());
          }

        }

        else {
          UtentiBatchError utentiBatchError = new UtentiBatchError();
          utentiBatchError.setCodiceFiscaleUtente(codiceFiscale);
          utentiBatchError.setErrror(
              "Errore su inserimenti profili per utente " + codiceFiscale + ",utente non registrato");
          errors.add(utentiBatchError);
          context.reportWarning(
              "Errore su inserimenti profili per utente " + codiceFiscale + ",utente non registrato");
        }
      });
    }

  }

  private void manageProfiliUtente(List<CosmoTProfilo> profili, CosmoTUtente cosmoTUtente,
      CosmoTEnte cosmoTEnte, CosmoCConfigurazioneEnte cosmoCConfigurazioneEnte) {
    this.utentiBatchService.saveProfiliUtente(cosmoTUtente, cosmoTEnte, cosmoCConfigurazioneEnte,
        profili);
  }

  private void manageSheetGruppi(CosmoTEnte cosmoTEnte, String codiceEnte, Workbook wb, Path temp,
      List<UtentiBatchError> errors,
      BatchExecutionContext context) {

    if (wb.getNumberOfSheets() >= 4 && wb.getSheetAt(3) != null) {
      Iterable<Row> iterableGruppi = () -> wb.getSheetAt(3).iterator();
      Stream<Row> gruppiRows = StreamSupport.stream(iterableGruppi.spliterator(), false);
      gruppiRows.filter(tempGruppiRow -> !this.isRowEmpty(tempGruppiRow)).forEach(tempGruppiRow -> {
        List<CosmoTGruppo> gruppi = new ArrayList<>();
        String codiceFiscale = getCellValue(tempGruppiRow.getCell(0));
        Optional<CosmoTUtente> utente = this.utentiBatchService
            .findUtenteByFieldEqualsIgnoreCase(CosmoTUtente_.codiceFiscale, codiceFiscale, null);
        if (utente.isPresent()) {
          int profiliCount = tempGruppiRow.getLastCellNum();
          for (int i = 1; i < profiliCount; i++) {
            if (this.cellNotEmpty(tempGruppiRow.getCell(i))) {
              Optional<CosmoTGruppo> gruppoTemp = this.utentiBatchService.findGruppoByCodiceAndEnte(
                  getCellValue(tempGruppiRow.getCell(i)), cosmoTEnte);
              if (gruppoTemp.isPresent()) {
                if (gruppi.stream().filter(t -> t.getCodice().equals(gruppoTemp.get().getCodice()))
                    .findAny().isEmpty())
                  gruppi.add(gruppoTemp.get());
              }
              else {
                UtentiBatchError erroreGruppo = new UtentiBatchError();
                erroreGruppo.setCodiceFiscaleUtente(getCellValue(tempGruppiRow.getCell(0)));
                String gruppo = getCellValue(tempGruppiRow.getCell(i));
                erroreGruppo.setErrror("Gruppo " + gruppo
                    + " non registrato per ente " + codiceEnte);
                errors.add(erroreGruppo);
                context.reportWarning("Gruppo " + gruppo
                    + " non registrato per ente " + codiceEnte);

              }
            }

          }
          try {
            manageGruppiUtente(gruppi, utente.get());
          } catch (Exception e) {
            UtentiBatchError utentiBatchError = new UtentiBatchError();
            utentiBatchError.setCodiceFiscaleUtente(codiceFiscale);
            utentiBatchError.setErrror(e.getMessage());
            errors.add(utentiBatchError);
            context.reportWarning("Errore su elaborazione file " + temp.getFileName() + " per utente "
                + codiceFiscale + " per inserimento gruppi", e);
            logger.error("manageSheetGruppi", e.getMessage());
          }

        }

        else {
          UtentiBatchError utentiBatchError = new UtentiBatchError();
          utentiBatchError.setCodiceFiscaleUtente(codiceFiscale);
          utentiBatchError.setErrror(
              "Errore su inserimenti gruppi per utente " + codiceFiscale + ",utente non registrato");
          errors.add(utentiBatchError);
          context.reportWarning(
              "Errore su inserimenti gruppi per utente " + codiceFiscale + ",utente non registrato");
        }
      });
    }

  }

  private void manageGruppiUtente(List<CosmoTGruppo> gruppi, CosmoTUtente cosmoTUtente) {
    this.utentiBatchService.saveGruppiUtente(cosmoTUtente, gruppi);
  }

  private List<Path> getFilesUtenti() {

    Path rootPath = Paths
        .get(configurazioneService.requireConfig(ParametriApplicativo.BATCH_UTENTI_ROOT_PATH).asString());
    Path path = Paths.get(rootPath.toString(),
        configurazioneService.requireConfig(ParametriApplicativo.BATCH_UTENTI_PATH).asString());

    if (!Files.exists(path))
      return new ArrayList<>();

    try {
      return Files.walk(path, Integer.MAX_VALUE).filter(file -> !Files.isDirectory(file))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new InternalServerException("Errore lettura files nella directory " + path.toString(), e);
    }
  }

  private void manageEmail(Path filePath, Workbook wb, List<UtentiBatchError> errors) {

    if (wb.getSheetAt(0).getRow(1) != null && this.cellNotEmpty(wb.getSheetAt(0).getRow(1).getCell(0))) {

      if (!errors.isEmpty()) {

        sendErrorsMail(filePath, getCellValue(wb.getSheetAt(0).getRow(1).getCell(0)), errors);

      }

      else {
        sendMailOK(filePath, getCellValue(wb.getSheetAt(0).getRow(1).getCell(0)));
      }

    }

  }

  private void sendEnteErrorMail(Path filePath, String codiceEnte, String mailTo) {

    this.mailService.inviaMailUtentiBatch(mailTo, "Elaborazione file " + filePath.getFileName(),
        "L'elaborazione del file " + filePath.getFileName() + " non e' stata eseguita "
            + "in quanto l'ente " + codiceEnte + " non e' registrato");

  }


  private void sendConfigurazioneEnteErrorMail(Path filePath, String codiceEnte, String mailTo) {
    this.mailService.inviaMailUtentiBatch(mailTo, "Elaborazione file " + filePath.getFileName(),
        "L'elaborazione del file " + filePath.getFileName() + " non e' stata eseguita "
            + "in quanto la configurazione dell'ente " + codiceEnte
            + " non e' stata effettuata correttamente");

  }

  private void sendConfigurazioneEnteProfiloErrorMail(Path filePath, String codiceEnte,
      String nomeProfilo, String mailTo) {
    this.mailService.inviaMailUtentiBatch(mailTo, "Elaborazione file " + filePath.getFileName(),
        "L'elaborazione del file " + filePath.getFileName() + " non e' stata eseguita "
            + "in quanto la configurazione dell'ente " + codiceEnte
            + " e' associata ad un profilo ("
            + nomeProfilo + " non registrato");

  }


  private void sendErrorsMail(Path filePath, String mailTo, List<UtentiBatchError> errors) {

    ObjectMapper mapper = new ObjectMapper();
    String errorsToString = null;
    try {
      errorsToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors);
    } catch (JsonProcessingException e) {
      logger.error("sendErrorsMail", e.getMessage());
    }

    this.mailService.inviaMailUtentiBatch(mailTo, "Elaborazione file " + filePath.getFileName(),
        "Elaborazione del documento " + filePath.getFileName() + " ha restuituito questi problemi:<br>esiti:"
            + "<br>" + errorsToString);

  }

  private void sendMailOK(Path filePath, String mailTo) {
    this.mailService.inviaMailUtentiBatch(mailTo, "Elaborazione file " + filePath.getFileName(),
        "Elaborazione del documento " + filePath.getFileName() + " andata a buon fine");

  }

  public boolean validateWorkbook(Workbook wb) {

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
    String methodName = "verifySheetAndSheetName";

    if (required && (wb.getNumberOfSheets() < sheetNumber)) {
      logger.error(methodName, "Foglio " + sheetNumber + " obbligatorio");
      throw new FileShareUploadExcelException("Foglio " + sheetNumber + " obbligatorio");
    }

    if (wb.getNumberOfSheets() >= sheetNumber) {
      Sheet sheet = wb.getSheetAt(sheetNumber - 1);

      if (!caseSensitive && (StringUtils.isEmpty(sheet.getSheetName())
          || !sheet.getSheetName().equalsIgnoreCase(sheetName))) {
        logger.error(methodName, "Nome foglio " + sheetNumber + " non valido");
        throw new FileShareUploadExcelException("Nome foglio " + sheetNumber + " non valido");

      }

      if (caseSensitive
          && (StringUtils.isEmpty(sheet.getSheetName()) || !sheet.getSheetName().equals(sheetName))) {
        logger.error(methodName, "Foglio " + sheetNumber + " obbligatorio");
        throw new FileShareUploadExcelException("Foglio " + sheetNumber + " obbligatorio");
      }

    }
  }

  private void validateSheet(Workbook wb, int sheetNumber, boolean required) {
    var methodName = "validateSheet";

    logger.info(methodName, "Inizio validazione sheet " + sheetNumber);

    Sheet sheet = wb.getSheetAt(sheetNumber - 1);

    Iterator<Row> sheetRowsIterator = sheet.iterator();

    int rowNum = 0;
    boolean almostOneLine = false;
    while (sheetRowsIterator.hasNext()) {
      almostOneLine = true;
      rowNum++;
      Row sheetCurrentRow = sheetRowsIterator.next();

      if (!isRowEmpty(sheetCurrentRow) && StringUtils.isEmpty(getCellValue(sheetCurrentRow.getCell(0)))) {
        logger.error(methodName,
            "Foglio " + sheetNumber + ", Riga " + rowNum + ", Cella 1 obbligatoria");
        throw new FileShareUploadExcelException(
            "Foglio " + sheetNumber + ", Riga " + rowNum + ", Cella 1 obbligatoria");
      }

    }

    if (required && !almostOneLine) {
      logger.error(methodName,
          "Foglio " + sheetNumber + ", Riga " + (rowNum + 1) + ", Cella 1 obbligatoria");
      throw new FileShareUploadExcelException(
          "Foglio " + sheetNumber + ", Riga " + (rowNum + 1) + ", Cella 1 obbligatoria");

    }

    logger.info(methodName, "Fine validazione sheet " + sheetNumber);

  }

  private boolean validateSheetDue(Workbook wb) {
    var methodName = "validateSheetDue";

    logger.info(methodName, "Inizio validazione sheet 2");

    if (wb.getNumberOfSheets() < 2) {
      logger.error(methodName, "Foglio 2 obbligatorio");
      throw new FileShareUploadExcelException("Foglio 2 obbligatorio");
    }

    Sheet sheet = wb.getSheetAt(1);

    if (StringUtils.isEmpty(sheet.getSheetName()) || !sheet.getSheetName().equalsIgnoreCase("Utenti")) {
      logger.error(methodName, "Nome foglio 2 non valido");
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
            logger.error(methodName,
                "Foglio 2, Riga " + (rowNum + 1) + ", Cella " + (i + 1) + " obbligatoria");
            throw new FileShareUploadExcelException(
                "Foglio 2, Riga " + (rowNum + 1) + ", Cella " + (i + 1) + " obbligatoria");
          }

        }
      }
      rowNum++;

    }

    if (!almostOneLine) {
      logger.error(methodName, "Foglio 2 vuoto");
      throw new FileShareUploadExcelException("Foglio 2 vuoto");
    }

    logger.info(methodName, "Fine validazione sheet 2");
    return true;

  }

  private String getCellValue(Cell cell) {

    if (cell == null)
      return null;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    int cellType = cell.getCellType();

    switch (cellType) {
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          return sdf.format(cell.getDateCellValue());
        }
        return NumberToTextConverter.toText(cell.getNumericCellValue());
      case Cell.CELL_TYPE_STRING:
        return cell.getStringCellValue().trim();
      case Cell.CELL_TYPE_ERROR:
        return String.valueOf(cell.getErrorCellValue());
      case Cell.CELL_TYPE_BLANK:
        return "";
      case Cell.CELL_TYPE_FORMULA:
        return cell.getCellFormula();
      case Cell.CELL_TYPE_BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      default:
        return null;
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

  private boolean cellNotEmpty(Cell cell) {
    return cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
        && cell.getStringCellValue().trim().length() > 0;
  }

}
