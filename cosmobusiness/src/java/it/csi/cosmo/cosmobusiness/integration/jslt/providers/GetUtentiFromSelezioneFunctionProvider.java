/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaGruppiDTO;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoAuthorizationGruppiFeignClient;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public class GetUtentiFromSelezioneFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {

    if (arguments == null || arguments.length == 0) {
      throw new BadRequestException("Utenti o Gruppi non indicati");
    }

    if (arguments[0] == null || arguments[0].isNull() || !arguments[0].isLong()) {
      throw new BadRequestException("Ente non indicato");
    }

    LongFilter idEnteFilter = new LongFilter();
    idEnteFilter.setEquals(arguments[0].longValue());

    List<String> gruppi = new LinkedList<>();
    List<String> utenti = new LinkedList<>();

    for (int i = 1; i < arguments.length; i++) {
      if (arguments[i] != null && !arguments[i].isNull() && arguments[i].isArray()) {
        var elements = arguments[i].elements();

        while (elements.hasNext()) {
          JsonNode element = elements.next();
          if (element != null && !element.isNull() && element.isObject()) {
            if (element.has("userId") && !utenti.contains(element.get("userId").textValue())) {
              utenti.add(element.get("userId").textValue());
            } else if (element.has("groupId")
                && !gruppi.contains(element.get("groupId").textValue())) {
              gruppi.add(element.get("groupId").textValue());
            }
          }
        }
      }
    }

    if (!gruppi.isEmpty()) {
      GenericRicercaParametricaDTO<FiltroRicercaGruppiDTO> gruppiRequest =
          new GenericRicercaParametricaDTO<>();

      FiltroRicercaGruppiDTO gruppiFilter = new FiltroRicercaGruppiDTO();
      gruppiRequest.setFilter(gruppiFilter);

      StringFilter codiceFilter = new StringFilter();
      codiceFilter.setIn(gruppi.toArray(new String[0]));
      gruppiFilter.setCodice(codiceFilter);
      gruppiFilter.setIdEnte(idEnteFilter);

      CosmoAuthorizationGruppiFeignClient gruppiFeignClient =
          SpringApplicationContextHelper.getBean(CosmoAuthorizationGruppiFeignClient.class);

      var gruppiResponse = gruppiFeignClient.getGruppi(ObjectUtils.toJson(gruppiRequest));

      gruppiResponse.getGruppi().forEach(gruppo -> gruppo.getUtenti().forEach(utente -> {
        if (!utenti.contains(utente.getCodiceFiscale())) {
          utenti.add(utente.getCodiceFiscale());
        }
      }));
    }

    String utentiString = String.join(",", utenti);
    return ObjectUtils.getDataMapper().convertValue(utentiString, JsonNode.class);
  }

  @Override
  public int getMaxArguments() {
    return 10;
  }

  @Override
  public int getMinArguments() {
    return 1;
  }

  @Override
  public String getName() {
    return "getUtentiFromSelezione";
  }


}
