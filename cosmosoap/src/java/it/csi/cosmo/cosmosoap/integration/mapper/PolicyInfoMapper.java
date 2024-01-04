/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.PolicyInfo;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.PolicyInfoDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PolicyInfoMapper {
	PolicyInfoMapper INSTANCE = Mappers.getMapper(PolicyInfoMapper.class);

	static PolicyInfoMapper getInstance() {
		return INSTANCE;
	}

	PolicyInfo toDTO(PolicyInfoDto input);

	PolicyInfoDto toRecord(PolicyInfo input);
}
