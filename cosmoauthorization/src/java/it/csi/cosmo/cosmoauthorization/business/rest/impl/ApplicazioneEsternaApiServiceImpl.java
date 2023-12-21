/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;


import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoauthorization.business.rest.ApplicazioneEsternaApi;
import it.csi.cosmo.cosmoauthorization.business.service.ApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.business.service.FunzionalitaApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;

public class ApplicazioneEsternaApiServiceImpl extends ParentApiImpl
implements ApplicazioneEsternaApi {

  @Autowired
  private ApplicazioneEsternaService applicazioneService;

  @Autowired
  private FunzionalitaApplicazioneEsternaService funzionalitaService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteApplicazioneEsternaId(String id, SecurityContext securityContext) {
    applicazioneService.eliminaApplicazione(id);
    return Response.noContent().build();
  }


  @Override
  public Response deleteApplicazioneEsternaIdAssociazioneEnte(String id,
      SecurityContext securityContext) {
    applicazioneService.eliminaApplicazioneAssociata(id);
    return Response.noContent().build();
  }

  @Override
  public Response deleteApplicazioneEsternaIdApplicazioneFunzionalitaIdFunzionalita(
      String idApplicazione, String idFunzionalita, SecurityContext securityContext) {
    funzionalitaService.eliminaFunzionalita(idApplicazione, idFunzionalita);
    return Response.noContent().build();
  }


  @Override
  public Response getApplicazioneEsterna(Boolean configurata, SecurityContext securityContext) {
    List<ApplicazioneEsterna> applicazioni = applicazioneService.getApplicazioni(configurata);

    if (null == applicazioni) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazioni).build();
    }
  }

  @Override
  public Response getApplicazioneEsternaId(String id, SecurityContext securityContext) {
    ApplicazioneEsterna applicazione = applicazioneService.getApplicazione(id);

    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Override
  public Response getApplicazioneEsternaIdApplicazioneFunzionalita(String idApplicazione,
      SecurityContext securityContext) {
    List<FunzionalitaApplicazioneEsternaConValidita> funzionalita =
        funzionalitaService.getFunzionalita(idApplicazione);

    if (null == funzionalita) {
      return Response.noContent().build();
    } else {
      return Response.ok(funzionalita).build();
    }
  }

  @Override
  public Response getApplicazioneEsternaIdFunzionalitaId(String idApplicazione,
      String idFunzionalita, SecurityContext securityContext) {
    FunzionalitaApplicazioneEsternaConValidita singolaFunzionalita =
        funzionalitaService.getSingolaFunzionalita(idApplicazione, idFunzionalita);

    if (null == singolaFunzionalita) {
      return Response.noContent().build();
    } else {
      return Response.ok(singolaFunzionalita).build();
    }
  }

  @Override
  public Response getApplicazioneEsternaIdAssociazioneEnte(String id,
      SecurityContext securityContext) {
    ApplicazioneEsternaConValidita applicazione =
        applicazioneService.getApplicazioneAssociataEnte(id);

    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postApplicazioneEsterna(ApplicazioneEsterna body,
      SecurityContext securityContext) {
    ApplicazioneEsterna applicazione = applicazioneService.salvaApplicazione(body);

    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Override
  public Response postApplicazioneEsternaIdFunzionalita(String idApplicazione,
      FunzionalitaApplicazioneEsternaConValidita body, SecurityContext securityContext) {
    FunzionalitaApplicazioneEsternaConValidita singolaFunzionalita =
        funzionalitaService.salvaSingolaFunzionalita(idApplicazione, body);

    if (null == singolaFunzionalita) {
      return Response.noContent().build();
    } else {
      return Response.ok(singolaFunzionalita).build();
    }
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putApplicazioneEsternaId(String id, ApplicazioneEsterna body,
      SecurityContext securityContext) {
    ApplicazioneEsterna applicazione = applicazioneService.aggiornaApplicazione(id, body);
    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Override
  public Response putApplicazioneEsternaIdFunzionalitaId(String idApplicazione,
      String idFunzionalita, FunzionalitaApplicazioneEsternaConValidita body,
      SecurityContext securityContext) {
    FunzionalitaApplicazioneEsternaConValidita singolaFunzionalita =
        funzionalitaService.aggiornaSingolaFunzionalita(idApplicazione, idFunzionalita, body);

    if (null == singolaFunzionalita) {
      return Response.noContent().build();
    } else {
      return Response.ok(singolaFunzionalita).build();
    }
  }

  @Override
  public Response getApplicazioneEsternaAssociazioneEnte(Boolean enteAssociato,
      SecurityContext securityContext) {
    List<ApplicazioneEsternaConValidita> applicazione =
        applicazioneService.getApplicazioniAssociateEnte(enteAssociato);
    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Override
  public Response putApplicazioneEsternaIdAssociazioneEnte(String id,
      ApplicazioneEsternaConValidita body, SecurityContext securityContext) {
    ApplicazioneEsternaConValidita applicazione =
        applicazioneService.associaModificaAssociazioneAppEnte(body);
    if (null == applicazione) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazione).build();
    }
  }

  @Override
  public Response putApplicazioneEsternaAssociazioneUtente(List<ApplicazioneEsterna> body,
      SecurityContext securityContext) {
    List<ApplicazioneEsterna> applicazioni = applicazioneService.associaDissociaAppUtente(body);

    if (null == applicazioni) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazioni).build();
    }
  }


  @Override
  public Response getApplicazioneEsternaAll(SecurityContext securityContext) {

    List<ApplicazioneEsternaConValidita> applicazioni = applicazioneService.getTutteApplicazioni();

    if (null == applicazioni) {
      return Response.noContent().build();
    } else {
      return Response.ok(applicazioni).build();
    }
  }

}
