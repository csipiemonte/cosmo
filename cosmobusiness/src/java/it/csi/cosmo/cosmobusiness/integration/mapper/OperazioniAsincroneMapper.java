/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.async.model.LongTaskMessage;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface OperazioniAsincroneMapper {

  @Mapping(target = "uuid", source = "uuid")
  @Mapping(target = "nome", source = "name")
  @Mapping(target = "versione", source = "version")
  @Mapping(target = "dataAvvio", source = "startedAt")
  @Mapping(target = "dataFine", source = "finishedAt")
  @Mapping(target = "errore", source = "errorMessage")
  @Mapping(target = "dettagliErrore", source = "errorDetails")
  @Mapping(target = "messaggi", source = "messages")
  @Mapping(target = "stato", source = "status")
  @Mapping(target = "risultato", source = "result", qualifiedByName = "JSONtoObject")
  OperazioneAsincrona toDTO(LongTaskPersistableEntry input);

  @Mapping(target = "livello", source = "type")
  @Mapping(target = "testo", source = "text")
  @Mapping(target = "timestamp", source = "timestamp")
  MessaggioOperazioneAsincrona toDTO(LongTaskMessage input);

  @Mapping(target = "type", source = "livello")
  @Mapping(target = "text", source = "testo")
  @Mapping(target = "timestamp", source = "timestamp")
  LongTaskMessage toEntity(MessaggioOperazioneAsincrona input);

  @Named("JSONtoObject")
  default Object fromJSON(String raw) throws IOException {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return ObjectUtils.getDataMapper().readTree(raw);
  }

  default List<MessaggioOperazioneAsincrona> longTaskMessageListToMessaggioOperazioneAsincronaList(
      List<LongTaskMessage> list) {
    if (list == null) {
      return null;
    }

    List<MessaggioOperazioneAsincrona> list1 = new LinkedList<MessaggioOperazioneAsincrona>();
    for (LongTaskMessage longTaskMessage : list) {
      list1.add(toDTO(longTaskMessage));
    }

    return list1;
  }

  default List<OperazioneAsincrona> longTaskPersistableEntryListToOperazioneAsincronaList(
      List<LongTaskPersistableEntry> list) {
    if (list == null) {
      return null;
    }

    List<OperazioneAsincrona> list1 = new LinkedList<OperazioneAsincrona>();
    for (LongTaskPersistableEntry longTaskPersistableEntry : list) {
      list1.add(toDTO(longTaskPersistableEntry));
    }

    return list1;
  }
}
