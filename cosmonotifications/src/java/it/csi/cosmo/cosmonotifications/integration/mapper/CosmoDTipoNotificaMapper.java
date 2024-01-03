/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoNotifica;


/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoDTipoNotificaMapper {

  TipoNotifica toDTO(CosmoDTipoNotifica input);
}
