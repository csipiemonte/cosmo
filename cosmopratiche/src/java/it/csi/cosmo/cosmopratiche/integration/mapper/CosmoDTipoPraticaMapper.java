/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {CosmoTEnteMapper.class, CosmoDStatoPraticaMapper.class, CosmoDTipoDocumentoMapper.class,
    CosmoDTrasformazioneDatiPraticaMapper.class, CosmoDTabDettaglioMapper.class,
    AbstractMapper.class})
public interface CosmoDTipoPraticaMapper {

  @Mapping(target = "ente", ignore = true)
  @Mapping(target = "stati", ignore = true)
  @Mapping(target = "tipiDocumento", ignore = true)
  @Mapping(target = "customForm", ignore = true)
  @Mapping(target = "tabsDettaglio", ignore = true)
  @Mapping(target = "gruppoCreatore", ignore = true)
  @Mapping(target = "gruppoSupervisore", ignore = true)
  @Mapping(target = "immagine", ignore = true)
  @Mapping(target = "enteCertificatore", ignore = true)
  @Mapping(target = "tipoCredenziale", ignore = true)
  @Mapping(target = "tipoOtp", ignore = true)
  @Mapping(target = "profiloFEQ", ignore = true)
  @Mapping(target = "sceltaMarcaTemporale", ignore = true)
  TipoPratica toLightDTO(CosmoDTipoPratica input);

  @Mapping(target = "ente", source = "cosmoTEnte")
  @Mapping(target = "stati", source = "cosmoRStatoTipoPraticas")
  @Mapping(target = "tipiDocumento", source = "cosmoRTipodocTipopraticas")
  @Mapping(target = "tabsDettaglio", source = "cosmoRTabDettaglioTipoPraticas")
  @Mapping(target = "customForm", ignore = true)
  @Mapping(target = "gruppoCreatore", ignore = true)
  @Mapping(target = "gruppoSupervisore", ignore = true)
  @Mapping(target = "immagine", ignore = true)
  @Mapping(target = "enteCertificatore", source = "cosmoDEnteCertificatore")
  @Mapping(target = "tipoCredenziale", source = "cosmoDTipoCredenzialiFirma")
  @Mapping(target = "tipoOtp", source = "cosmoDTipoOtp")
  @Mapping(target = "profiloFEQ", source = "cosmoDProfiloFeq")
  @Mapping(target = "sceltaMarcaTemporale", source = "cosmoDSceltaMarcaTemporale")
  TipoPratica toDTO(CosmoDTipoPratica input);

  @Mapping(target = "ente", source = "cosmoTEnte")
  @Mapping(target = "stati", ignore = true)
  @Mapping(target = "tipiDocumento", ignore = true)
  @Mapping(target = "customForm", ignore = true)
  @Mapping(target = "tabsDettaglio", ignore = true)
  @Mapping(target = "gruppoCreatore", ignore = true)
  @Mapping(target = "gruppoSupervisore", ignore = true)
  @Mapping(target = "immagine", ignore = true)
  @Mapping(target = "enteCertificatore", ignore = true)
  @Mapping(target = "tipoCredenziale", ignore = true)
  @Mapping(target = "tipoOtp", ignore = true)
  @Mapping(target = "profiloFEQ", ignore = true)
  @Mapping(target = "sceltaMarcaTemporale", ignore = true)
  TipoPratica toLightDTOEnte(CosmoDTipoPratica input);

}
