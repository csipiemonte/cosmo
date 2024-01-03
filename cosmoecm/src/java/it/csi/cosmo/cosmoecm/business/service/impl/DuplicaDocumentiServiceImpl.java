/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoFirmato;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoTAnteprimaContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoFirmato;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.DuplicaDocumentiService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.dto.index2.IndexContentDisposition;
import it.csi.cosmo.cosmoecm.dto.index2.IndexShareScope;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioneDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTAnteprimaContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTInfoVerificaFirmaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;

/**
 *
 */
@Service
@Transactional
public class DuplicaDocumentiServiceImpl implements DuplicaDocumentiService {

  private static final CosmoLogger logger = LoggerFactory.getLogger(
      LogCategory.BUSINESS_LOG_CATEGORY, DuplicaDocumentiServiceImpl.class.getSimpleName());

  private static final String PATH_SEPARATOR = "/";

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoDStatoDocumentoRepository cosmoDStatoDocumentoRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Autowired
  private CosmoTInfoVerificaFirmaRepository cosmoTInfoVerificaFirmaRepository;

  @Autowired
  private CosmoTAnteprimaContenutoDocumentoRepository cosmoTAnteprimaContenutoDocumentoRepository;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public RiferimentoOperazioneAsincrona duplicaDocumenti(Long idPraticaDa, Long idPraticaA) {
    String methodName = "duplicaDocumenti";

    ValidationUtils.require(idPraticaDa, "id della pratica da cui duplicare i documenti");
    ValidationUtils.require(idPraticaA, "id della pratica in cui duplicare i documenti");

    var praticaDa = cosmoTPraticaRepository.findOneNotDeleted(idPraticaDa).orElseThrow(() -> {
      String errorMessage = String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPraticaDa);
      logger.error(methodName, errorMessage);
      throw new BadRequestException(errorMessage);
    });

    Documenti documentiDaDuplicare = preparaDuplicazione(praticaDa.getId(), idPraticaA, true);

    var asyncTask = asyncTaskService.start("Duplicazione documenti", t -> {
      logger.debug(methodName, "Duplicazione documenti");

      RelazioniDocumentoDuplicato docDaDuplicareDocNuovo = new RelazioniDocumentoDuplicato();

      for (int i = 0; i < documentiDaDuplicare.getDocumenti().size(); i++) {
        int count = i;

        t.step(
            "Duplicazione documento " + (count + 1) + " di "
                + documentiDaDuplicare.getDocumenti().size(),
                step -> {
                  logger.debug(methodName, "Duplicazione documento " + (count + 1) + " di "
                      + documentiDaDuplicare.getDocumenti().size());

                  var response = duplicaDocumento(idPraticaA,
                      documentiDaDuplicare.getDocumenti().get(count).getId(), docDaDuplicareDocNuovo);

                  response.getRelazioneDocumenti().stream().forEach(rel -> {
                    if (docDaDuplicareDocNuovo.getRelazioneDocumenti().stream().noneMatch(
                        p -> p.getIdDocumentoDaDuplicare().equals(rel.getIdDocumentoDaDuplicare())
                        && p.getIdDocumentoDuplicato().equals(rel.getIdDocumentoDuplicato()))) {
                      docDaDuplicareDocNuovo.getRelazioneDocumenti().add(rel);
                    }
                  });
                });
      }
      return null;
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(asyncTask.getTaskId());
    return output;
  }

  @Override
  public Documenti preparaDuplicazione(Long idPraticaDa, Long idPraticaA, Boolean returnDocs) {
    String methodName = "controlliPreliminariDuplicazione";

    CosmoTPratica praticaA =
        cosmoTPraticaRepository.findOneNotDeleted(idPraticaA).orElseThrow(() -> {
          String errorMessage = String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPraticaA);
          logger.error(methodName, errorMessage);
          throw new BadRequestException(errorMessage);
        });

    Documenti documenti = new Documenti();

    if (returnDocs != null && Boolean.TRUE.equals(returnDocs)) {
      List<Documento> documentiDaDuplicare =
          cosmoTDocumentoRepository.findAllNotDeleted((root, cq, cb) -> cq
              .where(cb.and(
                  cb.equal(root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id), idPraticaDa),
                  cb.equal(root.get(CosmoTDocumento_.stato).get(CosmoDStatoDocumento_.codice),
                      StatoDocumento.ELABORATO.toString())))
              .orderBy(cb.asc(root.get(CosmoTDocumento_.id))).getRestriction()).stream().map(m -> {
                Documento doc = new Documento();
                doc.setId(m.getId());
                return doc;
              }).collect(Collectors.toList());


      if (documentiDaDuplicare.isEmpty()) {
        logger.info(methodName, "Nessun documento da duplicare");
      }

      documenti.setDocumenti(documentiDaDuplicare);
    }

