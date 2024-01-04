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
import it.csi.cosmo.cosmosoap.integration.dto.dosign.TimeStamp;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TimeStampDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {LocalDateTimeXmlGregorianCalendarMapper.class})
public interface TimeStampMapper {
	TimeStampMapper INSTANCE = Mappers.getMapper(TimeStampMapper.class);

	static TimeStampMapper getInstance() {
		return INSTANCE;
	}

	@Mappings(value = {
			@Mapping(ignore = true, target = "signer")
	})
	TimeStamp toDTO(TimeStampDto input);

	@Mappings(value = {
			@Mapping(ignore = true, target = "signer"),
			@Mapping(ignore = true, target = "error"),
			@Mapping(ignore = true, target = "errorMsg"),
			@Mapping(ignore = true, target = "hexErrorCode")
	})
	TimeStampDto toRecord(TimeStamp input);
}
