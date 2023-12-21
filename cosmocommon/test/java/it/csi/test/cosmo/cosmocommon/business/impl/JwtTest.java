/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import org.junit.Test;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class JwtTest extends ParentUnitTest {

  @Test
  public void testNull() {

    var token =
        "xxx";

    // var decoder = new CosmoJWTTokenDecoder("xxx", "cosmo.unit");
    // var verified = decoder.decodeAndVerify(token, Object.class);
    //
    // dump(verified);
  }

}
