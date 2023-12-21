/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistemaResponse;


public interface ParametriDiSistemaService {

	/**
	 * Metodo che restituisce una lista di parametri di sistema attivi. Questa lista
	 * ha un numero di elementi pari al valore indicato da filter o, se filter non
	 * e' valorizzato, un numero di elementi pari al valore definito nella tabella
	 * di configurazione
	 *
	 * @param filter indica i filtri da utilizzare nella ricerca dei parametri di sistema.
	 * @return una lista di parametri di sistema attivi.
	 */
	ParametroDiSistemaResponse getParamtriDiSistema(String filter);

	/**
	 * Metodo per disabilitare un parametro di sistema dal database
	 *
	 * @param chiave e' l'identificativo del parametro di sistema da disabilitare.
	 */
	ParametroDiSistema deleteParametroDiSistemaByChiave(String chiave);

	/**
	 * Metodo che restituisce un parametro di sistema specifico
	 *
	 * @param chiave del parametro di sistema che deve essere cercato nel database
	 * @return parametro di sistema presente nel database
	 */
	ParametroDiSistema getParamtroDiSistemaByChiave(String chiave);

	/**
	 * Metodo che inserisce un parametro di sistema sul database
	 *
	 * @param parametroDiSistema e' il parametro di sistema che deve essere inserito sul database
	 * @return il parametroDiSistema inserito sul database
	 */
	ParametroDiSistema postParametroDiSistema(CreaParametroDiSistemaRequest parametroDiSistema);

	/**
	 * Metodo che aggiorna un parametro di sistema gia' esistente nel database
	 *
	 * @param parametroDiSistema e' il parametro di sistema che deve essere aggiornato
	 * @return il parametro di sistema aggiornato nel database
	 */
	ParametroDiSistema putParametroDiSistemaByChiave(String chiave,
			AggiornaParametroDiSistemaRequest parametroDiSistema);



}
