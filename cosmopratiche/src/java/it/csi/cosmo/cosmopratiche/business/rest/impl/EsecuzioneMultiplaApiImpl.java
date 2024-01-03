/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.EsecuzioneMultiplaApi;
import it.csi.cosmo.cosmopratiche.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;

public class EsecuzioneMultiplaApiImpl extends ParentApiImpl implements EsecuzioneMultiplaApi {

  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;

  @Deprecated
  @Override
  public Response getEsecuzioneMultiplaTasks(SecurityContext securityContext) {
    List<AttivitaEseguibileMassivamente> responseContent =
        esecuzioneMultiplaService.getTaskDisponibiliPerUtenteCorrente();
    return Response.ok(responseContent).build();
  }

  @Override
  public Response getEsecuzioneMultiplaTaskDisponibili(String filter,
      SecurityContext securityContext) {
    List<RiferimentoAttivita> responseContent =
        esecuzioneMultiplaService.getTipologieTaskDisponibiliPerUtenteCorrente(filter);
    return Response.ok(responseContent).build();
  }

}
