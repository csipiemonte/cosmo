/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.EsecuzioneMultiplaApi;
import it.csi.cosmo.cosmobusiness.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaApprovaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaVariabiliProcessoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;

public class EsecuzioneMultiplaApiImpl extends ParentApiImpl implements EsecuzioneMultiplaApi {

  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;

  @Override
  public Response postEsecuzioneMultiplaApprova(EsecuzioneMultiplaApprovaRequest body,
      SecurityContext securityContext) {

    RiferimentoOperazioneAsincrona content =
        esecuzioneMultiplaService.postEsecuzioneMultiplaApprova(body);

    return Response.status(201).entity(content).build();
  }

  @Override
  public Response postEsecuzioneMultiplaVariabiliProcesso(
      EsecuzioneMultiplaVariabiliProcessoRequest body, SecurityContext securityContext) {
    RiferimentoOperazioneAsincrona content =
        esecuzioneMultiplaService.postEsecuzioneMultiplaVariabiliProcesso(body);

    return Response.status(201).entity(content).build();
  }

}
