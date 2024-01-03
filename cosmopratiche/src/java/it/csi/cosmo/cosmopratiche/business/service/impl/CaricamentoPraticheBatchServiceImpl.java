/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_TROVATA;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoDTipoTag_;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTagPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica_;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.enums.StatoCaricamentoPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.dto.rest.FileDocumentiZipUnzipRequest;
import it.csi.cosmo.cosmo.dto.rest.FileUploadResult;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmobusiness.dto.rest.Processo;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticheBatchService;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.EventService;
import it.csi.cosmo.cosmopratiche.business.service.TransactionService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDStatoCaricamentoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTipoDocTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTipoDocumentoTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTCaricamentoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoFileUploadFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoProcessoFeignClient;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 *
 */
@Service
public class CaricamentoPraticheBatchServiceImpl implements CaricamentoPraticheBatchService {

  private static final String CLASS_NAME =
      CaricamentoPraticheBatchServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTCaricamentoPraticaRepository cosmoTCaricamentoPraticaRepository;

  @Autowired
  private CosmoDStatoCaricamentoPraticaRepository cosmoDStatoCaricamentoPraticaRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoTipoPraticaRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoTipoDocumentoRepository;

  @Autowired
  private CosmoRTipoDocTipoPraticaRepository cosmoRTipoDocTipoPraticaRepository;

  @Autowired
  private CosmoRTipoDocumentoTipoDocumentoRepository cosmoRTipoDocumentoTipoDocumentoRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoFileUploadFeignClient cosmoFileUploadFeignClient;

  @Autowired
  private CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  CosmoRPraticaTagRepository cosmoRPraticaTagRepository;

  @Autowired
  EventService eventService;

  @Autowired
  private CosmoProcessoFeignClient cosmoProcessoFeignClient;

  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;

  @Override
  public List<CosmoTCaricamentoPratica> getCaricamentoPraticheWithCaricamentoCompletato() {

    return cosmoTCaricamentoPraticaRepository
        .findByCosmoDStatoCaricamentoPraticaCodice(
            StatoCaricamentoPratica.IN_ATTESA_DI_ELABORAZIONE.getCodice());

  }


  @Override
  public void elaboraCosmoTCaricamentoPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {

    String relativePath = cosmoTCaricamentoPratica.getPathFile();
    String rootPath =
        configurazioneService.requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH)
        .asString();

    String fileName = cosmoTCaricamentoPratica.getNomeFile();

    File excelFile = Paths.get(rootPath, relativePath, fileName).toFile();

