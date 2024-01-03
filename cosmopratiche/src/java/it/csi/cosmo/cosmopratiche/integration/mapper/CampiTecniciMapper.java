/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.cosmopratiche.dto.rest.CampiTecnici;

/**
 *
 */
@Component
public class CampiTecniciMapper {

  public CampiTecnici toDTO(IntervalloValiditaEntity entity) {
    CampiTecnici campiTecnici = new CampiTecnici();

    campiTecnici.setDtIniVal(toOffsetDateTime(entity.getDtInizioVal()));

    campiTecnici.setDtModifica(toOffsetDateTime(entity.getDtInizioVal()));

    campiTecnici.setDtFineVal(toOffsetDateTime(entity.getDtFineVal()));

    return campiTecnici;
  }

  public CampiTecnici toDTO(CampiTecniciEntity entity) {
    CampiTecnici campiTecnici = new CampiTecnici();

    campiTecnici.setDtIniVal(toOffsetDateTime(entity.getDtInserimento()));

    campiTecnici.setDtModifica(
        toOffsetDateTime(entity.getDtUltimaModifica() != null ? entity.getDtUltimaModifica()
            : entity.getDtInserimento()));

    campiTecnici.setDtFineVal(toOffsetDateTime(entity.getDtCancellazione()));

    return campiTecnici;
  }

  private OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()),
        ZoneId.systemDefault());
  }

}
