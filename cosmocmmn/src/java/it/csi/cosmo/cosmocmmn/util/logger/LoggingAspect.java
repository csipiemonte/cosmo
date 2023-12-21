/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.util.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.components.CosmoLoggingAspect;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmocmmn.config.Constants;

@Component
@Aspect
public class LoggingAspect extends CosmoLoggingAspect {

  public LoggingAspect() {
    super(LoggerConstants.ROOT_LOG_CATEGORY, LogCategory.BUSINESS_LOG_CATEGORY.getCategory());
  }

  @Around(value = "execution(* it.csi." + Constants.PRODUCT + "." + Constants.COMPONENT_NAME
      + ".business.service.impl..*.*(..))", argNames = "joinPoint")
  public Object aroundBusinessImplCall(ProceedingJoinPoint joinPoint) throws Throwable {
    return this.logAroundBusinessImplCall(joinPoint);
  }


  @Override
  protected String getIdentationStringForCurrentRequestLogging() {

    return RequestUtils.getCurrentRequestId().map(ray -> "[RAY " + ray + "] ").orElse("[NO-REQ] ");
  }

}
