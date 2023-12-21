/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.util.concurrent.Callable;
import it.csi.cosmo.common.exception.InternalServerException;

/**
 *
 */

public interface ExceptionUtils {

  public static String exceptionToString(Throwable e) {

    if (e == null) {
      return null;
    }
    StringBuilder output = new StringBuilder();

    //@formatter:off
    output
    .append(e.getClass().getName() + ": " + e.getMessage())
    .append("\n")
    .append(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
    //@formatter:on

    int cap = 10;
    while (e.getCause() != null && cap-- > 0) {
      e = e.getCause();

      //@formatter:off
      output
      .append("\nCAUSED BY: ").append(e.getClass().getName() + ": " + e.getMessage())
      .append("\n")
      .append(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
      //@formatter:on
    }

    return output.toString();
  }

  public static <T> T toChecked(Callable<T> task) {
    try {
      return task.call();
    } catch (Exception e) {
      throw toChecked(e);
    }
  }

  public static void toChecked(Runnable task) {
    try {
      task.run();
    } catch (Exception e) {
      throw toChecked(e);
    }
  }

  public static RuntimeException toChecked(Throwable e) {
    if (e instanceof RuntimeException) {
      throw (RuntimeException) e;
    } else {
      throw new InternalServerException(e.getMessage(), e);
    }
  }

}
