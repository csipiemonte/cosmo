/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security.proxy.model;

import java.util.Collection;

/**
 *
 */

public interface RouteMatcher<T> {

  T antMatchers(String matcher);

  T antMatchers(Collection<String> matcher);

  T antMatchers(String... matcher);

}
