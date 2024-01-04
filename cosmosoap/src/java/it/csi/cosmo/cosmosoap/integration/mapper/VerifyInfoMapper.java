/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.VerifyInfo;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyInfoDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {SignerMapper.class})
public interface VerifyInfoMapper {
	VerifyInfoMapper INSTANCE = Mappers.getMapper(VerifyInfoMapper.class);

	static VerifyInfoMapper getInstance() {
		return INSTANCE;
	}

    @Mapping(target = "signers", source = "signer")
	VerifyInfo toDTO(VerifyInfoDto input);

	default List<VerifyInfo> toListDTO(List<VerifyInfoDto> sources) {
		List<VerifyInfo> result = null;
        if (!CollectionUtils.isEmpty(sources)) {
			result = new ArrayList<>();
			for (VerifyInfoDto source : sources) {
				result.add(toDTO(source));
			}
		}
		return result;
	}

	@Mappings(value = {
			@Mapping(ignore = true, target = "error"),
			@Mapping(ignore = true, target = "errorMsg"),
			@Mapping(ignore = true, target = "hexErrorCode")
	})
    @Mapping(target = "signer", source = "signers")
	VerifyInfoDto toRecord(VerifyInfo input);

	default List<VerifyInfoDto> toListRecord(List<VerifyInfo> sources) {
		List<VerifyInfoDto> result = null;
        if (!CollectionUtils.isEmpty(sources)) {
			result = new ArrayList<>();
			for (VerifyInfo source : sources) {
				result.add(toRecord(source));
			}
		}
		return result;
	}
}
