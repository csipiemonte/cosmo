/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.batch.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.cosmopratiche.business.batch.CaricamentoPraticheBatch;
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticheBatchService;
import it.csi.cosmo.cosmopratiche.business.service.EventService;
import it.csi.cosmo.cosmopratiche.business.service.LockService;
import it.csi.cosmo.cosmopratiche.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;

@Service
public class CaricamentoPraticheBatchImpl
extends ParentBatchImpl implements CaricamentoPraticheBatch {



  @Autowired
  private CaricamentoPraticheBatchService caricamentoPraticheBatchService;

  @Autowired
  EventService eventService;
  
  @Autowired
  private LockService lockService;

  private String result = "";
  
  public static final String JOB_LOCK_RESOURCE_CODE = "CARICAMENTO_PRATICHE_JOB_LOCK";

  @Override
  public boolean isEnabled() {

    return configurazioneService != null
        && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_CARICAMENTOPRATICHE_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public String execute(BatchExecutionContext context) {

    String method = "execute";
    logger.info(method, "inizio batch caricamento pratiche");
    
    this.lockCaricamentoPratiche();

    logger.info(method, "fine batch caricamento pratiche");
    return result;
  }

  private List<CosmoTCaricamentoPratica> lockCaricamentoPratiche() {
  //@formatter:off
    return this.lockService.executeLocking(lock -> caricamentoPraticheInsideLock(lock),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
    
  }
  
  private List<CosmoTCaricamentoPratica> caricamentoPraticheInsideLock(CosmoTLock lock) {
    
    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di caricamento pratiche senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di caricamento pratiche con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }
    
    List<CosmoTCaricamentoPratica> listCaricamentoPratiche =
        this.caricamentoPraticheBatchService.getCaricamentoPraticheWithCaricamentoCompletato();


    listCaricamentoPratiche.stream().forEach(temp -> {
      boolean erroreElaborazione = false;

      try {
        this.caricamentoPraticheBatchService.iniziaElaborazione(temp);
        this.caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(temp);
      } catch (Exception e) {
        erroreElaborazione = true;
        this.caricamentoPraticheBatchService.registraErroreGenerico(temp, e.getMessage());
        result = "ERROR";
      }
      finally{
        if (!erroreElaborazione)
          this.caricamentoPraticheBatchService.completaElaborazione(temp);
        result = "OK";
      }

    });
    
    return listCaricamentoPratiche;
    
  }
}
