/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.cripto;

/**
 * Classe astratta con i messaggi di errore per la criptazione.
 */
public abstract class ErrorMessages {

  private ErrorMessages() {
    // private constructor
  }

  public static final String FILE_NON_TROVATO = "File di configurazione non trovato";

  public static final String CRYPT_ERRORE_DURANTE_DECRIPTAZIONE =
      "Errore durante l'operazione di decrypt";

  public static final String CRYPT_ERRORE_DURANTE_CRIPTAZIONE =
      "Errore durante l'operazione di encrypt";

  public static final String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_EN =
      "Errore durante l'inizializzazione del cipher di encryption";

  public static final String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_DEC =
      "Errore durante l'inizializzazione del cipher di decryption";

  public static final String CRYPT_PASSPHRASE_NON_TROVATA_NELLE_PROPERTIES_ESTERNE =
      "passphrase non trovata nel file di properties esterno";

  public static final String CRYPT_IVPARAMETER_NON_TROVATO_NELLE_PROPERTIES_ESTERNE =
      "ivparameter non trovato nel file di properties esterno";
}
