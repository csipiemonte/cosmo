/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.CertificatePolicy;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.CertificatePolicyDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {PolicyInfoMapper.class})
public interface CertificatePolicyMapper {
  CertificatePolicyMapper INSTANCE = Mappers.getMapper(CertificatePolicyMapper.class);

  static CertificatePolicyMapper getInstance() {
    return INSTANCE;
  }

  @Mapping(target = "policyInfos", source = "policyInfo")
  CertificatePolicy toDTO(CertificatePolicyDto input);

  @Mapping(target = "policyInfo", source = "policyInfos")
  CertificatePolicyDto toRecord(CertificatePolicy input);
}
