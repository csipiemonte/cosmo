/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.business.service;

import java.util.List;
import it.csi.cosmo.cosmosoap.dto.rest.CommonRemoteData;
import it.csi.cosmo.cosmosoap.dto.rest.ContinueTransaction;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SigillaDocumentoResponse;
import it.csi.cosmo.cosmosoap.dto.rest.StartTransaction;
import it.csi.cosmo.cosmosoap.integration.dto.DosignOutcomeDTO;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.VerifyInfo;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.VerifyInput;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteEndTransactionDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteStartTransactionDto;

/**
 *
 */

public interface DoSignService {
  public static final String DEFAULT_ERROR_STATUS = "KO";

  public static final String DEFAULT_INFO_STATUS = "OK";

  public static final String DEFAULT_ERROR_DESCRIPTION = "Errore generico in fase di firma";

  public static final String OTP_REQUESTED = "OTP richiesto con successo";

  public static final String DOCS_SUCCESSFULLY_SIGNED = "Documenti firmati con successo";

  public static final String DEFAULT_ERROR_RETURN_CODE = "9999";

  public static final String DEFAULT_INFO_RETURN_CODE = "0000";

  public static final int STRONG_SIGNATURE = 1;
  public static final int WEAK_SIGNATURE = 0;
  public static final int VERIFY_CRL = 1;
  public static final int NO_VERIFY_CRL = 0;
  public static final int SCOPE_DIGITAL_SIGNATURE = 1;
  public static final int ERROR_CODE_NOT_PRESENT = 0;

  /**
   * Metodo che richiede un OTP, se l'utente e' configurato per riceverli via SMS
   *
   * @param body informazioni necessarie per la richiesta dell' OTP
   * @return un oggetto che contiene l'esito della richiesta. In caso quest'ultima sia completata
   *         correttamente l'utente ricevera' la OTP via SMS sul numero di telefono associato
   *         all'alias
   */
  DosignOutcomeDTO richiediOTP(RichiediOTPRequest body);

  /**
   * Metodo che firma uno o piu' documenti
   *
   * @param alias dell'utente
   * @param PIN dell'utente
   * @param OTP one-time password ricevuta dall'utente
   * @param documentiDaFirmare uno o piu' documenti che necessitano di firma
   * @return un oggetto che contiene i documenti originali insieme ai corrispettivi firmati
   *         insieme all'esito dell'operazione
   */
  void firma(DoSignFirmaRequest request);

  void firmaMassiva(DoSignFirmaMassivaRequest request);

  List<VerifyInfo> verify(VerifyInput verifyInput);

  List<VerifyInfo> verifyPdf(VerifyInput verifyInput);

  CommonRemoteData executeStartSession(StartTransaction startTransaction, String idTransazione);

  String uanatacaExecuteStartSession(RemoteStartTransactionDto startTransaction, String idTransazione);

  void executeEndSession(ContinueTransaction continueTransaction, String idTransazione);

  void uanatacaExecuteEndSession(RemoteEndTransactionDto endTransaction, String idTransazione);

  SigillaDocumentoResponse apponiSigillo(DoSignSigilloRequest request);

}