    if (!excelFile.exists()) {
      throw new BadRequestException("File non trovato");
    } else
      registraFileExcel(cosmoTCaricamentoPratica, excelFile);



  }



  private void registraFileExcel(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      File excelFile) {
    Workbook wb = null;
    try {
      wb = WorkbookFactory.create(excelFile);
    } catch (InvalidFormatException | IOException e) {
      throw new BadRequestException(e.getMessage());
    }
    Sheet sheet = wb.getSheetAt(0);
    boolean almostOneLine = false;

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {

      Row currentRow = sheet.getRow(i);
      if (!this.isRowEmpty(currentRow)) {
        almostOneLine = true;
        try {
          validaRegistraRigaPratica(cosmoTCaricamentoPratica, i, sheet);


        }
        catch (Exception e) {
          this.registraErroreRigaPratica(cosmoTCaricamentoPratica,
              currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim()
                  : null,
                  e.getMessage());


        }

      }

    }

    if (sheet.getLastRowNum() < 1 || !almostOneLine)
      throw new BadRequestException("Il file excel deve contenere almeno una riga");

  }

  private void validaRegistraRigaPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      int row, Sheet sheet) {

    Row currentRow = sheet.getRow(row);
    Row titleRow = sheet.getRow(0);

    boolean rigaValidata = validaRigaPratica(cosmoTCaricamentoPratica, row, sheet);
    CosmoTCaricamentoPratica pratica = new CosmoTCaricamentoPratica();
    if (rigaValidata)
      try {
        boolean caricamentoCorretto = true;
        pratica = this.registraPratica(cosmoTCaricamentoPratica, currentRow);

        if (currentRow.getCell(7) != null
            && !currentRow.getCell(7).getStringCellValue().trim().isBlank()) {
          var documenti = currentRow.getCell(7).getStringCellValue().trim().split(",");
          if (documenti != null && documenti.length != 0) {
            caricamentoCorretto = elaboraFileZip(pratica, documenti);
          }
        }
        if (caricamentoCorretto) {
          postAvviaProcessoIdPratica(pratica, currentRow, titleRow);
        }
        inviaEvento(cosmoTCaricamentoPratica, Constants.EVENTS.CARICAMENTOPRATICHE_BATCH_PRATICA,
            pratica.getCosmoDStatoCaricamentoPratica().getDescrizione(),
            pratica.getIdentificativoPratica());


      } catch (Exception e) {
        this.registraErroreRigaPratica(cosmoTCaricamentoPratica,
            currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim()
                : null,
                e.getMessage());

      }

  }

  private void svuotaPathFile(String path, String nomeFile) {
    try {
      cosmoFileUploadFeignClient.deleteFilePratiche(path);
    } catch (Exception e) {
      throw new BadRequestException("Errore cancellazione " + nomeFile);
    }
  }

  private Processo avviaProcesso(String idPratica) {
    Processo processo = null;
    try {
      processo = cosmoProcessoFeignClient.postAvviaProcessoIdPratica(idPratica);
    } catch (Exception e) {
      throw new BadRequestException("Errore avvio processo");
    }
    return processo;
  }

  private void postAvviaProcessoIdPratica(CosmoTCaricamentoPratica pratica, Row currentRow, Row titleRow) {
    try {
      if (pratica.getCosmoTPratica() != null && pratica.getCosmoTPratica().getId() != null) {
        logger.info("CaricamentoPraticheBatchServiceImpl", String.valueOf(pratica.getCosmoTPratica().getId()));
        Processo processo = avviaProcesso(String.valueOf(pratica.getCosmoTPratica().getId()));
        List<VariabileProcesso> listaVariabileProcesso = new ArrayList<>();
        putPraticheVariabiliProcessInstanceId(processo, listaVariabileProcesso, currentRow, titleRow);
        this.registraProcessoOk(pratica,
            currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim()
                : null,
            "Processo avviato correttamente");

      }
    }catch (Exception e) {
      this.registraErroreRigaProcesso(pratica,
          currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim()
              : null,
              e.getMessage());
      this.registraPraticaCreataConErrore(pratica, e.getMessage());

    }
  }

  private void registraProcessoOk(CosmoTCaricamentoPratica pratica, String identificativoPratica, String messaggio) {
    CosmoTCaricamentoPratica cosmoTCaricamentoPraticaProcessoOk = new CosmoTCaricamentoPratica();
    cosmoTCaricamentoPraticaProcessoOk
    .setUtenteInserimento(pratica.getUtenteInserimento());
    cosmoTCaricamentoPraticaProcessoOk.setIdentificativoPratica(identificativoPratica);
    cosmoTCaricamentoPraticaProcessoOk.setDescrizioneEvento(messaggio);
    cosmoTCaricamentoPraticaProcessoOk.setNomeFile(pratica.getNomeFile());
    cosmoTCaricamentoPraticaProcessoOk.setPathFile(pratica.getPathFile());
    cosmoTCaricamentoPraticaProcessoOk.setCosmoTEnte(pratica.getCosmoTEnte());
    cosmoTCaricamentoPraticaProcessoOk.setCosmoTUtente(pratica.getCosmoTUtente());
    cosmoTCaricamentoPraticaProcessoOk.setCosmoTPratica(pratica.getCosmoTPratica());
    cosmoTCaricamentoPraticaProcessoOk
    .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.PROCESSO_AVVIATO.getCodice()));
    cosmoTCaricamentoPraticaProcessoOk.setCosmoTCaricamentoPratica(pratica);

    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPraticaProcessoOk);
  }


  private void putPraticheVariabiliProcessInstanceId(Processo processo, List<VariabileProcesso> listaVariabileProcesso, Row currentRow, Row titleRow) {
    for(int i = 8; i < currentRow.getLastCellNum(); i++) {
      if (titleRow.getCell(i) == null || titleRow.getCell(i).getCellType() != Cell.CELL_TYPE_STRING
          || titleRow.getCell(i).getStringCellValue() == null
          || titleRow.getCell(i).getStringCellValue().trim().isBlank()) {
        throw new BadRequestException("Nome variabile di processo non trovato");
      }
      if (Cell.CELL_TYPE_BLANK != titleRow.getCell(i).getCellType() && currentRow.getCell(i) != null
          && currentRow.getCell(i).getCellType() != Cell.CELL_TYPE_BLANK) {
        VariabileProcesso variabileProcesso = new VariabileProcesso();
        variabileProcesso.setName(titleRow.getCell(i).getStringCellValue().trim());
        switch (currentRow.getCell(i).getCellType()) {
          case Cell.CELL_TYPE_NUMERIC:
            formatCellTypeNumeric(currentRow, i, variabileProcesso);
            listaVariabileProcesso.add(variabileProcesso);
            break;
          case Cell.CELL_TYPE_STRING:
            if (!currentRow.getCell(i).getStringCellValue().trim().isBlank()) {
              variabileProcesso.setValue(currentRow.getCell(i).getStringCellValue().trim());
              listaVariabileProcesso.add(variabileProcesso);
            }
            break;
          case Cell.CELL_TYPE_BOOLEAN:
            variabileProcesso.setValue(currentRow.getCell(i).getBooleanCellValue());
            listaVariabileProcesso.add(variabileProcesso);
            break;
          default:
            break;
        }

      }
    }
    try {
      cosmoBusinessPraticheFeignClient.putPraticheVariabiliProcessInstanceId(processo.getId(), listaVariabileProcesso);
    } catch (Exception e) {
      throw new BadRequestException("Errore impostazione variabili di processo");
    }
  }

  private void formatCellTypeNumeric(Row currentRow, int i, VariabileProcesso variabile) {
    if (DateUtil.isCellDateFormatted(currentRow.getCell(i))) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      variabile.setValue(sdf.format(currentRow.getCell(i).getDateCellValue()));
    }else {
      double numberValue = currentRow.getCell(i).getNumericCellValue();
      if(String.valueOf(numberValue).contains(",") || String.valueOf(numberValue).contains(".")){
        variabile.setValue(numberValue);
      }else {
        variabile.setValue(Long.valueOf(String.valueOf(numberValue)));
      }
    }
  }

  private CosmoTCaricamentoPratica registraPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica, Row currentRow) {
    return this.transactionService
        .inTransactionOrThrow(() -> {

          CosmoTPratica pratica = new CosmoTPratica();
          pratica.setEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
          pratica.setUtenteCreazionePratica(currentRow.getCell(0).getStringCellValue().trim());
          pratica.setOggetto(currentRow.getCell(1).getStringCellValue().trim());
          pratica.setTipo(
              cosmoTipoPraticaRepository
              .findOneActive(currentRow.getCell(2).getStringCellValue().trim())
              .orElse(null));
          pratica.setRiassunto(
              currentRow.getCell(4) != null
              && !currentRow.getCell(4).getStringCellValue().trim().isBlank()
              ? currentRow.getCell(4).getStringCellValue().trim()
                  : null);
          pratica.setMetadati(
              currentRow.getCell(5) != null
              && !currentRow.getCell(5).getStringCellValue().trim().isBlank()
              ? currentRow.getCell(5).getStringCellValue().trim()
                  : null);
          pratica.setDataCreazionePratica(Timestamp.valueOf(LocalDateTime.now()));

          if (!StringUtils.isBlank(pratica.getRiassunto())) {
            // tenta indicizzazione del campo riassunto come best-attempt
            try {
              pratica.setRiassuntoTestuale(Jsoup.parse(pratica.getRiassunto()).text());
            } catch (Exception e) {
              logger.error("registraPratica",
                  "errore nell'indicizzazione del campo riassunto a versione testuale", e);
            }
          }
          pratica = this.cosmoTPraticaRepository.save(pratica);


          if (currentRow.getCell(6) != null
              && !currentRow.getCell(6).getStringCellValue().trim().isEmpty()) {
            cosmoRPraticaTagRepository
            .save(getPraticaTags(currentRow, cosmoTCaricamentoPratica, pratica));
          }



          CosmoTCaricamentoPratica cosmoTCaricamentoPraticaCreata = new CosmoTCaricamentoPratica();
          cosmoTCaricamentoPraticaCreata
          .setIdentificativoPratica(currentRow.getCell(3).getStringCellValue().trim());
          cosmoTCaricamentoPraticaCreata.setNomeFile(cosmoTCaricamentoPratica.getNomeFile());
          cosmoTCaricamentoPraticaCreata.setPathFile(cosmoTCaricamentoPratica.getPathFile());
          cosmoTCaricamentoPraticaCreata
          .setCosmoTUtente(cosmoTCaricamentoPratica.getCosmoTUtente());
          cosmoTCaricamentoPraticaCreata.setCosmoTEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
          cosmoTCaricamentoPraticaCreata
          .setUtenteInserimento(cosmoTCaricamentoPratica.getUtenteInserimento());
          cosmoTCaricamentoPraticaCreata.setCosmoTPratica(pratica);
          cosmoTCaricamentoPraticaCreata
          .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
              .findOne(StatoCaricamentoPratica.PRATICA_CREATA.getCodice()));
          cosmoTCaricamentoPraticaCreata.setCosmoTCaricamentoPratica(cosmoTCaricamentoPratica);
          cosmoTCaricamentoPraticaCreata
          .setDescrizioneEvento("Creata pratica " + pratica.getOggetto());


          return this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPraticaCreata);

        });

  }




  private List<CosmoRPraticaTag> getPraticaTags(Row currentRow,
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica, CosmoTPratica pratica) {

    ObjectMapper objectMapper = new ObjectMapper();

    List<TagRidotto> langList;
    try {
      langList = objectMapper.readValue(currentRow.getCell(6).getStringCellValue().trim(),
          new TypeReference<List<TagRidotto>>() {});
    } catch (IOException e) {
      throw new BadRequestException("Errore acquisizione tag");
    }

    List<CosmoTTag> tagsOnDB = new LinkedList<>();

    List<String> codiciTag = langList.stream().map(TagRidotto::getCodice).filter(Objects::nonNull)
        .collect(Collectors.toList());

    List<String> codiciTipiTag = langList.stream().map(tag -> tag.getTipoTag().getCodice())
        .filter(Objects::nonNull).collect(Collectors.toList());

    if (codiciTag.isEmpty()) {
      throw new BadRequestException("I codici dei tag non sono valorizzati");
    }

    if (codiciTipiTag.isEmpty()) {
      throw new BadRequestException("Le tipologie dei tag non sono valorizzate");
    }

    tagsOnDB = cosmoTTagRepository.findAllNotDeleted((root, q, cb) -> cb.and(
        cb.equal(root.get(CosmoTTag_.cosmoTEnte), cosmoTCaricamentoPratica.getCosmoTEnte()),
        root.get(CosmoTTag_.codice).in(codiciTag),
        root.get(CosmoTTag_.cosmoDTipoTag).get(CosmoDTipoTag_.codice).in(codiciTipiTag)));

    if (tagsOnDB.isEmpty()) {
      throw new BadRequestException("Tags non trovati");
    }


    List<CosmoRPraticaTag> praticaTags = new LinkedList<>();
    for (var tag : tagsOnDB) {
      CosmoRPraticaTag praticaTag = new CosmoRPraticaTag();
      CosmoRPraticaTagPK id = new CosmoRPraticaTagPK();
      id.setIdPratica(pratica.getId());
      id.setIdTag(tag.getId());
      praticaTag.setId(id);
      praticaTag.setCosmoTPratica(pratica);
      praticaTag.setCosmoTTag(tag);
      praticaTag.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
      praticaTags.add(praticaTag);

    }

    return praticaTags;
  }


  private boolean validaRigaPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      int row, Sheet sheet) {

    Row currentRow = sheet.getRow(row);

    String cfInitiator =
        currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue().trim() : null;
    String oggettoPratica =
        currentRow.getCell(1) != null ? currentRow.getCell(1).getStringCellValue().trim() : null;
    String tipologiaPratica =
        currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue().trim() : null;
    String identificativoPratica =
        currentRow.getCell(3) != null ? currentRow.getCell(3).getStringCellValue().trim() : null;


    if (StringUtils.isEmpty(oggettoPratica)) {
      registraErroreRigaPratica(cosmoTCaricamentoPratica, identificativoPratica,
          "oggetto pratica obbligatorio");
      return false;
    }

    if (StringUtils.isEmpty(identificativoPratica)) {
      registraErroreRigaPratica(cosmoTCaricamentoPratica, null,
          "Identificativo Pratica obbligatorio");
      return false;
    }

    if (checkDuplicateIdentificativoPratica(row, sheet)) {
      registraErroreRigaPratica(cosmoTCaricamentoPratica, null,
          "Identificativo Pratica già presente");
      return false;
    }




    if (StringUtils.isEmpty(cfInitiator)) {
      registraErroreRigaPratica(cosmoTCaricamentoPratica, identificativoPratica,
          "Cf Initiator obbligatorio");
      return false;
    }

    if (StringUtils.isEmpty(tipologiaPratica)) {
      registraErroreRigaPratica(cosmoTCaricamentoPratica, identificativoPratica,
          "Tipologia Pratica obbligatoria");
      return false;
    }

    else {
      List<CosmoDTipoPratica> tipoPratica =
          cosmoTipoPraticaRepository
          .findAllActive(
              (root, cq,
                  cb) -> cq.where(cb
                      .and(cb.equal(root.get(CosmoDTipoPratica_.codice), tipologiaPratica),
                          cb.equal(
                              root.get(CosmoDTipoPratica_.cosmoTEnte)
                              .get(CosmoTEnte_.id),
                              cosmoTCaricamentoPratica.getCosmoTEnte().getId())))
              .getRestriction());
      if (tipoPratica.isEmpty()) {
        registraErroreRigaPratica(cosmoTCaricamentoPratica, identificativoPratica,
            String.format(TIPO_PRATICA_NON_TROVATA, tipologiaPratica));
        return false;

      }

    }


    return true;

  }



  private boolean checkDuplicateIdentificativoPratica(int row, Sheet sheet) {
    var identificativoPratica = sheet.getRow(row).getCell(3).getStringCellValue().trim();
    for (int i = 1; i < row; i++) {
      var otherId = sheet.getRow(i).getCell(3).getStringCellValue().trim();
      if (identificativoPratica.equals(otherId))
        return true;
    }
    return false;
  }

  private void registraErroreRigaProcesso(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String identificativoPratica, String messaggio) {

    CosmoTCaricamentoPratica cosmoTCaricamentoPraticaInErrore = new CosmoTCaricamentoPratica();
    cosmoTCaricamentoPraticaInErrore.setIdentificativoPratica(identificativoPratica);
    cosmoTCaricamentoPraticaInErrore.setErrore(messaggio);
    cosmoTCaricamentoPraticaInErrore.setNomeFile(cosmoTCaricamentoPratica.getNomeFile());
    cosmoTCaricamentoPraticaInErrore.setPathFile(cosmoTCaricamentoPratica.getPathFile());
    cosmoTCaricamentoPraticaInErrore.setCosmoTUtente(cosmoTCaricamentoPratica.getCosmoTUtente());
    cosmoTCaricamentoPraticaInErrore.setCosmoTEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
    cosmoTCaricamentoPraticaInErrore.setCosmoTPratica(cosmoTCaricamentoPratica.getCosmoTPratica());
    cosmoTCaricamentoPraticaInErrore
    .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.PROCESSO_IN_ERRORE.getCodice()));
    cosmoTCaricamentoPraticaInErrore.setCosmoTCaricamentoPratica(cosmoTCaricamentoPratica);

    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPraticaInErrore);


  }


  private void registraErroreRigaPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String identificativoPratica, String messaggio) {

    CosmoTCaricamentoPratica cosmoTCaricamentoPraticaInErrore = new CosmoTCaricamentoPratica();
    cosmoTCaricamentoPraticaInErrore.setIdentificativoPratica(identificativoPratica);
    cosmoTCaricamentoPraticaInErrore.setErrore(messaggio);
    cosmoTCaricamentoPraticaInErrore.setNomeFile(cosmoTCaricamentoPratica.getNomeFile());
    cosmoTCaricamentoPraticaInErrore.setPathFile(cosmoTCaricamentoPratica.getPathFile());
    cosmoTCaricamentoPraticaInErrore.setCosmoTUtente(cosmoTCaricamentoPratica.getCosmoTUtente());
    cosmoTCaricamentoPraticaInErrore.setCosmoTEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
    cosmoTCaricamentoPraticaInErrore
    .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.PRATICA_IN_ERRORE.getCodice()));
    cosmoTCaricamentoPraticaInErrore.setCosmoTCaricamentoPratica(cosmoTCaricamentoPratica);



    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPraticaInErrore);

    var padre = cosmoTCaricamentoPraticaInErrore.getCosmoTCaricamentoPratica();

    if (padre != null) {


      inviaEvento(cosmoTCaricamentoPraticaInErrore,
          Constants.EVENTS.CARICAMENTOPRATICHE_BATCH_PRATICA,
          cosmoTCaricamentoPraticaInErrore.getCosmoDStatoCaricamentoPratica().getDescrizione(),
          cosmoTCaricamentoPraticaInErrore.getIdentificativoPratica());

    }

  }

  private void registraPraticaCreataConErrore(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String messaggio) {
    cosmoTCaricamentoPratica.setErrore(messaggio);
    cosmoTCaricamentoPratica
    .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.PRATICA_CREATA_CON_ERRORE.getCodice()));

    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPratica);
  }

  private void registraErroreRigaDocumento(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String identificativoPratica, String messaggio) {

    CosmoTCaricamentoPratica cosmoTCaricamentoPraticaInErrore = new CosmoTCaricamentoPratica();
    cosmoTCaricamentoPraticaInErrore.setIdentificativoPratica(identificativoPratica);
    cosmoTCaricamentoPraticaInErrore.setErrore(messaggio);
    cosmoTCaricamentoPraticaInErrore.setNomeFile(cosmoTCaricamentoPratica.getNomeFile());
    cosmoTCaricamentoPraticaInErrore.setPathFile(cosmoTCaricamentoPratica.getPathFile());
    cosmoTCaricamentoPraticaInErrore.setCosmoTUtente(cosmoTCaricamentoPratica.getCosmoTUtente());
    cosmoTCaricamentoPraticaInErrore.setCosmoTEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
    cosmoTCaricamentoPraticaInErrore
    .setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.CARICAMENTO_DOCUMENTI_IN_ERRORE.getCodice()));
    cosmoTCaricamentoPraticaInErrore.setCosmoTCaricamentoPratica(cosmoTCaricamentoPratica);


    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPraticaInErrore);



  }

  private CosmoTCaricamentoPratica registraCaricamentoDocumenti(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {

    CosmoTCaricamentoPratica documento = new CosmoTCaricamentoPratica();
    documento.setIdentificativoPratica(cosmoTCaricamentoPratica.getIdentificativoPratica());
    documento.setNomeFile(cosmoTCaricamentoPratica.getNomeFile());
    documento.setPathFile(cosmoTCaricamentoPratica.getPathFile());
    documento.setCosmoTUtente(cosmoTCaricamentoPratica.getCosmoTUtente());
    documento.setCosmoTEnte(cosmoTCaricamentoPratica.getCosmoTEnte());
    documento.setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.CARICAMENTO_DOCUMENTI.getCodice()));
    documento.setCosmoTCaricamentoPratica(cosmoTCaricamentoPratica);
    documento.setUtenteInserimento(cosmoTCaricamentoPratica.getUtenteInserimento());

    return documento;


  }




  @Override
  public void registraErroreGenerico(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String error) {

    cosmoTCaricamentoPratica.setCosmoDStatoCaricamentoPratica
    (cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.ERRORE_ELABORAZIONE.getCodice()));
    cosmoTCaricamentoPratica.setErrore(error);
    this.cosmoTCaricamentoPraticaRepository.save(cosmoTCaricamentoPratica);
    inviaEvento(cosmoTCaricamentoPratica, Constants.EVENTS.CARICAMENTOPRATICHE_BATCH,
        cosmoTCaricamentoPratica.getCosmoDStatoCaricamentoPratica().getDescrizione(), null);

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

  private boolean elaboraFileZip(CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String[] documenti) {

    String rootPath = configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString();

    List<String> fileCaricati = new ArrayList<>();

    for (String documento : documenti) {
      if (!documento.trim().isBlank()) {
        Workbook wb = null;

        File zip =
            Paths.get(rootPath, cosmoTCaricamentoPratica.getPathFile(), documento.trim()).toFile();

        try {
          wb = estraiFileZip(zip);


        } catch (Exception e) {
          this.registraErroreRigaDocumento(cosmoTCaricamentoPratica,
              cosmoTCaricamentoPratica.getIdentificativoPratica(),
              "Errore apertura zip " + documento + ": " + e.getMessage());
          this.registraPraticaCreataConErrore(cosmoTCaricamentoPratica,
              "Errore apertura zip " + documento + ": " + e.getMessage());
          return false;

        }
        if (wb != null) {

          try {

            var caricamentoDocumenti = this.registraCaricamentoDocumenti(cosmoTCaricamentoPratica);

            int caricati =
                validaRegistraExcelDocumentiZip(wb, documento.trim(), cosmoTCaricamentoPratica,
                    fileCaricati, caricamentoDocumenti, 0, false);
            validaRegistraExcelDocumentiZip(wb, documento.trim(), cosmoTCaricamentoPratica,
                fileCaricati,
                caricamentoDocumenti, caricati, true);

          } catch (Exception e) {

            this.registraErroreRigaDocumento(cosmoTCaricamentoPratica,
                cosmoTCaricamentoPratica.getIdentificativoPratica(), e.getMessage());
            this.registraPraticaCreataConErrore(cosmoTCaricamentoPratica, e.getMessage());
            return false;

          }
        }
      }

    }
    return true;


  }




  private Workbook estraiFileZip(File zip) {

    Workbook wb = null;


    try (ZipFile zipFile = new ZipFile(zip)) {

      boolean excelDocumentiDaCaricareFound = false;
      Enumeration<? extends ZipEntry> entries = zipFile.entries();


      while (entries.hasMoreElements() && !excelDocumentiDaCaricareFound) {

        ZipEntry zipEntry = entries.nextElement();

        if (zipEntry.isDirectory()) {
          throw new BadRequestException("Lo zip deve contenere solo singoli file");
        }
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
            wb = getExcelFile(zipFile.getInputStream(zipEntry));
          }
        }

      }
      if (!excelDocumentiDaCaricareFound)
        throw new BadRequestException(
            "L'archivio zip deve contenere un file excel con nome documentiDaCaricare oppure documenti_da_caricare");


    } catch (IOException e) {
      throw new InternalServerException("Errore nella lettura dell'archivio zip", e);
    }

    return wb;


  }

  private boolean hasDocumentoPadre(Row currentRow) {
    return currentRow.getCell(3) != null
        && !currentRow.getCell(3).getStringCellValue().trim().isBlank();
  }

  private String getNomeFile(Row currentRow) {
    return currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue().trim() : "";
  }



  private int validaRegistraExcelDocumentiZip(Workbook wb, String fileName,
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica, List<String> fileCaricati,
      CosmoTCaricamentoPratica documento, int caricati, boolean hasPadre) {

    Sheet sheet = wb.getSheetAt(0);


    for (int i = 1; i <= sheet.getLastRowNum(); i++) {

      Row currentRow = sheet.getRow(i);
      if (!this.isRowEmpty(currentRow)) {

        var identificativoPratica =
            currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue().trim()
                : null;

        try {
          if (identificativoPratica != null
              && identificativoPratica.equals(cosmoTCaricamentoPratica.getIdentificativoPratica())
              && hasDocumentoPadre(currentRow) == hasPadre) {

            validaRigaExcelDocumentiZip(currentRow, cosmoTCaricamentoPratica, fileCaricati);
            registraDocumenti(currentRow, cosmoTCaricamentoPratica, fileName, hasPadre);
            fileCaricati.add(currentRow.getCell(0).getStringCellValue().trim());
            caricati++;
            documento.setDescrizioneEvento(
                "Caricati " + caricati + " documenti da " + fileName);
            documento = this.cosmoTCaricamentoPraticaRepository.save(documento);


          }
        } catch (Exception e) {

          throw new BadRequestException(
              "Archivio zip " + fileName + ": errore acquisizione documento "
                  + getNomeFile(currentRow) + ": " + e.getMessage());



        }


      }

    }

    return caricati;


  }

  private Workbook getExcelFile(InputStream excelFile) {
    Workbook wb = null;
    try {
      wb = WorkbookFactory.create(excelFile);
    } catch (InvalidFormatException | IOException e) {
      throw new BadRequestException(e.getMessage());
    }
    return wb;
  }

  private void creaDocumento(String codiceTipo, String uuID, CosmoTPratica pratica, Row currentRow,
      boolean conPadre) {
    final String methodName = "creaDocumento";

    var docs = new CreaDocumentiRequest();
    var doc = new CreaDocumentoRequest();
    doc.setCodiceTipo(codiceTipo);
    doc.setUuidFile(uuID);
    doc.setTitolo(
        currentRow.getCell(4) != null
        && !currentRow.getCell(4).getStringCellValue().trim().isBlank()
        ? currentRow.getCell(4).getStringCellValue().trim()
            : null);
    doc.setDescrizione(
        currentRow.getCell(5) != null
        && !currentRow.getCell(5).getStringCellValue().trim().isBlank()
        ? currentRow.getCell(5).getStringCellValue().trim()
            : null);
    doc.setAutore(
        currentRow.getCell(6) != null
        && !currentRow.getCell(6).getStringCellValue().trim().isBlank()
        ? currentRow.getCell(6).getStringCellValue().trim()
            : null);



    if (conPadre) {
      String filter = "{\"filter\":{\"nomeFile\":{\"eq\":\""
          + currentRow.getCell(3).getStringCellValue().trim() + "\"}, \"idPratica\":{\"eq\":\""
          + pratica.getId() + "\"}}, \"page\":0,\"size\":10}";
      DocumentiResponse padre = null;
      try {
        padre = cosmoEcmDocumentiFeignClient.getDocumento(filter, false);

      } catch (Exception e) {
        throw new BadRequestException("errore ottenimento documento padre per "
            + currentRow.getCell(0).getStringCellValue().trim() + " con idPratica "
            + pratica.getId());
      }

      if (padre == null || padre.getDocumenti() == null || padre.getDocumenti().size() != 1) {
        throw new BadRequestException("errore ottenimento documento padre per "
            + currentRow.getCell(0).getStringCellValue().trim() + " con idPratica "
            + pratica.getId());
      }
      var padreEffettivo = padre;

      var tipoDocTipoDoc = cosmoRTipoDocumentoTipoDocumentoRepository.findAllActive((root, cq,
          cb) -> cq.where(
              cb.and(
                  cb.equal(
                      root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre)
                      .get(CosmoDTipoDocumento_.codice),
                      padreEffettivo.getDocumenti().get(0).getTipo().getCodice()),
                  cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato)
                      .get(CosmoDTipoDocumento_.codice), codiceTipo),
                  cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica)
                      .get(CosmoDTipoPratica_.codice), pratica.getTipo().getCodice())))
          .getRestriction());

      if (tipoDocTipoDoc.isEmpty()) {
        throw new BadRequestException("File " + currentRow.getCell(0).getStringCellValue().trim()
            + ": tipo allegato non associato al tipo del documento principale indicato");
      }


      doc.setParentId(String.valueOf(padre.getDocumenti().get(0).getId()));

    } else {
      var relazioniTipiDocumento = cosmoRTipoDocumentoTipoDocumentoRepository.findActiveByField(
          CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica, pratica.getTipo());
      var findAllegatoInRelazione = relazioniTipiDocumento.stream()
          .filter(p -> p.getCosmoDTipoDocumentoAllegato().getCodice().equalsIgnoreCase(codiceTipo))
          .findFirst();
      if (findAllegatoInRelazione.isPresent()) {
        throw new BadRequestException("File " + currentRow.getCell(0).getStringCellValue().trim()
            + ": tipo documento non assegnabile come documento principale");
      }
    }

    docs.setDocumenti(Arrays.asList(doc));


    try {
      cosmoEcmDocumentiFeignClient.postDocumento(pratica.getId(), docs);
    } catch (Exception e) {
      throw new BadRequestException(
          "errore creazione documento per la pratica " + pratica.getId());
    }
    logger.info(methodName, "creato documento per la pratica " + pratica.getId());

  }

  private void registraDocumenti(Row currentRow, CosmoTCaricamentoPratica cosmoTCaricamentoPratica,
      String zipName, boolean conPadre) {

    final String methodName = "registraDocumenti";

    FileDocumentiZipUnzipRequest request = null;

    String nomeFile = currentRow.getCell(0).getStringCellValue().trim();

    String codiceTipo = currentRow.getCell(1).getStringCellValue().trim();

    String rootPath = configurazioneService
        .requireConfig(ParametriApplicativo.UPLOAD_PRATICHE_ROOT_PATH).asString();

    File file = Paths.get(rootPath, cosmoTCaricamentoPratica.getPathFile(), zipName).toFile();

    try (ZipFile zipFile = new ZipFile(file)) {

      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      boolean found = false;

      while (entries.hasMoreElements() && !found) {
        ZipEntry zipEntry = entries.nextElement();

        if (zipEntry.getName().equals(nomeFile)) {
          found = true;

          request = new FileDocumentiZipUnzipRequest();
          // invio path contenente il file zip
          request
          .setPathFile(Paths.get(cosmoTCaricamentoPratica.getPathFile(), zipName).toString());
          // invio nome file documento da caricare
          request.setNomeFile(nomeFile);
          request.setUtente(cosmoTCaricamentoPratica.getCosmoTUtente().getCodiceFiscale());


        }


      }
      if (!found) {
        throw new BadRequestException("File non trovato");
      }

    } catch (ZipException e) {
      throw new InternalServerException("Errore nella lettura dell'archivio ", e);
    } catch (IOException e1) {
      throw new InternalServerException("Errore nella lettura dell'archivio ", e1);
    }

    FileUploadResult result = null;
    try {
      result = cosmoFileUploadFeignClient.postFilePraticheUnzipFile(request);
    } catch (Exception e2) {
      throw new BadRequestException("Errore estrazione archivio zip");
    }
    if (result == null || result.getUploadUUID().isBlank()) {
      throw new BadRequestException("errore estrazione file");
    }
    logger.info(methodName, "estratto file " + nomeFile + " in fileshare");

    creaDocumento(codiceTipo, result.getUploadUUID(),
        cosmoTCaricamentoPratica.getCosmoTPratica(), currentRow, conPadre);

  }


  private void validaRigaExcelDocumentiZip(Row currentRow,
      CosmoTCaricamentoPratica caricamentoPratica, List<String> fileCaricati) {



    String nomeFile =
        currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue().trim() : null;
    String codiceTipo =
        currentRow.getCell(1) != null ? currentRow.getCell(1).getStringCellValue().trim() : null;
    String identificativoPratica =
        currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue().trim() : null;

    if (StringUtils.isEmpty(nomeFile))
      throw new BadRequestException(
          "nome file obbligatorio");

    if (fileCaricati.contains(nomeFile)) {
      throw new BadRequestException(
          "File " + nomeFile + " già associato con questo identificativo pratica");
    }

    if (StringUtils.isEmpty(codiceTipo))
      throw new BadRequestException(
          "File " + nomeFile + ": codice tipo documento obbligatorio");


    var tipo = cosmoTipoDocumentoRepository.findOneActive(codiceTipo);

    if (!tipo.isPresent()) {
      throw new BadRequestException(
          "File " + nomeFile + ": codice tipo documento non trovato");
    }


    var tipoDocTipoPratica = cosmoRTipoDocTipoPraticaRepository
        .findAllActive(
            (root, cq,
                cb) -> cq.where(cb.and(
                    cb.equal(root.get(CosmoRTipodocTipopratica_.cosmoDTipoDocumento)
                        .get(CosmoDTipoDocumento_.codice), codiceTipo),
                    cb.equal(
                        root.get(CosmoRTipodocTipopratica_.cosmoDTipoPratica)
                        .get(CosmoDTipoPratica_.codice),
                        caricamentoPratica.getCosmoTPratica().getTipo().getCodice())))
            .getRestriction());

    if (tipoDocTipoPratica.isEmpty()) {
      throw new BadRequestException(
          "File " + nomeFile + ": tipo documento non associato alla tipologia di pratica");
    }


    if (StringUtils.isEmpty(identificativoPratica))
      throw new BadRequestException(
          "File " + nomeFile + ": identificativo pratica obbligatorio");

    if (caricamentoPratica.getCosmoTPratica() == null) {
      throw new BadRequestException("Nessuna pratica associata al caricamento");
    }

  }



  @Override
  public void iniziaElaborazione(CosmoTCaricamentoPratica temp) {
    temp.setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
        .findOne(StatoCaricamentoPratica.ELABORAZIONE_INIZIATA.getCodice()));

    this.cosmoTCaricamentoPraticaRepository.save(temp);


    inviaEvento(temp, Constants.EVENTS.CARICAMENTOPRATICHE_BATCH,
        temp.getCosmoDStatoCaricamentoPratica().getDescrizione(), null);

  }


  @Override
  public void completaElaborazione(CosmoTCaricamentoPratica temp) {

    var elaborazioneInErrore = temp.getCosmoTCaricamentoPraticas().stream()
        .filter(pratica -> pratica.getCosmoDStatoCaricamentoPratica().getCodice()
            .equals(StatoCaricamentoPratica.PRATICA_IN_ERRORE.getCodice())
            || pratica.getCosmoDStatoCaricamentoPratica().getCodice()
            .equals(StatoCaricamentoPratica.PRATICA_CREATA_CON_ERRORE.getCodice())
            || !pratica.getCosmoTCaricamentoPraticas().stream()
            .filter(docProcesso -> docProcesso.getCosmoDStatoCaricamentoPratica().getCodice()
                .equals(StatoCaricamentoPratica.PROCESSO_IN_ERRORE.getCodice())
                || docProcesso.getCosmoDStatoCaricamentoPratica().getCodice().equals(
                    StatoCaricamentoPratica.CARICAMENTO_DOCUMENTI_IN_ERRORE.getCodice()))
            .collect(Collectors.toList()).isEmpty())
        .findAny();
    if (elaborazioneInErrore.isPresent()) {
      temp.setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
          .findOne(StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA_CON_ERRORE.getCodice()));

      temp.setErrore("Elaborazione Completata con Errore");

      this.cosmoTCaricamentoPraticaRepository.save(temp);
    } else {

      String path = temp.getPathFile();

      try {

        temp.setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
            .findOne(StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA.getCodice()));

        temp.setPathFile(null);

        this.cosmoTCaricamentoPraticaRepository.save(temp);

        svuotaPathFile(path, temp.getNomeFile());

      } catch (Exception e) {

        temp.setPathFile(path);

        temp.setCosmoDStatoCaricamentoPratica(this.cosmoDStatoCaricamentoPraticaRepository
            .findOne(StatoCaricamentoPratica.ELABORAZIONE_COMPLETATA.getCodice()));

        this.cosmoTCaricamentoPraticaRepository.save(temp);
      }
    }


    inviaEvento(temp, Constants.EVENTS.CARICAMENTOPRATICHE_BATCH,
        temp.getCosmoDStatoCaricamentoPratica().getDescrizione(), null);

  }


  private void inviaEvento(CosmoTCaricamentoPratica caricamentoPratica, String tipoEvento,
      String evento,
      String identificativo) {
    var target = new WebSocketTargetSelector();
    target.setCodiceFiscale(caricamentoPratica.getCosmoTUtente().getCodiceFiscale());
    target.setIdEnte(caricamentoPratica.getCosmoTEnte().getId());
    Map<String, Object> payload = new HashMap<>();
    payload.put("nomeFile", caricamentoPratica.getNomeFile());
    if (identificativo != null) {
      payload.put("identificativo", identificativo);
    }
    payload.put("evento", evento);
    eventService.broadcastEvent(tipoEvento, payload, target);

  }






}
