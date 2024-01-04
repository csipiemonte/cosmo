/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlDataObjectFormat;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlDataObjectFormatDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {XmlObjectIdentifierMapper.class})
public interface XmlDataObjectFormatMapper {
	XmlDataObjectFormatMapper INSTANCE = Mappers.getMapper(XmlDataObjectFormatMapper.class);

	static XmlDataObjectFormatMapper getInstance() {
		return INSTANCE;
	}

	XmlDataObjectFormat toDTO(XmlDataObjectFormatDto input);

	XmlDataObjectFormatDto toRecord(XmlDataObjectFormat input);
}
