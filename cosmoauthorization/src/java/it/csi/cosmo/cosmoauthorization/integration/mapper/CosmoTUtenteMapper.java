/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTPreferenzeUtenteMapper.class,
    CosmoTGruppoMapper.class, CosmoRUtenteEnteMapper.class, CosmoRUtenteProfiloMapper.class,},
componentModel = "spring")
public interface CosmoTUtenteMapper {

  RiferimentoUtente toRiferimentoDTO(CosmoTUtente input);

  @Mapping(source = "cosmoTGruppos", target = "gruppi")
  @Mapping(source = "cosmoTPreferenzeUtentes", target = "preferenze")
  @Mapping(source = "cosmoRUtenteEntes", target = "enti")
  @Mapping(source = "cosmoRUtenteProfilos", target = "profili")
  Utente toDTO(CosmoTUtente input);

  @Mapping(ignore = true, target = "gruppi")
  @Mapping(ignore = true, target = "preferenze")
  @Mapping(ignore = true, target = "enti")
  @Mapping(ignore = true, target = "profili")
  Utente toLightDTO(CosmoTUtente input);

  @Mapping(source = "preferenze", target = "cosmoTPreferenzeUtentes")
  @Mapping(ignore = true, target = "cosmoTGruppos")
  @Mapping(ignore = true, target = "cosmoRNotificaUtenteEntes")
  @Mapping(ignore = true, target = "cosmoRPraticaUtenteGruppos")
  @Mapping(ignore = true, target = "cosmoRUtenteEntes")
  @Mapping(ignore = true, target = "cosmoRUtenteProfilos")
  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoRUtenteFunzionalitaApplicazioneEsternas")
  @Mapping(ignore = true, target = "cosmoTUtenteGruppos")
  @Mapping(ignore = true, target = "cosmoTCaricamentoPraticas")
  CosmoTUtente toRecord(Utente input);

  @Named("utente_toDTOSenzaEntiEGruppi")
  @Mapping(ignore = true, target = "gruppi")
  @Mapping(source = "cosmoTPreferenzeUtentes", target = "preferenze")
  @Mapping(ignore = true, target = "enti")
  @Mapping(source = "cosmoRUtenteProfilos", target = "profili")
  Utente toDTOSenzaEntiEGruppi(CosmoTUtente input);


}
