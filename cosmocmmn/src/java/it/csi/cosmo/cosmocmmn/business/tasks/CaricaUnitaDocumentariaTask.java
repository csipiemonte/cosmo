/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoPratichePraticheFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoSoapStiloFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmosoap.dto.rest.AttributoUnitaDocumentaria;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaResponse;

/**
 *
 */

public class CaricaUnitaDocumentariaTask extends ParentFlowableTask {


  /**
   * parametri forniti in input da process design
   */
  private Expression registrazioneStilo;
  private Expression tipoDocumentoStilo;
  private Expression oggetto;
  private Expression note;
  private Expression attributi;
  private Expression jsonMapping;
  private Expression nomeVariabile;


  @Override
  public void executeTask(DelegateExecution execution) {

    final var method = "executeTask";

    String nomeVariabileValue =
        this.requireClassField(execution, this.nomeVariabile, "nomeVariabile");

    CaricaUnitaDocumentariaRequest caricaUnitaDocumentariaRequest =
        getCaricaUnitaDocumentariaRequest(execution);

    CosmoSoapStiloFeignClient cosmoSoapStiloFeignClient =
        this.getBean(CosmoSoapStiloFeignClient.class);

    CaricaUnitaDocumentariaResponse caricaUnitaDocumentariaResponse = null;

    try {
      caricaUnitaDocumentariaResponse =
          cosmoSoapStiloFeignClient
          .postStiloCaricaUnitaDocumentaria(caricaUnitaDocumentariaRequest);
    }
    catch (Exception e) {
      logger.error(method, "Errore nella comunicazine con Stilo", e);
      throw new InternalServerException("Errore nella comunicazine con Stilo", e);
    }

    if (!caricaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria().getWsResult()
        .equals("1")) {
      String errorMessage = caricaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria()
          .getWsError().getErrorMessage();
      Integer errorNumber = caricaUnitaDocumentariaResponse.getBaseOutputUnitaDocumentaria()
          .getWsError().getErrorNumber();
      logger.error(method,
          "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
      throw new InternalServerException(
          "Errore addUD stilo errorNumber: " + errorNumber + " errorMessage: " + errorMessage);
    }

    else {
      ObjectMapper mapper = ObjectUtils.getDataMapper();

      String jsonUnitaDocumentaria;
      try {

        jsonUnitaDocumentaria =
            mapper.writeValueAsString(caricaUnitaDocumentariaResponse.getOutputUnitaDocumentaria());

        execution.setVariable(nomeVariabileValue, jsonUnitaDocumentaria);
      } catch (JsonProcessingException e) {
        logger.error(method, "Errore nel parsing del json", e);
        throw new BadRequestException("Errore nel parsing del json");
      }
    }

  }

  private CaricaUnitaDocumentariaRequest getCaricaUnitaDocumentariaRequest(
      DelegateExecution execution) {

    String businessKey = execution.getProcessInstanceBusinessKey();

    String oggettoValue = this.requireClassField(execution, this.oggetto, "oggetto");
    String noteValue = this.getClassField(execution, this.note);
    String registrazioneStiloValue = this.getClassField(execution, this.registrazioneStilo);
    String tipoDocumentoStiloValue = this.getClassField(execution, this.tipoDocumentoStilo);

    if (registrazioneStiloValue == null || tipoDocumentoStiloValue == null) {
      CosmoPratichePraticheFeignClient cosmoPratichePraticheFeignClient =
          this.getBean(CosmoPratichePraticheFeignClient.class);
      Pratica pratica = cosmoPratichePraticheFeignClient.getPraticheIdPratica(businessKey, false);

      registrazioneStiloValue = registrazioneStiloValue != null ? registrazioneStiloValue
          : pratica.getTipo().getRegistrazioneStilo();

      tipoDocumentoStiloValue = tipoDocumentoStiloValue != null ? tipoDocumentoStiloValue
          : pratica.getTipo().getTipoUnitaDocumentariaStilo();

      if (StringUtils.isBlank(registrazioneStiloValue)) {
        throw new InternalServerException("Valorizzare il class field 'registrazioneStilo'");
      }

      if (StringUtils.isBlank(tipoDocumentoStiloValue)) {
        throw new InternalServerException("Valorizzare il class field 'tipoDocumentoStilo'");
      }
    }

    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {
      // call mapping
      Map<String, Object> mappingResult = richiediElaborazioneMappatura(businessKey, exJsonMapping);

      // replace mapping in fields
      oggettoValue = compila(oggettoValue, mappingResult);
      noteValue = compila(noteValue, mappingResult);
    }

    List<AttributoUnitaDocumentaria> attributiList = this.getAttributi(execution);
    if (attributiList.isEmpty()) {
      throw new InternalServerException("Valorizzare il class field 'attributi'");
    }

    CaricaUnitaDocumentariaRequest caricaUnitaDocumentariaRequest = new CaricaUnitaDocumentariaRequest();
    caricaUnitaDocumentariaRequest.setRegistrazioneStilo(registrazioneStiloValue);
    caricaUnitaDocumentariaRequest.setTipoDocumentoStilo(tipoDocumentoStiloValue);
    caricaUnitaDocumentariaRequest.setOggetto(oggettoValue);
    caricaUnitaDocumentariaRequest.setNote(noteValue);
    caricaUnitaDocumentariaRequest.setNroAllegati(0);
    caricaUnitaDocumentariaRequest.setAttributi(attributiList);

    return caricaUnitaDocumentariaRequest;

  }

  private List<AttributoUnitaDocumentaria> getAttributi(DelegateExecution execution) {
    String attributiJsonValue = getClassField(execution, attributi);

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
    Map<String, Object> datiElaborati =
    (Map<String, Object>) client.postPraticheIdElaborazione(Long.valueOf(idPratica),
        requestElaborazione);

    return datiElaborati;
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

  public void setRegistrazioneStilo(Expression registrazioneStilo) {
    this.registrazioneStilo = registrazioneStilo;
  }

  public void setTipoDocumentoStilo(Expression tipoDocumentoStilo) {
    this.tipoDocumentoStilo = tipoDocumentoStilo;
  }

  public void setOggetto(Expression oggetto) {
    this.oggetto = oggetto;
  }

  public void setNote(Expression note) {
    this.note = note;
  }

  public void setAttributi(Expression attributi) {
    this.attributi = attributi;
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
  }

  public void setNomeVariabile(Expression nomeVariabile) {
    this.nomeVariabile = nomeVariabile;
  }


}
