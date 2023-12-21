/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.ParametriDiSistemaApi;
import it.csi.cosmo.cosmoauthorization.business.service.ParametriDiSistemaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;



public class ParametriDiSistemaApiImpl extends ParentApiImpl implements ParametriDiSistemaApi {

	@Autowired
	ParametriDiSistemaService parametriDiSistemaService;

	@Override
	public Response getParametriDiSistema(String filter, SecurityContext securityContext) {
		return Response.ok(parametriDiSistemaService.getParamtriDiSistema(filter)).build();
	}

	@Override
	public Response deleteParametroDiSistemaByChiave(String chiave, SecurityContext securityContext) {
		parametriDiSistemaService.deleteParametroDiSistemaByChiave(chiave);	
		return Response.noContent().build();
	}
	
	@Override
	public Response getParametroDiSistemaByChiave(String chiave, SecurityContext securityContext) {
		ParametroDiSistema parametroDiSistema = parametriDiSistemaService.getParamtroDiSistemaByChiave(chiave);
		return Response.ok(parametroDiSistema).build();
	}

	@Override
	public Response postParametroDiSistema(CreaParametroDiSistemaRequest body, SecurityContext securityContext) {
		ParametroDiSistema parametroDiSistema = parametriDiSistemaService.postParametroDiSistema(body);
		return Response.status(201).entity(parametroDiSistema).build();
	}

	@Override
	public Response putParametroDiSistemaByChiave(String chiave, AggiornaParametroDiSistemaRequest body,
			SecurityContext securityContext) {
		ParametroDiSistema parametroDiSistema = parametriDiSistemaService.putParametroDiSistemaByChiave(chiave, body);
		return Response.ok(parametroDiSistema).build();
	}


}
