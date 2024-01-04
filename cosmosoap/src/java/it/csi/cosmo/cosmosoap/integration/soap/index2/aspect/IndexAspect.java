/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention ( value = java.lang.annotation.RetentionPolicy.RUNTIME )
@Target(value = {java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE})
public @interface IndexAspect {

  String value() default "";

  String prefix () default "";

  boolean declared() default false;
}
