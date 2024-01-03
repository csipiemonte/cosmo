/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.Cartella;

/**
 *
 */

public interface CartellaService {


  /**
   * Metodo per creare la cartella di una pratica in index
   *
   * @param cartella, oggetto contenente il path della pratica di cui creare la cartella
   * @return un oggetto contenente il path e l'identificativo della cartella su index
   */
  Cartella creaCartellaIndex(Cartella cartella);

  /**
   * Metodo per cancellare la cartella di una pratica in index
   *
   * @param cartella, oggetto contenenente l'identificativo della cartella da cancellare in Index
   */
  void cancellaCartellaIndex(Cartella cartella);
}
