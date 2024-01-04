/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.converter.IndexValueConverter;


@Documented
@Retention ( value = java.lang.annotation.RetentionPolicy.RUNTIME )
@Target ( value = { java.lang.annotation.ElementType.FIELD } )
public @interface IndexProperty {

  boolean required() default false;

  String value () default "";

  boolean readOnly () default false;

  Class<? extends IndexValueConverter<?>>[] converter() default {};
}
