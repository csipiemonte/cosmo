/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.testbed.model;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.csi.cosmo.common.exception.InternalServerException;

public abstract class ParentUnitTest {

  private final Logger logger = Logger.getLogger ( ParentUnitTest.class );

  private static ObjectMapper mapper;

  static {
    mapper = new ObjectMapper ();
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.registerModule(new JavaTimeModule());
  }

  public Logger getLogger () {
    return logger;
  }

  @Before
  public void beforeParent () {
    MockitoAnnotations.initMocks ( this );
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


  @SuppressWarnings ( "unchecked" )
  public static <T> T getTargetObject ( Object proxy ) {
    if ( ( AopUtils.isJdkDynamicProxy ( proxy ) ) ) {
      try {
        return (T) getTargetObject ( ( (Advised) proxy ).getTargetSource ().getTarget () );
      } catch ( Exception e ) {
        throw new InternalServerException("Failed to unproxy target.", e);
      }
    }
    return (T) proxy;
  }

  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
