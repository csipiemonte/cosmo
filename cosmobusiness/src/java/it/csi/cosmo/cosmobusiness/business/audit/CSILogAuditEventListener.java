/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.business.service.AuditService;
import it.csi.cosmo.cosmobusiness.business.service.impl.AuditServiceImpl;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */

public class CSILogAuditEventListener {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "CSILogAuditEventListener");

  private AuditService auditService;

  private synchronized AuditService getService() {
    if (this.auditService == null) {
      try {
        this.auditService = (AuditService) SpringApplicationContextHelper
            .getBean(AuditServiceImpl.class.getSimpleName());
      } catch (Exception e) {
        logger.warn("getService",
            "error retrieving audit service (context may not be ready): " + e.getMessage());
        return null;
      }
    }
    return this.auditService;
  }

  @PrePersist
  public void prePersist(Object target) {
    var service = getService();
    if (service != null) {
      service.prePersist(target);
    }
  }

  @PreUpdate
  public void preUpdate(Object target) {
    var service = getService();
    if (service != null) {
      service.preUpdate(target);
    }
  }

  @PreRemove
  public void preRemove(Object target) {
    var service = getService();
    if (service != null) {
      service.preRemove(target);
    }
  }

}
