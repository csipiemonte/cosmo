/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.business.service.SchedulerService;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 * Servizio per la gestione delle operazioni schedulate
 *
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "SchedulerServiceImpl");

  @Scheduled(cron = "0 30 1 * * ?")
  public void everyNight() {
    logger.info("everyNight", "executing scheduled operation for the night");
  }
}
