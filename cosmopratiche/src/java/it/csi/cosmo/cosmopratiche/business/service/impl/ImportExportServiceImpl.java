/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.flowable.rest.service.api.repository.FormDefinitionResponse;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneMetadati;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneMetadati_;
import it.csi.cosmo.common.entities.CosmoDChiaveParametroFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoDChiaveParametroFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio_;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore_;
import it.csi.cosmo.common.entities.CosmoDFiltroCampo;
import it.csi.cosmo.common.entities.CosmoDFiltroCampo_;
import it.csi.cosmo.common.entities.CosmoDFormatoCampo;
import it.csi.cosmo.common.entities.CosmoDFormatoCampo_;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoDProfiloFeq;
import it.csi.cosmo.common.entities.CosmoDProfiloFeq_;
import it.csi.cosmo.common.entities.CosmoDSceltaMarcaTemporale;
import it.csi.cosmo.common.entities.CosmoDSceltaMarcaTemporale_;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDStatoPratica_;
import it.csi.cosmo.common.entities.CosmoDTabDettaglio;
import it.csi.cosmo.common.entities.CosmoDTabDettaglio_;
import it.csi.cosmo.common.entities.CosmoDTipoCredenzialiFirma;
import it.csi.cosmo.common.entities.CosmoDTipoCredenzialiFirma_;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoOtp;
import it.csi.cosmo.common.entities.CosmoDTipoOtp_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoDTipoTag;
import it.csi.cosmo.common.entities.CosmoDTipoTag_;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica_;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalitaPK;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita_;
import it.csi.cosmo.common.entities.CosmoRFunzionalitaParametroFormLogico;
import it.csi.cosmo.common.entities.CosmoRFunzionalitaParametroFormLogico_;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPraticaPK;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValorePK;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore_;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPraticaPK;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPraticaPK_;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK_;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopraticaPK;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopraticaPK_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica_;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte_;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoTRisorsaTemplateReport;
import it.csi.cosmo.common.entities.CosmoTRisorsaTemplateReport_;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTTemplateFea;
import it.csi.cosmo.common.entities.CosmoTTemplateFea_;
import it.csi.cosmo.common.entities.CosmoTTemplateReport;
import it.csi.cosmo.common.entities.CosmoTTemplateReport_;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso_;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ImportExportService;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.DryRunExitException;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.DryRunExitException.FieldConflictResolutionInput;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.DryRunExitException.FieldConflictResolutionInputAction;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.EsportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.ImportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.LivelloMessaggio;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.MessaggioImportazione;
import it.csi.cosmo.cosmopratiche.dto.rest.ProcessDeploymentWrapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoCConfigurazioneMetadatiRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDChiaveParametroFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDCustomFormFormioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFiltroCampoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFormatoCampoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTabsDettaglioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTrasformazioneDatiPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRFormLogicoIstanzaFunzionalitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRFunzionalitaParametroFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRGruppoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRStatoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTRisorsaTemplateReportRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTemplateFeaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTemplateReportRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTVariabileProcessoRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
@Transactional
public class ImportExportServiceImpl implements ImportExportService {

  private static final String CLASS_NAME = ImportExportServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String CODICE_FISCALE_UTENTE = "codiceFiscaleUtente";
  private static final String ID_ENTE = "idEnte";
  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTFormLogicoRepository formLogicoRepository;

  @Autowired
  private CosmoTUtenteRepository utenteRepository;

  @Autowired
  private CosmoTGruppoRepository gruppoRepository;

  @Autowired
  private CosmoTUtenteGruppoRepository utenteGruppoRepository;

  @Autowired
  private CosmoRGruppoTipoPraticaRepository gruppoTipoPraticaRepository;

  @Autowired
  private CosmoRStatoTipoPraticaRepository cosmoRStatoTipoPraticaRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoRFunzionalitaParametroFormLogicoRepository cosmoRFunzionalitaParametroFormLogicoRepository;

  @Autowired
  private CosmoDFunzionalitaFormLogicoRepository cosmoDFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoDChiaveParametroFunzionalitaFormLogicoRepository cosmoDChiaveParametroFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoRFormLogicoIstanzaFunzionalitaRepository cosmoRFormLogicoIstanzaFunzionalitaRepository;

  @Autowired
  private CosmoCConfigurazioneMetadatiRepository cosmoCConfigurazioneMetadatiRepository;

  @Autowired
  private CosmoDCustomFormFormioRepository cosmoDCustomFormFormioRepository;

  @Autowired
  private CosmoTTemplateReportRepository cosmoTTemplateReportRepository;

  @Autowired
  private CosmoTRisorsaTemplateReportRepository cosmoTRisorsaTemplateReportRepository;

  @Autowired
  private CosmoDTrasformazioneDatiPraticaRepository cosmoDTrasformazioneDatiPraticaRepository;

  @Autowired
  private CosmoDTabsDettaglioRepository cosmoDTabsDettaglioRepository;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  private CosmoTVariabileProcessoRepository cosmoTVariabileProcessoRepository;

  @Autowired
  private CosmoDFiltroCampoRepository cosmoDFiltroCampoRepository;

  @Autowired
  private CosmoDFormatoCampoRepository cosmoDFormatoCampoRepository;

  @Autowired
  private CosmoTTemplateFeaRepository cosmoTTemplateFeaRepository;

  @Autowired
  private EntityManager entityManager;

  private static Map<String, Boolean> exportPolicyVerifications = new HashMap<>();

  private static Map<String, Boolean> importPolicyVerifications = new HashMap<>();

  @Override
  public byte[] esporta(EsportaTipoPraticaRequest request) {
    try {
      return doExport(request);
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  @Override
  public Map<String, Object> importa(ImportaTipoPraticaRequest request) {
    try {
      return doImport(request);
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  @Override
  public List<Object> getOpzioniEsportazioneTenant(String codiceTipoPratica) {
    ValidationUtils.require(codiceTipoPratica, "codiceTipoPratica");

    // get dei tenant associati al processo

    CosmoDTipoPratica tipoPratica = cosmoDTipoPraticaRepository.findOneActive(codiceTipoPratica)
        .orElseThrow(NotFoundException::new);

    var definitions = cosmoCmmnFeignClient.listProcessDefinitionsByKey(true,
        tipoPratica.getProcessDefinitionKey(), null);

    List<Object> output = new LinkedList<>();
    for (var definition : definitions.getData()) {
      if (StringUtils.isEmpty(definition.getTenantId())) {
        continue;
      }
      var ente =
          cosmoTEnteRepository.findOneByField(CosmoTEnte_.codiceIpa, definition.getTenantId());
      if (ente.isPresent()) {
        var dto = new HashMap();
        dto.put("nome", ente.get().getNome());
        dto.put("codiceIpa", ente.get().getCodiceIpa());
        output.add(dto);
      }
    }

    return output;
  }

  public ExportPolicy<CosmoTFormLogico> buildPolicyForFormLogicoExport() {

    var cosmoRFunzionalitaParametroFormLogicoPolicy =
        new ExportPolicy<CosmoRFunzionalitaParametroFormLogico>();
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.codiceFunzionalita, ExportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.codiceParametro, ExportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.funzionalita, ExportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.id, ExportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.obbligatorio, ExportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.parametro, ExportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRFunzionalitaParametroFormLogico_.class,
        cosmoRFunzionalitaParametroFormLogicoPolicy);

    var cosmoDFunzionalitaFormLogicoPolicy = new ExportPolicy<CosmoDFunzionalitaFormLogico>();
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.codice,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.descrizione,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.handler,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.multiIstanza,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDFunzionalitaFormLogico_.eseguibileMassivamente, ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ExportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoDFunzionalitaFormLogico_.associazioniParametri,
        ExportPolicy.clone(cosmoRFunzionalitaParametroFormLogicoPolicy));
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.multiIstanza,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDFunzionalitaFormLogico_.class, cosmoDFunzionalitaFormLogicoPolicy);

    var cosmoDChiaveParametroFunzionalitaFormLogicoPolicy =
        new ExportPolicy<CosmoDChiaveParametroFunzionalitaFormLogico>();
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.codice, ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.descrizione, ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.schema, ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.tipo, ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.valoreDefault, ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDChiaveParametroFunzionalitaFormLogico_.class,
        cosmoDChiaveParametroFunzionalitaFormLogicoPolicy);

    var cosmoRIstanzaFormLogicoParametroValoresPolicy =
        new ExportPolicy<CosmoRIstanzaFormLogicoParametroValore>();
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy
    .put(CosmoRIstanzaFormLogicoParametroValore_.id, ExportPolicy.ignore());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(
        CosmoRIstanzaFormLogicoParametroValore_.cosmoTIstanzaFunzionalitaFormLogico,
        ExportPolicy.ignore());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy
    .put(CosmoRIstanzaFormLogicoParametroValore_.valoreParametro, ExportPolicy.copy());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());

    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(
        CosmoRIstanzaFormLogicoParametroValore_.cosmoDChiaveParametroFunzionalitaFormLogico,
        ExportPolicy.clone(cosmoDChiaveParametroFunzionalitaFormLogicoPolicy));

    checkPolicyIsComplete(CosmoRIstanzaFormLogicoParametroValore_.class,
        cosmoRIstanzaFormLogicoParametroValoresPolicy);

