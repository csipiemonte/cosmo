/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.GetStatoRichiestaRequestType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.GetStatoRichiestaResponseType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.SmistaDocumentoRequestType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.SmistaDocumentoResponseType;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface StardasMapper {

  @Mapping(ignore = true, target = "datiSmistaDocumento.datiDocumentoXML.content")
  SmistaDocumentoRequestType toStardasDTO(SmistaDocumentoRequest input);

  SmistaDocumentoResponse toCosmoDTO(SmistaDocumentoResponseType input);

  GetStatoRichiestaRequestType toStardasDTO(GetStatoRichiestaRequest input);

  @Mapping(ignore = true, target = "statoRichiesta")
  GetStatoRichiestaResponse toCosmoDTO(GetStatoRichiestaResponseType input);

}
