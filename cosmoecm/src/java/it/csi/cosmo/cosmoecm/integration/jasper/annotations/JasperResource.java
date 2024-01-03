/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import it.csi.cosmo.cosmoecm.integration.jasper.model.JasperResourceType;


@Documented
@Retention ( value = java.lang.annotation.RetentionPolicy.RUNTIME )
@Target ( value = { java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD } )
public @interface JasperResource {

    String key () default "";

    String value () default "";

    JasperResourceType type () default JasperResourceType.IMAGE;

}
