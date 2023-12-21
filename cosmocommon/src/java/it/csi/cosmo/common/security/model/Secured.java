/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotazione utilizzata per specificare le autorizzazioni necessarie all'accesso ai metodi delle
 * risorse RestEasy.
 *
 * puo' essere utilizzata per richiedere: - una singola autorizzazione - piu' autorizzazioni allo
 * stesso tempo - almeno una di una lista di autorizzazioni
 */
@Documented
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
public @interface Secured {

  /**
   * @return valore da specificare per permettere l'accesso a chiunque, ma ricordandosi di
   *         modificare in seguito
   */
  boolean notSecured() default false;

  /**
   * @return valore da specificare quando si vuole permettere l'accesso solo in ambienti di test
   */
  boolean testOnly() default false;

  /**
   * @return valore da specificare quando si vuole permettere l'accesso in ogni caso, es: Secured(
   *         permitAll = true )
   */
  boolean permitAll() default false;

  /**
   * @return valore da specificare quando si vuole negare l'accesso in ogni caso, es: Secured(
   *         denyAll = true )
   */
  boolean denyAll() default false;

  /**
   * @return valore da specificare quando vengono richieste contemporaneamente delle authorities
   *         multiple, es: Secured( value = { Authorities.LOGIN, Authorities.ADMIN } )
   *
   *         accetta anche la rappresentazione breve per authority singole, es: Secured(
   *         Authorities.LOGIN )
   */
  String[] value() default {};

  /**
   * @return valore da specificare quando viene richiesta almeno una fra un set di authorities, es:
   *         Secured( hasAnyAuthority = { Authorities.LOGIN, Authorities.ADMIN } )
   */
  String[] hasAnyAuthority() default {};

  String[] hasScopes() default {};

  String[] hasAnyScope() default {};

  boolean authenticated() default false;

  boolean guest() default false;

  boolean authenticatedClient() default false;

  boolean guestClient() default false;

  boolean profiled() default false;
}
