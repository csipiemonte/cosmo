/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoTRisorsaTemplateReport;
import it.csi.cosmo.common.entities.CosmoTRisorsaTemplateReport_;
import it.csi.cosmo.common.entities.CosmoTTemplateReport;
import it.csi.cosmo.common.entities.CosmoTTemplateReport_;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.DocumentGeneratorService;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;
import it.csi.cosmo.cosmoecm.integration.jasper.engine.JasperExporter;
import it.csi.cosmo.cosmoecm.integration.jasper.engine.JasperResourceLoader;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperModelValidationException;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperResourceResolvingException;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTRisorsaTemplateReportRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTTemplateReportRepository;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;


/**
 *
 */
@Service
public class DocumentGeneratorServiceImpl
implements DocumentGeneratorService, JasperResourceLoader {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private JasperExporter exporter = new JasperExporter();

  @Autowired
  private CosmoTTemplateReportRepository cosmoTTemplateReportRepository;

  @Autowired
  private CosmoTRisorsaTemplateReportRepository cosmoTRisorsaTemplateReportRepository;

  @Override
  public byte[] render(Collection<?> data, ContestoCreazioneDocumento context) {
    return exporter.render(this, data, context);
  }

  @Override
  public byte[] render(Object data, ContestoCreazioneDocumento context) {
    return exporter.render(this, data, context);
  }

  @Override
  public byte[] render(Collection<?> data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, data, parameters, context);
  }

  @Override
  public byte[] render(Object data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, data, parameters, context);
  }

  @Override
  public byte[] render(Collection<?> data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context) {
    return exporter.render(this, data, parameters, outputFormat, context);
  }

  @Override
  public byte[] render(Object data, Map<String, Object> parameters, ExportFormat outputFormat,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, data, parameters, outputFormat, context);
  }

  @Override
  public byte[] render(String templateCode, Collection<?> data,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, context);
  }

  @Override
  public byte[] render(String templateCode, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, parameters, context);
  }

  @Override
  public byte[] render(String templateCode, Collection<?> data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, parameters, context);
  }

  @Override
  public byte[] render(String templateCode, Object data, ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, context);
  }

  @Override
  public byte[] render(String templateCode, Object data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, parameters, context);
  }

  @Override
  public byte[] render(String templateCode, Object data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, parameters, context);
  }

  @Override
  public byte[] render(String templateCode, Collection<?> data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context) {
    return exporter.render(this, templateCode, data, parameters, outputFormat, context);
  }

  @Override
  public Object loadSubreport(String parentCode, String resourceCode,
      ContestoCreazioneDocumento context) {
    try {
      return exporter.loadSubreport(parentCode, resourceCode, this, context);
    } catch (Exception e) {
      throw new JasperResourceResolvingException("Error loading subreport " + resourceCode, e);
    }
  }

  @Override
  public Object loadImage(String parentCode, String templateCode, String resourceCode,
      ContestoCreazioneDocumento context) {
    try {
      return exporter.resolveResource(parentCode, templateCode, resourceCode, this, context);
    } catch (Exception e) {
      throw new JasperResourceResolvingException("Error loading image " + resourceCode, e);
    }
  }

  @Override
  public InputStream findResourceFromDB(String parentCode, String templateCode, String resourceCode,
      ContestoCreazioneDocumento context) {
    final var method = "findResourceFromDB";
    logger.debug(method, "findResourceFromDB per codice {} e contesto {}", resourceCode, context);

    CosmoTRisorsaTemplateReport entity =
        findResourceEntityFromDB(templateCode, resourceCode, context);
    if (entity == null) {
      return null;
    }

    if (entity.getContenutoRisorsa() != null) {
      logger.debug(method, "trovata risorsa con contenuto embedded per codice {} e contesto {}",
          resourceCode, context);

      return new ByteArrayInputStream(entity.getContenutoRisorsa());
    }



    return null;
  }

  @Override
  public InputStream findTemplateFromDB(String parentCode, String templateCode,
      ContestoCreazioneDocumento context) {
    final var method = "findTemplateFromDB";
    logger.debug(method, "findTemplateFromDB per codice {} e contesto {}", templateCode, context);

    CosmoTTemplateReport entity = findTemplateEntityFromDB(parentCode, templateCode, context);
    if (entity == null) {
      return null;
    }

    if (entity.getTemplateCompilato() != null) {
      logger.debug(method, "trovato template gia' compilato per codice {} e contesto {}",
          templateCode, context);

      return new ByteArrayInputStream(entity.getTemplateCompilato());
    }

    // compila il template
    logger.info(method,
        "trovato template DA COMPILARE per codice {} e contesto {}, avvio compilazione ora",
        templateCode, context);

    JasperReport compilato;
    byte[] compiledTemplate;

    try {
      compilato = exporter.compileModel(new ByteArrayInputStream(entity.getSorgenteTemplate()));
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      JRSaver.saveObject(compilato, os);
      compiledTemplate = os.toByteArray();

    } catch (JRException e) {
      logger.debug(method, "errore nella compilazione on-the-fly del template", e);
      throw new JasperResourceResolvingException(
          "errore nella compilazione on-the-fly del template", e);
    }

    // salva il template compilato
    logger.debug(method, "salvo il template appena compilato nel db sulla entity {}",
        entity.getId());
    entity.setTemplateCompilato(compiledTemplate);

    cosmoTTemplateReportRepository.saveAndFlush(entity);

    logger.info(method, "salvato il template compilato per codice {} e contesto {} sulla entity {}",
        templateCode, context, entity.getId());
    return new ByteArrayInputStream(compiledTemplate);
  }

  private CosmoTTemplateReport findTemplateEntityFromDB(String parentCode, String templateCode,
      ContestoCreazioneDocumento context) {

    final var method = "findTemplateEntityFromDB";
    logger.debug(method, "findTemplateEntityFromDB per codice {} e contesto {}", templateCode,
        context);

    CosmoTTemplateReport entity = null;

    if (context != null && context.getIdEnte() != null
        && !StringUtils.isBlank(context.getCodiceTipoPratica())) {
      // ricerca per codice, idEnte e codice tipo pratica
      logger.debug(method,
          "ricerco template {} per match su codice {}, idEnte {} e codiceTipoPratica {}",
          templateCode, context.getIdEnte(), context.getCodiceTipoPratica());

      //@formatter:off
      entity = cosmoTTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          parentCode == null ? cb.isNull(root.get(CosmoTTemplateReport_.codiceTemplatePadre)) : cb.equal(root.get(CosmoTTemplateReport_.codiceTemplatePadre), parentCode),
              cb.equal(root.get(CosmoTTemplateReport_.codice), templateCode),
              cb.equal(root.get(CosmoTTemplateReport_.idEnte), context.getIdEnte()),
              cb.equal(root.get(CosmoTTemplateReport_.codiceTipoPratica), context.getCodiceTipoPratica())
          )
          ).orElse(null);
      //@formatter:on
    }

    if (entity == null && context != null && context.getIdEnte() != null) {
      // ricerca per codice e idEnte
      logger.debug(method, "ricerco template {} per match su codice {} ed idEnte {}", templateCode,
          context.getIdEnte());

      //@formatter:off
      entity = cosmoTTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          parentCode == null ? cb.isNull(root.get(CosmoTTemplateReport_.codiceTemplatePadre)) : cb.equal(root.get(CosmoTTemplateReport_.codiceTemplatePadre), parentCode),
              cb.equal(root.get(CosmoTTemplateReport_.codice), templateCode),
              cb.equal(root.get(CosmoTTemplateReport_.idEnte), context.getIdEnte())
          )
          ).orElse(null);
      //@formatter:on
    }

    if (entity == null) {
      // ricerca per codice
      logger.debug(method, "ricerco template {} per match su codice {}", templateCode);

      //@formatter:off
      entity = cosmoTTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          parentCode == null ? cb.isNull(root.get(CosmoTTemplateReport_.codiceTemplatePadre)) : cb.equal(root.get(CosmoTTemplateReport_.codiceTemplatePadre), parentCode),
              cb.equal(root.get(CosmoTTemplateReport_.codice), templateCode)
          )
          ).orElse(null);
      //@formatter:on
    }

    logger.debug(method, "nessun template trovato");
    return entity;
  }

  private CosmoTRisorsaTemplateReport findResourceEntityFromDB(String templateCode,
      String resourceCode, ContestoCreazioneDocumento context) {

    final var method = "findResourceEntityFromDB";
    logger.debug(method, "findResourceEntityFromDB per codice {} e contesto {}", resourceCode,
        context);

    CosmoTRisorsaTemplateReport entity = null;

    if (context != null && context.getIdEnte() != null
        && !StringUtils.isBlank(context.getCodiceTipoPratica())) {
      // ricerca per codice, idEnte e codice tipo pratica
      logger.debug(method,
          "ricerco risorsa template {} per match su codice {}, idEnte {} e codiceTipoPratica {}",
          resourceCode, context.getIdEnte(), context.getCodiceTipoPratica());

      //@formatter:off
      entity = cosmoTRisorsaTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate), templateCode),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codice), resourceCode),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.idEnte), context.getIdEnte()),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTipoPratica), context.getCodiceTipoPratica())
          )
          ).orElse(null);
      //@formatter:on
    }

    if (entity == null && context != null && context.getIdEnte() != null) {
      // ricerca per codice e idEnte
      logger.debug(method, "ricerco risorsa template {} per match su codice {} ed idEnte {}",
          resourceCode, context.getIdEnte());

      //@formatter:off
      entity = cosmoTRisorsaTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate), templateCode),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codice), resourceCode),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.idEnte), context.getIdEnte())
          )
          ).orElse(null);
      //@formatter:on
    }

    if (entity == null) {
      // ricerca per codice
      logger.debug(method, "ricerco risorsa template {} per match su codice {}", resourceCode);

      //@formatter:off
      entity = cosmoTRisorsaTemplateReportRepository.findOneNotDeleted((root, cq, cb) ->
      cb.and(
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate), templateCode),
          cb.equal(root.get(CosmoTRisorsaTemplateReport_.codice), resourceCode)
          )
          ).orElse(null);
      //@formatter:on
    }

    logger.debug(method, "nessuna risorsa template trovato");
    return entity;
  }

  @Override
  public InputStream findTemplateSourceFromDB(String parentCode, String templateCode,
      ContestoCreazioneDocumento context) {
    logger.debug("findTemplateSourceFromDB", "findTemplateSourceFromDB(" + templateCode + ")");

    // non implementato perche' il salvataggio nel db e' contestuale e necessita di informazioni che
    // qui non sono presenti
    return null;
  }

  @Override
  public void saveCompiledTemplateToDB(String parentCode, String templateCode,
      byte[] compiledTemplate,
      ContestoCreazioneDocumento context) {
    logger.debug("saveCompiledTemplateToDB", "saveCompiledTemplateToDB( ... )");

    // non implementato perche' il salvataggio nel db e' contestuale e necessita di informazioni che
    // qui non sono presenti
  }

  @Override
  public Map<String, Object> loadRisorsePerTemplate(String parentCode, String codiceTemplate,
      ContestoCreazioneDocumento context) {
    Map<String, Object> output = new HashMap<>();

    var allResources = cosmoTRisorsaTemplateReportRepository.findAllNotDeleted((root, cq, cb) -> cb
        .equal(root.get(CosmoTRisorsaTemplateReport_.codiceTemplate), codiceTemplate));

    var allResourceNames = allResources.stream().map(CosmoTRisorsaTemplateReport::getCodice)
        .distinct().collect(Collectors.toSet());

    allResourceNames.forEach(name -> {
      try {
        output.put(name, exporter.resolveResource(parentCode, codiceTemplate, name, this, context));
      } catch (IOException | JRException e) {
        throw new JasperResourceResolvingException(e);
      }
    });

    var allSubreports = cosmoTTemplateReportRepository.findAllNotDeleted(
        (root, cq, cb) -> cb.equal(root.get(CosmoTTemplateReport_.codiceTemplatePadre), codiceTemplate));

    var allSubreportNames = allSubreports.stream().map(CosmoTTemplateReport::getCodice)
        .distinct().collect(Collectors.toSet());

    allSubreportNames.forEach(name -> {
      try {
        output.put(name, exporter.resolveTemplate(codiceTemplate, name, this, context));
      } catch (JRException e) {
        throw new JasperResourceResolvingException(e);
      }

      // importa le risorse per il subreport
      Map<String, Object> subreportResources =
          loadRisorsePerTemplate(codiceTemplate, name, context);

      subreportResources.entrySet().forEach(sr -> output.put(sr.getKey(), sr.getValue()));
    });

    return output;
  }

  @Override
  public String getMimeTypeByFormato(ExportFormat format) {

    switch (format) {
      case PDF:
        return "application/pdf";
      case CSV:
        return "text/csv";
      case DOCX:
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
      case ODT:
        return "application/vnd.oasis.opendocument.text";
      case PPTX:
        return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        // di seguito formati non supportati:
      case XLS:
        return "application/vnd.ms-excel";
      case ODS:
        return "application/vnd.oasis.opendocument.spreadsheet";
      case RTF:
        return "text/richtext";
      case TEXT:
        return "text/plain";
      case XML:
        return "text/xml";
      default:
        throw new JasperModelValidationException("Unrecognized format: " + format);
    }

  }
}
