/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FruitoriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.OperazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;

/**
 *
 */

public interface FruitoriService {

  /**
   * Metodo per eliminare un fruitore dal database
   *
   * @param id e' l'identificativo numerico del fruitore da eliminare
   */
  Fruitore deleteFruitore(Long id);

  /**
   * Metodo che restituisce una lista di fruitori validi. Questa lista ha un numero di elementi pari
   * al valore indicato da filter o, se filter non e' valorizzato, un numero di elementi pari al
   * valore definito nella tabella di configurazione
   *
   * @param filter indica i filtri da utilizzare nella ricerca dei fruitori
   * @return una lista di fruitori validi
   */
  FruitoriResponse getFruitori(String filter);

  /**
   * Metodo che restituisce un fruitore specifico
   *
   * @param apiManangerId del fruitore che deve essere cercato nel database
   * @return fruitore presente nel database
   */
  Fruitore getFruitoreApiManagerId(String apiManangerId);

  /**
   * Metodo che restituisce un fruitore specifico
   *
   * @param id del fruitore che deve essere cercato nel database
   * @return fruitore presente nel database
   */
  Fruitore getFruitore(Long id);

  /**
   * Metodo che inserisce un fruitore sul database
   *
   * @param fruitore e' il fruitore che deve essere inserito sul database
   * @return il fruitore inserito sul database
   */
  Fruitore createFruitore(CreaFruitoreRequest fruitore);

  /**
   * Metodo che aggiorna un fruitore gia' esistente nel database
   *
   * @param fruitore e' il fruitore che deve essere aggiornato
   * @return il fruitore aggiornato nel database
   */
  Fruitore putFruitore(Long id, AggiornaFruitoreRequest fruitore);

  List<OperazioneFruitore> getOperazioniFruitore();

  /**
   * @param idFruitore
   * @param idEndpoint
   */
  void deleteEndpointFruitore(Long idFruitore, Long idEndpoint);

  /**
   * @param idFruitore
   * @param idSchemaAutenticazione
   */
  void deleteSchemaAuthFruitore(Long idFruitore, Long idSchemaAutenticazione);

  /**
   * @param idFruitore
   * @param body
   * @return
   */
  EndpointFruitore postEndpointsFruitori(Long idFruitore, CreaEndpointFruitoreRequest body);

  /**
   * @param idFruitore
   * @param body
   * @return
   */
  SchemaAutenticazioneFruitore postSchemiAuthFruitori(Long idFruitore,
      CreaSchemaAutenticazioneFruitoreRequest body);

  /**
   * @param idFruitore
   * @param idEndpoint
   * @param body
   * @return
   */
  EndpointFruitore putEndpointFruitore(Long idFruitore, Long idEndpoint,
      AggiornaEndpointFruitoreRequest body);

  /**
   * @param idFruitore
   * @param idSchemaAutenticazione
   * @param body
   * @return
   */
  SchemaAutenticazioneFruitore putSchemaAuthFruitore(Long idFruitore, Long idSchemaAutenticazione,
      AggiornaSchemaAutenticazioneFruitoreRequest body);

  /**
   * @param body
   * @return
   */
  Fruitore postFruitoriAutentica(CredenzialiAutenticazioneFruitore body);

}
