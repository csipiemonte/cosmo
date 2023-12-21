/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service.impl;

import java.util.Arrays;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import it.csi.cosmo.common.config.Environments;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.config.Constants;
import it.csi.cosmo.cosmo.config.ErrorMessages;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.config.ParametriApplicativo.ExposurePolicy;
import it.csi.cosmo.cosmo.integration.rest.CosmoAuthorizationConfigurazioneParametriClient;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Parametro;

@PropertySource("classpath:config.properties")
@Service
public class ConfigurazioneServiceImpl implements ConfigurazioneService, InitializingBean {

  private static final String AMBIENTE = "ambiente";

  private static final String VERSIONE = "versione";

  private static final String COMPONENTE = "componente";

  private static final String PRODOTTO = "prodotto";

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ConfigurazioneServiceImpl");

  private static ResourceBundle currentProfileConfiguration;

  private static ResourceBundle buildProperties;

  private static ResourceBundle commonProperties;

  private static Map<String, ResourceBundle> propertiesPerAmbiente;

  private static Environments currentEnvironment;

  private static boolean isTest = false;

  private static Map<String, String> mergedConfiguration = null;

  @Autowired
  CosmoAuthorizationConfigurazioneParametriClient cosmoAuthorizationConfigurazioneParametriClient;

  static {
    caricaResourceBundle();
  }

  private static ResourceBundle getConfigProperties(String env) {
    return (env == null) ? currentProfileConfiguration : propertiesPerAmbiente.get(env);
  }

  @Override
  public boolean isReady() {
    try {
      requireConfigFromAuthorization(ParametriApplicativo.TESTMODE_ENABLED);
      return true;
    } catch (Exception e) {
      LOGGER.warn("isReady", "Servizio di configurazione non pronto: " + e.getMessage());
      return false;
    }
  }

  @Override
  @Scheduled(cron = "${get.valori.props.timeInterval}")
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("afterPropertiesSet", "Inizializzato servizio di configurazione per l'ambiente "
        + this.getConfig(ParametriApplicativo.NOME_AMBIENTE).asString("[NULL]"));

