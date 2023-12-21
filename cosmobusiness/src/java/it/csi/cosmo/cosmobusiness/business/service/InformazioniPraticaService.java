/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniPratica;

public interface InformazioniPraticaService {

  InformazioniPratica getInfoPratica(String idPratica, String idDocumento);

  /**
   * @param id
   * @param body
   * @return
   */
  Object elaboraInformazioniPratica(Long id, GetElaborazionePraticaRequest body);

  /**
   * @param id
   * @return
   */
  Object getContestoInformazioniPratica(Long id);

}
