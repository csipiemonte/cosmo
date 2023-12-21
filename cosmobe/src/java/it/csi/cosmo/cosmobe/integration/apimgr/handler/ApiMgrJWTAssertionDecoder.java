/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.apimgr.handler;

import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrJWTAssertion;

public interface ApiMgrJWTAssertionDecoder {

  public ApiMgrJWTAssertion decodeAndVerify(String encoded);

}
