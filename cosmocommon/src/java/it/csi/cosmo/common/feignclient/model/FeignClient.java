/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.common.feignclient.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 */
@Documented
@Retention ( value = java.lang.annotation.RetentionPolicy.RUNTIME )
@Target ( value = { java.lang.annotation.ElementType.TYPE } )
public @interface FeignClient {

  String value () default "";

  Class<? extends FeignClientContextConfigurator>[] configurator() default {};

  Class<? extends FeignClientBeforeRequestInterceptor>[] interceptors() default {};

}
