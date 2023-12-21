/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.mail.model.CosmoMailAttachment;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaUtentiDTO;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.business.service.MailService;
import it.csi.cosmo.cosmocmmn.business.service.impl.MailServiceImpl;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoAuthorizationUtentiFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneTipologiaDocumentiRequest;


public class InviaMailTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression to;
  private Expression cc;
  private Expression bcc;
  private Expression from;
  private Expression fromName;
  private Expression text;
  private Expression subject;
  private Expression jsonMapping;
  private Expression attachments;
  private Expression aggiungiSha;
  private Expression ultimoDoc;

  // usato per deserializzare configurazione
  public static class OpzioneAllegato {
    protected String codiceTipoDocumento;
    protected Boolean obbligatorio;
    protected Boolean link;
    protected Long durata;

    public Long getDurata() {
      return durata;
    }

    public void setDurata(Long durata) {
      this.durata = durata;
    }

    public String getCodiceTipoDocumento() {
      return codiceTipoDocumento;
    }

    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
      this.codiceTipoDocumento = codiceTipoDocumento;
    }

    public Boolean getObbligatorio() {
      return obbligatorio;
    }

    public void setObbligatorio(Boolean obbligatorio) {
      this.obbligatorio = obbligatorio;
    }

    public Boolean getLink() {
      return link;
    }

    public void setLink(Boolean link) {
      this.link = link;
    }
  }

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    Long idPratica = Long.valueOf(execution.getProcessInstanceBusinessKey());

    MailService mailService = this.getBean(MailServiceImpl.class);

    List<String> exTo = this.getTo(execution);
    List<String> exCc = this.getCc(execution);
    List<String> exBcc = this.getBcc(execution);
    String exSubject = this.requireClassField(execution, this.subject, "subject");
    String exText = this.requireClassField(execution, this.text, "text");
    String exFrom = this.getClassField(execution, this.from);
    String exFromName = this.getClassField(execution, this.fromName);
    Boolean exUltimoDoc = this.getClassFieldAsBoolean(execution, this.ultimoDoc);
    List<OpzioneAllegato> exDocuments = this.getDocuments(execution);
    Boolean exAggiungiSha = this.getClassFieldAsBoolean(execution, aggiungiSha);

    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {
      // call mapping
      Map<String, Object> mappingResult = richiediElaborazioneMappatura(idPratica, exJsonMapping);

      // replace mapping in fields
      exTo = toEmailAddressList(execution, compila(exTo, mappingResult));
      exCc = toEmailAddressList(execution, compila(exCc, mappingResult));
      exBcc = toEmailAddressList(execution, compila(exBcc, mappingResult));
      exSubject = compila(exSubject, mappingResult);
      exText = compila(exText, mappingResult);
      exFrom = compila(exFrom, mappingResult);
      exFromName = compila(exFromName, mappingResult);
    }

    List<CosmoMailAttachment> exAttachments = new ArrayList<>();

    if (exDocuments != null && !exDocuments.isEmpty()) {
      // creo una mappa per evitare scansioni multiple
      Map<String, OpzioneAllegato> docMap = new HashMap<>();
      Map<String, Boolean> flagInseriti = new HashMap<>();


      // get dei documenti
      CosmoEcmDocumentiFeignClient documentiClient =
          (CosmoEcmDocumentiFeignClient) SpringApplicationContextHelper
          .getBean(CosmoEcmDocumentiFeignClient.class);

      var esposizioneDocumentiRequest = new PreparaEsposizioneDocumentiRequest();
      esposizioneDocumentiRequest.setTipologieDaEsporre(new ArrayList<>());
      esposizioneDocumentiRequest.setUltimoDocumento(exUltimoDoc);
      for (OpzioneAllegato exDocument : exDocuments) {
        docMap.put(exDocument.codiceTipoDocumento, exDocument);

        var e = new PreparaEsposizioneTipologiaDocumentiRequest();
        e.setCodiceTipoDocumento(exDocument.codiceTipoDocumento);
        e.setDurata(exDocument.durata);
        esposizioneDocumentiRequest.getTipologieDaEsporre().add(e);
      }

      var risultatoEsposizione =
          documentiClient.postDocumentiIdPraticaEsposizione(idPratica, esposizioneDocumentiRequest);

      for (PreparaEsposizioneDocumentoResponse risultatoEsposizioneDocumento : risultatoEsposizione
          .getDocumentiEsposti()) {

        if (risultatoEsposizioneDocumento.getEsito().getCode().equals("DocumentoNonPronto")) {
          throw new BpmnError("DocumentiNonPronti",
              "Uno o piu' documenti non sono ancora pronti per l'esposizione (documento #"
                  + risultatoEsposizioneDocumento.getIdDocumento() + ")");
        }

        if (risultatoEsposizioneDocumento.getEsito().getStatus() >= 300) {
          throw new InternalServerException(
              "Errore nella preparazione dell'esposizione per il documento "
                  + risultatoEsposizioneDocumento.getIdDocumento() + ": "
                  + risultatoEsposizioneDocumento.getEsito().getStatus() + " "
                  + risultatoEsposizioneDocumento.getEsito().getCode() + " - "
                  + risultatoEsposizioneDocumento.getEsito().getTitle());
        } else {
          var opzioni = docMap.get(risultatoEsposizioneDocumento.getCodiceTipoDocumento());
          flagInseriti.put(risultatoEsposizioneDocumento.getCodiceTipoDocumento(), Boolean.TRUE);

          if(Boolean.TRUE.equals(exAggiungiSha)) {
            StringBuilder bld = new StringBuilder(exText);
            bld.append("\n");
            if(risultatoEsposizioneDocumento.getShaFile() != null) {
              bld.append("\n" + " Documento: " + risultatoEsposizioneDocumento.getNomeFile() + " || SHA: " + risultatoEsposizioneDocumento.getShaFile() + "\n");
            }else {
              bld.append("\n" + " Documento: " + risultatoEsposizioneDocumento.getNomeFile() + " || SHA: Not Available" + "\n");
            }
            exText = bld.toString();
          }

          //@formatter:off
          exAttachments.add(CosmoMailAttachment.builder()
              .withFileName(risultatoEsposizioneDocumento.getNomeFile())
              .withUrl(risultatoEsposizioneDocumento.getUrl())
              .withAsLink(Boolean.TRUE.equals(opzioni.link))
              .build());
          //@formatter:on
        }

      }

      for (OpzioneAllegato opzione : docMap.values()) {
        if (Boolean.TRUE.equals(opzione.obbligatorio)
            && !flagInseriti.containsKey(opzione.codiceTipoDocumento)) {
          throw new BadRequestException("Nessun documento disponibile per la tipologia '"
              + opzione.codiceTipoDocumento + "'");
        }
      }
    }

    try {
      logger.info(method, "invio email a {} con subject: {}", exTo, exSubject);

      mailService.inviaMail(exTo, exFrom, exFromName, exCc, exBcc, exSubject, exText,
          exAttachments);
      logger.info(method, "invio email completato");
    } catch (Exception e) {
      logger.error(method, "invio email fallito", e);
      throw e;
    }
  }

  private List<String> compila(List<String> raw, Map<String, Object> risultatoElaborazione) {
    if (raw == null || raw.isEmpty()) {
      return raw;
    }
    return raw.stream().map(v -> compila(v, risultatoElaborazione))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @SuppressWarnings("deprecation")
  private String compila(String raw, Map<String, Object> risultatoElaborazione) {
    if (risultatoElaborazione == null) {
      risultatoElaborazione = new HashMap<>();
    }

    if (raw == null || !raw.contains("${")) {
      return raw;
    }

    StrSubstitutor sub = new StrSubstitutor(risultatoElaborazione);
    return sub.replace(raw);
  }

  private Map<String, Object> richiediElaborazioneMappatura(Long idPratica, String mappatura) {

    GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
    requestElaborazione.setMappatura(mappatura);

    logger.debug("richiediElaborazioneMappatura",
        "richiedo elaborazione mappa parametri per invio mail relativa alla pratica: {}",
        mappatura);

    CosmoBusinessPraticheFeignClient client = this.getBean(CosmoBusinessPraticheFeignClient.class);

    @SuppressWarnings("unchecked")
    Map<String, Object> datiElaborati =
    (Map<String, Object>) client.postPraticheIdElaborazione(idPratica, requestElaborazione);

    return datiElaborati;
  }

  private String resolveEmail(String raw, String tenantId) {
    if (raw.contains("@")) {
      return raw;
    }

    if (raw.startsWith("${")) {
      return raw;
    }

    final var method = "resolveEmail";

    String resolved = resolveEmailForUtente(raw, tenantId);
    if (resolved != null) {
      logger.info(method, "resolved email {} -> {}", raw, resolved);
      return resolved;
    }

    throw new BadRequestException("Bad email format for " + raw);
  }

  private String resolveEmailForUtente(String raw, String tenantId) {

    CosmoAuthorizationUtentiFeignClient utentiFeignClient =
        this.getBean(CosmoAuthorizationUtentiFeignClient.class);

    GenericRicercaParametricaDTO<FiltroRicercaUtentiDTO> getUtentiRequest =
        new GenericRicercaParametricaDTO<>();
    getUtentiRequest.setSize(2);
    FiltroRicercaUtentiDTO getUtentiFilter = new FiltroRicercaUtentiDTO();

    getUtentiRequest.setFilter(getUtentiFilter);
    StringFilter codiceFiscaleFilter = new StringFilter();
    codiceFiscaleFilter.setEquals(raw.strip());
    getUtentiFilter.setCodiceFiscale(codiceFiscaleFilter);
    StringFilter codiceIpaEnteFilter = new StringFilter();
    codiceIpaEnteFilter.setEqualsIgnoreCase(tenantId);
    getUtentiFilter.setCodiceIpaEnte(codiceIpaEnteFilter);

    var utentiResponse = utentiFeignClient.getUtenti(ObjectUtils.toJson(getUtentiRequest));

    if (utentiResponse.getPageInfo().getTotalElements().equals(1)) {
      var utente = utentiFeignClient.getUtentiCodiceFiscale(raw).getUtente();
      var associazione = utente.getEnti().stream()
          .filter(e -> e.getEnte() != null && e.getEnte().getCodiceIpa().equalsIgnoreCase(tenantId))
          .findFirst().orElse(null);
      if (associazione == null) {
        return null;
      }
      return StringUtils.isBlank(associazione.getEmail()) ? null : associazione.getEmail();

    } else {
      return null;
    }
  }

  private String getJsonMapping(DelegateExecution execution) {
    return getClassField(execution, jsonMapping);
  }

  private String getTenantId(DelegateExecution execution) {
    return execution.getTenantId();
  }

  private List<String> toEmailAddressList(DelegateExecution execution, String raw) {
    if (StringUtils.isBlank(raw)) {
      return Collections.emptyList();
    }
    return Arrays.stream(raw.split(",")).map(String::strip).filter(StringUtils::isNotBlank)
        .map(e -> resolveEmail(e, getTenantId(execution))).filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private List<String> toEmailAddressList(DelegateExecution execution, List<String> raw) {

    return raw.stream().flatMap(e -> Arrays.stream(e.split(","))).map(String::strip)
        .filter(StringUtils::isNotBlank).map(e -> resolveEmail(e, getTenantId(execution)))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  private List<String> getTo(DelegateExecution execution) {
    return toEmailAddressList(execution, getClassField(execution, to));
  }

  private List<String> getCc(DelegateExecution execution) {
    return toEmailAddressList(execution, getClassField(execution, cc));
  }

  private List<String> getBcc(DelegateExecution execution) {
    return toEmailAddressList(execution, getClassField(execution, bcc));
  }

  private List<OpzioneAllegato> getDocuments(DelegateExecution execution) {
    String raw = getClassField(execution, attachments);
    if (StringUtils.isBlank(raw)) {
      return Collections.emptyList();
    }

    JavaType type = ObjectUtils.getDataMapper().getTypeFactory()
        .constructParametricType(ArrayList.class, OpzioneAllegato.class);

    try {
      return ObjectUtils.getDataMapper().readValue(raw, type);
    } catch (JsonProcessingException e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
  }

  public void setTo(Expression to) {
    this.to = to;
  }

  public void setCc(Expression cc) {
    this.cc = cc;
  }

  public void setBcc(Expression bcc) {
    this.bcc = bcc;
  }

  public void setFrom(Expression from) {
    this.from = from;
  }

  public void setFromName(Expression fromName) {
    this.fromName = fromName;
  }

  public void setText(Expression text) {
    this.text = text;
  }

  public void setSubject(Expression subject) {
    this.subject = subject;
  }

  public void setAggiungiSha(Expression aggiungiSha) {
    this.aggiungiSha = aggiungiSha;
  }
}
