/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.business.service;


/**
 *
 */

public interface CryptoService {

  String encrypt ( String value );

  String decrypt ( String value );

}
