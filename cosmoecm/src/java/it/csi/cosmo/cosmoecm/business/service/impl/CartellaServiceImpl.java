/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.CartellaService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.rest.Cartella;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class CartellaServiceImpl implements CartellaService {

  private static final String CLASSNAME = CartellaServiceImpl.class.getSimpleName();

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASSNAME);

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Override
  public Cartella creaCartellaIndex(Cartella cartella) {
    String methodName = "creaCartellaIndex";

    if (cartella == null || StringUtils.isBlank(cartella.getPath())) {
      LOGGER.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "path"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "path"));
    }

    String cartellaUUID = indexFeignClient.createFolder(cartella.getPath());

    if (StringUtils.isBlank(cartellaUUID)) {
      LOGGER.error(methodName, ErrorMessages.I_ERRORE_CREAZIONE_CARTELLA);
      throw new InternalServerException(
          ErrorMessages.I_ERRORE_CREAZIONE_CARTELLA);
    }

    Cartella output = new Cartella();
    output.setPath(cartella.getPath());
    output.setUuid(cartellaUUID);

    return output;
  }

  @Override
  public void cancellaCartellaIndex(Cartella cartella) {
    String methodName = "cancellaPraticaIndex";

    if (cartella == null || StringUtils.isBlank(cartella.getUuid())) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "uuid"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "uuid"));
    }

    indexFeignClient.deleteIdentifier(cartella.getUuid());

  }

}
