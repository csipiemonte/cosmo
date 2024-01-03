/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;

/**
 *
 */

public interface CaricamentoPraticaService {

  CaricamentoPratica creaCaricamentoPratica(CaricamentoPraticaRequest request);

  CaricamentoPratica aggiornaCaricamentoPratica(Long id,
      CaricamentoPraticaRequest request);

  List<StatoCaricamentoPratica> getStatiCaricamento();

  CaricamentoPraticheResponse getCaricamentoPratiche(String filter);

  CaricamentoPraticheResponse getCaricamentoPraticheId(String id, String filter, Boolean export);

  CaricamentoPraticheResponse getCaricamentoPraticheCaricamentoInBozza(String filter);

  List<String> getPathElaborazioni(String dataInserimento);

  void deletePathFile(String path);

  void deleteCaricamentoPratiche(Long id);
}
