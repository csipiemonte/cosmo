/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.batch.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.enums.StatoInvioNotificaMail;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.mail.model.MailStatus;
import it.csi.cosmo.cosmonotifications.business.batch.InvioNotificheMailBatch;
import it.csi.cosmo.cosmonotifications.business.service.LockService;
import it.csi.cosmo.cosmonotifications.business.service.MailService;
import it.csi.cosmo.cosmonotifications.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmonotifications.config.ParametriApplicativo;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRNotificaUtenteEnteRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRUtenteEnteRepository;

/**
 *
 */
@Service
public class InvioNotificheMailBatchImpl extends ParentBatchImpl implements InvioNotificheMailBatch {
  
  private static final String JOB_LOCK_RESOURCE_CODE = "INVIO_NOTIFICHE_EMAIL_JOB_LOCK";

  @Autowired
  private CosmoRNotificaUtenteEnteRepository cosmoRNotificaUtenteEnteRepository;

  @Autowired
  private CosmoRUtenteEnteRepository cosmoRUtenteEnteRepository;

  @Autowired
  protected MailService mailService;
  
  @Autowired
  private LockService lockService;

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_INVIO_NOTIFICHE_MAIL_ENABLE).asBool();
  }


  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void execute(BatchExecutionContext context) {
    String method = "execute";

    logger.info(method, "esecuzione batch di invio notifiche mail");
    
    this.invioNotificaMail();
    
    logger.info(method, "termine batch di invio notifiche mail");
  }

  private List<CosmoRNotificaUtenteEnte> invioNotificaMail() {
  //@formatter:off
    return this.lockService.executeLocking(lock -> invioNotificaMailLock(lock),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
    
  }
  
  private List<CosmoRNotificaUtenteEnte> invioNotificaMailLock(CosmoTLock lock) {
    
    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di invio notifica mail senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di invio notifica mail con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }
    
    List<CosmoRNotificaUtenteEnte> daInviare = getNotificheDaInviare();
    if (daInviare.isEmpty()) {
      return daInviare;
    }

    for (CosmoRNotificaUtenteEnte singoloInvio : daInviare) {
      inTransaction(() -> elaboraInvioNotificaMail(singoloInvio));
    }
    return daInviare;
}


  /**
   * ricerca tutte le notifiche da inviare via email in stato DA_INVIARE o FALLITO per ognuno tenta
   * o ritenta l'invio e aggiorna lo stato a INVIATO o FALLITO
   *
   * @return la lista di notifiche, utenti ed enti da notificare via mail
   */
  protected List<CosmoRNotificaUtenteEnte> getNotificheDaInviare() {
    final var method = "getNotificheDaInviare";
    List<String> statiInvioMail = List.of(StatoInvioNotificaMail.DA_INVIARE.name(),
        StatoInvioNotificaMail.FALLITO.name());
    List<CosmoRNotificaUtenteEnte> users = cosmoRNotificaUtenteEnteRepository.findByInvioMailAndStatoInvioMailIn(true, statiInvioMail);

    if (!users.isEmpty()) {
      logger.info(method, "trovate {} notifiche da inviare via mail", users.size());
    } else {
      logger.debug(method, "nessuna notifica da inviare via mail");
    }

    return users;
  }


  /**
   * elabora l'invio delle notifiche via mail
   *
   * @param
   */
  protected void elaboraInvioNotificaMail(CosmoRNotificaUtenteEnte user) {
    final var method = "elaboraInvioNotificaMail";
    logger.info(method, "start()");
    String subject = user.getCosmoTEnte().getNome();
    String text = user.getCosmoTNotifica().getDescrizione();
    String recipient = cosmoRUtenteEnteRepository
        .findByCosmoTEnteAndCosmoTUtente(
          user.getCosmoTEnte(), user.getCosmoTUtente())
      .getEmail();
    try {
      Future<CosmoMailDeliveryResult> result = mailService.inviaNotifiche(subject, text, recipient);
      if (result.get().getStatus().name().equals(MailStatus.SENT.name())) {
        logger.info(method, "invio mail avvenuto con successo");
        user.setStatoInvioMail(StatoInvioNotificaMail.INVIATO.name());
      } else {
        logger.info(method, "invio mail fallito: ", result.get().getStatus().name());
        user.setStatoInvioMail(StatoInvioNotificaMail.FALLITO.name());
      }
    } catch (InterruptedException e) {
      logger.info(method, "invio mail fallito: ", e.getMessage());
      user.setStatoInvioMail(StatoInvioNotificaMail.FALLITO.name());
    } catch (ExecutionException e) {
      logger.info(method, "invio mail fallito: ", e.getMessage());
      user.setStatoInvioMail(StatoInvioNotificaMail.FALLITO.name());
      }
      cosmoRNotificaUtenteEnteRepository.save(user);
      logger.info(method, "end()");
  }

}
