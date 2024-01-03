/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.rest.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmonotifications.business.rest.HelperApi;
import it.csi.cosmo.cosmonotifications.business.service.HelperService;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.DecodificaHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;

/**
 *
 */

public class HelperApiServiceImpl extends ParentApiImpl implements HelperApi {

  @Autowired
  private HelperService helperService;

  @Override
  public Response deleteHelperId(String id, SecurityContext securityContext) {
    helperService.deleteHelper(id);
    return Response.ok().build();
  }

  @Override
  public Response getHelper(String filter, SecurityContext securityContext) {
    HelperResponse helpers = helperService.getHelpers(filter);
    return Response.ok(helpers).build();
  }

  @Override
  public Response getHelperId(String id, SecurityContext securityContext) {
    Helper helper = helperService.getHelper(id);
    return Response.ok(helper).build();
  }

  @Override
  public Response postHelper(Helper body, SecurityContext securityContext) {
    Helper helper = helperService.postHelper(body);
    return Response.ok(helper).build();
  }

  @Override
  public Response putHelperId(String id, Helper body, SecurityContext securityContext) {
    Helper helper = helperService.putHelper(id, body);
    return Response.ok(helper).build();
  }

  @Override
  public Response getCodicePagina(SecurityContext securityContext) {
    List<CodicePagina> codiciPagina = helperService.getCodiciPagina();
    return Response.ok(codiciPagina).build();
  }

  @Override
  public Response getCodiciTabs(String codice, SecurityContext securityContext) {
    List<CodiceTab> codiciTab = helperService.getCodiciTab(codice);
    return Response.ok(codiciTab).build();
  }

  @Override
  public Response getHelperDecodifica(String pagina, String tab, String form,
      SecurityContext securityContext) {
    DecodificaHelper dh = helperService.getDecodifica(pagina, tab, form);
    return Response.ok(dh).build();
  }

  @Override
  public Response getHelperCodiceModale(String filter, SecurityContext securityContext) {
    List<CodiceModale> modali = helperService.getModali(filter);
    return Response.ok(modali).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response getHelperExport(String id, SecurityContext securityContext) {
    byte[] export = helperService.getExport(id);
    var encoded = new String(Base64.getEncoder().encode(export), StandardCharsets.UTF_8);

    Map<String, Object> output = new HashMap<>();
    output.put("encoded", encoded);
    return Response.ok(output).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postHelperImport(HelperImportRequest body, SecurityContext securityContext) {
    HelperImportResult hir = helperService.postImport(body);
    return Response.ok(hir).build();
  }

}
