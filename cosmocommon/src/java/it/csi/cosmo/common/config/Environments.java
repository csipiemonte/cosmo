/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.config;

/**
 * Enumerazione con tutti gli ambienti condfigurati
 */

public enum Environments {
  //@formatter:off
  LOCAL("local"),
  DOCKER("docker"),
  DEV_ENG("dev-eng"),
  DEV("dev-int-01"),
  TEST("tst-int-01"),
  TEST_RP("tst-rp-01"),
  EXP("exp-int-01"),
  PROD("prod-int-01"),
  PROD_PVTO("prod-pvto-01"),
  PROD_COTO("prod-coto-01"),
  PROD_CSI("prod-csi-01"),
  PROD_RP("prod-rp-01"),
  PROD_RPCR("prod-rpcr-01");


  //@formatter:on

  private String codice;

  public String getCodice() {
    return codice;
  }

  private Environments(String codice) {
    this.codice = codice;
  }

  public static Environments fromCodice(String codice) {
    for (Environments candidate : Environments.values()) {
      if (candidate.getCodice().equals(codice)) {
        return candidate;
      }
    }
    return null;
  }

}
