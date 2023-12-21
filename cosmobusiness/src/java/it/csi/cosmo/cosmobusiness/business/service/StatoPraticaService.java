/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import java.util.List;
import java.util.Map;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;

public interface StatoPraticaService {

  AggiornaStatoPraticaRequest getPratica(CosmoTPratica pratica);

  List<AttivitaFruitore> getAttivita(CosmoTPratica pratica);

  List<DocumentoFruitore> getDocumenti(CosmoTPratica pratica);

  List<MessaggioFruitore> getMessaggi(CosmoTPratica pratica);

  Map<String, Object> getVariabiliProcesso(CosmoTPratica pratica);

  Map<String, Object> getVariabiliProcessoByProcessInstanceId(String processInstanceId);
}
