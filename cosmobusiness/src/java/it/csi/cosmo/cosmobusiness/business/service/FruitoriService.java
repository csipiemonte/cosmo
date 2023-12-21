/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import java.util.Optional;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreResponse;

public interface FruitoriService {

  AvviaProcessoFruitoreResponse avviaProcesso(AvviaProcessoFruitoreRequest body);

  CreaEventoFruitoreResponse creaEvento(CreaEventoFruitoreRequest body);

  AggiornaEventoFruitoreResponse aggiornaEvento(String idEvento,
      AggiornaEventoFruitoreRequest body);

  void eliminaEvento(String idEvento, EliminaEventoFruitoreRequest request);

  GetPraticaFruitoreResponse getPratica(String idPratica);

  InviaSegnaleFruitoreResponse inviaSegnaleProcesso(String idPratica,
      InviaSegnaleFruitoreRequest body);

  Optional<CosmoTEndpointFruitore> verificaEsistenzaEndpoint(OperazioneFruitore operazione,
      Long idFruitore, String codiceDescrittivo);

  CosmoTEndpointFruitore getEndpoint(OperazioneFruitore operazione, Long idFruitore,
      String codiceDescrittivo);

  /**
   * @param body
   * @return
   */
  CreaPraticaEsternaFruitoreResponse postPraticheEsterne(CreaPraticaEsternaFruitoreRequest body);

  /**
   * @param idPraticaExt
   * @param body
   * @return
   */
  AggiornaPraticaEsternaFruitoreResponse putPraticheEsterne(String idPraticaExt,
      AggiornaPraticaEsternaFruitoreRequest body);

  /**
   * @param idPraticaExt
   * @param body
   * @return
   */
  EliminaPraticaEsternaFruitoreResponse deletePraticheEsterne(String idPraticaExt,
      EliminaPraticaEsternaFruitoreRequest body);

}
