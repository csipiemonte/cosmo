/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmocmmn.business.service.DocumentiService;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiDTO;

@Service
@Transactional
public class DocumentiServiceImpl implements DocumentiService {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "DocumentiServiceImpl");

  @SuppressWarnings("unchecked")
  protected <T> T getBean(Class<T> beanClass) {
    return (T) SpringApplicationContextHelper.getBean(beanClass);
  }

  @Override
  public void verificaStatoAcquisizioneDocumentiIndex(Long idPratica) {
    verificaStatoAcquisizioneDocumentiIndex(idPratica, null);
  }

  @Override
  public void verificaStatoAcquisizioneDocumentiIndex(Long idPratica, Collection<String> tipi) {
    final var method = "verificaStatoAcquisizioneDocumentiIndex";

    if (tipi != null && !tipi.isEmpty() && !(tipi instanceof Set)) {
      // prepara per lookup O(1) nel ciclo seguente
      tipi = tipi.stream().collect(Collectors.toSet());
    }

    // fetch dei documenti pagina per pagina
    logger.info(method, "preparo la chiamata a cosmoecm");
    List<Documento> documenti = getDocumentiPratica(idPratica, tipi);

    // verifico lo stato di tutti i documenti
    for (Documento doc : documenti) {
      // se specificati considera solo i documenti dei tipi desiderati
      if (tipi != null && !tipi.isEmpty() && doc.getTipo() != null
          && !tipi.contains(doc.getTipo().getCodice())) {
        continue;
      }

      if (doc.getStato() == null
          || (!StatoDocumento.ELABORATO.name().equals(doc.getStato().getCodice())
              && !StatoDocumento.ERR_ANALISI.name().equals(doc.getStato().getCodice())
              && !StatoDocumento.ERR_SBUSTAMENTO.name().equals(doc.getStato().getCodice())
              && !StatoDocumento.ERR_VERIFICA.name().equals(doc.getStato().getCodice()))) {

        var msg =
            "il documento #" + doc.getId() + " non e' stato elaborato correttamente (in stato "
                + (doc.getStato() != null ? doc.getStato().getCodice() : "null") + ")";

        logger.error(method, msg);
        throw new ConflictException(msg);
      }
    }
  }

  @Override
  public List<Documento> getDocumentiPratica(Long idPratica) {
    return getDocumentiPratica(idPratica, null);
  }

  @Override
  public List<Documento> getDocumentiPratica(Long idPratica, Collection<String> tipi) {

    var client = getBean(CosmoEcmDocumentiFeignClient.class);

    var request = new GenericRicercaParametricaDTO<FiltroRicercaDocumentiDTO>();
    request.setPage(0);
    request.setSize(20);
    request.setSort("id ASC");
    var filter = new FiltroRicercaDocumentiDTO();
    request.setFilter(filter);

    Map<String, String> idPraticaFilter = new HashMap<>();
    idPraticaFilter.put("eq", idPratica.toString());
    filter.setIdPratica(idPraticaFilter);

    // se specificati i tipi documento filtro solo per quelli che mi interessano
    if (tipi != null && !tipi.isEmpty()) {
      Map<String, Object> tipiFilter = new HashMap<>();
      tipiFilter.put("in", tipi);
      filter.setTipo(tipiFilter);
    }

    // fetch dei documenti pagina per pagina
    List<Documento> documenti = new ArrayList<>();

    while (true) {
      // chiamata in GET per la pagina corrente
      logger.info("getDocumentiPratica", "chiamata feign");
      var documentiPage = client.getDocumento(ObjectUtils.toJson(request), null);

      // interrompiamo il fetch se siamo arrivati alla fine (pagina vuota)
      if (documentiPage.getDocumenti() == null || documentiPage.getDocumenti().isEmpty()) {
        break;
      }

      documenti.addAll(documentiPage.getDocumenti());

      // richiedi la pagina successiva
      request.setPage(request.getPage() + 1);
    }

    return documenti;
  }

}
