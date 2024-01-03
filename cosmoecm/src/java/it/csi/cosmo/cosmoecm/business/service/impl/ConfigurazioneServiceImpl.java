/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service.impl;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.config.Environments;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo.ExposurePolicy;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoCConfigurazioneMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoCConfigurazioneRepository;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

@Service
public class ConfigurazioneServiceImpl
    implements ConfigurazioneService, InitializingBean, Monitorable {

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
  private CosmoCConfigurazioneRepository cosmoCConfigurazioneRepository;

  @Autowired
  private CosmoCConfigurazioneMapper cosmoCConfigurazioneMapper;

  private List<CosmoCConfigurazione> configSuDatabase;

  static {
    caricaResourceBundle();
  }

  private static ResourceBundle getConfigProperties(String env) {
    return (env == null) ? currentProfileConfiguration : propertiesPerAmbiente.get(env);
  }

  @Override
  public boolean isReady() {
    try {
      cosmoCConfigurazioneRepository.count();
      return true;
    } catch (Exception e) {
      LOGGER.warn("isReady", "Servizio di configurazione non pronto: " + e.getMessage());
      return false;
    }
  }

  @Override
  public List<ConfigurazioneDTO> getConfig() {
    return Arrays.stream(ParametriApplicativo.values())
        .filter(value -> value.getPolicyEsposizione() == ExposurePolicy.EXTERNAL)
        .map(this::getConfig).collect(Collectors.toList());
  }

  @Override
  public ServiceStatusDTO checkStatus() {

    final String name = "ConfigurazioneService";
    if (isReady()) {
      return ServiceStatusDTO.up().withName(name).withMessage("Connessione al DB disponibile")
          .build();
    } else {
      return ServiceStatusDTO.down().withName(name).withMessage("Connessione al DB non disponibile")
          .build();
    }
  }

  @Override
  @Scheduled(cron = "${get.valori.props.timeInterval}")
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("afterPropertiesSet", "Inizializzato servizio di configurazione");

    if (!isTest) {
      verificaConfigurazionePacchetti();
    }
  }

  public static synchronized Map<String, String> getMergedConfiguration() {
    if (null == mergedConfiguration) {
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

  private synchronized void ensureDatabaseConfigurationLoaded() {
      this.configSuDatabase = this.cosmoCConfigurazioneRepository.findAllActive();
  }

  @Transactional
  public ConfigurazioneDTO getFromDatabase(String key) {
    ensureDatabaseConfigurationLoaded();

    String componentSpecificKey = Constants.COMPONENT_NAME.toLowerCase() + "://" + key;

    CosmoCConfigurazione configurazioneRecord = this.configSuDatabase.stream()
        .filter(c -> c.getChiave().equals(componentSpecificKey)).findFirst().orElse(null);

    if (configurazioneRecord == null) {
      configurazioneRecord = this.configSuDatabase.stream().filter(c -> c.getChiave().equals(key))
          .findFirst().orElse(null);
    }

    ConfigurazioneDTO configurazioneDTO = null;

    if (configurazioneRecord != null) {
      configurazioneDTO = cosmoCConfigurazioneMapper.toDTO(configurazioneRecord);
    }

    return configurazioneDTO;
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

    // verifico che siano presenti tutti i parametri obbligatori per tutte le
    // configurazioni
    for (ParametriApplicativo parametro : ParametriApplicativo.values()) {
      verificaParametro(parametro, map, errorMessages);
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
      Map<ParametriApplicativo, ConfigurazioneDTO> map, List<String> errorMessages) {
    if (parametro.isObbligatorio()) {
      final String method = "verificaParametro";
      LOGGER.debug(method, "Verifico configurazione del parametro " + parametro.getCodice() + ": ");

      // verifico per la configurazione corrente
      ConfigurazioneDTO result = fetchConfig(null, parametro, true, true, true);
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
      output.put(PRODOTTO, Constants.PRODUCT);
      output.put(COMPONENTE, Constants.COMPONENT_NAME);
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

  @Override
  public boolean isTestModeEnabled() {
    return this.getConfig(ParametriApplicativo.TESTMODE_ENABLED).asBool();
  }

}
