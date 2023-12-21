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
public class ApprovazioneFirmaHookHandler extends AbstractFormLogicoHookHandler {


  @Autowired
  private PracticeService practiceService;

  @Override
  public void beforeComplete(BeforeCompleteHookContext context) {
    var parametri = parseParametri(context);

    String esitoApprovazioneFirma =
        (String) context.getVariabileRequest(parametri[0]).orElse(null);
    if (StringUtils.isBlank(esitoApprovazioneFirma)) {
      return;
    }

    boolean approvata = "true".equalsIgnoreCase(esitoApprovazioneFirma);

    String motivazione = ((String) context.getVariabileRequest(parametri[1]).orElse(null));

    if (!StringUtils.isBlank(motivazione)) {

      String commentText =
          "Motivazione " + (approvata ? "FIRMA DOCUMENTI" : "RIFIUTO IN FIRMA DOCUMENTI") + ": "
              + motivazione;

      Commento commento = new Commento();
      commento.setCfAutore(context.getUtente().getCodiceFiscale());
      commento.setMessaggio(commentText);


      practiceService.postCommenti(context.getPratica().getProcessInstanceId(), commento);

      practiceService.creaNotificaDelCommento(
          context.getPratica().getId(),
          String.format(Constants.INSERIMENTO_COMMENTO, commento.getMessaggio()));

    }
  }

  private String[] parseParametri(FunzionalitaFormLogicoHookContext context) {
    String[] output = new String[] {"ris-firma", "note"};

    context.getParametro("O_FIRMA")
    .filter(p -> p instanceof String && !StringUtils.isBlank((String) p))
    .ifPresent(param -> {
      // ha il formato { "variabileRisultatoFirma": "ris-approvazione",
      // "variabileNoteFirma": "note" }
      @SuppressWarnings("unchecked")
      Map<String, Object> parsed = ObjectUtils.fromJson((String) param, Map.class);
      output[0] = (String) parsed.getOrDefault("variabileRisultatoFirma", output[0]);
      output[1] = (String) parsed.getOrDefault("variabileNoteFirma", output[1]);
    });

    return output;
  }

  @Override
  public boolean failSafe() {
    return false;
  }
}
