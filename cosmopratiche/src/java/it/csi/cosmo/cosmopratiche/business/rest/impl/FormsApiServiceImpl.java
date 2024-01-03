/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.FormsApi;
import it.csi.cosmo.cosmopratiche.business.service.FormsService;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;

@SuppressWarnings("unused")
public class FormsApiServiceImpl extends ParentApiImpl implements FormsApi {

  @Autowired
  FormsService s;

  @Override
  public Response getFormsNome(String nome, SecurityContext securityContext) {
    var strutturaFormLogico = this.s.recuperaStrutturaDaNome(nome);
    return Response.ok(strutturaFormLogico).build();
  }

  @Override
  public Response getFormDefinitionFormKey(String formKey, SecurityContext securityContext) {
    SimpleForm response = this.s.getFormDefinitionFormKey(formKey);
    return Response.ok(response).build();
  }

  @Override
  public Response getFormsAttivitaId(String id, SecurityContext securityContext) {
    var strutturaFormLogico = this.s.recuperaStrutturaDaIdAttivita(id);
    return Response.ok(strutturaFormLogico).build();
  }

}
