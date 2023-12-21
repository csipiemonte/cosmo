/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmocmmn.business.service.DocumentiService;
import it.csi.cosmo.cosmocmmn.business.service.impl.DocumentiServiceImpl;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessCallbackFeignClient;


public class InviaStatoPraticaTask extends ParentFlowableTask {

  private static final boolean DEFAULT_SINCRONO = false;

  /**
   * parametri forniti in input da process design
   */
  private Expression sincrono;
  private Expression codiceSegnale;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "invio stato pratica per businessKey {}", businessKey);
    Long idPratica = Long.valueOf(businessKey);

    logger.info(method,
        "verifico stato acquisizione documenti per businessKey {} prima di inviare callback stato pratica",
        businessKey);
    DocumentiService service = getBean(DocumentiServiceImpl.class);
    logger.info(method, "service acquisito");
    try {
      service.verificaStatoAcquisizioneDocumentiIndex(idPratica);
    } catch (Exception e) {
      logger.error(method, e.getMessage());
      throw new BpmnError("DocumentiNonPronti", e.getMessage());
    }

    boolean isSincrono = getSincrono(execution);

    if (isSincrono) {
      inviaSincrono(execution, idPratica);
    } else {
      schedulaInvioAsincrono(execution, idPratica);
    }

    execution.setVariable(VARIABLE_DATA_ULTIMO_INVIO_STATO,
        OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
  }

  protected void inviaSincrono(DelegateExecution execution, Long idPratica) {
    final var method = "inviaSincrono";

    String codiceSegnale = getCodiceSegnale(execution);

    InviaCallbackStatoPraticaRequest request = new InviaCallbackStatoPraticaRequest();
    request.setIdPratica(idPratica);
    request.setCodiceSegnale(codiceSegnale);

    CosmoBusinessCallbackFeignClient feignClient =
        this.getBean(CosmoBusinessCallbackFeignClient.class);

    try {
      logger.info(method, "invio stato pratica per idPratica {}", idPratica);
      feignClient.postCallbackStatoPraticaInvia(request);
      logger.info(method, "invio stato pratica completato per idPratica {}", idPratica);
    } catch (Exception e) {
      logger.error(method, "invio stato pratica fallito per idPratica " + idPratica, e);
      throw e;
    }
  }

  protected void schedulaInvioAsincrono(DelegateExecution execution, Long idPratica) {
    final var method = "schedulaInvioAsincrono";

    String codiceSegnale = getCodiceSegnale(execution);

    SchedulaCallbackStatoPraticaRequest request = new SchedulaCallbackStatoPraticaRequest();
    request.setIdPratica(idPratica);
    request.setCodiceSegnale(codiceSegnale);

    CosmoBusinessCallbackFeignClient feignClient =
        this.getBean(CosmoBusinessCallbackFeignClient.class);

    try {
      logger.info(method, "schedulo invio stato pratica per idPratica {}", idPratica);
      feignClient.postCallbackStatoPraticaSchedula(request);
      logger.info(method, "schedulazione invio stato pratica completato per idPratica {}",
          idPratica);
    } catch (Exception e) {
      logger.error(method, "schedulazione invio stato pratica fallito per idPratica " + idPratica,
          e);
      throw e;
    }
  }

  public boolean getSincrono(DelegateExecution execution) {
    if (sincrono == null) {
      return DEFAULT_SINCRONO;
    }
    String raw = (String) sincrono.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return DEFAULT_SINCRONO;
    }
    return Boolean.valueOf(raw.strip());
  }

  private String getCodiceSegnale(DelegateExecution execution) {
    if (codiceSegnale == null) {
      return null;
    }
    String raw = (String) codiceSegnale.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  public void setSincrono(Expression sincrono) {
    this.sincrono = sincrono;
  }
}
