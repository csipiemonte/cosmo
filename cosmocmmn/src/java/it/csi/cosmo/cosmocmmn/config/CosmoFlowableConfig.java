/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.config;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.cmmn.rest.service.api.CmmnRestResponseFactory;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.rest.resolver.ContentTypeResolver;
import org.flowable.common.rest.resolver.DefaultContentTypeResolver;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.form.engine.FormEngineConfiguration;
import org.flowable.form.rest.FormRestResponseFactory;
import org.flowable.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.cosmocmmn.business.CosmoFormEngineConfigurator;
import it.csi.cosmo.cosmocmmn.business.CosmoIdmEngineConfigurator;
import it.csi.cosmo.cosmocmmn.business.FaultInjectionEventListener;
import it.csi.cosmo.cosmocmmn.business.LoggingEventListener;
import it.csi.cosmo.cosmocmmn.business.PraticheFlowableEventListener;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoApplyOffsetToOffsetDateTimeDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoComputeDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoGetApiUrlDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoGetClientCorrenteDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoGetUtenteCorrenteDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoGetUtenteFromCodiceFiscaleDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoToLocalDateDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoToLocalDateTimeDelegate;
import it.csi.cosmo.cosmocmmn.business.delegates.CosmoToOffsetDateTimeDelegate;
import it.csi.cosmo.cosmocmmn.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmocmmn.business.service.EventSenderService;
import it.csi.cosmo.cosmocmmn.exception.MisconfigurationException;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessProcessEventFeignClient;

@Configuration
@EnableWebMvc
public class CosmoFlowableConfig {

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoBusinessProcessEventFeignClient cosmoBusinessFeignClient;

  @Autowired
  private EventSenderService eventSenderService;

  @Bean
  public RestResponseFactory restResponseFactory(ObjectMapper mapper) {
    return new RestResponseFactory(mapper);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public ContentTypeResolver contentTypeResolver() {
    return new DefaultContentTypeResolver();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {

    return new JdbcTemplate(dataSource);
  }

  @Bean
  public NamedParameterJdbcOperations template(JdbcTemplate template) {
    return new NamedParameterJdbcTemplate(template);
  }

  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setDefaultEncoding("utf-8");
    resolver.setMaxInMemorySize((int) 1e6);
    resolver.setMaxUploadSizePerFile((long) 1e6);
    resolver.setMaxUploadSize((long) 1e6);
    return resolver;
  }

  @Bean
  ProcessEngineConfiguration processEngineConfiguration(
      CosmoIdmEngineConfigurator idmEngineConfigurator,
      CosmoFormEngineConfigurator formEngineConfigurator, DataSource dataSource,
      JmsTemplate jmsTemplate) {
    StandaloneProcessEngineConfiguration result = new StandaloneProcessEngineConfiguration();

    if (result.getCustomFlowableFunctionDelegates() == null) {
      result.setCustomFlowableFunctionDelegates(new ArrayList<>());
    }

    result.getCustomFlowableFunctionDelegates().add(new CosmoGetUtenteCorrenteDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoGetClientCorrenteDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoGetApiUrlDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoGetUtenteFromCodiceFiscaleDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoToLocalDateDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoToLocalDateTimeDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoToOffsetDateTimeDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoApplyOffsetToOffsetDateTimeDelegate());
    result.getCustomFlowableFunctionDelegates().add(new CosmoComputeDelegate());

    configureMailServer(result);

    result.setIdmEngineConfigurator(idmEngineConfigurator);
    result.addConfigurator(formEngineConfigurator);
    result.setDataSource(dataSource);
    result.setAsyncExecutorActivate(true);
    result.setDatabaseSchemaUpdate(FormEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    configureEventListeners(result);

    return result;
  }

  private void configureEventListeners(StandaloneProcessEngineConfiguration config) {

    List<FlowableEventListener> listeners = new ArrayList<>();

    FlowableEventListener pec = new PraticheFlowableEventListener(config, eventSenderService);
    listeners.add(pec);

    FlowableEventListener lel = new LoggingEventListener();
    listeners.add(lel);

    if (Boolean.TRUE.equals(this.configurazioneService
        .getConfig(ParametriApplicativo.ENABLE_FAULT_INJECTION).asBoolean())) {

      FlowableEventListener fiel = new FaultInjectionEventListener();
      listeners.add(fiel);
    }

    config.setEventListeners(listeners);
  }

  private void configureMailServer(StandaloneProcessEngineConfiguration config) {

    config.setMailServerHost(
        this.configurazioneService.getConfig(ParametriApplicativo.MAIL_SERVER).getValue());
    config.setMailServerDefaultFrom(
        this.configurazioneService.getConfig(ParametriApplicativo.MAIL_DEFAULT_FROM).getValue());
    String port = this.configurazioneService.getConfig(ParametriApplicativo.MAIL_PORT).getValue();
    try {
      config.setMailServerPort(Integer.valueOf(port));
    } catch (NumberFormatException e) {
      throw new MisconfigurationException(String.format("Configured %s value is [%s]",
          ParametriApplicativo.MAIL_PORT.getCodice(), port), e);
    }

    config.setMailServerUseSSL(Boolean.valueOf(
        this.configurazioneService.getConfig(ParametriApplicativo.MAIL_USE_SSL).getValue()));
    config.setMailServerUseTLS(Boolean.valueOf(this.configurazioneService
        .getConfig(ParametriApplicativo.MAIL_ENABLE_START_TLS).getValue()));

    if (Boolean.TRUE.equals(Boolean.valueOf(this.configurazioneService
        .getConfig(ParametriApplicativo.MAIL_ENABLE_AUTHENTICATION).getValue()))) {
      config.setMailServerUsername(
          this.configurazioneService.getConfig(ParametriApplicativo.MAIL_USERNAME).getValue());
      config.setMailServerPassword(
          this.configurazioneService.getConfig(ParametriApplicativo.MAIL_PASSWORD).getValue());
    }
  }

  @Bean
  CmmnEngineConfiguration cmmnEngineConfiguration(CosmoIdmEngineConfigurator idmEngineConfigurator,
      DataSource dataSource) {
    CmmnEngineConfiguration cmmnEngine =
        CmmnEngineConfiguration.createStandaloneCmmnEngineConfiguration();
    cmmnEngine.setIdmEngineConfigurator(idmEngineConfigurator);
    cmmnEngine.setDataSource(dataSource);
    cmmnEngine.setDatabaseSchemaUpdate(FormEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    cmmnEngine.buildCmmnEngine();
    return cmmnEngine;
  }

  @Bean
  FormEngineConfiguration formEngineConfiguration(CosmoIdmEngineConfigurator idmEngineConfigurator,
      DataSource dataSource) {
    FormEngineConfiguration formEngine =
        FormEngineConfiguration.createStandaloneFormEngineConfiguration();
    formEngine.setIdmEngineConfigurator(idmEngineConfigurator);
    formEngine.setDataSource(dataSource);
    formEngine.setDatabaseSchemaUpdate(FormEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    formEngine.buildFormEngine();
    return formEngine;
  }

  @Bean
  CmmnRestResponseFactory cmmnRestResponseFactory() {
    return new CmmnRestResponseFactory(new ObjectMapper());
  }

  @Bean
  FormRestResponseFactory formRestResponseFactory() {
    return new FormRestResponseFactory();
  }
}
