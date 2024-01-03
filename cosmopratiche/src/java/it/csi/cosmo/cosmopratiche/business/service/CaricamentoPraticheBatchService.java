/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;

/**
 *
 */

public interface CaricamentoPraticheBatchService {

  List<CosmoTCaricamentoPratica> getCaricamentoPraticheWithCaricamentoCompletato();

  void elaboraCosmoTCaricamentoPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica);

  void registraErroreGenerico(CosmoTCaricamentoPratica cosmoTCaricamentoPratica, String error);

  void iniziaElaborazione(CosmoTCaricamentoPratica temp);

  void completaElaborazione(CosmoTCaricamentoPratica temp);


}
