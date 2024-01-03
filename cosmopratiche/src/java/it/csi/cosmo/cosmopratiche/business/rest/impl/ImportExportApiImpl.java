/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmopratiche.business.rest.ImportExportApi;
import it.csi.cosmo.cosmopratiche.business.service.ImportExportService;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.EsportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.ImportaTipoPraticaRequest;

/**
 *
 */

public class ImportExportApiImpl extends ParentApiImpl implements ImportExportApi {

  @Autowired
  ImportExportService importExportService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response importaPratica(ImportaTipoPraticaRequest body, HttpServletRequest request) {
    Map<String, Object> result = importExportService.importa(body);
    return Response.ok(result).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response esporta(EsportaTipoPraticaRequest body, SecurityContext securityContext) {
    byte[] result = importExportService.esporta(body);

    var encoded = new String(Base64.getEncoder().encode(result), StandardCharsets.UTF_8);

    Map<String, Object> output = new HashMap<>();
    output.put("encoded", encoded);
    return Response.ok(output).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response getOpzioniEsportazioneTenant(String codiceTipoPratica,
      HttpServletRequest request) {
    List<Object> result = importExportService.getOpzioniEsportazioneTenant(codiceTipoPratica);
    return Response.ok(result).build();
  }

}
