/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.acta;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.cosmosoap.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaServiceConnectionException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ClassificazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoSempliceActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FascicoloRealeAnnualeActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ProtocolloActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.RegistrazioneClassificazioniActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieFascicoliActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieTipologicaDocumenti;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.TitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VoceTitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VolumeSerieTipologicaDocumenti;
import it.doqui.acta.acaris.common.EnumQueryOperator;


public interface ActaClient extends AutoCloseable {

  /**
   * Esegue un test del layer.</br>
   *
   * @return True se il layer e' operativo.
   * @throws ActaServiceConnectionException In caso di errore.
   */
  boolean testResouces() throws ActaServiceConnectionException;

  ActaClientContext getContesto();

  <T extends EntitaActa> T find(String id, Class<T> targetClass);

  <T extends EntitaActa> T findBy(String property, String value, Class<T> targetClass);

  <T extends EntitaActa> T findBy(String property, EnumQueryOperator operator, String value,
      Class<T> targetClass);

  <T extends EntitaActa> T create(String parentId, T entity);

  <T extends EntitaActa> T update(T entity);

  VoceTitolarioActa findVoceTitolarioByClassificazioneEstesa(String classificazione);

  SerieFascicoliActa findSerieFascicoliByCodice(String codice);

  FascicoloRealeAnnualeActa findFascicoloRealeAnnualeById(String id);

  SerieTipologicaDocumenti findSerieTipologicaDocumentiByCodice(String codice);

  VolumeSerieTipologicaDocumenti findVolumeSerieTipologicaDocumentiByParoleChiave(
      String paroleChiave);

  Page<DocumentoSempliceActa> findDocumentiSemplici(
      FiltroRicercaDocumentiActaDTO filtri, Pageable pageable);

  Page<RegistrazioneClassificazioniActa> findRegistrazioni(FiltroRicercaDocumentiActaDTO filtri,
      Pageable pageable);

  List<DocumentoSempliceActa> findDocumentiSempliciByParolaChiave(String paroleChiave, int maxItems,
      int skipCount);

  List<DocumentoFisicoActa> findDocumentiFisiciByIdDocumentoSemplice(String id);

  List<DocumentoSempliceActa> findDocumentiSempliciByIdClassificazione(String id);

  ContenutoDocumentoFisicoActa findContenutoPrimarioByIdDocumentoFisico(String id);

  List<IdentitaActa> findIdentitaByCodiceFiscaleUtente(String codiceFiscale);

  List<ClassificazioneActa> findClassificazioniByIdDocumentoSemplice(String id);

  ProtocolloActa findProtocolloById(String id);

  String findObjectIDStrutturaAggregativa(String indiceClassificazioneEstesa);

  TitolarioActa findTitolario(String codice);

  Page<VoceTitolarioActa> findVociTitolarioInAlberatura(String chiaveTitolario, String chiavePadre, Pageable pageable);

  /**
   * @return
   */
  ActaProvider getProvider();
}
