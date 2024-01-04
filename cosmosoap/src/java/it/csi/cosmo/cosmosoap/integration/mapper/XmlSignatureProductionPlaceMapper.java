/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlSignatureProductionPlace;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlSignatureProductionPlaceDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface XmlSignatureProductionPlaceMapper {
	XmlSignatureProductionPlaceMapper INSTANCE = Mappers.getMapper(XmlSignatureProductionPlaceMapper.class);

	static XmlSignatureProductionPlaceMapper getInstance() {
		return INSTANCE;
	}

	XmlSignatureProductionPlace toDTO(XmlSignatureProductionPlaceDto input);

	XmlSignatureProductionPlaceDto toRecord(XmlSignatureProductionPlace input);
}
