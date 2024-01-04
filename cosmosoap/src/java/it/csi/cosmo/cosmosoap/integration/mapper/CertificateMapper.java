/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.Certificate;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.CertificateDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CertificateMapper {
  CertificateMapper INSTANCE = Mappers.getMapper(CertificateMapper.class);

  static CertificateMapper getInstance() {
    return INSTANCE;
  }

  Certificate toDTO(CertificateDto input);

  CertificateDto toRecord(Certificate input);
}
