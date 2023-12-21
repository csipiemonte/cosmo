/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.rest.impl;

import java.util.Arrays;
import java.util.Optional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.security.model.LivelloAutenticazione;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.rest.SessioneApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.UserService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.dto.AmbienteLogoutEnum;
import it.csi.cosmo.cosmo.dto.rest.LoginRequest;
import it.csi.cosmo.cosmo.dto.rest.LogoutResponse;
import it.csi.cosmo.cosmo.dto.rest.UserInfo;
import it.csi.cosmo.cosmo.integration.mapper.UserInfoMapper;
import it.csi.cosmo.cosmo.integration.rest.CosmoAuthorizationUtentiClient;
import it.csi.cosmo.cosmo.security.SecurityUtils;
import it.csi.cosmo.cosmo.util.ValidationUtils;


/**
 * Risorsa RestEasy per la gestione del logout
 */
public class SessioneApiImpl extends ParentApiImpl implements SessioneApi {

  @Autowired
  public UserService userService;

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Autowired
  public CosmoAuthorizationUtentiClient authorizationUtentiClient;

  @Override
  public Response deleteSessione(String ambienteLogout, SecurityContext securityContext) {

    if(Arrays.stream(AmbienteLogoutEnum.values()).noneMatch(e -> e.getValue().equals(ambienteLogout))) {
      throw new BadRequestException("Identificativo logout non accettato");
    }

    AmbienteLogoutEnum identificativoLogout = AmbienteLogoutEnum.fromValue(ambienteLogout);

    UserInfoDTO currUser = SecurityUtils.getUtenteCorrente();

    LivelloAutenticazione lvl = currUser.getIdentita().getLivelloAutenticazione();

    userService.invalidaSessione(RequestUtils.getCurrentRequest().orElseThrow());

    UserInfo userResponse = userInfoMapper.toDTO(currUser);

    LogoutResponse response = new LogoutResponse();
    response.setUserInfo(userResponse);

    ParametriApplicativo parametro = null;

    switch (lvl) {
      case USERNAME_PASSWORD_AUTOREGISTRATI:
      case USERNAME_PASSWORD_IDENTITA_VERIFICATA:
        parametro = checkLivello(identificativoLogout,1);
        response.setUrl(
            configurazioneService.getConfig(parametro).getValue());
        break;

      case USERNAME_PASSWORD_PIN_IDENTITA_VERIFICATA:
        parametro = checkLivello(identificativoLogout,2);
        response.setUrl(
            configurazioneService.getConfig(parametro).getValue());
        break;

      case X509_CA_NON_QUALIFICATA:
      case X509_CA_QUALIFICATA:
        parametro = checkLivello(identificativoLogout,3);
        response.setUrl(
            configurazioneService.getConfig(parametro).getValue());
        break;

      default:
        break;
    }

    return Response.ok(response).build();
  }

  private ParametriApplicativo checkLivello(AmbienteLogoutEnum identificativoLogout, int livello) {
    ParametriApplicativo parametro = null;
    switch(identificativoLogout) {
      case INTRANET:
          parametro = ParametriApplicativo.LOGOUT_INTRANET_URL;
        break;
      case INTRACOM:
          parametro = ParametriApplicativo.LOGOUT_INTRACOM_URL;
        break;
      case INTERNET:
          parametro = ParametriApplicativo.LOGOUT_INTERNET_URL;
        break;
      default:
          parametro = ParametriApplicativo.LOGOUT_URL;
        break;
    }
    return parametro;
  }

  @Override
  public Response getSessione(SecurityContext securityContext) {
    UserInfo response = userInfoMapper.toDTO(SecurityUtils.getUtenteCorrente());
    var result = authorizationUtentiClient.getUtentiCodiceFiscale(response.getCodiceFiscale());
    Optional.ofNullable(result.getUtente())
    .orElseThrow(() -> new UnauthorizedException("Utente non registrato o disabilitato."));
    response.setAccessoDiretto(userService.isAccessoDiretto(result.getUtente()));
    return Response.ok(response).build();
  }

  @Override
  public Response postSessione(LoginRequest body, SecurityContext securityContext) {

    ValidationUtils.require(body, "body");
    // ValidationUtils.require(body.getIdEnte(), "id ente");
    ValidationUtils.require(body.getIdProfilo(), "id profilo");

    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();

    userService.caricaIdentityInSessione(RequestUtils.getCurrentRequest().orElseThrow(),
        userInfo.getIdentita(), body.getIdEnte(), body.getIdProfilo());

    UserInfo response = userInfoMapper.toDTO(SecurityUtils.getUtenteCorrente());

    return Response.ok(response).build();
  }
}
