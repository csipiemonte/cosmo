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
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
import it.csi.cosmo.cosmopratiche.integration.infinispan.connector.InfinispanResourceProvider;
import it.csi.cosmo.cosmopratiche.util.listener.SpringApplicationContextHelper;


@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan(
    basePackages = {"it.csi.cosmo.cosmopratiche.business", "it.csi.cosmo.cosmopratiche.integration",
    "it.csi.test.cosmo.cosmopratiche.testbed.providers"},
    excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = Configuration.class),
      @Filter(type = FilterType.ASSIGNABLE_TYPE, value = InfinispanResourceProvider.class)})
@EnableJpaRepositories(
    basePackages = {"it.csi.cosmo.cosmopratiche.integration.repository",
    "it.csi.test.cosmo.cosmopratiche.testbed.repository"},
    repositoryFactoryBeanClass = CosmoRepositoryFactory.class)
@EnableTransactionManagement

@TestExecutionListeners({DirtiesContextTestExecutionListener.class})
public class CosmoPraticheUnitTestDB {

  @Resource
  private Environment env;

  @Bean
  public SpringApplicationContextHelper springApplicationContextHelper() {
    return new SpringApplicationContextHelper();
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
  public DataSource dataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(env.getRequiredProperty("csi.db.driver"));
    ds.setUrl(env.getRequiredProperty("csi.db.url"));
    ds.setUsername(env.getRequiredProperty("csi.db.username"));
    ds.setPassword(env.getRequiredProperty("csi.db.password"));

    ds.setDefaultAutoCommit(false);
    return ds;
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
    properties.setProperty("hibernate.dialect", "it.csi.cosmo.common.config.PostgreSQLDialect");

    properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
    properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "true");
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
