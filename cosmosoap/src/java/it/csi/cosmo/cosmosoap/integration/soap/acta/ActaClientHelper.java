/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.acta;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaAccessDeniedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaContextBuildingException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaQueryFailedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaTooManyResultsException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.archive.AcarisRepositoryEntryType;
import it.doqui.acta.acaris.backoffice.ClientApplicationInfo;
import it.doqui.acta.acaris.backoffice.PrincipalExtResponseType;
import it.doqui.acta.acaris.common.AcarisFaultType;
import it.doqui.acta.acaris.common.ChangeTokenType;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.EnumPropertyFilter;
import it.doqui.acta.acaris.common.EnumQueryOperator;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertyFilterType;
import it.doqui.acta.acaris.common.QueryConditionType;
import it.doqui.acta.acaris.common.QueryNameType;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


public abstract class ActaClientHelper {

  private ActaClientHelper() {
    // private
  }

  public static final int CONSERVAZIONE_INFINITA = 99;

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaClientHelper.class.getSimpleName());

  private static final DateTimeFormatter FORMATTER_CHANGE_TOKEN =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private static ObjectMapper mapper = null;

  private static ObjectMapper getMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    return mapper;
  }

  static RuntimeException wrapException(Throwable e, String operationDescription) {
    return wrapException(e, operationDescription,
        e2 -> new ActaQueryFailedException("Query failed for operation " + operationDescription,
            e2));
  }

  static RuntimeException wrapException(Throwable e, String operationDescription,
      Function<Throwable, RuntimeException> wrapper) {
    logFault(operationDescription, e);

    var fault = getFault(e);

    if (fault != null) {
      if (fault.getErrorCode().equals("SERQRY-E017")) {
        return new ActaTooManyResultsException(
            "Too many results for operation: " + operationDescription, e);
      }
      if (fault.getErrorCode().equals("SERNAV-E004")) {
        return new ActaAccessDeniedException("Access denied for operation: " + operationDescription,
            e);
      }
    }

    if (e instanceof RuntimeException) {
      return ((RuntimeException) e);
    }

    return wrapper.apply(e);
  }

  static void logFault(Throwable e) {
    logFault("ActaClientExecutor", e);
  }

  static AcarisFaultType getFault(Throwable e) {

    if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.management.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.management.AcarisException) e)
          .getFaultInfo();
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.document.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.document.AcarisException) e)
          .getFaultInfo();
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.backoffice.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.backoffice.AcarisException) e)
          .getFaultInfo();
      // } else if (e instanceof it.doqui.acta.acaris.multifilingservice.AcarisException) {
      // logAcarisFault(nomeMetodo,
      // ((it.doqui.acta.acaris.multifilingservice.AcarisException) e).getFaultInfo());
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.AcarisException) e)
          .getFaultInfo();
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.object.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.object.AcarisException) e)
          .getFaultInfo();
      // } else if (e instanceof it.doqui.acta.acaris.officialbookservice.AcarisException) {
      // logAcarisFault(nomeMetodo,
      // ((it.doqui.acta.acaris.officialbookservice.AcarisException) e).getFaultInfo());
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.relationships.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.relationships.AcarisException) e)
          .getFaultInfo();
    } else if (e instanceof it.csi.cosmo.cosmosoap.integration.soap.acta.repository.AcarisException) {
      return ((it.csi.cosmo.cosmosoap.integration.soap.acta.repository.AcarisException) e)
          .getFaultInfo();
      // } else if (e instanceof it.doqui.acta.acaris.smsservice.AcarisException) {
      // logAcarisFault(nomeMetodo,
      // ((it.doqui.acta.acaris.smsservice.AcarisException) e).getFaultInfo());
      // } else if (e instanceof it.doqui.acta.acaris.subjectregistryservice.AcarisException) {
      // logAcarisFault(nomeMetodo,
      // ((it.doqui.acta.acaris.subjectregistryservice.AcarisException) e).getFaultInfo());
    }

    return null;
  }

  static void logFault(String nomeMetodo, Throwable e) {
    log.error("logFault",
        "[ActaAdapter::" + nomeMetodo + "]  GenericFault - Error details: " + e.getMessage(), e);
    var fault = getFault(e);
    if (fault != null) {
      logAcarisFault(nomeMetodo, fault);
    }
  }

  static void logAcarisFault(String nomeMetodo, AcarisFaultType fault) {
    log.error("logAcarisFault",
        "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Error code: " + fault.getErrorCode());
    log.error("logAcarisFault", "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Property name: "
        + fault.getPropertyName());
    log.error("logAcarisFault", "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Technical info: "
        + fault.getTechnicalInfo());
    log.error("logAcarisFault",
        "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Class name: " + fault.getClassName());
    log.error("logAcarisFault", "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Exception type: "
        + fault.getExceptionType());
    log.error("logAcarisFault",
        "[ActaAdapter::" + nomeMetodo + "]  AcarisFault - Object id: " + fault.getObjectId());
  }

  static void logObject(String header, Object o) {
    if (!log.isDebugEnabled()) {
      return;
    }

    try {
      log.debug("logObject", "[ACTA adapter] " + header + ": " + getMapper().writeValueAsString(o));
    } catch (IOException e) {
      log.trace("logObject",
          "[ACTA adapter] " + header + ": <CANNOT_REPRESENT> (" + e.getClass().getName() + ")");
      log.error("logObject", "representation error", e);
    }
  }

  /**
   * Crea un contesto per effettuare le operazioni su ACTA.
   *
   * @param params Parametri da usare per la creazione del contesto.
   * @return Un oggetto che rappresenta il contesto delle operazioni su ACTA.
   * @throws ActaContextBuildingException In caso di errore.
   */
  static ActaClientContext loadContext(ActaProvider facade, String repositoryName,
      String codiceProtocollista, String codiceStruttura, String codiceAreaOrganizzativaOmogenea,
      String codiceNodo, String codiceApplicazione) throws ActaContextBuildingException {

    if (repositoryName == null || "".equals(repositoryName)) {
      throw new ActaContextBuildingException("Il repository e' obbligatorio.");
    }
    if (codiceProtocollista == null || "".equals(codiceProtocollista)) {
      throw new ActaContextBuildingException("Il protocollista e' obbligatorio.");
    }

    try {
      String repositoryId = null;

      // prelievo del repositoryId con aggiornamento del valore sui parametri applicativi
      if (repositoryId == null || "".equals(repositoryId)) {
        log.info("creaContesto",
            "RepositoryId non presente nei parametri: verra' cercato su Acta con il nome '"
                + repositoryName + "'");
        List<AcarisRepositoryEntryType> repositories =
            facade.getRepositoryService().getRepositories();
        for (AcarisRepositoryEntryType repos : repositories) {
          if (repos.getRepositoryName().equals(repositoryName)) {
            repositoryId = repos.getRepositoryId().getValue();
            break;
          }
        }
      }

      log.info("loadContext", "creaContesto: findto il repositoryId: " + repositoryId);
      if (repositoryId == null) {
        throw new ActaContextBuildingException("Repository ID non trovato");
      }

      // prelievo dell'elenco dei VitalRecordCode
      ObjectIdType repository = new ObjectIdType();
      repository.setValue(repositoryId);
      List<VitalRecordCodeType> vrcs = facade.getManagementService().getVitalRecordCode(repository);

      log.info("loadContext", "creaContesto: findto l'elenco dei VitalRecordCode: " + vrcs.size());

      // preparazione dell'oggetto da restituire
      ActaClientContext contesto = new ActaClientContext();
      contesto.setRepository(repositoryId);
      contesto.setUtente(null); // valorizzato in seguito
      if (!StringUtils.isBlank(codiceStruttura)) {
        contesto.setStruttura(Long.valueOf(codiceStruttura));
      }
      if (!StringUtils.isBlank(codiceAreaOrganizzativaOmogenea)) {
        contesto.setAoo(Long.valueOf(codiceAreaOrganizzativaOmogenea));
      }
      if (!StringUtils.isBlank(codiceNodo)) {
        contesto.setNodo(Long.valueOf(codiceNodo));
      }
      contesto.setVitalRecordCodes(vrcs.toArray(new VitalRecordCodeType[0]));
      contesto.setCodiceProtocollista(codiceProtocollista);
      contesto.setApplicationInfo(codiceApplicazione);

      if (!StringUtils.isBlank(codiceNodo) || !StringUtils.isBlank(codiceStruttura)
          || !StringUtils.isBlank(codiceAreaOrganizzativaOmogenea)) {
        getValidPrincipal(facade, contesto);
      }

      log.info("loadContext", "creaContesto: Contesto creato: " + contesto);

      ActaClientHelper.logObject("contesto di ACTA", contesto);

      return contesto;
    } catch (it.csi.cosmo.cosmosoap.integration.soap.acta.management.AcarisException e) {
      log.error("loadContext", "creaContesto: Impossibile creare il contesto: " + e, e);
      ActaClientHelper.logAcarisFault("creaContesto", e.getFaultInfo());
      throw new ActaContextBuildingException("Impossibile creare il contesto: " + e, e);
    } catch (it.csi.cosmo.cosmosoap.integration.soap.acta.repository.AcarisException e) {
      log.error("loadContext", "creaContesto: Impossibile creare il contesto: " + e, e);
      ActaClientHelper.logAcarisFault("creaContesto", e.getFaultInfo());
      throw new ActaContextBuildingException("Impossibile creare il contesto: " + e, e);
    } catch (Exception e) {
      log.error("loadContext", "creaContesto: Impossibile creare il contesto: " + e, e);
      throw new ActaContextBuildingException("Impossibile creare il contesto: " + e, e);
    }
  }

  public static String getValidPrincipal(ActaProvider facade, ActaClientContext contesto)
      throws ActaContextBuildingException {
    LocalDateTime now = LocalDateTime.now();

    if (contesto == null) {
      throw new RuntimeException("Context is missing");
    }

    if (contesto != null && contesto.getUtente() != null
        && contesto.getScadenzaUtente().isAfter(now)) {
      log.trace("getValidPrincipal", "cache hit: utente from contesto still valid");
      return contesto.getUtente();
    }

    log.trace("getValidPrincipal", "cache miss: utente from contesto missing or expired at " + now);

    // prelievo del principal (N.B. il principal cosi' ottenuto scade dopo 30 min)
    ObjectIdType repository = new ObjectIdType();
    repository.setValue(contesto.getRepository());
    CodiceFiscaleType codiceFiscale = new CodiceFiscaleType();
    codiceFiscale.setValue(contesto.getCodiceProtocollista());
    IdAOOType aoo = new IdAOOType();
    aoo.setValue(new Long(contesto.getAoo()));
    IdStrutturaType struttura = new IdStrutturaType();
    struttura.setValue(new Long(contesto.getStruttura()));
    IdNodoType nodo = new IdNodoType();
    nodo.setValue(new Long(contesto.getNodo()));
    ClientApplicationInfo applInfo = new ClientApplicationInfo();
    applInfo.setAppKey(contesto.getApplicationInfo());

    List<PrincipalExtResponseType> principals;
    try {
      principals = facade.getBackofficeService().getPrincipalExt(repository, codiceFiscale, aoo,
          struttura, nodo, applInfo);
    } catch (Exception e) {
      throw new ActaContextBuildingException("Error getting valid ACTA principal", e);
    }

    ActaClientHelper.logObject("loaded principals", principals);
    String principalId = principals.get(0).getPrincipalId().getValue();

    contesto.setUtente(principalId);
    contesto.setScadenzaUtente(now.plus(25, ChronoUnit.MINUTES));
    log.info("getValidPrincipal",
        "creato nuovo principal con scadenza a " + contesto.getScadenzaUtente());

    return principalId;
  }

  public static ObjectIdType wrapId(String raw) {
    ObjectIdType wrapper = new ObjectIdType();
    wrapper.setValue(raw);
    return wrapper;
  }

  static PrincipalIdType wrapPrincipal(String raw) {
    PrincipalIdType wrapper = new PrincipalIdType();
    wrapper.setValue(raw);
    return wrapper;
  }

  static ChangeTokenType getChangeToken(EntitaActa entita) {
    ChangeTokenType output = new ChangeTokenType();

    // DESIRED 2019-06-10 12:27:21.000
    if (entita.getChangeToken() != null) {
      output.setValue(entita.getChangeToken().format(FORMATTER_CHANGE_TOKEN));
    } else {
      // output.setValue ( LocalDateTime.now ().format ( FORMATTER_CHANGE_TOKEN ) );
    }

    return output;
  }

  public static List<QueryConditionType> getCriteria(EnumQueryOperator[] operator,
      String[] propertyName, String[] value) {
    List<QueryConditionType> criteria = new LinkedList<>();
    if ((operator != null && operator.length > 0)
        && (propertyName != null && propertyName.length > 0) && (value != null && value.length > 0)
        && (operator.length == propertyName.length && operator.length == value.length)) {
      List<QueryConditionType> criteri = new ArrayList<QueryConditionType>();
      QueryConditionType criterio = null;
      for (int i = 0; i < propertyName.length; i++) {
        criterio = new QueryConditionType();
        criterio.setOperator(operator[i]);
        criterio.setPropertyName(propertyName[i]);
        criterio.setValue(value[i]);
        criteri.add(criterio);
      }
      for (QueryConditionType c : criteri) {
        criteria.add(c);
      }
    }
    return criteria;
  }

  public static PropertyFilterType getPropertyFilterNone() {
    return getPropertyFilter(EnumPropertyFilter.NONE, null, null, null);
  }

  public static PropertyFilterType getPropertyFilterAll() {
    return getPropertyFilter(EnumPropertyFilter.ALL, null, null, null);
  }

  public static PropertyFilterType getPropertyFilterList(String[] className, String[] propertyName,
      PropertyFilterType prevFilter) {
    return getPropertyFilter(EnumPropertyFilter.LIST, className, propertyName, prevFilter);
  }

  public static PropertyFilterType getPropertyFilter(EnumPropertyFilter type, String[] className,
      String[] propertyName, PropertyFilterType prevFilter) {
    PropertyFilterType filter = null;
    if (type != null) {
      if (type.value().equals(EnumPropertyFilter.LIST.value())) {
        filter = (prevFilter != null) ? prevFilter : new PropertyFilterType();
        filter.setFilterType(type);
        List<QueryNameType> properties = new ArrayList<QueryNameType>();
        QueryNameType property = null;
        if (className.length == propertyName.length) {
          if (prevFilter != null
              && prevFilter.getFilterType().value().equals(EnumPropertyFilter.LIST.value())
              && prevFilter.getPropertyList().size() > 0) {
            for (int j = 0; j < prevFilter.getPropertyList().size(); j++) {
              properties.add(prevFilter.getPropertyList().get(j));
            }
          }
          for (int i = 0; i < propertyName.length; i++) {
            property = new QueryNameType();
            property.setClassName(className[i]);
            property.setPropertyName(propertyName[i]);
            properties.add(property);
          }
          for (QueryNameType p : properties) {
            filter.getPropertyList().add(p);
          }

        } else
          return null;

      } else {
        filter = new PropertyFilterType();
        filter.setFilterType(type);
      }
    }
    return filter;
  }

  public static List<QueryConditionType> criteriaFromFilters(FiltroRicercaDocumentiActaDTO filtri) {

    List<QueryConditionType> criteria = new LinkedList<>();

    if (filtri == null) {
      return criteria;
    }

    if (filtri.getAooProtocollante() != null) {
      if (!StringUtils.isBlank(filtri.getAooProtocollante().getLike())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LIKE);
        criterio.setPropertyName("codiceAooProtocollante");
        criterio.setValue(filtri.getAooProtocollante().getLike());
        criteria.add(criterio);
      }
      if (!StringUtils.isBlank(filtri.getAooProtocollante().getEquals())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.EQUALS);
        criterio.setPropertyName("codiceAooProtocollante");
        criterio.setValue(filtri.getAooProtocollante().getEquals());
        criteria.add(criterio);
      }
    }
    if (filtri.getNumeroProtocollo() != null) {
      if (!StringUtils.isBlank(filtri.getNumeroProtocollo().getLike())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LIKE);
        criterio.setPropertyName("codice");
        criterio.setValue(filtri.getNumeroProtocollo().getLike());
        criteria.add(criterio);
      }
      if (!StringUtils.isBlank(filtri.getNumeroProtocollo().getEquals())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.EQUALS);
        criterio.setPropertyName("codice");
        criterio.setValue(filtri.getNumeroProtocollo().getEquals());
        criteria.add(criterio);
      }
    }
    if (filtri.getNumeroRepertorio() != null) {
      if (!StringUtils.isBlank(filtri.getNumeroRepertorio().getLike())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LIKE);
        criterio.setPropertyName("numRepertorio");
        criterio.setValue(filtri.getNumeroRepertorio().getLike());
        criteria.add(criterio);
      }
    }
    if (filtri.getOggetto() != null) {
      if (!StringUtils.isBlank(filtri.getOggetto().getLike())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LIKE);
        criterio.setPropertyName("oggetto");
        criterio.setValue(filtri.getOggetto().getLike());
        criteria.add(criterio);
      }
    }
    if (filtri.getParoleChiave() != null) {
      if (!StringUtils.isBlank(filtri.getParoleChiave().getLike())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LIKE);
        criterio.setPropertyName("paroleChiave");
        criterio.setValue(filtri.getParoleChiave().getLike());
        criteria.add(criterio);
      }
    }
    if (filtri.getDataCronica() != null) {
      if (null != (filtri.getDataCronica().getGreaterThanOrEqualTo())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.GREATER_THAN_OR_EQUAL_TO);
        criterio.setPropertyName("dataDocCronica");
        criterio.setValue(filtri.getDataCronica().getGreaterThanOrEqualTo()
            .format(DateTimeFormatter.ISO_LOCAL_DATE));
        criteria.add(criterio);
      }
      if (null != (filtri.getDataCronica().getLessThanOrEqualTo())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LESS_THAN_OR_EQUAL_TO);
        criterio.setPropertyName("dataDocCronica");
        criterio.setValue(filtri.getDataCronica().getLessThanOrEqualTo()
            .format(DateTimeFormatter.ISO_LOCAL_DATE));
        criteria.add(criterio);
      }
    }
    if (filtri.getDataRegistrazioneProtocollo() != null) {
      if (null != (filtri.getDataRegistrazioneProtocollo().getGreaterThanOrEqualTo())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.GREATER_THAN_OR_EQUAL_TO);
        criterio.setPropertyName("dataProtocollo");
        criterio.setValue(filtri.getDataRegistrazioneProtocollo().getGreaterThanOrEqualTo()
            .format(DateTimeFormatter.ISO_LOCAL_DATE));
        criteria.add(criterio);
      }
      if (null != (filtri.getDataRegistrazioneProtocollo().getLessThanOrEqualTo())) {
        QueryConditionType criterio = new QueryConditionType();
        criterio.setOperator(EnumQueryOperator.LESS_THAN_OR_EQUAL_TO);
        criterio.setPropertyName("dataProtocollo");
        criterio.setValue(filtri.getDataRegistrazioneProtocollo().getLessThanOrEqualTo()
            .format(DateTimeFormatter.ISO_LOCAL_DATE));
        criteria.add(criterio);
      }
    }
    return criteria;
  }

}
