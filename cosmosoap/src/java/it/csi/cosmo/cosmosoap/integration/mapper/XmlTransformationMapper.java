/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlTransformation;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlTransformationDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface XmlTransformationMapper {
	XmlTransformationMapper INSTANCE = Mappers.getMapper(XmlTransformationMapper.class);

	static XmlTransformationMapper getInstance() {
		return INSTANCE;
	}

	@Mappings(value = {
			@Mapping(ignore = true, target = "verifyInfo")
	})
	XmlTransformation toDTO(XmlTransformationDto input);

	@Mappings(value = {
			@Mapping(ignore = true, target = "verifyInfo")
	})
	XmlTransformationDto toRecord(XmlTransformation input);
}
