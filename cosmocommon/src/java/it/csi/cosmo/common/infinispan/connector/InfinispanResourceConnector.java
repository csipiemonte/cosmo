/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.infinispan.connector;

import java.util.Map;


/**
 * Servizio per l'injection delle risorse via Spring IOC
 *
 */
public interface InfinispanResourceConnector extends AutoCloseable {

  Map<String, Object> getCache ();

}
