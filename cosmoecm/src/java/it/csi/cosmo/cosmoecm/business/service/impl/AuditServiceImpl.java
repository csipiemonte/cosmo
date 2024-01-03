/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.audit.CSILogAuditActionRegister;
import it.csi.cosmo.common.entities.CsiLogAudit;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmoecm.business.service.AuditService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.integration.repository.CsiLogAuditRepository;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;


@Service
@Transactional
public class AuditServiceImpl implements AuditService {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.AUDIT_LOG_CATEGORY, "AuditServiceImpl");

  @Autowired
  private CsiLogAuditRepository csiLogAuditRepository;

  @Override
  public void prePersist(Object target) {

    logger.beginForClass(AuditServiceImpl.class.getSimpleName(),
        "prePersist: " + target.getClass().getName());

    if (target instanceof CampiTecniciEntity) {
      ((CampiTecniciEntity) target).setDtInserimento(Timestamp.from(Instant.now()));
      ((CampiTecniciEntity) target).setUtenteInserimento(getPrincipalCode());
    }

    if (target instanceof CsiLogAuditedEntity) {
      CsiLogAudit dto = getAudit(CSILogAuditActionRegister.INSERT);

      dto.setOggOper(target.getClass().getSimpleName());
      dto.setKeyOper(getKey(target));

      save(dto);
    }

    logger.endForClass(AuditServiceImpl.class.getSimpleName(),
        "prePersist: " + target.getClass().getName());
  }

  @Override
  public void preUpdate(Object target) {

    logger.beginForClass(AuditServiceImpl.class.getSimpleName(),
        "preUpdate: " + target.getClass().getName());

    if (target instanceof CampiTecniciEntity) {
      ((CampiTecniciEntity) target).setDtUltimaModifica(Timestamp.from(Instant.now()));
      ((CampiTecniciEntity) target).setUtenteUltimaModifica(getPrincipalCode());
    }

    if (target instanceof CsiLogAuditedEntity) {
      CsiLogAudit dto = getAudit(CSILogAuditActionRegister.UPDATE);

      dto.setOggOper(target.getClass().getSimpleName());
      dto.setKeyOper(getKey(target));

      save(dto);
    }
    logger.endForClass(AuditServiceImpl.class.getSimpleName(),
        "preUpdate: " + target.getClass().getName());
  }

  @Override
  public void preRemove(Object target) {

    logger.beginForClass(AuditServiceImpl.class.getSimpleName(),
        "preRemove: " + target.getClass().getName());

    if (target instanceof CampiTecniciEntity) {
      ((CampiTecniciEntity) target).setDtCancellazione(Timestamp.from(Instant.now()));
      ((CampiTecniciEntity) target).setUtenteCancellazione(getPrincipalCode());
    }

    if (target instanceof CsiLogAuditedEntity) {
      CsiLogAudit dto = getAudit(CSILogAuditActionRegister.DELETE);

      dto.setOggOper(target.getClass().getSimpleName());
      dto.setKeyOper(getKey(target));

      save(dto);
    }

    logger.endForClass(AuditServiceImpl.class.getSimpleName(),
        "preRemove: " + target.getClass().getName());
  }

  private String getKey(Object o) {
    if (o == null) {
      return "null";
    } else if (o instanceof CsiLogAuditedEntity) {
      return ((CsiLogAuditedEntity) o).getPrimaryKeyRepresentation();
    } else {
      return String.valueOf(o);
    }
  }

  private CsiLogAudit getAudit(CSILogAuditActionRegister action) {
    CsiLogAudit dto = new CsiLogAudit();

    dto.setId(null);
    dto.setIdApp(Constants.COMPONENT_NAME);
    dto.setDataOra(Timestamp.valueOf(LocalDateTime.now()));

    Optional<String> ip = RequestUtils.extractIp(RequestUtils.getCurrentRequest().orElse(null));

    if (ip.isPresent()) {
      dto.setIpAddress(ip.get());
    }

    dto.setUtente(getPrincipalCode());
    dto.setOperazione(action.name());

    return dto;
  }

  private void save(CsiLogAudit raw) {
    if (logger.isDebugEnabled()) {
      logger.debug("save", "[AUDIT] saving audit entity " + ObjectUtils.represent(raw));
    }

    try {
      csiLogAuditRepository.save(raw);
    } catch (Exception e) {
      logger.error("save", "errore nel salvataggio della voce di audit", e);
      throw e;
    }
  }

  public static String getPrincipalCode() {
    var utenteCorrente = SecurityUtils.getUtenteCorrente();
    var clientCorrente = SecurityUtils.getClientCorrente();
    if (Boolean.FALSE.equals(utenteCorrente.getAnonimo())) {
      return utenteCorrente.getCodiceFiscale() + "@" + clientCorrente.getCodice();
    } else {
      return clientCorrente.getCodice();
    }
  }
}

