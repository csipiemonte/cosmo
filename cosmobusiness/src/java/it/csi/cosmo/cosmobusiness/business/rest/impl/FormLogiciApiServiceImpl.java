/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmobusiness.business.rest.FormLogiciApi;
import it.csi.cosmo.cosmobusiness.business.service.FormLogiciService;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzeFormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologieParametroFormLogicoResponse;
import it.csi.cosmo.cosmobusiness.security.UseCase;

public class FormLogiciApiServiceImpl extends ParentApiImpl implements FormLogiciApi {

  @Autowired
  private IstanzaFormLogiciService istanzaFormLogiciService;

  @Autowired
  private FormLogiciService formLogiciService;

  @Autowired
  private TransactionService transactionService;

  @Secured(UseCase.Constants.ADMIN_COSMO)
  @Override
  public Response deleteFormLogiciIstanzeId(Long id, SecurityContext securityContext) {
    istanzaFormLogiciService.deleteFormLogiciIstanzeId(id);
    return Response.noContent().build();
  }

  @Override
  public Response getFormLogiciIstanze(String filter, SecurityContext securityContext) {
    IstanzeFormLogiciResponse resp = istanzaFormLogiciService.getFormLogiciIstanze(filter);
    return Response.ok(resp).build();
  }

  @Override
  public Response getFormLogiciIstanzeId(Long id, SecurityContext securityContext) {
    IstanzaFunzionalitaFormLogico resp = istanzaFormLogiciService.getFormLogiciIstanzeId(id);
    return Response.ok(resp).build();
  }

  @Secured(UseCase.Constants.ADMIN_COSMO)
  @Override
  public Response postFormLogiciIstanze(CreaIstanzaFunzionalitaFormLogicoRequest body,
      SecurityContext securityContext) {
    IstanzaFunzionalitaFormLogico resp = istanzaFormLogiciService.postFormLogiciIstanze(body);
    return Response.ok(resp).build();
  }

  @Secured(UseCase.Constants.ADMIN_COSMO)
  @Override
  public Response putFormLogiciIstanzeId(Long id, AggiornaIstanzaFunzionalitaFormLogicoRequest body,
      SecurityContext securityContext) {
    IstanzaFunzionalitaFormLogico resp = istanzaFormLogiciService.putFormLogiciIstanzeId(id, body);
    return Response.ok(resp).build();
  }

  @Override
  public Response getFormLogici(String filter, SecurityContext securityContext) {
    FormLogiciResponse response = formLogiciService.getFormLogici(filter);
    return Response.ok(response).build();
  }

  @Override
  public Response getFormLogico(Long id, SecurityContext securityContext) {
    FormLogico response = formLogiciService.getFormLogico(id);
    return Response.ok(response).build();
  }

  @Override
  public Response getFormLogiciTipologieIstanzeFunzionalita(SecurityContext securityContext) {
    List<TipologiaFunzionalitaFormLogico> response = istanzaFormLogiciService.getAllTipologie();
    return Response.ok(response).build();
  }

  @Secured(hasAnyAuthority = {UseCase.Constants.ADMIN_COSMO, UseCase.Constants.CONF})
  @Override
  public Response postFormLogici(CreaFormLogicoRequest body, SecurityContext securityContext) {
    FormLogico response = formLogiciService.postFormLogici(body);


    formLogiciService.setFunzionalitaMultipla(response.getId(), body.getIstanzeFunzionalita(),
        false);
    formLogiciService.setFunzionalitaMultipla(response.getId(), body.getIstanzeFunzionalita(),
        true);

        return Response.status(201).entity(response).build();
  }

  @Secured(hasAnyAuthority = {UseCase.Constants.ADMIN_COSMO, UseCase.Constants.CONF})
  @Override
  public Response putFormLogici(Long id, AggiornaFormLogicoRequest body,
      SecurityContext securityContext) {


    FormLogico response = formLogiciService.putFormLogici(id, body);



    formLogiciService.setFunzionalitaMultipla(response.getId(), body.getIstanzeFunzionalita(),
        false);
    formLogiciService.setFunzionalitaMultipla(response.getId(), body.getIstanzeFunzionalita(),
        true);


    return Response.ok(response).build();
  }

  @Secured(hasAnyAuthority = {UseCase.Constants.ADMIN_COSMO, UseCase.Constants.CONF})
  @Override
  public Response deleteFormLogici(Long id, SecurityContext securityContext) {
    formLogiciService.deleteFormLogici(id);
    return Response.noContent().build();
  }

  @Override
  public Response getFormLogiciTipologieIstanzeFunzionalitaParametri(String id,
      SecurityContext securityContext) {
    TipologieParametroFormLogicoResponse response =
        istanzaFormLogiciService.getFormLogiciTipologieIstanzeFunzionalitaParametri(id);
    return Response.ok(response).build();
  }

}
