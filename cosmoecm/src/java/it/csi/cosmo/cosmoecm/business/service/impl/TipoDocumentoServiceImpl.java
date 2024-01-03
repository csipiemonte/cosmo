/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.TipoDocumentoService;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoDTipoDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoRFormatoFileTipoDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRFormatoFileTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocumentoTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.util.CommonUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

  private static final String CLASS_NAME = TipoDocumentoServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoDTipoDocumentoMapper cosmoDTipoDocumentoMapper;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Autowired
  private CosmoRTipoDocTipoPraticaRepository cosmoRTipoDocTipoPraticaRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoRFormatoFileTipoDocumentoRepository cosmoRFormatoFileTipoDocumentoRepository;

  @Autowired
  private CosmoRFormatoFileTipoDocumentoMapper cosmoRFormatoFileTipoDocumentoMapper;

  @Autowired
  private CosmoRTipoDocumentoTipoDocumentoRepository cosmoRTipoDocumentoTipoDocumentoRepository;

  @Override
  @Transactional(readOnly = true)
  public List<TipoDocumento> getTipiDocumenti(String idPratica, String codiceTipoDocPadre) {
    final var methodName = "getTipiDocumenti";
    logger.info(methodName, "Inizio getTipiDocumenti");
    List<TipoDocumento> output = new LinkedList<>();

    CommonUtils.validaDatiInput(idPratica, "id pratica");

    CosmoTPratica pratica = cosmoTPraticaRepository.findOneNotDeleted(Long.valueOf(idPratica))
        .orElseThrow(NotFoundException::new);

    if (pratica.getTipo() != null) {
      pratica
      .getTipo().getCosmoRTipodocTipopraticas().stream().filter(r -> r.valido()
          && r.getCosmoDTipoDocumento() != null && r.getCosmoDTipoDocumento().valido())
      .forEach(tipoDocTipoPratica -> {

        var relazioneTipiDocAllegati = cosmoRTipoDocumentoTipoDocumentoRepository
            .findActiveByField(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre,
                tipoDocTipoPratica.getCosmoDTipoDocumento());

        var relazioneTipiDocPrincipale = cosmoRTipoDocumentoTipoDocumentoRepository
            .findActiveByField(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato,
                tipoDocTipoPratica.getCosmoDTipoDocumento());

        tipoDocTipoPratica.getCosmoDTipoDocumento()
        .setCosmoRTipoDocumentoTipoDocumentosAllegato(relazioneTipiDocAllegati);

        tipoDocTipoPratica.getCosmoDTipoDocumento()
        .setCosmoRTipoDocumentoTipoDocumentosPadre(relazioneTipiDocPrincipale);

        if ((StringUtils.isBlank(codiceTipoDocPadre)
            && tipoDocTipoPratica.getCosmoDTipoDocumento()
            .getCosmoRTipoDocumentoTipoDocumentosPadre().isEmpty())
            || (!StringUtils.isBlank(codiceTipoDocPadre)
                && !tipoDocTipoPratica.getCosmoDTipoDocumento()
                .getCosmoRTipoDocumentoTipoDocumentosPadre().isEmpty()
                && tipoDocTipoPratica.getCosmoDTipoDocumento()
                .getCosmoRTipoDocumentoTipoDocumentosPadre().stream()
                .anyMatch(tipoDoc -> tipoDoc.getId().getCodicePadre()
                    .equals(codiceTipoDocPadre)
                    && tipoDoc.getId().getCodiceTipoPratica()
                    .equals(pratica.getTipo().getCodice())))) {

          var pkRelazioneTipoDoc = new CosmoRTipoDocumentoTipoDocumentoPK();
          pkRelazioneTipoDoc.setCodiceAllegato(tipoDocTipoPratica.getCosmoDTipoDocumento().getCodice());
          pkRelazioneTipoDoc.setCodicePadre(codiceTipoDocPadre);
          pkRelazioneTipoDoc.setCodiceTipoPratica(tipoDocTipoPratica.getCosmoDTipoPratica().getCodice());
          var tipoDocPadre =
              cosmoRTipoDocumentoTipoDocumentoRepository.findOneActive(pkRelazioneTipoDoc)
              .orElseGet(CosmoRTipoDocumentoTipoDocumento::new);
          var tipoDoc = cosmoDTipoDocumentoMapper.toDTO(tipoDocTipoPratica.getCosmoDTipoDocumento());
          tipoDoc.setPrincipali(Arrays.asList(cosmoDTipoDocumentoMapper.toDTO(tipoDocPadre.getCosmoDTipoDocumentoPadre())));

              output.add(tipoDoc);
        }
      });
    }
    logger.info(methodName, "Fine getTipiDocumenti");
    return output;
  }

  @Override
  public List<TipoDocumento> getTipiDocumentiAll(String tipoPratica) {
    final var methodName = "getTipiDocumentiAll";
    logger.info(methodName, "Inizio getTipiDocumentiAll");
    CommonUtils.requireString(tipoPratica, "tipo pratica");

    CosmoDTipoPratica tipoPraticaDB =
        cosmoDTipoPraticaRepository.findOneActiveByField(CosmoDTipoPratica_.codice, tipoPratica)
        .orElseThrow(NotFoundException::new);

    List<CosmoDTipoDocumento> tipiDocumenti = cosmoRTipoDocTipoPraticaRepository
        .findActiveByField(CosmoRTipodocTipopratica_.cosmoDTipoPratica, tipoPraticaDB).stream()
        .map(CosmoRTipodocTipopratica::getCosmoDTipoDocumento).collect(Collectors.toList());

    List<TipoDocumento> tipiDocumentiDTO = new ArrayList<>();
    tipiDocumenti.forEach(tipoDB -> {
      var tipo = cosmoDTipoDocumentoMapper.toDTO(tipoDB);

      var rel = tipoDB.getCosmoRTipoDocumentoTipoDocumentosAllegato().stream()
          .filter(p -> p.valido() && p.getCosmoDTipoPratica().getCodice().equals(tipoPratica))
          .map(CosmoRTipoDocumentoTipoDocumento::getCosmoDTipoDocumentoPadre)
          .collect(Collectors.toList());

      tipo.setPrincipali(cosmoDTipoDocumentoMapper
          .toDTOs(rel));
      tipiDocumentiDTO.add(tipo);

    });
    logger.info(methodName, "Fine getTipiDocumentiAll");
    return tipiDocumentiDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public List<TipoDocumento> getTipiDocumento(List<String> codici) {
    final var methodName = "getTipiDocumento";
    logger.info(methodName, "Inizio getTipiDocumento");

    List<CosmoDTipoDocumento> tipiDocumentoDB = codici != null && !codici.isEmpty()
        ? cosmoDTipoDocumentoRepository.findAllByCodiceIn(codici)
            : cosmoDTipoDocumentoRepository.findAllActive();

    logger.info(methodName, "Fine getTipiDocumento");
    return cosmoDTipoDocumentoMapper.toDTOs(tipiDocumentoDB);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TipoDocumento> getTipiDocumento(List<String> codici, Boolean addFormatoFile,
      String codicePadre, String codiceTipoPratica) {

    final var methodName = "getTipiDocumento";
    logger.info(methodName, "Inizio getTipiDocumento");

    List<CosmoDTipoDocumento> tipiDocumentoDB;

    if (codici != null && !codici.isEmpty()) {
      tipiDocumentoDB = cosmoDTipoDocumentoRepository.findAllActive(
          (root, cq, cb) -> cb.and(root.get(CosmoDTipoDocumento_.codice).in(codici)));

    } else {
      tipiDocumentoDB = cosmoDTipoDocumentoRepository.findAllActive();

      if (codicePadre == null || codicePadre.isBlank()) {
        tipiDocumentoDB = tipiDocumentoDB.stream()
            .filter(tipoDoc -> tipoDoc.getCosmoRTipoDocumentoTipoDocumentosAllegato().isEmpty())
            .collect(Collectors.toList());
      } else {
        tipiDocumentoDB =
            tipiDocumentoDB.stream()
            .filter(filterPerResetCodiceStardas(codiceTipoPratica, codicePadre))
            .map(r -> {
              r.setCodiceStardas(null);
              return r;
            })
            .collect(Collectors.toList());
      }
    }


    List<TipoDocumento> output = new LinkedList<>();

    if (Boolean.TRUE.equals(addFormatoFile)) {
      tipiDocumentoDB.stream().filter(r -> r != null && r.valido()).forEach(tipoDocumentoDB -> {
        var relTipoDocFormatoFile = cosmoRFormatoFileTipoDocumentoRepository.findActiveByField(
            CosmoRFormatoFileTipoDocumento_.cosmoDTipoDocumento, tipoDocumentoDB);

        var tipoDocDTO = cosmoDTipoDocumentoMapper.toDTO(tipoDocumentoDB);
        tipoDocDTO
        .setFormatiFile(cosmoRFormatoFileTipoDocumentoMapper.toDTOs(relTipoDocFormatoFile));
        output.add(tipoDocDTO);
      });
    } else {
      output.addAll(cosmoDTipoDocumentoMapper.toDTOs(tipiDocumentoDB));
    }

    if (codicePadre != null && !codicePadre.isBlank()) {
      var padre = cosmoDTipoDocumentoRepository.findOneActive(codicePadre);

      if (padre.isPresent()) {
        output.forEach(single -> single
            .setPrincipali(Arrays.asList(cosmoDTipoDocumentoMapper.toDTO(padre.get()))));
      }
    }

    logger.info(methodName, "Fine getTipiDocumento");

    return output;
  }

  private Predicate<CosmoDTipoDocumento> filterPerResetCodiceStardas(String codiceTipoPratica, String codicePadre) {
    return tipoDoc -> tipoDoc.getCosmoRTipoDocumentoTipoDocumentosAllegato().isEmpty()
        && tipoDoc.getCosmoRTipoDocumentoTipoDocumentosPadre().isEmpty()
        && tipoDoc.getCosmoRTipoDocumentoTipoDocumentosAllegato().stream()
            .noneMatch(tipoDocAllegato -> tipoDocAllegato.getCosmoDTipoDocumentoPadre().getCodice()
                .equals(codicePadre)
                && tipoDocAllegato.getCosmoDTipoPratica().getCodice().equals(codiceTipoPratica)
                && tipoDocAllegato.valido());
  }

}
