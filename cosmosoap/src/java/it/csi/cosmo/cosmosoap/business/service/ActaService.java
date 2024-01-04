/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import java.util.List;
import it.csi.cosmo.cosmosoap.dto.acta.ContestoOperativoUtenteActa;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazione;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisicoMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSempliceMap;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaUtente;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioniClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VoceTitolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaClient;

/**
 *
 */

public interface ActaService {

  ActaClient getClient(String idIdentita);

  IdentitaActa findIdentitaDisponibili(ContestoOperativoUtenteActa contesto,
      IdentitaUtente identitaUtente);

  List<IdentitaActa> findIdentitaDisponibili();

  ContenutoDocumentoFisico findContenutoPrimarioByIdDocumentoFisico(String idIdentita,
      String id);

  ContenutoDocumentoFisico findContenutoPrimario(String idIdentita, String idFisico);

  List<DocumentoSemplice> findDocumentiSempliciByParolaChiave(String idIdentita,
      String parolaChiave);

  List<DocumentoSemplice> findDocumentiSempliciByIdClassificazione(String idIdentita,
      String idClassificazione);

  List<DocumentoFisico> findDocumentiFisiciByIdDocumentoSemplice(String idIdentita, String id);

  List<Classificazione> findClassificazioniByIdDocumentoSemplice(String idIdentita, String id);

  Protocollo findProtocolloById(String idIdentita, String id);

  List<DocumentoFisicoMap> findDocumentiFisici(String idIdentita, ImportaDocumentiRequest body);

  List<DocumentoSempliceMap> findDocumentiSemplici(String idIdentita, ImportaDocumentiRequest body);

  RegistrazioniClassificazioni findRegistrazioni(String idIdentita, String filter);

  DocumentiSemplici findDocumentiSemplici(String idIdentita, String filter);

  DocumentiSemplici findDocumentiSempliciPerProtocollo(String idIdentita, String filter);

  String findObjectIdStrutturaAggregativa(String identita, String indiceClassificazioneEsteso);

  Titolario getTitolario(String idIdentita, String codice);

  VociTitolario ricercaAlberaturaVociTitolario(String idIdentita, String chiaveTitolario, String chiavePadre, String filter);

}
