/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")
public interface CosmoRStatoTipoPraticaMapper {

}
