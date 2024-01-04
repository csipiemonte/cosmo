/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

/**
 *
 */
public enum IndexSignatureVerificationError {
  //@formatter:off
  IGNOTO(0),
  VERIFICA_CONFORMITA_E_INTEGRITA_BUSTA_CRITTOGRAFICA(1),
  SBUSTAMENTO_BUSTA_CRITTOGRAFICA(2),
  VERIFICA_CONSISTENZA_FIRMA(3),
  VERIFICA_VALIDITA_CERTIFICATO(4),
  VERIFICA_CERTIFICATION_AUTHORITY(5),
  VERIFICA_LISTA_DI_REVOCA_CRL_AGGIORNATA_NON_DISPONIBILE(6),
  VERIFICA_LISTA_DI_REVOCA_CERTIFICATO_PRESENTE_NELLA_CRL(7);
  //@formatter:on

  public static IndexSignatureVerificationError byValue(int value) {
    for (IndexSignatureVerificationError candidate : IndexSignatureVerificationError.values()) {
      if (candidate.getValue() == value) {
        return candidate;
      }
    }
    return null;
  }

  private int value;

  private IndexSignatureVerificationError(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
