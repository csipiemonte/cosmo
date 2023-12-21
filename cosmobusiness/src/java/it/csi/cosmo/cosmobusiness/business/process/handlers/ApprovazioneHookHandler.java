/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.process.handlers;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.business.process.model.AbstractFormLogicoHookHandler;
import it.csi.cosmo.cosmobusiness.business.process.model.BeforeCompleteHookContext;
import it.csi.cosmo.cosmobusiness.business.process.model.FunzionalitaFormLogicoHookContext;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;

/**
 *
 *
 */
@Component
public class ApprovazioneHookHandler extends AbstractFormLogicoHookHandler {


  @Autowired
  private PracticeService practiceService;

  @Override
  public void beforeComplete(BeforeCompleteHookContext context) {
    var parametri = parseParametri(context);

    String esitoApprovazione =
        (String) context.getVariabileRequest(parametri[0]).orElse(null);
    if (StringUtils.isBlank(esitoApprovazione)) {
      return;
    }

    boolean approvata = "true".equalsIgnoreCase(esitoApprovazione);

    String motivazione = ((String) context.getVariabileRequest(parametri[1]).orElse(null));

    if (StringUtils.isBlank(motivazione)) {
      motivazione = "Nessun commento.";
    }

    String commentText =
        "Motivazione " + (approvata ? "APPROVAZIONE" : "RIFIUTO") + ": " + motivazione;

    Commento commento = new Commento();
    commento.setCfAutore(context.getUtente().getCodiceFiscale());
    commento.setMessaggio(commentText);
    practiceService.postCommenti(context.getPratica().getProcessInstanceId(), commento);


    practiceService.creaNotificaDelCommento(
        context.getPratica().getId(),
        String.format(Constants.INSERIMENTO_COMMENTO, commento.getMessaggio()));

    String vistoGrafico = (String) context.getParametro("GESTIONE_VISTO_GRAFICO").orElse(null);

    if (approvata && vistoGrafico != null && vistoGrafico.equals("true")) {
      practiceService.creaVistoGrafico(context.getAttivita().getId());
    }

  }

  private String[] parseParametri(FunzionalitaFormLogicoHookContext context) {
    String[] output = new String[] {"ris-approvazione", "note"};

    context.getParametro("O_APPROVAZIONE")
    .filter(p -> p instanceof String && !StringUtils.isBlank((String) p))
    .ifPresent(param -> {
      // ha il formato { "variabileRisultatoApprovazione": "ris-approvazione",
      // "variabileNoteApprovazione": "note" }
      @SuppressWarnings("unchecked")
      Map<String, Object> parsed = ObjectUtils.fromJson((String) param, Map.class);
      output[0] = (String) parsed.getOrDefault("variabileRisultatoApprovazione", output[0]);
      output[1] = (String) parsed.getOrDefault("variabileNoteApprovazione", output[1]);
    });

    return output;
  }

  @Override
  public boolean failSafe() {
    return false;
  }
}
