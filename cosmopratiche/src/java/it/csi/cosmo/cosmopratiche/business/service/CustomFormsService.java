/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;

/**
 *
 */

public interface CustomFormsService {

  void deleteCustomForm(String codice);

  CustomFormResponse getCustomForms(String filter);

  CustomForm getCustomForm(String codice);

  CustomForm postCustomForm(CustomForm body);

  CustomForm putCustomForm(String codice, CustomForm body);

  CustomForm getCustomFormFromCodiceTipoPratica(String codiceTipoPratica);
}
