/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import it.csi.cosmo.common.async.internal.ContextAwareCallable;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.enums.SceltaMarcaTemporale;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.common.util.ValoreParametroFormLogicoWrapper;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmoecm.business.service.LockService;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.Task;
import it.csi.cosmo.cosmoecm.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.integration.mapper.DosignMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapDosignFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;

/**
 *
 */
@Service
public class EsecuzioneMultiplaServiceImpl implements EsecuzioneMultiplaService {

  private static final int MAX_PARALLEL_EXECUTIONS = 1;

  int marcheTemporali = 0;

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private LockService lockService;

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;

  @Autowired
  private CosmoSoapDosignFeignClient dosignFeignClient;

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  @Autowired
  private DosignMapper dosignMapper;

  private static final String CONFIG_FIRMA_NOTE_FIRMA_KEY = "variabileNoteFirma";

  private static final String CONFIG_FIRMA_RISULTATO_FIRMA_KEY = "variabileRisultatoFirma";

  private static final String CONFIG_FIRMA_KEY = "O_FIRMA";


  @Override
  public void postEsecuzioneMultiplaFirma(
      EsecuzioneMultiplaFirmaRequest request) {

    String methodName = "postEsecuzioneMultiplaFirma";

    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    String profiloFEQ = request.getCertificato().getProfiloFEQ().getCodice();

    CosmoDEnteCertificatore enteCertificatore = cosmoDEnteCertificatoreRepository
        .findOneActive(request.getCertificato().getEnteCertificatore().getCodice())
        .orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.FIRMA_ENTE_CERTIFICATOR_NON_PRESENTE_O_NON_ATTIVO,
              request.getCertificato().getEnteCertificatore().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });


    boolean marcaTemporale = false;


    List<it.csi.cosmo.cosmosoap.dto.rest.AttivitaEseguibileMassivamente> aem =
        dosignMapper.toAttvitaEseguibileMassivamenteSoap(request.getTasks());

    List<it.csi.cosmo.cosmosoap.dto.rest.DocumentiTask> documentiTaskSoap =
        dosignMapper.toDocumentiTaskSoap(request.getDocumentiTask());


    try {


      String identificativoTransazione = java.util.UUID.randomUUID().toString();
      marcaTemporale = request.getCertificato().getSceltaMarcaTemporale().getCodice()
          .equals(SceltaMarcaTemporale.SI.toString());


      DoSignFirmaMassivaRequest doSignFirmaMassivaRequest = new DoSignFirmaMassivaRequest();
      doSignFirmaMassivaRequest.setAlias(request.getCertificato().getUsername());
      doSignFirmaMassivaRequest.setPin(request.getCertificato().getPin());
      doSignFirmaMassivaRequest.setOtp(request.getOtp());
      doSignFirmaMassivaRequest.setUuidTransaction(identificativoTransazione);
      doSignFirmaMassivaRequest.setCodiceCa(enteCertificatore.getCodiceCa());
      doSignFirmaMassivaRequest.setCodiceTsa(enteCertificatore.getCodiceTsa());
      doSignFirmaMassivaRequest.setProfiloFEQ(profiloFEQ);
      doSignFirmaMassivaRequest.setMarcaTemporale(marcaTemporale);
      doSignFirmaMassivaRequest.setNotificaFirma(true);
      doSignFirmaMassivaRequest.setNote(request.getNote());
      doSignFirmaMassivaRequest.setTasks(aem);
      doSignFirmaMassivaRequest.setDocumentiDaFirmare(documentiTaskSoap);
      doSignFirmaMassivaRequest.setProcessCarryOn(request.isMandareAvantiProcesso());
      doSignFirmaMassivaRequest.setPassword(request.getCertificato().getPassword());
      doSignFirmaMassivaRequest.setCodiceEnteCertificatore(enteCertificatore.getCodice());
      doSignFirmaMassivaRequest.setProvider(enteCertificatore.getProvider());


      dosignFeignClient.firmaMassiva(doSignFirmaMassivaRequest);
    } catch (Exception e) {
      String error =
          e.getMessage() != null ? e.getCause() + " " + e.getMessage() : ErrorMessages.FIRMA_ERRORE;
      logger.error(methodName, error);
      throw new InternalServerException(error);
    }

  }

  @Override
  public RiferimentoOperazioneAsincrona postEsecuzioneMultiplaRifiutoFirma(
      EsecuzioneMultiplaRifiutoFirmaRequest request) {

    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    var resultCollector = new ConcurrentHashMap<Long, EsitoAttivitaEseguibileMassivamente>();

    var async = asyncTaskService.start("Rifiuto firma multiplo", task -> {

      var executor = Executors
          .newFixedThreadPool(Math.min(request.getTasks().size(), MAX_PARALLEL_EXECUTIONS));

      // submit di subtask all'executor per l'esecuzione parallela
      for (AttivitaEseguibileMassivamente t : request.getTasks()) {
        var callable = new ContextAwareCallable<Object>(() -> task.step(
            "Rfiuto Firma Documenti Pratica" + " (" + t.getPratica().getOggetto() + ")", step -> {
              EsitoAttivitaEseguibileMassivamente esito = new EsitoAttivitaEseguibileMassivamente();
              esito.task = t;
              try {
                rifiutoFirmaMassivo(request, t, step);
                esito.successo = true;
              } catch (Throwable terr) { // NOSONAR
                esito.successo = false;
                esito.errore = terr;
              } finally {
                resultCollector.put(t.getAttivita().getId().longValue(), esito);
              }
              return null;
            }), getCurrentRequestAttributes());
        executor.submit(callable);
      }

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(Math.max(300, request.getTasks().size() * 60),
            TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        task.warn(
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }

      // verifica risultati da resultCollector
      return ObjectUtils.toJson(resultCollector.values());
    });
    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(async.getTaskId());
    return output;

  }

  public static class EsitoAttivitaEseguibileMassivamente {
    AttivitaEseguibileMassivamente task;
    Boolean successo;
    Throwable errore;

    public AttivitaEseguibileMassivamente getTask() {
      return task;
    }

    public Boolean getSuccesso() {
      return successo;
    }

    public Throwable getErrore() {
      return errore;
    }

  }

  protected Object variable(String name, String value) {
    var output = new HashMap<String, Object>();
    output.put("name", name);
    output.put("value", value);
    return output;
  }

  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private Map<String, ValoreParametroFormLogicoWrapper> getValoriParametri(
      CosmoTIstanzaFunzionalitaFormLogico istanza) {

    //@formatter:off
    return istanza.getCosmoRIstanzaFormLogicoParametroValores().stream()
        .filter(CosmoREntity::valido)
        .map(ValoreParametroFormLogicoWrapper::new)
        .collect(Collectors.toMap(ValoreParametroFormLogicoWrapper::getCodice, e -> e));
    //@formatter:on
  }

  private void rifiutoFirmaMassivo(EsecuzioneMultiplaRifiutoFirmaRequest request,
      AttivitaEseguibileMassivamente task, LongTask<Serializable> step) {

    var attivita = cosmoTAttivitaRepository.findOne(task.getAttivita().getId().longValue());

    String lockResourceCode = "@task(" + attivita.getTaskId() + ")";

    String lockOwner = UUID.randomUUID().toString();

    CosmoTLock[] locks = {null};

    step.step("Acquisizione del lock", substep -> {

      var lockRequest = new LockAcquisitionRequest();
      lockRequest.codiceOwner = lockOwner;
      lockRequest.codiceRisorsa = lockResourceCode;
      lockRequest.durata = 300000L;
      lockRequest.ritardoRetry = 250L;
      lockRequest.timeout = 5000L;

      var lockAcquisitionResult = lockService.acquire(lockRequest);

      if (lockAcquisitionResult.acquired) {
        locks[0] = lockAcquisitionResult.lock;
      } else {
        throw new ConflictException(lockAcquisitionResult.reason);
      }

      substep.sleep(500);
      return null;
    });

    try {
      step.step("Elaborazione del task", substep -> {
        rifiutoFirmaMassivoInLock(request, task, attivita, locks[0].getCodiceOwner());
      });
    } finally {
      step.step("Rilascio del lock", substep -> {
        lockService.release(locks[0]);
      });
    }
  }

  private void rifiutoFirmaMassivoInLock(EsecuzioneMultiplaRifiutoFirmaRequest request,
      AttivitaEseguibileMassivamente task, CosmoTAttivita attivita,
      String codiceOwner) {

    Long idIstanza = task.getFunzionalita().getId();

    // leggo la configurazione per sapere in che variabili salvare il risultato

    var istanza = cosmoTIstanzaFunzionalitaFormLogicoRepository.findOne(idIstanza);

    var parametri = getValoriParametri(istanza);

    var outputFirma = parametri.get(CONFIG_FIRMA_KEY).asObject();

    var lavorazioneRequest = new Task();
    List<Object> variables = new ArrayList<>();
    lavorazioneRequest.setVariables(variables);

    variables.add(
        variable(outputFirma.get(CONFIG_FIRMA_RISULTATO_FIRMA_KEY)
            .asText(),
            String.valueOf(false)));
    variables
    .add(variable(outputFirma.get(CONFIG_FIRMA_NOTE_FIRMA_KEY)
        .asText(),
        request.getNote().strip()));


    //@formatter:off
    RiferimentoOperazioneAsincrona riferimento = cosmoBusinessPraticheFeignClient.postPraticaAttivitaConfermaConLock(
        task.getPratica().getId().longValue(),
        attivita.getId(),
        lavorazioneRequest,
        codiceOwner
        );

    asyncTaskService.wait(riferimento.getUuid(), 600L);
    //@formatter:on
  }


}
