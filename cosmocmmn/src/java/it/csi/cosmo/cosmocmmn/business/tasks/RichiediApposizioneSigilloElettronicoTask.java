/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;


public class RichiediApposizioneSigilloElettronicoTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression identificativoAlias;
  private Expression tipiDocumento;
  private Expression identificativoMessaggio;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    Long idPratica = Long.valueOf(businessKey);

    String exIdentificativoAlias = exIdentificativoAlias(execution);
    List<CodiceTipologiaDocumento> exTipiDocumento = getTipiDocumento(execution);
    String exIdentificativoMessaggio = getIdentificativoMessaggio(execution);

    RichiediApposizioneSigilloRequest request = new RichiediApposizioneSigilloRequest();
    request.setIdentificativoAlias(exIdentificativoAlias);
    request.setTipiDocumento(exTipiDocumento);
    request.setIdentificativoMessaggio(exIdentificativoMessaggio);

    CosmoEcmDocumentiFeignClient feignClient = this.getBean(CosmoEcmDocumentiFeignClient.class);

    try {
      logger.info(method, "invio richiesta apposizione sigillo elettronico per idPratica {}",
          idPratica);
      feignClient.postDocumentiIdPraticaRichiediApposizioneSigillo(businessKey, request);
      logger.info(method,
          "invio richiesta apposizione sigillo elettronico completato per idPratica {}", idPratica);
    } catch (Exception e) {
      logger.error(method,
          "invio richiesta apposizione sigillo elettronico  fallito per idPratica " + idPratica, e);
      throw e;
    }
  }

  public String getIdentificativoMessaggio(DelegateExecution execution) {
    return this.requireClassField(execution, identificativoMessaggio, "identificativoMessaggio");
  }

  public String exIdentificativoAlias(DelegateExecution execution) {
    return this.requireClassField(execution, identificativoAlias, "IdentificativoAlias");
  }

  private List<CodiceTipologiaDocumento> getTipiDocumento(DelegateExecution execution) {
    var raw = this.requireClassField(execution, tipiDocumento, "tipiDocumento");
    List<CodiceTipologiaDocumento> output = new ArrayList<>();


    Arrays.stream(raw.split(",")).map(String::strip).filter(StringUtils::isNotBlank).forEach(codiceString -> {

      var split = codiceString.split("\\.");
      if (StringUtils.isNotBlank(split[0].strip())) {
        CodiceTipologiaDocumento singolo = new CodiceTipologiaDocumento();

        if (split.length > 1 && StringUtils.isNotBlank(split[1].strip())) {
          singolo.setCodicePadre(split[0].strip());
          singolo.setCodice(split[1].strip());
        } else {
          singolo.setCodice(split[0].strip());
        }

        output.add(singolo);
      }

    });
    return output;
  }


}
