/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperField;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperIgnore;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperParameter;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperReportConfig;
import it.csi.cosmo.cosmoecm.integration.jasper.annotations.JasperResource;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperDataValidationException;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperModelValidationException;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperRenderException;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperResourceConflictException;
import it.csi.cosmo.cosmoecm.integration.jasper.exceptions.JasperResourceResolvingException;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;
import it.csi.cosmo.cosmoecm.integration.jasper.model.JasperResourceType;
import it.csi.cosmo.cosmoecm.util.listener.AppServletContextListener;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


/**
 *
 */

public class JasperExporter {

  private static final String TEMPLATES_CLASSPATH_LOCATION = "/templates";

  private static final String JASPER_COMPILER_CLASSPATH = "net.sf.jasperreports.compiler.classpath";

  private static final String JASPER_LOCATION = "WEB-INF/lib/jasperreports-6.16.0.jar";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "JasperExporter");

  private ObjectMapper mapper = new ObjectMapper();

  public byte[] render(JasperResourceLoader loader, Collection<?> data,
      ContestoCreazioneDocumento context) {
    return render(loader, null, data, null, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, Object data,
      ContestoCreazioneDocumento context) {
    return render(loader, null, Arrays.asList(data), null, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, Collection<?> data,
      Map<String, Object> parameters, ContestoCreazioneDocumento context) {
    return render(loader, null, data, parameters, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, Object data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context) {
    return render(loader, null, Arrays.asList(data), parameters, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, Collection<?> data,
      Map<String, Object> parameters, ExportFormat outputFormat,
      ContestoCreazioneDocumento context) {
    return render(loader, null, data, parameters, outputFormat, context);
  }

  public byte[] render(JasperResourceLoader loader, Object data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context) {
    return render(loader, null, Arrays.asList(data), parameters, outputFormat, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode, Collection<?> data,
      ContestoCreazioneDocumento context) {
    return render(loader, templateCode, data, null, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode,
      Map<String, Object> parameters, ContestoCreazioneDocumento context) {
    return render(loader, templateCode, null, parameters, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode, Collection<?> data,
      Map<String, Object> parameters, ContestoCreazioneDocumento context) {
    return render(loader, templateCode, data, parameters, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode, Object data,
      ContestoCreazioneDocumento context) {
    return render(loader, templateCode, Arrays.asList(data), null, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode, Object data,
      Map<String, Object> parameters, ContestoCreazioneDocumento context) {
    return render(loader, templateCode, Arrays.asList(data), parameters, ExportFormat.PDF, context);
  }

  public byte[] render(JasperResourceLoader loader, String templateCode, Object data,
      Map<String, Object> parameters, ExportFormat outputFormat,
      ContestoCreazioneDocumento context) {
    return render(loader, templateCode, Arrays.asList(data), parameters, outputFormat, context);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public byte[] render(
      //@formatter:off
      JasperResourceLoader loader,
      String templateCode,
      Collection<?> data,
      Map<String, Object> parameters,
      ExportFormat outputFormat,
      ContestoCreazioneDocumento context
      //@formatter:on
      ) {

    String mn = "render";
    logger.debug(mn, "rendering template " + templateCode);

    if (parameters == null) {
      parameters = new HashMap<>();
    }

    JRDataSource dataSource;

    if (data == null || data.isEmpty()) {
      dataSource = new JREmptyDataSource();

      if (logger.isTraceEnabled()) {
        logger.debug(mn, "rendering template with empty data source");
      }

    } else {
      // data not null and at least one element
      Collection<Object> mappedDataBeans = new LinkedList<>();

      for (Object o : data) {
        JasperReportConfig reportAnnotation = o.getClass().getAnnotation(JasperReportConfig.class);

        if (reportAnnotation != null) {
          validateJasperReportAnnotation(reportAnnotation);
          if (StringUtils.isEmpty(templateCode)) {
            templateCode = getJasperReportValue(reportAnnotation, o.getClass());

            logger.debug(mn, "rendering template (resolved dynamically) " + templateCode);
          }

          Object mappedSource = loadPojo(loader, o, parameters, context);
          mappedDataBeans.add(mappedSource);
        } else {
          mappedDataBeans.add(o);
        }
      }

      dataSource = new JRBeanCollectionDataSource(mappedDataBeans, false);

      if (logger.isTraceEnabled()) {
        logger.debug(mn, "rendering template with data: " + represent(mappedDataBeans));
      }

    }

    if (StringUtils.isEmpty(templateCode)) {
      throw new JasperRenderException(
          "Template code could not be resolved from supplied data and was not specified as input parameter");
    }

    if (logger.isTraceEnabled()) {
      logger.debug(mn, "rendering template with parameters: " + represent(parameters));
    }

    try {
      JasperReport template = resolveTemplate(null, templateCode, loader, context);

      logger.debug(mn, "filling template " + templateCode);

      JasperPrint print = JasperFillManager.fillReport(template, parameters, dataSource);

      ByteArrayOutputStream outputStream;
      JRAbstractExporter exporter = this.resolveExporter(outputFormat);

      logger.trace(mn,
          "resolved exporter " + outputFormat + " -> " + exporter.getClass().getName());

      outputStream = new ByteArrayOutputStream();
      exporter.setExporterInput(new SimpleExporterInput(print));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
      exporter.exportReport();

      return outputStream.toByteArray();

    } catch (Exception e) {
      throw new JasperRenderException("Error generating template: " + e.getMessage(), e);
    }
  }

  public JasperReport resolveTemplate(String parentCode, String templateCode,
      JasperResourceLoader loader,
      ContestoCreazioneDocumento context) throws JRException {
    InputStream stream;
    String mn = "resolveTemplate";
    logger.debug(mn, "resolving template " + templateCode);

    stream = loader.findTemplateFromDB(parentCode, templateCode, context);

    if (stream != null) {
      logger.trace(mn, "compiled template found on DB");
      return (JasperReport) JRLoader.loadObject(stream);
    }

    logger.trace(mn, "compiled template not found on DB");

    stream = loader.findTemplateSourceFromDB(parentCode, templateCode, context);

    if (stream != null) {

      logger.trace(mn, "template source found on DB");

      JasperReport template = compileModel(stream);

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      JRSaver.saveObject(template, os);
      byte[] compiledTemplate = os.toByteArray();

      loader.saveCompiledTemplateToDB(parentCode, templateCode, compiledTemplate, context);

      return template;
    }

    logger.trace(mn, "template source not found on DB");

    stream = findTemplateFromEmbeddedResources(parentCode, templateCode);

    if (stream != null) {
      logger.trace(mn, "compiled template found on embedded resources");
      return (JasperReport) JRLoader.loadObject(stream);
    }

    logger.trace(mn, "compiled template not found on embedded resources");

    stream = findTemplateSourceFromEmbeddedResources(parentCode, templateCode);

    if (stream != null) {
      logger.trace(mn, "template source found on embedded resources");

      JasperReport template = compileModel(stream);

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      JRSaver.saveObject(template, os);
      byte[] compiledTemplate = os.toByteArray();

      loader.saveCompiledTemplateToDB(parentCode, templateCode, compiledTemplate, context);

      return template;
    }

    logger.trace(mn, "template source not found on embedded resources");

    throw new JasperResourceResolvingException("Template not found: " + templateCode);
  }

  public Object resolveResource(String parentCode, String templateCode, String resourceCode,
      JasperResourceLoader loader,
      ContestoCreazioneDocumento context) throws IOException, JRException {
    String mn = "resolveImage";
    logger.debug(mn, "resolving image " + resourceCode);

    InputStream stream = loader.findResourceFromDB(parentCode, templateCode, resourceCode, context);
    if (stream == null) {
      logger.trace(mn, "image not found on DB, loading from embedded resources");

      stream = findResourceFromEmbeddedResources(parentCode, resourceCode);
    } else {
      logger.trace(mn, "image found on DB");
    }

    if (stream == null) {
      throw new JasperResourceResolvingException("Resource not found: " + resourceCode);
    }

    return ImageIO.read(stream);
  }

  public Object loadSubreport(String parentCode, String resourceCode, JasperResourceLoader loader,
      ContestoCreazioneDocumento context) throws JRException {
    logger.debug("loadSubreport", "resolving subreport " + resourceCode);

    return resolveTemplate(parentCode, resourceCode, loader, context);
  }

  public InputStream findResourceFromEmbeddedResources(String parentCode, String resourceCode) {
    logger.debug("findResourceFromEmbeddedResources",
        "findResourceFromEmbeddedResources(" + resourceCode + ")");

    try {

      InputStream stream = JasperExporter.class
          .getResourceAsStream(TEMPLATES_CLASSPATH_LOCATION + "/" + resourceCode);
      return stream;

    } catch (Exception e) {
      throw new JasperResourceResolvingException("Error reading resource " + resourceCode, e);
    }
  }

  public InputStream findTemplateFromEmbeddedResources(String parentCode, String templateCode) {
    logger.debug("findTemplateFromEmbeddedResources",
        "findTemplateFromEmbeddedResources(" + templateCode + ")");

    try {

      InputStream stream = JasperExporter.class
          .getResourceAsStream(TEMPLATES_CLASSPATH_LOCATION + "/" + templateCode + ".class");
      return stream;

    } catch (Exception e) {
      throw new JasperResourceResolvingException("Error reading template " + templateCode, e);
    }
  }

  public InputStream findTemplateSourceFromEmbeddedResources(String parentCode,
      String templateCode) {
    logger.debug("findTemplateSourceFromEmbeddedResources",
        "findTemplateSourceFromEmbeddedResources(" + templateCode + ")");

    try {

      InputStream stream = JasperExporter.class
          .getResourceAsStream(TEMPLATES_CLASSPATH_LOCATION + "/" + templateCode + ".jrxml");
      return stream;

    } catch (Exception e) {
      throw new JasperResourceResolvingException("Error reading template " + templateCode, e);
    }
  }

  public JasperReport compileModel(InputStream input) throws JRException {
    logger.debug("compileModel", "compiling template from source ...");

    JasperDesign jasperDesign = JRXmlLoader.load(input);
    JasperReportsContext context = DefaultJasperReportsContext.getInstance();
    context.setProperty(JASPER_COMPILER_CLASSPATH,
        AppServletContextListener.getServletContext().getRealPath(JASPER_LOCATION));
    return JasperCompileManager.getInstance(context).compile(jasperDesign);
  }

  public JRAbstractExporter<?, ?, ?, ?> resolveExporter(ExportFormat outputFormat) {

    switch (outputFormat) {
      case PDF:
        return new JRPdfExporter();
      case CSV:
        return new JRCsvExporter();
      case DOCX:
        return new JRDocxExporter();
      case ODT:
        return new JROdtExporter();
      case XLS:
        return new JRXlsExporter();
      case ODS:
        return new JROdsExporter();
      case PPTX:
        return new JRPptxExporter();
      case RTF:
        return new JRRtfExporter();
      case TEXT:
        return new JRTextExporter();
      case XML:
        return new JRXmlExporter();
      default:
        throw new JasperModelValidationException("Unrecognized output format: " + outputFormat);
    }
  }

  @SuppressWarnings("unchecked")
  private Object loadPojo(JasperResourceLoader loader, Object input, Map<String, Object> map,
      ContestoCreazioneDocumento context) {
    String mn = "loadPojo";
    if (input == null) {
      return null;
    }

    logger.trace(mn, "loading source data of class " + input.getClass());

    if (input instanceof Collection) {
      logger.trace(mn, "loading source data as collection");

      Collection<Object> outputCollection;
      // try {
      // outputCollection = (Collection<Object>) input.getClass ().newInstance ();
      outputCollection = new LinkedList<>();
      // } catch ( InstantiationException | IllegalAccessException e ) {
      // throw new RuntimeException ( "Error instantiating class " + input.getClass ().getName (), e
      // );
      // }

      for (Object item : ((Collection<Object>) input)) {
        outputCollection.add(loadPojo(loader, item, map, context));
      }

      logger.trace(mn, "source collection data loaded");
      return outputCollection;
    }

    JasperReportConfig reportAnnotation = input.getClass().getAnnotation(JasperReportConfig.class);
    if (reportAnnotation != null) {
      logger.trace(mn,
          "found JasperReport annotation on class " + input.getClass().getSimpleName());

      if (reportAnnotation.resources().length > 0) {
        logger.trace(mn, "loading resources required from annotation");

        for (JasperResource resource : reportAnnotation.resources()) {
          logger.trace(mn, "loading resource required from annotation: " + resource.value());
          validateJasperResourceAnnotation(resource);
          loadResourceByValue(loader, reportAnnotation.value(), resource, map, context);
        }
      }

      if (!StringUtils.isEmpty(reportAnnotation.value())) {
        // carica sub-report
        logger.trace(mn, "loading subreport required from annotation: " + reportAnnotation.value());
        loadResourceBySpecifics(loader, reportAnnotation.value(), reportAnnotation.value(),
            reportAnnotation.value(),
            JasperResourceType.SUBREPORT, map, context);
      }
    }

    Map<String, Object> outputMap = new HashMap<>();

    Set<Field> pojoFields = findFields(input.getClass());
    for (Field field : pojoFields) {
      boolean processed = false;

      JasperParameter parameterAnnotation = field.getAnnotation(JasperParameter.class);
      if (parameterAnnotation != null) {
        logger.trace(mn, "found JasperParameter annotation on field " + field.getName());

        loadParameter(parameterAnnotation, field, input, map);
        processed = true;
      }

      JasperResource resourceAnnotation = field.getAnnotation(JasperResource.class);
      if (resourceAnnotation != null) {
        logger.trace(mn, "found JasperResource annotation on field " + field.getName());

        loadResourceField(resourceAnnotation, field, input, map);
        processed = true;
      }

      if (!processed) {
        String key;
        Object fieldValue;

        if (field.getAnnotation(JasperIgnore.class) != null) {
          logger.trace(mn,
              "found JasperIgnore annotation on field " + field.getName() + ", ignoring field");
          continue;
        }

        JasperField fieldAnnotation = field.getAnnotation(JasperField.class);
        if (fieldAnnotation != null) {
          logger.trace(mn, "found JasperField annotation on field " + field.getName());

          if (fieldAnnotation.ignore()) {
            logger.trace(mn,
                "ignoring field " + field.getName() + " as requested from JasperField annotation");
            continue;
          }

          validateJasperFieldAnnotation(fieldAnnotation);

          key = getJasperFieldValue(fieldAnnotation, field);

          fieldValue = get(field, input);

          if (fieldAnnotation.required()) {
            if (fieldValue == null) {
              throw new JasperDataValidationException(
                  "Required field is missing: " + key + " on " + input.getClass());
            }
          }

        } else {
          if (!PropertyUtils.isReadable(input, field.getName())) {
            // not readable
            logger.trace(mn, "skipping non-readable field " + field.getName());
            continue;
          }

          key = field.getName();

          fieldValue = get(field, input);
        }

        if (fieldValue != null) {
          if (fieldValue instanceof Collection) {
            logger.trace(mn, "mapping collection field " + field.getName());
            fieldValue = loadPojo(loader, fieldValue, map, context);

          } else if (fieldValue.getClass().getName().startsWith("java.")) {
            // do not edit
            logger.trace(mn, "preserving native field " + field.getName());

          } else {
            logger.trace(mn, "mapping pojo field " + field.getName());
            fieldValue = loadPojo(loader, fieldValue, map, context);
          }
        }

        logger.trace(mn, "mapped field " + field.getName() + " to key " + key);
        outputMap.put(key, fieldValue);

        processed = true;
      }
    }

    return outputMap;
  }

  private void validateJasperReportAnnotation(JasperReportConfig input) {
    if (input == null) {
      throw new JasperModelValidationException("JasperReport annotation cannot be null");
    }
  }

  private void validateJasperFieldAnnotation(JasperField input) {
    if (input == null) {
      throw new JasperModelValidationException("JasperField annotation cannot be null");
    }
  }

  private void loadParameter(JasperParameter parameter, Field field, Object object,
      Map<String, Object> map) {
    String key = getJasperParameterValue(parameter, field);

    Object value = get(field, object);

    if (map.containsKey(key)) {
      Object oldValue = map.get(key);

      if (!equals(value, oldValue)) {
        throw new JasperResourceConflictException("Conflicting parameter key: " + key
            + " with different values: was " + oldValue + " and is now " + value);
      }
    }

    if (parameter.required()) {
      if (value == null) {
        throw new JasperDataValidationException(
            "Required parameter is missing: " + key + " on " + object.getClass());
      }
    }

    if (logger.isTraceEnabled()) {
      logger.trace("loadParameter", "loaded parameter " + key + " to value " + value);
    }

    map.put(key, value);
  }

  private boolean equals(Object o1, Object o2) {
    return (o1 == null && o2 == null) || (o1 != null && o2 != null && (o1 == o2 || o1.equals(o2)));
  }

  private Object get(Field field, Object o) {
    if (o == null) {
      throw new InvalidParameterException();
    }

    try {
      // return field.get ( o );
      return PropertyUtils.getProperty(o, field.getName());
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
        | NoSuchMethodException e) {
      throw new JasperDataValidationException(
          "Error reading field " + field.getName() + " from class " + o.getClass(), e);
    }
  }

  private String getJasperReportValue(JasperReportConfig report, Class<?> parentClass) {
    if (!StringUtils.isEmpty(report.value())) {
      return report.value();
    } else {
      // return parentClass.getSimpleName ();
      return null;
    }
  }

  private String getJasperParameterValue(JasperParameter parameter, Field parentField) {
    if (!StringUtils.isEmpty(parameter.value())) {
      return parameter.value();
    } else {
      return parentField.getName();
    }
  }

  private String getJasperResourceValue(JasperResource resource, Field parentField) {
    if (!StringUtils.isEmpty(resource.key())) {
      return resource.key();
    } else {
      return parentField.getName();
    }
  }

  private String getJasperFieldValue(JasperField field, Field parentField) {
    if (!StringUtils.isEmpty(field.value())) {
      return field.value();
    } else {
      return parentField.getName();
    }
  }

  private void loadResourceBySpecifics(JasperResourceLoader loader, String templateCode, String key,
      String resValue,
      JasperResourceType resourceType, Map<String, Object> map,
      ContestoCreazioneDocumento context) {
    String metaLoadingInfo = key + "#" + resourceType + "=" + resValue;
    String metaLoadingInfoKey = "___MLIK_" + key;

    if (map.containsKey(metaLoadingInfoKey)) {
      String oldValue = (String) map.get(metaLoadingInfoKey);

      if (!equals(metaLoadingInfo, oldValue)) {
        throw new JasperResourceConflictException("Conflicting resource key: " + key
            + " with different values: was " + oldValue + " and is now " + metaLoadingInfo);
      } else {
        logger.trace("loadResourceBySpecifics", "resource already loaded: " + metaLoadingInfo);
        return;
      }
    }

    Object value;

    switch (resourceType) {
      case IMAGE:
        try {
          value = resolveResource(null, templateCode, resValue, loader, context);
        } catch (IOException | JRException e) {
          throw new JasperResourceResolvingException("Error loading image " + resValue, e);
        }
        break;
      case SUBREPORT:
        try {
          value = loadSubreport(templateCode, resValue, loader, context);
        } catch (JRException e) {
          throw new JasperResourceResolvingException("Error loading subreport template " + resValue,
              e);
        }
        break;
      default:
        throw new JasperModelValidationException("Unrecognized resource type: " + resourceType);
    }

    if (logger.isTraceEnabled()) {
      logger.trace("loadResourceBySpecifics", "loaded resource " + key
          + " to value [ omitted ] with meta loading info " + metaLoadingInfo);
    }

    map.put(key, value);
    map.put(metaLoadingInfoKey, metaLoadingInfo);
  }

  private void loadResourceByValue(JasperResourceLoader loader, String templateCode,
      JasperResource resource,
      Map<String, Object> map, ContestoCreazioneDocumento context) {
    loadResourceBySpecifics(loader, templateCode, resource.key(), resource.value(), resource.type(),
        map,
        context);
  }

  private void loadResourceField(JasperResource resource, Field field, Object object,
      Map<String, Object> map) {
    String key = getJasperResourceValue(resource, field);

    if (logger.isTraceEnabled()) {
      logger.trace("loadResourceField", "loading resource " + key + " from field " + field.getName()
      + " of object " + object.getClass().getSimpleName());
    }

    Object value = get(field, object);

    if (map.containsKey(key)) {
      Object oldValue = map.get(key);

      if (!equals(value, oldValue)) {
        throw new JasperResourceConflictException("Conflicting resource key: " + key
            + " with different values: was " + oldValue + " and is now " + value);
      }
    }

    if (logger.isTraceEnabled()) {
      logger.trace("loadResourceField",
          "loaded resource " + key + " to value [ omitted ] from field " + field.getName()
          + " of object " + object.getClass().getSimpleName());
    }

    map.put(key, value);
  }

  private void validateJasperResourceAnnotation(JasperResource input) {
    if (input == null) {
      throw new JasperModelValidationException("JasperResource annotation cannot be null");
    }

    if (input.key() == null || input.key().isEmpty()) {
      throw new JasperModelValidationException("JasperResource annotation 'key' cannot be null");
    }

    if (input.value() == null || input.value().isEmpty()) {
      throw new JasperModelValidationException("JasperResource annotation 'value' cannot be null");
    }

    if (input.type() == null) {
      throw new JasperModelValidationException("JasperResource annotation 'type' cannot be null");
    }
  }

  private static Set<Field> findFields(Class<?> classs) {
    Set<Field> set = new HashSet<>();

    Class<?> c = classs;
    while (c != null) {
      for (Field field : c.getDeclaredFields()) {
        set.add(field);
      }
      c = c.getSuperclass();
    }
    return set;
  }

  private String represent(Object o) {
    if (o == null) {
      return "null";
    }
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    } catch (Exception e) {
      return "[" + o.getClass().getName() + " (error representing: " + e.getClass().getSimpleName()
          + " - " + e.getMessage() + ")";
    }
  }
}