    verificaNodoPratica(praticaA);

    return documenti;
  }

  @Override
  public RelazioniDocumentoDuplicato duplicaDocumento(Long idPraticaA, Long idDocumentoDaDuplicare,
      RelazioniDocumentoDuplicato docDaDuplicareDocNuovo) {
    String methodName = "duplicaDocumento";

    CosmoTDocumento documentoDaDuplicare =
        cosmoTDocumentoRepository.findOne(idDocumentoDaDuplicare);

    if (documentoDaDuplicare == null) {
      String message = String.format("Documento con id %d non trovato", idDocumentoDaDuplicare);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    CosmoTPratica praticaA =
        cosmoTPraticaRepository.findOneNotDeleted(idPraticaA).orElseThrow(() -> {
          String errorMessage = String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPraticaA);
          logger.error(methodName, errorMessage);
          throw new BadRequestException(errorMessage);
        });

    if (docDaDuplicareDocNuovo.getRelazioneDocumenti().stream()
        .anyMatch(p -> documentoDaDuplicare.getId().equals(p.getIdDocumentoDaDuplicare()))) {
      logger.info(methodName, "Documento gia' duplicato");
      return docDaDuplicareDocNuovo;
    }

    if (documentoDaDuplicare.getDocumentoPadre() != null) {
      this.duplicaDocumento(idPraticaA, documentoDaDuplicare.getDocumentoPadre().getId(),
          docDaDuplicareDocNuovo);
    }

    List<CosmoTContenutoDocumento> contenutiDaDuplicare =
        cosmoTContenutoDocumentoRepository.findAllNotDeleted((root, cq, cb) -> {
          var predicate = cb.and(
              cb.equal(root.get(CosmoTContenutoDocumento_.documentoPadre), documentoDaDuplicare),
              root.get(CosmoTContenutoDocumento_.tipo).get(CosmoDTipoContenutoDocumento_.codice)
              .in(Arrays.asList(TipoContenutoDocumento.TEMPORANEO.toString())).not());

          return cq.where(predicate).orderBy(cb.asc(root.get(CosmoTContenutoDocumento_.id)))
              .getRestriction();
        });


    if (contenutiDaDuplicare.isEmpty()) {
      logger.info(methodName,
          "Non ci sono contenuti da duplicare quindi non si duplica il documento");
      return docDaDuplicareDocNuovo;
    }

    return creaNuovoDocumento(documentoDaDuplicare, praticaA, contenutiDaDuplicare,
        docDaDuplicareDocNuovo);

  }

  private CosmoTPratica verificaNodoPratica(CosmoTPratica pratica) {
    final var methodName = "verificaNodoPratica";

    if (StringUtils.isBlank(pratica.getUuidNodo())) {
      logger.info(methodName, "il nodo della pratica {} non esiste su index, lo creo",
          pratica.getId());

      try {
        if (pratica.getEnte() == null || pratica.getEnte().getCodiceIpa() == null
            || pratica.getEnte().getCodiceIpa().isBlank()) {
          String message = String.format("Ente della pratica %d non definito", pratica.getId());
          logger.error(methodName, message);
          throw new BadRequestException(message);
        }

        if (pratica.getDataCreazionePratica() == null) {
          String message =
              String.format("Data creazione della pratica %d non definita", pratica.getId());
          logger.error(methodName, message);
          throw new BadRequestException(message);
        }

        String path =
            pratica.getEnte().getCodiceIpa() + PATH_SEPARATOR + pratica.getDataCreazionePratica()
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                + PATH_SEPARATOR + pratica.getId();

        var praticaFolderUUID = indexFeignClient.createFolder(path);

        logger.info(methodName,
            "il nodo della pratica {} e' stato creato al percorso {} con uuid {}", pratica.getId(),
            path, praticaFolderUUID);

        pratica.setUuidNodo(praticaFolderUUID);
        pratica = cosmoTPraticaRepository.save(pratica);
      } catch (Exception e) {
        String message = "Errore nella creazione del nodo su index per la pratica "
            + pratica.getId() + ": " + e.getMessage();
        logger.error(methodName, message, e);
        throw new UnexpectedResponseException(message, e);
      }
    }
    return pratica;
  }

  private RelazioniDocumentoDuplicato creaNuovoDocumento(CosmoTDocumento documentoDaDuplicare,
      CosmoTPratica pratica, List<CosmoTContenutoDocumento> contenutiDaDuplicare,
      RelazioniDocumentoDuplicato docDaDuplicareDocNuovo) {
    String methodName = "creaNuovoDocumento";

    CosmoTDocumento output = creaDocumento(documentoDaDuplicare, pratica, docDaDuplicareDocNuovo);
    output = cosmoTDocumentoRepository.save(output);

    RelazioneDocumentoDuplicato rel = new RelazioneDocumentoDuplicato();
    rel.setIdDocumentoDaDuplicare(documentoDaDuplicare.getId());
    rel.setIdDocumentoDuplicato(output.getId());
    docDaDuplicareDocNuovo.getRelazioneDocumenti().add(rel);

    logger.info(methodName, "Documento " + documentoDaDuplicare.getId()
    + " duplicato. Il nuovo documento ha id: " + output.getId());

    duplicaContenuti(output, contenutiDaDuplicare, pratica);

    return docDaDuplicareDocNuovo;
  }

  private CosmoTDocumento creaDocumento(CosmoTDocumento documentoDaDuplicare, CosmoTPratica pratica,
      RelazioniDocumentoDuplicato docDaDuplicareDocNuovo) {
    CosmoTDocumento output = new CosmoTDocumento();

    output.setAutore(documentoDaDuplicare.getAutore());
    output.setDescrizione(documentoDaDuplicare.getDescrizione());
    output.setIdDocumentoExt(documentoDaDuplicare.getIdDocumentoExt());
    output.setPratica(pratica);
    output.setTitolo(documentoDaDuplicare.getTitolo());

    output.setStato(cosmoDStatoDocumentoRepository.findOne(StatoDocumento.ELABORATO.toString()));

    CosmoDTipoDocumento tipoDocumento = cosmoDTipoDocumentoRepository.findOne((root, cq, cb) -> {
      ListJoin<CosmoDTipoDocumento, CosmoTDocumento> tipoDocumentoDocumento =
          root.join(CosmoDTipoDocumento_.cosmoTDocumentos, JoinType.INNER);
      return cb.and(
          cb.equal(tipoDocumentoDocumento.get(CosmoTDocumento_.id), documentoDaDuplicare.getId()));
    });
    output.setTipo(tipoDocumento);

    if (documentoDaDuplicare.getDocumentoPadre() != null) {
      RelazioneDocumentoDuplicato idDocumentoPadreNuovo =
          docDaDuplicareDocNuovo.getRelazioneDocumenti().stream()
          .filter(p -> p.getIdDocumentoDaDuplicare()
              .equals(documentoDaDuplicare.getDocumentoPadre().getId()))
          .findAny().orElse(null);

      if (idDocumentoPadreNuovo != null) {
        Optional<CosmoTDocumento> documentoPadreNuovo =
            cosmoTDocumentoRepository
            .findOneNotDeleted(idDocumentoPadreNuovo.getIdDocumentoDuplicato());
        if (documentoPadreNuovo.isPresent()) {
          output.setDocumentoPadre(documentoPadreNuovo.get());
          output.setIdDocParentExt(documentoPadreNuovo.get().getIdDocumentoExt());
        }
      }
    }

    return output;
  }

  private List<CosmoTContenutoDocumento> duplicaContenuti(CosmoTDocumento documentoNuovo,
      List<CosmoTContenutoDocumento> contenutiDaDuplicare, CosmoTPratica pratica) {
    String methodName = "DuplicaContenuti";

    List<CosmoTContenutoDocumento> output = new ArrayList<>();

    contenutiDaDuplicare.forEach(contenutoDaDuplicare -> {

      String contenutoDuplicato =
          indexFeignClient.copiaNodo(contenutoDaDuplicare.getUuidNodo(), pratica.getUuidNodo());

      logger.info(methodName, "Contenuto " + contenutoDaDuplicare.getUuidNodo()
      + " duplicato su index al nodo " + pratica.getUuidNodo());

      CosmoTContenutoDocumento nuovoContenuto = new CosmoTContenutoDocumento();
      if (TipoContenutoDocumento.SBUSTATO.toString()
          .equals(contenutoDaDuplicare.getTipo().getCodice())) {
        var contOriginale = output.stream()
            .filter(
                c -> TipoContenutoDocumento.ORIGINALE.toString().equals(c.getTipo().getCodice()))
            .findFirst().orElse(null);
        nuovoContenuto.setContenutoSorgente(contOriginale);
      } else if (TipoContenutoDocumento.FIRMATO.toString()
          .equals(contenutoDaDuplicare.getTipo().getCodice())) {
        var cont = output.stream()
            .sorted(Comparator.comparing(CosmoTContenutoDocumento::getId).reversed())
            .filter(c -> TipoContenutoDocumento.ORIGINALE.toString().equals(c.getTipo().getCodice())
                || TipoContenutoDocumento.FIRMATO.toString().equals(c.getTipo().getCodice()))
            .findFirst().orElse(null);
        nuovoContenuto.setContenutoSorgente(cont);
      }

      nuovoContenuto.setDimensione(contenutoDaDuplicare.getDimensione());
      nuovoContenuto.setDocumentoPadre(documentoNuovo);
      nuovoContenuto.setFormatoFile(contenutoDaDuplicare.getFormatoFile());
      nuovoContenuto.setNomeFile(contenutoDaDuplicare.getNomeFile());
      nuovoContenuto.setTipo(contenutoDaDuplicare.getTipo());
      nuovoContenuto.setShaFile(contenutoDaDuplicare.getShaFile());
      nuovoContenuto.setUuidNodo(contenutoDuplicato);

      ShareOptions shareOptions = new ShareOptions();
      shareOptions.setContentDisposition(IndexContentDisposition.INLINE.name());
      shareOptions.setFilename(nuovoContenuto.getNomeFile());
      shareOptions.setSource(IndexShareScope.INTERNET.name());

      CondivisioniRequest request = new CondivisioniRequest();
      request.setSourceIdentifier(contenutoDuplicato);
      request.setOptions(shareOptions);

      var share = indexFeignClient.share(request);

      nuovoContenuto.setUrlDownload(share.getDownloadUri());

      nuovoContenuto = cosmoTContenutoDocumentoRepository.save(nuovoContenuto);

      if (TipoContenutoDocumento.ORIGINALE.toString()
          .equals(contenutoDaDuplicare.getTipo().getCodice())
          && !contenutoDaDuplicare.getInfoVerificaFirme().isEmpty()) {
        duplicaFirma(contenutoDaDuplicare, nuovoContenuto);
        nuovoContenuto.setDataVerificaFirma(contenutoDaDuplicare.getDataVerificaFirma());
        nuovoContenuto.setEsitoVerificaFirma(contenutoDaDuplicare.getEsitoVerificaFirma());
        nuovoContenuto.setTipoFirma(contenutoDaDuplicare.getTipoFirma());
        CosmoDTipoContenutoFirmato tipoContenutoFirmato = new CosmoDTipoContenutoFirmato();
        tipoContenutoFirmato.setCodice(TipoContenutoFirmato.FIRMA_DIGITALE.getCodice());
        nuovoContenuto.setTipoContenutoFirmato(tipoContenutoFirmato);
        nuovoContenuto = cosmoTContenutoDocumentoRepository.save(nuovoContenuto);
        logger.info(methodName, "Contenuto " + contenutoDaDuplicare.getId() + " duplicato con id "
            + nuovoContenuto.getId());
      }

      if (!contenutoDaDuplicare.getAnteprime().isEmpty()) {
        duplicaAnteprime(contenutoDaDuplicare.getAnteprime(), pratica, nuovoContenuto);
      }
      output.add(nuovoContenuto);

    });
    return output;
  }

  private void duplicaFirma(
      CosmoTContenutoDocumento contenutoDaDuplicare, CosmoTContenutoDocumento nuovoContenuto) {

    List<CosmoTInfoVerificaFirma> firmeDaDuplicare =
        cosmoTInfoVerificaFirmaRepository.findNotDeletedByField(
            CosmoTInfoVerificaFirma_.contenutoDocumentoPadre, contenutoDaDuplicare);

    if (firmeDaDuplicare.isEmpty()) {
      return;
    }

    firmeDaDuplicare.forEach(firmaDaDuplicare -> {
      CosmoTInfoVerificaFirma firma = duplicaFirma(firmaDaDuplicare, nuovoContenuto);

      if (firmaDaDuplicare.getInfoVerificaFirmaPadre() != null) {
        CosmoTInfoVerificaFirma firmaPadre =
            duplicaFirma(firmaDaDuplicare.getInfoVerificaFirmaPadre(), nuovoContenuto);
        firma.setInfoVerificaFirmaPadre(firmaPadre);
      }

      firma = cosmoTInfoVerificaFirmaRepository.save(firma);
    });

  }

  private CosmoTInfoVerificaFirma duplicaFirma(CosmoTInfoVerificaFirma input,
      CosmoTContenutoDocumento contenuto) {

    CosmoTInfoVerificaFirma output = new CosmoTInfoVerificaFirma();

    output.setCfFirmatario(input.getCfFirmatario());
    output.setCodiceErrore(input.getCodiceErrore());
    output.setDtApposizione(input.getDtApposizione());
    output.setDtApposizioneMarcaturaTemporale(input.getDtApposizioneMarcaturaTemporale());
    output.setDtVerificaFirma(input.getDtVerificaFirma());
    output.setEsito(input.getEsito());
    output.setFirmatario(input.getFirmatario());
    output.setOrganizzazione(input.getOrganizzazione());
    output.setContenutoDocumentoPadre(contenuto);

    return output;
  }

  private void duplicaAnteprime(List<CosmoTAnteprimaContenutoDocumento> anteprimeDaDuplicare,
      CosmoTPratica pratica, CosmoTContenutoDocumento nuovoContenuto) {

    var folder = indexFeignClient.findFolder(pratica.getUuidNodo());

    String path = folder.getEffectivePath() + "/anteprime";

    var anteprimaFolder = indexFeignClient.findFolder(path);
    String anteprimaFolderUUID;
    if (anteprimaFolder == null) {
      anteprimaFolderUUID = indexFeignClient.createFolder(path);
    } else {
      anteprimaFolderUUID = anteprimaFolder.getUid();
    }

    anteprimeDaDuplicare.forEach(anteprimaDaDuplicare -> {

      CosmoTAnteprimaContenutoDocumento anteprima = new CosmoTAnteprimaContenutoDocumento();
      anteprima.setContenuto(nuovoContenuto);
      anteprima.setDimensione(anteprimaDaDuplicare.getDimensione());
      anteprima.setFormatoFile(anteprimaDaDuplicare.getFormatoFile());
      anteprima.setNomeFile(anteprimaDaDuplicare.getNomeFile());

      var anteprimaDaDuplicareSuIndex =
          indexFeignClient.getFile(anteprimaDaDuplicare.getUuidNodo(), true);

      ContenutoEntity contenuto = new ContenutoEntity();
      contenuto.setEntity(anteprimaDaDuplicareSuIndex);
      contenuto.setContent(null);
      var createdOnIndex = indexFeignClient.creaFile(anteprimaFolderUUID, contenuto);

      ShareOptions options = new ShareOptions();
      options.setFilename(anteprima.getNomeFile());
      options.setSource(IndexShareScope.INTERNET.name());

      CondivisioniRequest request = new CondivisioniRequest();
      request.setOptions(options);
      request.setEntity(createdOnIndex);
      var share = indexFeignClient.share(request);

      anteprima.setShareUrl(share.getDownloadUri());
      anteprima.setUuidNodo(createdOnIndex.getUid());

      cosmoTAnteprimaContenutoDocumentoRepository.save(anteprima);
    });
  }


}
