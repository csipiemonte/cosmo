/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.util.List;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoIstanzaFunzionalitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;

public interface FormLogiciService {

  /**
   * @param filter
   * @return
   */
  FormLogiciResponse getFormLogici(String filter);

  /**
   * @param id
   * @return
   */
  FormLogico getFormLogico(Long id);

  /**
   * @param body
   * @return
   */
  FormLogico postFormLogici(CreaFormLogicoRequest body);

  /**
   * @param id
   * @param body
   * @return
   */
  FormLogico putFormLogici(Long id, AggiornaFormLogicoRequest body);

  /**
   * @param id
   */
  void deleteFormLogici(Long id);

  void setFunzionalitaMultipla(Long idFormLogico,
      List<AggiornaFormLogicoIstanzaFunzionalitaRequest> input,
      boolean esecuzioneMultipla);

}
