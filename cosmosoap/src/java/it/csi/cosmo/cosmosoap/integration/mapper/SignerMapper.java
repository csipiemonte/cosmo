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
import it.csi.cosmo.cosmosoap.integration.dto.dosign.Signer;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.SignerDto;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {TimeStampMapper.class, CertificateMapper.class, LocalDateTimeXmlGregorianCalendarMapper.class, CertificatePolicyMapper.class,
    XmlReferenceMapper.class, XmlSignatureProductionPlaceMapper.class})
public interface SignerMapper {
  SignerMapper INSTANCE = Mappers.getMapper(SignerMapper.class);

  static SignerMapper getInstance() {
    return INSTANCE;
  }

  @Mapping(target = "timeStamps", source = "timeStamp")
  Signer toDTO(SignerDto input);

  @Mappings(value = {
      @Mapping(ignore = true, target = "error"),
      @Mapping(ignore = true, target = "errorMsg"),
      @Mapping(ignore = true, target = "hexErrorCode")
  })
  @Mapping(target = "timeStamp", source = "timeStamps")
  SignerDto toRecord(Signer input);
}