    if (!isTest) {
      verificaConfigurazionePacchetti();
    }
  }

  public static Map<String, String> getMergedConfiguration() {
    if (mergedConfiguration == null) {
      Map<String, String> builtMergedConfiguration = new HashMap<>();

      for (String key : currentProfileConfiguration.keySet()) {
        builtMergedConfiguration.put(key, currentProfileConfiguration.getString(key));
      }
      for (String key : commonProperties.keySet()) {
        builtMergedConfiguration.putIfAbsent(key, commonProperties.getString(key));
      }

      mergedConfiguration = builtMergedConfiguration;
    }
    return mergedConfiguration;
  }

  @Override
  @Cacheable(value = Constants.CACHES.CONFIGURATION, key = "#parametro")
  public ConfigurazioneDTO getConfig(String parametro) {
    return fetchConfig(null, parametro, true, true, true);
  }

  @Override
  @Cacheable(value = Constants.CACHES.CONFIGURATION, key = "#parametro.getCodice()")
  public ConfigurazioneDTO getConfig(ParametriApplicativo parametro) {
    ConfigurazioneDTO result = fetchConfig(null, parametro, true, true, true);

    if ((result == null || result.isEmpty()) && parametro.isObbligatorio()) {
      throw new BadConfigurationException(
          String.format("Il parametro %s e' obbligatorio ma non e' configurato", parametro));

    }

    return result;
  }

  @Override
  public ParametriResponse getConfigAPI(String raw) {

    List<ConfigurazioneDTO> parametriDTO = new LinkedList<>();
    if (StringUtils.hasText(raw)) {
      ConfigurazioneDTO singoloParametro = this.getConfig(raw);
      if (null != singoloParametro) {
        parametriDTO.add(singoloParametro);
      }

    } else {
      parametriDTO = this.getConfig();
    }
    ParametriResponse response = new ParametriResponse();

    response.setParametri(new LinkedList<>());
    if (null != parametriDTO) {
      parametriDTO.forEach(parametroDTO -> {
        Parametro parametro = new Parametro();
        parametro.setChiave(parametroDTO.getKey());
        parametro.setValore(parametroDTO.getValue());
        response.getParametri().add(parametro);
      });
    }

    return response;
  }

  @Override
  public List<ConfigurazioneDTO> getConfig() {
    return Arrays.stream(ParametriApplicativo.values())
        .filter(value -> value.getPolicyEsposizione() == ExposurePolicy.EXTERNAL)
        .map(this::getConfig).collect(Collectors.toList());
  }

  @Override
  @Cacheable(value = Constants.CACHES.CONFIGURATION, key = "#parametro.getCodice()")
  public ConfigurazioneDTO requireConfig(ParametriApplicativo parametro) {

    ConfigurazioneDTO found = fetchConfig(null, parametro, true, true, true);
    if (found == null || found.isEmpty()) {
      throw new BadConfigurationException(
          String.format("Il parametro %s e' richiesto ma non e' configurato", parametro));
    }

    return found;
  }

  private ConfigurazioneDTO fetchConfig(Environments ambiente, String key, boolean fromDB,
      boolean fromCommonProperties, boolean fromCurrentProperties) {

    return fetchConfig(ambiente, key, null, fromDB, fromCommonProperties, fromCurrentProperties);
  }

  private ConfigurazioneDTO fetchConfig(Environments ambiente, ParametriApplicativo parametro,
      boolean fromDB, boolean fromCommonProperties, boolean fromCurrentProperties) {

    return fetchConfig(ambiente, null, parametro, fromDB, fromCommonProperties,
        fromCurrentProperties);
  }

  private ConfigurazioneDTO fetchConfig(Environments ambiente, String key,
      ParametriApplicativo parametro, boolean fromDB, boolean fromCommonProperties,
      boolean fromCurrentProperties) {

    if (key == null) {
      key = parametro.getCodice();
    }

    final String method = "fetchConfig";
    LOGGER.debug(method, "avvio risoluzione parametro " + key);

    Set<String> variazioni = getVariazioniChiave(key);

    ConfigurazioneDTO configurazione = null;

    if (fromDB) {
      LOGGER.trace(method, "ricerco parametro [{}] e relative varianti su database", key);
      configurazione = risolviDaDatabase(variazioni);
    }

    if (configurazione == null && fromCurrentProperties) {
      LOGGER.trace(method,
          "ricerco parametro [{}] e relative varianti su file di properties per la configurazione corrente",
          key);
      configurazione = risolviDaResourceBundle(
          getConfigProperties(ambiente != null ? ambiente.getCodice() : null), variazioni);
    }

    if (configurazione == null && fromCommonProperties) {
      LOGGER.trace(method, "ricerco parametro [{}] e relative varianti su file common.properties",
          key);
      configurazione = risolviDaResourceBundle(commonProperties, variazioni);
    }

    if (parametro == null) {
      return configurazione == null ? null
          : ConfigurazioneDTO.builder(key, configurazione.getValue()).build();
    } else {
      return buildForParam(configurazione, key, parametro);
    }
  }

  private ConfigurazioneDTO buildForParam(ConfigurazioneDTO configurazione, String key,
      ParametriApplicativo parametro) {
    final String method = "buildForParam";

    if (configurazione == null) {
      LOGGER.trace(method, "Parametro di configurazione {} non risolvibile", key);
      configurazione = ConfigurazioneDTO.builder(key, null).build();
    }

    LOGGER.debug(method, "risolto parametro di configurazione {} al valore [{}]", key,
        (parametro.getPolicyEsposizione() == ExposurePolicy.PUBLIC ? configurazione.getValue()
            : "<*******>"));

    return configurazione == null ? null
        : ConfigurazioneDTO.builder(parametro.getCodice(), configurazione.getValue()).build();
  }

  protected ConfigurazioneDTO getFromDatabase(String key) {
    LOGGER.trace("getFromDatabase",
        "configuration retrieval from database is not implemented. Skipping check for key {}", key);
    return null;
  }

  private Set<String> getVariazioniChiave(String raw) {
    Set<String> variazioni = new LinkedHashSet<>();

    variazioni.add(raw);
    variazioni.add(raw.toUpperCase());
    variazioni.add(raw.toLowerCase());
    variazioni.add(raw.toUpperCase().replaceAll("[^A-Z0-9]+", "_"));
    variazioni.add(raw.toLowerCase().replaceAll("[^a-z0-9]+", "."));
    variazioni.add(raw.toUpperCase().replaceAll("[^A-Z0-9]+", "."));
    variazioni.add(raw.toLowerCase().replaceAll("[^a-z0-9]+", "_"));

    return variazioni;
  }

  private ConfigurazioneDTO risolviDaResourceBundle(ResourceBundle rb,
      Collection<String> variazioniChiave) {
    ConfigurazioneDTO resolved = null;
    for (String variazioneChiave : variazioniChiave) {
      final String method = "risolviDaResourceBundle";
      LOGGER.trace(method, "ricerco parametro [" + variazioneChiave + "] su file di properties");
      if (rb.containsKey(variazioneChiave)) {
        if (resolved == null) {
          resolved =
              ConfigurazioneDTO.builder(variazioneChiave, rb.getString(variazioneChiave)).build();
        } else {
          LOGGER.error(method, String
              .format(ErrorMessages.PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI, variazioneChiave));
          throw new BadConfigurationException(String
              .format(ErrorMessages.PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI, variazioneChiave));
        }
      }
    }
    return resolved;
  }

  private ConfigurazioneDTO risolviDaDatabase(Collection<String> variazioniChiave) {
    ConfigurazioneDTO resolved = null;
    for (String variazioneChiave : variazioniChiave) {
      final String method = "risolviDaDatabase";
      LOGGER.trace(method, "ricerco parametro [{}] su database", variazioneChiave);
      ConfigurazioneDTO onDB = getFromDatabase(variazioneChiave);
      if (onDB != null) {
        if (resolved == null) {
          resolved = onDB;
        } else {
          LOGGER.error(method, String
              .format(ErrorMessages.PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI, variazioneChiave));
          throw new BadConfigurationException(String
              .format(ErrorMessages.PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI, variazioneChiave));

        }
      }
    }
    return resolved;
  }

  private static void caricaResourceBundle() {

    currentProfileConfiguration = ResourceBundle.getBundle("config");

    if (currentProfileConfiguration.getString("nome.ambiente").equals("JUNIT")) {
      currentEnvironment = Environments.LOCAL;
      isTest = true;
      commonProperties = currentProfileConfiguration;
    } else {

      ResourceBundle runtimeProperties = ResourceBundle.getBundle("properties-cache/runtime");
      buildProperties = ResourceBundle.getBundle("properties-cache/build");
      ResourceBundle environmentProperties = ResourceBundle.getBundle("properties-cache/env");
      commonProperties = ResourceBundle.getBundle("properties-cache/common");

      currentEnvironment = Environments.fromCodice(runtimeProperties.getString("target"));

      propertiesPerAmbiente = new HashMap<>();
      propertiesPerAmbiente.put("config", currentProfileConfiguration);
      propertiesPerAmbiente.put("runtime", runtimeProperties);
      propertiesPerAmbiente.put("build", buildProperties);
      propertiesPerAmbiente.put("env", environmentProperties);
      propertiesPerAmbiente.put("common", commonProperties);

      for (Environments env : Environments.values()) {
        try {
          propertiesPerAmbiente.put(env.getCodice(),
              ResourceBundle.getBundle("properties-cache/" + env.getCodice()));
        } catch (Exception e) {
          LOGGER.error("caricaResourceBundle",
              String.format(ErrorMessages.ERRORE_VERIFICA_FILE_PROPERTIES, env.getCodice()), e);
        }

      }
    }

    getMergedConfiguration();
  }

  private void verificaConfigurazionePacchetti() {
    Map<ParametriApplicativo, ConfigurazioneDTO> map = new EnumMap<>(ParametriApplicativo.class);

    List<String> errorMessages = new LinkedList<>();

    // verifico che sia correttamente configurato l'ambiente corrente
    final String method = "verificaConfigurazionePacchetti";
    if (currentEnvironment == null) {
      LOGGER.error(method, ErrorMessages.AMBIENTE_CORRENTE_NON_CORRETTO);
      throw new BadConfigurationException(ErrorMessages.AMBIENTE_CORRENTE_NON_CORRETTO);
    }

    boolean isReady = isReady();
    
    // verifico che siano presenti tutti i parametri obbligatori per tutte le
    // configurazioni
    for (ParametriApplicativo parametro : ParametriApplicativo.values()) {
      verificaParametro(parametro, map, errorMessages, isReady);
    }

    if (!errorMessages.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder(
          "****************** \n\n\tERRORE: Sono presenti degli errori bloccanti nella configurazione dei profili: \n");
      for (String error : errorMessages) {
        errorMessage.append("\t- " + error + "\n");
      }
      errorMessage.append(
          "\n\nATTENZIONE: il parametro puo' essere configurato nella apposita tabella del database di ogni ambiente, "
              + "ma e' comunque richiesta la configurazione di un valore di fallback a livello di configurazione delle properties per ogni ambiente. \n"
              + "In alternativa e' possibile configurare un valore di fallback comune a tutti gli ambienti utilizzando solamente il file common.properties."
              + "\n\n******************\n");

      LOGGER.error(method, errorMessage.toString());
      LOGGER.error(method, ErrorMessages.ERRORI_NELLA_CONFIGURAZIONE_DEI_PROFILI);


      throw new BadConfigurationException(ErrorMessages.ERRORI_NELLA_CONFIGURAZIONE_DEI_PROFILI);
    } else {
      // stampa un riepilogo
      stampaRiepilogoConfigurazione(map);
    }
  }

  private void verificaParametro(ParametriApplicativo parametro,
      Map<ParametriApplicativo, ConfigurazioneDTO> map, List<String> errorMessages, boolean isReady) {
    if (parametro.isObbligatorio()) {
      final String method = "verificaParametro";
      LOGGER.debug(method, "Verifico configurazione del parametro " + parametro.getCodice() + ": ");

      ConfigurazioneDTO result = null;
      
      // verifico per la configurazione corrente
      if(isReady) {
      result = requireConfigFromAuthorization(parametro);
      }else {
      result = fetchConfig(null, parametro, true, true, true);
      }
      if (result == null || result.isEmpty()) {
        errorMessages.add("Il parametro " + parametro.getCodice()
        + " e' richiesto ma non e' configurato nel file di properties corrente ne' su database.");
      } else {
        LOGGER.debug(method,
            "parametro configurato " + parametro.getCodice() + "=[" + result.asString() + "]");
        map.put(parametro, result);
      }

      // verifico anche per gli altri profili
      for (Environments env : Environments.values()) {
        if (env == null) {
          continue;
        }
        result = fetchConfig(env, parametro, false, true, true);
        if (result == null || result.isEmpty()) {
          errorMessages.add("Il parametro " + parametro.getCodice()
          + " e' richiesto ma non e' configurato nel file di properties per l'ambiente "
          + env.name() + " (" + env.getCodice() + ").\n\tConfigurare la voce nel file "
          + env.getCodice() + ".properties oppure nel file common.properties");
        }
      }
    }
  }

  private void stampaRiepilogoConfigurazione(Map<ParametriApplicativo, ConfigurazioneDTO> map) {
    for (Entry<ParametriApplicativo, ConfigurazioneDTO> entry : map.entrySet()) {
      LOGGER.info("stampaRiepilogoConfigurazione", "configurazione parametro [{}]=[{}]",
          entry.getKey().getCodice(),
          (entry.getKey().getPolicyEsposizione() == ExposurePolicy.PUBLIC
          ? entry.getValue().getValue()
              : "<*******>"));
    }
  }

  @Override
  public Map<String, Object> getBuildProperties() {
    Map<String, Object> output = new HashMap<>();

    if (!isTest) {
      output.put(PRODOTTO, buildProperties.getString(PRODOTTO));
      output.put(COMPONENTE, buildProperties.getString(COMPONENTE));
      output.put(VERSIONE, buildProperties.getString("version"));
      output.put(AMBIENTE, currentEnvironment.getCodice());
    } else {
      output.put(PRODOTTO, "cosmo");
      output.put(COMPONENTE, "cosmo");
      output.put(VERSIONE, "1.0.0");
      output.put(AMBIENTE, "local");
    }

    return output;
  }

  public static Environments getCurrentEnvironment() {
    return currentEnvironment;
  }

  public static boolean isUnitTest() {
    return isTest;
  }

  public static boolean isLocal() {
    return currentEnvironment == Environments.LOCAL;
  }

  public static boolean isProd() {
    return currentEnvironment == Environments.PROD;
  }

  @Override
  public boolean isTestModeEnabled() {
    return this.getConfig(ParametriApplicativo.TESTMODE_ENABLED).asBool();
  }

  @Override
  public ConfigurazioneDTO requireConfigFromAuthorization(
      ParametriApplicativo parametroApplicativo) {



    final String method = "risolviDaDatabase";
    LOGGER.trace(method, "ricerco parametro [{}] su database", parametroApplicativo.getCodice());
    ConfigurazioneDTO onDB = getFromCosmoAuthorization(parametroApplicativo.getCodice());

    if (onDB != null)
      return onDB;
    return fetchConfig(null, null, parametroApplicativo, false, true, true);




  }

  private ConfigurazioneDTO getFromCosmoAuthorization(String variazioneChiave) {

    ParametriResponse parametriResponse = this.cosmoAuthorizationConfigurazioneParametriClient
        .getConfigurazioneParametro(variazioneChiave);

    if (parametriResponse == null || parametriResponse.getParametri() == null)
      return null;

    return parametriResponse.getParametri().stream()
        .filter(temp -> temp.getChiave().equals(variazioneChiave)).findFirst().map(temp -> {
          ConfigurazioneDTO configurazioneDTO = new ConfigurazioneDTO();
          configurazioneDTO.setKey(variazioneChiave);
          configurazioneDTO.setValue(temp.getValore());
          return configurazioneDTO;

        }).orElse(null);

  }

}
