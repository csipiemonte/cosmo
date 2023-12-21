/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import java.util.Collection;
import java.util.List;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;

public interface DocumentiService {

  /**
   * @param idPratica
   * @return
   */
  List<Documento> getDocumentiPratica(Long idPratica);

  /**
   * @param idPratica
   * @param tipi
   * @return
   */
  List<Documento> getDocumentiPratica(Long idPratica, Collection<String> tipi);

  /**
   * @param idPratica
   */
  void verificaStatoAcquisizioneDocumentiIndex(Long idPratica);

  /**
   * @param idPratica
   * @param tipi
   */
  void verificaStatoAcquisizioneDocumentiIndex(Long idPratica, Collection<String> tipi);
}
