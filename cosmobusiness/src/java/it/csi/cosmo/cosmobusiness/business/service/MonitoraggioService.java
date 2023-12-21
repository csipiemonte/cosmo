/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.util.List;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

public interface MonitoraggioService {

  List<Pratica> monitoraggioPratiche(BatchExecutionContext context);
}
