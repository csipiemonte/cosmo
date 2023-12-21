/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.search;

/**
 *
 *
 */
public interface RecursiveFilterSpecification<T> {

  T[] getAnd();

  T[] getOr();

}
