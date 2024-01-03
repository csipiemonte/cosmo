/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.GenerazioneReportResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public interface ReportService {

  /**
   * @param body
   * @return
   */
  GenerazioneReportResponse generaReportDaProcesso(RichiediGenerazioneReportRequest body);

  it.csi.cosmo.cosmoecm.dto.jasper.RisultatoGenerazioneReport generaReportDaProcessoInMemory(
      RichiediGenerazioneReportRequest body);

  /**
   * @param body
   * @return
   */
  RiferimentoOperazioneAsincrona generaReportAsincrono(RichiediGenerazioneReportRequest body);

}
