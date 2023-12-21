/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service;

import java.util.List;
import java.util.Map;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.cosmo.business.service.impl.ConfigurazioneServiceImpl;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametriResponse;

/**
 * Servizio per la gestione della configurazione
 */
public interface ConfigurazioneService {

  boolean isReady();

  /**
   * Restituisce la configurazione del parametro passato in ingresso
   *
   * @param raw e' il parametro di cui si vuole la configurazione
   * @return e' un dto con le informazioni con la configurazione del paramentro di ingresso, trovate
   *         nelle properties o nel database
   */
  ConfigurazioneDTO getConfig(String raw);

  /**
   * Restituisce la configurazione del parametro passato in ingresso
   *
   * @param raw e' il parametro di cui si vuole la configurazione
   * @return e' un dto con le informazioni con la configurazione del paramentro di ingresso, trovate
   *         nelle properties o nel database
   */
  ConfigurazioneDTO getConfig(ParametriApplicativo parametro);

  /**
   * Restituisce la configurazione del parametro passato in ingresso
   *
   * @param raw e' il parametro di cui si vuole la configurazione
   * @return e' un dto con le informazioni con la configurazione del paramentro di ingresso, trovate
   *         nelle properties o nel database
   */
  ConfigurazioneDTO requireConfig(ParametriApplicativo parametro);

  /**
   * Ottiene le informazioni dell'applicativo dalle properties
   *
   * @return una mappa con informazioni relative a "prodotto","componente","versione" e"ambiente"
   */
  Map<String, Object> getBuildProperties();

  static ConfigurazioneService getInstance() {
    return (ConfigurazioneService) SpringApplicationContextHelper
        .getBean(ConfigurazioneServiceImpl.class.getSimpleName());
  }

  /**
   * Restituisce tutti i parametri di configurazione dell'applicativo con ExposurePolicy.EXTERNAL
   *
   * @return tutti i parametri con ExposurePolicy.EXTERNAL
   */
  List<ConfigurazioneDTO> getConfig();

  /**
   * Metoto usato dall'API di configurazione. Utilizza getConfig() e getConfig(String raw)
   *
   * @param raw
   * @return uno o tutti i parametri (a seconda dell'input fornito) imbustati un oggetto
   *         ParametriResponse
   */
  ParametriResponse getConfigAPI(String raw);

  /**
   * Metodo usato per capire se l'ambiente di esecuzione e' di test o meno
   *
   * @return il valore della property testmode.enabled
   */
  boolean isTestModeEnabled();

  ConfigurazioneDTO requireConfigFromAuthorization(ParametriApplicativo parametroApplicativo);

}
