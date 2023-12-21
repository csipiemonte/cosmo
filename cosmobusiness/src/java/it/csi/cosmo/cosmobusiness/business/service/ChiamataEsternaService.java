/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import org.springframework.http.ResponseEntity;
import it.csi.cosmo.cosmobusiness.dto.ChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;

public interface ChiamataEsternaService {

  <T> ResponseEntity<T> inviaChiamataEsterna(ChiamataEsternaRequest<T> request);

  String getDescrizioneErroreChiamataEsterna(Exception e);

  RiferimentoOperazioneAsincrona inviaChiamataEsternaDaProcesso(Long idPratica,
      InviaChiamataEsternaRequest body);

  Object testSchemaAutenticazione(Long idSchemaAutenticazione);

}
