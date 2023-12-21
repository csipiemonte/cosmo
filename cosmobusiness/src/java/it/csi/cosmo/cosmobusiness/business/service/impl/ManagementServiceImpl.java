/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.flowable.rest.service.api.RestActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ManagementService;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJob;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobsResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadletterJobResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnManagementFeignClient;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  public static final String MAX_PAGE_SIZE = "1000";

  public static final String DUE_DATE_SORT = "dueDate";

  public static final String LINK_PRATICHE = "/pratiche/";
  @Autowired
  private CosmoCmmnManagementFeignClient cosmoCmmnManagementFeignClient;

  @Autowired
  private CosmoTPraticaRepository praticaRepository;

  @Autowired
  private CosmoTEnteRepository enteRepository;

  @Override
  public DeadLetterJobsResponse getDeadLetterJobs() {

    logger.info("getDeadLetterJobs",
        "Ricerca di deadletterjob per un massimo di " + MAX_PAGE_SIZE + "page");
    List<DeadLetterJob> result = new LinkedList<>();

    DeadletterJobResponseWrapper allDLJ = cosmoCmmnManagementFeignClient.getDeadletterJobs(null,
        null, null, DUE_DATE_SORT, MAX_PAGE_SIZE);

    var processIstanceIds = allDLJ.getData().stream()
        .map(data -> LINK_PRATICHE + data.getProcessInstanceId()).collect(Collectors.toList());

    if (processIstanceIds != null && !processIstanceIds.isEmpty()) {
      var pratiche = praticaRepository.findAllNotDeleted(
          (root, cq, cb) -> root.get(CosmoTPratica_.linkPratica).in(processIstanceIds));

      allDLJ.getData().forEach(data -> {

        CosmoTPratica pratica = pratiche.stream()
            .filter(p -> p.getLinkPratica().equals(LINK_PRATICHE + data.getProcessInstanceId()))
            .findFirst().orElse(null);

        DeadLetterJob job = new DeadLetterJob();
        job.setId(data.getId());
        job.setTentativi(Long.valueOf(data.getRetries()));
        job.setInfo(data.getExceptionMessage());
        job.setNomeJob(data.getElementName());

        ZoneOffset zoneOffSet = ZoneId.of("Europe/Berlin").getRules()
            .getOffset(LocalDateTime.ofInstant(data.getDueDate().toInstant(), ZoneOffset.UTC));
        job.setData(data.getDueDate().toInstant().atOffset(zoneOffSet));


        if (pratica != null) {

          job.setDescrizioneEnte(pratica.getEnte().getNome());
          job.setTipoPratica(pratica.getTipo().getDescrizione());
          job.setOggettoPratica(pratica.getOggetto());

        } else {

          logger.info("getDeadLetterJobs", "Nessun pratica collegata al job: " + data.getId());
          CosmoTEnte ente = enteRepository
              .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, data.getTenantId()).orElse(null);
          if (ente != null) {
            job.setDescrizioneEnte(ente.getNome());
          }

        }
        result.add(job);
      });
    }

    logger.info("getDeadLetterJobs", "Deadletter jobs identificati: " + result.size());

    DeadLetterJobsResponse output = new DeadLetterJobsResponse();
    output.setDeadLetterJobs(result);

    return output;

  }

  @Override
  public void moveDeadLetterJob(String jobId, DeadLetterJobAction action) {
    ValidationUtils.validaAnnotations(action);

    RestActionRequest request = new RestActionRequest();
    request.setAction(action.getAction());
    cosmoCmmnManagementFeignClient.moveDeadletterJobs(jobId, request);

    logger.info("moveDeadLetterJob", "Tentativo di mandare avanti il deadletterjob: " + jobId);
  }
}
