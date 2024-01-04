/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlReference;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlReferenceDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {XmlReferenceDataMapper.class, XmlTransformationMapper.class, XmlDataObjectFormatMapper.class})
public interface XmlReferenceMapper {
	XmlReferenceMapper INSTANCE = Mappers.getMapper(XmlReferenceMapper.class);

	static XmlReferenceMapper getInstance() {
		return INSTANCE;
	}

    @Mapping(target = "transformations", source = "transformation")
	XmlReference toDTO(XmlReferenceDto input);

    @Mapping(target = "transformation", source = "transformations")
	XmlReferenceDto toRecord(XmlReference input);
}
