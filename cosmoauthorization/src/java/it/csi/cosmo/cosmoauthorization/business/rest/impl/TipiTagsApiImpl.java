/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.TipiTagsApi;
import it.csi.cosmo.cosmoauthorization.business.service.TipiTagsService;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;

/**
 *
 */

public class TipiTagsApiImpl extends ParentApiImpl implements TipiTagsApi {

  @Autowired
  private TipiTagsService tipiTagsService;

  @Override
  public Response getTipiTags(SecurityContext securityContext) {
    List<TipoTag> tipiTags = tipiTagsService.getTipiTags();
    return Response.ok(tipiTags).build();
  }

}
