/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import org.aspectj.lang.ProceedingJoinPoint;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.util.performance.StopWatch;


public abstract class CosmoLoggingAspect {

  private StopWatch watcher;

  private CosmoLogger logBusiness;

  private CosmoLogger loggerBusinessInput;
  private CosmoLogger loggerBusinessOutput;
  private CosmoLogger loggerBusinessTiming;
  private CosmoLogger loggerBusinessError;

  public CosmoLoggingAspect(String stopwatchCategory, String loggingCategory) {
    watcher = new StopWatch(stopwatchCategory);

    logBusiness = new CosmoLogger(loggingCategory);

    loggerBusinessInput = new CosmoLogger(loggingCategory + ".input");
    loggerBusinessOutput = new CosmoLogger(loggingCategory + ".output");
    loggerBusinessTiming = new CosmoLogger(loggingCategory + ".timing");
    loggerBusinessError = new CosmoLogger(loggingCategory + ".error");
  }

  protected Object logAroundBusinessImplCall(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      // avvia il timer prima dell'operazione da monitorare
      watcher.start();
      logBusiness.beginForClass(joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName());
      if (logBusiness.isDebugEnabled()) {
        return logInputOutput(joinPoint);
      } else {
        return joinPoint.proceed();
      }
    } finally {
      logBusiness.endForClass(joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName());
      // arresta il timer
      watcher.stop();
      // inserisce il log del tempo trascorso
      watcher.dumpElapsed(joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName(), "business", "");
    }
  }

  protected Object logInputOutput(ProceedingJoinPoint joinPoint) throws Throwable {

    if (RequestUtils.getCurrentRequest().isEmpty()) {
      return joinPoint.proceed();
    }

    String separator = "======================================================";
    String rayPrefix = getIdentationStringForCurrentRequestLogging();
    Object result = null;
    Throwable threw = null;
    Long startTime = null;

    String signatureName = ObjectUtils.getTargetObject(joinPoint.getTarget()).getSimpleName() + "."
        + joinPoint.getSignature().getName();
    StringBuilder methodLog = new StringBuilder();

    methodLog.append(rayPrefix).append(signatureName);

    if (loggerBusinessInput.isDebugEnabled()) {
      writeInput(joinPoint, rayPrefix, signatureName, methodLog);
    }

    if (loggerBusinessTiming.isDebugEnabled()) {
      startTime = System.currentTimeMillis();
    }

    try {
      result = joinPoint.proceed();
    } catch (Throwable e) {
      threw = e;
    }

    if (loggerBusinessTiming.isDebugEnabled()) {

      methodLog.append("\n").append(rayPrefix).append(signatureName).append(" DURATION = ")
      .append((System.currentTimeMillis() - startTime)).append(" ms");
    }

    if (threw != null) {
      if (loggerBusinessError.isDebugEnabled()) {
        methodLog.append("\n").append(rayPrefix).append(signatureName).append(" THREW EXCEPTION: ")
        .append(threw.getClass().getName()).append(" - ").append(threw.getMessage());
      }
    } else {
      if (loggerBusinessOutput.isDebugEnabled()) {
        methodLog.append("\n").append(rayPrefix).append(signatureName).append(" OUTPUT = ")
        .append(ObjectUtils.represent(result));
      }
    }

    logBusiness.debugForClass(joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        "\n" + rayPrefix + separator + "\n" + methodLog.toString() + "\n" + rayPrefix + separator);

    if (threw != null) {
      logAndRethrow(joinPoint, rayPrefix, signatureName, threw);
    }

    return result;
  }

  protected static void writeInput(ProceedingJoinPoint joinPoint, String rayPrefix,
      String signatureName, StringBuilder methodLog) {
    methodLog.append("\n").append(rayPrefix).append(signatureName).append(" INPUT = [");

    StringBuilder logStr = new StringBuilder("");
    Object[] args = joinPoint.getArgs();
    for (int i = 0; i < args.length; i++) {
      if (args[i] != null) {
        logStr.append(ObjectUtils.represent(args[i])).append(", ");
      } else {
        logStr.append("null, ");
      }
    }
    if (args.length > 0) {
      logStr = new StringBuilder(logStr.substring(0, logStr.length() - 2));
    }
    logStr.append("]");

    methodLog.append(logStr.toString());
  }

  protected void logAndRethrow(ProceedingJoinPoint joinPoint, String rayPrefix,
      String signatureName, Throwable threw) throws Throwable { // NOSONAR

    if (loggerBusinessError.isDebugEnabled()) {
      logBusiness.errorForClass(joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName(), rayPrefix + "%s THREW EXCEPTION in " + signatureName,
          new Exception(threw));
    }

    // RETHROW EXCEPTION
    throw threw;
  }

  protected String getIdentationStringForCurrentRequestLogging() {

    return RequestUtils.getCurrentRequestId().map(ray -> "[RAY " + ray + "] ").orElse("[NO-REQ] ");
  }

}
