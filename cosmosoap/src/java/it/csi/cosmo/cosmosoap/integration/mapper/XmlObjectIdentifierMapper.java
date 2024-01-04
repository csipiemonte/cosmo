/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlObjectIdentifier;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlObjectIdentifierDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface XmlObjectIdentifierMapper {
	XmlObjectIdentifierMapper INSTANCE = Mappers.getMapper(XmlObjectIdentifierMapper.class);

	static XmlObjectIdentifierMapper getInstance() {
		return INSTANCE;
	}

	XmlObjectIdentifier toDTO(XmlObjectIdentifierDto input);

	XmlObjectIdentifierDto toRecord(XmlObjectIdentifier input);
}
