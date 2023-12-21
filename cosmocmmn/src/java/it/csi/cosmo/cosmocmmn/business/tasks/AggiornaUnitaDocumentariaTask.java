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
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoSoapStiloFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.AttributoUnitaDocumentaria;
import it.csi.cosmo.cosmosoap.dto.rest.OutputUnitaDocumentaria;

/**
 *
 */

public class AggiornaUnitaDocumentariaTask extends ParentFlowableTask {


  /**
   * parametri forniti in input da process design
   */
  private Expression nomeVariabile;
  private Expression documenti;
  private Expression attributi;
  private Expression jsonMapping;
  private Expression chiusura;


  @Override
  public void executeTask(DelegateExecution execution) {

    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();

    String nomeVariabileValue =
        this.requireClassField(execution, this.nomeVariabile, "nomeVariabile");

    ObjectMapper objectMapper = ObjectUtils.getDataMapper();
    OutputUnitaDocumentaria outputUnitaDocumentaria = null;
    try {

      outputUnitaDocumentaria = objectMapper.readValue(
          (String) execution.getVariable(nomeVariabileValue), OutputUnitaDocumentaria.class);
    } catch (JsonProcessingException e) {
      throw new InternalServerException(
          "Errore deserialize json OutputUnitaDocumentaria:" + e.getMessage());
    }

    var idUd = outputUnitaDocumentaria.getIdUD();

    List<String> tipologieDocumenti = getTipologieDocumenti(getClassField(execution, documenti));
    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {
      // call mapping
      Map<String, Object> mappingResult = richiediElaborazioneMappatura(businessKey, exJsonMapping);

      // replace mapping in fields
      tipologieDocumenti = getTipologieDocumenti(compila(tipologieDocumenti, mappingResult));

    }

    CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient =
        this.getBean(CosmoEcmDocumentiFeignClient.class);

    CosmoSoapStiloFeignClient cosmoSoapStiloFeignClient =
        this.getBean(CosmoSoapStiloFeignClient.class);

    List<AttributoUnitaDocumentaria> attributiList = this.getList(execution, attributi);

    List<AttributoUnitaDocumentaria> chiusuraList = this.getList(execution, chiusura);

    if (tipologieDocumenti != null && !tipologieDocumenti.isEmpty()) {

      String filter = getDocumentiInvioStiloFilter(businessKey,
          getTipiDocumento(tipologieDocumenti), outputUnitaDocumentaria.getIdUD());

      List<Documento> documentiInvioStilo =
          objectMapper.convertValue(cosmoEcmDocumentiFeignClient.getDocumentiInvioStilo(filter),
              new TypeReference<List<Documento>>() {});

      logger.info(method, "Numero documenti invio stilo trovati:" + documentiInvioStilo.size());

      documentiInvioStilo.forEach(documentoInvioStilo -> {

        AggiornaUnitaDocumentariaResponse aggiornaUnitaDocumentariaResponse = null;

        try {
          cosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoInvioDocumentoStilo(
              String.valueOf(documentoInvioStilo.getId()), Long.valueOf(idUd));

          aggiornaUnitaDocumentariaResponse =
              cosmoSoapStiloFeignClient.putAggiornaUnitaDocumentaria(
                  getAggiornaUnitaDocumentariaRequest(documentoInvioStilo, idUd, attributiList));

        } catch (Exception e) {
          this.logger.error(method,
              "gestione idDocumento " + documentoInvioStilo.getId() + "idUd" + idUd + " fallita",
              e);
          postDocumentiIdDocumentoEsitoInvioStiloWithError(documentoInvioStilo, idUd,
              e.getMessage());

          throw new InternalServerException(
              "Errore nell'invio del documento " + documentoInvioStilo.getId() + "", e);
        }

        String wsResult =
            aggiornaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria().getWsResult();

        EsitoInvioStiloRequest esitoInvioStiloRequest = new EsitoInvioStiloRequest();
        esitoInvioStiloRequest.setIdUd(Long.valueOf(idUd));
        esitoInvioStiloRequest.setRisultato(Integer.parseInt(wsResult));
        if (!wsResult.equals("1")) {
          esitoInvioStiloRequest.setCodiceEsitoInvioStilo(aggiornaUnitaDocumentariaResponse
              .getBaseOutputUnitaDocumentaria().getWsError().getErrorNumber());
          esitoInvioStiloRequest.setMessaggioEsitoInvioStilo(aggiornaUnitaDocumentariaResponse
              .getBaseOutputUnitaDocumentaria().getWsError().getErrorMessage());
        }

        try {
          cosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoEsitoInvioStilo(
              documentoInvioStilo.getId().toString(), esitoInvioStiloRequest);
        } catch (Exception e) {
          this.logger.error(method, "postDocumentiIdDocumentoEsitoInvioStilo idDocumento="
              + documentoInvioStilo.getId() + "idUD" + idUd + " fallita", e);
          throw new InternalServerException("Errore nell'aggiornamento del documento "
              + documentoInvioStilo.getId() + " inviato a stilo", e);
        }

        if (!wsResult.equals("1")) {
          String errorMessage = aggiornaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria()
              .getWsError().getErrorMessage();
          Integer errorNumber = aggiornaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria()
              .getWsError().getErrorNumber();
          logger.error(method,
              "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
          throw new InternalServerException(
              "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
        }
      });
    }



    if (chiusuraList != null && !chiusuraList.isEmpty()) {
      try {

        var result = cosmoSoapStiloFeignClient
            .putAggiornaUnitaDocumentaria(getAggiornaUnitaDocumentariaRequest(null,
                outputUnitaDocumentaria.getIdUD(), chiusuraList));
        String wsResult = result.getBaseOutputUnitaDocumentaria().getWsResult();
        if (!wsResult.equals("1")) {
          String errorMessage =
              result.getBaseOutputUnitaDocumentaria().getWsError().getErrorMessage();
          Integer errorNumber =
              result.getBaseOutputUnitaDocumentaria().getWsError().getErrorNumber();
          logger.error(method,
              "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
          throw new InternalServerException(
              "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
        }
      }
      catch (Exception e) {
        this.logger.error(method,
            "putAggiornaUnitaDocumentaria per attributi idUD" + idUd + " fallita\"", e);

        throw new InternalServerException("Errore nella chiamata di completamento", e);
      }
    }



  }

  private void postDocumentiIdDocumentoEsitoInvioStiloWithError(Documento documentoInvioStilo,
      String idUd, String message) {

    CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient =
        this.getBean(CosmoEcmDocumentiFeignClient.class);


    EsitoInvioStiloRequest esitoInvioStiloRequest = new EsitoInvioStiloRequest();
    esitoInvioStiloRequest.setIdUd(Long.valueOf(idUd));
    esitoInvioStiloRequest.setRisultato(0);
    esitoInvioStiloRequest.setCodiceEsitoInvioStilo(999);
    esitoInvioStiloRequest.setMessaggioEsitoInvioStilo(message);

    try {
      cosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoEsitoInvioStilo(
          documentoInvioStilo.getId().toString(), esitoInvioStiloRequest);
    } catch (Exception e) {
      this.logger.error("postDocumentiIdDocumentoEsitoInvioStiloWithError",
          "postDocumentiIdDocumentoEsitoInvioStilo idDocumento" + documentoInvioStilo.getId()
          + "idUd" + idUd + " fallita",
          e);
      throw new InternalServerException(
          "Errore nella chiamata di completamento processo dell'invio a stilo", e);

    }


  }

  private String getDocumentiInvioStiloFilter(String businessKey,
      List<CodiceTipologiaDocumento> tipologieDocumenti, String idUD) {

    // refactoring appena possibile

    String tipologieDocumentiToString = String.join(",",
        tipologieDocumenti.stream().map(value -> ("\"" + value.getCodice() + "\""))
        .collect(Collectors.toList()));

    StringBuilder sb = new StringBuilder();
    tipologieDocumenti.forEach(tipo -> sb
        .append("{\"codice\": " + (tipo.getCodice() == null ? null : "\"" + tipo.getCodice() + "\"")
            + ", \"codicePadre\": "
            + (tipo.getCodicePadre() == null ? null : "\"" + tipo.getCodicePadre() + "\"") + "},"));


    return "{\"filter\":{\"idPratica\":{\"eq\": \"" + businessKey + "\"},"
    + "\"tipologiaDocumento\":{\"in\": [" + tipologieDocumentiToString + "]},"
    + "\"idUd\":{\"eq\": \""
    + idUD + "\"}, \"codici\": [" + sb.substring(0, sb.length() - 1) + "]}}";

  }

  private List<CodiceTipologiaDocumento> getTipiDocumento(List<String> codici) {
    List<CodiceTipologiaDocumento> output = new ArrayList<>();

    codici.forEach(codiceString -> {

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
  private AggiornaUnitaDocumentariaRequest getAggiornaUnitaDocumentariaRequest(
      Documento documentoInvioStilo, String idUD, List<AttributoUnitaDocumentaria> inputList) {

    AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest =
        new AggiornaUnitaDocumentariaRequest();
    aggiornaUnitaDocumentariaRequest.setIdUd(idUD);
    if (documentoInvioStilo != null && documentoInvioStilo.getId() != null)
      aggiornaUnitaDocumentariaRequest.setIdDocumento(String.valueOf(documentoInvioStilo.getId()));
    if (inputList != null && !inputList.isEmpty())
      aggiornaUnitaDocumentariaRequest.setAttributi(inputList);

    return aggiornaUnitaDocumentariaRequest;
  }

  private List<String> getTipologieDocumenti(List<String> raw) {
    if (raw == null || raw.isEmpty()) {
      return Collections.emptyList();
    }
    return raw.stream().flatMap(e -> Arrays.stream(e.split(","))).map(String::strip)
        .filter(StringUtils::isNotBlank).collect(Collectors.toList());
  }

  private List<String> getTipologieDocumenti(String raw) {
    if (StringUtils.isBlank(raw)) {
      return Collections.emptyList();
    }
    return Arrays.stream(raw.split(",")).map(String::strip).filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }

  private List<AttributoUnitaDocumentaria> getList(DelegateExecution execution, Expression input) {
    String attributiJsonValue = getClassField(execution, input);

    if (StringUtils.isBlank(attributiJsonValue))
      return new ArrayList<>();

    JavaType type = ObjectUtils.getDataMapper().getTypeFactory()
        .constructParametricType(ArrayList.class, AttributoUnitaDocumentaria.class);

    try {
      return ObjectUtils.getDataMapper().readValue(attributiJsonValue, type);
    } catch (JsonProcessingException e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  @Override
  protected String getClassField(DelegateExecution execution, Expression field) {
    if (field == null) {
      return null;
    }
    String raw = (String) field.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  @Override
  protected String requireClassField(DelegateExecution execution, Expression field, String name) {
    String raw = getClassField(execution, field);
    if (StringUtils.isBlank(raw)) {
      throw new InternalServerException("Valorizzare il class field '" + name + "'");
    }
    return raw;
  }

  private String getJsonMapping(DelegateExecution execution) {
    return getClassField(execution, jsonMapping);
  }

  private Map<String, Object> richiediElaborazioneMappatura(String idPratica, String mappatura) {

    GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
    requestElaborazione.setMappatura(mappatura);

    logger.debug("richiediElaborazioneMappatura",
        "richiedo elaborazione mappa parametri per invio mail relativa alla pratica: {}",
        mappatura);

    CosmoBusinessPraticheFeignClient client = this.getBean(CosmoBusinessPraticheFeignClient.class);

    @SuppressWarnings("unchecked")
    Map<String, Object> datiElaborati = (Map<String, Object>) client
    .postPraticheIdElaborazione(Long.valueOf(idPratica), requestElaborazione);

    return datiElaborati;
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

    // utilizzo metodi deprecati perche' il replacement sta in un jar che non abbiamo.
    StrSubstitutor sub = new StrSubstitutor(risultatoElaborazione);
    return sub.replace(raw);
  }

  public void setNomeVariabile(Expression nomeVariabile) {
    this.nomeVariabile = nomeVariabile;
  }

  public void setDocumenti(Expression documenti) {
    this.documenti = documenti;
  }

  public void setAttributi(Expression attributi) {
    this.attributi = attributi;
  }

  public void setChiusura(Expression chiusura) {
    this.chiusura = chiusura;
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
  }

}
