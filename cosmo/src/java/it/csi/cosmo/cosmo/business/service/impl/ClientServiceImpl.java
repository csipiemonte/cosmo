/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service.impl;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.service.ClientService;
import it.csi.cosmo.cosmo.filter.ClientAuthenticationFilter;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per la gestione del client
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ClientServiceImpl");

  @Override
  public ClientInfoDTO getClientCorrente(HttpServletRequest req) {
    logger.trace( "getClientCorrente", "getClientCorrente from request" );

    if ( req == null ) {
      return ProfilazioneClientServiceImpl.getClientSistema ();
    }

    ClientInfoDTO output =
        (ClientInfoDTO) req.getAttribute(ClientAuthenticationFilter.CLIENTINFO_REQUESTATTR);

    if ( output == null ) {
      return ProfilazioneClientServiceImpl.getClientAnonimo ();
    } else {
      return output;
    }
  }

  @Override
  public ClientInfoDTO getClientCorrente() {
    logger.trace( "getClientCorrente", "getClientCorrente from current request" );
    return getClientCorrente(RequestUtils.getCurrentRequest().orElse(null));
  }

}
