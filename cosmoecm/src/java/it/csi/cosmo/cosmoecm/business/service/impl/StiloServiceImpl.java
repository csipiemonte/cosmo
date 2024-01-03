/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDStatoInvioStilo;
import it.csi.cosmo.common.entities.CosmoRInvioStiloDocumento;
import it.csi.cosmo.common.entities.CosmoRInvioStiloDocumentoPK;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.enums.StatoInvioStilo;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.StiloService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiInvioStiloDTO;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CycleAvoidingMappingContext;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoInvioStiloRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRInvioStiloDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class StiloServiceImpl implements StiloService {

  private static final String CLASS_NAME = StiloServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoRInvioStiloDocumentoRepository cosmoRInvioStiloDocumentoRepository;

  @Autowired
  private CosmoDStatoInvioStiloRepository cosmoDStatoInvioStiloRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;
  @Autowired
  private CosmoTDocumentoMapper cosmoTDocumentoMapper;

  @Override
  @Transactional(readOnly = true)
  public List<Documento> getDocumentiInvioStilo(String filter) {

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiInvioStiloDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiInvioStiloDTO.class);

    List<Documento> documentiInvioStilo = this.cosmoTDocumentoRepository
        .findAll(
            CosmoTDocumentoSpecifications.findDocumentiInvioStilo(ricercaParametrica.getFilter()))
        .stream()
        .filter(documento -> {
          var tipoDoc = !CollectionUtils.isEmpty(ricercaParametrica.getFilter().getCodici()) ? ricercaParametrica.getFilter().getCodici().stream()
              .filter(tipo -> tipo.getCodice().equals(documento.getTipo().getCodice())).findFirst()
                  .orElse(null) : null;

          if (tipoDoc == null) {
            return false;
          }

          if (tipoDoc.getCodicePadre() != null && !tipoDoc.getCodicePadre().isBlank()) {

            if (documento.getDocumentoPadre() == null) {
              return false;
            } else {
              return tipoDoc.getCodicePadre()
                  .equals(documento.getDocumentoPadre().getTipo().getCodice());
            }
          } else {
            return documento.getDocumentoPadre() == null;
          }
        })
        .map(temp -> this.cosmoTDocumentoMapper.toDTO(temp, new CycleAvoidingMappingContext()))
        .collect(Collectors.toList());

    logger.info("getDocumentiInvioStilo",
        "Numero documenti invio stilo trovati:" + documentiInvioStilo.size());

    return documentiInvioStilo;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void impostaEsitoInvioStilo(String idDocumento, EsitoInvioStiloRequest body) {
    String methodName = "impostaEsitoInvioStilo";

    ValidationUtils.require(idDocumento, "idDocumento");
    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getIdUd(), "body.idUd");
    ValidationUtils.require(body.getRisultato(), "body.risultato");

    CosmoRInvioStiloDocumentoPK pk = new CosmoRInvioStiloDocumentoPK();
    pk.setIdDocumento(Long.valueOf(idDocumento));
    pk.setIdUd(body.getIdUd());

    CosmoRInvioStiloDocumento documentoStilo =
        cosmoRInvioStiloDocumentoRepository.findOneActive(pk).orElse(null);


    if (documentoStilo != null && !documentoStilo.getCosmoDStatoInvioStilo().getCodice()
        .equals(StatoInvioStilo.INVIATO.getCodice())) {

      aggiornamentoStatoInvioStiloDocPresenteEsitoDiversoDaInviato(body, documentoStilo);

    } else if (documentoStilo == null) {

      var statoDaInserire = body.getRisultato() == 0 ? StatoInvioStilo.ERR_INVIO.getCodice()
          : StatoInvioStilo.INVIATO.getCodice();

      CosmoDStatoInvioStilo stato = cosmoDStatoInvioStiloRepository
          .findOneActive(statoDaInserire)
          .orElse(null);

      if (stato == null) {
        logger.error(methodName,
            String.format(ErrorMessages.STILO_STATO_NON_TROVATO, statoDaInserire));
        throw new NotFoundException(
            String.format(ErrorMessages.STILO_STATO_NON_TROVATO, statoDaInserire));
      }

      CosmoTDocumento documentoDaInserire = cosmoTDocumentoRepository.findOne(pk.getIdDocumento());

      if (documentoDaInserire == null || documentoDaInserire.cancellato()) {
        logger.error(methodName,
            String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(idDocumento)));
        throw new NotFoundException(
            String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(idDocumento)));
      }

      CosmoRInvioStiloDocumento documentoStiloDaSalvare = new CosmoRInvioStiloDocumento();
      documentoStiloDaSalvare.setId(pk);
      documentoStiloDaSalvare.setCosmoDStatoInvioStilo(stato);

      documentoStiloDaSalvare.setCodiceEsitoInvioStilo(body.getCodiceEsitoInvioStilo() != null
          ? Integer.toString(body.getCodiceEsitoInvioStilo())
              : null);

      documentoStiloDaSalvare.setMessaggioEsitoInvioStilo(body.getMessaggioEsitoInvioStilo());
      documentoStiloDaSalvare.setCosmoTDocumento(documentoDaInserire);
      documentoStiloDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

      if (statoDaInserire.equals(StatoInvioStilo.ERR_INVIO.getCodice())) {
        documentoStiloDaSalvare.setNumeroRetry(1);
      }

      cosmoRInvioStiloDocumentoRepository.save(documentoStiloDaSalvare);

    } else {

      logger.error(methodName, String.format(ErrorMessages.STILO_GIA_INVIATO,
          Long.valueOf(idDocumento), body.getIdUd()));
      throw new BadRequestException(String.format(ErrorMessages.STILO_GIA_INVIATO,
          Long.valueOf(idDocumento), body.getIdUd()));

    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void impostaInvioDocumentoStilo(String idDocumento, Long idUd) {
    String methodName = "impostaInvioDocumentoStilo";

    ValidationUtils.require(idDocumento, "idDocumento");
    ValidationUtils.require(idUd, "idUd");

    CosmoRInvioStiloDocumentoPK pk= new CosmoRInvioStiloDocumentoPK();
    pk.setIdDocumento(Long.valueOf(idDocumento));
    pk.setIdUd(idUd);

    CosmoRInvioStiloDocumento documentoStilo =
        cosmoRInvioStiloDocumentoRepository.findOneActive(pk).orElse(null);

    if (documentoStilo != null) {
      if (documentoStilo.getCosmoDStatoInvioStilo().getCodice()
          .equals(StatoInvioStilo.INVIATO.getCodice())) {

        logger.error(methodName,
            String.format(ErrorMessages.STILO_GIA_INVIATO, Long.valueOf(idDocumento), idUd));
        throw new BadRequestException(
            String.format(ErrorMessages.STILO_GIA_INVIATO, Long.valueOf(idDocumento), idUd));

      } else if (documentoStilo.getCosmoDStatoInvioStilo().getCodice()
          .equals(StatoInvioStilo.IN_FASE_DI_INVIO.getCodice())) {

        logger.error(methodName, String.format(ErrorMessages.STILO_GIA_IN_FASE_DI_INVIO,
            Long.valueOf(idDocumento), idUd));
        throw new BadRequestException(String.format(ErrorMessages.STILO_GIA_IN_FASE_DI_INVIO,
            Long.valueOf(idDocumento), idUd));

      } else {

        CosmoDStatoInvioStilo stato = cosmoDStatoInvioStiloRepository
            .findOneActive(StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()).orElse(null);

        if (stato == null) {
          logger.error(methodName, String.format(ErrorMessages.STILO_STATO_NON_TROVATO,
              StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()));
          throw new NotFoundException(String.format(ErrorMessages.STILO_STATO_NON_TROVATO,
              StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()));
        }

        documentoStilo.setCosmoDStatoInvioStilo(stato);
        documentoStilo.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
        documentoStilo.setDtFineVal(null);
        cosmoRInvioStiloDocumentoRepository.save(documentoStilo);

      }
    } else {
      CosmoDStatoInvioStilo stato = cosmoDStatoInvioStiloRepository
          .findOneActive(StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()).orElse(null);

      if (stato == null) {
        logger.error(methodName, String.format(ErrorMessages.STILO_STATO_NON_TROVATO,
            StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()));
        throw new NotFoundException(String.format(ErrorMessages.STILO_STATO_NON_TROVATO,
            StatoInvioStilo.IN_FASE_DI_INVIO.getCodice()));
      }

      CosmoTDocumento documentoDaInserire = cosmoTDocumentoRepository.findOne(pk.getIdDocumento());

      if (documentoDaInserire == null || documentoDaInserire.cancellato()) {
        logger.error(methodName,
            String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(idDocumento)));
        throw new NotFoundException(
            String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, Long.valueOf(idDocumento)));
      }

      CosmoRInvioStiloDocumento documentoStiloDaSalvare = new CosmoRInvioStiloDocumento();
      documentoStiloDaSalvare.setId(pk);
      documentoStiloDaSalvare.setCosmoDStatoInvioStilo(stato);
      documentoStiloDaSalvare.setCosmoTDocumento(documentoDaInserire);
      documentoStiloDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

      cosmoRInvioStiloDocumentoRepository.save(documentoStiloDaSalvare);
    }

  }

  private void aggiornamentoStatoInvioStiloDocPresenteEsitoDiversoDaInviato(
      EsitoInvioStiloRequest body, CosmoRInvioStiloDocumento documentoStilo) {
    final var methodName = "aggiornamentoStatoInvioStiloDocPresenteEsitoDiversoDaInviato";
    var statoDaInserire = body.getRisultato() == 0 ? StatoInvioStilo.ERR_INVIO.getCodice()
        : StatoInvioStilo.INVIATO.getCodice();

    CosmoDStatoInvioStilo stato =
        cosmoDStatoInvioStiloRepository.findOneActive(statoDaInserire).orElse(null);

    if (stato == null) {
      logger.error(methodName,
          String.format(ErrorMessages.STILO_STATO_NON_TROVATO, statoDaInserire));
      throw new NotFoundException(
          String.format(ErrorMessages.STILO_STATO_NON_TROVATO, statoDaInserire));
    }

    documentoStilo.setCosmoDStatoInvioStilo(stato);
    documentoStilo.setCodiceEsitoInvioStilo(
        body.getCodiceEsitoInvioStilo() != null ? Integer.toString(body.getCodiceEsitoInvioStilo())
            : null);
    documentoStilo.setMessaggioEsitoInvioStilo(body.getMessaggioEsitoInvioStilo());
    documentoStilo.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    documentoStilo.setDtFineVal(null);

    if (statoDaInserire.equals(StatoInvioStilo.ERR_INVIO.getCodice())) {
      documentoStilo.setNumeroRetry(
          documentoStilo.getNumeroRetry() != null ? documentoStilo.getNumeroRetry() + 1 : 1);
    }

    cosmoRInvioStiloDocumentoRepository.save(documentoStilo);

  }

}
