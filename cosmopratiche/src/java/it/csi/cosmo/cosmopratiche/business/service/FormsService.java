/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;

public interface FormsService {
  public StrutturaFormLogico recuperaStrutturaDaNome(String nome);

  /**
   * @param idTask
   * @return
   */
  SimpleForm getPraticheTaskIdTaskForm(String idTask);

  /**
   * @param formKey
   * @return
   */
  public SimpleForm getFormDefinitionFormKey(String formKey);

  StrutturaFormLogico recuperaStrutturaDaIdAttivita(String id);
}
