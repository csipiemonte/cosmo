/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.converter;

/**
 *
 */

public interface IndexValueConverter<T> {

  T parse(String raw);

  String serialize(T entity);

}
