/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.testbed.model;

import static org.junit.Assert.assertEquals;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmonotifications.util.listener.AppServletContextListener;


public abstract class ParentUnitTest implements ApplicationContextAware {

  private final Logger logger = Logger.getLogger(ParentUnitTest.class);

  private JdbcTemplate jdbcTemplate;

  private ApplicationContext applicationContext;

  private ServletContext servletContext = new MockServletContext();

  private static java.util.ResourceBundle res;

  private static ObjectMapper mapper;

  static {
    res = java.util.ResourceBundle.getBundle("config");
    mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.registerModule(new JavaTimeModule());
  }

  protected static java.util.ResourceBundle getRes() {
    return res;
  }

  protected ServletContext getServletContext() {
    return servletContext;
  }

  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  protected ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  protected Logger getLogger() {
    return logger;
  }

  @Before
  public void beforeParent() {
    mockServletEnvironment();
    MockitoAnnotations.initMocks(this);

    assertEquals("L'ambiente deve essere pre-configurato e dichiarato come JUNIT", "JUNIT",
        res.getString("nome.ambiente"));
  }

  protected void mockServletEnvironment() {
    MockHttpServletRequest request = new MockHttpServletRequest();

    AppServletContextListener.setServletContext(servletContext);

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  protected void log(Object... data) {
    StringBuilder dstr = new StringBuilder("");
    for (Object s : data) {
      dstr.append(s).append(" ");
    }

    logger.info(dstr.toString());
  }

  protected void dump(Object... data) {
    StringBuilder dstr = new StringBuilder("");
    for (Object s : data) {
      try {
        dstr.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(s)).append("\n");
      } catch (JsonProcessingException e) {
        dstr.append(s.toString()).append("(CANNOT SERIALIZE) \n");
      }
    }

    logger.info(dstr.toString());
  }

  @Autowired
  public void setDataSource(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;

    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
        applicationContext);
  }

  @SuppressWarnings("unchecked")
  protected static <T> T getTargetObject(Object proxy) {
    if ((AopUtils.isJdkDynamicProxy(proxy))) {
      try {
        return (T) getTargetObject(((Advised) proxy).getTargetSource().getTarget());
      } catch (Exception e) {
        throw new InternalServerException("Failed to unproxy target.", e);
      }
    }
    return (T) proxy;
  }

  protected void verifyDatabaseTestData() {

    int result =
        getJdbcTemplate().queryForObject(" select count(*) from cosmo_t_ente", Integer.class);
    if (result < 1) {
      throw new InternalServerException(
          "Test environment is not ready. Missing environment data identifier on DB");
    }
  }

  @SuppressWarnings("unchecked")
  protected <T> T getBean(Class<T> beanClass) {

    String name = beanClass.getSimpleName();
    name = name.substring(0, 1).toLowerCase() + name.substring(1);

    Object rawBean = getApplicationContext().getBean(name);
    T operation = null;

    if (AopUtils.isAopProxy(rawBean) && rawBean instanceof Advised) {
      Advised advised = (Advised) rawBean;
      try {
        operation = (T) advised.getTargetSource().getTarget();
      } catch (Exception e) {
        throw new InternalServerException("Error in getBean", e);
      }
    }

    return operation;
  }
}
