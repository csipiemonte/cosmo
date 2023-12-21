/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.LinkedList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoTApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsterna;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTFunzionalitaApplicazioneEsternaMapper.class},
componentModel = "spring")
public interface CosmoTApplicazioneEsternaMapper {

  @Mapping(source = "cosmoREnteApplicazioneEsternas", target = "funzionalita",
      qualifiedByName = "funzionalita_mapping")
  @Mapping(ignore = true, target = "posizione")
  ApplicazioneEsterna toDTO(CosmoTApplicazioneEsterna input);

  @Mapping(ignore = true, target = "funzionalitaPrincipale")
  @Mapping(ignore = true, target = "campiTecnici")
  @Mapping(ignore = true, target = "associataUtenti")
  @Mapping(ignore = true, target = "associataEnti")
  @Mapping(ignore = true, target = "numEntiAssociati")
  ApplicazioneEsternaConValidita toDTOconValidita(CosmoTApplicazioneEsterna input);

  List<ApplicazioneEsterna> toDTOList(List<CosmoTApplicazioneEsterna> input);

  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "cosmoREnteApplicazioneEsternas")
  CosmoTApplicazioneEsterna toRecord(ApplicazioneEsterna input);

  @Named("funzionalita_mapping")
  default List<FunzionalitaApplicazioneEsterna> mapFunzionalita(List<CosmoREnteApplicazioneEsterna> input) {

    List<FunzionalitaApplicazioneEsterna> output = new LinkedList<>();

    if (null != input && !input.isEmpty()) {
      input.forEach(enteApplicazione -> enteApplicazione.getCosmoTFunzionalitaApplicazioneEsternas()
          .forEach(funzionalita -> {
            FunzionalitaApplicazioneEsterna singolaFunzionalita =
                new FunzionalitaApplicazioneEsterna();
            singolaFunzionalita.setId(funzionalita.getId());
            singolaFunzionalita.setDescrizione(funzionalita.getDescrizione());
            singolaFunzionalita.setUrl(funzionalita.getUrl());
            singolaFunzionalita.setPrincipale(funzionalita.getPrincipale());
            output.add(singolaFunzionalita);
          }));
    }

    return output;
  }


}
