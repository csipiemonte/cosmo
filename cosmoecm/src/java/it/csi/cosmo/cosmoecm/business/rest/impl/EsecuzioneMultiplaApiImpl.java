/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoecm.business.rest.EsecuzioneMultiplaApi;
import it.csi.cosmo.cosmoecm.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public class EsecuzioneMultiplaApiImpl extends ParentApiImpl implements EsecuzioneMultiplaApi {

  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;

  @Secured(profiled = true)
  @Override
  public Response postEsecuzioneMultiplaFirma(EsecuzioneMultiplaFirmaRequest body,
      SecurityContext securityContext) {
    esecuzioneMultiplaService.postEsecuzioneMultiplaFirma(body);

    return Response.status(202).build();
  }

  @Secured(profiled = true)
  @Override
  public Response postEsecuzioneMultiplaRifiutoFirma(EsecuzioneMultiplaRifiutoFirmaRequest body,
      SecurityContext securityContext) {
    RiferimentoOperazioneAsincrona content =
        esecuzioneMultiplaService.postEsecuzioneMultiplaRifiutoFirma(body);

    return Response.status(201).entity(content).build();
  }

}
