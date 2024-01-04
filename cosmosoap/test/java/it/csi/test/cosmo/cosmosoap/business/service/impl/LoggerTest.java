/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.business.service.impl;

import org.apache.log4j.Level;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class LoggerTest extends ParentUnitTest {

  @Test
  public void testLogging() {
    final String className = "LoggerTest";
    final String classNameAlt = "LoggerTestALTERNATIVE";
    final String method = "testLogging";
    final String message = "test message p1=%s p2=%s p3=%s";
    final Throwable ex = new InternalServerException("test exception");
    Object[] params = new Object[] {"param1", 42L, new Object()};

    CosmoLogger createdLogger =
        LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, className);

    org.apache.log4j.Logger backLogger = org.apache.log4j.Logger
        .getLogger(LogCategory.BUSINESS_LOG_CATEGORY.getCategory());
    backLogger.setLevel(Level.TRACE);

    createdLogger.beginForClass(classNameAlt, method);
    createdLogger.endForClass(classNameAlt, method);
    createdLogger.debug(method, message);
    createdLogger.debug(method, message, params);
    createdLogger.debugForClass(classNameAlt, method, message);
    createdLogger.debugForClass(classNameAlt, method, message, params);
    createdLogger.error(method, message);
    createdLogger.error(method, message, params);
    createdLogger.error(method, message, ex, params);
    createdLogger.errorForClass(classNameAlt, method, message, ex);
    createdLogger.errorForClass(classNameAlt, method, message, ex, params);
    createdLogger.info(method, message);
    createdLogger.info(method, message, params);
    createdLogger.infoForClass(classNameAlt, method, message);
    createdLogger.infoForClass(classNameAlt, method, message, params);
    createdLogger.trace(method, message);
    createdLogger.trace(method, message, params);
    createdLogger.warn(method, message);
    createdLogger.warn(method, message, params);
    createdLogger.warn(method, message, ex);
    createdLogger.warn(method, message, ex, params);
    createdLogger.warnForClass(classNameAlt, method, message, ex);
    createdLogger.warnForClass(classNameAlt, method, message, ex, params);

  }

}
