/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.delegates;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoAuthorizationUtentiFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmocmmn.security.SecurityUtils;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;


/**
 *
 *
 */
public class CosmoDelegateImpl {

  private static final String FORMAT_DATE_ONLY = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}";

  private static final String FORMAT_ISO_DATE_ONLY = "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}";

  private static final DateTimeFormatter FORMATTER_DATE_ONLY =
      DateTimeFormatter.ofPattern("d/MM/yyyy");

  private static final String FORMAT_LOCAL_DATE_TIME =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}";

  private static final DateTimeFormatter FORMATTER_LOCAL_DATE_TIME =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String FORMAT_LOCAL_DATE_TIME_NANO =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}\\.[0-9]+";

  private static final DateTimeFormatter FORMATTER_LOCAL_DATE_TIME_NANO =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private static final String FORMAT_ZONED_DATE_TIME =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}T[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}.*";

  private static final String FORMAT_OFFSET_DATE_TIME =
      "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)$";

  protected static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "CosmoDelegateImpl");

  public static UserInfoDTO getUtenteCorrente() {
    return SecurityUtils.getUtenteCorrente();
  }

  public static ClientInfoDTO getClientCorrente() {
    return SecurityUtils.getClientCorrente();
  }

  public static String getApiUrl() {
    return ConfigurazioneService.getInstance()
        .requireConfig(ParametriApplicativo.DISCOVERY_SERVER_LOCATION).asString() + "/api/proxy";
  }

  public static UtenteResponse getUtenteFromCodiceFiscale(Object rawInput) {
    String codiceFiscale = (String) rawInput;
    CosmoAuthorizationUtentiFeignClient client =
        (CosmoAuthorizationUtentiFeignClient) SpringApplicationContextHelper
        .getBean(CosmoAuthorizationUtentiFeignClient.class);

    return client.getUtentiCodiceFiscale(codiceFiscale);
  }

  public static String toLocalDate(Object rawInput) {
    String raw = (String) rawInput;
    if (StringUtils.isBlank(raw)) {
      return raw;
    }
    var converted = stringToOffsetDateTime(raw);
    if (converted == null) {
      return null;
    }
    return converted.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static String toLocalDateTime(Object rawInput) {
    String raw = (String) rawInput;
    if (StringUtils.isBlank(raw)) {
      return raw;
    }
    var converted = stringToOffsetDateTime(raw);
    if (converted == null) {
      return null;
    }
    return converted.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public static String toOffsetDateTime(Object rawInput) {
    String raw = (String) rawInput;
    if (StringUtils.isBlank(raw)) {
      return raw;
    }
    var converted = stringToOffsetDateTime(raw);
    if (converted == null) {
      return null;
    }
    return converted.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static String applyOffsetToOffsetDateTime(Object raw, Object offset) {
    if (StringUtils.isBlank((String) raw)) {
      return null;
    }
    var converted = stringToOffsetDateTime((String) raw);
    if (converted == null) {
      return null;
    }
    var duration = Duration.parse((String) offset);
    return converted.plus(duration).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  private static LocalDate stringToLocalDate(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY);
    } else if (raw.matches(FORMAT_ISO_DATE_ONLY)) {
      return LocalDate.parse(raw, DateTimeFormatter.ISO_DATE);
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .toLocalDate();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).toLocalDate();
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw).toLocalDate();
    } else if (raw.matches(FORMAT_OFFSET_DATE_TIME)) {
      return OffsetDateTime.parse(raw).toLocalDate();
    } else {
      throw new RuntimeException("Value not translatable to LocalDate: " + raw);
    }
  }

  private static LocalDateTime stringToLocalDateTime(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0));
    } else if (raw.matches(FORMAT_ISO_DATE_ONLY)) {
      return LocalDate.parse(raw, DateTimeFormatter.ISO_DATE).atTime(LocalTime.of(0, 0));
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO);
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME);
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw).toLocalDateTime();
    } else if (raw.matches(FORMAT_OFFSET_DATE_TIME)) {
      return OffsetDateTime.parse(raw).toLocalDateTime();
    } else {
      throw new RuntimeException("Value not translatable to toLocalDateTime: " + raw);
    }
  }

  private static ZonedDateTime stringToZonedDateTime(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0))
          .atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_ISO_DATE_ONLY)) {
      return LocalDate.parse(raw, DateTimeFormatter.ISO_DATE).atTime(LocalTime.of(0, 0))
          .atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw);
    } else if (raw.matches(FORMAT_OFFSET_DATE_TIME)) {
      return OffsetDateTime.parse(raw).atZoneSameInstant(ZoneId.systemDefault());
    } else {
      throw new RuntimeException("Value not translatable to ZonedDateTime: " + raw);
    }
  }

  private static OffsetDateTime stringToOffsetDateTime(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    if (raw.matches(FORMAT_OFFSET_DATE_TIME)) {
      return OffsetDateTime.parse(raw);
    } else if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0))
          .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault())
          .toOffsetDateTime();
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return OffsetDateTime.parse(raw);
    } else {
      throw new RuntimeException("Value not translatable to OffsetDateTime: " + raw);
    }
  }

  private static String fillRight(String raw, int num, String with) {
    while (raw.length() < num) {
      raw += with;
    }
    return raw;
  }

  public static JsonNode compute(Object executionRaw, Object jsonMappingRaw) {
    DelegateExecution execution = (DelegateExecution) executionRaw;
    String jsonMapping = (String) jsonMappingRaw;

    if (StringUtils.isBlank(jsonMapping)) {
      return null;
    }

    Long idPratica = Long.valueOf(execution.getProcessInstanceBusinessKey());
    JsonNode mappingResult = richiediElaborazioneMappatura(idPratica, jsonMapping);

    return mappingResult;
  }

  private static JsonNode richiediElaborazioneMappatura(Long idPratica, String mappatura) {

    GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
    requestElaborazione.setMappatura(mappatura);

    logger.debug("richiediElaborazioneMappatura",
        "richiedo elaborazione relativa alla pratica: {}",
        mappatura);

    CosmoBusinessPraticheFeignClient client =
        (CosmoBusinessPraticheFeignClient) SpringApplicationContextHelper
        .getBean(CosmoBusinessPraticheFeignClient.class);

    return client.postPraticheIdElaborazioneJsonNode(idPratica, requestElaborazione);
  }
}
