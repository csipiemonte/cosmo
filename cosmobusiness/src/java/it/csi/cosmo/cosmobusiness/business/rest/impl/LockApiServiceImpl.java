/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.LockApi;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.dto.rest.AcquisizioneLockRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Lock;
import it.csi.cosmo.cosmobusiness.dto.rest.RilascioLockRequest;

public class LockApiServiceImpl extends ParentApiImpl implements LockApi {

  @Autowired
  private LockService lockService;

  @Override
  public Response getLock(String codiceRisorsa, SecurityContext securityContext) {
    Lock res = lockService.getLock(codiceRisorsa);
    return Response.ok(res).build();
  }

  @Override
  public Response postLock(AcquisizioneLockRequest body, SecurityContext securityContext) {
    Lock res = lockService.postLock(body);
    return Response.status(201).entity(res).build();
  }

  @Override
  public Response deleteLock(RilascioLockRequest body, SecurityContext securityContext) {
    lockService.deleteLock(body);
    return Response.noContent().build();
  }

  @Override
  public Response postLockRelease(RilascioLockRequest body, SecurityContext securityContext) { // NOSONAR
    lockService.deleteLock(body);
    return Response.noContent().build();
  }

}
