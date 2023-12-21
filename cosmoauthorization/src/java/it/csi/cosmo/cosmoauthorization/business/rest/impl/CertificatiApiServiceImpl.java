/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.CertificatiApi;
import it.csi.cosmo.cosmoauthorization.business.service.CertificatiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;

public class CertificatiApiServiceImpl extends ParentApiImpl implements CertificatiApi {

  @Autowired
  private CertificatiService certificatiService;

  @Override
  public Response getCertificatiId(String id, SecurityContext securityContext) {
    CertificatoFirma certificato = certificatiService.getCertificato(id);
    if (certificato == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificato).build();
    }
  }

  @Override
  public Response getCertificati(SecurityContext securityContext) {

    List<CertificatoFirma> certificati = certificatiService.getCertificati();

    if (certificati == null || certificati.isEmpty()) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificati).build();
    }
  }

  @Override
  public Response postCertificati(CertificatoFirma body,
      SecurityContext securityContext) {

    CertificatoFirma certificato = certificatiService.postCertificato(body);

    if (certificato == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificato).build();
    }
  }


  @Override
  public Response putCertificatiId(String id, CertificatoFirma body,
      SecurityContext securityContext) {
    CertificatoFirma certificato = certificatiService.putCertificato(id, body);

    if (certificato == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificato).build();
    }
  }

  @Override
  public Response deleteCertificatiId(String id, SecurityContext securityContext) {
    CertificatoFirma certificato = certificatiService.deleteCertificato(id);
    if (certificato == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificato).build();
    }
  }

  @Override
  public Response putCertificatiSalvaUltimoUsato(String id,
      SecurityContext securityContext) {
    CertificatoFirma certificato = certificatiService.putUltimoCertificatoUsato(id);

    if (certificato == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(certificato).build();
    }
  }

}
