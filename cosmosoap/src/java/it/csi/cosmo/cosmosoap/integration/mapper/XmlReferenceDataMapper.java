/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.XmlReferenceData;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlReferenceDataDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface XmlReferenceDataMapper {
	XmlReferenceDataMapper INSTANCE = Mappers.getMapper(XmlReferenceDataMapper.class);

	static XmlReferenceDataMapper getInstance() {
		return INSTANCE;
	}

	XmlReferenceData toDTO(XmlReferenceDataDto input);

	XmlReferenceDataDto toRecord(XmlReferenceData input);
}
