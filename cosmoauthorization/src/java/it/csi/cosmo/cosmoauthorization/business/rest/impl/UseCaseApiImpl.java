/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmoauthorization.business.rest.UseCaseApi;
import it.csi.cosmo.cosmoauthorization.business.service.UseCaseService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CategorieUseCaseResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCaseResponse;

/**
 *
 */

public class UseCaseApiImpl extends ParentApiImpl implements UseCaseApi {

  @Autowired
  private UseCaseService useCaseService;
  @Override
  public Response getUc(String filter, SecurityContext securityContext) {
    UseCaseResponse useCases = useCaseService.getUseCases(filter);
    if (CollectionUtils.isEmpty(useCases.getUseCases())) {
      return Response.noContent().build();
    } else {
      return Response.ok(useCases).build();
    }
  }

  @Override
  public Response getUseCaseCategorie(String string, SecurityContext securityContext) {

    CategorieUseCaseResponse useCase = useCaseService.getUseCase(string);
    if (CollectionUtils.isEmpty(useCase.getCategorieUseCase())) {
      return Response.ok(useCase).build();
    } else {
      return Response.noContent().build();
    }
  }

}
