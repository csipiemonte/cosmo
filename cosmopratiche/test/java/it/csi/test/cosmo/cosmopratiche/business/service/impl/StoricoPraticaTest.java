/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentUnitTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class StoricoPraticaTest extends ParentUnitTest {

  @Autowired
  CosmoTPraticaRepository cosmoTPraticaRepository;

  @Test
  public void getStoricoAttivita() {
    Long idPratica = 1L;

    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    Map<String, Object> output = new HashMap<>();
    List<Map<String, Object>> events = new LinkedList<>();
    
    output.put("idPratica", pratica.getId());
    output.put("oggettoPratica", pratica.getOggetto());
    output.put("events", events);
    
    events.add(event(
        "TIMESTAMP", pratica.getDataCreazionePratica(), 
        "TYPE", "CREAZIONE PRATICA",
        "NATURAL_ORDER", 1000,
        "NAME", pratica.getOggetto(),
        "USER", pratica.getUtenteCreazionePratica(),
        "CLIENT", pratica.getFruitore() != null ? pratica.getFruitore().getNomeApp() : null
    ));
    
    for (CosmoTAttivita attivita : pratica.getAttivita()) {

      events.add(event("TIMESTAMP", attivita.getDtInserimento(), "TYPE", "CREAZIONE ATTIVITA",
          "NATURAL_ORDER", 2000, "NAME", attivita.getNome(), "DETAILS", attivita.getDescrizione()));

      var assegnazioni = attivita.getCosmoRAttivitaAssegnaziones().stream()
          .sorted((a1, a2) -> {
            int r = a1.getDtInizioVal().compareTo(a2.getDtInizioVal());
            if (r == 0) {
              r = a1.getId().compareTo(a2.getId());
            }
            return r;
          })
          .collect(Collectors.toCollection(LinkedList::new));

      CosmoRAttivitaAssegnazione precedente = null;
      for (CosmoRAttivitaAssegnazione assegnazione : assegnazioni) {
        if (Boolean.TRUE.equals(assegnazione.getAssegnatario())) {
          if (precedente != null && !Boolean.TRUE.equals(precedente.getAssegnatario())) {
            // presa in carico
            events.add(event(
                "TIMESTAMP", assegnazione.getDtInizioVal(), 
                "TYPE", "PRESA IN CARICO",
                "NATURAL_ORDER", 4000,
                "UTENTE", assegnazione.getIdUtente()
            ));
            
          } else {
            // assegnata ad utente direttamente
            events.add(event(
                "TIMESTAMP", assegnazione.getDtInizioVal(), 
                "TYPE", "ASSEGNATA",
                "NATURAL_ORDER", 4000,
                "UTENTE", assegnazione.getIdUtente()
            ));
          }
        } else if (assegnazione.getIdGruppo() != null) {
          // assegnata ad un gruppo
          events.add(event(
              "TIMESTAMP", assegnazione.getDtInizioVal(), 
              "TYPE", "DELEGATA A GRUPPO",
              "NATURAL_ORDER", 3000,
              "GRUPPO", assegnazione.getIdGruppo()
          ));
        }
        precedente = assegnazione;
      }

      if (attivita.getDtCancellazione() != null) {
        events.add(event("TIMESTAMP", attivita.getDtCancellazione(), "TYPE", "TERMINE ATTIVITA",
            "NATURAL_ORDER", 5000, "NAME", attivita.getNome(), "DETAILS",
            attivita.getDescrizione()));
      }
    }

    if (pratica.getDataFinePratica() != null) {
      events.add(event("TIMESTAMP", pratica.getDataFinePratica(), "NATURAL_ORDER", 9100, "TYPE",
          "CHIUSURA PRATICA"));
    }
    
    if (pratica.getDtCancellazione() != null) {
      events.add(event(
          "TIMESTAMP", pratica.getDtCancellazione(),
          "NATURAL_ORDER", 9200, "TYPE", "ANNULLAMENTO PRATICA"
      ));  
    }
    
    events.sort((e1, e2) -> {
      var v1 = (Timestamp) e1.get("TIMESTAMP");
      var v2 = (Timestamp) e2.get("TIMESTAMP");
      var result = v1 != null && v2 != null ? v1.compareTo(v2) : 0;
      if (result == 0) {
        result = ((int) e1.get("NATURAL_ORDER")) - ((int) e2.get("NATURAL_ORDER"));
      }
      return result;
    });

    dump("OUTPUT", output);
  }

  private Map<String, Object> event(Object... data) {
    Map<String, Object> output = new HashMap<>();
    for (int i = 0; i < data.length; i += 2) {
      output.put(data[i].toString(), data[i + 1]);
    }
    return output;
  }

}
