/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmopratiche.testbed.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import it.csi.cosmo.common.infinispan.connector.InfinispanResourceConnector;
import it.csi.cosmo.common.repository.CosmoRepositoryFactory;
import it.csi.cosmo.cosmopratiche.business.service.UserService;
import it.csi.cosmo.cosmopratiche.business.service.impl.UserServiceImpl;
import it.csi.cosmo.cosmopratiche.integration.infinispan.connector.InfinispanResourceProvider;
import it.csi.cosmo.cosmopratiche.util.listener.SpringApplicationContextHelper;

@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan(
    basePackages = {"it.csi.cosmo.cosmopratiche",
    "it.csi.test.cosmo.cosmopratiche.testbed"},
    excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = Configuration.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserServiceImpl.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = InfinispanResourceProvider.class)})
@EnableJpaRepositories(
    basePackages = {"it.csi.cosmo.cosmopratiche.integration.repository",
    "it.csi.test.cosmo.cosmopratiche.testbed.repository"},
    repositoryFactoryBeanClass = CosmoRepositoryFactory.class)
@EnableTransactionManagement

@TestExecutionListeners({DirtiesContextTestExecutionListener.class})
public class CosmoPraticheUnitTestInMemory {

  @Resource
  private Environment env;

  @Bean
  public SpringApplicationContextHelper springApplicationContextHelper() {
    return new SpringApplicationContextHelper();
  }

  @Bean
  public UserService userServiceImpl() {
    return new UserServiceImpl();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("it.csi.cosmo.common.entities");
    em.setMappingResources("orm.xml");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  @Bean
  public EmbeddedDatabase dataSource() {
    //@formatter:off
    var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
        .addScript("cosmocommon/test-resources/sql/init_schema.sql")
        .addScript("cosmocommon/test-resources/sql/init_data.sql")
        .addScript("sql/edit_schema.sql")
        .addScript("sql/edit_data.sql")
        .build();

    ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
    rdp.addScript(new ClassPathResource("cosmocommon/test-resources/sql/init_database.sql"));
    rdp.setSeparator(";;");
    rdp.execute(dataSource);

    return dataSource;
    //@formatter:on
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);

    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }


  Properties additionalProperties() {
    Properties properties = new Properties();

    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

    properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
    properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "false");
    properties.setProperty("hibernate.hbm2ddl.auto", "none");
    properties.setProperty("org.hibernate.flushMode", "COMMIT");
    properties.setProperty("hibernate.flushMode", "COMMIT");

    return properties;
  }

  @Bean
  public InfinispanResourceConnector infinispanConnector() {

    return new InfinispanResourceConnector() {

      private Map<String, Object> fallbackLocalCache = new LinkedHashMap<>();

      @Override
      public void close() throws Exception {
        // NOP
      }

      @Override
      public Map<String, Object> getCache() {
        return fallbackLocalCache;
      }

    };
  }


}
