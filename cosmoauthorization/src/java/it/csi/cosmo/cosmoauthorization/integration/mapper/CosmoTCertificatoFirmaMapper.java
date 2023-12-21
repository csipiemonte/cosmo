/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTCertificatoFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {DateFormatsMapper.class, CosmoDEnteCertificatoreMapper.class,
    CosmoDProfiloFeqMapper.class,
    CosmoDSceltaMarcaTemporaleMapper.class, CosmoDTipoCredenzialiFirmaMapper.class,
    CosmoDTipoOtpMapper.class},
componentModel = "spring")
public interface CosmoTCertificatoFirmaMapper {

  @Mapping(source = "cosmoDEnteCertificatore", target = "enteCertificatore")
  @Mapping(source = "cosmoDProfiloFeq", target = "profiloFEQ")
  @Mapping(source = "cosmoDSceltaMarcaTemporale", target = "sceltaMarcaTemporale")
  @Mapping(source = "cosmoDTipoCredenzialiFirma", target = "tipoCredenzialiFirma")
  @Mapping(source = "cosmoDTipoOtp", target = "tipoOTP")
  @Mapping(ignore = true, target = "username")
  @Mapping(ignore = true, target = "password")
  @Mapping(ignore = true, target = "dataScadenza")
  @Mapping(source = "ultimoUtilizzato", target = "ultimoUtilizzato")
  CertificatoFirma toDTO(CosmoTCertificatoFirma input);

}
