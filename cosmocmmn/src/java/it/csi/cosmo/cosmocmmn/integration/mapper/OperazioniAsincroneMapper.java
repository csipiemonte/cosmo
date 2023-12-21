/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.mapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskMessage;
import it.csi.cosmo.common.async.model.LongTaskMessage.LongTaskMessageType;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

public abstract class OperazioniAsincroneMapper {

  public static LongTaskPersistableEntry toEntity(OperazioneAsincrona entity) {
    if (entity == null) {
      return null;
    }

    LongTaskPersistableEntry metadati = new LongTaskPersistableEntry();

    metadati.setUuid(entity.getUuid());
    metadati.setName(entity.getNome());
    metadati.setStartedAt(entity.getDataAvvio());
    metadati.setStatus(LongTaskStatus.valueOf(entity.getStato()));
    metadati.setVersion(entity.getVersione());

    metadati.setErrorDetails(entity.getDettagliErrore());
    metadati.setErrorMessage(entity.getErrore());
    metadati.setFinishedAt(entity.getDataFine());
    metadati.setMessages(entity.getMessaggi() != null ? entity.getMessaggi().stream()
        .map(OperazioniAsincroneMapper::toEntity).collect(Collectors.toCollection(LinkedList::new))
        : null);
    metadati.setResult(ObjectUtils.toJson(entity.getRisultato()));
    metadati.setSteps(entity.getSteps() != null ? entity.getSteps().stream()
        .map(OperazioniAsincroneMapper::toEntity).collect(Collectors.toCollection(LinkedList::new))
        : null);

    return metadati;
  }

  public static LongTaskMessage toEntity(MessaggioOperazioneAsincrona input) {
    if (input == null) {
      return null;
    }

    LongTaskMessage longTaskMessage = new LongTaskMessage();

    longTaskMessage.setText(input.getTesto());
    if (input.getLivello() != null) {
      longTaskMessage.setType(Enum.valueOf(LongTaskMessageType.class, input.getLivello()));
    }
    longTaskMessage.setTimestamp(input.getTimestamp());

    return longTaskMessage;
  }

  public static OperazioneAsincrona toDTO(LongTaskPersistableEntry input) {
    if (input == null) {
      return null;
    }

    OperazioneAsincrona operazioneAsincrona = new OperazioneAsincrona();

    if (input.getStatus() != null) {
      operazioneAsincrona.setStato(input.getStatus().name());
    }
    operazioneAsincrona.setVersione(input.getVersion());
    operazioneAsincrona
        .setMessaggi(longTaskMessageListToMessaggioOperazioneAsincronaList(input.getMessages()));
    operazioneAsincrona.setDataFine(input.getFinishedAt());
    operazioneAsincrona.setErrore(input.getErrorMessage());
    operazioneAsincrona.setDettagliErrore(input.getErrorDetails());
    operazioneAsincrona.setNome(input.getName());
    try {
      operazioneAsincrona.setRisultato(fromJSON(input.getResult()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    operazioneAsincrona.setDataAvvio(input.getStartedAt());
    operazioneAsincrona.setUuid(input.getUuid());
    operazioneAsincrona
        .setSteps(longTaskPersistableEntryListToOperazioneAsincronaList(input.getSteps()));

    return operazioneAsincrona;
  }

  public static MessaggioOperazioneAsincrona toDTO(LongTaskMessage input) {
    if (input == null) {
      return null;
    }

    MessaggioOperazioneAsincrona messaggioOperazioneAsincrona = new MessaggioOperazioneAsincrona();

    if (input.getType() != null) {
      messaggioOperazioneAsincrona.setLivello(input.getType().name());
    }
    messaggioOperazioneAsincrona.setTesto(input.getText());
    messaggioOperazioneAsincrona.setTimestamp(input.getTimestamp());

    return messaggioOperazioneAsincrona;
  }

  public static List<MessaggioOperazioneAsincrona> longTaskMessageListToMessaggioOperazioneAsincronaList(
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

  public static List<OperazioneAsincrona> longTaskPersistableEntryListToOperazioneAsincronaList(
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

  public static Object fromJSON(String raw) throws IOException {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return ObjectUtils.getDataMapper().readTree(raw);
  }

}
