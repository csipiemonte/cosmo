/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmosoap.business.rest.StiloApi;
import it.csi.cosmo.cosmosoap.business.service.StiloService;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.integration.mapper.StiloMapper;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.AddUdOutput;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.UpdUdOutput;

/**
 *
 */

public class StiloApiImpl extends ParentApiImpl implements StiloApi {


  @Autowired
  StiloService stiloService;


  @Autowired
  StiloMapper stiloMapper;

  @Override
  public Response postStiloCaricaUnitaDocumentaria(
      CaricaUnitaDocumentariaRequest caricaUnitaDocumentariaRequest,
      SecurityContext securityContext) {

    AddUdOutput addUdOutput =
        stiloService.addUd(
            this.stiloMapper.mapCaricaUnitaDocumentariaRequestToNewUD(caricaUnitaDocumentariaRequest), null);

    return Response.ok(this.stiloMapper.toCaricaUnitaDocumentariaResponse(addUdOutput)).build();


  }

  @Override
  public Response putAggiornaUnitaDocumentaria(
      AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest,
      SecurityContext securityContext) {

    UpdUdOutput updUdOutput =
        stiloService.aggiornaUnitaDocumentaria(aggiornaUnitaDocumentariaRequest);

    return Response.ok(this.stiloMapper.toAggiornaUnitaDocumentariaResponse(updUdOutput)).build();

  }


}
