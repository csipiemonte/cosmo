/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoRFunzionalitaParametroFormLogico;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.Funzionalita;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaParametroFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaParametroFormLogico;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {CosmoTEnteMapper.class})
public interface FormLogicoMapper {

  @Mapping(target = "istanzeFunzionalita", ignore = true)
  @Mapping(target = "riferimentoEnte", source = "cosmoTEnte")
  FormLogico toLightDTO(CosmoTFormLogico input);

  @Mapping(target = "istanzeFunzionalita", source = "cosmoRFormLogicoIstanzaFunzionalitas")
  @Mapping(target = "riferimentoEnte", source = "cosmoTEnte")
  FormLogico toDTO(CosmoTFormLogico input);

  @Mapping(target = "id", source = "cosmoTIstanzaFunzionalitaFormLogico.id")
  @Mapping(target = "descrizione", source = "cosmoTIstanzaFunzionalitaFormLogico.descrizione")
  @Mapping(target = "codice",
  source = "cosmoTIstanzaFunzionalitaFormLogico.cosmoDFunzionalitaFormLogico.codice")
  @Mapping(target = "ordine", source = "ordine")
  @Mapping(target = "parametri",
  source = "cosmoTIstanzaFunzionalitaFormLogico.cosmoRIstanzaFormLogicoParametroValores")
  IstanzaFunzionalitaFormLogico toDTO(CosmoRFormLogicoIstanzaFunzionalita input);

  @Mapping(target = "chiave", source = "cosmoDChiaveParametroFunzionalitaFormLogico.codice")
  @Mapping(target = "descrizione",
  source = "cosmoDChiaveParametroFunzionalitaFormLogico.descrizione")
  @Mapping(target = "valore", source = "valoreParametro")
  IstanzaParametroFormLogico toDTO(CosmoRIstanzaFormLogicoParametroValore input);

  @Mapping(source = "cosmoDFunzionalitaFormLogico.codice", target = "codice")
  @Mapping(ignore = true, target = "ordine")
  @Mapping(ignore = true, target = "eseguibileMassivamente")
  @Mapping(target = "parametri", source = "cosmoRIstanzaFormLogicoParametroValores")
  IstanzaFunzionalitaFormLogico toDTO(CosmoTIstanzaFunzionalitaFormLogico input);

  TipologiaFunzionalitaFormLogico toDTO(CosmoDFunzionalitaFormLogico input);

  @Mapping(source = "parametro.codice", target = "codice")
  @Mapping(source = "parametro.descrizione", target = "descrizione")
  @Mapping(source = "parametro.tipo", target = "tipo")
  @Mapping(source = "parametro.schema", target = "schema")
  @Mapping(source = "parametro.valoreDefault", target = "valoreDefault")
  TipologiaParametroFormLogico toDTO(CosmoRFunzionalitaParametroFormLogico input);

  List<TipologiaFunzionalitaFormLogico> toDTOList(List<CosmoDFunzionalitaFormLogico> input);

  @Mapping(target = "nomeFunzionalita", ignore = true)
  Funzionalita toFunzionalita(it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaFormLogico in);

  default List<IstanzaFunzionalitaFormLogico> toFunzionalitaFormLogicoDTOList(
      Collection<CosmoRFormLogicoIstanzaFunzionalita> input) {
    if (input == null || input.isEmpty()) {
      return Collections.emptyList();
    }
    return input.stream().filter(CosmoREntity::valido)
        .sorted((r1, r2) -> r1.getOrdine().compareTo(r2.getOrdine()))
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  default List<IstanzaParametroFormLogico> toParametroFormLogicoDTOList(
      Collection<CosmoRIstanzaFormLogicoParametroValore> input) {
    if (input == null || input.isEmpty()) {
      return Collections.emptyList();
    }
    return input.stream().filter(CosmoREntity::valido).map(this::toDTO)
        .collect(Collectors.toList());
  }
}