    var cosmoTIstanzaFunzionalitaFormLogicoPolicy =
        new ExportPolicy<CosmoTIstanzaFunzionalitaFormLogico>();
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoRFormLogicoIstanzaFunzionalitas,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoTIstanzaFunzionalitaFormLogico_.descrizione, ExportPolicy.copy());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoTIstanzaFunzionalitaFormLogico_.id, ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica,
        ExportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoDFunzionalitaFormLogico,
        ExportPolicy.clone(cosmoDFunzionalitaFormLogicoPolicy));
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoRIstanzaFormLogicoParametroValores,
        ExportPolicy.clone(cosmoRIstanzaFormLogicoParametroValoresPolicy));
    checkPolicyIsComplete(CosmoTIstanzaFunzionalitaFormLogico_.class,
        cosmoTIstanzaFunzionalitaFormLogicoPolicy);

    var cosmoRFormLogicoIstanzaFunzionalitaPolicy =
        new ExportPolicy<CosmoRFormLogicoIstanzaFunzionalita>();
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTFormLogico, ExportPolicy.ignore());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.id, ExportPolicy.ignore());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.ordine, ExportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.eseguibileMassivamente, ExportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(
        CosmoRFormLogicoIstanzaFunzionalita_.cosmoTIstanzaFunzionalitaFormLogico,
        ExportPolicy.clone(cosmoTIstanzaFunzionalitaFormLogicoPolicy));
    checkPolicyIsComplete(CosmoRFormLogicoIstanzaFunzionalita_.class,
        cosmoRFormLogicoIstanzaFunzionalitaPolicy);

    var formLogicoPolicy = new ExportPolicy<CosmoTFormLogico>();
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.codice, ExportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.descrizione, ExportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.eseguibileMassivamente,
        ExportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.id, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.wizard, ExportPolicy.copy());

    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoTAttivitas, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoRFormLogicoIstanzaFunzionalitas,
        ExportPolicy.clone(cosmoRFormLogicoIstanzaFunzionalitaPolicy));
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoTEnte, ExportPolicy.ignore());

    formLogicoPolicy = formLogicoPolicy.withPreExport((entity, map) -> map.put("enteForm",
        entity.getCosmoTEnte() != null ? entity.getCosmoTEnte().getCodiceIpa() : null));
    checkPolicyIsComplete(CosmoTFormLogico_.class, formLogicoPolicy);

    return formLogicoPolicy;
  }

  public ExportPolicy<CosmoDTipoPratica> buildPolicyForTipoPraticaExport() {

    var exportSpecForConfigMetadati = new ExportPolicy<CosmoCConfigurazioneMetadati>();
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.chiave,
        ExportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.descrizione,
        ExportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.valore,
        ExportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.cosmoDTipoPratica,
        ExportPolicy.ignore());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCEntity_.dtInizioVal, ExportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCEntity_.dtFineVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoCConfigurazioneMetadati_.class, exportSpecForConfigMetadati);

    var cosmoRStatoTipoPraticasPolicy = buildPolicyForStatoDocExport();

    var cosmoRTipodocTipopraticasPolicy = buildPolicyForTipoDocExport();

    var cosmoRTipodocTipodocPolicy = buildPolicyForTipoDocTipoDocExport();

    var cosmoRTabDettaglioTipoPraticaPolicy = buildPolicyForTabDettaglioExport();

    var trasformazioniPolicy = new ExportPolicy<CosmoDTrasformazioneDatiPratica>();
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.codiceFase,
        ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.codiceTipoPratica,
        ExportPolicy.ignore());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.definizione,
        ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.descrizione,
        ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.id,
        ExportPolicy.ignore());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.obbligatoria,
        ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.ordine,
        ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.tipoPratica,
        ExportPolicy.ignore());
    checkPolicyIsComplete(CosmoDTrasformazioneDatiPratica_.class, trasformazioniPolicy);

    var enteCertificatorePolicy = new ExportPolicy<CosmoDEnteCertificatore>();
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codice, ExportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codiceCa,
        ExportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codiceTsa,
        ExportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoDTipoPraticas,
        ExportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoREnteCertificatoreEntes,
        ExportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.descrizione,
        ExportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoTCertificatoFirmas,
        ExportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.provider,
        ExportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ExportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDEnteCertificatore_.class, enteCertificatorePolicy);

    var tipoCredenzialiFirmaPolicy = new ExportPolicy<CosmoDTipoCredenzialiFirma>();
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.codice,
        ExportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.descrizione,
        ExportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.cosmoDTipoPraticas,
        ExportPolicy.ignore());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.nonValidoInApposizione,
        ExportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.cosmoTCertificatoFirmas,
        ExportPolicy.ignore());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ExportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoCredenzialiFirma_.class, tipoCredenzialiFirmaPolicy);

    var tipoOtpPolicy = new ExportPolicy<CosmoDTipoOtp>();
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.codice, ExportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.descrizione, ExportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.nonValidoInApposizione, ExportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.cosmoDTipoPraticas, ExportPolicy.ignore());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.cosmoTCertificatoFirmas, ExportPolicy.ignore());
    checkPolicyIsComplete(CosmoDTipoOtp_.class, tipoOtpPolicy);

    var profiloFeqPolicy = new ExportPolicy<CosmoDProfiloFeq>();
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.codice, ExportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.descrizione, ExportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.nonValidoInApposizione,
        ExportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoDTipoPraticas, ExportPolicy.ignore());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoRFormatoFileProfiloFeqTipoFirmas,
        ExportPolicy.ignore());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoTCertificatoFirmas,
        ExportPolicy.ignore());
    checkPolicyIsComplete(CosmoDProfiloFeq_.class, profiloFeqPolicy);

    var sceltaMarcaTemporalePolicy = new ExportPolicy<CosmoDSceltaMarcaTemporale>();
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.codice,
        ExportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.descrizione,
        ExportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.nonValidoInApposizione,
        ExportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ExportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ExportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.cosmoTCertificatoFirmas,
        ExportPolicy.ignore());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.cosmoDTipoPraticas,
        ExportPolicy.ignore());
    checkPolicyIsComplete(CosmoDSceltaMarcaTemporale_.class, sceltaMarcaTemporalePolicy);

    var exportSpecForTipoPratica = new ExportPolicy<CosmoDTipoPratica>();
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.codice, ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.descrizione, ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.caseDefinitionKey,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.codiceApplicazioneStardas,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRStatoTipoPraticas,
        ExportPolicy.clone(cosmoRStatoTipoPraticasPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTipodocTipopraticas,
        ExportPolicy.clone(cosmoRTipodocTipopraticasPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTEnte, ExportPolicy.ignore());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTPraticas,
        ExportPolicy.ignore());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.creabileDaInterfaccia,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.creabileDaServizio,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.annullabile, ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.assegnabile, ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.condivisibile,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.processDefinitionKey,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.trasformazioni,
        ExportPolicy.clone(trasformazioniPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTabDettaglioTipoPraticas,
        ExportPolicy.clone(cosmoRTabDettaglioTipoPraticaPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.responsabileTrattamentoStardas,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.overrideResponsabileTrattamento,
        ExportPolicy.copy());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.overrideFruitoreDefault,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.codiceFruitoreStardas,
        ExportPolicy.copy());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoCConfigurazioneMetadatis,
        ExportPolicy.clone(exportSpecForConfigMetadati));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRGruppoTipoPraticas,
        ExportPolicy.ignore());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDCustomFormFormios,
        ExportPolicy.ignore());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.registrazioneStilo,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.tipoUnitaDocumentariaStilo,
        ExportPolicy.copy());
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.icona, ExportPolicy.copy());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTVariabileProcessos,
        ExportPolicy.ignore());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTMessaggioNotificas,
        ExportPolicy.ignore());

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTipoDocumentoTipoDocumentos,
        ExportPolicy.clone(cosmoRTipodocTipodocPolicy));


    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTTemplateFeas,
        ExportPolicy.clone(buildPolicyForTTemplateFeaExport()));

    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDEnteCertificatore,
        ExportPolicy.clone(enteCertificatorePolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDTipoCredenzialiFirma,
        ExportPolicy.clone(tipoCredenzialiFirmaPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDSceltaMarcaTemporale,
        ExportPolicy.clone(sceltaMarcaTemporalePolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDProfiloFeq,
        ExportPolicy.clone(profiloFeqPolicy));
    exportSpecForTipoPratica.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDTipoOtp,
        ExportPolicy.clone(tipoOtpPolicy));

    checkPolicyIsComplete(CosmoDTipoPratica_.class, exportSpecForTipoPratica);

    return exportSpecForTipoPratica;
  }

  private ExportPolicy<CosmoTTemplateFea> buildPolicyForTTemplateFeaExport() {

    var tipoDocumentoPolicy = new ExportPolicy<CosmoDTipoDocumento>();
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codice, ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codiceStardas, ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRFormatoFileTipoDocumentos,
        ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRTipodocTipopraticas,
        ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosAllegato, ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosPadre, ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTDocumentos,
        ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTTemplateFeas,
        ExportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.descrizione, ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.dimensioneMassima,
        ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.firmabile, ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoDocumento_.class, tipoDocumentoPolicy);

    var templatesFEAPolicy = new ExportPolicy<CosmoTTemplateFea>();
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.id, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.descrizione, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.coordinataX, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.coordinataY, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.pagina, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.caricatoDaTemplate, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.ente, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.cosmoTPratica, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.tipologiaDocumento,
        ExportPolicy.clone(tipoDocumentoPolicy));
    templatesFEAPolicy.fieldsPolicy.put(CosmoTTemplateFea_.tipologiaPratica, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.copy());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());
    templatesFEAPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());

    templatesFEAPolicy = templatesFEAPolicy.withPreExport(
        (entity, map) -> map.put("codiceTipoPratica", entity.getTipologiaPratica().getCodice()));

    checkPolicyIsComplete(CosmoTTemplateFea_.class, templatesFEAPolicy);


    return templatesFEAPolicy;
  }

  private ExportPolicy<CosmoTTag> buildPolicyForTTagExport() {

    var exportSpecForDTipoTag = new ExportPolicy<CosmoDTipoTag>();

    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDTipoTag_.codice, ExportPolicy.copy());
    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDTipoTag_.cosmoTTags, ExportPolicy.ignore());
    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDTipoTag_.descrizione, ExportPolicy.copy());
    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDTipoTag_.label, ExportPolicy.copy());
    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());
    exportSpecForDTipoTag.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoTag_.class, exportSpecForDTipoTag);

    var exportSpecForTTag = new ExportPolicy<CosmoTTag>();

    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.codice, ExportPolicy.copy());
    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.descrizione, ExportPolicy.copy());
    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.cosmoDTipoTag,
        ExportPolicy.clone(exportSpecForDTipoTag));
    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.cosmoRPraticaTags, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.cosmoTEnte, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.cosmoRUtenteGruppoTags, ExportPolicy.ignore());

    exportSpecForTTag.fieldsPolicy.put(CosmoTTag_.id, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpecForTTag.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoTTag_.class, exportSpecForTTag);

    return exportSpecForTTag;

  }

  public ImportPolicy<CosmoTTag> buildPolicyForTagsImport(ImportContext importContext) {

    var importDTipoTagSpec = new ImportPolicy<CosmoDTipoTag>();

    importDTipoTagSpec.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.ignore());
    importDTipoTagSpec.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());

    importDTipoTagSpec.fieldsPolicy.put(CosmoDTipoTag_.codice, ImportPolicy.copy());
    importDTipoTagSpec.fieldsPolicy.put(CosmoDTipoTag_.cosmoTTags, ImportPolicy.ignore());
    importDTipoTagSpec.fieldsPolicy.put(CosmoDTipoTag_.descrizione, ImportPolicy.copy());
    importDTipoTagSpec.fieldsPolicy.put(CosmoDTipoTag_.label, ImportPolicy.copy());


    checkPolicyIsComplete(CosmoDTipoTag_.class, importDTipoTagSpec);


    var importSpec = new ImportPolicy<CosmoTTag>();

    importSpec.fieldsPolicy.put(CosmoTTag_.id, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTTag_.codice, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTTag_.descrizione, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTTag_.cosmoDTipoTag, ImportPolicy.clone(importDTipoTagSpec));
    importSpec.fieldsPolicy.put(CosmoTTag_.cosmoRPraticaTags, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTTag_.cosmoRUtenteGruppoTags, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTTag_.cosmoTEnte, ImportPolicy.ignore());

    importSpec = importSpec.withPrePersistExecution(ctx -> {
      var target = (CosmoTTag) ctx.target;
      target.setCosmoTEnte(cosmoTEnteRepository
          .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElse(null));
    });

    checkPolicyIsComplete(CosmoTTag_.class, importSpec);

    importSpec = importSpec.withConflictChecker(ctx -> {
      var target = (CosmoTTag) ctx.target;
      var results = cosmoTTagRepository.findAllNotDeleted((root, cq, cb) ->

      cb.and(cb.equal(root.get(CosmoTTag_.codice), target.getCodice()), cb.equal(
          root.get(CosmoTTag_.cosmoTEnte).get(CosmoTEnte_.codiceIpa), importContext.tenantId)));

      if (results.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else if (!results.isEmpty()) {
        return results.get(0);
      }
      return null;

    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTTag) ctx.target;
      return target.getCodice();
    });

    return importSpec;
  }



  private ExportPolicy<CosmoTVariabileProcesso> buildPolicyForTVariabileProcessoExport() {

    var exportSpecForDFiltroCampo = new ExportPolicy<CosmoDFiltroCampo>();
    exportSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.codice, ExportPolicy.copy());
    exportSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.cosmoTVariabileProcessos,
        ExportPolicy.ignore());
    exportSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.descrizione, ExportPolicy.copy());
    exportSpecForDFiltroCampo.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());
    exportSpecForDFiltroCampo.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDFiltroCampo_.class, exportSpecForDFiltroCampo);

    var exportSpecForDFormatoCampo = new ExportPolicy<CosmoDFormatoCampo>();

    exportSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.codice, ExportPolicy.copy());
    exportSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.cosmoTVariabileProcessos,
        ExportPolicy.ignore());
    exportSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.descrizione,
        ExportPolicy.copy());
    exportSpecForDFormatoCampo.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());
    exportSpecForDFormatoCampo.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDFormatoCampo_.class, exportSpecForDFormatoCampo);

    var exportSpecForTVariabileProcesso = new ExportPolicy<CosmoTVariabileProcesso>();

    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.filtroCampo,
        ExportPolicy.clone(exportSpecForDFiltroCampo));
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.formatoCampo,
        ExportPolicy.clone(exportSpecForDFormatoCampo));
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.id,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.nomeVariabile,
        ExportPolicy.copy());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.nomeVariabileFlowable,
        ExportPolicy.copy());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.visualizzareInTabella,
        ExportPolicy.copy());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.tipoPratica,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtCancellazione,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtInserimento,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteInserimento,
        ExportPolicy.ignore());
    exportSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica,
        ExportPolicy.ignore());

    exportSpecForTVariabileProcesso = exportSpecForTVariabileProcesso.withPreExport(
        (entity, map) -> map.put("codiceTipoPratica", entity.getTipoPratica().getCodice()));

    checkPolicyIsComplete(CosmoTVariabileProcesso_.class, exportSpecForTVariabileProcesso);

    return exportSpecForTVariabileProcesso;

  }

  private ExportPolicy<CosmoRStatoTipoPratica> buildPolicyForStatoDocExport() {

    var cosmoDStatPraticasPolicy = new ExportPolicy<CosmoDStatoPratica>();
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.classe, ExportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.codice, ExportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.cosmoRStatoTipoPraticas,
        ExportPolicy.ignore());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.descrizione, ExportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDStatoPratica_.class, cosmoDStatPraticasPolicy);

    var cosmoRStatoTipoPraticasPolicy = new ExportPolicy<CosmoRStatoTipoPratica>();
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.id,
        ExportPolicy.ignore());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal, ExportPolicy.copy());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal, ExportPolicy.copy());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.cosmoDStatoPratica,
        ExportPolicy.clone(cosmoDStatPraticasPolicy));
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.cosmoDTipoPratica,
        ExportPolicy.ignore());
    checkPolicyIsComplete(CosmoRStatoTipoPratica_.class, cosmoRStatoTipoPraticasPolicy);

    return cosmoRStatoTipoPraticasPolicy;
  }

  private ExportPolicy<CosmoRTipoDocumentoTipoDocumento> buildPolicyForTipoDocTipoDocExport() {
    var cosmoRTipoDocumentoTipoDocumentosPKPolicy =
        new ExportPolicy<CosmoRTipoDocumentoTipoDocumentoPK>();
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codiceAllegato, ExportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codicePadre, ExportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codiceTipoPratica, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipoDocumentoTipoDocumentoPK_.class,
        cosmoRTipoDocumentoTipoDocumentosPKPolicy);

    var cosmoRTipoDocumentoTipoDocumentosPolicy =
        new ExportPolicy<CosmoRTipoDocumentoTipoDocumento>();
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoRTipoDocumentoTipoDocumento_.id,
        ExportPolicy.clone(cosmoRTipoDocumentoTipoDocumentosPKPolicy));
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.codiceStardasAllegato, ExportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica, ExportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato, ExportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre, ExportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipoDocumentoTipoDocumento_.class,
        cosmoRTipoDocumentoTipoDocumentosPolicy);

    return cosmoRTipoDocumentoTipoDocumentosPolicy;
  }

  private ExportPolicy<CosmoRTipodocTipopratica> buildPolicyForTipoDocExport() {
    var cosmoDTipoDocumentoPolicy = new ExportPolicy<CosmoDTipoDocumento>();

    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codice, ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codiceStardas,
        ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.dimensioneMassima,
        ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosPadre,
        ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(
        CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosAllegato, ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRTipodocTipopraticas,
        ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTDocumentos,
        ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.descrizione,
        ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.firmabile, ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRFormatoFileTipoDocumentos,
        ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTTemplateFeas,
        ExportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoDocumento_.class, cosmoDTipoDocumentoPolicy);


    var cosmoRTipodocTipopraticasPKPolicy = new ExportPolicy<CosmoRTipodocTipopraticaPK>();
    cosmoRTipodocTipopraticasPKPolicy.fieldsPolicy
    .put(CosmoRTipodocTipopraticaPK_.codiceTipoDocumento, ExportPolicy.copy());
    cosmoRTipodocTipopraticasPKPolicy.fieldsPolicy
    .put(CosmoRTipodocTipopraticaPK_.codiceTipoPratica, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipodocTipopraticaPK_.class, cosmoRTipodocTipopraticasPKPolicy);

    var cosmoRTipodocTipopraticasPolicy = new ExportPolicy<CosmoRTipodocTipopratica>();
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.id,
        ExportPolicy.clone(cosmoRTipodocTipopraticasPKPolicy));
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.cosmoDTipoDocumento,
        ExportPolicy.clone(cosmoDTipoDocumentoPolicy));
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.cosmoDTipoPratica,
        ExportPolicy.ignore());
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal, ExportPolicy.copy());
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipodocTipopratica_.class, cosmoRTipodocTipopraticasPolicy);

    return cosmoRTipodocTipopraticasPolicy;
  }

  private ExportPolicy<CosmoRTabDettaglioTipoPratica> buildPolicyForTabDettaglioExport() {

    var cosmoDTabDettaglioPolicy = new ExportPolicy<CosmoDTabDettaglio>();
    cosmoDTabDettaglioPolicy.fieldsPolicy.put(CosmoDTabDettaglio_.codice, ExportPolicy.copy());
    cosmoDTabDettaglioPolicy.fieldsPolicy.put(CosmoDTabDettaglio_.descrizione, ExportPolicy.copy());
    cosmoDTabDettaglioPolicy.fieldsPolicy.put(CosmoDTabDettaglio_.cosmoRTabDettaglioTipoPraticas,
        ExportPolicy.ignore());
    cosmoDTabDettaglioPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    cosmoDTabDettaglioPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoDTabDettaglio_.class, cosmoDTabDettaglioPolicy);

    var cosmoRTabDettaglioTipoPraticaPKPolicy = new ExportPolicy<CosmoRTabDettaglioTipoPraticaPK>();
    cosmoRTabDettaglioTipoPraticaPKPolicy.fieldsPolicy
    .put(CosmoRTabDettaglioTipoPraticaPK_.codiceTabDettaglio, ExportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPKPolicy.fieldsPolicy
    .put(CosmoRTabDettaglioTipoPraticaPK_.codiceTipoPratica, ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTabDettaglioTipoPraticaPK_.class,
        cosmoRTabDettaglioTipoPraticaPKPolicy);

    var cosmoRTabDettaglioTipoPraticaPolicy = new ExportPolicy<CosmoRTabDettaglioTipoPratica>();
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoRTabDettaglioTipoPratica_.id,
        ExportPolicy.clone(cosmoRTabDettaglioTipoPraticaPKPolicy));
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(
        CosmoRTabDettaglioTipoPratica_.cosmoDTabDettaglio,
        ExportPolicy.clone(cosmoDTabDettaglioPolicy));
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy
    .put(CosmoRTabDettaglioTipoPratica_.cosmoDTipoPratica, ExportPolicy.ignore());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoRTabDettaglioTipoPratica_.ordine,
        ExportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ExportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ExportPolicy.copy());
    checkPolicyIsComplete(CosmoRTabDettaglioTipoPratica_.class,
        cosmoRTabDettaglioTipoPraticaPolicy);

    return cosmoRTabDettaglioTipoPraticaPolicy;
  }

  public ExportPolicy<CosmoDCustomFormFormio> buildPolicyForCustomFormExport() {

    var exportSpecForTipoCustomForm = new ExportPolicy<CosmoDCustomFormFormio>();

    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.codice,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.descrizione,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.customForm,
        ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ExportPolicy.copy());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ExportPolicy.ignore());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoDTipoPratica,
        ExportPolicy.ignore());
    exportSpecForTipoCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoTHelpers, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoDCustomFormFormio_.class, exportSpecForTipoCustomForm);

    exportSpecForTipoCustomForm =
        exportSpecForTipoCustomForm.withPreExport((entity, map) -> map.put("codiceTipoPratica",
            entity.getCosmoDTipoPratica() != null ? entity.getCosmoDTipoPratica().getCodice()
                : null));

    return exportSpecForTipoCustomForm;
  }

  public ExportPolicy<CosmoTTemplateReport> buildPolicyForTemplateReportExport() {

    var exportSpec = new ExportPolicy<CosmoTTemplateReport>();

    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.id, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.codice, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.codiceTemplatePadre, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.codiceTipoPratica, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.idEnte, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.sorgenteTemplate, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.templateCompilato, ExportPolicy.copy());

    // TODO - in fase di import va fixato a ente selezionato se idEnte != NULL
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.ente, ExportPolicy.ignore());
    // TODO - in fase di import va fixato a tipo pratica selezionata se codiceTipoPratica != NULL
    exportSpec.fieldsPolicy.put(CosmoTTemplateReport_.tipoPratica, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoTTemplateReport_.class, exportSpec);

    return exportSpec;
  }

  public ExportPolicy<CosmoTRisorsaTemplateReport> buildPolicyForRisorsaTemplateReportExport() {

    var exportSpec = new ExportPolicy<CosmoTRisorsaTemplateReport>();

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.id, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codice, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codiceTemplate, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codiceTipoPratica,
        ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.idEnte, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.contenutoRisorsa, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.nodoRisorsa, ExportPolicy.copy());

    // TODO - in fase di import va fixato a ente selezionato se idEnte != NULL
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.ente, ExportPolicy.ignore());
    // TODO - in fase di import va fixato a tipo pratica selezionata se codiceTipoPratica != NULL
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.tipoPratica, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoTRisorsaTemplateReport_.class, exportSpec);

    return exportSpec;
  }

  public ExportPolicy<CosmoTUtenteGruppo> buildPolicyForTUtenteGruppoExport() {

    var exportSpec = new ExportPolicy<CosmoTUtenteGruppo>();

    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.id, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.idUtente, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.idGruppo, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.gruppo, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.utente, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.cosmoRUtenteGruppoTags, ExportPolicy.ignore());
    // exportSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.cosmoRUtenteGruppoTags,
    // ExportPolicy.clone(buildPolicyForRUtenteGruppoTagExport()));

    checkPolicyIsComplete(CosmoTUtenteGruppo_.class, exportSpec);

    exportSpec = exportSpec.withPreExport(
        (entity, map) -> map.put(CODICE_FISCALE_UTENTE, entity.getUtente().getCodiceFiscale()));

    return exportSpec;
  }

  private ExportPolicy<CosmoRUtenteGruppoTag> buildPolicyForRUtenteGruppoTagExport() {

    var exportSpec = new ExportPolicy<CosmoRUtenteGruppoTag>();

    exportSpec.fieldsPolicy.put(CosmoREntity_.dtInizioVal, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoREntity_.dtFineVal, ExportPolicy.copy());

    exportSpec.fieldsPolicy.put(CosmoRUtenteGruppoTag_.id, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoRUtenteGruppoTag_.cosmoTTag,
        ExportPolicy.clone(buildPolicyForTTagExport()));
    exportSpec.fieldsPolicy.put(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo, ExportPolicy.ignore());

    checkPolicyIsComplete(CosmoRUtenteGruppoTag_.class, exportSpec);

    return exportSpec;
  }

  public ExportPolicy<CosmoRGruppoTipoPratica> buildPolicyForRGruppoTipoPraticaExport() {

    var exportSpec = new ExportPolicy<CosmoRGruppoTipoPratica>();

    exportSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.id, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoREntity_.dtFineVal, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoREntity_.dtInizioVal, ExportPolicy.copy());

    exportSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.cosmoTGruppo, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.cosmoDTipoPratica, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.creatore, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.supervisore, ExportPolicy.copy());

    checkPolicyIsComplete(CosmoRGruppoTipoPratica_.class, exportSpec);

    exportSpec = exportSpec.withPreExport(
        (entity, map) -> map.put("codiceTipoPratica", entity.getCosmoDTipoPratica().getCodice()));

    return exportSpec;
  }

  public ExportPolicy<CosmoTGruppo> buildPolicyForGruppoExport() {


    var exportSpec = new ExportPolicy<CosmoTGruppo>();

    exportSpec.fieldsPolicy.put(CosmoTGruppo_.id, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTGruppo_.codice, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTGruppo_.descrizione, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTGruppo_.ente, ExportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTGruppo_.nome, ExportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTGruppo_.cosmoRPraticaUtenteGruppos, ExportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTGruppo_.associazioniUtenti,
        ExportPolicy.clone(buildPolicyForTUtenteGruppoExport()));

    exportSpec.fieldsPolicy.put(CosmoTGruppo_.cosmoRGruppoTipoPraticas,
        ExportPolicy.clone(buildPolicyForRGruppoTipoPraticaExport()));

    checkPolicyIsComplete(CosmoTGruppo_.class, exportSpec);

    exportSpec = exportSpec.withPreExport((entity, exported) -> exported.put("_policy", 1));

    return exportSpec;
  }

  public ImportPolicy<CosmoTTemplateReport> buildPolicyForTemplateReportImport(
      ImportContext importContext) {

    var importSpec = new ImportPolicy<CosmoTTemplateReport>();

    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.id, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.codice, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.codiceTemplatePadre, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.sorgenteTemplate, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.templateCompilato, ImportPolicy.copy());

    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.codiceTipoPratica, ImportPolicy.copy());

    importSpec = importSpec.withPrePersistExecution(ctx -> {
      var target = (CosmoTTemplateReport) ctx.target;
      if (target.getIdEnte() != null) {
        target.setEnte(cosmoTEnteRepository
            .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElse(null));
      }
    });

    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.idEnte, ImportPolicy.builder(ctx -> {
      var inputField = ctx.source.get(ID_ENTE);
      if (inputField != null && !inputField.isNull()) {
        return cosmoTEnteRepository
            .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId)
            .map(CosmoTEnte::getId).orElseThrow();
      }
      return null;
    }));

    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.ente, ImportPolicy.ignore());

    // in fase di import va fixato a tipo pratica selezionata se codiceTipoPratica != NULL
    importSpec.fieldsPolicy.put(CosmoTTemplateReport_.tipoPratica, ImportPolicy.builder(ctx -> {
      var inputField = ctx.source.get("codiceTipoPratica");
      if (inputField != null && !inputField.isNull()) {
        return cosmoDTipoPraticaRepository
            .findOneByField(CosmoDTipoPratica_.codice, inputField.asText()).orElseThrow();
      }
      return null;
    }));

    importSpec = importSpec.withConflictChecker(ctx -> {

      var target = (CosmoTTemplateReport) ctx.target;

      var found = cosmoTTemplateReportRepository.findAllNotDeleted((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(root.get(CosmoTTemplateReport_.codice), target.getCodice()),
          (target.getIdEnte() == null ?
              cb.isNull(root.get(CosmoTTemplateReport_.idEnte))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTTemplateReport_.idEnte)),
                    cb.equal(root.get(CosmoTTemplateReport_.idEnte), target.getIdEnte())
                    )
              ),
          (target.getTipoPratica() == null ?
              cb.isNull(root.get(CosmoTTemplateReport_.codiceTipoPratica))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTTemplateReport_.codiceTipoPratica)),
                    cb.equal(root.get(CosmoTTemplateReport_.codiceTipoPratica), target.getTipoPratica().getCodice())
                    )
              )
          )
      //@formatter:on
          );
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    });

    importSpec = importSpec.withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTTemplateReport) ctx.target;

      String out = "[";

      out += ((target.getEnte() != null) ? "E" : "*") + ",";
      out += ((target.getTipoPratica() != null) ? target.getTipoPratica().getCodice() : "*") + ",";
      out += target.getCodice();

      out += "]";
      return out;
    });

    checkPolicyIsComplete(CosmoTTemplateReport_.class, importSpec);

    return importSpec;
  }

  public ImportPolicy<CosmoTRisorsaTemplateReport> buildPolicyForRisorsaTemplateReportImport(
      ImportContext importContext) {

    var exportSpec = new ImportPolicy<CosmoTRisorsaTemplateReport>();

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.id, ImportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    exportSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codice, ImportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codiceTemplate, ImportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.contenutoRisorsa, ImportPolicy.copy());
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.nodoRisorsa, ImportPolicy.copy());

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.codiceTipoPratica,
        ImportPolicy.copy());

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.idEnte, ImportPolicy.builder(ctx -> {
      var inputField = ctx.source.get(ID_ENTE);
      if (inputField != null && !inputField.isNull()) {
        return cosmoTEnteRepository
            .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId)
            .map(CosmoTEnte::getId).orElseThrow();
      }
      return null;
    }));

    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.ente, ImportPolicy.ignore());

    // in fase di import va fixato a tipo pratica selezionata se codiceTipoPratica != NULL
    exportSpec.fieldsPolicy.put(CosmoTRisorsaTemplateReport_.tipoPratica,
        ImportPolicy.builder(ctx -> {
          var inputField = ctx.source.get("codiceTipoPratica");
          if (inputField != null && !inputField.isNull()) {
            return cosmoDTipoPraticaRepository
                .findOneActiveByField(CosmoDTipoPratica_.codice, inputField.asText()).orElseThrow();
          }
          return null;
        }));

    exportSpec = exportSpec.withConflictChecker(ctx -> {
      var target = (CosmoTRisorsaTemplateReport) ctx.target;

      var found = cosmoTRisorsaTemplateReportRepository.findAllNotDeleted((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codice), target.getCodice()),
          (target.getEnte() == null ?
              cb.isNull(root.get(CosmoTRisorsaTemplateReport_.idEnte))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTRisorsaTemplateReport_.idEnte)),
                    cb.equal(root.get(CosmoTRisorsaTemplateReport_.idEnte), target.getEnte().getId())
                    )
              ),
          (target.getTipoPratica() == null ?
              cb.isNull(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica)),
                    cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica), target.getTipoPratica().getCodice())
                    )
              ),
          (StringUtils.isBlank(target.getCodiceTemplate()) ?
              cb.isNull(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate)),
                    cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate), target.getCodiceTemplate())
                    )
              )
          )
      //@formatter:on
          );
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    });

    exportSpec = exportSpec.withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTRisorsaTemplateReport) ctx.target;

      String out = "[";

      out += ((target.getEnte() != null) ? "E" : "*") + ",";
      out += ((target.getTipoPratica() != null) ? target.getTipoPratica().getCodice() : "*") + ",";
      out += (!StringUtils.isBlank(target.getCodiceTemplate()) ? target.getCodiceTemplate() : "*")
          + ",";
      out += target.getCodice();

      out += "]";
      return out;
    });

    checkPolicyIsComplete(CosmoTRisorsaTemplateReport_.class, exportSpec);

    return exportSpec;
  }

  public ImportPolicy<CosmoTFormLogico> buildPolicyForFormLogicoImport(
      ImportContext importContext) {

    var cosmoRFunzionalitaParametroFormLogicoPolicy =
        new ImportPolicy<CosmoRFunzionalitaParametroFormLogico>();
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.id, ImportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.codiceFunzionalita, ImportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.codiceParametro, ImportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.funzionalita, ImportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.obbligatorio, ImportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy
    .put(CosmoRFunzionalitaParametroFormLogico_.parametro, ImportPolicy.ignore());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ImportPolicy.copy());
    cosmoRFunzionalitaParametroFormLogicoPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());

    cosmoRFunzionalitaParametroFormLogicoPolicy =
        cosmoRFunzionalitaParametroFormLogicoPolicy.withPrePersistRequest(ctx -> {
          var entity = (CosmoRFunzionalitaParametroFormLogico) ctx.target;

          if (entity.getFunzionalita() == null && entity.getCodiceFunzionalita() != null) {
            entity.setFunzionalita(cosmoDFunzionalitaFormLogicoRepository
                .findOneActive(entity.getCodiceFunzionalita()).orElse(null));
          }

          if (entity.getParametro() == null && entity.getCodiceParametro() != null) {
            entity.setParametro(cosmoDChiaveParametroFunzionalitaFormLogicoRepository
                .findOneActive(entity.getCodiceParametro()).orElse(null));
          }
        });

    cosmoRFunzionalitaParametroFormLogicoPolicy =
        cosmoRFunzionalitaParametroFormLogicoPolicy.withConflictChecker(ctx -> {
          var target = (CosmoRFunzionalitaParametroFormLogico) ctx.target;

          var results = cosmoRFunzionalitaParametroFormLogicoRepository
              .findAllActive((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoREntity_.dtFineVal)),
                  cb.equal(root.get(CosmoRFunzionalitaParametroFormLogico_.codiceFunzionalita),
                      target.getCodiceFunzionalita()),
                  cb.equal(root.get(CosmoRFunzionalitaParametroFormLogico_.codiceParametro),
                      target.getCodiceParametro())));
          if (results.size() > 1) {
            throw new InternalServerException("too many candidates");
          } else if (!results.isEmpty()) {
            return results.get(0);
          }
          return null;
        }).withUniqueIdentifierProvider(ctx -> {
          var target = (CosmoRFunzionalitaParametroFormLogico) ctx.target;

          return "[" + Arrays.asList(target.getCodiceFunzionalita(), target.getCodiceParametro())
          .stream().collect(Collectors.joining(",")) + "]";
        });

    checkPolicyIsComplete(CosmoRFunzionalitaParametroFormLogico_.class,
        cosmoRFunzionalitaParametroFormLogicoPolicy);

    var cosmoDFunzionalitaFormLogicoPolicy = new ImportPolicy<CosmoDFunzionalitaFormLogico>();
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.codice,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.descrizione,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.handler,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDFunzionalitaFormLogico_.multiIstanza,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDFunzionalitaFormLogico_.eseguibileMassivamente, ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ImportPolicy.copy());
    cosmoDFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoDFunzionalitaFormLogico_.associazioniParametri,
        ImportPolicy.clone(cosmoRFunzionalitaParametroFormLogicoPolicy));
    checkPolicyIsComplete(CosmoDFunzionalitaFormLogico_.class, cosmoDFunzionalitaFormLogicoPolicy);

    var cosmoDChiaveParametroFunzionalitaFormLogicoPolicy =
        new ImportPolicy<CosmoDChiaveParametroFunzionalitaFormLogico>();
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.codice, ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.descrizione, ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.schema, ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.tipo, ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoDChiaveParametroFunzionalitaFormLogico_.valoreDefault, ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ImportPolicy.copy());
    cosmoDChiaveParametroFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDChiaveParametroFunzionalitaFormLogico_.class,
        cosmoDChiaveParametroFunzionalitaFormLogicoPolicy);

    var cosmoRIstanzaFormLogicoParametroValoresPolicy =
        new ImportPolicy<CosmoRIstanzaFormLogicoParametroValore>();

    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(
        CosmoRIstanzaFormLogicoParametroValore_.id,
        ImportPolicy.<CosmoRIstanzaFormLogicoParametroValorePK>builder(ctx -> {
          var parentTarget = (CosmoTIstanzaFunzionalitaFormLogico) ctx.parentTarget;
          var targetRel = (CosmoRIstanzaFormLogicoParametroValore) ctx.target;
          var pk = new CosmoRIstanzaFormLogicoParametroValorePK();
          pk.setCodiceChiaveParametro(
              targetRel.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice());
          pk.setIdIstanza(parentTarget.getId());
          return pk;
        }).withOrder(1100));

    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(
        CosmoRIstanzaFormLogicoParametroValore_.cosmoTIstanzaFunzionalitaFormLogico,
        ImportPolicy.ignore());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy
    .put(CosmoRIstanzaFormLogicoParametroValore_.valoreParametro, ImportPolicy.copy());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ImportPolicy.copy());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());
    cosmoRIstanzaFormLogicoParametroValoresPolicy.fieldsPolicy.put(
        CosmoRIstanzaFormLogicoParametroValore_.cosmoDChiaveParametroFunzionalitaFormLogico,
        ImportPolicy.clone(cosmoDChiaveParametroFunzionalitaFormLogicoPolicy));

    cosmoRIstanzaFormLogicoParametroValoresPolicy =
        cosmoRIstanzaFormLogicoParametroValoresPolicy.withUniqueIdentifierProvider(ctx -> {
          var parentTarget = (CosmoTIstanzaFunzionalitaFormLogico) ctx.breadcrumb.getLast();
          var targetRel = (CosmoRIstanzaFormLogicoParametroValore) ctx.target;
          return "[inst:"
          + (parentTarget.getCosmoDFunzionalitaFormLogico() != null
          ? parentTarget.getCosmoDFunzionalitaFormLogico().getCodice()
              : "")
          + ","
          + (targetRel.getCosmoDChiaveParametroFunzionalitaFormLogico() != null
          ? targetRel.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice()
              : "")
          + "]";
        });

    checkPolicyIsComplete(CosmoRIstanzaFormLogicoParametroValore_.class,
        cosmoRIstanzaFormLogicoParametroValoresPolicy);

    var cosmoTIstanzaFunzionalitaFormLogicoPolicy =
        new ImportPolicy<CosmoTIstanzaFunzionalitaFormLogico>();
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoRFormLogicoIstanzaFunzionalitas,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoTIstanzaFunzionalitaFormLogico_.descrizione, ImportPolicy.copy());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy
    .put(CosmoTIstanzaFunzionalitaFormLogico_.id, ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica,
        ImportPolicy.ignore());
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoDFunzionalitaFormLogico,
        ImportPolicy.clone(cosmoDFunzionalitaFormLogicoPolicy));
    cosmoTIstanzaFunzionalitaFormLogicoPolicy.fieldsPolicy.put(
        CosmoTIstanzaFunzionalitaFormLogico_.cosmoRIstanzaFormLogicoParametroValores,
        ImportPolicy.clone(cosmoRIstanzaFormLogicoParametroValoresPolicy).withOrder(20000));

    cosmoTIstanzaFunzionalitaFormLogicoPolicy =
        cosmoTIstanzaFunzionalitaFormLogicoPolicy.withConflictChecker(ctx -> {
          // expect breadcrumb to have a parent CosmoRFormLogicoIstanzaFunzionalita
          // and a CosmoTFormLogico as second from last
          if (ctx.breadcrumb.size() == 2 && ctx.breadcrumb.get(0) instanceof CosmoTFormLogico
              && ctx.breadcrumb.get(1) instanceof CosmoRFormLogicoIstanzaFunzionalita) {

            var target = (CosmoTIstanzaFunzionalitaFormLogico) ctx.target;
            var formLogico = (CosmoTFormLogico) ctx.breadcrumb.get(0);

            var matching =
                cosmoRFormLogicoIstanzaFunzionalitaRepository.findAllActive((root, cq, cb) -> {
                  //@formatter:off
                  var joinFormLogico = root.join(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTFormLogico);
                  var joinIstanza = root.join(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTIstanzaFunzionalitaFormLogico);
                  var joinFunzionalita = joinIstanza.join(CosmoTIstanzaFunzionalitaFormLogico_.cosmoDFunzionalitaFormLogico);
                  return cb.and(
                      cb.isNull(root.get(CosmoREntity_.dtFineVal)),
                      cb.isNull(root.get(CosmoREntity_.dtFineVal)),
                      cb.isNull(joinFormLogico.get(CosmoTEntity_.dtCancellazione)),
                      cb.isNull(joinIstanza.get(CosmoTEntity_.dtCancellazione)),
                      cb.equal(joinFormLogico.get(CosmoTFormLogico_.id), formLogico.getId()),
                      cb.isNull(joinFunzionalita.get(CosmoDEntity_.dtFineVal)),
                      cb.equal(joinFunzionalita.get(CosmoDFunzionalitaFormLogico_.codice), target.getCosmoDFunzionalitaFormLogico().getCodice())
                      );
                  //@formatter:on
                });

            if (matching.isEmpty()) {
              return null;
            } else if (matching.size() > 1) {
              throw new InternalServerException("Multiple instances already associated");
            }
            return matching.get(0).getCosmoTIstanzaFunzionalitaFormLogico();
          }

          return null;
        }).withUniqueIdentifierProvider(ctx -> {
          var target = (CosmoTIstanzaFunzionalitaFormLogico) ctx.target;
          return "[inst:" + (target.getCosmoDFunzionalitaFormLogico() != null
              ? target.getCosmoDFunzionalitaFormLogico().getCodice()
                  : "") + "]";
        });

    checkPolicyIsComplete(CosmoTIstanzaFunzionalitaFormLogico_.class,
        cosmoTIstanzaFunzionalitaFormLogicoPolicy);

    var cosmoRFormLogicoIstanzaFunzionalitaPolicy =
        new ImportPolicy<CosmoRFormLogicoIstanzaFunzionalita>();
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTFormLogico, ImportPolicy.ignore());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.ordine, ImportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy
    .put(CosmoRFormLogicoIstanzaFunzionalita_.eseguibileMassivamente, ImportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());
    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ImportPolicy.copy());

    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(
        CosmoRFormLogicoIstanzaFunzionalita_.cosmoTIstanzaFunzionalitaFormLogico,
        ImportPolicy.clone(cosmoTIstanzaFunzionalitaFormLogicoPolicy));

    cosmoRFormLogicoIstanzaFunzionalitaPolicy.fieldsPolicy.put(
        CosmoRFormLogicoIstanzaFunzionalita_.id,
        ImportPolicy.<CosmoRFormLogicoIstanzaFunzionalitaPK>builder(ctx -> {
          var parentTarget = (CosmoTFormLogico) ctx.parentTarget;
          var targetRel = (CosmoRFormLogicoIstanzaFunzionalita) ctx.target;
          var pk = new CosmoRFormLogicoIstanzaFunzionalitaPK();
          pk.setIdFormLogico(parentTarget.getId());
          pk.setIdIstanzaFunzionalita(targetRel.getCosmoTIstanzaFunzionalitaFormLogico().getId());
          return pk;
        }).withOrder(1100));

    cosmoRFormLogicoIstanzaFunzionalitaPolicy =
        cosmoRFormLogicoIstanzaFunzionalitaPolicy.withUniqueIdentifierProvider(ctx -> {
          var target = (CosmoRFormLogicoIstanzaFunzionalita) ctx.target;
          return "[ROOT," + (target.getCosmoTIstanzaFunzionalitaFormLogico() != null && target
              .getCosmoTIstanzaFunzionalitaFormLogico().getCosmoDFunzionalitaFormLogico() != null
              ? target.getCosmoTIstanzaFunzionalitaFormLogico()
                  .getCosmoDFunzionalitaFormLogico().getCodice()
                  : "")
              + "]";
        });

    checkPolicyIsComplete(CosmoRFormLogicoIstanzaFunzionalita_.class,
        cosmoRFormLogicoIstanzaFunzionalitaPolicy);

    var formLogicoPolicy = new ImportPolicy<CosmoTFormLogico>();
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.codice, ImportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.descrizione, ImportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.eseguibileMassivamente,
        ImportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.id, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.wizard, ImportPolicy.copy());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoTEnte, ImportPolicy.ignore());

    formLogicoPolicy = formLogicoPolicy.withPrePersistExecution(ctx -> {
      var target = (CosmoTFormLogico) ctx.target;
      var enteForm = ctx.input.get("enteForm");
      if (enteForm != null) {
        target.setCosmoTEnte(cosmoTEnteRepository
            .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElse(null));
      }
    });


    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoTAttivitas, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());
    formLogicoPolicy.fieldsPolicy.put(CosmoTFormLogico_.cosmoRFormLogicoIstanzaFunzionalitas,
        ImportPolicy.clone(cosmoRFormLogicoIstanzaFunzionalitaPolicy).withOrder(20000));


    formLogicoPolicy = formLogicoPolicy.withConflictChecker(ctx -> {
      var target = (CosmoTFormLogico) ctx.target;
      var enteForm = ctx.input.get("enteForm");
      return formLogicoRepository
          .findNotDeletedByField(CosmoTFormLogico_.codice, target.getCodice()).stream()
          .filter(x -> (x.getCosmoTEnte() == null && enteForm == null)
              || (x.getCosmoTEnte() != null && enteForm != null
              && x.getCosmoTEnte().getCodiceIpa().equals(importContext.tenantId)))
          .findFirst().orElse(null);
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTFormLogico) ctx.target;
      return target.getCodice();
    });


    checkPolicyIsComplete(CosmoTFormLogico_.class, formLogicoPolicy);

    return formLogicoPolicy;
  }

  public ImportPolicy<CosmoTVariabileProcesso> buildPolicyForVariabileProcessoImport() {

    var importSpecForDFiltroCampo = new ImportPolicy<CosmoDFiltroCampo>();
    importSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.codice, ImportPolicy.copy());
    importSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.cosmoTVariabileProcessos,
        ImportPolicy.ignore());
    importSpecForDFiltroCampo.fieldsPolicy.put(CosmoDFiltroCampo_.descrizione, ImportPolicy.copy());
    importSpecForDFiltroCampo.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.ignore());
    importSpecForDFiltroCampo.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());

    importSpecForDFiltroCampo = importSpecForDFiltroCampo.withConflictChecker(ctx -> {
      var target = (CosmoDFiltroCampo) ctx.target;
      var found = cosmoDFiltroCampoRepository.findAllActive((root, cq, cb) -> {
        //@formatter:off
        return cb.and(
            cb.isNull(root.get(CosmoDEntity_.dtFineVal)),
            cb.equal(root.get(CosmoDFiltroCampo_.codice), target.getCodice())
            );
        //@formatter:on
      });
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoDFiltroCampo) ctx.target;
      return "[ROOT," + target.getCodice() + "]";

    });

    checkPolicyIsComplete(CosmoDFiltroCampo_.class, importSpecForDFiltroCampo);

    var importSpecForDFormatoCampo = new ImportPolicy<CosmoDFormatoCampo>();

    importSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.codice, ImportPolicy.copy());
    importSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.cosmoTVariabileProcessos,
        ImportPolicy.ignore());
    importSpecForDFormatoCampo.fieldsPolicy.put(CosmoDFormatoCampo_.descrizione,
        ImportPolicy.copy());
    importSpecForDFormatoCampo.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.ignore());
    importSpecForDFormatoCampo.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());

    importSpecForDFormatoCampo = importSpecForDFormatoCampo.withConflictChecker(ctx -> {
      var target = (CosmoDFormatoCampo) ctx.target;
      var found = cosmoDFormatoCampoRepository.findAllActive((root, cq, cb) -> {
        //@formatter:off
        return cb.and(
            cb.isNull(root.get(CosmoDEntity_.dtFineVal)),
            cb.equal(root.get(CosmoDFormatoCampo_.codice), target.getCodice())
            );
        //@formatter:on
      });
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoDFormatoCampo) ctx.target;
      return "[ROOT," + target.getCodice() + "]";

    });

    checkPolicyIsComplete(CosmoDFormatoCampo_.class, importSpecForDFormatoCampo);

    var importSpecForTVariabileProcesso = new ImportPolicy<CosmoTVariabileProcesso>();

    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.filtroCampo,
        ImportPolicy.clone(importSpecForDFiltroCampo));
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.formatoCampo,
        ImportPolicy.clone(importSpecForDFormatoCampo));
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.id,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.nomeVariabile,
        ImportPolicy.copy());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.nomeVariabileFlowable,
        ImportPolicy.copy());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.visualizzareInTabella,
        ImportPolicy.copy());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtCancellazione,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtInserimento,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteInserimento,
        ImportPolicy.ignore());
    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica,
        ImportPolicy.ignore());


    importSpecForTVariabileProcesso.fieldsPolicy.put(CosmoTVariabileProcesso_.tipoPratica,
        ImportPolicy.builder(ctx -> {
          var inputField = ctx.source.get("codiceTipoPratica");
          if (inputField != null && !inputField.isNull()) {
            return cosmoDTipoPraticaRepository
                .findOneByField(CosmoDTipoPratica_.codice, inputField.asText()).orElseThrow();
          }
          return null;
        }));

    importSpecForTVariabileProcesso = importSpecForTVariabileProcesso.withConflictChecker(ctx -> {
      var target = (CosmoTVariabileProcesso) ctx.target;

      var found = cosmoTVariabileProcessoRepository.findAllNotDeleted((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(root.get(CosmoTVariabileProcesso_.nomeVariabile), target.getNomeVariabile()),
          cb.equal(root.get(CosmoTVariabileProcesso_.nomeVariabileFlowable), target.getNomeVariabileFlowable()),
          (target.getTipoPratica() == null ?
              cb.isNull(root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.codice))
              :
                cb.and(
                    cb.isNotNull(root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.codice)),
                    cb.equal(root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.codice), target.getTipoPratica().getCodice())
                    )
              ))
      //@formatter:on
          );
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    });

    importSpecForTVariabileProcesso =
        importSpecForTVariabileProcesso.withUniqueIdentifierProvider(ctx -> {
          var target = (CosmoTVariabileProcesso) ctx.target;

          String out = "[";

          out +=
              ((target.getTipoPratica() != null) ? target.getTipoPratica().getCodice() : "*") + ",";
          out += (!StringUtils.isBlank(target.getNomeVariabile()) ? target.getNomeVariabile() : "*")
              + ",";
          out += (!StringUtils.isBlank(target.getNomeVariabileFlowable())
              ? target.getNomeVariabileFlowable()
                  : "*");
          out += "]";
          return out;
        });

    checkPolicyIsComplete(CosmoTVariabileProcesso_.class, importSpecForTVariabileProcesso);

    return importSpecForTVariabileProcesso;

  }

  public ImportPolicy<CosmoDTipoPratica> buildPolicyForTipoPraticaImport(
      ImportContext importContext) {

    var exportSpecForConfigMetadati = new ImportPolicy<CosmoCConfigurazioneMetadati>();
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.chiave,
        ImportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.descrizione,
        ImportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.valore,
        ImportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCEntity_.dtInizioVal, ImportPolicy.copy());
    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCEntity_.dtFineVal, ImportPolicy.copy());

    exportSpecForConfigMetadati.fieldsPolicy.put(CosmoCConfigurazioneMetadati_.cosmoDTipoPratica,
        ImportPolicy.ignore());

    exportSpecForConfigMetadati = exportSpecForConfigMetadati.withConflictChecker(ctx -> {
      var tipoPratica = (CosmoDTipoPratica) ctx.breadcrumb.get(0);
      var target = (CosmoCConfigurazioneMetadati) ctx.target;
      var found = cosmoCConfigurazioneMetadatiRepository.findAllActive((root, cq, cb) -> {
        var joinTipoPratica = root.join(CosmoCConfigurazioneMetadati_.cosmoDTipoPratica);
        //@formatter:off
        return cb.and(
            cb.isNull(root.get(CosmoCEntity_.dtFineVal)),
            cb.isNull(joinTipoPratica.get(CosmoDEntity_.dtFineVal)),
            cb.equal(root.get(CosmoCConfigurazioneMetadati_.chiave), target.getChiave()),
            cb.equal(joinTipoPratica.get(CosmoDTipoPratica_.codice), tipoPratica.getCodice())
            );
        //@formatter:on
      });
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoCConfigurazioneMetadati) ctx.target;
      return "[ROOT," + target.getChiave() + "]";

    }).withPrePersistExecution(ctx -> {
      var tipoPratica = (CosmoDTipoPratica) ctx.breadcrumb.get(0);
      var entity = (CosmoCConfigurazioneMetadati) ctx.target;
      entity.setCosmoDTipoPratica(tipoPratica);
    });

    checkPolicyIsComplete(CosmoCConfigurazioneMetadati_.class, exportSpecForConfigMetadati);

    var cosmoDStatPraticasPolicy = new ImportPolicy<CosmoDStatoPratica>();
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.classe, ImportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.codice, ImportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.cosmoRStatoTipoPraticas,
        ImportPolicy.ignore());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDStatoPratica_.descrizione, ImportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    cosmoDStatPraticasPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDStatoPratica_.class, cosmoDStatPraticasPolicy);

    var cosmoRStatoTipoPraticasPolicy = new ImportPolicy<CosmoRStatoTipoPratica>();
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.id,
        ImportPolicy.ignore());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal, ImportPolicy.copy());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal, ImportPolicy.copy());
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.cosmoDStatoPratica,
        ImportPolicy.clone(cosmoDStatPraticasPolicy));
    cosmoRStatoTipoPraticasPolicy.fieldsPolicy.put(CosmoRStatoTipoPratica_.cosmoDTipoPratica,
        ImportPolicy.builder(ctx -> {
          var parentTarget = (CosmoDTipoPratica) ctx.parentTarget;
          return cosmoDTipoPraticaRepository.findOne(parentTarget.getCodice());
        }));

    cosmoRStatoTipoPraticasPolicy = cosmoRStatoTipoPraticasPolicy.withConflictChecker(ctx -> {
      var target = (CosmoRStatoTipoPratica) ctx.target;

      var results = cosmoRStatoTipoPraticaRepository
          .findAllActive((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoREntity_.dtFineVal)),
              cb.equal(root.get(CosmoRStatoTipoPratica_.cosmoDStatoPratica)
                  .get(CosmoDStatoPratica_.codice), target.getCosmoDStatoPratica().getCodice()),
              cb.equal(root.get(CosmoRStatoTipoPratica_.cosmoDTipoPratica)
                  .get(CosmoDTipoPratica_.codice), target.getCosmoDTipoPratica().getCodice())));
      if (!results.isEmpty()) {
        return results.get(0);
      }
      return null;
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoRStatoTipoPratica) ctx.target;

      return "[" + Arrays.asList(
          (target.getCosmoDStatoPratica() != null ? target.getCosmoDStatoPratica().getCodice()
              : ""),
          (target.getCosmoDTipoPratica() != null ? target.getCosmoDTipoPratica().getCodice() : ""))
      .stream().collect(Collectors.joining(",")) + "]";
    });

    checkPolicyIsComplete(CosmoRStatoTipoPratica_.class, cosmoRStatoTipoPraticasPolicy);

    var cosmoRTipoDocumentoTipoDocumentosPKPolicy =
        new ImportPolicy<CosmoRTipoDocumentoTipoDocumentoPK>();
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codiceAllegato, ImportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codicePadre, ImportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPKPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumentoPK_.codiceTipoPratica, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipoDocumentoTipoDocumentoPK_.class,
        cosmoRTipoDocumentoTipoDocumentosPKPolicy);

    var cosmoRTipoDocumentoTipoDocumentosPolicy =
        new ImportPolicy<CosmoRTipoDocumentoTipoDocumento>();
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoRTipoDocumentoTipoDocumento_.id,
        ImportPolicy.clone(cosmoRTipoDocumentoTipoDocumentosPKPolicy).withOrder(3000));
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.codiceStardasAllegato, ImportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica, ImportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato, ImportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy
    .put(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre, ImportPolicy.ignore());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());
    cosmoRTipoDocumentoTipoDocumentosPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ImportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipoDocumentoTipoDocumento_.class,
        cosmoRTipoDocumentoTipoDocumentosPolicy);

    var cosmoDTipoDocumentoPolicy = new ImportPolicy<CosmoDTipoDocumento>();
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codice, ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codiceStardas,
        ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.dimensioneMassima,
        ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosPadre, ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(
        CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosAllegato,
        ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRTipodocTipopraticas,
        ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTDocumentos,
        ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.descrizione,
        ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.firmabile, ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRFormatoFileTipoDocumentos,
        ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTTemplateFeas,
        ImportPolicy.ignore());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    cosmoDTipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoDocumento_.class, cosmoDTipoDocumentoPolicy);

    var cosmoRTipodocTipopraticasPKPolicy = new ImportPolicy<CosmoRTipodocTipopraticaPK>();
    cosmoRTipodocTipopraticasPKPolicy.fieldsPolicy
    .put(CosmoRTipodocTipopraticaPK_.codiceTipoDocumento, ImportPolicy.copy());
    cosmoRTipodocTipopraticasPKPolicy.fieldsPolicy
    .put(CosmoRTipodocTipopraticaPK_.codiceTipoPratica, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipodocTipopraticaPK_.class, cosmoRTipodocTipopraticasPKPolicy);

    var cosmoRTipodocTipopraticasPolicy = new ImportPolicy<CosmoRTipodocTipopratica>();
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.id,
        ImportPolicy.clone(cosmoRTipodocTipopraticasPKPolicy));
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.cosmoDTipoDocumento,
        ImportPolicy.clone(cosmoDTipoDocumentoPolicy));
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoRTipodocTipopratica_.cosmoDTipoPratica,
        ImportPolicy.ignore());
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal, ImportPolicy.copy());
    cosmoRTipodocTipopraticasPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());
    checkPolicyIsComplete(CosmoRTipodocTipopratica_.class, cosmoRTipodocTipopraticasPolicy);

    var cosmoRTabDettaglioTipoPraticaPKPolicy = new ImportPolicy<CosmoRTabDettaglioTipoPraticaPK>();
    cosmoRTabDettaglioTipoPraticaPKPolicy.fieldsPolicy

    .put(CosmoRTabDettaglioTipoPraticaPK_.codiceTabDettaglio, ImportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPKPolicy.fieldsPolicy
    .put(CosmoRTabDettaglioTipoPraticaPK_.codiceTipoPratica, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoRTabDettaglioTipoPraticaPK_.class,
        cosmoRTabDettaglioTipoPraticaPKPolicy);

    var cosmoRTabDettaglioTipoPraticaPolicy = new ImportPolicy<CosmoRTabDettaglioTipoPratica>();
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoRTabDettaglioTipoPratica_.id,
        ImportPolicy.clone(cosmoRTabDettaglioTipoPraticaPKPolicy));
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(
        CosmoRTabDettaglioTipoPratica_.cosmoDTabDettaglio,
        ImportPolicy.builder(ctx -> cosmoDTabsDettaglioRepository
            .findOneActive(ctx.source.get("cosmoDTabDettaglio").get("codice").asText())
            .orElseThrow()));
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy
    .put(CosmoRTabDettaglioTipoPratica_.cosmoDTipoPratica, ImportPolicy.ignore());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoRTabDettaglioTipoPratica_.ordine,
        ImportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoREntity_.dtFineVal,
        ImportPolicy.copy());
    cosmoRTabDettaglioTipoPraticaPolicy.fieldsPolicy.put(CosmoREntity_.dtInizioVal,
        ImportPolicy.copy());

    checkPolicyIsComplete(CosmoRTabDettaglioTipoPratica_.class,
        cosmoRTabDettaglioTipoPraticaPolicy);


    var enteCertificatorePolicy = new ImportPolicy<CosmoDEnteCertificatore>();
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codice, ImportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codiceCa,
        ImportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.codiceTsa,
        ImportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoDTipoPraticas,
        ImportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoREnteCertificatoreEntes,
        ImportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.descrizione,
        ImportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.cosmoTCertificatoFirmas,
        ImportPolicy.ignore());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEnteCertificatore_.provider,
        ImportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal,
        ImportPolicy.copy());
    enteCertificatorePolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal,
        ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDEnteCertificatore_.class, enteCertificatorePolicy);

    var tipoCredenzialiFirmaPolicy = new ImportPolicy<CosmoDTipoCredenzialiFirma>();
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.codice,
        ImportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.descrizione,
        ImportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.cosmoDTipoPraticas,
        ImportPolicy.ignore());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.nonValidoInApposizione,
        ImportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDTipoCredenzialiFirma_.cosmoTCertificatoFirmas,
        ImportPolicy.ignore());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    tipoCredenzialiFirmaPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoCredenzialiFirma_.class, tipoCredenzialiFirmaPolicy);

    var tipoOtpPolicy = new ImportPolicy<CosmoDTipoOtp>();
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.codice, ImportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.descrizione, ImportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.nonValidoInApposizione, ImportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.cosmoDTipoPraticas, ImportPolicy.ignore());
    tipoOtpPolicy.fieldsPolicy.put(CosmoDTipoOtp_.cosmoTCertificatoFirmas, ImportPolicy.ignore());
    checkPolicyIsComplete(CosmoDTipoOtp_.class, tipoOtpPolicy);

    var profiloFeqPolicy = new ImportPolicy<CosmoDProfiloFeq>();
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.codice, ImportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.descrizione, ImportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.nonValidoInApposizione,
        ImportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoDTipoPraticas, ImportPolicy.ignore());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoRFormatoFileProfiloFeqTipoFirmas,
        ImportPolicy.ignore());
    profiloFeqPolicy.fieldsPolicy.put(CosmoDProfiloFeq_.cosmoTCertificatoFirmas,
        ImportPolicy.ignore());
    checkPolicyIsComplete(CosmoDProfiloFeq_.class, profiloFeqPolicy);

    var sceltaMarcaTemporalePolicy = new ImportPolicy<CosmoDSceltaMarcaTemporale>();
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.codice,
        ImportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.descrizione,
        ImportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.nonValidoInApposizione,
        ImportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.cosmoTCertificatoFirmas,
        ImportPolicy.ignore());
    sceltaMarcaTemporalePolicy.fieldsPolicy.put(CosmoDSceltaMarcaTemporale_.cosmoDTipoPraticas,
        ImportPolicy.ignore());
    checkPolicyIsComplete(CosmoDSceltaMarcaTemporale_.class, sceltaMarcaTemporalePolicy);


    var importSpecForRoot = new ImportPolicy<CosmoDTipoPratica>();
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.codice, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.descrizione, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.caseDefinitionKey, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.codiceApplicazioneStardas,
        ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTPraticas, ImportPolicy.ignore());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.creabileDaInterfaccia,
        ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.creabileDaServizio, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.annullabile, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.assegnabile, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.condivisibile, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.processDefinitionKey,
        ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoCConfigurazioneMetadatis,
        ImportPolicy.clone(exportSpecForConfigMetadati).withOrder(22000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRStatoTipoPraticas,
        ImportPolicy.clone(cosmoRStatoTipoPraticasPolicy).withOrder(20000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTipodocTipopraticas,
        ImportPolicy.clone(cosmoRTipodocTipopraticasPolicy).withOrder(21000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTabDettaglioTipoPraticas,
        ImportPolicy.clone(cosmoRTabDettaglioTipoPraticaPolicy).withOrder(23000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.trasformazioni,
        ImportPolicy.clone(buildPolicyForTrasformazioniDatiPraticaImport()).withOrder(24000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTTemplateFeas,
        ImportPolicy.clone(buildPolicyForTemplateFeaImport(importContext)).withOrder(25000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRGruppoTipoPraticas,
        ImportPolicy.ignore());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDCustomFormFormios,
        ImportPolicy.ignore());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTEnte, ImportPolicy.ignore());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTMessaggioNotificas,
        ImportPolicy.ignore());

    importSpecForRoot = importSpecForRoot.withPrePersistExecution(ctx -> {
      var target = (CosmoDTipoPratica) ctx.target;
      target.setCosmoTEnte(cosmoTEnteRepository
          .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElse(null));
    });


    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.responsabileTrattamentoStardas,
        ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.overrideResponsabileTrattamento,
        ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.overrideFruitoreDefault,
        ImportPolicy.copy());
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.codiceFruitoreStardas,
        ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.registrazioneStilo, ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.tipoUnitaDocumentariaStilo,
        ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.icona, ImportPolicy.copy());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoTVariabileProcessos,
        ImportPolicy.ignore());

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoRTipoDocumentoTipoDocumentos,
        ImportPolicy.clone(cosmoRTipoDocumentoTipoDocumentosPolicy).withOrder(26000));

    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDEnteCertificatore,
        ImportPolicy.clone(enteCertificatorePolicy));
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDTipoCredenzialiFirma,
        ImportPolicy.clone(tipoCredenzialiFirmaPolicy));
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDSceltaMarcaTemporale,
        ImportPolicy.clone(sceltaMarcaTemporalePolicy));
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDProfiloFeq,
        ImportPolicy.clone(profiloFeqPolicy));
    importSpecForRoot.fieldsPolicy.put(CosmoDTipoPratica_.cosmoDTipoOtp,
        ImportPolicy.clone(tipoOtpPolicy));

    checkPolicyIsComplete(CosmoDTipoPratica_.class, importSpecForRoot);

    return importSpecForRoot;
  }


  private ImportPolicy<CosmoDTrasformazioneDatiPratica> buildPolicyForTrasformazioniDatiPraticaImport() {
    var trasformazioniPolicy = new ImportPolicy<CosmoDTrasformazioneDatiPratica>();
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.codiceFase,
        ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.codiceTipoPratica,
        ImportPolicy.builder(ctx -> {
          var root = (CosmoDTipoPratica) ctx.parentTarget;
          return root.getCodice();
        }));
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.definizione,
        ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.descrizione,
        ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.id,
        ImportPolicy.ignore());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.obbligatoria,
        ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.ordine,
        ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    trasformazioniPolicy.fieldsPolicy.put(CosmoDTrasformazioneDatiPratica_.tipoPratica,
        ImportPolicy.builder(ctx -> {
          var root = (CosmoDTipoPratica) ctx.parentTarget;
          return cosmoDTipoPraticaRepository.findOne(root.getCodice());
        }));

    trasformazioniPolicy = trasformazioniPolicy.withConflictChecker(ctx -> {
      var target = (CosmoDTrasformazioneDatiPratica) ctx.target;
      var found = cosmoDTrasformazioneDatiPraticaRepository.findAllActive((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.isNull(root.get(CosmoDEntity_.dtFineVal)),
          cb.equal(root.get(CosmoDTrasformazioneDatiPratica_.codiceFase), target.getCodiceFase()),
          cb.equal(root.get(CosmoDTrasformazioneDatiPratica_.codiceTipoPratica), target.getCodiceTipoPratica()),
          cb.equal(root.get(CosmoDTrasformazioneDatiPratica_.ordine), target.getOrdine())
          )
      //@formatter:on
          );
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoDTrasformazioneDatiPratica) ctx.target;
      return "[ROOT," + target.getCodiceFase() + "," + target.getOrdine() + "]";
    });

    checkPolicyIsComplete(CosmoDTrasformazioneDatiPratica_.class, trasformazioniPolicy);
    return trasformazioniPolicy;
  }

  private ImportPolicy<CosmoTTemplateFea> buildPolicyForTemplateFeaImport(
      ImportContext importContext) {


    var tipoDocumentoPolicy = new ImportPolicy<CosmoDTipoDocumento>();
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codice, ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.codiceStardas, ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRFormatoFileTipoDocumentos,
        ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoRTipodocTipopraticas,
        ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosAllegato, ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy
    .put(CosmoDTipoDocumento_.cosmoRTipoDocumentoTipoDocumentosPadre, ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTDocumentos,
        ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.cosmoTTemplateFeas,
        ImportPolicy.ignore());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.descrizione, ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.dimensioneMassima,
        ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDTipoDocumento_.firmabile, ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.copy());
    tipoDocumentoPolicy.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    checkPolicyIsComplete(CosmoDTipoDocumento_.class, tipoDocumentoPolicy);

    var templateFeaPolicy = new ImportPolicy<CosmoTTemplateFea>();
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.id, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.descrizione, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.coordinataX, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.coordinataY, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.pagina, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.caricatoDaTemplate, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.cosmoTPratica, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.copy());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.tipologiaPratica, ImportPolicy.ignore());
    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.tipologiaDocumento,
        ImportPolicy.clone(tipoDocumentoPolicy));

    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.ente, ImportPolicy.ignore());

    templateFeaPolicy = templateFeaPolicy.withPrePersistExecution(ctx -> {
      var target = (CosmoTTemplateFea) ctx.target;
      target.setEnte(cosmoTEnteRepository
          .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElse(null));
    });


    templateFeaPolicy.fieldsPolicy.put(CosmoTTemplateFea_.tipologiaPratica,
        ImportPolicy.builder(ctx -> {
          var inputField = ctx.source.get("codiceTipoPratica");
          if (inputField != null && !inputField.isNull()) {
            return cosmoDTipoPraticaRepository
                .findOneByField(CosmoDTipoPratica_.codice, inputField.asText()).orElseThrow();
          }
          return null;
        }));


    templateFeaPolicy = templateFeaPolicy.withConflictChecker(ctx -> {
      var target = (CosmoTTemplateFea) ctx.target;


      var found = cosmoTTemplateFeaRepository.findAllNotDeleted((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.equal(root.get(CosmoTTemplateFea_.tipologiaDocumento).get(CosmoDTipoDocumento_.codice), target.getTipologiaDocumento().getCodice()),
          cb.equal(root.get(CosmoTTemplateFea_.tipologiaPratica).get(CosmoDTipoPratica_.codice), target.getTipologiaPratica().getCodice()),
          cb.isNull(root.get(CosmoTTemplateFea_.cosmoTPratica))
          )
      //@formatter:on
          );
      if (found.isEmpty()) {
        return null;
      } else if (found.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else {
        return found.get(0);
      }
    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTTemplateFea) ctx.target;
      return target.getDescrizione();
    });

    checkPolicyIsComplete(CosmoTTemplateFea_.class, templateFeaPolicy);
    return templateFeaPolicy;
  }

  public ImportPolicy<CosmoDCustomFormFormio> buildPolicyForCustomFormImport() {

    var importSpecForCustomForm = new ImportPolicy<CosmoDCustomFormFormio>();

    importSpecForCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.codice, ImportPolicy.copy());
    importSpecForCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.descrizione,
        ImportPolicy.copy());
    importSpecForCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.customForm,
        ImportPolicy.copy());
    importSpecForCustomForm.fieldsPolicy.put(CosmoDEntity_.dtInizioVal, ImportPolicy.copy());
    importSpecForCustomForm.fieldsPolicy.put(CosmoDEntity_.dtFineVal, ImportPolicy.ignore());

    importSpecForCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoDTipoPratica,
        ImportPolicy.ignore());
    importSpecForCustomForm.fieldsPolicy.put(CosmoDCustomFormFormio_.cosmoTHelpers, ImportPolicy.ignore());

    importSpecForCustomForm = importSpecForCustomForm.withConflictChecker(ctx -> {
      var target = (CosmoDCustomFormFormio) ctx.target;
      return cosmoDCustomFormFormioRepository
          .findOneActiveByField(CosmoDCustomFormFormio_.codice, target.getCodice()).orElse(null);

    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoDCustomFormFormio) ctx.target;
      return target.getCodice();
    });

    checkPolicyIsComplete(CosmoDCustomFormFormio_.class, importSpecForCustomForm);

    return importSpecForCustomForm;
  }

  public ImportPolicy<CosmoTUtenteGruppo> buildPolicyForTUtenteGruppoImport(
      ImportContext importContext) {

    var importSpec = new ImportPolicy<CosmoTUtenteGruppo>();

    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.id, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.idUtente, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.idGruppo, ImportPolicy.copy());

    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.cosmoRUtenteGruppoTags, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.gruppo, ImportPolicy.builder(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.parentTarget;

      return gruppoRepository.findOneNotDeleted((root, cq, cb) -> {
        var joinEnte = root.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinEnte),
            cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
            cb.equal(root.get(CosmoTGruppo_.codice), parentGroup.getCodice()));
      }).orElse(null);
    }));

    importSpec.fieldsPolicy.put(CosmoTUtenteGruppo_.utente, ImportPolicy.builder(ctx ->

    utenteRepository.findOneNotDeleted((root, cq, cb) -> {
      var joinAssocUtenteEnte = root.join(CosmoTUtente_.cosmoRUtenteEntes);
      var joinEnte = joinAssocUtenteEnte.join(CosmoRUtenteEnte_.cosmoTEnte);
      return cb.and(cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(joinAssocUtenteEnte.get(CosmoREntity_.dtFineVal)),
          cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
          cb.equal(root.get(CosmoTUtente_.codiceFiscale),
              ctx.source.get(CODICE_FISCALE_UTENTE).asText()));
    }).orElse(null)));

    importSpec = importSpec.withConflictChecker(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.breadcrumb.getLast();

      var results = utenteGruppoRepository.findAllNotDeleted((root, cq, cb) -> {
        var joinUtente = root.join(CosmoTUtenteGruppo_.utente);
        var joinGruppo = root.join(CosmoTUtenteGruppo_.gruppo);
        var joinEnte = joinGruppo.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinUtente), cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
            cb.isNull(joinUtente.get(CosmoTEntity_.dtCancellazione)),
            cb.isNull(joinGruppo.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.equal(joinGruppo.get(CosmoTGruppo_.codice), parentGroup.getCodice()),
            cb.equal(joinUtente.get(CosmoTUtente_.codiceFiscale),
                ctx.input.get(CODICE_FISCALE_UTENTE).asText()));
      });
      if (results.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else if (!results.isEmpty()) {
        return results.get(0);
      }
      return null;

    }).withUniqueIdentifierProvider(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.breadcrumb.getLast();
      return "[" + parentGroup.getCodice() + "," + ctx.input.get(CODICE_FISCALE_UTENTE).asText()
          + "]";
    });

    importSpec = importSpec.withBeforePersistHook(ctx -> {
      var casted = (CosmoTUtenteGruppo) ctx.target;
      if ((casted).getId() == null) {
        String cf = ctx.input.get(CODICE_FISCALE_UTENTE).asText();

        ctx.importContext.danger(String.format(
            "Il processo che si sta importando referenzia il gruppo utenti '%s' con codice '%s' "
                + "associato ad un utente con codice fiscale '%s' che corrisponde ad un utente non censito o non associato all'ente su cui si sta importando. "
                + "Il gruppo utenti verra' creato (se non esiste gia') ma l'utente in questione non verra' associato.",
                casted.getGruppo().getNome(), casted.getGruppo().getCodice(), cf));

        var out = new HookResult();
        out.abort = Boolean.TRUE;
        return out;
      }

      return null;
    });

    checkPolicyIsComplete(CosmoTUtenteGruppo_.class, importSpec);

    return importSpec;
  }


  public ImportPolicy<CosmoRGruppoTipoPratica> buildPolicyForRGruppoTipoPraticaImport(
      ImportContext importContext) {


    var importSpec = new ImportPolicy<CosmoRGruppoTipoPratica>();

    importSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.id, ImportPolicy.builder(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.parentTarget;

      var tipoPraticaFound = cosmoDTipoPraticaRepository.findOne((root, cq, cb) -> {
        var joinEnte = root.join(CosmoDTipoPratica_.cosmoTEnte);
        return cb.and(cb.isNotNull(joinEnte), cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.isNull(root.get(CosmoDEntity_.dtFineVal)), cb.equal(
                root.get(CosmoDTipoPratica_.codice), ctx.source.get("codiceTipoPratica").asText()));
      });

      var groupFound = gruppoRepository.findOneNotDeleted((root, cq, cb) -> {
        var joinEnte = root.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinEnte),
            cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
            cb.equal(root.get(CosmoTGruppo_.codice), parentGroup.getCodice()));
      }).orElse(null);

      if (tipoPraticaFound != null && groupFound != null) {
        var id = new CosmoRGruppoTipoPraticaPK();
        id.setIdGruppo(groupFound.getId());
        id.setCodiceTipoPratica(tipoPraticaFound.getCodice());
        return id;
      }

      return null;
    }));

    importSpec.fieldsPolicy.put(CosmoREntity_.dtFineVal, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoREntity_.dtInizioVal, ImportPolicy.copy());

    importSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.cosmoTGruppo, ImportPolicy.builder(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.parentTarget;

      return gruppoRepository.findOneNotDeleted((root, cq, cb) -> {
        var joinEnte = root.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinEnte),
            cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
            cb.equal(root.get(CosmoTGruppo_.codice), parentGroup.getCodice()));
      }).orElse(null);
    }));

    importSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.cosmoDTipoPratica,
        ImportPolicy.builder(ctx -> cosmoDTipoPraticaRepository.findOne((root, cq, cb) -> {
          var joinEnte = root.join(CosmoDTipoPratica_.cosmoTEnte);
          return cb.and(cb.isNotNull(joinEnte), cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
              cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
              cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
              cb.isNull(root.get(CosmoDEntity_.dtFineVal)),
              cb.equal(root.get(CosmoDTipoPratica_.codice),
                  ctx.source.get("codiceTipoPratica").asText()));
        })));

    importSpec = importSpec.withConflictChecker(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.breadcrumb.getLast();

      var results = gruppoTipoPraticaRepository.findAllActive((root, cq, cb) -> {
        var joinTipoPratica = root.join(CosmoRGruppoTipoPratica_.cosmoDTipoPratica);
        var joinGruppo = root.join(CosmoRGruppoTipoPratica_.cosmoTGruppo);
        var joinEnte = joinGruppo.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinTipoPratica), cb.isNotNull(joinGruppo),
            cb.isNull(root.get(CosmoREntity_.dtFineVal)),
            cb.isNull(joinTipoPratica.get(CosmoDEntity_.dtFineVal)),
            cb.isNull(joinGruppo.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.equal(joinGruppo.get(CosmoTGruppo_.codice), parentGroup.getCodice()),
            cb.equal(joinTipoPratica.get(CosmoDTipoPratica_.codice),
                ctx.input.get("codiceTipoPratica").asText()));
      });

      if (results.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else if (!results.isEmpty()) {
        return results.get(0);
      }
      return null;

    }).withUniqueIdentifierProvider(ctx -> {
      var parentGroup = (CosmoTGruppo) ctx.breadcrumb.getLast();
      return "[" + parentGroup.getCodice() + "," + ctx.input.get("codiceTipoPratica").asText()
          + "]";
    });

    importSpec = importSpec.withBeforePersistHook(ctx -> {
      var casted = (CosmoRGruppoTipoPratica) ctx.target;
      if ((casted).getId() == null) {
        String ctp = ctx.input.get("codiceTipoPratica").asText();

        ctx.importContext.danger(String.format(
            "Il processo che si sta importando referenzia il gruppo '%s' con codice '%s' "
                + "associato ad un tipo pratica con codice '%s' che corrisponde ad un tipo pratica non censito. "
                + "Il gruppo verra' creato (se non esiste gia') ma il tipo pratica in questione non verra' associato.",
                casted.getCosmoTGruppo().getNome(), casted.getCosmoTGruppo().getCodice(), ctp));

        var out = new HookResult();
        out.abort = Boolean.TRUE;
        return out;
      }

      return null;
    });

    importSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.creatore, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoRGruppoTipoPratica_.supervisore, ImportPolicy.copy());

    checkPolicyIsComplete(CosmoRGruppoTipoPratica_.class, importSpec);

    return importSpec;
  }

  public ImportPolicy<CosmoTGruppo> buildPolicyForGruppoImport(ImportContext importContext) {

    var importSpec = new ImportPolicy<CosmoTGruppo>();

    importSpec.fieldsPolicy.put(CosmoTGruppo_.id, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTEntity_.dtCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.dtUltimaModifica, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteCancellazione, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteInserimento, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTEntity_.utenteUltimaModifica, ImportPolicy.ignore());

    importSpec.fieldsPolicy.put(CosmoTGruppo_.codice, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTGruppo_.cosmoRPraticaUtenteGruppos, ImportPolicy.ignore());
    importSpec.fieldsPolicy.put(CosmoTGruppo_.descrizione, ImportPolicy.copy());
    importSpec.fieldsPolicy.put(CosmoTGruppo_.ente, ImportPolicy.builder(ctx -> {
      return cosmoTEnteRepository
          .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, importContext.tenantId).orElseThrow();
    }));

    importSpec.fieldsPolicy.put(CosmoTGruppo_.nome, ImportPolicy.copy());

    importSpec.fieldsPolicy.put(CosmoTGruppo_.associazioniUtenti,
        ImportPolicy.clone(buildPolicyForTUtenteGruppoImport(importContext)).withOrder(20000));

    importSpec.fieldsPolicy.put(CosmoTGruppo_.cosmoRGruppoTipoPraticas,
        ImportPolicy.clone(buildPolicyForRGruppoTipoPraticaImport(importContext)).withOrder(21000));

    importSpec.fieldsPolicy.put(CosmoTGruppo_.cosmoRPraticaUtenteGruppos, ImportPolicy.ignore());

    checkPolicyIsComplete(CosmoTGruppo_.class, importSpec);

    importSpec = importSpec.withConflictChecker(ctx -> {
      var target = (CosmoTGruppo) ctx.target;
      var results = gruppoRepository.findAllNotDeleted((root, cq, cb) -> {
        var joinEnte = root.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinEnte),
            cb.isNull(joinEnte.get(CosmoTEntity_.dtCancellazione)),
            cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), importContext.tenantId),
            cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
            cb.equal(root.get(CosmoTGruppo_.codice), target.getCodice()));
      });

      if (results.size() > 1) {
        throw new InternalServerException("too many candidates");
      } else if (!results.isEmpty()) {
        return results.get(0);
      }
      return null;

    }).withUniqueIdentifierProvider(ctx -> {
      var target = (CosmoTGruppo) ctx.target;
      return target.getCodice();
    });

    return importSpec;
  }


  private byte[] doExport(EsportaTipoPraticaRequest request) throws Exception {
    ValidationUtils.require(request, "request");

    String codiceTipoPratica = request.getCodiceTipoPratica();
    String tenantId = request.getTenantId();

    ValidationUtils.require(codiceTipoPratica, "codiceTipoPratica");
    ValidationUtils.require(tenantId, "tenantId");

    var ente = cosmoTEnteRepository.findOneNotDeletedByField(CosmoTEnte_.codiceIpa, tenantId)
        .orElseThrow();

    var exportSpecForRoot = buildPolicyForTipoPraticaExport();
    var formLogicoPolicy = buildPolicyForFormLogicoExport();
    var customFormPolicy = buildPolicyForCustomFormExport();
    var templatePolicy = buildPolicyForTemplateReportExport();
    var risorsaTemplatePolicy = buildPolicyForRisorsaTemplateReportExport();
    var gruppoPolicy = buildPolicyForGruppoExport();
    var variabileProcessoPolicy = buildPolicyForTVariabileProcessoExport();
    var tagPolicy = buildPolicyForTTagExport();

    var tipoPratica = cosmoDTipoPraticaRepository.findOneActive(codiceTipoPratica).orElseThrow();

    // retrieve form keys from process definition
    var processDefinitions = cosmoCmmnFeignClient.listProcessDefinitionsByKey(Boolean.TRUE,
        tipoPratica.getProcessDefinitionKey(), tenantId);
    var processDefinition = processDefinitions.getData()[0];

    // retrieve process model
    var processModel = cosmoCmmnFeignClient.getProcessDefinitionModel(processDefinition.getId());

    // extract all distinct form keys from process model
    Set<String> formKeys = new HashSet<>();
    Set<String> groupsInvolved = new HashSet<>();
    extractFormKeysAndGroups(formKeys, groupsInvolved, "", processModel);

    // rebuild BAR zip from definitions
    // retrieve main BPMN data
    var mainBpmnData =
        cosmoCmmnFeignClient.getProcessDefinitionResourceData(processDefinition.getId());
    var mainBpmnDataTargetFile = processDefinition.getKey() + ".bpmn";

    var formDefinitions = new LinkedList<byte[]>();
    var formDefinitionsTargetFile = new LinkedList<String>();

    // find the form deployment
    var formDeployments = cosmoCmmnFeignClient
        .getFormRepositoryDeployments(processDefinition.getDeploymentId(), tenantId);
    if (formDeployments.getData().length > 0) {

      var formDeployment = formDeployments.getData()[0];

      // get forms in selected form deployment
      var formsInDeployment = cosmoCmmnFeignClient
          .getFormRepositoryDefinitionsForFormDeployment(formDeployment.getId(), tenantId);

      // extract form definitions

      for (FormDefinitionResponse formInDeployment : formsInDeployment.getData()) {
        // get form data
        var formInDeploymentResourceData =
            cosmoCmmnFeignClient.getFormDefinitionResourceData(formInDeployment.getId());
        formDefinitions.add(formInDeploymentResourceData);
        formDefinitionsTargetFile.add("form-" + formInDeployment.getKey() + ".form");
      }
    }

    // build app file
    var appPayload = new LinkedHashMap<>();
    appPayload.put("key", processDefinition.getKey());
    appPayload.put("name", processDefinition.getName());
    appPayload.put("description", "");
    appPayload.put("theme", "theme-5");
    appPayload.put("icon", "glyphicon-asterisk");
    appPayload.put("usersAccess", null);
    appPayload.put("groupsAccess", null);
    var appPayloadData = ObjectUtils.toJson(appPayload).getBytes();
    var appPayloadTargetFile = processDefinition.getKey() + ".app";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      ZipEntry entry = new ZipEntry(mainBpmnDataTargetFile);
      zos.putNextEntry(entry);
      zos.write(mainBpmnData);
      zos.closeEntry();

      ZipEntry entry1 = new ZipEntry(appPayloadTargetFile);
      zos.putNextEntry(entry1);
      zos.write(appPayloadData);
      zos.closeEntry();

      for (int i = 0; i < formDefinitions.size(); i++) {

        ZipEntry entry2 = new ZipEntry(formDefinitionsTargetFile.get(i));
        zos.putNextEntry(entry2);
        zos.write(formDefinitions.get(i));
        zos.closeEntry();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    var processFileBytes = baos.toByteArray();

    if (tipoPratica.getCosmoTTemplateFeas() != null
        && !tipoPratica.getCosmoTTemplateFeas().isEmpty()) {
      tipoPratica.setCosmoTTemplateFeas(tipoPratica.getCosmoTTemplateFeas().stream()
          .filter(elem -> elem.getCosmoTPratica() == null && elem.getTipologiaDocumento().valido())
          .collect(Collectors.toList()));
    }


    var copiedTipoPratica =
        applyExportPolicy(tipoPratica, CosmoDTipoPratica.class, exportSpecForRoot);

    Set<String> codiceCustomFormsDaEsportare = new HashSet<>();

    Set<String> codiceTemplatesDaEsportare = new HashSet<>();

    List<String> groupsIn = new ArrayList<>();

    var copiedGruppi = new LinkedList<Object>();
    var codici = new LinkedList<String>();
    if (!groupsInvolved.isEmpty()) {

      var gruppi = gruppoRepository.findAllNotDeleted((root, cq, cb) -> {
        var joinEnte = root.join(CosmoTGruppo_.ente);
        return cb.and(cb.isNotNull(joinEnte), cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), tenantId),
            cb.isNotNull(root.get(CosmoTGruppo_.codice)),
            root.get(CosmoTGruppo_.codice).in(groupsInvolved));
      });

      for (CosmoTGruppo gruppo : gruppi) {
        groupsIn.add(gruppo.getCodice());
        var copiedGruppo = applyExportPolicy(gruppo, CosmoTGruppo.class, gruppoPolicy);
        copiedGruppi.add(copiedGruppo);
        codici.add(gruppo.getCodice());
      }

    }

    var gruppiPratica = gruppoRepository.findAllNotDeleted((root, cq, cb) -> {
      var joinEnte = root.join(CosmoTGruppo_.ente);
      var joinTipoPratica = root.join(CosmoTGruppo_.cosmoRGruppoTipoPraticas);
      if (groupsIn.isEmpty()) {
        return cb.and(cb.isNotNull(joinEnte), cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), tenantId),
            cb.isNotNull(root.get(CosmoTGruppo_.codice)),
            cb.equal(joinTipoPratica.get(CosmoRGruppoTipoPratica_.cosmoDTipoPratica)
                .get(CosmoDTipoPratica_.codice), codiceTipoPratica),
            cb.isNull(joinTipoPratica.get(CosmoREntity_.dtFineVal)));
      } else {
        return cb.and(cb.isNotNull(joinEnte), cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
            cb.equal(joinEnte.get(CosmoTEnte_.codiceIpa), tenantId),
            cb.isNotNull(root.get(CosmoTGruppo_.codice)),
            root.get(CosmoTGruppo_.codice).in(groupsIn).not(),
            cb.equal(joinTipoPratica.get(CosmoRGruppoTipoPratica_.cosmoDTipoPratica)
                .get(CosmoDTipoPratica_.codice), codiceTipoPratica),
            cb.isNull(joinTipoPratica.get(CosmoREntity_.dtFineVal)));

      }
    });

    for (CosmoTGruppo gruppo : gruppiPratica) {
      var copiedGruppo = applyExportPolicy(gruppo, CosmoTGruppo.class, gruppoPolicy);
      copiedGruppi.add(copiedGruppo);
      codici.add(gruppo.getCodice());
    }

    var copiedFormLogici = new LinkedList<Object>();
    if (!formKeys.isEmpty()) {

      var formLogici = formLogicoRepository.findAllNotDeleted((root, cq, cb) -> {
        return root.get(CosmoTFormLogico_.codice).in(formKeys);
      });


      for (CosmoTFormLogico formLogico : formLogici) {

        var copiedFormLogico = applyExportPolicy(formLogico, CosmoTFormLogico.class, formLogicoPolicy);

        copiedFormLogici.add(copiedFormLogico);

        // scan dei parametri
        extractCodiciCustomFormEReportTemplates(codiceCustomFormsDaEsportare,
            codiceTemplatesDaEsportare, formLogico);
      }

    }

    var copiedCustomForms = new LinkedList<Object>();
    if (!codiceCustomFormsDaEsportare.isEmpty()) {
      // export all custom forms
      var customForms = cosmoDCustomFormFormioRepository.findAllActive((root, cq, cb) -> {
        return root.get(CosmoDCustomFormFormio_.codice).in(codiceCustomFormsDaEsportare);
      });

      for (CosmoDCustomFormFormio customForm : customForms) {
        var copiedCustomForm =
            applyExportPolicy(customForm, CosmoDCustomFormFormio.class, customFormPolicy);
        copiedCustomForms.add(copiedCustomForm);
      }
    }

    // retrieve and export all report templates and resources
    List<CosmoTTemplateReport> templatesDaEsportare = extractTemplatesDaEsportareFromCodici(
        codiceTemplatesDaEsportare, ente.getId(), tipoPratica.getCodice());

    var copiedTemplates = new LinkedList<Object>();
    var copiedTemplateResources = new LinkedList<Object>();

    if (!templatesDaEsportare.isEmpty()) {
      List<CosmoTRisorsaTemplateReport> risorseTemplatesDaEsportare =
          extractTemplateResourcesDaEsportareFromTemplates(templatesDaEsportare, ente.getId(),
              tipoPratica.getCodice());

      // export dei template
      for (CosmoTTemplateReport templateDaEsportare : templatesDaEsportare) {
        var copied =
            applyExportPolicy(templateDaEsportare, CosmoTTemplateReport.class, templatePolicy);
        copiedTemplates.add(copied);
      }

      // export risorse dei template
      for (CosmoTRisorsaTemplateReport risorsaTemplateDaEsportare : risorseTemplatesDaEsportare) {
        var copied = applyExportPolicy(risorsaTemplateDaEsportare,
            CosmoTRisorsaTemplateReport.class, risorsaTemplatePolicy);
        copiedTemplateResources.add(copied);
      }
    }

    var variabiliProcesso = cosmoTVariabileProcessoRepository
        .findAllNotDeleted((root, cq, cb) -> cb.and(root.get(CosmoTVariabileProcesso_.tipoPratica)
            .get(CosmoDTipoPratica_.codice).in(tipoPratica.getCodice())));

    var copiedTVariabiliProcesso = new LinkedList<Object>();

    for (CosmoTVariabileProcesso variabileProcesso : variabiliProcesso) {
      var copiedTVariabileProcesso = applyExportPolicy(variabileProcesso,
          CosmoTVariabileProcesso.class, variabileProcessoPolicy);
      copiedTVariabiliProcesso.add(copiedTVariabileProcesso);
    }

    var tags = codici.isEmpty() ? new LinkedList<CosmoTTag>()
        : cosmoTTagRepository.findAllNotDeleted((root, cq, cb) -> {
          var joinUtenteGruppoTags = root.join(CosmoTTag_.cosmoRUtenteGruppoTags);
          return cb.and(cb.isNull(joinUtenteGruppoTags.get(CosmoREntity_.dtFineVal)),
              joinUtenteGruppoTags.get(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo)
              .get(CosmoTUtenteGruppo_.gruppo).get(CosmoTGruppo_.codice).in(codici),
              root.get(CosmoTTag_.cosmoTEnte).get(CosmoTEnte_.id).in(ente.getId()));
        });

    var copiedTTags = new LinkedList<Object>();

    for (CosmoTTag tag : tags) {
      var copiedTTag = applyExportPolicy(tag, CosmoTTag.class, tagPolicy);
      copiedTTags.add(copiedTTag);
    }

    // serialize process file to base64
    var processFileBase64 = Base64.getEncoder().encodeToString(processFileBytes);

    // serialize metadata
    var metadata = new HashMap<String, Object>();
    metadata.put("schemaVersion", 5);
    metadata.put("codiceTipoPratica", tipoPratica.getCodice());
    metadata.put("processDefinitionKey", tipoPratica.getProcessDefinitionKey());
    metadata.put("tenantId", tenantId);

    Map<String, Object> export = new HashMap<>();
    export.put("tipoPratica", copiedTipoPratica);
    export.put("formLogici", copiedFormLogici);
    export.put("customForms", copiedCustomForms);
    export.put("gruppi", copiedGruppi);
    export.put("templates", copiedTemplates);
    export.put("templateResources", copiedTemplateResources);
    export.put("processArchiveFile", processFileBase64);
    export.put("metadata", metadata);
    export.put("variabiliProcesso", copiedTVariabiliProcesso);
    export.put("tags", copiedTTags);

    return ObjectUtils.toJson(export).getBytes(StandardCharsets.UTF_8);
  }

  private List<CosmoTRisorsaTemplateReport> extractTemplateResourcesDaEsportareFromTemplates(
      List<CosmoTTemplateReport> templates, Long idEnte, String codiceTipoPratica) {

    Set<String> codiciTrovati =
        templates.stream().map(CosmoTTemplateReport::getCodice).collect(Collectors.toSet());

    return cosmoTRisorsaTemplateReportRepository.findAllNotDeleted((root, cq, cb) ->
    //@formatter:off
    cb.and(
        cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
        cb.or(
            cb.isNull(root.get(CosmoTRisorsaTemplateReport_.idEnte)),
            cb.equal(root.get(CosmoTRisorsaTemplateReport_.idEnte), idEnte)
            ),
        cb.or(
            cb.isNull(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica)),
            cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica), codiceTipoPratica)
            ),
        cb.or(
            cb.isNull(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate)),
            root.get(CosmoTRisorsaTemplateReport_.codiceTemplate).in(codiciTrovati)
            )
        )
    //@formatter:on
        );

  }

  private List<CosmoTTemplateReport> extractTemplatesDaEsportareFromCodici(Set<String> codici,
      Long idEnte, String codiceTipoPratica) {

    int iterations = 0;
    List<CosmoTTemplateReport> allFound = new ArrayList<>();

    var found = cosmoTTemplateReportRepository.findAllNotDeleted((root, cq, cb) ->
    //@formatter:off
    cb.and(
        cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
        cb.or(
            cb.isNull(root.get(CosmoTTemplateReport_.idEnte)),
            cb.equal(root.get(CosmoTTemplateReport_.idEnte), idEnte)
            ),
        cb.or(
            cb.isNull(root.get(CosmoTTemplateReport_.codiceTipoPratica)),
            cb.equal(root.get(CosmoTTemplateReport_.codiceTipoPratica), codiceTipoPratica)
            )
        )
    //@formatter:on
        );

    while (!found.isEmpty()) {
      if (++iterations > 20) {
        throw new InternalServerException("Troppe iterazioni per l'esportazione dei template.");
      }

      allFound.addAll(found);
      Set<String> codiciTrovati =
          found.stream().map(CosmoTTemplateReport::getCodice).collect(Collectors.toSet());

      found = cosmoTTemplateReportRepository.findAllNotDeleted((root, cq, cb) ->
      //@formatter:off
      cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNotNull(root.get(CosmoTTemplateReport_.codiceTemplatePadre)),
          cb.or(
              cb.isNull(root.get(CosmoTTemplateReport_.idEnte)),
              cb.equal(root.get(CosmoTTemplateReport_.idEnte), idEnte)
              ),
          cb.or(
              cb.isNull(root.get(CosmoTTemplateReport_.codiceTipoPratica)),
              cb.equal(root.get(CosmoTTemplateReport_.codiceTipoPratica), codiceTipoPratica)
              ),
          root.get(CosmoTTemplateReport_.codiceTemplatePadre).in(codiciTrovati)
          )
      //@formatter:on
          );
    }

    return allFound;
  }

  private static void extractCodiciCustomFormEReportTemplates(Set<String> customFormCollector,
      Set<String> templateCollector, CosmoTFormLogico formLogico) {
    //@formatter:off
    formLogico.getCosmoRFormLogicoIstanzaFunzionalitas().stream()
    .filter(CosmoREntity::valido)
    .map(CosmoRFormLogicoIstanzaFunzionalita::getCosmoTIstanzaFunzionalitaFormLogico)
    .filter(CosmoTEntity::nonCancellato)
    .flatMap(e -> e.getCosmoRIstanzaFormLogicoParametroValores().stream())
    .filter(CosmoREntity::valido)
    .filter(e -> e.getCosmoDChiaveParametroFunzionalitaFormLogico() != null && e.getCosmoDChiaveParametroFunzionalitaFormLogico().valido())
    .forEach(config -> {
      if (config.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice().equals("CODICE_CUSTOM_FORM")) {
        String val = config.getValoreParametro();
        if (!StringUtils.isBlank(val)) {
          if (val.startsWith("\"") && val.endsWith("\"")) {
            val = ObjectUtils.fromJson(val, String.class);
          }
          customFormCollector.add(val);
        }
      } else if (config.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice().equals("REPORT_GENERABILI")) {
        JsonNode reportGenerabiliNode =  ObjectUtils.fromJson(config.getValoreParametro(), JsonNode.class);
        for (JsonNode reportGenerabileNode: reportGenerabiliNode) {
          String codiceReport = reportGenerabileNode.get("codiceTemplate").asText();
          if (!StringUtils.isBlank(codiceReport)) {
            templateCollector.add(codiceReport);
          }
        }
      }
    });
    //@formatter:on
  }

  private static void extractFormKeysAndGroups(Set<String> formKeysCollector,
      Set<String> groupsCollector, String prefix, JsonNode currentNode) {
    if (currentNode.isArray()) {
      ArrayNode arrayNode = (ArrayNode) currentNode;
      Iterator<JsonNode> node = arrayNode.elements();
      int index = 1;
      while (node.hasNext()) {
        extractFormKeysAndGroups(formKeysCollector, groupsCollector,
            !prefix.isEmpty() ? prefix + "[" + index + "]" : String.valueOf(index), node.next());
        index += 1;
      }
    } else if (currentNode.isObject()) {
      currentNode.fields()
      .forEachRemaining(entry -> extractFormKeysAndGroups(formKeysCollector, groupsCollector,
          !prefix.isEmpty() ? prefix + "." + entry.getKey() : entry.getKey(),
              entry.getValue()));
    } else if (!currentNode.isNull()) {
      if (prefix.endsWith(".formKey")) {
        formKeysCollector.add(currentNode.asText());
      } else if (prefix.contains(".candidateGroups[")
          || prefix.contains(".candidateStarterGroups[")) {
        if (currentNode.asText().contains(".gruppi.")) {
          groupsCollector.add(currentNode.asText().split(".gruppi.")[1]);
        } else {
          groupsCollector.add(currentNode.asText());
        }

      }
    }
  }

  private <T extends Object> Object applyExportPolicy(Object root, Class<?> type,
      ExportPolicy<T> policy) throws Exception {

    if (policy.clone && !exportPolicyVerifications.containsKey(policy.uuid)) {
      throw new RuntimeException("Rejected unverified policy: " + policy);
    }

    Map<String, Object> copy = new HashMap<>();
    copy.put("_class", root.getClass().getName());

    for (Entry<Attribute<?, ?>, ExportPolicy<?>> fieldPolicy : policy.fieldsPolicy.entrySet()) {

      if (fieldPolicy.getValue().copy) {

        var value = FieldUtils.readField(root, fieldPolicy.getKey().getName(), true);
        if (value == null) {
          continue;
        }

        copy.put(fieldPolicy.getKey().getName(), value);

      } else if (fieldPolicy.getValue().clone) {

        var value = FieldUtils.readField(root, fieldPolicy.getKey().getName(), true);
        if (value == null) {
          continue;
        }
        value = initializeAndUnproxy(value);

        if (isCollection(value.getClass())) {

          // check orig type
          var field = root.getClass().getDeclaredField(fieldPolicy.getKey().getName());

          Collection<Object> clonedCollection = createCollectionFromType(field);

          for (Object item : (Collection<?>) value) {
            item = initializeAndUnproxy(item);

            var clonedItem = applyExportPolicy(item, item.getClass(), fieldPolicy.getValue());
            if (!isSoftDeleted(item)) {
              clonedCollection.add(clonedItem);
            }
          }

          copy.put(fieldPolicy.getKey().getName(), clonedCollection);


        } else {
          var cloned = applyExportPolicy(value, value.getClass(), fieldPolicy.getValue());
          if (!isSoftDeleted(value)) {
            copy.put(fieldPolicy.getKey().getName(), cloned);
          }

        }

      }

    }

    if (policy.preExport != null) {
      policy.preExport.accept((T) root, copy);
    }

    return copy;
  }

  private boolean isSoftDeleted(Object entity) {
    entity = initializeAndUnproxy(entity);
    if (entity instanceof CosmoTEntity) {
      return ((CosmoTEntity) entity).cancellato();
    } else if (entity instanceof CosmoREntity) {
      return ((CosmoREntity) entity).nonValido();
    } else if (entity instanceof CosmoDEntity) {
      return ((CosmoDEntity) entity).nonValido();
    }
    return false;
  }

  private Map<String, Object> doImport(ImportaTipoPraticaRequest request) throws Exception {
    ValidationUtils.require(request, "request");

    byte[] sourceContent =
        Base64.getDecoder().decode(request.getSourceContent().getBytes(StandardCharsets.UTF_8));

    List<FieldConflictResolutionInput> conflictResolutionInput =
        request.getConflictResolutionInput();
    String tenantId = request.getTenantId();
    boolean preview = request.getPreview();

    Map<String, Object> output = new HashMap<>();

    var ctx = new ImportContext();
    ctx.tenantId = tenantId;
    ctx.messages = new LinkedList<>();
    ctx.conflictInput = new HashMap<>();
    ctx.pendingConflicts = new LinkedList<>();
    ctx.preview = preview;

    var importSpecForRoot = buildPolicyForTipoPraticaImport(ctx);
    var formLogicoPolicy = buildPolicyForFormLogicoImport(ctx);
    var customFormPolicy = buildPolicyForCustomFormImport();
    var templatePolicy = buildPolicyForTemplateReportImport(ctx);
    var templateResourcePolicy = buildPolicyForRisorsaTemplateReportImport(ctx);
    var groupsPolicy = buildPolicyForGruppoImport(ctx);
    var variabileProcessoPolicy = buildPolicyForVariabileProcessoImport();
    var tagsPolicy = buildPolicyForTagsImport(ctx);

    output.put("messages", ctx.messages);

    conflictResolutionInput.forEach(e -> ctx.conflictInput.put(e.getFullKey(), e));

    JsonNode input =
        ObjectUtils.fromJson(new String(sourceContent, StandardCharsets.UTF_8), JsonNode.class);

    // read tenantId from file metadata
    JsonNode metadata = input.get("metadata");
    if (metadata == null) {
      throw new BadRequestException("Il file fornito non e' un export valido");
    }

    JsonNode versionNode = metadata.get("schemaVersion");
    long schemaVersion = versionNode == null ? 0 : versionNode.asLong();

    String processDefKey = metadata.get("processDefinitionKey").asText();
    // String tenantId = metadata.get("tenantId").asText();

    // deploy del processo
    var processDefinitionBase64 = input.get("processArchiveFile").asText();
    var processDefinitionBytes = Base64.getDecoder().decode(processDefinitionBase64.getBytes());

    // import tipo pratica
    CosmoDTipoPratica copiedTipoPratica =
        applyImportPolicy(ctx, new CosmoDTipoPratica(), CosmoDTipoPratica.class, importSpecForRoot,
            input.get("tipoPratica"), null, new LinkedList<>());

    ctx.codiceTipoPratica = copiedTipoPratica.getCodice();


    // import gruppi utenti
    List<Object> outputInfoForGruppi = new LinkedList<>();
    for (JsonNode node : input.get("gruppi")) {
      CosmoTGruppo copied = applyImportPolicy(ctx, new CosmoTGruppo(), CosmoTGruppo.class,
          groupsPolicy, node, null, new LinkedList<>());
      outputInfoForGruppi.add(copied.getId());
    }


    List<Object> outputInfoForTags = new LinkedList<>();

    for (JsonNode node : input.get("tags")) {
      CosmoTTag copied = applyImportPolicy(ctx, new CosmoTTag(), CosmoTTag.class, tagsPolicy, node,
          null, new LinkedList<>());
      outputInfoForTags.add(copied.getId());
    }


    // import form logici
    List<Object> outputInfoForFormLogici = new LinkedList<>();
    for (JsonNode node : input.get("formLogici")) {
      CosmoTFormLogico copiedFormLogico = applyImportPolicy(ctx, new CosmoTFormLogico(),
          CosmoTFormLogico.class, formLogicoPolicy, node, null, new LinkedList<>());
      outputInfoForFormLogici.add(copiedFormLogico.getId());
    }



    List<Object> outputInfoForCustomForms = new LinkedList<>();
    if (schemaVersion >= 3) {
      // import custom forms

      for (JsonNode node : input.get("customForms")) {
        CosmoDCustomFormFormio copiedCustomForm =
            applyImportPolicy(ctx, new CosmoDCustomFormFormio(), CosmoDCustomFormFormio.class,
                customFormPolicy, node, null, new LinkedList<>());
        outputInfoForCustomForms.add(copiedCustomForm.getCodice());
      }

    }


    List<Object> outputInfoForTemplates = new LinkedList<>();
    List<Object> outputInfoForTemplateResources = new LinkedList<>();
    if (schemaVersion >= 4) {
      // import templates
      for (JsonNode node : input.get("templates")) {
        CosmoTTemplateReport copied = applyImportPolicy(ctx, new CosmoTTemplateReport(),
            CosmoTTemplateReport.class, templatePolicy, node, null, new LinkedList<>());

        outputInfoForTemplates.add(copied.getId());
      }
      // import template resources
      for (JsonNode node : input.get("templateResources")) {
        CosmoTRisorsaTemplateReport copied = applyImportPolicy(ctx,
            new CosmoTRisorsaTemplateReport(), CosmoTRisorsaTemplateReport.class,
            templateResourcePolicy, node, null, new LinkedList<>());

        outputInfoForTemplateResources.add(copied.getId());
      }
    }


    List<Object> outputInfoForVariabileProcesso = new LinkedList<>();

    for (JsonNode node : input.get("variabiliProcesso")) {
      CosmoTVariabileProcesso copied = applyImportPolicy(ctx, new CosmoTVariabileProcesso(),
          CosmoTVariabileProcesso.class, variabileProcessoPolicy, node, null, new LinkedList<>());
      outputInfoForVariabileProcesso.add(copied.getId());
    }


    // check for pending conflicts
    if (!ctx.pendingConflicts.isEmpty()) {
      logger.info("testImportazione", "pendingConflicts", ctx.pendingConflicts);

      output.put("done", false);
      output.put("exitReason", "pendingConflicts");
      output.put("conflicts", ctx.pendingConflicts);
      output.put("message", "There are pending conflicts. \n"
          + ctx.pendingConflicts.stream().map(x -> x.message).collect(Collectors.joining("\n")));

      throw new DryRunExitException(output);
    }

    // prepare for process deployment
    var processDefinitionAsUploadableResource = new ByteArrayResource(processDefinitionBytes) {
      @Override
      public String getFilename() {
        return processDefKey + ".bar";
      }
    };

    MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();
    formParams.add("tenantId", tenantId);
    formParams.add(processDefKey, processDefinitionAsUploadableResource);

    // deploy the process
    if (!preview) {
      ProcessDeploymentWrapper processDeploymentResponse =
          cosmoCmmnFeignClient.createDeployment(formParams);
      output.put("processDeployment", processDeploymentResponse);
    } else {
      output.put("processDeployment", "skipped");
    }

    output.put("tipoPratica", copiedTipoPratica.getCodice());
    output.put("formLogici", outputInfoForFormLogici);
    output.put("customForms", outputInfoForCustomForms);
    output.put("conflicts", null);
    output.put("preview", preview);
    output.put("done", true);

    if (preview) {
      output.put("exitReason", "preview");
      throw new DryRunExitException(output);
    }

    return output;
  }

  private <T extends Object> T applyImportPolicy(ImportContext ctx, Object root, Class<?> type,
      ImportPolicy<T> policy, JsonNode input, Object parentTarget, LinkedList<Object> breadcrumb)
          throws Exception {

    if (policy.clone && !importPolicyVerifications.containsKey(policy.uuid)) {
      throw new BadConfigurationException("Rejected unverified policy: " + policy);
    }

    T copy = (T) type.newInstance();

    final Object[] finalResultCollector = new Object[] {null};
    finalResultCollector[0] = copy;

    var importExecutors = new ArrayList<ExecutionCandidate>();

    for (Entry<Attribute<?, ?>, ImportPolicy<?>> fieldPolicy : policy.fieldsPolicy.entrySet()) {
      var ec = new ExecutionCandidate();
      importExecutors.add(ec);
      ec.policy = fieldPolicy.getValue();
      ec.order = fieldPolicy.getValue().order;
      ec.callable = () -> {

        if (fieldPolicy.getValue().copy) {

          var value = input.get(fieldPolicy.getKey().getName());

          if (value != null) {

            var field = getDeclaredFieldRecursive(copy.getClass(), fieldPolicy.getKey().getName());
            var converted = ObjectUtils.fromJson(value.toString(), field.getType());

            FieldUtils.writeField(copy, fieldPolicy.getKey().getName(), converted, true);
          }

        } else if (fieldPolicy.getValue().clone) {

          var value = input.get(fieldPolicy.getKey().getName());
          if (value == null) {
            return null;
          }

          if (value.isArray()) {

            var field = getDeclaredFieldRecursive(copy.getClass(), fieldPolicy.getKey().getName());
            Collection<Object> clonedCollection = createCollectionFromType(field);

            for (JsonNode item : value) {
              var cloned = Class.forName(item.get("_class").asText()).newInstance();

              breadcrumb.addLast(finalResultCollector[0]);

              cloned = applyImportPolicy(ctx, cloned, cloned.getClass(), fieldPolicy.getValue(),
                  item, finalResultCollector[0], breadcrumb);

              breadcrumb.removeLast();

              clonedCollection.add(cloned);
            }

            FieldUtils.writeField(copy, fieldPolicy.getKey().getName(), clonedCollection, true);

          } else if (value.isObject()) {

            var field = getDeclaredFieldRecursive(copy.getClass(), fieldPolicy.getKey().getName());
            var cloned = field.getType().newInstance();

            breadcrumb.addLast(finalResultCollector[0]);

            cloned = applyImportPolicy(ctx, cloned, cloned.getClass(), fieldPolicy.getValue(),
                value, finalResultCollector[0], breadcrumb);

            breadcrumb.removeLast();

            FieldUtils.writeField(copy, fieldPolicy.getKey().getName(), cloned, true);

          } else if (value.isValueNode()) {

            throw new BadConfigurationException(
                "Unexpected clone on value node: " + value.toString());

          } else {
            throw new InternalServerException("Unexpected node: " + value.toString());
          }

        } else if (fieldPolicy.getValue().build) {

          var builder = fieldPolicy.getValue().builder;

          var buildContext = new FieldValueBuilderContext();
          buildContext.source = input;
          buildContext.target = copy;
          buildContext.fieldName = fieldPolicy.getKey().getName();
          buildContext.fieldPolicy = fieldPolicy.getValue();
          buildContext.parentTarget = parentTarget;

          var builtValue = builder.apply(buildContext);

          FieldUtils.writeField(copy, fieldPolicy.getKey().getName(), builtValue, true);
        }
        return null;
      };
    }

    var ec = new ExecutionCandidate();
    importExecutors.add(ec);
    ec.policy = policy;
    ec.order = 10000;
    ec.callable = () -> {
      finalResultCollector[0] = persist(ctx, input, copy, ec, breadcrumb);
      return null;
    };

    // sort execution candidates for order
    importExecutors.stream().sorted((e1, e2) -> e1.order - e2.order).forEach(executor -> {
      try {
        executor.callable.call();
      } catch (Exception e) {
        throw ExceptionUtils.toChecked(e);
      }
    });

    return (T) finalResultCollector[0];
  }

  private Field findAndValidatePrimaryKeyField(Object pojo) {
    // check primary key
    for (Field field : pojo.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)
          || field.isAnnotationPresent(org.springframework.data.annotation.Id.class)) {
        // this field is the primary key
        return field;
      }
    }
    return null;
  }

  private Object getAndValidatePrimaryKeyValue(Object pojo, Field field) {
    if (field == null) {
      return null;
    }

    Object fieldValue;
    try {
      fieldValue = FieldUtils.readField(field, pojo, true);
    } catch (IllegalAccessException e) {
      throw new InternalServerException("could not read primary key field", e);
    }

    // check that there are no conflicts with the primary key and its value (if present)
    var key = pojo.getClass().getName() + "[" + ObjectUtils.toJson(fieldValue) + "]";

    var isGenerated = field.isAnnotationPresent(GeneratedValue.class);
    if (isGenerated && fieldValue != null) {
      throw new InternalServerException(
          "Generated PK value on " + key + " should be null and has value instead");
    } else if (!isGenerated && fieldValue == null) {
      throw new InternalServerException(
          "Non-generated PK value should be provided and is null instead on " + key);
    }

    return fieldValue;
  }

  private Object persist(ImportContext ctx, JsonNode input, Object pojo, ExecutionCandidate ec,
      LinkedList<Object> breadcrumb) {
    final var method = "persist";
    if (pojo == null || !pojo.getClass().isAnnotationPresent(Entity.class)) {
      return pojo;
    }

    var ccc = new EntityPersistContext();
    ccc.executionCandidate = ec;
    ccc.breadcrumb = breadcrumb;
    ccc.target = pojo;
    ccc.input = input;
    ccc.importContext = ctx;

    if (ec.policy.beforePersistHook != null) {
      var hookResult = ec.policy.beforePersistHook.apply(ccc);
      if (hookResult != null && hookResult.abort != null && hookResult.abort.booleanValue()) {
        return null;
      }
    }

    // find primary key field and read its value (if present)
    Field[] pkFieldContainer = new Field[] {findAndValidatePrimaryKeyField(pojo)};
    Object pkValue = getAndValidatePrimaryKeyValue(pojo, pkFieldContainer[0]);

    Object entityThatExistedAlready = null;
    Object importCandidate = pojo;
    Object finalEntity = pojo;

    // define a routine to extract a unique string identifier for the object
    Function<Object, String> getKey = (Object subject) -> {
      String ckey = null;

      if (ec.policy.uniqueIdentifierProvider != null) {

        var ccc2 = new EntityPersistContext();
        ccc2.executionCandidate = ec;
        ccc2.breadcrumb = breadcrumb;
        ccc2.target = subject;
        ccc2.input = input;
        ccc2.importContext = ctx;

        String pkFieldValue = ec.policy.uniqueIdentifierProvider.apply(ccc2);
        if (StringUtils.isBlank(pkFieldValue)) {
          throw new BadConfigurationException(
              "uniqueIdentifierProvider cannot return a blank output");
        }
        ckey = subject.getClass().getSimpleName() + ":" + ObjectUtils.toJson(pkFieldValue);

      } else {

        // unproxy and extract pkValue
        subject = initializeAndUnproxy(subject);
        if (subject != null) {
          Object pkFieldValue;
          try {
            pkFieldValue = FieldUtils.readField(pkFieldContainer[0], subject, true);
          } catch (IllegalAccessException e) {
            throw new InternalServerException("could not read primary key field value for entity",
                e);
          }

          if (pkFieldValue != null) {
            ckey = subject.getClass().getSimpleName() + ":" + ObjectUtils.toJson(pkFieldValue);
          } else {
            throw new InternalServerException("could not build full qualifier for entity");
          }
        }
      }

      return ckey;
    };

    // extract initial object identifier
    var key = getKey.apply(importCandidate);

    logger.debug(method, "requesting persist for: " + key);

    // if a prePersistRequest hook exists on the policy, call it now
    if (ec.policy.prePersistRequest != null) {
      ec.policy.prePersistRequest.accept(ccc);
    }

    if (ec.policy.conflictChecker != null) {
      // if a conflictChecker exists on the policy, run it now instead of checking with the primary
      // key

      entityThatExistedAlready = initializeAndUnproxy(ec.policy.conflictChecker.apply(ccc));

    } else if (pkValue != null) {
      // if the pk value is not null, check if the object exists already

      logger.debug(method, "checking for existence by primary key: " + key);
      entityThatExistedAlready =
          initializeAndUnproxy(entityManager.find(importCandidate.getClass(), pkValue));
    }

    // now check if the object is fresh or if an existing version was found
    if (entityThatExistedAlready != null) {
      logger.debug(method, "ALREADY EXISTS: " + key);

      // if an existing version was found we will return it in output
      finalEntity = entityThatExistedAlready;
      ccc.target = finalEntity;

      // recompute the key
      key = getKey.apply(entityThatExistedAlready);

      // CHECK FOR CONFLICTING FIELDS
      var exactlyTheSame = true;
      var someUpdateLaunched = false;
      var someUnsolved = false;

      for (var ecPolicyFieldPolicy : ec.policy.fieldsPolicy.entrySet()) {
        var conflictContext = checkIfConflicting(ecPolicyFieldPolicy.getValue(),
            ecPolicyFieldPolicy.getKey().getName(), entityThatExistedAlready, importCandidate, key);

        if (conflictContext != null) {
          // there's a conflict on the field.
          exactlyTheSame = false;

          // check if conflict is resolved from input
          var resolution = ctx.conflictInput.getOrDefault(conflictContext.fullKey, null);
          if (resolution == null) {
            // the conflict might be autoresolvable.
            resolution = checkIfConflictCanBeAutoResolved(conflictContext);
            if (resolution != null) {
              if (ctx.preview) {
                ctx.success("L'entita' " + key + " ha un conflitto sul campo "
                    + conflictContext.fieldName + " che verra' pero' risolto automaticamente.");
              } else {
                ctx.success("L'entita' " + key + " aveva un conflitto sul campo "
                    + conflictContext.fieldName + " ma e' stato risolto automaticamente.");
              }
            }
          }

          if (resolution != null) {
            if (applyConflictResolution(ctx, conflictContext, resolution)) {
              someUpdateLaunched = true;
            }

          } else {
            // if not resolved add to pending conflicts
            ctx.addToPendingConflicts(conflictContext);
            someUnsolved = true;
          }
        }
      }

      // recompute the key
      key = getKey.apply(entityThatExistedAlready);

      if (ctx.preview) {
        if (exactlyTheSame) {
          ctx.info("L'entita' " + key
              + " non verra' importata perche' esiste gia' ed e' identica allo stato desiderato.");
        } else {
          if (someUnsolved) {
            ctx.warning("L'entita' " + key
                + " esiste gia' ma NON e' identica allo stato desiderato. I conflitti andranno risolti manualmente.");
          } else {
            ctx.success("L'entita' " + key
                + " esiste gia' ma NON e' identica allo stato desiderato. Per tutti i conflitti e' stata fornita una soluzione.");
          }
        }
      }

      if (someUpdateLaunched) {
        // LAUNCH ENTITY UPDATE
        entityManager.merge(entityThatExistedAlready);
        entityManager.flush();

        if (ctx.preview) {
          ctx.success("L'entita' " + key
              + " verra' aggiornata sovrascrivendo alcuni campi come da indicazioni per la risoluzione dei conflitti.");
        } else {
          ctx.success("L'entita' " + key
              + " e' stata aggiornata. Alcuni campi sono stati sovrascritti come da indicazioni per la risoluzione dei conflitti.");
        }

      } else if (!ctx.preview) {
        ctx.info("L'entita' " + key
            + " NON e' stata aggiornata. Tutti i campi sono rimasti allo stesso valore che avevano prima dell'importazione.");
      }

    } else {
      if (ec.policy.prePersistExecution != null) {
        ec.policy.prePersistExecution.accept(ccc);
      }

      // LAUNCH ENTITY PERSIST
      entityManager.persist(finalEntity);
      entityManager.flush();

      // recompute entity key (insert may validate the field)
      key = getKey.apply(finalEntity);

      if (ctx.preview) {
        ctx.success("L'entita' " + key + " verra' creata e salvata.");
      } else {
        ctx.success("L'entita' " + key + " e' stata creata e salvata.");
      }
    }

    return finalEntity;
  }

  private FieldConflictContext checkIfConflicting(ImportPolicy<?> policyValue, String fieldName,
      Object existing, Object importing, String entityKey) {
    if (policyValue.ignore || policyValue.clone) {
      return null;
    }

    try {
      var existingField = getDeclaredFieldRecursive(existing.getClass(), fieldName);
      var existingValue = FieldUtils.readField(existingField, existing, true);

      var importingField = getDeclaredFieldRecursive(importing.getClass(), fieldName);
      var importingValue = FieldUtils.readField(importingField, importing, true);

      if (!checkEquality(existingValue, importingValue)) {

        var conflictContext = new FieldConflictContext();
        conflictContext.existingValue = existingValue;
        conflictContext.fieldName = fieldName;
        conflictContext.fullKey = entityKey + "." + fieldName;
        conflictContext.importValue = importingValue;
        conflictContext.path = entityKey;
        conflictContext.target = existing;
        conflictContext.targetField = existingField;
        conflictContext.message = "CONFLICTING value on " + entityKey + " for field " + fieldName
            + ": existing [" + ObjectUtils.represent(conflictContext.existingValue)
            + "] differs from importing [" + ObjectUtils.represent(conflictContext.importValue)
            + "]";

        return conflictContext;
      }
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }

    return null;
  }

  private FieldConflictResolutionInput checkIfConflictCanBeAutoResolved(
      FieldConflictContext conflict) {

    if ((conflict.fieldName.equals(CosmoCEntity_.dtInizioVal.getName())
        || conflict.fieldName.equals(CosmoTEntity_.dtInserimento.getName()))
        && conflict.existingValue instanceof Timestamp
        && conflict.importValue instanceof Timestamp) {

      var resolution = new FieldConflictResolutionInput();
      resolution.setAutomatic(Boolean.TRUE);
      resolution.setFullKey(conflict.fullKey);
      resolution.setAcceptedValue(null);

      // take the earliest value
      var v1 = (Timestamp) conflict.existingValue;
      var v2 = (Timestamp) conflict.importValue;

      if (v1.before(v2)) {
        resolution.setAction(FieldConflictResolutionInputAction.IGNORE);
      } else {
        resolution.setAction(FieldConflictResolutionInputAction.OVERWRITE);
      }

      return resolution;

    } else if ((conflict.fieldName.equals(CosmoTEntity_.dtUltimaModifica.getName()))
        && conflict.existingValue instanceof Timestamp
        && conflict.importValue instanceof Timestamp) {

      // take the latest value
      var resolution = new FieldConflictResolutionInput();
      resolution.setAutomatic(Boolean.TRUE);
      resolution.setFullKey(conflict.fullKey);
      resolution.setAcceptedValue(null);

      // take the earliest value
      var v1 = (Timestamp) conflict.existingValue;
      var v2 = (Timestamp) conflict.importValue;

      if (v1.after(v2)) {
        resolution.setAction(FieldConflictResolutionInputAction.IGNORE);
      } else {
        resolution.setAction(FieldConflictResolutionInputAction.OVERWRITE);
      }

      return resolution;

    }

    return null;
  }

  private boolean applyConflictResolution(ImportContext ctx, FieldConflictContext conflictContext,
      FieldConflictResolutionInput resolution) {
    final var method = "applyConflictResolution";
    String solvedAs = Boolean.TRUE.equals(resolution.getAutomatic())
        ? "come deciso dalle politiche di risoluzione automatiche dei conflitti noti"
            : "come specificato per la risoluzione del conflitto";

    // resolved, apply resolution according to action
    if (resolution.getAction() == FieldConflictResolutionInputAction.OVERWRITE) {

      logger.debug(method,
          "applying resolution action: OVERWRITE on field " + conflictContext.fullKey);

      if (resolution.getAcceptedValue() != null) {
        // check that it sill matches the input "acceptedValue"
        var valA = ObjectUtils.fromJson(resolution.getAcceptedValue(), JsonNode.class);
        var valB =
            ObjectUtils.fromJson(ObjectUtils.toJson(conflictContext.importValue), JsonNode.class);
        if (!valA.equals(valB)) {
          throw new ConflictException(
              "obsolete or badly constructed overwrite resolution action: current value [" + valB
              + "] differs from accepted value [" + valA + "]");
        }
      }

      if (ctx.preview) {
        ctx.success("L'entita' " + conflictContext.fullKey + " ha un conflitto sul campo "
            + conflictContext.fieldName
            + ": il vecchio valore verra' stato sovrascritto dal nuovo, " + solvedAs + ".");

      } else {
        ctx.success("L'entita' " + conflictContext.fullKey + " aveva un conflitto sul campo "
            + conflictContext.fieldName + ": il vecchio valore e' stato sovrascritto dal nuovo, "
            + solvedAs + ".");
      }

      try {
        FieldUtils.writeField(conflictContext.targetField, conflictContext.target,
            conflictContext.importValue, true);
        return true;

      } catch (IllegalAccessException e) {
        throw new InternalServerException("could not write field for conflict resolution", e);
      }

    } else if (resolution.getAction() == FieldConflictResolutionInputAction.IGNORE) {

      logger.debug(method,
          "applying resolution action: IGNORE on field " + conflictContext.fullKey);

      if (ctx.preview) {
        ctx.success("L'entita' " + conflictContext.fullKey + " aveva un conflitto sul campo "
            + conflictContext.fieldName + ": il vecchio valore e' stato mantenuto, " + solvedAs
            + ".");
      } else {
        ctx.success("L'entita' " + conflictContext.fullKey + " ha un conflitto sul campo "
            + conflictContext.fieldName + ": il vecchio valore verra' mantenuto, " + solvedAs
            + ".");
      }

    } else {

      throw new BadRequestException("unknown resolution action: " + resolution.getAction());

    }

    return false;
  }

  private Field getDeclaredFieldRecursive(Class<?> clazz, String name) throws NoSuchFieldException {
    NoSuchFieldException e2 = null;
    while (clazz != null) {
      try {
        return clazz.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        e2 = e;
        clazz = clazz.getSuperclass();
      }
    }
    throw e2;
  }

  public Collection<Object> createCollectionFromType(Field field) {
    var fieldType = field.getType();
    if (List.class.isAssignableFrom(fieldType)) {
      return new ArrayList<>();
    }
    if (Set.class.isAssignableFrom(fieldType)) {
      return new HashSet<>();
    }
    throw new RuntimeException("Unexpected collecion class: " + fieldType.getName());
  }

  private boolean isCollection(Class<?> clazz) {
    return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
  }

  private <T> void checkPolicyIsComplete(Class<?> metamodelType, ExportPolicy<T> policy) {
    for (Field field : metamodelType.getDeclaredFields()) {
      var found = false;
      for (Attribute<?, ?> specifiedAttribute : policy.fieldsPolicy.keySet()) {
        if (specifiedAttribute.getName().equals(field.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new RuntimeException(
            "Invalid policy: missing specification for field " + field.getName());
      }
    }
    exportPolicyVerifications.put(policy.uuid, true);
  }

  public <T> T initializeAndUnproxy(T entity) {
    if (entity == null) {
      return null;
    }

    if (entity instanceof HibernateProxy) {
      entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
    }
    return entity;
  }

  private boolean checkEquality(Object o1, Object o2) {
    if (o1 instanceof Timestamp && o2 instanceof Timestamp) {
      var other1 = new Timestamp(1000 * (long) Math.floor(((Timestamp) o1).getTime() / 1000));
      var other2 = new Timestamp(1000 * (long) Math.floor(((Timestamp) o2).getTime() / 1000));
      return other1.equals(other2);
    }

    if (o1 instanceof byte[] && o2 instanceof byte[]) {
      return Arrays.equals((byte[]) o1, (byte[]) o2);
    }

    return Objects.equals(o1, o2);
  }

  private <T> void checkPolicyIsComplete(Class<?> metamodelType, ImportPolicy<T> policy) {
    for (Field field : metamodelType.getDeclaredFields()) {
      var found = false;
      for (Attribute<?, ?> specifiedAttribute : policy.fieldsPolicy.keySet()) {
        if (specifiedAttribute.getName().equals(field.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new RuntimeException(
            "Invalid policy: missing specification for field " + field.getName());
      }
    }
    importPolicyVerifications.put(policy.uuid, true);
  }

  private static class ExportPolicy<T> {
    String uuid = UUID.randomUUID().toString();
    boolean copy;
    boolean clone;
    HashMap<Attribute<?, ?>, ExportPolicy<?>> fieldsPolicy = new HashMap<>();
    BiConsumer<T, Map<String, Object>> preExport = null;

    public static ExportPolicy ignore() {
      ExportPolicy out = new ExportPolicy();
      out.copy = false;
      out.clone = false;
      return out;
    }

    public static ExportPolicy copy() {
      ExportPolicy out = new ExportPolicy();
      out.copy = true;
      out.clone = false;
      return out;
    }

    public static <T> ExportPolicy<T> clone(ExportPolicy<T> withPolicy) {
      if (!exportPolicyVerifications.containsKey(withPolicy.uuid)) {
        throw new BadConfigurationException("Cannot clone unverified policy: " + withPolicy);
      }
      ExportPolicy<T> out = new ExportPolicy<>();
      out.copy = false;
      out.clone = true;
      out.fieldsPolicy.putAll(withPolicy.fieldsPolicy);
      out.preExport = withPolicy.preExport;
      exportPolicyVerifications.put(out.uuid, true);
      return out;
    }

    public ExportPolicy() {
      this.copy = false;
    }

    public ExportPolicy<T> withPreExport(BiConsumer<T, Map<String, Object>> preExport) {
      this.preExport = preExport;
      return this;
    }
  }

  private static class ImportPolicy<T> {
    String uuid = UUID.randomUUID().toString();
    boolean ignore;
    boolean copy;
    boolean clone;
    boolean build;
    int order = 1000;
    HashMap<Attribute<?, ?>, ImportPolicy<?>> fieldsPolicy = new HashMap<>();
    Function<FieldValueBuilderContext, T> builder;
    Function<EntityPersistContext, T> conflictChecker;
    Consumer<EntityPersistContext> prePersistRequest;
    Consumer<EntityPersistContext> prePersistExecution;
    Function<EntityPersistContext, String> uniqueIdentifierProvider;
    Function<EntityPersistContext, HookResult> beforePersistHook;

    public static <X> ImportPolicy<X> builder(Function<FieldValueBuilderContext, X> builder) {
      ImportPolicy<X> out = new ImportPolicy<>();
      out.copy = false;
      out.clone = false;
      out.build = true;
      out.builder = builder;
      out.ignore = false;
      return out;
    }

    public static ImportPolicy ignore() {
      ImportPolicy out = new ImportPolicy();
      out.copy = false;
      out.clone = false;
      out.build = false;
      out.ignore = true;
      return out;
    }

    public static ImportPolicy copy() {
      ImportPolicy out = new ImportPolicy();
      out.ignore = false;
      out.copy = true;
      out.clone = false;
      out.build = false;
      return out;
    }

    public static <T> ImportPolicy<T> clone(ImportPolicy<T> withPolicy) {
      if (!importPolicyVerifications.containsKey(withPolicy.uuid)) {
        throw new RuntimeException("Cannot clone unverified policy: " + withPolicy);
      }
      ImportPolicy<T> out = new ImportPolicy<>();
      out.ignore = false;
      out.copy = false;
      out.clone = true;
      out.build = false;
      out.fieldsPolicy.putAll(withPolicy.fieldsPolicy);
      importPolicyVerifications.put(out.uuid, true);
      out.builder = withPolicy.builder;
      out.conflictChecker = withPolicy.conflictChecker;
      out.prePersistExecution = withPolicy.prePersistExecution;
      out.prePersistRequest = withPolicy.prePersistRequest;
      out.uniqueIdentifierProvider = withPolicy.uniqueIdentifierProvider;
      out.beforePersistHook = withPolicy.beforePersistHook;
      return out;
    }

    public ImportPolicy() {
      this.copy = false;
    }

    public ImportPolicy<T> withOrder(int order) {
      this.order = order;
      return this;
    }

    public ImportPolicy<T> withConflictChecker(Function<EntityPersistContext, T> conflictChecker) {
      this.conflictChecker = conflictChecker;
      return this;
    }

    public ImportPolicy<T> withPrePersistRequest(Consumer<EntityPersistContext> prePersistRequest) {
      this.prePersistRequest = prePersistRequest;
      return this;
    }

    public ImportPolicy<T> withPrePersistExecution(
        Consumer<EntityPersistContext> prePersistExecution) {
      this.prePersistExecution = prePersistExecution;
      return this;
    }

    public ImportPolicy<T> withUniqueIdentifierProvider(
        Function<EntityPersistContext, String> uniqueIdentifierProvider) {
      this.uniqueIdentifierProvider = uniqueIdentifierProvider;
      return this;
    }

    public ImportPolicy<T> withBeforePersistHook(
        Function<EntityPersistContext, HookResult> beforePersistHook) {
      this.beforePersistHook = beforePersistHook;
      return this;
    }
  }

  private static class HookResult {
    protected Boolean abort;
  }

  private static class ExecutionCandidate {
    protected int order;
    protected Callable<?> callable;
    protected ImportPolicy<?> policy;
  }

  private static class EntityPersistContext {
    ImportContext importContext;
    Object target;
    ExecutionCandidate executionCandidate;
    LinkedList<Object> breadcrumb;
    JsonNode input;
  }

  private static class FieldValueBuilderContext {

    public Object parentTarget;
    public ImportPolicy<?> fieldPolicy;
    public String fieldName;
    public Object target;
    public JsonNode source;

  }

  private static class FieldConflictContext {

    public String message;

    public String fieldName;

    public Object existingValue;

    public Object importValue;

    public String path;

    public String fullKey;

    public Object target;

    public Field targetField;
  }

  private static class FieldConflict {

    public String message;

    public String fieldName;

    public String existingValue;

    public String importValue;

    public String path;

    public String fullKey;
  }

  private static class ImportContext {

    protected boolean preview;

    protected String tenantId;

    protected String codiceTipoPratica;

    protected Map<String, FieldConflictResolutionInput> conflictInput;

    protected List<FieldConflict> pendingConflicts;

    protected List<MessaggioImportazione> messages;

    protected void addToPendingConflicts(FieldConflictContext conflictContext) {

      var conflictObj = new FieldConflict();
      conflictObj.existingValue = ObjectUtils.toJson(conflictContext.existingValue);
      conflictObj.importValue = ObjectUtils.toJson(conflictContext.importValue);
      conflictObj.fieldName = conflictContext.fieldName;
      conflictObj.message = conflictContext.message;
      conflictObj.path = conflictContext.path;
      conflictObj.fullKey = conflictContext.fullKey;

      this.pendingConflicts.add(conflictObj);
    }

    protected void debug(String text) {
      this.messages.add(new MessaggioImportazione(LivelloMessaggio.DEBUG, text));
    }

    protected void info(String text) {
      this.messages.add(new MessaggioImportazione(LivelloMessaggio.INFO, text));
    }

    protected void success(String text) {
      this.messages.add(new MessaggioImportazione(LivelloMessaggio.SUCCESS, text));
    }

    protected void warning(String text) {
      this.messages.add(new MessaggioImportazione(LivelloMessaggio.WARNING, text));
    }

    protected void danger(String text) {
      this.messages.add(new MessaggioImportazione(LivelloMessaggio.DANGER, text));
    }
  }
}
