/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.testbed.config;

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
import it.csi.cosmo.common.cripto.CryptoConfigurationProvider;
import it.csi.cosmo.common.cripto.CryptoConverter;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.repository.CosmoRepositoryFactory;
import it.csi.cosmo.cosmoauthorization.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;


@Configuration

@PropertySource ( "classpath:database.properties" )

@ComponentScan(basePackages = {
		"it.csi.cosmo.cosmoauthorization.business.batch.impl",
    "it.csi.cosmo.cosmoauthorization.business.service.impl",
"it.csi.cosmo.cosmoauthorization.integration.mapper"},
excludeFilters = {
    @Filter ( type = FilterType.ANNOTATION, value = Configuration.class ) } )

@EnableJpaRepositories(basePackages = {"it.csi.cosmo.cosmoauthorization.integration.repository"},
    repositoryFactoryBeanClass = CosmoRepositoryFactory.class)
@EnableTransactionManagement

@TestExecutionListeners ( { DirtiesContextTestExecutionListener.class } )
public class CosmoAuthorizationUnitTestDB {

  @Resource
  private Environment env;

  @Bean
  public SpringApplicationContextHelper springApplicationContextHelper () {
    return new SpringApplicationContextHelper ();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory () {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean ();

    em.setDataSource ( dataSource () );
    em.setPackagesToScan("it.csi.cosmo.common.entities");
    em.setMappingResources("orm.xml");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter ();
    em.setJpaVendorAdapter ( vendorAdapter );
    em.setJpaProperties ( additionalProperties () );

    return em;
  }

  @Bean
  public DataSource dataSource () {
    BasicDataSource ds = new BasicDataSource ();
    ds.setDriverClassName ( env.getRequiredProperty ( "csi.db.driver" ) );
    ds.setUrl ( env.getRequiredProperty ( "csi.db.url" ) );
    ds.setUsername ( env.getRequiredProperty ( "csi.db.username" ) );
    ds.setPassword ( env.getRequiredProperty ( "csi.db.password" ) );

    ds.setDefaultAutoCommit ( false );
    return ds;
  }

  @Bean
  public PlatformTransactionManager transactionManager ( EntityManagerFactory emf ) {
    JpaTransactionManager transactionManager = new JpaTransactionManager ();
    transactionManager.setEntityManagerFactory ( emf );
    return transactionManager;

  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation () {
    return new PersistenceExceptionTranslationPostProcessor ();
  }

  @Bean
  public CryptoConverter cryptoConverter() {
    return CryptoConverter.builder()
        .withLoggingPrefix(LogCategory.BUSINESS_LOG_CATEGORY.getCategory())
        .withConfigurationProvider(new CryptoConfigurationProvider() {

          @Override
          public String getPassphrase() {
            throw new ForbiddenException(
                "Crypto conversion on real DB is not allowed in unit tests");
          }

          @Override
          public String getIvParameter() {
            throw new ForbiddenException(
                "Crypto conversion on real DB is not allowed in unit tests");
          }
        }).build();
  }

  Properties additionalProperties () {
    Properties properties = new Properties ();
    properties.setProperty("hibernate.dialect", "it.csi.cosmo.common.config.PostgreSQLDialect");
    properties.setProperty ( "hibernate.temp.use_jdbc_metadata_defaults", "false" );
    properties.setProperty ( "hibernate.jdbc.lob.non_contextual_creation", "true" );
    properties.setProperty ( "hibernate.show_sql", "false" );
    properties.setProperty ( "hibernate.format_sql", "true" );
    properties.setProperty ( "hibernate.hbm2ddl.auto", "none" );
    properties.setProperty("org.hibernate.flushMode", "COMMIT");
    properties.setProperty("hibernate.flushMode", "COMMIT");

    return properties;
  }

}
