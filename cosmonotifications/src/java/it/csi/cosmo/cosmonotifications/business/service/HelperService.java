/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service;

import java.util.List;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.DecodificaHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperImportResult;
import it.csi.cosmo.cosmonotifications.dto.rest.HelperResponse;




/**
 * Servizio per la gestione degli helper
 */
public interface HelperService {

  /**
   * Restituisce una lista di helper, a seconda dei filtri impostati
   *
   * @return List<Helper> lista di helper
   */
  HelperResponse getHelpers(String filter);

  void deleteHelper(String id);

  Helper getHelper(String id);

  Helper postHelper(Helper body);

  Helper putHelper(String id, Helper body);

  /**
   * Restituisce la lista dei codici pagina in corso di validita'
   *
   * @return List<CodicePagina> lista dei codici pagina
   */
  List<CodicePagina> getCodiciPagina();

  /**
   * Restituisce la lista dei codici tab in corso di validita' a partire dal codice pagina
   *
   * @return List<CodiceTab> lista dei codici tab
   */
  List<CodiceTab> getCodiciTab(String codicePagina);

  DecodificaHelper getDecodifica(String pagina, String tab, String form);

  List<CodiceModale> getModali(String filter);

  /*
   * Restituisce un oggetto contenente un json composto dalle informazioni relative all'helper
   * selezionato
   *
   * @param id
   *
   * @return byte[]
   */
  byte[] getExport(String id);

  HelperImportResult postImport(HelperImportRequest input);

}
