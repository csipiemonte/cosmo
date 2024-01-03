/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RichiestaOTPRequest;

public interface FirmaService {

  /**
   * Metodo che richiede l'OTP via SMS
   *
   * @param request
   */
  void richiediOTP(RichiestaOTPRequest request);

  /**
   * Metodo che esegue la firma digitale su uno o piu' documenti di una data pratica
   *
   * @param firmaRequest
   */
  void firma(FirmaRequest firmaRequest);

  /**
   * Metodo che esegue l'apposizione del sigillo elettronico su uno o piu' documenti di una data
   * pratica
   *
   * @param documento
   * @param alias
   */
  EsitoRichiestaSigilloElettronicoDocumento apponiSigilloElettronico(CosmoTDocumento documento, String alias);

}
