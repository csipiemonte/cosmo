/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoEnte;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTPreferenzeEnteMapper.class, CosmoTGruppoMapper.class},
componentModel = "spring")
public interface CosmoTEnteMapper {

  RiferimentoEnte toRiferimentoDTO(CosmoTEnte input);

  @Mapping(source = "preferenze", target = "cosmoTPreferenzeEntes")
  @Mapping(ignore = true, target = "cosmoTGruppos")
  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "cosmoRUtenteEntes")
  @Mapping(ignore = true, target = "cosmoRFruitoreEntes")
  @Mapping(ignore = true, target = "cosmoRUtenteProfilos")
  @Mapping(ignore = true, target = "cosmoTPraticas")
  @Mapping(ignore = true, target = "codiceFiscale")
  @Mapping(ignore = true, target = "cosmoREnteCertificatoreEntes")
  @Mapping(ignore = true, target = "cosmoREnteApplicazioneEsternas")
  @Mapping(ignore = true, target = "cosmoREnteFunzionalitaApplicazioneEsternas")
  @Mapping(ignore = true, target = "cosmoRNotificaUtenteEntes")
  @Mapping(ignore = true, target = "cosmoTFormLogicos")
  @Mapping(ignore = true, target = "cosmoCConfigurazioneEntes")
  @Mapping(ignore = true, target = "cosmoTCaricamentoPraticas")
  @Mapping(ignore = true, target = "cosmoTOtps")
  @Mapping(ignore = true, target = "cosmoTMessaggioNotificas")
  CosmoTEnte toRecord(Ente input);

  @Named("ente_toDTO")
  @Mapping(source = "cosmoTGruppos", target = "gruppi")
  @Mapping(source = "cosmoTPreferenzeEntes", target = "preferenze")
  @Mapping(ignore = true, target = "codiceProfiloDefault")
  Ente toDTO(CosmoTEnte input);


  @Mapping(ignore = true, target = "logo")
  @Mapping(source = "cosmoTGruppos", target = "gruppi")
  @Mapping(source = "cosmoTPreferenzeEntes", target = "preferenze")
  @Mapping(ignore = true, target = "codiceProfiloDefault")
  Ente toDTOSenzaLogo(CosmoTEnte input);

  @Named("ente_toDTOSenzaGruppiELogo")
  @Mapping(ignore = true, target = "logo")
  @Mapping(ignore = true, target = "gruppi")
  @Mapping(source = "cosmoTPreferenzeEntes", target = "preferenze")
  @Mapping(ignore = true, target = "codiceProfiloDefault")
  Ente toDTOSenzaGruppiELogo(CosmoTEnte input);

}
