/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.CategorieUseCaseResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCaseResponse;

/**
 *
 */

public interface UseCaseService {

  /**
   * Restituisce una lista di , il cui numero di elementi e' pari al valore indicato da filter o, se
   * filter non e' valorizzato, un numero di elementi pari al valore definito nella tabella di
   * configurazione
   *
   * @param filter indica i filtri per la ricerca dei usecase
   * @return una lista di usecase
   */
  UseCaseResponse getUseCases(String filter);

  /**
   * Restituisce il usecase il cui id e' quello inserito nel database
   *
   * @param id del usecase da cercare sul database
   * @return il usecase del database
   */
  CategorieUseCaseResponse getUseCase(String id);

}
