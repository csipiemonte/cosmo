/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.entities.CosmoTPreferenzeEnte;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;


/**
 * Classe per funzioni di utilities comuni a tutto l'applicativo
 */
public abstract class CommonUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "CommonUtils");

  private CommonUtils() {
    // NOP
  }

  public static void validaDatiInput(String id, String parametro) {
    String methodName = "validaDatiInput";

    if (StringUtils.isBlank(id)) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
    }

    if (!StringUtils.isNumeric(id)) {
      LOGGER.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, parametro));
    }
  }

  public static void validaEnte(Ente ente) {
    String methodName = "validaEnte";

    if (ente == null) {
      LOGGER.error(methodName, ErrorMessages.E_ENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.E_ENTE_NON_VALORIZZATO);
    }
    if (StringUtils.isBlank(ente.getNome())) {
      LOGGER.error(methodName, ErrorMessages.E_NOME_ENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.E_NOME_ENTE_NON_VALORIZZATO);
    }

  }

  public static void validaProfilo(Profilo profilo) {
    String methodName = "validaProfilo";

    if (profilo == null) {
      LOGGER.error(methodName, ErrorMessages.P_PROFILO_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.P_PROFILO_NON_VALORIZZATO);
    }

    if (StringUtils.isBlank(profilo.getCodice())) {
      LOGGER.error(methodName, ErrorMessages.P_CODICE_PROFILO_NON_VALORIZZATO);
      throw new BadRequestException(
          ErrorMessages.P_CODICE_PROFILO_NON_VALORIZZATO);
    }
  }

  public static void validaPreferenzeEnte(CosmoTPreferenzeEnte preferenzeEnte) {
    String methodName = "validaPreferenze";
    if (preferenzeEnte == null) {
      LOGGER.error(methodName, ErrorMessages.PE_PREFERENZE_ENTE_NON_VALORIZZATO);
      throw new BadRequestException(
          ErrorMessages.PE_PREFERENZE_ENTE_NON_VALORIZZATO);
    }
    if (StringUtils.isBlank(preferenzeEnte.getVersione())) {
      LOGGER.error(methodName, ErrorMessages.PE_VERSIONE_PREFERENZE_ENTE_NON_VALORIZZATO);
      throw new BadRequestException(
          ErrorMessages.PE_VERSIONE_PREFERENZE_ENTE_NON_VALORIZZATO);
    }
    if (preferenzeEnte.getValore() == null) {
      LOGGER.error(methodName, ErrorMessages.PE_VALORE_PREFERENZE_ENTE_NON_VALORIZZATO);
      throw new BadRequestException(
          ErrorMessages.PE_VALORE_PREFERENZE_ENTE_NON_VALORIZZATO);
    }
  }

  public static CampiTecnici creaCampiTecnici(CosmoREntity entity) {

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(null == entity.getDtInizioVal() ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(entity.getDtInizioVal().getTime()),
            ZoneId.systemDefault()));

    campiTecnici.setDtFineVal(null == entity.getDtFineVal() ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(entity.getDtFineVal().getTime()),
            ZoneId.systemDefault()));

    return campiTecnici;
  }
}
