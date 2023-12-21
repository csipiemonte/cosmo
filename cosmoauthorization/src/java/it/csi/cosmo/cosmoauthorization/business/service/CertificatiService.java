/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;

/**
 *
 */

public interface CertificatiService {


  /**
   * Metodo che restituisce il certificato di cui si passa l'id
   *
   * @param idCertificato l'id del certificato che deve essere ricercato
   * @return il certificato identificato
   */
  CertificatoFirma getCertificato(String idCertificato);

  /**
   * Metodo che restituisce tutti i certificati di firma dell'utente in sessione
   *
   * @return una lista con tutti i certificati di firma dell'utente passato in argomento
   */
  List<CertificatoFirma> getCertificati();

  /**
   * Metodo per il primo salvataggio di un certificato di firma dell'utente in sessione
   *
   * @param body contiene tutte le informazioni necessarie di un certificato di firma
   * @return il certificato di firma salvato
   */
  CertificatoFirma postCertificato(CertificatoFirma body);


  /**
   * Metodo per l'aggiornamento di un certificato di firma dell'utente in sessione
   *
   * @param idCertificato del certificato da aggiornare
   * @param body contiene tutte le informazioni del certificato di firma da aggiornare
   * @return il certificato di firma aggiornato
   */
  CertificatoFirma putCertificato(String idCertificato, CertificatoFirma body);


  /**
   * Metodo per eliminare logicamente (quindi valorizzando data cancellazione e utente
   * cancellazione) del certificato di firma di cui si passa l'id
   *
   * @param idCertificato e' l'id del certificato che deve essere eliminato
   * @return il certificato di firma eliminato
   */
  CertificatoFirma deleteCertificato(String idCertificato);

  /**
   * Metodo che imposta l'ultimo certificato di firma utilizzato
   *
   * @param idCertificato e' l'id dell'ultimo certificato di firma utilizzato dall'utente
   * @return l'ultimo certificato di firma utilizzato
   */
  CertificatoFirma putUltimoCertificatoUsato(String idCertificato);
}
