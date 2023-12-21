/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmReportFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;


public class GenerazioneReportTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression codiceTemplate;
  private Expression mappaturaParametri;
  private Expression codiceTipoDocumento;
  private Expression nomeFile;
  private Expression titolo;
  private Expression autore;
  private Expression sovrascrivi;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "eseguo generazione report per businessKey {}", businessKey);

    Long idPratica = Long.valueOf(businessKey);

    RichiediGenerazioneReportRequest request = new RichiediGenerazioneReportRequest();
    request.setCodiceTemplate(getCodiceTemplate(execution));
    request.setIdPratica(idPratica);
    request.setMappaturaParametri(getMappaturaParametri(execution));
    request.setAutore(getAutore(execution));
    request.setNomeFile(getNomeFile(execution));
    request.setTitolo(getTitolo(execution));
    request.setSovrascrivi(getSovrascrivi(execution));
    request.setCodiceTipoDocumento(getCodiceTipoDocumento(execution));

    var client = getBean(CosmoEcmReportFeignClient.class);

    logger.info(method, "richiedo generazione report {}", request);

    var response = client.postReport(request);

    logger.info(method, "generato report {}", response);
  }

  public String getCodiceTemplate(DelegateExecution execution) {
    return getClassField(execution, codiceTemplate);
  }

  public String getMappaturaParametri(DelegateExecution execution) {
    return getClassField(execution, mappaturaParametri);
  }

  public String getCodiceTipoDocumento(DelegateExecution execution) {
    return getClassField(execution, codiceTipoDocumento);
  }

  public String getNomeFile(DelegateExecution execution) {
    return getClassField(execution, nomeFile);
  }

  public String getTitolo(DelegateExecution execution) {
    return getClassField(execution, titolo);
  }

  public String getAutore(DelegateExecution execution) {
    return getClassField(execution, autore);
  }

  public Boolean getSovrascrivi(DelegateExecution execution) {
    return getClassFieldAsBoolean(execution, sovrascrivi);
  }

  public void setCodiceTipoDocumento(Expression codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  public void setCodiceTemplate(Expression codiceTemplate) {
    this.codiceTemplate = codiceTemplate;
  }

  public void setMappaturaParametri(Expression mappaturaParametri) {
    this.mappaturaParametri = mappaturaParametri;
  }

  public void setNomeFile(Expression nomeFile) {
    this.nomeFile = nomeFile;
  }

  public void setTitolo(Expression titolo) {
    this.titolo = titolo;
  }

  public void setAutore(Expression autore) {
    this.autore = autore;
  }

  public void setSovrascrivi(Expression sovrascrivi) {
    this.sovrascrivi = sovrascrivi;
  }

}
